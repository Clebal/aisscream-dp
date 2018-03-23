package usecases;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.LoginService;
import services.AnnouncementService;
import services.RendezvousService;
import utilities.AbstractTest;
import domain.Announcement;
import domain.Rendezvous;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DeleteAnnouncementTest extends AbstractTest {

	@Autowired
	private AnnouncementService	announcementService;
	
	@Autowired
	private RendezvousService rendezvousService;

	/*
	 * Pruebas:
	 * 		1. El administrador trata de eliminar un announcement
	 * 		2. Un usuario autenticado como usuario trata de eliminar un announcement
	 * 
	 * Requisitos:
	 * 		17.1 - An actor who is authenticated as an administrator must be able to 
	 * 			   remove an announcement that he or she thinks is inappropriate.
	 * 		Un usuario también puede borrar los announcement de sus rendezvouses.
	 */
	@Test
	public void driverPostiveTest() {
		final Object testingData[][] = {
			{
				"admin", "announcement6", null
			}, {
				"user1", "announcement1", null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	/*
	 * Pruebas:
	 * 		1. Un usuario autenticado como manager trata de eliminar un announcement
	 * 		2. Un usuario autenticado como usuario trata de eliminar un announcement que no es suyo
	 * 		3. Un usuario trata de hackear el sistema eliminando un announcement que no le pertenece pero le cambia la propiedad rendezvous a otro que sí es suyo
	 * 
	 * Requisitos:
	 * 		17.1 - An actor who is authenticated as an administrator must be able to 
	 * 			   remove an announcement that he or she thinks is inappropriate.
	 * 		Un usuario también puede borrar los announcement de sus rendezvouses.
	 */
	@Test
	public void driverUrlNegativeTest() {
		final Object testingData[][] = {
			{
				"manager1", "announcement6", null, IllegalArgumentException.class
			}, {
				"user1", "announcement5", null, IllegalArgumentException.class
			}, {
				"user1", "announcement5", "rendezvous1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateUrl((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	// Ancillary methods ------------------------------------------------------

	/*
	 * Eliminar un announcement. Pasos:
	 * 1. Autenticar usuario
	 * 1. Listar los announcements
	 * 2. Escoger un announcement (entrar en la vista de Editar)
	 * 3. Eliminar el announcement
	 * 4. Dirigir a la vista de announcemt
	 */
	protected void template(final String user, final String announcementBean, final Class<?> expected) {
		Class<?> caught;
		int announcementId, page;
		Announcement announcement, oldAnnouncement;
		Collection<Announcement> announcements;
 
		caught = null;
		announcement = null;
		try {
			
			announcementId = super.getEntityId(announcementBean);
			oldAnnouncement = this.announcementService.findOne(announcementId);
			
			// 1. Autenticar usuario
			super.authenticate(user); 
			Assert.isTrue(LoginService.getPrincipal().getUsername().equals(user));
			
			// 2. Listar los announcements
			if(user.equals("admin")) {
				page = this.getPage(oldAnnouncement, user);
				Assert.notNull(page);
				announcements = this.announcementService.findAll(page, 5);
			} else
				announcements = this.announcementService.findByCreatorUserAccountId(LoginService.getPrincipal().getId(), this.getPage(oldAnnouncement, user), 5);
			
			// 3. Escoger un announcement
			for(Announcement a: announcements)
				if(a.getId() == announcementId) announcement = a;
			
			// 5. Eliminar el announcement
			this.announcementService.delete(announcement);
			this.announcementService.flush();

			// 6. Dirigir al listado de announcements
			Assert.isTrue(!this.announcementService.findAll().contains(announcement));

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}
	
	/*
	 * Eliminar template a través de URL. Pasos:
	 * 1. Autenticar usuario
	 * 2. Eliminar el announcement
	 */
	protected void templateUrl(final String user, final String announcementBean, final String rendezvousBean, final Class<?> expected) {
		Class<?> caught;
		int announcementId, rendezvousId;
		Announcement announcement;
		Rendezvous rendezvous;
 
		caught = null;
		announcement = null;
		try {
			
			/***********/
			announcementId = super.getEntityId(announcementBean);
			announcement = this.copyAnnouncement(this.announcementService.findOne(announcementId));
			
			// Simulamos que el usuario trata de cambiar las propiedades del announcement al enviarlo por URL
			if(rendezvousBean != null) {
				rendezvousId = super.getEntityId(rendezvousBean);
				rendezvous = this.rendezvousService.findOne(rendezvousId);
				announcement.setRendezvous(rendezvous);
			}
			/***********/
			
			// 1. Autenticarnos como usuario
			super.authenticate(user); 
			Assert.isTrue(LoginService.getPrincipal().getUsername().equals(user));
			
			// 2. Eliminar el announcement
			this.announcementService.delete(announcement);
			this.announcementService.flush();
			
			// Comprobar
			Assert.isTrue(!this.announcementService.findAll().contains(announcement));

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}
	
	private Announcement copyAnnouncement(final Announcement announcement) {
		Announcement result;
		
		Assert.notNull(announcement);
		
		result = new Announcement();
		result.setId(announcement.getId());
		result.setVersion(announcement.getVersion());
		result.setMoment(announcement.getMoment());
		result.setTitle(announcement.getTitle());
		result.setDescription(announcement.getDescription());
		result.setRendezvous(announcement.getRendezvous());
	
		return result;
	}
	
	private Integer getPage(final Announcement announcement, final String user) {
		Integer result, pageNumber, collectionSize;
		Collection<Announcement> announcements;

		if(user.equals("admin"))
			collectionSize = this.announcementService.countAll();
		else
			collectionSize = this.announcementService.countByCreatorUserAccountId(LoginService.getPrincipal().getId());

		pageNumber = (int) Math.floor(((collectionSize / (5 + 0.0)) - 0.1) + 1);

		result = null;

		for (int i = 1; i <= pageNumber; i++) {
			if(user.equals("admin"))
				announcements = this.announcementService.findAll(i, 5);
			else
				announcements = this.announcementService.findByCreatorUserAccountId(LoginService.getPrincipal().getId(), i, 5);
			if (announcements.contains(announcement)){
				result = i;
				break;
			}
		}

		return result;
	}

}
