
package usecases;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ServicioService;
import utilities.AbstractTest;
import domain.Servicio;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ListServicioTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private ServicioService	servicioService;


	// Tests ------------------------------------------------------------------

	//Pruebas
	/*
	 * Probamos el findAll como user1
	 * Probamos el findAll como manager1
	 * Probamos el findAll como admin
	 * Probamos el findByCategoryId como user1
	 * Probamos el findByCategoryId como manager1
	 * Probamos el findByCategoryId como admin
	 * Probamos el topBestSellingService como admin
	 * Probamos la primera página de un listado de 6 servicios
	 * Probamos la segunda página de un listado de 6 servicios
	 * Probamos la primera página de un listado de 5 servicios
	 */

	//Pruebas
	/*
	 * Intentamos hacer el findByCategpryId como un usario no logeado
	 * Intentamos hacer el findByCategoryId con una categoria de id 0
	 * Intentamos hacer el topBestSellingService logeado como un user
	 * Intentamos hacer el topBestSellingService sin estar logeado
	 * Intentamos hacer el topBestSellingService poniendo un tamaño no permitido
	 * Intentamos hacer el findServicesForRequetsByRendezvousId logeados como un usuario que no sea un user
	 * Intentamos hacer el findServicesForRequestByRendezvousId con el id de un rendezvous que no es del usuario logeado
	 * Intentamos hacer el findServicesForRequestByRendezvousId con un rendezvous borrado del usuario
	 * Intentamos hacer el findServicesForRequestByRendezvousId con un rendezvous de id 0
	 */

	@Test()
	public void testFindAll() {
		final Object testingData[][] = {
			{
				"user", "user1", "findAll", false, null, null, 7, null, null
			}, {
				"manager", "manager1", "findAll", false, null, null, 7, null, null
			}, {
				"admin", "admin", "findAll", false, null, null, 7, null, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Class<?>) testingData[i][8]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test()
	public void testFindByCategoryId() {
		final Object testingData[][] = {
			{
				"user", "user1", "findByCategoryId", false, null, null, 3, null, null
			}, {
				"manager", "manager1", "findByCategoryId", false, null, null, 3, null, null
			}, {
				"admin", "admin", "findByCategoryId", false, null, null, 3, null, null
			}, {
				null, null, "findByCategoryId", false, null, null, 3, null, IllegalArgumentException.class
			}, {
				"manager", "manager1", "findByCategoryId", true, null, null, 3, null, IllegalArgumentException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Class<?>) testingData[i][8]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test()
	public void testTopBestSellingServices() {
		final Object testingData[][] = {
			{
				"admin", "admin", "topBestSellingService", false, null, null, 5, 5, null
			}, {
				"admin", "admin", "topBestSellingService", false, null, null, 7, 10, null
			}, {
				"admin", "admin", "topBestSellingService", false, null, null, 7, 20, null
			}, {
				"user", "user1", "topBestSellingService", false, null, null, 5, 5, IllegalArgumentException.class
			}, {
				null, null, "topBestSellingService", false, null, null, 5, 5, IllegalArgumentException.class
			}, {
				"admin", "admin", "topBestSellingService", false, null, null, 8, 7, IllegalArgumentException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Class<?>) testingData[i][8]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test()
	public void testFindServicesForRequetsByRendezvousId() {
		final Object testingData[][] = {
			{
				"user", "user1", "findServicesForRequetsByRendezvousId", false, "rendezvous1", 1, 5, null, null
			}, {
				"user", "user1", "findServicesForRequetsByRendezvousId", false, "rendezvous1", 2, 1, null, null
			}, {
				"user", "user1", "findServicesForRequetsByRendezvousId", false, "rendezvous9", 1, 5, null, null
			}, {
				"admin", "admin", "findServicesForRequetsByRendezvousId", false, "rendezvous1", 1, 5, null, IllegalArgumentException.class
			}, {
				"user", "user2", "findServicesForRequetsByRendezvousId", false, "rendezvous1", 2, 1, null, IllegalArgumentException.class
			}, {
				"user", "user3", "findServicesForRequetsByRendezvousId", false, "rendezvous4", 1, 5, null, IllegalArgumentException.class
			}, {
				"user", "user1", "findServicesForRequetsByRendezvousId", true, "rendezvous9", 1, 5, null, IllegalArgumentException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Class<?>) testingData[i][8]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test()
	public void testFindAllPaginated() {
		final Object testingData[][] = {
			{
				"user", "user1", "findAllPaginated", false, null, 1, 5, 2, 5, null
			}, {
				"user", "user1", "findAllPaginated", false, null, 2, 2, 2, 5, null
			}, {
				"user", "user1", "findAllPaginated", false, null, 1, 7, 1, 7, null
			}, {
				"user", "user1", "findAllPaginated", false, null, 2, 0, 1, 7, null
			}, {
				"manager", "manager1", "findAllPaginated", false, null, 2, 2, 2, 5, null
			}, {
				"admin", "admin", "findAllPaginated", false, null, 2, 2, 2, 5, null
			}, {
				null, null, "findAllPaginated", false, null, 1, 5, 2, 5, IllegalArgumentException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template2((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}

	}
	/*
	 * Hacemos el findAllPaginated mirando la página 0 al paginar con un tamaño de 5
	 * Hacemos el findAllPaginated mirando la página 1 al paginar con un tamaño de 5
	 * Hacemos el findAllPaginated mirando la página 0 al paginar con un tamaño de 7
	 * Hacemos el findAllPaginated mirando la página 1 al paginar con un tamaño de 7
	 */
	//	@Test()
	//	public void positivePaginatedTest() {
	//		final Object testingData[][] = {
	//			{
	//				"user", "user1", "findAllPaginated", false, null, 0, 5, 2, 5, null
	//			}, {
	//				"user", "user1", "findAllPaginated", false, null, 1, 2, 2, 5, null
	//			}, {
	//				"user", "user1", "findAllPaginated", false, null, 0, 7, 1, 7, null
	//			}, {
	//				"user", "user1", "findAllPaginated", false, null, 1, 0, 1, 7, null
	//			}, {
	//				"manager", "manager1", "findAllPaginated", false, null, 1, 2, 2, 5, null
	//			}, {
	//				"admin", "admin", "findAllPaginated", false, null, 1, 2, 2, 5, null
	//			}, {
	//				"admin", "admin", "findAll", false, null, null, 7, null
	//			}, {
	//				"user", "user1", "findByCategoryId", false, null, null, 3, null
	//			}, {
	//				"manager", "manager1", "findByCategoryId", false, null, null, 3, null
	//			}, {
	//				"admin", "admin", "findByCategoryId", false, null, null, 3, null
	//			}, {
	//				"admin", "admin", "topBestSellingService", false, null, null, 5, null
	//			}, {
	//				"user", "user1", "findServicesForRequetsByRendezvousId", false, "rendezvous1", 1, 5, null
	//			}, {
	//				"user", "user1", "findServicesForRequetsByRendezvousId", false, "rendezvous1", 2, 1, null
	//			}, {
	//				"user", "user1", "findServicesForRequetsByRendezvousId", false, "rendezvous9", 1, 5, null
	//			}
	//		};
	//
	//			for (int i = 0; i < testingData.length; i++)
	//				try {
	//					System.out.println(i);
	//					super.startTransaction();
	//					this.template2((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
	//						(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
	//				} catch (final Throwable oops) {
	//					throw new RuntimeException(oops);
	//				} finally {
	//					super.rollbackTransaction();
	//				}
	//	
	//		}

	// Ancillary methods ------------------------------------------------------

	protected void template(final String user, final String username, final String method, final boolean falseId, final String bean, final Integer page, final Integer size, final Integer tamTop, final Class<?> expected) {
		Class<?> caught;
		Collection<Servicio> servicios;
		int categoryId;
		int rendezvousId;

		caught = null;
		servicios = null;
		try {
			if (user != null)
				super.authenticate(username);

			if (method.equals("findAll"))
				servicios = this.servicioService.findAll();
			else if (method.equals("findByCategoryId")) {
				if (falseId == false)
					categoryId = super.getEntityId("category1");
				else
					categoryId = 0;
				servicios = this.servicioService.findByCategoryId(categoryId);
			} else if (method.equals("topBestSellingService"))
				servicios = this.servicioService.topBestSellingServices(tamTop);
			else if (method.equals("findServicesForRequetsByRendezvousId")) {
				if (falseId == false)
					rendezvousId = super.getEntityId(bean);
				else
					rendezvousId = 0;
				servicios = this.servicioService.findServicesForRequetsByRendezvousId(rendezvousId, page);
			}
			Assert.isTrue(servicios.size() == size);
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
		super.checkExceptions(expected, caught);
	}

	protected void template2(final String user, final String username, final String method, final boolean falseId, final String bean, final Integer page, final Integer size, final Integer totalPage, final Integer tam, final Class<?> expected) {
		Class<?> caught;
		Page<Servicio> servicios;
		//		final int categoryId;
		//		final int rendezvousId;

		caught = null;
		servicios = null;
		try {
			if (user != null)
				super.authenticate(username);

			if (method.equals("findAllPaginated"))
				servicios = this.servicioService.findAllPaginated(page, tam);
			//			else if (method.equals("findByCategoryId")) {
			//				if (falseId == false)
			//					categoryId = super.getEntityId("category1");
			//				else
			//					categoryId = 0;
			//				servicios = this.servicioService.findByCategoryId(categoryId);
			//			} else if (method.equals("topBestSellingService"))
			//				servicios = this.servicioService.topBestSellingServices(size);
			//			else if (method.equals("findServicesForRequetsByRendezvousId")) {
			//				if (falseId == false)
			//					rendezvousId = super.getEntityId(bean);
			//				else
			//					rendezvousId = 0;
			//				servicios = this.servicioService.findServicesForRequetsByRendezvousId(rendezvousId, page);
			//			}
			Assert.isTrue(servicios.getContent().size() == size);
			Assert.isTrue(servicios.getTotalPages() == totalPage);

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
		super.checkExceptions(expected, caught);
	}
}
