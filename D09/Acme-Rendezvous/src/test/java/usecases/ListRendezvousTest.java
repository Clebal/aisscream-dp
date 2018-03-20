
package usecases;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.CategoryService;
import services.RendezvousService;
import services.UserService;
import utilities.AbstractTest;
import domain.Category;
import domain.Rendezvous;
import domain.User;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ListRendezvousTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private UserService			userService;

	@Autowired
	private CategoryService		categoryService;


	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas
	 * 1.Hacemos el findAll logeados como user1 (no salta excepcion)
	 * 2.Hacemos el findAll logeados como manager1 (no salta excepción)
	 * 3.Hacemos el findAll logeados como admin (no salta excepción)
	 */
	@Test()
	public void testFindAll() {
		final Object testingData[][] = {
			{
				"user", "user1", "findAll", false, null, null, 10, null, null, null
			}, {
				"manager", "manager1", "findAll", false, null, null, 10, null, null, null
			}, {
				"admin", "admin", "findAll", false, null, null, 10, null, null, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	/*
	 * Pruebas
	 * 1.Hacemos el findByCreatorId logeados como user1 mirando la primera página con tamaño 5 (no salta excepción)
	 * 2.Hacemos el findByCreatorId logeados como user1 mirando la segunda página con tamaño 5 (no salta excepción)
	 * 3.Hacemos el findByCreatorId logeados como user1 mirando la tercera página con tamaño 5 (no salta excepción)
	 * 4.Hacemos el findByCreatorId logeados como user1 mirando la primera página con tamaño 8 (no salta excepción)
	 * 5.Hacemos el findByCreatorId logeados como user1 mirando la segunda página con tamaño 8 (no salta excepción)
	 * 6.Hacemos el findByCreatorId logeados como user1 mirando la primera página con tamaño 7 (no salta excepción)
	 * 7.Hacemos el findByCreatorId del user1 logeados como manager1, mirando la segunda página con tamaño 5 (no salta excepción)
	 * 8.Hacemos el findByCreatorId del user1 logeados como admin, mirando la segunda página con tamaño 5 (no salta excepción)
	 * 9.Hacemos el findByCreatorId logeados como user3 mirando la segunda página con tamaño 5 (no salta excepción)
	 * 10.Hacemos el findByCreatorId del user4 logeados como user3 mirando la segunda página con tamaño 5 (no salta excepción)
	 * 
	 * Requisitos
	 * C.4.3:. List the rendezvouses in the system and navigate to the profiles of the corresponding creators and attendants
	 * B.14: Such rendezvouses must not be displayed unless the user who is browsing them is at least 18 year old. Obviously, they must not be shown to unauthenticated users.
	 */
	@Test()
	public void positiveFindByCreatorIdTest() {
		final Object testingData[][] = {
			{
				"user", "user1", "findByCreatorId", false, "user1", 1, 5, 5, 2, null
			}, {
				"user", "user1", "findByCreatorId", false, "user1", 2, 2, 5, 2, null
			}, {
				"user", "user1", "findByCreatorId", false, "user1", 3, 0, 5, 2, null
			}, {
				"user", "user1", "findByCreatorId", false, "user1", 1, 7, 8, 1, null
			}, {
				"user", "user1", "findByCreatorId", false, "user1", 2, 0, 8, 1, null
			}, {
				"user", "user1", "findByCreatorId", false, "user1", 1, 7, 7, 1, null
			}, {
				"manager", "manager1", "findByCreatorId", false, "user1", 2, 2, 5, 2, null
			}, {
				"admin", "admin", "findByCreatorId", false, "user1", 2, 2, 5, 2, null
			}, {
				"user", "user3", "findByCreatorId", false, "user3", 1, 2, 5, 1, null
			}, {
				"user", "user3", "findByCreatorId", false, "user4", 1, 1, 5, 1, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findByCreatorId logeados como user4 que es menor de edad de los rendezvouses del user1 mirando la primera página con tamaño 5 (salta un IllegalArgumentEception)
	 * 2.Hacemos el findByCreatorId sin estar logeados de los rendezvouses del user1 mirando la segunda página con tamaño 5 (salta un IllegalArgumentEception)
	 * 3.Hacemos el findByCreatorId logeados como user1 de un usuario con id 0 (salta un IllegalArgumentEception)
	 */
	@Test()
	public void negativeFindByCreatorIdTest() {
		final Object testingData[][] = {
			{
				"user", "user4", "findByCreatorId", false, "user1", 1, 5, 5, 2, IllegalArgumentException.class
			}, {
				null, null, "findByCreatorId", false, "user1", 2, 2, 5, 2, IllegalArgumentException.class
			}, {
				"user", "user1", "findByCreatorId", true, "user1", 2, 2, 5, 2, IllegalArgumentException.class
			},
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findByCreatorIdAllPublics del user1 logeados como user4 mirando la primera página con tamaño 5 (no salta excepción)
	 * 2.Hacemos el findByCreatorIdAllPublics del user1 logeados como user4 mirando la segunda página con tamaño 5 (no salta excepción)
	 * 3.Hacemos el findByCreatorIdAllPublics del user1 logeados como user4 mirando la tercera página con tamaño 5 (no salta excepción)
	 * 4.Hacemos el findByCreatorIdAllPublics del user1 logeados como user4 mirando la primera página con tamaño 8 (no salta excepción)
	 * 5.Hacemos el findByCreatorIdAllPublics del user1 logeados como user4 mirando la segunda página con tamaño 8 (no salta excepción)
	 * 6.Hacemos el findByCreatorIdAllPublics del user1 logeados como user4 mirando la primera página con tamaño 5 (no salta excepción)
	 * 7.Hacemos el findByCreatorId del user1 logeados como manager1, mirando la segunda página con tamaño 6 (no salta excepción)
	 * 8.Hacemos el findByCreatorIdAllPublics del user1 logeados como manager1 mirando la primera página con tamaño 5 (no salta excepción)
	 * 9.Hacemos el findByCreatorIdAllPublics del user1 logeados como admin mirando la primera página con tamaño 5 (no salta excepción)
	 * 10.Hacemos el findByCreatorIdAllPublics del user4 logeados como user3 mirando la primera página con tamaño 5 (no salta excepción)
	 * 11.Hacemos el findByCreatorIdAllPublics del user1 sin estar logeado mirando la primera página con tamaño 5 (no salta excepción)
	 * 
	 * Requisitos
	 * C.4.3:. List the rendezvouses in the system and navigate to the profiles of the corresponding creators and attendants
	 */
	@Test()
	public void positiveFindByCreatorIdAllPublicsTest() {
		final Object testingData[][] = {
			{
				"user", "user4", "findByCreatorIdAllPublics", false, "user1", 1, 5, 5, 2, null
			}, {
				"user", "user4", "findByCreatorIdAllPublics", false, "user1", 2, 1, 5, 2, null
			}, {
				"user", "user4", "findByCreatorIdAllPublics", false, "user1", 3, 0, 5, 2, null
			}, {
				"user", "user4", "findByCreatorIdAllPublics", false, "user1", 1, 6, 8, 1, null
			}, {
				"user", "user4", "findByCreatorIdAllPublics", false, "user1", 2, 0, 8, 1, null
			}, {
				"user", "user4", "findByCreatorIdAllPublics", false, "user1", 1, 6, 6, 1, null
			}, {
				"manager", "manager1", "findByCreatorIdAllPublics", false, "user1", 1, 5, 5, 2, null
			}, {
				"admin", "admin", "findByCreatorIdAllPublics", false, "user1", 1, 5, 5, 2, null
			}, {
				"user", "user3", "findByCreatorIdAllPublics", false, "user4", 1, 1, 5, 1, null
			}, {
				null, null, "findByCreatorIdAllPublics", false, "user1", 1, 5, 5, 2, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findByCreatorIdAllPublics de un usuario con id 0 logeados como user4 mirando la primera página con tamaño 5 (salta un IllegalArgumentException)
	 */
	@Test()
	public void negativeFindByCreatorIdAllPublicsTest() {
		final Object testingData[][] = {
			{
				"user", "user4", "findByCreatorIdAllPublics", true, "user1", 1, 5, 5, 2, IllegalArgumentException.class
			},
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findByAttendantId del user2 logeados como user1 mirando la primera página con tamaño 5 (no salta excepción)
	 * 2.Hacemos el findByAttendantId del user2 logeados como user1 mirando la primera página con tamaño 2 (no salta excepción)
	 * 3.Hacemos el findByAttendantId del user2 logeados como user1 mirando la segunda página con tamaño 2 (no salta excepción)
	 * 4.Hacemos el findByAttendantId del user2 logeados como user1 mirando la tercera página con tamaño 2 (no salta excepción)
	 * 5.Hacemos el findByAttendantId del user2 logeados como user1 mirando la primera página con tamaño 3 (no salta excepción)
	 * 6.Hacemos el findByAttendantId del user2 logeados como user1 mirando la segunda página con tamaño 3 (no salta excepción)
	 * 7.Hacemos el findByAttendantId del user2 logeados como manager1 mirando la primera página con tamaño 5 (no salta excepción)
	 * 8.Hacemos el findByAttendantId del user2 logeados como admin mirando la primera página con tamaño 5 (no salta excepción)
	 * 9.Hacemos el findByAttendantId del user3 logeados como user3 mirando la primera página con tamaño 5 (no salta excepción)
	 * 10.Hacemos el findByAttendantId del user3 logeados como user4 mirando la primera página con tamaño 5 (no salta excepción)
	 * 
	 * Requisitos
	 * C.4.2: List the rendezvouses in the system and navigate to the profiles of the corresponding creators and attendants
	 * C.5.5: An actor who is authenticated as a user must be able to list the rendezvouses that he or she is RSVPd
	 * B.14: Such rendezvouses must not be displayed unless the user who is browsing them is at least 18 year old. Obviously, they must not be shown to unauthenticated users.
	 */
	@Test()
	public void positiveFindByAttendantIdTest() {
		final Object testingData[][] = {
			{
				"user", "user1", "findByAttendantId", false, "user2", 1, 3, 5, 1, null
			}, {
				"user", "user1", "findByAttendantId", false, "user2", 1, 2, 2, 2, null
			}, {
				"user", "user1", "findByAttendantId", false, "user2", 2, 1, 2, 2, null
			}, {
				"user", "user1", "findByAttendantId", false, "user2", 3, 0, 2, 2, null
			}, {
				"user", "user1", "findByAttendantId", false, "user2", 1, 3, 3, 1, null
			}, {
				"user", "user1", "findByAttendantId", false, "user2", 2, 0, 3, 1, null
			}, {
				"manager", "manager1", "findByAttendantId", false, "user2", 1, 3, 5, 1, null
			}, {
				"admin", "admin", "findByAttendantId", false, "user2", 1, 3, 5, 1, null
			}, {
				"user", "user3", "findByAttendantId", false, "user3", 1, 2, 5, 1, null
			}, {
				"user", "user3", "findByAttendantId", false, "user4", 1, 2, 5, 1, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findByAttendantId del user2 logeados como user4 que es menor de edad mirando la primera página con tamaño 5 (salta un IllegalArgumentException)
	 * 2.Hacemos el findByAttendantId del user2 sin estar logeado mirando la primera página con tamaño 5 (salta un IllegalArgumentException)
	 * 3.Hacemos el findByAttendantId de un usario con id 0 (salta un IllegalArgumentException)
	 */
	@Test()
	public void negativeFindByAttendantIdTest() {
		final Object testingData[][] = {
			{
				"user", "user4", "findByAttendantId", false, "user2", 1, 3, 5, 1, IllegalArgumentException.class
			}, {
				null, null, "findByAttendantId", false, "user2", 1, 3, 5, 1, IllegalArgumentException.class
			}, {
				"user", "user1", "findByAttendantId", true, "user2", 1, 3, 5, 1, IllegalArgumentException.class
			},
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findByAttendantIdAllPublics del user4 logeados como user4 mirando la primera página con tamaño 5 (no salta excepción)
	 * 2.Hacemos el findByAttendantIdAllPublics del user4 logeados como user4 mirando la primera página con tamaño 1 (no salta excepción)
	 * 3.Hacemos el findByAttendantIdAllPublics del user4 logeados como user4 mirando la segunda página con tamaño 1 (no salta excepción)
	 * 4.Hacemos el findByAttendantIdAllPublics del user4 logeados como user4 mirando la tercera página con tamaño 1 (no salta excepción)
	 * 5.Hacemos el findByAttendantIdAllPublics del user4 logeados como user4 mirando la primera página con tamaño 2 (no salta excepción)
	 * 6.Hacemos el findByAttendantIdAllPublics del user4 logeados como user4 mirando la primera segunda con tamaño 2 (no salta excepción)
	 * 7.Hacemos el findByAttendantIdAllPublics del user4 logeados como manager1 mirando la primera página con tamaño 5 (no salta excepción)
	 * 8.Hacemos el findByAttendantIdAllPublics del user4 logeados como admin mirando la primera página con tamaño 5 (no salta excepción)
	 * 9.Hacemos el findByAttendantIdAllPublics del user4 logeados como user3 mirando la primera página con tamaño 5 (no salta excepción)
	 * 10.Hacemos el findByAttendantIdAllPublics del user4 sin estar logeado mirando la primera página con tamaño 5 (no salta excepción)
	 * 
	 * Requisitos
	 * C.4.2: List the rendezvouses in the system and navigate to the profiles of the corresponding creators and attendants
	 * C.5.5: An actor who is authenticated as a user must be able to list the rendezvouses that he or she is RSVPd
	 */
	@Test()
	public void positiveFindByAttendantIdAllPublicsTest() {
		final Object testingData[][] = {
			{
				"user", "user4", "findByAttendantIdAllPublics", false, "user4", 1, 2, 5, 1, null
			}, {
				"user", "user4", "findByAttendantIdAllPublics", false, "user4", 1, 1, 1, 2, null
			}, {
				"user", "user4", "findByAttendantIdAllPublics", false, "user4", 2, 1, 1, 2, null
			}, {
				"user", "user4", "findByAttendantIdAllPublics", false, "user4", 3, 0, 1, 2, null
			}, {
				"user", "user4", "findByAttendantIdAllPublics", false, "user4", 1, 2, 2, 1, null
			}, {
				"user", "user4", "findByAttendantIdAllPublics", false, "user4", 2, 0, 2, 1, null
			}, {
				"manager", "manager1", "findByAttendantIdAllPublics", false, "user4", 1, 2, 5, 1, null
			}, {
				"admin", "admin", "findByAttendantIdAllPublics", false, "user4", 1, 2, 5, 1, null
			}, {
				"user", "user3", "findByAttendantIdAllPublics", false, "user4", 1, 2, 5, 1, null
			}, {
				null, null, "findByAttendantIdAllPublics", false, "user4", 1, 2, 5, 1, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findByAttendantIdAllPublics de un usuario con id 0 logeados como user4 mirando la primera página con tamaño 5 (salta un IllegalArgumentException)
	 */
	@Test()
	public void negativeFindByAttendantIdAllPublicsTest() {
		final Object testingData[][] = {
			{
				"user", "user4", "findByAttendantIdAllPublics", true, "user4", 1, 2, 5, 1, IllegalArgumentException.class
			},
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findByLinkerRendezvousId del rendezvous2 logeados como user1 mirando la primera página con tamaño 5 (no salta excepción)
	 * 2.Hacemos el findByLinkerRendezvousId del rendezvous2 logeados como user1 mirando la primera página con tamaño 2 (no salta excepción)
	 * 3.Hacemos el findByLinkerRendezvousId del rendezvous2 logeados como user1 mirando la segunda página con tamaño 2 (no salta excepción)
	 * 4.Hacemos el findByLinkerRendezvousId del rendezvous2 logeados como user1 mirando la tercera página con tamaño 2 (no salta excepción)
	 * 5.Hacemos el findByLinkerRendezvousId del rendezvous2 logeados como user1 mirando la primera página con tamaño 3 (no salta excepción)
	 * 6.Hacemos el findByLinkerRendezvousId del rendezvous2 logeados como user1 mirando la segunda página con tamaño 3 (no salta excepción)
	 * 7.Hacemos el findByLinkerRendezvousId del rendezvous2 logeados como manager1 mirando la primera página con tamaño 5 (no salta excepción)
	 * 8.Hacemos el findByLinkerRendezvousId del rendezvous2 logeados como admin mirando la primera página con tamaño 5 (no salta excepción)
	 * 9.Hacemos el findByLinkerRendezvousId del rendezvous2 logeados como user3 mirando la primera página con tamaño 5 (no salta excepción)
	 * 10.Hacemos el findByLinkerRendezvousId del rendezvous1 logeados como user3 mirando la primera página con tamaño 5 (no salta excepción)
	 * 
	 * Requisitos
	 * B.13: A rendezvous may be explicitly linked to similar ones by its creator. Note that such links may be added or removed even if the rendezvous is saved in final mode. They must be listed whenever a rendezvous is shown.
	 * B.14: Such rendezvouses must not be displayed unless the user who is browsing them is at least 18 year old. Obviously, they must not be shown to unauthenticated users.
	 */
	@Test()
	public void positiveFindByLinkerRendezvousIdTest() {
		final Object testingData[][] = {
			{
				"user", "user1", "findByLinkerRendezvousId", false, "rendezvous2", 1, 3, 5, 1, null
			}, {
				"user", "user1", "findByLinkerRendezvousId", false, "rendezvous2", 1, 2, 2, 2, null
			}, {
				"user", "user1", "findByLinkerRendezvousId", false, "rendezvous2", 2, 1, 2, 2, null
			}, {
				"user", "user1", "findByLinkerRendezvousId", false, "rendezvous2", 3, 0, 2, 2, null
			}, {
				"user", "user1", "findByLinkerRendezvousId", false, "rendezvous2", 1, 3, 3, 1, null
			}, {
				"user", "user1", "findByLinkerRendezvousId", false, "rendezvous2", 2, 0, 3, 1, null
			}, {
				"manager", "manager1", "findByLinkerRendezvousId", false, "rendezvous2", 1, 3, 5, 1, null
			}, {
				"admin", "admin", "findByLinkerRendezvousId", false, "rendezvous2", 1, 3, 5, 1, null
			}, {
				"user", "user3", "findByLinkerRendezvousId", false, "rendezvous2", 1, 3, 5, 1, null
			}, {
				"user", "user3", "findByLinkerRendezvousId", false, "rendezvous1", 1, 0, 5, 0, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findByLinkerRendezvousId del rendezvous1 logeados como user4 que es menor de edad mirando la primera página con tamaño 5 (salta un IllegalArgumentException)
	 * 2.Hacemos el findByLinkerRendezvousId del rendezvous1 sin estar logeados mirando la primera página con tamaño 5 (salta un IllegalArgumentException)
	 * 3.Hacemos el findByLinkerRendezvousId de un rendezvous con id 0 (salta un IllegalArgumentException)
	 */
	@Test()
	public void negativeFindByLinkerRendezvousIdTest() {
		final Object testingData[][] = {
			{
				"user", "user4", "findByLinkerRendezvousId", false, "rendezvous1", 1, 0, 5, 0, IllegalArgumentException.class
			}, {
				null, null, "findByLinkerRendezvousId", false, "rendezvous1", 1, 0, 5, 0, IllegalArgumentException.class
			}, {
				"user", "user1", "findByLinkerRendezvousId", true, "rendezvous2", 1, 3, 5, 1, IllegalArgumentException.class
			},
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findByLinkerRendezvousIdAllPublics del rendezvous9 logeados como user4 mirando la primera página con tamaño 5 (no salta excepción)
	 * 2.Hacemos el findByLinkerRendezvousIdAllPublics del rendezvous9 logeados como user4 mirando la primera página con tamaño 1 (no salta excepción)
	 * 3.Hacemos el findByLinkerRendezvousIdAllPublics del rendezvous9 logeados como user4 mirando la segunda página con tamaño 1 (no salta excepción)
	 * 4.Hacemos el findByLinkerRendezvousIdAllPublics del rendezvous9 logeados como user4 mirando la tercera página con tamaño 1 (no salta excepción)
	 * 5.Hacemos el findByLinkerRendezvousIdAllPublics del rendezvous9 logeados como user4 mirando la primera página con tamaño 2 (no salta excepción)
	 * 6.Hacemos el findByLinkerRendezvousIdAllPublics del rendezvous9 logeados como user4 mirando la segunda página con tamaño 2 (no salta excepción)
	 * 7.Hacemos el findByLinkerRendezvousIdAllPublics del rendezvous9 logeados como manager1 mirando la primera página con tamaño 5 (no salta excepción)
	 * 8.Hacemos el findByLinkerRendezvousIdAllPublics del rendezvous9 logeados como admin mirando la primera página con tamaño 5 (no salta excepción)
	 * 9.Hacemos el findByLinkerRendezvousIdAllPublics del rendezvous9 logeados como user3 mirando la primera página con tamaño 5 (no salta excepción)
	 * 10.Hacemos el findByLinkerRendezvousIdAllPublics del rendezvous1 logeados como user3 mirando la primera página con tamaño 5 (no salta excepción)
	 * 
	 * Requisitos
	 * B.13: A rendezvous may be explicitly linked to similar ones by its creator. Note that such links may be added or removed even if the rendezvous is saved in final mode. They must be listed whenever a rendezvous is shown.
	 */
	@Test()
	public void positiveFindByLinkerRendezvousIdAllPublicsTest() {
		final Object testingData[][] = {
			{
				"user", "user4", "findByLinkerRendezvousAllPublicsId", false, "rendezvous9", 1, 2, 5, 1, null
			}, {
				"user", "user4", "findByLinkerRendezvousAllPublicsId", false, "rendezvous9", 1, 1, 1, 2, null
			}, {
				"user", "user4", "findByLinkerRendezvousAllPublicsId", false, "rendezvous9", 2, 1, 1, 2, null
			}, {
				"user", "user4", "findByLinkerRendezvousAllPublicsId", false, "rendezvous9", 3, 0, 1, 2, null
			}, {
				"user", "user4", "findByLinkerRendezvousAllPublicsId", false, "rendezvous9", 1, 2, 2, 1, null
			}, {
				"user", "user4", "findByLinkerRendezvousAllPublicsId", false, "rendezvous9", 2, 0, 2, 1, null
			}, {
				"manager", "manager1", "findByLinkerRendezvousAllPublicsId", false, "rendezvous9", 1, 2, 5, 1, null
			}, {
				"admin", "admin", "findByLinkerRendezvousAllPublicsId", false, "rendezvous9", 1, 2, 5, 1, null
			}, {
				"user", "user3", "findByLinkerRendezvousAllPublicsId", false, "rendezvous9", 1, 2, 5, 1, null
			}, {
				"user", "user3", "findByLinkerRendezvousAllPublicsId", false, "rendezvous1", 1, 0, 5, 0, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findByLinkerRendezvousIdAllPublics de un rendezvous con id 0 (salta un IllegalArgumentException)
	 */
	@Test()
	public void negativeFindByLinkerRendezvousIdAllPublicsTest() {
		final Object testingData[][] = {
			{
				"user", "user4", "findByLinkerRendezvousAllPublicsId", true, "rendezvous9", 1, 2, 5, 1, IllegalArgumentException.class
			},
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findLinkerRendezvousesByRendezvousId del rendezvous1 logeados como user1 mirando la primera página con tamaño 5 (no salta excepción)
	 * 2.Hacemos el findLinkerRendezvousesByRendezvousId del rendezvous1 logeados como user1 mirando la primera página con tamaño 2 (no salta excepción)
	 * 3.Hacemos el findLinkerRendezvousesByRendezvousId del rendezvous1 logeados como user1 mirando la primera página con tamaño 2 (no salta excepción)
	 * 4.Hacemos el findLinkerRendezvousesByRendezvousId del rendezvous1 logeados como user1 mirando la tercera página con tamaño 2 (no salta excepción)
	 * 5.Hacemos el findLinkerRendezvousesByRendezvousId del rendezvous1 logeados como user1 mirando la primera página con tamaño 3 (no salta excepción)
	 * 6.Hacemos el findLinkerRendezvousesByRendezvousId del rendezvous1 logeados como user1 mirando la segunda página con tamaño 3 (no salta excepción)
	 * 7.Hacemos el findLinkerRendezvousesByRendezvousId del rendezvous1 logeados como manager1 mirando la primera página con tamaño 5 (no salta excepción)
	 * 8.Hacemos el findLinkerRendezvousesByRendezvousId del rendezvous1 logeados como admin mirando la primera página con tamaño 5 (no salta excepción)
	 * 9.Hacemos el findLinkerRendezvousesByRendezvousId del rendezvous3 logeados como user3 mirando la primera página con tamaño 5 (no salta excepción)
	 * 10.Hacemos el findLinkerRendezvousesByRendezvousId del rendezvous3 logeados como user3 mirando la segunda página con tamaño 5 (no salta excepción)
	 * 
	 * Requisitos
	 * B.13: A rendezvous may be explicitly linked to similar ones by its creator. Note that such links may be added or removed even if the rendezvous is saved in final mode. They must be listed whenever a rendezvous is shown.
	 * B.14: Such rendezvouses must not be displayed unless the user who is browsing them is at least 18 year old. Obviously, they must not be shown to unauthenticated users.
	 */
	@Test()
	public void positiveFindLinkerRendezvousesByRendezvousIdTest() {
		final Object testingData[][] = {
			{
				"user", "user1", "findLinkerRendezvousesByRendezvousId", false, "rendezvous1", 1, 3, 5, 1, null
			}, {
				"user", "user1", "findLinkerRendezvousesByRendezvousId", false, "rendezvous1", 1, 2, 2, 2, null
			}, {
				"user", "user1", "findLinkerRendezvousesByRendezvousId", false, "rendezvous1", 2, 1, 2, 2, null
			}, {
				"user", "user1", "findLinkerRendezvousesByRendezvousId", false, "rendezvous1", 3, 0, 2, 2, null
			}, {
				"user", "user1", "findLinkerRendezvousesByRendezvousId", false, "rendezvous1", 1, 3, 3, 1, null
			}, {
				"user", "user1", "findLinkerRendezvousesByRendezvousId", false, "rendezvous1", 2, 0, 3, 1, null
			}, {
				"manager", "manager1", "findLinkerRendezvousesByRendezvousId", false, "rendezvous1", 1, 3, 5, 1, null
			}, {
				"admin", "admin", "findLinkerRendezvousesByRendezvousId", false, "rendezvous1", 1, 3, 5, 1, null
			}, {
				"user", "user3", "findLinkerRendezvousesByRendezvousId", false, "rendezvous3", 1, 1, 5, 1, null
			}, {
				"user", "user3", "findLinkerRendezvousesByRendezvousId", false, "rendezvous3", 2, 0, 5, 1, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findLinkerRendezvousesByRendezvousId del rendezvous1 logeados como user4 que es menor de edad mirando la primera página con tamaño 5 (salta un IllegalArgumentException)
	 * 2.Hacemos el findLinkerRendezvousesByRendezvousId del rendezvous1 sin estar logeado mirando la primera página con tamaño 5 (salta un IllegalArgumentException)
	 * 3.Hacemos el findLinkerRendezvousesByRendezvousId de un rendezvous con id 0 (salta un IllegalArgumentException)
	 */
	@Test()
	public void negativeFindLinkerRendezvousesByRendezvousIdTest() {
		final Object testingData[][] = {
			{
				"user", "user4", "findLinkerRendezvousesByRendezvousId", false, "rendezvous1", 1, 3, 5, 1, IllegalArgumentException.class
			}, {
				null, null, "findLinkerRendezvousesByRendezvousId", false, "rendezvous1", 1, 3, 5, 1, IllegalArgumentException.class
			}, {
				"user", "user1", "findLinkerRendezvousesByRendezvousId", true, "rendezvous1", 1, 3, 5, 1, IllegalArgumentException.class
			},
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findLinkerRendezvousesAllPublicsByRendezvousId del rendezvous1 logeados como user4 mirando la primera página con tamaño 5 (no salta excepción)
	 * 2.Hacemos el findLinkerRendezvousesAllPublicsByRendezvousId del rendezvous1 logeados como user4 mirando la primera página con tamaño 1 (no salta excepción)
	 * 3.Hacemos el findLinkerRendezvousesAllPublicsByRendezvousId del rendezvous1 logeados como user4 mirando la segunda página con tamaño 1 (no salta excepción)
	 * 4.Hacemos el findLinkerRendezvousesAllPublicsByRendezvousId del rendezvous1 logeados como user4 mirando la tercera página con tamaño 1 (no salta excepción)
	 * 5.Hacemos el findLinkerRendezvousesAllPublicsByRendezvousId del rendezvous1 logeados como user4 mirando la primera página con tamaño 2 (no salta excepción)
	 * 6.Hacemos el findLinkerRendezvousesAllPublicsByRendezvousId del rendezvous1 logeados como user4 mirando la segunda página con tamaño 2 (no salta excepción)
	 * 7.Hacemos el findLinkerRendezvousesAllPublicsByRendezvousId del rendezvous1 logeados como manager1 mirando la primera página con tamaño 5 (no salta excepción)
	 * 8.Hacemos el findLinkerRendezvousesAllPublicsByRendezvousId del rendezvous1 logeados como admin mirando la primera página con tamaño 5 (no salta excepción)
	 * 9.Hacemos el findLinkerRendezvousesAllPublicsByRendezvousId del rendezvous3 logeados como user3 mirando la primera página con tamaño 5 (no salta excepción)
	 * 10.Hacemos el findLinkerRendezvousesAllPublicsByRendezvousId del rendezvous7 logeados como user3 mirando la primera página con tamaño 5 (no salta excepción)
	 * 
	 * Requisitos
	 * B.13: A rendezvous may be explicitly linked to similar ones by its creator. Note that such links may be added or removed even if the rendezvous is saved in final mode. They must be listed whenever a rendezvous is shown.
	 */
	@Test()
	public void positiveFindLinkerRendezvousesAllPublicsByRendezvousIdTest() {
		final Object testingData[][] = {
			{
				"user", "user4", "findLinkerRendezvousesAllPublicsByRendezvousId", false, "rendezvous1", 1, 2, 5, 1, null
			}, {
				"user", "user4", "findLinkerRendezvousesAllPublicsByRendezvousId", false, "rendezvous1", 1, 1, 1, 2, null
			}, {
				"user", "user4", "findLinkerRendezvousesAllPublicsByRendezvousId", false, "rendezvous1", 2, 1, 1, 2, null
			}, {
				"user", "user4", "findLinkerRendezvousesAllPublicsByRendezvousId", false, "rendezvous1", 3, 0, 1, 2, null
			}, {
				"user", "user4", "findLinkerRendezvousesAllPublicsByRendezvousId", false, "rendezvous1", 1, 2, 2, 1, null
			}, {
				"user", "user4", "findLinkerRendezvousesAllPublicsByRendezvousId", false, "rendezvous1", 2, 0, 2, 1, null
			}, {
				"manager", "manager1", "findLinkerRendezvousesAllPublicsByRendezvousId", false, "rendezvous1", 1, 2, 5, 1, null
			}, {
				"admin", "admin", "findLinkerRendezvousesAllPublicsByRendezvousId", false, "rendezvous1", 1, 2, 5, 1, null
			}, {
				"user", "user3", "findLinkerRendezvousesAllPublicsByRendezvousId", false, "rendezvous3", 1, 0, 5, 0, null
			}, {
				"user", "user3", "findLinkerRendezvousesAllPublicsByRendezvousId", false, "rendezvous7", 1, 0, 5, 0, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findLinkerRendezvousesAllPublicsByRendezvousId de un rendezvous con id 0 (salta un IllegalArgumentException)
	 */
	@Test()
	public void negativeFindLinkerRendezvousesAllPublicsByRendezvousIdTest() {
		final Object testingData[][] = {
			{
				"user", "user4", "findLinkerRendezvousesAllPublicsByRendezvousId", true, "rendezvous7", 1, 0, 5, 0, IllegalArgumentException.class
			},
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findAllPublics logeados como user4 mirando la primera página con tamaño 8 (no salta excepción)
	 * 2.Hacemos el findAllPublics logeados como user4 mirando la primera página con tamaño 5 (no salta excepción)
	 * 3.Hacemos el findAllPublics logeados como user4 mirando la segunda página con tamaño 5 (no salta excepción)
	 * 4.Hacemos el findAllPublics logeados como user4 mirando la tercera página con tamaño 5 (no salta excepción)
	 * 5.Hacemos el findAllPublics logeados como user4 mirando la primera página con tamaño 7 (no salta excepción)
	 * 6.Hacemos el findAllPublics logeados como user4 mirando la segunda página con tamaño 7 (no salta excepción)
	 * 7.Hacemos el findAllPublics logeados como manager1 mirando la primera página con tamaño 8 (no salta excepción)
	 * 8.Hacemos el findAllPublics logeados como admin mirando la primera página con tamaño 8 (no salta excepción)
	 * 9.Hacemos el findAllPublics sin estar logeados mirando la primera página con tamaño 8 (no salta excepción)
	 * 
	 * Requisitos
	 * C.4.3: List the rendezvouses in the system and navigate to the profiles of the corresponding creators and attendants
	 */
	@Test()
	public void findAllPublicsTest() {
		final Object testingData[][] = {
			{
				"user", "user4", "findAllPublics", false, null, 1, 7, 8, 1, null
			}, {
				"user", "user4", "findAllPublics", false, null, 1, 5, 5, 2, null
			}, {
				"user", "user4", "findAllPublics", false, null, 2, 2, 5, 2, null
			}, {
				"user", "user4", "findAllPublics", false, null, 3, 0, 5, 2, null
			}, {
				"user", "user4", "findAllPublics", false, null, 1, 7, 7, 1, null
			}, {
				"user", "user4", "findAllPublics", false, null, 2, 0, 7, 1, null
			}, {
				"manager", "manager1", "findAllPublics", false, null, 1, 7, 8, 1, null
			}, {
				"admin", "admin", "findAllPublics", false, null, 1, 7, 8, 1, null
			}, {
				null, null, "findAllPublics", false, null, 1, 7, 8, 1, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findAllPaginated logeados como user1 mirando la primera página con tamaño 11 (no salta excepción)
	 * 2.Hacemos el findAllPaginated logeados como user1 mirando la primera página con tamaño 6 (no salta excepción)
	 * 3.Hacemos el findAllPaginated logeados como user1 mirando la segunda página con tamaño 6 (no salta excepción)
	 * 4.Hacemos el findAllPaginated logeados como user1 mirando la tercera página con tamaño 6 (no salta excepción)
	 * 5.Hacemos el findAllPaginated logeados como user1 mirando la primera página con tamaño 10 (no salta excepción)
	 * 6.Hacemos el findAllPaginated logeados como user1 mirando la segunda página con tamaño 10 (no salta excepción)
	 * 7.Hacemos el findAllPaginated logeados como manager1 mirando la primera página con tamaño 11 (no salta excepción)
	 * 8.Hacemos el findAllPaginated logeados como admin mirando la primera página con tamaño 11 (no salta excepción)
	 * 9.Hacemos el findAllPaginated logeados como user4 que es menor de edad mirando la primera página con tamaño 11 (salta un IllegalArgumentException)
	 * 10.Hacemos el findAllPaginated sin estar logeado mirando la primera página con tamaño 11 (salta un IllegalArgumentException)
	 * 
	 * Requisitos
	 * C.4.3: List the rendezvouses in the system and navigate to the profiles of the corresponding creators and attendants
	 * B.14: Such rendezvouses must not be displayed unless the user who is browsing them is at least 18 year old. Obviously, they must not be shown to unauthenticated users.
	 */
	@Test()
	public void findAllPaginatedTest() {
		final Object testingData[][] = {
			{
				"user", "user1", "findAllPaginated", false, null, 1, 10, 11, 1, null
			}, {
				"user", "user1", "findAllPaginated", false, null, 1, 6, 6, 2, null
			}, {
				"user", "user1", "findAllPaginated", false, null, 2, 4, 6, 2, null
			}, {
				"user", "user1", "findAllPaginated", false, null, 3, 0, 6, 2, null
			}, {
				"user", "user1", "findAllPaginated", false, null, 1, 10, 10, 1, null
			}, {
				"user", "user1", "findAllPaginated", false, null, 2, 0, 10, 1, null
			}, {
				"manager", "manager1", "findAllPaginated", false, null, 1, 10, 11, 1, null
			}, {
				"admin", "admin", "findAllPaginated", false, null, 1, 10, 11, 1, null
			}, {
				"user", "user4", "findAllPaginated", false, null, 1, 10, 11, 1, IllegalArgumentException.class
			}, {
				null, null, "findAllPaginated", false, null, 1, 10, 11, 1, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findNotLinkedByRendezvous del rendezvous2 logeados como user1 mirando la primera página con tamaño 4 (no salta excepción)
	 * 2.Hacemos el findNotLinkedByRendezvous del rendezvous2 logeados como user1 mirando la segunda página con tamaño 4 (no salta excepción)
	 * 3.Hacemos el findNotLinkedByRendezvous del rendezvous2 logeados como user1 mirando la tercera página con tamaño 4 (no salta excepción)
	 * 4.Hacemos el findNotLinkedByRendezvous del rendezvous2 logeados como user1 mirando la primera página con tamaño 8 (no salta excepción)
	 * 5.Hacemos el findNotLinkedByRendezvous del rendezvous2 logeados como user1 mirando la primera página con tamaño 5 (no salta excepción)
	 * 6.Hacemos el findNotLinkedByRendezvous del rendezvous2 logeados como user1 mirando la segunda página con tamaño 5 (no salta excepción)
	 * 7.Hacemos el findNotLinkedByRendezvous del rendezvous2 logeados como manager1 mirando la primera página con tamaño 4 (no salta excepción)
	 * 8.Hacemos el findNotLinkedByRendezvous del rendezvous2 logeados como admin mirando la primera página con tamaño 4 (no salta excepción)
	 * 9.Hacemos el findNotLinkedByRendezvous del rendezvous1 logeados como user3 mirando la primera página con tamaño 10 (no salta excepción)
	 * 10.Hacemos el findNotLinkedByRendezvous del rendezvous9 logeados como user3 mirando la primera página con tamaño 6 (no salta excepción)
	 * 
	 * Requisitos
	 * B.13: A rendezvous may be explicitly linked to similar ones by its creator. Note that such links may be added or removed even if the rendezvous is saved in final mode. They must be listed whenever a rendezvous is shown.
	 * B.14: Such rendezvouses must not be displayed unless the user who is browsing them is at least 18 year old. Obviously, they must not be shown to unauthenticated users.
	 */
	@Test()
	public void positiveFindNotLinkedByRendezvousTest() {
		final Object testingData[][] = {
			{
				"user", "user1", "findNotLinkedByRendezvous", false, "rendezvous2", 1, 4, 4, 2, null
			}, {
				"user", "user1", "findNotLinkedByRendezvous", false, "rendezvous2", 2, 1, 4, 2, null
			}, {
				"user", "user1", "findNotLinkedByRendezvous", false, "rendezvous2", 3, 0, 4, 2, null
			}, {
				"user", "user1", "findNotLinkedByRendezvous", false, "rendezvous2", 1, 5, 8, 1, null
			}, {
				"user", "user1", "findNotLinkedByRendezvous", false, "rendezvous2", 1, 5, 5, 1, null
			}, {
				"user", "user1", "findNotLinkedByRendezvous", false, "rendezvous2", 2, 0, 5, 1, null
			}, {
				"manager", "manager1", "findNotLinkedByRendezvous", false, "rendezvous2", 1, 4, 4, 2, null
			}, {
				"admin", "admin", "findNotLinkedByRendezvous", false, "rendezvous2", 1, 4, 4, 2, null
			}, {
				"user", "user3", "findNotLinkedByRendezvous", false, "rendezvous1", 1, 8, 10, 1, null
			}, {
				"user", "user3", "findNotLinkedByRendezvous", false, "rendezvous9", 1, 6, 6, 1, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findNotLinkedByRendezvous del rendezvous1 logeados como user4 que es menor de edad mirando la primera página con tamaño 10 (salta un IllegalArgumentException)
	 * 2.Hacemos el findNotLinkedByRendezvous del rendezvous1 sin estar logeados mirando la primera página con tamaño 10 (salta un IllegalArgumentException)
	 * 3.Hacemos el findNotLinkedByRendezvous de un rendezvous con Id 0 (salta un IllegalArgumentException)
	 */
	@Test()
	public void negativeFindNotLinkedByRendezvousTest() {
		final Object testingData[][] = {
			{
				"user", "user4", "findNotLinkedByRendezvous", false, "rendezvous1", 1, 9, 10, 1, IllegalArgumentException.class
			}, {
				null, null, "findNotLinkedByRendezvous", false, "rendezvous1", 1, 9, 10, 1, IllegalArgumentException.class
			}, {
				"user", "user1", "findNotLinkedByRendezvous", true, "rendezvous1", 1, 9, 10, 1, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findNotLinkedByRendezvousAllPublics del rendezvous9 logeados como user4 mirando la primera página con tamaño 3 (no salta excepción)
	 * 2.Hacemos el findNotLinkedByRendezvousAllPublics del rendezvous9 logeados como user4 mirando la segunda página con tamaño 3 (no salta excepción)
	 * 3.Hacemos el findNotLinkedByRendezvousAllPublics del rendezvous9 logeados como user4 mirando la tercera página con tamaño 3 (no salta excepción)
	 * 4.Hacemos el findNotLinkedByRendezvousAllPublics del rendezvous9 logeados como user4 mirando la primera página con tamaño 5 (no salta excepción)
	 * 5.Hacemos el findNotLinkedByRendezvousAllPublics del rendezvous9 logeados como user4 mirando la primera página con tamaño 4 (no salta excepción)
	 * 6.Hacemos el findNotLinkedByRendezvousAllPublics del rendezvous9 logeados como user4 mirando la segunda página con tamaño 4 (no salta excepción)
	 * 7.Hacemos el findNotLinkedByRendezvousAllPublics del rendezvous9 logeados como manager1 mirando la primera página con tamaño 3 (no salta excepción)
	 * 8.Hacemos el findNotLinkedByRendezvousAllPublics del rendezvous9 logeados como admin mirando la primera página con tamaño 3 (no salta excepción)
	 * 9.Hacemos el findNotLinkedByRendezvousAllPublics del rendezvous1 logeados como user3 mirando la primera página con tamaño 10 (no salta excepción)
	 * 10.Hacemos el findNotLinkedByRendezvousAllPublics del rendezvous7 logeados como user3 mirando la primera página con tamaño 6 (no salta excepción)
	 * 
	 * Requisitos
	 * B.13: A rendezvous may be explicitly linked to similar ones by its creator. Note that such links may be added or removed even if the rendezvous is saved in final mode. They must be listed whenever a rendezvous is shown.
	 */
	@Test()
	public void positiveFindNotLinkedByRendezvousAllPublicsTest() {
		final Object testingData[][] = {
			{
				"user", "user4", "findNotLinkedByRendezvousAllPublics", false, "rendezvous9", 1, 3, 3, 2, null
			}, {
				"user", "user4", "findNotLinkedByRendezvousAllPublics", false, "rendezvous9", 2, 1, 3, 2, null
			}, {
				"user", "user4", "findNotLinkedByRendezvousAllPublics", false, "rendezvous9", 3, 0, 3, 2, null
			}, {
				"user", "user4", "findNotLinkedByRendezvousAllPublics", false, "rendezvous9", 1, 4, 5, 1, null
			}, {
				"user", "user4", "findNotLinkedByRendezvousAllPublics", false, "rendezvous9", 1, 4, 4, 1, null
			}, {
				"user", "user4", "findNotLinkedByRendezvousAllPublics", false, "rendezvous9", 2, 0, 4, 1, null
			}, {
				"manager", "manager1", "findNotLinkedByRendezvousAllPublics", false, "rendezvous9", 1, 3, 3, 2, null
			}, {
				"admin", "admin", "findNotLinkedByRendezvousAllPublics", false, "rendezvous9", 1, 3, 3, 2, null
			}, {
				"user", "user3", "findNotLinkedByRendezvousAllPublics", false, "rendezvous1", 1, 6, 10, 1, null
			}, {
				"user", "user3", "findNotLinkedByRendezvousAllPublics", false, "rendezvous7", 1, 6, 6, 1, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findNotLinkedByRendezvousAllPublics de un rendezvous con id 0 (salta un IllegalArgumentException)
	 */
	@Test()
	public void negativeFindNotLinkedByRendezvousAllPublicsTest() {
		final Object testingData[][] = {
			{
				"user", "user4", "findNotLinkedByRendezvousAllPublics", true, "rendezvous9", 1, 3, 3, 2, IllegalArgumentException.class
			},
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findByCategoryId de la category1 logeados como user1 mirando la primera página con tamaño 4 (no salta excepción)
	 * 2.Hacemos el findByCategoryId de la category1 logeados como user1 mirando la primera página con tamaño 2 (no salta excepción)
	 * 3.Hacemos el findByCategoryId de la category1 logeados como user1 mirando la segunda página con tamaño 2 (no salta excepción)
	 * 4.Hacemos el findByCategoryId de la category1 logeados como user1 mirando la primera página con tamaño 3 (no salta excepción)
	 * 5.Hacemos el findByCategoryId de la category1 logeados como manager1 mirando la primera página con tamaño 4 (no salta excepción)
	 * 6.Hacemos el findByCategoryId de la category1 logeados como admin mirando la primera página con tamaño 4 (no salta excepción)
	 * 7.Hacemos el findByCategoryId de la category2 logeados como user3 mirando la primera página con tamaño 4 (no salta excepción)
	 * 
	 * Requisitos
	 * B.10.1:List the rendezvouses in the system grouped by category.
	 * B.14: Such rendezvouses must not be displayed unless the user who is browsing them is at least 18 year old. Obviously, they must not be shown to unauthenticated users.
	 */
	@Test()
	public void positiveFindByCategoryIdTest() {
		final Object testingData[][] = {
			{
				"user", "user1", "findByCategoryId", false, "category1", 1, 3, 4, 1, null
			}, {
				"user", "user1", "findByCategoryId", false, "category1", 1, 2, 2, 2, null
			}, {
				"user", "user1", "findByCategoryId", false, "category1", 2, 1, 2, 2, null
			}, {
				"user", "user1", "findByCategoryId", false, "category1", 1, 3, 3, 1, null
			}, {
				"manager", "manager1", "findByCategoryId", false, "category1", 1, 3, 4, 1, null
			}, {
				"admin", "admin", "findByCategoryId", false, "category1", 1, 3, 4, 1, null
			}, {
				"user", "user3", "findByCategoryId", false, "category2", 1, 3, 4, 1, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findByCategoryId de la category1 logeados como user4 que es menor de edad mirando la primera página con tamaño 4 (salta un IllegalArgumentException)
	 * 2.Hacemos el findByCategoryId de la category1 sin estar logeado mirando la primera página con tamaño 2 (salta un IllegalArgumentException)
	 * 3.Hacemos el findByCategoryId de una categoria de id 0 (salta un IllegalArgumentException)
	 */
	@Test()
	public void negativeFindByCategoryIdTest() {
		final Object testingData[][] = {
			{
				"user", "user4", "findByCategoryId", false, "category1", 1, 3, 4, 1, IllegalArgumentException.class
			}, {
				null, null, "findByCategoryId", false, "category1", 1, 2, 2, 2, IllegalArgumentException.class
			}, {
				"user", "user1", "findByCategoryId", true, "category1", 2, 1, 2, 2, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findByCategoryIdAllPublics de la category1 logeados como user4 que es menor de edad mirando la primera página con tamaño 1 (no salta excepción)
	 * 2.Hacemos el findByCategoryIdAllPublics de la category2 logeados como user4 que es menor de edad mirando la primera página con tamaño 1 (no salta excepción)
	 * 3.Hacemos el findByCategoryIdAllPublics de la category3 logeados como user4 que es menor de edad mirando la primera página con tamaño 1 (no salta excepción)
	 * 4.Hacemos el findByCategoryIdAllPublics de la category1 logeados como manager1 que es menor de edad mirando la primera página con tamaño 1 (no salta excepción)
	 * 5.Hacemos el findByCategoryIdAllPublics de la category1 logeados como admin que es menor de edad mirando la primera página con tamaño 1 (no salta excepción)
	 * 6.Hacemos el findByCategoryIdAllPublics de la category1 logeados como user3 que es menor de edad mirando la primera página con tamaño 1 (no salta excepción)
	 * 
	 * Requisitos
	 * B.10.1:List the rendezvouses in the system grouped by category.
	 */
	@Test()
	public void positiveFindByCategoryIdAllPublicsTest() {
		final Object testingData[][] = {
			{
				"user", "user4", "findByCategoryIdAllPublics", false, "category1", 1, 1, 1, 1, null
			}, {
				"user", "user4", "findByCategoryIdAllPublics", false, "category2", 1, 1, 1, 1, null
			}, {
				"user", "user4", "findByCategoryIdAllPublics", false, "category3", 1, 1, 1, 1, null
			}, {
				"manager", "manager1", "findByCategoryIdAllPublics", false, "category1", 1, 1, 1, 1, null
			}, {
				"admin", "admin", "findByCategoryIdAllPublics", false, "category1", 1, 1, 1, 1, null
			}, {
				"user", "user3", "findByCategoryIdAllPublics", false, "category3", 1, 1, 1, 1, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findByCategoryIdAllPublics de una categoria con id 0 (salta un IllegalArgumentException)
	 */
	@Test()
	public void negativeFindByCategoryIdAllPublicsTest() {
		final Object testingData[][] = {
			{
				"user", "user4", "findByCategoryIdAllPublics", true, "category1", 1, 1, 1, 1, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	// Ancillary methods ------------------------------------------------------

	protected void template(final String user, final String username, final String method, final boolean falseId, final String bean, final Integer page, final Integer size, final Integer tamPagination, final Integer numTotalPages, final Class<?> expected) {
		Class<?> caught;
		Collection<Rendezvous> rendezvouses;
		int creatorId, creatorIdAux, attendantId, attendantIdAux, linkerRendezvousId, linkerRendezvousIdAux, rendezvousId, rendezvousIdAux, categoryId, categoryIdAux, pagesNumber, totalPages;

		Boolean isFindAll;
		Rendezvous rendezvous;

		caught = null;
		rendezvouses = null;
		try {
			if (user != null)
				super.authenticate(username); //Nos logeamos si es necesario

			//Este template está formado por secciones cada una siendo un tipo de listado concreto, estando estas secciones marcada con comentarios en la condición correspondiente
			//así que por cada tipo de test entra a una sección en concreto. Cada sección se se basa en aplicar el listado correspondientes accediendo previamente a otros listados
			// si es necesario para simular un caso cogiendo el id o la entidad correspondiente			
			if (method.equals("findAll"))
				rendezvouses = this.rendezvousService.findAll(); //------

			else if (method.equals("findByCreatorId")) { //----- FindByCreatorId -------
				if (falseId == false) {
					creatorIdAux = super.getEntityId(bean);
					pagesNumber = this.userService.countAllPaginated();
					pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
					if (user == null || user.equals("user"))
						creatorId = this.findAllUser(creatorIdAux, pagesNumber); //Buscamos el user cogiéndolo de la lista completa de users si es un user
					else
						creatorId = super.getEntityId(bean); //Si no es un user lo cogemos directamente
				} else
					creatorId = 0;
				rendezvouses = this.rendezvousService.findByCreatorId(creatorId, page, tamPagination); //Cogemos los rendezvouses del creador
				totalPages = this.rendezvousService.countByCreatorId(creatorId);
				totalPages = (int) Math.floor(((totalPages / (tamPagination + 0.0)) - 0.1) + 1);
				Assert.isTrue(totalPages == numTotalPages); //----------------

			} else if (method.equals("findByCreatorIdAllPublics")) { //------FindByCreatorIdAllPublics ---------
				if (falseId == false) {
					creatorIdAux = super.getEntityId(bean);
					pagesNumber = this.userService.countAllPaginated();
					pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
					if (user == null || user.equals("user"))
						creatorId = this.findAllUser(creatorIdAux, pagesNumber); //Cogemos la id del usuario que queremos
					else
						creatorId = super.getEntityId(bean); //Si no se coge directamente
				} else
					creatorId = 0;
				rendezvouses = this.rendezvousService.findByCreatorIdAllPublics(creatorId, page, tamPagination); //Se cogen todos los rendezvouses publicos del usuario
				totalPages = this.rendezvousService.countByCreatorIdAllPublics(creatorId);
				totalPages = (int) Math.floor(((totalPages / (tamPagination + 0.0)) - 0.1) + 1);
				Assert.isTrue(totalPages == numTotalPages); //----------------------

			} else if (method.equals("findByAttendantId")) { //-----FindByAttendantId------
				if (falseId == false) {
					attendantIdAux = super.getEntityId(bean);
					pagesNumber = this.userService.countAllPaginated();
					pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
					if (user == null || user.equals("user"))
						attendantId = this.findAllUser(attendantIdAux, pagesNumber); //Se coge la id del attendant
					else
						attendantId = super.getEntityId(bean);
				} else
					attendantId = 0;
				rendezvouses = this.rendezvousService.findByAttendantId(attendantId, page, tamPagination); //Se cogen todos los rendezvouses del attendant cogido
				totalPages = this.rendezvousService.countByAttendantId(attendantId);
				totalPages = (int) Math.floor(((totalPages / (tamPagination + 0.0)) - 0.1) + 1);
				Assert.isTrue(totalPages == numTotalPages); //----------------

			} else if (method.equals("findByAttendantIdAllPublics")) { //-----FindByAttendantIdAllPublics------
				if (falseId == false) {
					attendantIdAux = super.getEntityId(bean);
					pagesNumber = this.userService.countAllPaginated();
					pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
					if (user == null || user.equals("user"))
						attendantId = this.findAllUser(attendantIdAux, pagesNumber); //Se coge la id del attendant
					else
						attendantId = super.getEntityId(bean);
				} else
					attendantId = 0;
				rendezvouses = this.rendezvousService.findByAttendantIdAllPublics(attendantId, page, tamPagination); //Se cogen todos los rendezvouses publico del attendant cogido
				totalPages = this.rendezvousService.countByAttendantIdAllPublics(attendantId);
				totalPages = (int) Math.floor(((totalPages / (tamPagination + 0.0)) - 0.1) + 1);
				Assert.isTrue(totalPages == numTotalPages); //----------------

			} else if (method.equals("findByLinkerRendezvousId")) { //-----FindByLinkerRendezvousId------
				if (falseId == false) {
					linkerRendezvousIdAux = super.getEntityId(bean);
					if (username == null || username.equals("user4")) {
						pagesNumber = this.rendezvousService.countAllPublics(); //Se coge el rendezvous entre los públicos si eres menor de edad
						isFindAll = false;
					} else {
						pagesNumber = this.rendezvousService.countAllPaginated();//Se coge el rendezvous entre todos
						isFindAll = true;
					}
					pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
					linkerRendezvousId = this.findAllRendezvous(linkerRendezvousIdAux, pagesNumber, isFindAll); //Cogemos el id del rendezvous correspondiente
				} else
					linkerRendezvousId = 0;
				rendezvouses = this.rendezvousService.findByLinkerRendezvousId(linkerRendezvousId, page, tamPagination); //Cogemos los rendezvouses linkeados al rendezvous
				totalPages = this.rendezvousService.countByLinkerRendezvousId(linkerRendezvousId);
				totalPages = (int) Math.floor(((totalPages / (tamPagination + 0.0)) - 0.1) + 1);
				Assert.isTrue(totalPages == numTotalPages); //----------------

			} else if (method.equals("findByLinkerRendezvousAllPublicsId")) { //-----FindByLinkerRendezvousAllPublicsId------
				if (falseId == false) {
					linkerRendezvousIdAux = super.getEntityId(bean);
					if (username == null || username.equals("user4")) {
						pagesNumber = this.rendezvousService.countAllPublics(); //Se coge el rendezvous entre todos los públicos si eres menor de edad
						isFindAll = false;
					} else {
						pagesNumber = this.rendezvousService.countAllPaginated();//Se coge el rendezvous entre todos
						isFindAll = true;
					}
					pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
					linkerRendezvousId = this.findAllRendezvous(linkerRendezvousIdAux, pagesNumber, isFindAll); //Cogemos el id del rendezvous correspondiente
				} else
					linkerRendezvousId = 0;
				rendezvouses = this.rendezvousService.findByLinkerRendezvousIdAndAllpublics(linkerRendezvousId, page, tamPagination); //Se cogen todos los rendezvouses públicos linkeados al rendezvous
				totalPages = this.rendezvousService.countByLinkerRendezvousIdAndAllpublics(linkerRendezvousId);
				totalPages = (int) Math.floor(((totalPages / (tamPagination + 0.0)) - 0.1) + 1);
				Assert.isTrue(totalPages == numTotalPages); //----------------

			} else if (method.equals("findLinkerRendezvousesByRendezvousId")) { //-----FindLinkerRendezvousesByRendezvousId------
				if (falseId == false) {
					rendezvousIdAux = super.getEntityId(bean);
					if (username == null || username.equals("user4")) {
						pagesNumber = this.rendezvousService.countAllPublics(); //Se coge entre todos los rendezvouses públicos
						isFindAll = false;
					} else {
						pagesNumber = this.rendezvousService.countAllPaginated();//Se coge entre todos los rendezvouses
						isFindAll = true;
					}
					pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
					rendezvousId = this.findAllRendezvous(rendezvousIdAux, pagesNumber, isFindAll);//Se coge el id correspondiente al rendezvous
				} else
					rendezvousId = 0;
				rendezvouses = this.rendezvousService.findLinkerRendezvousesByRendezvousId(rendezvousId, page, tamPagination);//Cogemos los rendezvous que linkean mi rendezvous
				totalPages = this.rendezvousService.countLinkerRendezvousesByRendezvousId(rendezvousId);
				totalPages = (int) Math.floor(((totalPages / (tamPagination + 0.0)) - 0.1) + 1);
				Assert.isTrue(totalPages == numTotalPages); //----------------

			} else if (method.equals("findLinkerRendezvousesAllPublicsByRendezvousId")) { //-----FindLinkerRendezvousesAllPublicsByRendezvousId------
				if (falseId == false) {
					rendezvousIdAux = super.getEntityId(bean);
					if (username == null || username.equals("user4")) {
						pagesNumber = this.rendezvousService.countAllPublics();//Se coge el rendezvous entre todos los públicos si eres menor de edad
						isFindAll = false;
					} else {
						pagesNumber = this.rendezvousService.countAllPaginated();//Se coge el rendezvous entre todos los rendezvouses
						isFindAll = true;
					}
					pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
					rendezvousId = this.findAllRendezvous(rendezvousIdAux, pagesNumber, isFindAll); //Se coge la id del rendezvous correspondiente
				} else
					rendezvousId = 0;
				rendezvouses = this.rendezvousService.findLinkerRendezvousesAllPublicsByRendezvousId(rendezvousId, page, tamPagination); //Se coge los rendezvouses públicos que linkean a mi rendezvous
				totalPages = this.rendezvousService.countLinkerRendezvousesAllPublicsByRendezvousId(rendezvousId);
				totalPages = (int) Math.floor(((totalPages / (tamPagination + 0.0)) - 0.1) + 1);
				Assert.isTrue(totalPages == numTotalPages); //----------------

			} else if (method.equals("findAllPublics")) { //-----FindAllPublics------
				rendezvouses = this.rendezvousService.findAllPublics(page, tamPagination);
				totalPages = this.rendezvousService.countAllPublics();	//Cogemos todos los rendezvouses públicos
				totalPages = (int) Math.floor(((totalPages / (tamPagination + 0.0)) - 0.1) + 1);
				Assert.isTrue(totalPages == numTotalPages); //----------------
			} else if (method.equals("findAllPaginated")) { //-----FindAllPaginated------
				rendezvouses = this.rendezvousService.findAllPaginated(page, tamPagination);
				totalPages = this.rendezvousService.countAllPaginated(); //Cogemos todos los rendezvouses
				totalPages = (int) Math.floor(((totalPages / (tamPagination + 0.0)) - 0.1) + 1);
				Assert.isTrue(totalPages == numTotalPages); //----------------
			} else if (method.equals("findNotLinkedByRendezvous")) { //-----findNotLinkedByRendezvous------
				if (falseId == false) {
					rendezvousIdAux = super.getEntityId(bean);
					if (username == null || username.equals("user4")) {
						pagesNumber = this.rendezvousService.countAllPublics(); //Cogemos el rendezvous entre todos los públicos
						isFindAll = false;
					} else {
						pagesNumber = this.rendezvousService.countAllPaginated();//Cogemos el rendezvous entre todos
						isFindAll = true;
					}
					pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
					rendezvousId = this.findAllRendezvous(rendezvousIdAux, pagesNumber, isFindAll); //Cogemos la id del rendezvous correspondiente
					rendezvous = this.rendezvousService.findOne(rendezvousId);
				} else
					rendezvous = null;
				rendezvouses = this.rendezvousService.findNotLinkedByRendezvous(rendezvous, page, tamPagination); //Cogemos los rendezvouses que no están linkeado con mi rendezvous
				totalPages = this.rendezvousService.countNotLinkedByRendezvous(rendezvous);
				totalPages = (int) Math.floor(((totalPages / (tamPagination + 0.0)) - 0.1) + 1);
				Assert.isTrue(totalPages == numTotalPages); //----------------

			} else if (method.equals("findNotLinkedByRendezvousAllPublics")) { //-----findNotLinkedByRendezvousAllPublics------
				if (falseId == false) {
					rendezvousIdAux = super.getEntityId(bean);
					if (username == null || username.equals("user4")) {
						pagesNumber = this.rendezvousService.countAllPublics(); //Cogemos el rendezvous entre todos los rendezvouses públicos
						isFindAll = false;
					} else {
						pagesNumber = this.rendezvousService.countAllPaginated(); //Cogemos el rendezvous entre todos los rendezvouses
						isFindAll = true;
					}
					pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
					rendezvousId = this.findAllRendezvous(rendezvousIdAux, pagesNumber, isFindAll); //Cogemos la id del rendezvous correspondiente
					rendezvous = this.rendezvousService.findOne(rendezvousId);
				} else
					rendezvous = null;
				rendezvouses = this.rendezvousService.findNotLinkedByRendezvousAllPublics(rendezvous, page, tamPagination); //Cogemos los rendezvouses para todos los públicos que no están logeados con mi rendezvous
				totalPages = this.rendezvousService.countNotLinkedByRendezvousAllPublics(rendezvous);
				totalPages = (int) Math.floor(((totalPages / (tamPagination + 0.0)) - 0.1) + 1);
				Assert.isTrue(totalPages == numTotalPages); //----------------

			} else if (method.equals("findByCategoryId")) { //-----FindByCategoryId------
				if (falseId == false) {
					categoryIdAux = super.getEntityId(bean);
					pagesNumber = this.categoryService.findAllPaginated(1, 5).getTotalPages();
					categoryId = this.findAllCategory(categoryIdAux, pagesNumber); //Cogemos la id de la categoria entre todas
				} else
					categoryId = 0;
				rendezvouses = this.rendezvousService.findByCategoryId(categoryId, page, tamPagination); //Cogemos todos los rendezvouses asociados a la categoria
				totalPages = this.rendezvousService.countByCategoryId(categoryId);
				totalPages = (int) Math.floor(((totalPages / (tamPagination + 0.0)) - 0.1) + 1);
				Assert.isTrue(totalPages == numTotalPages); //----------------

			} else if (method.equals("findByCategoryIdAllPublics")) { //-----FindByCategoryIdAllPublics------
				if (falseId == false) {
					categoryIdAux = super.getEntityId(bean);
					pagesNumber = this.categoryService.findAllPaginated(1, 5).getTotalPages(); //Cogemos la id de la categoria entre todas
					categoryId = this.findAllCategory(categoryIdAux, pagesNumber);
				} else
					categoryId = 0;
				rendezvouses = this.rendezvousService.findByCategoryIdAllPublics(categoryId, page, tamPagination); //Cogemos todos los rendezvouses publicos asocados a la categoría
				totalPages = this.rendezvousService.countByCategoryIdAllPublics(categoryId);
				totalPages = (int) Math.floor(((totalPages / (tamPagination + 0.0)) - 0.1) + 1);
				Assert.isTrue(totalPages == numTotalPages); //----------------

			}

			Assert.isTrue(rendezvouses.size() == size); //Comparamos siempre el tamaño de la lista con el esperado
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}
	private Integer findAllUser(final int userId, final int pagesNumber) {
		Integer result;

		result = 0;
		for (int i = 1; i <= pagesNumber; i++)
			for (final User u : this.userService.findAllPaginated(i, 5))
				if (userId == u.getId()) {
					result = u.getId();
					break;
				}

		return result;

	}

	private Integer findAllRendezvous(final int rendezvousId, final int pagesNumber, final boolean isFindAll) {
		Integer result;

		result = 0;
		for (int i = 1; i <= pagesNumber; i++)
			if (isFindAll == true) {
				for (final Rendezvous r : this.rendezvousService.findAllPaginated(i, 5))
					if (rendezvousId == r.getId()) {
						result = r.getId();
						break;
					}
			} else
				for (final Rendezvous r : this.rendezvousService.findAllPublics(i, 5))
					if (rendezvousId == r.getId()) {
						result = r.getId();
						break;
					}
		return result;

	}
	private Integer findAllCategory(final int categoryId, final int pagesNumber) {
		Integer result;

		result = 0;
		for (int i = 1; i <= pagesNumber; i++)
			for (final Category c : this.categoryService.findAllPaginated(i, 5))
				if (categoryId == c.getId()) {
					result = c.getId();
					break;
				}

		return result;

	}
}
