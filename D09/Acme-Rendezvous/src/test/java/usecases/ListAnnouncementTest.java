
package usecases;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.AnnouncementService;
import services.UserService;
import utilities.AbstractTest;
import domain.User;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ListAnnouncementTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private AnnouncementService	announcementService;
	
	@Autowired
	private UserService 		userService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * 1. Un usuario logeado entra en una vista que llama al método findByRendezvousId
	 * 2. Un usuario no logeado entra en una vista que llama al método findByRendezvousId
	 * 3.
	 */
	@Test()
	public void testFindByRendezvousId() {
		final Object testingData[][] = {
			{
				"user1", "findByRendezvousId", "rendezvous1", 2, 0, 0, null
			}, {
				null, "findByRendezvousId", "rendezvous1", 2, 0, 0, null
			}, {
				"user1", "findByRendezvousId", "rendezvous1", 0, 1, 5, IllegalArgumentException.class
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Integer) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	/*
	 * 1. Un usuario logeado como USER entra en una vista que llama al método findByCreatorUserAccountId
	 * 2.
	 * 3.
	 */
	@Test
	public void testFindByCreatorUserAccountId() {
		final Object testingData[][] = {
			{
				"user1", "findByCreatorUserAccountId", null, 5, 0, 0, null
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Integer) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	/*
	 * 1. Prueba del método findAll
	 * 2. El administrador entra en la vista que llama al método findAllPaginated
	 * 3. 
	 * 4. Un usuario autenticado como user trata de entrar en una vista que llama al método findAllPaginated
	 * 5. Un usuario autenticado como manager trata de entrar en una vista que llama al método findAllPaginated
	 */
	@Test
	public void testFindAll() {
		final Object testingData[][] = {
			{
				null, "findAll", null, 11, 0, 0, null
			}, {
				"admin", "findAllPaginated", null, 5, 0, 5, null
			}, {
				"admin", "findAllPaginated", null, 1, 3, 5, null
			}, {
				"user1", "findAllPaginated", null, 5, 0, 0, IllegalArgumentException.class
			}, {
				"manager1", "findAllPaginated", null, 5, 0, 0, IllegalArgumentException.class
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Integer) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	
	// Ancillary methods ------------------------------------------------------

	protected void template(final String user, final String method, final String rendezvousName, final Integer tamano, final int page, final int size, final Class<?> expected) {
		Class<?> caught;
		int sizeAnnouncement, userId, userAccountId, rendezvousId;
		User userEntity;

		caught = null;
		try {
			super.authenticate(user);

			if (method.equals("findAll")) {
				sizeAnnouncement = this.announcementService.findAll().size();
			} else if(method.equals("findByCreatorUserAccountId")) {
				Assert.notNull(user);
				userId = super.getEntityId(user);
				Assert.notNull(userId);
				userEntity = this.userService.findOne(userId);
				Assert.notNull(userEntity);
				userAccountId = userEntity.getUserAccount().getId();
				sizeAnnouncement = this.announcementService.findByCreatorUserAccountId(userAccountId, page, size).size();
			}else if(method.equals("findByRendezvousId")) {
				rendezvousId = super.getEntityId(rendezvousName);
				Assert.notNull(rendezvousId);
				sizeAnnouncement = this.announcementService.findByRendezvousId(rendezvousId, page, size).size();
			}else{
				sizeAnnouncement = this.announcementService.findAll(page, size).size();
			}
			
			Assert.isTrue(sizeAnnouncement == tamano); 
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
		super.checkExceptions(expected, caught);
	}
}
