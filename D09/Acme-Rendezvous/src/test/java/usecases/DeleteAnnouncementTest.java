
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
import utilities.AbstractTest;
import domain.Announcement;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DeleteAnnouncementTest extends AbstractTest {

	@Autowired
	private AnnouncementService	announcementService;

	/*
	 * 1. El administrador trata de eliminar un announcement
	 * 2. Un usuario autenticado como usuario trata de eliminar un announcement
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
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	/*
	 * 1. Un usuario autenticado como manager trata de eliminar un announcement
	 * 2. Un usuario autenticado como usuario trata de eliminar un announcement que no es suyo
	 */
	@Test
	public void driverNegativeTest() {
		final Object testingData[][] = {
			{
				"manager1", "announcement6", IllegalArgumentException.class
			}, {
				"user1", "announcement5", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	// Ancillary methods ------------------------------------------------------

	/*
	 * Eliminar un announcement. Pasos:
	 * 1. Autenticarme como usuario
	 * 1. Listar los announcements
	 * 2. Escoger un announcement (entrando en la vista de Editar)
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
			
			// 1. Autenticarnos como usuario
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
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
		super.unauthenticate();
		super.checkExceptions(expected, caught);
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

		for (int i = 0; i <= pageNumber; i++) {
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
