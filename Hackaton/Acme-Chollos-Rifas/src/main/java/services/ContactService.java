
package services;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Servicio de envío de emails
 */
@Service
@Transactional
public class ContactService {

	private static final Log	log	= LogFactory.getLog(ContactService.class);

	/** wrapper de Spring sobre javax.mail */
	private JavaMailSenderImpl	mailSender;


	public void setMailSender(final JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}


	/** correo electrónico del remitente */
	private String	from;


	public void setFrom(final String from) {
		this.from = from;
	}

	public String getFrom() {
		return this.from;
	}


	/** flag para indicar si está activo el servicio */
	public boolean	active	= true;


	public boolean isActive() {
		return this.active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}


	private static final File[]	NO_ATTACHMENTS	= null;


	public void send(final String to, final String subject, final String text) {

		this.send(to, subject, text, ContactService.NO_ATTACHMENTS);
	}

	public void send(final String to, final String subject, final String text, final File... attachments) {
		// chequeo de parámetros 
		Assert.hasLength(to, "email 'to' needed");
		Assert.hasLength(subject, "email 'subject' needed");
		Assert.hasLength(text, "email 'text' needed");

		// asegurando la trazabilidad
		if (ContactService.log.isDebugEnabled()) {
			final boolean usingPassword = !"".equals(this.mailSender.getPassword());
			ContactService.log.debug("Sending email to: '" + to + "' [through host: '" + this.mailSender.getHost() + ":" + this.mailSender.getPort() + "', username: '" + this.mailSender.getUsername() + "' usingPassword:" + usingPassword + "].");
			ContactService.log.debug("isActive: " + this.active);
		}
		// el servicio esta activo?
		if (!this.active)
			return;

		// plantilla para el envío de email
		final MimeMessage message = this.mailSender.createMimeMessage();

		try {
			// el flag a true indica que va a ser multipart
			final MimeMessageHelper helper = new MimeMessageHelper(message, true);

			// settings de los parámetros del envío
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setFrom(this.getFrom());
			helper.setText(text);

			// adjuntando los ficheros
			if (attachments != null)
				for (int i = 0; i < attachments.length; i++) {
					final FileSystemResource file = new FileSystemResource(attachments[i]);
					helper.addAttachment(attachments[i].getName(), file);
					if (ContactService.log.isDebugEnabled())
						ContactService.log.debug("File '" + file + "' attached.");
				}

		} catch (final MessagingException e) {
			new RuntimeException(e);
		}
		System.out.println("Envio");
		// el envío

		try {
			this.mailSender.send(message);
		} catch (final Exception e) {
			System.out.println(e);
		}
		System.out.println("Enviado");
	}

}
