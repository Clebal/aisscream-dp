
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

import security.LoginService;
import services.ManagerService;
import services.RendezvousService;
import services.ServiceService;
import services.UserService;
import utilities.AbstractTest;
import domain.Manager;
import domain.Rendezvous;
import domain.Service;
import domain.User;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ListServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private ServiceService		serviceService;

	@Autowired
	private ManagerService		managerService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private UserService			userService;


	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 1. Probamos el findAll estando logeados como user
	 * 2. Probamos el findAll estando logeados como manager
	 * 3. Probamos el findAll estando logeados como admin
	 * 
	 * Requisitos:
	 * C.4.2: An actor who is authenticated as a user must be able to list the services that are available in the system.
	 * C.5.1: An actor who is registered as a manager must be able to list the services that are available in the system.
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

	/*
	 * Pruebas:
	 * 1.Probamos el findByCategoryId como user
	 * 2.Probamos el findByCategoryId como manager
	 * 3.Probamos el findByCategoryd como admin
	 * 4.Probamos el findByCategoryId sin estar logeado (salta una excepci�n)
	 * 5.Probamos el findByCategoryId usando categoryId igual a 0 (salta una excepci�n)
	 */
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

	/*
	 * Pruebas:
	 * 1.Probamos el topBestSellingService como admin usando de tama�o 5
	 * 2.Probamos el topBestSellingService como admin usando de tama�o 10
	 * 3.Probamos el topBestSellingService como admin usando de tama�o 20
	 * 4.Probamos el topBestSellingService logeados como user (salta una excepci�n)
	 * 5.Probamos el topBestSellingService sin estar logeado (salta una excepci�n)
	 * 6. Probamos el topBestSellingService logeados como admin usando un tama�o del top distinto de 5, 10 o 20 (salta una excepci�n)
	 * 
	 * Requisitos:
	 * B.11.1.4: An actor who is authenticated as an administrator must be able to display a dashboard with the following information: The top-selling services
	 */
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

	/*
	 * Pruebas
	 * 1. Probamos el findServicesForRequetsByRendezvousId con el rendezvous1 la primera p�gina
	 * 2. Probamos el findServicesForRequestByRendezvousId con el rendezvous1 la segunda p�gina
	 * 3. Probamos el findServicesForRequestByRendezvousId con el rendezvous9 la primera p�gina
	 * 4. Probamos el findServicesForRequestByRendezvousId con el rendezvous1 logeados como admin (salta una excepci�n)
	 * 5. Probamos el findServicesForRequestByRendezvousId con el rendezvous1 con un usuario que no es su creador (salta una excepci�n)
	 * 6. Probamos el findServicesForRequestByRendezvousId con un rendezvous borrado (salta una excepci�n)
	 * 7. Probamos el findServicesForRequestByRendezvousId usando 0 como rendezvousId (salta una excepci�n)
	 * 
	 * Requisitos:
	 * C.4.3: An actor who is authenticated as a user must be able to request a service for one of the rendezvouses that he or she is created
	 */
	@Test()
	public void testFindServicesForRequetsByRendezvousId() {
		final Object testingData[][] = {
			{
				"user", "user1", "findServicesForRequetsByRendezvousId", false, "rendezvous1", 1, 5, 2, null
			}, {
				"user", "user1", "findServicesForRequetsByRendezvousId", false, "rendezvous1", 2, 1, 2, null
			}, {
				"user", "user1", "findServicesForRequetsByRendezvousId", false, "rendezvous9", 1, 5, 1, null
			}, {
				"admin", "admin", "findServicesForRequetsByRendezvousId", false, "rendezvous1", 1, 5, 2, IllegalArgumentException.class
			}, {
				"user", "user2", "findServicesForRequetsByRendezvousId", false, "rendezvous1", 2, 1, 2, IllegalArgumentException.class
			}, {
				"user", "user3", "findServicesForRequetsByRendezvousId", false, "rendezvous4", 1, 5, 1, IllegalArgumentException.class
			}, {
				"user", "user1", "findServicesForRequetsByRendezvousId", true, "rendezvous9", 1, 5, 1, IllegalArgumentException.class
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
	/*
	 * Pruebas
	 * 1. Probamos el findAllPaginated logeados como user la primera p�gina con un tama�o de 5
	 * 2. Probamos el findAllPaginated logeados como user la segunda p�gina con un tama�o de 5
	 * 3. Probamos el findAllPaginated logeados como user la primera p�gina con un tama�o de 7
	 * 4. Probamos el findAllPaginated logeados como user la segunda p�gina con un tama�o de 7
	 * 5. Probamos el findAllPaginated logeados como manager la primera p�gina con un tama�o de 5
	 * 6. Probamos el findAllPaginated logeados como admin la primera p�gina con un tama�o de 5
	 * 7. Probamos el findAllPaginated sin estar logeados (salta una excepci�n)
	 * 
	 * Requisitos:
	 * C.4.2: An actor who is authenticated as a user must be able to list the services that are available in the system.
	 * C.5.1: An actor who is registered as a manager must be able to list the services that are available in the system.
	 */
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
	 * Pruebas:
	 * 1. Probamos el findByManagerUserAccountId logeados como manager la primera p�gina con un tama�o de 5
	 * 2. Probamos el findByManagerUserAccountId logeados como manager la primera p�gina con un tama�o de 2
	 * 3. Probamos el findByManagerUserAccountId logeados como user la primera p�gina con un tama�o de 2
	 * 4. Probamos el findByManagerUserAccountId logeados como admin la primera p�gina con un tama�o de 2
	 * 5. Probamos el findByManagerUserAccountId logeados como manager la primera p�gina con un tama�o de 1
	 * 6. Probamos el findByManagerUserAccountId logeados como manager la segunda p�gina con un tama�o de 1
	 * 7. Probamos el findByManagerUserAccountId logeados como manager la segunda p�gina con un tama�o de 5
	 * 8. Probamos el findByManagerUserAccountId sin estar logeados (salta una excepci�n)
	 * 9. Probamos el findByManagerUserAccountId usando como managerUserAccountId 0 (salta una excepci�n)
	 * 
	 * Requisitos:
	 * C.5.2: An actor who is registered as a manager must be able to manage his or her services, which includes listing them
	 */
	@Test()
	public void testFindByManagerUserAccountId() {
		final Object testingData[][] = {
			{
				"manager", "manager1", "findByManagerUserAccountId", false, "manager1", 1, 2, 1, 5, null
			}, {
				"manager", "manager1", "findByManagerUserAccountId", false, "manager1", 1, 2, 1, 2, null
			}, {
				"user", "user1", "findByManagerUserAccountId", false, "manager1", 1, 2, 1, 2, null
			}, {
				"admin", "admin", "findByManagerUserAccountId", false, "manager1", 1, 2, 1, 2, null
			}, {
				"manager", "manager1", "findByManagerUserAccountId", false, "manager1", 1, 1, 2, 1, null
			}, {
				"manager", "manager1", "findByManagerUserAccountId", false, "manager1", 2, 1, 2, 1, null
			}, {
				"manager", "manager1", "findByManagerUserAccountId", false, "manager1", 2, 0, 1, 5, null
			}, {
				null, "manager1", "findByManagerUserAccountId", false, "manager1", 2, 0, 1, 5, IllegalArgumentException.class
			}, {
				"manager", "manager1", "findByManagerUserAccountId", true, "manager1", 2, 0, 1, 5, IllegalArgumentException.class
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
	 * Pruebas
	 * 1. Probamos el findByCategoryId con la category1 logeados como manager la primera p�gina con un tama�o de 5
	 * 2. Probamos el findByCategoryId con la category1 logeados como user la primera p�gina con un tama�o de 5
	 * 3. Probamos el findByCategoryId con la category1 logeados como admin la primera p�gina con un tama�o de 5
	 * 4. Probamos el findByCategoryId con la category1 logeados como manager la primera p�gina con un tama�o de 3
	 * 5. Probamos el findByCategoryId con la category1 logeados como manager la primera p�gina con un tama�o de 2
	 * 6. Probamos el findByCategoryId con la category1 logeados como manager la segunda p�gina con un tama�o de 2
	 * 7. Probamos el findByCategoryId con la category1 logeados como manager la tercera p�gina con un tama�o de 2
	 * 8. Probamos el findByCategoryId con la category3 logeados como manager la primera p�gina con un tama�o de 7
	 * 9. Probamos el findByCategoryId con la category1 sin estar logeado (salta una excepci�n)
	 * 10.Probamos el findByCategoryId con la categoryId 0 (salta una excepci�n)
	 */
	@Test()
	public void testFindByCategoryIdPaginated() {
		final Object testingData[][] = {
			{
				"manager", "manager1", "findByCategoryId", false, "category1", 1, 3, 1, 5, null
			}, {
				"user", "user1", "findByCategoryId", false, "category1", 1, 3, 1, 5, null
			}, {
				"admin", "admin", "findByCategoryId", false, "category1", 1, 3, 1, 5, null
			}, {
				"manager", "manager1", "findByCategoryId", false, "category1", 1, 3, 1, 3, null
			}, {
				"manager", "manager1", "findByCategoryId", false, "category1", 1, 2, 2, 2, null
			}, {
				"manager", "manager1", "findByCategoryId", false, "category1", 2, 1, 2, 2, null
			}, {
				"manager", "manager1", "findByCategoryId", false, "category1", 3, 0, 2, 2, null
			}, {
				"manager", "manager1", "findByCategoryId", false, "category3", 1, 1, 1, 7, null
			}, {
				null, "manager1", "findByCategoryId", false, "category1", 1, 3, 1, 5, IllegalArgumentException.class
			}, {
				"manager1", "manager1", "findByCategoryId", true, "category1", 1, 3, 1, 5, IllegalArgumentException.class
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
	 * Pruebas
	 * 1. Probamos el findByRendezvousId con el rendezvous3 logeados como manager la primera p�gina con tama�o 5
	 * 2. Probamos el findByRendezvousId con el rendezvous3 logeados como user la primera p�gina con tama�o 5
	 * 3. Probamos el findByRendezvousId con el rendezvous3 logeados como admin la primera p�gina con tama�o 5
	 * 4. Probamos el findByRendezvousId con el rendezvous3 logeados como manager la primera p�gina con tama�o 3
	 * 5. Probamos el findByRendezvousId con el rendezvous3 logeados como manager la primera p�gina con tama�o 3
	 * 6. Probamos el findByRendezvousId con el rendezvous3 logeados como manager la segunda p�gina con tama�o 3
	 * 7. Probamos el findByRendezvousId con el rendezvous3 logeados como manager la tercera p�gina con tama�o 3
	 * 8. Probamos el findByRendezvousId con el rendezvous1 logeados como manager la primera p�gina con tama�o 5
	 * 9. Probamos el findByRendezvousId con el rendezvous3 sin estar logeado (salta una excepci�n)
	 * 10. Probamos el findByRendezvousId con el rendezvousId a 0 (salta una excepci�n)
	 */
	@Test()
	public void testFindByRendezvousId() {
		final Object testingData[][] = {
			{
				"manager", "manager1", "findByRendezvousId", false, "rendezvous3", 1, 3, 1, 5, null
			}, {
				"user", "user1", "findByRendezvousId", false, "rendezvous3", 1, 3, 1, 5, null
			}, {
				"admin", "admin", "findByRendezvousId", false, "rendezvous3", 1, 3, 1, 5, null
			}, {
				"manager", "manager1", "findByRendezvousId", false, "rendezvous3", 1, 3, 1, 3, null
			}, {
				"manager", "manager1", "findByRendezvousId", false, "rendezvous3", 1, 2, 2, 2, null
			}, {
				"manager", "manager1", "findByRendezvousId", false, "rendezvous3", 2, 1, 2, 2, null
			}, {
				"manager", "manager1", "findByRendezvousId", false, "rendezvous3", 3, 0, 2, 2, null
			}, {
				"manager", "manager1", "findByRendezvousId", false, "rendezvous1", 1, 0, 0, 5, null
			}, {
				null, "manager1", "findByRendezvousId", false, "rendezvous3", 1, 3, 1, 5, IllegalArgumentException.class
			}, {
				"manager1", "manager1", "findByRendezvousId", true, "rendezvous3", 1, 3, 1, 5, IllegalArgumentException.class
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

	// Ancillary methods ------------------------------------------------------

	protected void template(final String user, final String username, final String method, final boolean falseId, final String bean, final Integer page, final Integer size, final Integer tam, final Class<?> expected) {
		Class<?> caught;
		Collection<Service> services;
		int categoryId;
		int rendezvousId;
		int totalPages;
		User creator;
		int pagesNumber;

		caught = null;
		services = null;
		rendezvousId = 0;
		try {
			if (user != null)
				super.authenticate(username);

			if (method.equals("findAll"))
				services = this.serviceService.findAll();
			else if (method.equals("findByCategoryId")) {
				if (falseId == false)
					categoryId = super.getEntityId("category1");
				else
					categoryId = 0;
				services = this.serviceService.findByCategoryId(categoryId);
			} else if (method.equals("topBestSellingService"))
				services = this.serviceService.topBestSellingServices(tam);
			else if (method.equals("findServicesForRequetsByRendezvousId")) {
				if (falseId == false) {
					rendezvousId = super.getEntityId(bean);
					if (expected == null) {
						creator = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
						pagesNumber = this.rendezvousService.countByCreatorId(creator.getId());
						pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
						for (int i = 1; i <= pagesNumber; i++)
							for (final Rendezvous r : this.rendezvousService.findByCreatorId(creator.getId(), i, 5))
								if (rendezvousId == r.getId()) {
									rendezvousId = r.getId();
									break;
								}
					}
				} else
					rendezvousId = 0;
				services = this.serviceService.findServicesForRequetsByRendezvousId(rendezvousId, page);
				totalPages = this.serviceService.countFindServicesForRequetsByRendezvousId(rendezvousId);
				totalPages = (int) Math.floor(((totalPages / (5 + 0.0)) - 0.1) + 1);
				Assert.isTrue(totalPages == tam);
			}
			Assert.isTrue(services.size() == size);
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}
	protected void template2(final String user, final String username, final String method, final boolean falseId, final String bean, final Integer page, final Integer size, final Integer totalPage, final Integer tam, final Class<?> expected) {
		Class<?> caught;
		Page<Service> services;
		int managerId;
		Manager manager;
		int categoryId;
		int rendezvousId;
		int pagesNumber;

		caught = null;
		services = null;
		try {
			if (user != null)
				super.authenticate(username);

			if (method.equals("findAllPaginated"))
				services = this.serviceService.findAllPaginated(page, tam);
			else if (method.equals("findByManagerUserAccountId")) {
				managerId = super.getEntityId(bean);
				manager = this.managerService.findOne(managerId);
				if (falseId == false)
					services = this.serviceService.findByManagerUserAccountId(manager.getUserAccount().getId(), page, tam);
				else
					services = this.serviceService.findByManagerUserAccountId(0, page, tam);
			} else if (method.equals("findByCategoryId")) {
				categoryId = super.getEntityId(bean);
				if (falseId == false)
					services = this.serviceService.findByCategoryId(categoryId, page, tam);
				else
					services = this.serviceService.findByCategoryId(0, page, tam);
			} else if (method.equals("findByRendezvousId")) {
				rendezvousId = super.getEntityId(bean);
				if (falseId == false) {
					if (expected == null) {
						pagesNumber = this.rendezvousService.countAllPaginated();
						pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
						for (int i = 1; i <= pagesNumber; i++)
							for (final Rendezvous r : this.rendezvousService.findAllPaginated(i, 5))
								if (rendezvousId == r.getId()) {
									rendezvousId = r.getId();
									break;
								}
					}
					services = this.serviceService.findByRendezvousId(rendezvousId, page, tam);
				} else
					services = this.serviceService.findByRendezvousId(0, page, tam);
			}

			Assert.isTrue(services.getContent().size() == size);
			Assert.isTrue(services.getTotalPages() == totalPage);

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}
}
