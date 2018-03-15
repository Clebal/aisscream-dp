
package usecases;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.UserService;
import utilities.AbstractTest;
import domain.User;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ListUserTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private UserService	userService;


	// Tests ------------------------------------------------------------------

	/*
	 * 1. Probamos obtener el resultado previsto para el método findAll logueados como user1
	 * 	2. Probamos obtener el resultado previsto para el método findAll sin loguear
	 * 3. Probamos obtener el resultado previsto para el método findAll logueados como manager2
	 */
	@Test()
	public void testFindAll() {
		final Object testingData[][] = {
			{
				"user1", "findAll", null, 6, 0, 0, null
			}, {
				null, "findAll", null, 6, 0, 0, null
			}, {
				"manager2", "findAll", null, 6, 0, 0, null
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
	 * 1. Probamos obtener el resultado previsto para el método findAllPaginated logueados como user1, para la página 1 y el tamaño 5
	 * 	2. Probamos obtener el resultado previsto para el método findAllPaginated sin loguear, para la página 2 y el tamaño 4
	 * 3. Probamos obtener el resultado previsto para el método findAllPaginated logueados como user2, para la página 2 y el tamaño 3
	 * 4. Probamos no poder obtener el resultado previsto para el método findAllPaginated logueados como un manager
	 * 5. Probamos no poder obtener el resultado previsto para el método findAllPaginated logueados como un admin
	 */
	@Test()
	public void testFindAllPaginated() {
		final Object testingData[][] = {
				{
					"user1", "findAllPaginated", null, 5, 1, 5, null
				}, {
					null, "findAllPaginated", null, 2, 2, 4, null
				}, {
					"user2", "findAllPaginated", null, 2, 3, 2, null
				}, {
					"manager2", "findAllPaginated", null, 5, 1, 5, IllegalArgumentException.class
				}, {
					"admin", "findAllPaginated", null, 5, 1, 5, IllegalArgumentException.class
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
	 * 1. Probamos obtener el resultado previsto para el método findAttendantsPaginated logueados como user1, para el rendezvous1 y con la página 1 y el tamaño 10
	 * 2. Probamos obtener el resultado previsto para el método findAttendantsPaginated sin loguear, para el rendezvous2 y con la página 2 y el tamaño 4
	 * 3. Probamos obtener el resultado previsto para el método findAttendantsPaginated logueados como user2, para el rendezvous4 y con la página 1 y el tamaño 2
	 * 4. Probamos no poder obtener el resultado previsto para el método findAttendantsPaginated logueados como manager
	 * 5. Probamos no poder obtener el resultado previsto para el método findAttendantsPaginated logueados como admin
	 * 	6. Probamos no poder obtener el resultado previsto para el método findAttendantsPaginated logueados como user2 y el rendezvous a null
	 */
	@Test()
	public void testFindAttendantsPaginated() {
		final Object testingData[][] = {
				{
					"user1", "findAttendantsPaginated", "rendezvous1", 3, 1, 10, null
				}, {
					null, "findAttendantsPaginated", "rendezvous2", 0, 2, 4, null
				}, {
					"user2", "findAttendantsPaginated", "rendezvous4", 1, 1, 2, null
				}, {
					"manager2", "findAttendantsPaginated", "rendezvous3", 5, 1, 5, IllegalArgumentException.class
				}, {
					"admin", "findAttendantsPaginated", "rendezvous5", 5, 1, 5, IllegalArgumentException.class
				}, {
					"user2", "findAttendantsPaginated", null, 2, 1, 5, IllegalArgumentException.class
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

	/*
	 * An actor who is not authenticated must be able to list the users of 
	 * the system and navigate to their profiles, which include personal data 
	 * and the list of rendezvouses that they’ve attended or are going to attend.
	 */
	protected void template(final String user, final String method, final String rendezvous, final Integer tamano, final int page, final int size, final Class<?> expected) {
		Class<?> caught;
		Collection<User> users;
		int rendezvousId;

		caught = null;
		users = null;
		try {
			super.authenticate(user);

			if (method.equals("findAll")) {
				users = this.userService.findAll();
			} else if (method.equals("findAllPaginated")) {
				users = this.userService.findAllPaginated(page, size);
			} else {
				Assert.notNull(rendezvous);
				rendezvousId = super.getEntityId(rendezvous);
				users = this.userService.findAttendantsPaginated(page, size, rendezvousId);
				System.out.println(users.size());
			}
			Assert.isTrue(users.size() == tamano);
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
		super.checkExceptions(expected, caught);
	}
}
