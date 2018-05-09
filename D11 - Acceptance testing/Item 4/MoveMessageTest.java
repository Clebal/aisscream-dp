package usecases;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Folder;
import domain.Message;
import services.FolderService;
import services.MessageService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MoveMessageTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private MessageService		messageService;
	
	@Autowired
	private FolderService		folderService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 		1. Un actor autenticado como USER envia un mensaje con prioridad LOW
	 * 		2. Un actor autenticado como USER envia un mensaje con prioridad NEUTRAL
	 * 		3. Un actor autenticado como USER envia un mensaje con prioridad HIGH
	 * 		4. Un actor autenticado como ADMIN difunde un mensaje
	 *
	 *
	 * Requisitos:
	 * 		13.1 An actor who is authenticated must be able to exchange messages with 
	 * 				other actors and manage them, which includes deleting and
	 *				moving them from one folder to another folder.
	 * 
	 */
	@Test
	public void driverPositiveTest() {		
		final Object testingData[][] = {
			{
				"customer1", "message1", "folder3c1", null
			}, {
				"user2", "message3", "folder2u2", null
			}, {
				"administrator", "message2", "folder3ad1", null
			}, {
				"agent2", "message4", "folder3a2", null
			}
		};
			
	for (int i = 0; i < testingData.length; i++)
		try {
			super.startTransaction();
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		} finally {
			super.rollbackTransaction();
		}
	}
	
	/*
	 * Pruebas:
	 * 		1. Un usuario trata de mover un mensaje que no es suyo
	 * 		2. Un usuario trata de mover un mensaje a una carpeta que no es suya
	 * 
	 * Requisitos:
	 * 		13.1 An actor who is authenticated must be able to exchange messages with 
	 * 				other actors and manage them, which includes deleting and
	 *				moving them from one folder to another folder.
	 */
	@Test
	public void driverNegativeTest() {		
		final Object testingData[][] = {
				{
					"customer1", "message3", "folder3c1", IllegalArgumentException.class
				}, {
					"user2", "message3", "folder2u1", IllegalArgumentException.class
				}
			};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	/*
	 * Enviar/Difundir mensaje
	 * Pasos:
	 * 		1. Autenticar como usuario o admin
	 * 		2. Enviar/difundir mensaje
	 * 		3. Volver al listado de mensajes
	 */
	protected void template(final String actorBean, final String messageBean, final String folderBean, final Class<?> expected) {
		Class<?> caught;
		Message message;
		Integer folderId, messageId;
		Folder folder;

		caught = null;
		try {
			
			Assert.notNull(messageBean);
			messageId = super.getEntityId(messageBean);
			Assert.notNull(messageId);
			
			message = this.messageService.findOne(messageId);
			Assert.notNull(message);
			
			Assert.notNull(folderBean);
			folderId = super.getEntityId(folderBean);
			Assert.notNull(folderId);
			
			folder = this.folderService.findOne(folderId);
			Assert.notNull(folder);
			
			// 1. Autenticar como usuario o admin
			if(actorBean.equals("administrator"))
				super.authenticate("admin");
			else
				super.authenticate(actorBean);
			/*if(!actorBean.equals("admin")) {
				user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
				Assert.notNull(user);
			}*/
			
			// 2. Mover mensaje
			this.messageService.moveMessage(message, message.getFolder(), folder);
			
			this.messageService.flush();
			
			// 3. Volver al listado de mensajes
			// Comprobar que el mensaje ha cambiado de carpeta
			Assert.isTrue(this.messageService.findOne(messageId).getFolder().equals(folder));
			
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		
		super.checkExceptions(expected, caught);
	}

}

