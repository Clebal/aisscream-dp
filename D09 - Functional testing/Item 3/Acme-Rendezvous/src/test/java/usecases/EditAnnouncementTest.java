package usecases;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.DataBinder;

import domain.Announcement;
import security.LoginService;
import services.AnnouncementService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EditAnnouncementTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private AnnouncementService		announcementService;

	
	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 		1. Un usuario trata de editar el campo title de un announcement
	 * 		2. Un usuario trata de editar el campo description de un announcement
	 * 
	 * Realizamos las pruebas sobre la posibilidad del usuario de editar los announcements que ha creado.
	 */
	@Test
	public void driverPositiveTest() {
		final Object testingData[][] = {
			{
				"user1", "announcement1", null, "Test", null, null
			}, {
				"user1", "announcement1", null, null, "test", null
			}
		};
			
	for (int i = 0; i < testingData.length; i++)
		try {
			super.startTransaction();
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		} finally {
			super.rollbackTransaction();
		}
	}
	
	/*
	 * Pruebas:
	 * 		1. Un usuario trata de editar un announcement no creado por �l
	 * 		2. Un manager trata de editar un announcement cuando no lo tiene permitido
	 * 		3. Un administrator trata de editar un announcement cuando no lo tiene permitido
	 * 		4. Un usuario trata de editar el campo moment
	 * 		5. Un usuario trata de editar un announcement dejando el campo title vac�o
	 * 		6. Un usuario trata de editar un announcement dejando el campo descripticon vac�o
	 */
	@Test
	public void driverNegativeTest() {
		final Object testingData[][] = {
			{
				"user1", "announcement4", null, null, null, IllegalArgumentException.class
			}, 	{
				"manager1", "announcement1", null, null, null, IllegalArgumentException.class
			}, {
				"administrator1", "announcement1", null, null, null, IllegalArgumentException.class
			}, {
				"user1", "announcement1", "01/02/2018 15:12", null, null, IllegalArgumentException.class
			}, {
				"user1", "announcement1", null, "", null, ConstraintViolationException.class
			}, {
				"user1", "announcement1", null, null, "", ConstraintViolationException.class
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	/*
	 * Editar un announcement. Pasos:
	 * 1. Autenticar usuario
	 * 2. Listar los announcements
	 * 3. Escoger announcement
	 * 4. Editar el announcement
	 * 5. Salvar el announcement
	 * 6. Dirigir al listado de los announcements
	 */
	protected void template(final String user, final String announcementName, final String moment, final String title, final String description, final Class<?> expected) {
		Class<?> caught;
		Announcement savedAnnouncement, newAnnouncement, oldAnnouncement;
		Collection<Announcement> announcements;
		int announcementId;
		DateFormat formatter; 
		DataBinder binder;
		
		formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		caught = null;
		try {
			
			announcementId = super.getEntityId(announcementName);
			oldAnnouncement = this.announcementService.findOne(announcementId);
			
			// 1. Autenticar usuario
			super.authenticate(user);
			
			// 2. Listar los announcements
			announcements = this.announcementService.findByCreatorUserAccountId(LoginService.getPrincipal().getId(), this.getPage(oldAnnouncement), 5);
			
			// 3. Escoger announcement
			for(Announcement a: announcements)
				if(a.getId() == announcementId) oldAnnouncement = a;
			
			// 4. Editar el announcement
			newAnnouncement = this.copyAnnouncement(oldAnnouncement);
			if(moment != null) newAnnouncement.setMoment(formatter.parse(moment));
			if(title != null) newAnnouncement.setTitle(title);
			if(description != null) newAnnouncement.setDescription(description);
			
			// 5. Salvar el announcement
			binder = new DataBinder(newAnnouncement);
			newAnnouncement = this.announcementService.reconstruct(newAnnouncement, binder.getBindingResult());
			savedAnnouncement = this.announcementService.save(newAnnouncement);
			this.announcementService.flush();
			
			// 6. Dirigir al listado de los announcements
			for(Announcement a: this.announcementService.findAll())
				if(a.getId() == savedAnnouncement.getId()){
					if(title != null) Assert.isTrue(a.getTitle().equals(title)); 
					if(description != null) Assert.isTrue(a.getDescription().equals(description));
				}
			
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		
		super.checkExceptions(expected, caught);
	}
	
	private Announcement copyAnnouncement(final Announcement announcement) {
		Announcement result;
		
		Assert.notNull(announcement);
		
		result = new Announcement();
		result.setId(announcement.getId());
		result.setMoment(announcement.getMoment());
		result.setTitle(announcement.getTitle());
		result.setDescription(announcement.getDescription());
		result.setRendezvous(announcement.getRendezvous());

		return result;
	}
	
	private Integer getPage(final Announcement announcement) {
		Integer result, pageNumber, collectionSize;
		Collection<Announcement> announcements;

		collectionSize = this.announcementService.countByCreatorUserAccountId(LoginService.getPrincipal().getId());
        pageNumber = (int) Math.floor(((collectionSize / (5.0)) - 0.1) + 1);

		result = null;
		for (int i = 0; i <= pageNumber; i++) {
			announcements = this.announcementService.findByCreatorUserAccountId(LoginService.getPrincipal().getId(), i + 1, 5);
			if (!announcements.contains(announcement))
				result = i + 1;
		}

		return result;
	}

}

