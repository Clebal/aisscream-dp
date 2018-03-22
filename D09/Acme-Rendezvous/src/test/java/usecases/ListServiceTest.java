
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
	 * 4.Probamos el findByCategoryId sin estar logeado (salta una excepción)
	 * 5.Probamos el findByCategoryId usando categoryId igual a 0 (salta una excepción)
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
	 * 1.Probamos el topBestSellingService como admin usando de tamaño 5
	 * 2.Probamos el topBestSellingService como admin usando de tamaño 10
	 * 3.Probamos el topBestSellingService como admin usando de tamaño 20
	 * 4.Probamos el topBestSellingService logeados como user (salta una excepción)
	 * 5.Probamos el topBestSellingService sin estar logeado (salta una excepción)
	 * 6. Probamos el topBestSellingService logeados como admin usando un tamaño del top distinto de 5, 10 o 20 (salta una excepción)
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
	 * 1. Probamos el findServicesForRequetsByRendezvousId con el rendezvous1 la primera página
	 * 2. Probamos el findServicesForRequestByRendezvousId con el rendezvous1 la segunda página
	 * 3. Probamos el findServicesForRequestByRendezvousId con el rendezvous9 la primera página
	 * 4. Probamos el findServicesForRequestByRendezvousId con el rendezvous1 logeados como admin (salta una excepción)
	 * 5. Probamos el findServicesForRequestByRendezvousId con el rendezvous1 con un usuario que no es su creador (salta una excepción)
	 * 6. Probamos el findServicesForRequestByRendezvousId con un rendezvous borrado (salta una excepción)
	 * 7. Probamos el findServicesForRequestByRendezvousId usando 0 como rendezvousId (salta una excepción)
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
	 * 1. Probamos el findAllPaginated logeados como user la primera página con un tamaño de 5
	 * 2. Probamos el findAllPaginated logeados como user la segunda página con un tamaño de 5
	 * 3. Probamos el findAllPaginated logeados como user la primera página con un tamaño de 7
	 * 4. Probamos el findAllPaginated logeados como user la segunda página con un tamaño de 7
	 * 5. Probamos el findAllPaginated logeados como manager la primera página con un tamaño de 5
	 * 6. Probamos el findAllPaginated logeados como admin la primera página con un tamaño de 5
	 * 7. Probamos el findAllPaginated sin estar logeados (salta una excepción)
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
	 * 1. Probamos el findByManagerUserAccountId logeados como manager la primera página con un tamaño de 5
	 * 2. Probamos el findByManagerUserAccountId logeados como manager la primera página con un tamaño de 2
	 * 3. Probamos el findByManagerUserAccountId logeados como user la primera página con un tamaño de 2
	 * 4. Probamos el findByManagerUserAccountId logeados como admin la primera página con un tamaño de 2
	 * 5. Probamos el findByManagerUserAccountId logeados como manager la primera página con un tamaño de 1
	 * 6. Probamos el findByManagerUserAccountId logeados como manager la segunda página con un tamaño de 1
	 * 7. Probamos el findByManagerUserAccountId logeados como manager la segunda página con un tamaño de 5
	 * 8. Probamos el findByManagerUserAccountId sin estar logeados (salta una excepción)
	 * 9. Probamos el findByManagerUserAccountId usando como managerUserAccountId 0 (salta una excepción)
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
	 * 1. Probamos el findByCategoryId con la category1 logeados como manager la primera página con un tamaño de 5
	 * 2. Probamos el findByCategoryId con la category1 logeados como user la primera página con un tamaño de 5
	 * 3. Probamos el findByCategoryId con la category1 logeados como admin la primera página con un tamaño de 5
	 * 4. Probamos el findByCategoryId con la category1 logeados como manager la primera página con un tamaño de 3
	 * 5. Probamos el findByCategoryId con la category1 logeados como manager la primera página con un tamaño de 2
	 * 6. Probamos el findByCategoryId con la category1 logeados como manager la segunda página con un tamaño de 2
	 * 7. Probamos el findByCategoryId con la category1 logeados como manager la tercera página con un tamaño de 2
	 * 8. Probamos el findByCategoryId con la category3 logeados como manager la primera página con un tamaño de 7
	 * 9. Probamos el findByCategoryId con la category1 sin estar logeado (salta una excepción)
	 * 10.Probamos el findByCategoryId con la categoryId 0 (salta una excepción)
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
	 * 1. Probamos el findByRendezvousId con el rendezvous3 logeados como manager la primera página con tamaño 5
	 * 2. Probamos el findByRendezvousId con el rendezvous3 logeados como user la primera página con tamaño 5
	 * 3. Probamos el findByRendezvousId con el rendezvous3 logeados como admin la primera página con tamaño 5
	 * 4. Probamos el findByRendezvousId con el rendezvous3 logeados como manager la primera página con tamaño 2
	 * 5. Probamos el findByRendezvousId con el rendezvous3 logeados como manager la primera página con tamaño 1
	 * 6. Probamos el findByRendezvousId con el rendezvous3 logeados como manager la segunda página con tamaño 1
	 * 7. Probamos el findByRendezvousId con el rendezvous3 logeados como manager la tercera página con tamaño 1
	 * 8. Probamos el findByRendezvousId con el rendezvous1 logeados como manager la primera página con tamaño 5
	 * 9. Probamos el findByRendezvousId con el rendezvous3 sin estar logeado (salta una excepción)
	 * 10. Probamos el findByRendezvousId con el rendezvousId a 0 (salta una excepción)
	 */
	@Test()
	public void testFindByRendezvousId() {
		final Object testingData[][] = {
			{
				"manager", "manager1", "findByRendezvousId", false, "rendezvous3", 1, 2, 1, 5, null
			}, {
				"user", "user1", "findByRendezvousId", false, "rendezvous3", 1, 2, 1, 5, null
			}, {
				"admin", "admin", "findByRendezvousId", false, "rendezvous3", 1, 2, 1, 5, null
			}, {
				"manager", "manager1", "findByRendezvousId", false, "rendezvous3", 1, 2, 1, 2, null
			}, {
				"manager", "manager1", "findByRendezvousId", false, "rendezvous3", 1, 1, 2, 1, null
			}, {
				"manager", "manager1", "findByRendezvousId", false, "rendezvous3", 2, 1, 2, 1, null
			}, {
				"manager", "manager1", "findByRendezvousId", false, "rendezvous3", 3, 0, 2, 1, null
			}, {
				"manager", "manager1", "findByRendezvousId", false, "rendezvous1", 1, 0, 0, 5, null
			}, {
				null, "manager1", "findByRendezvousId", false, "rendezvous3", 1, 2, 1, 5, IllegalArgumentException.class
			}, {
				"manager1", "manager1", "findByRendezvousId", true, "rendezvous3", 1, 2, 1, 5, IllegalArgumentException.class
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
				super.authenticate(username); //Nos logeamos si es necesario

			if (method.equals("findAll"))
				services = this.serviceService.findAll(); //Cogemos todos los servicios usando el findAll
			else if (method.equals("findByCategoryId")) {
				if (falseId == false)
					categoryId = super.getEntityId("category1"); //Miramos la categoria correspondiente, se supone que simula haberla buscado en la navegación de categorias
				else
					categoryId = 0;
				services = this.serviceService.findByCategoryId(categoryId); //Se cogen los servicios que son de esa category
			} else if (method.equals("topBestSellingService"))
				services = this.serviceService.topBestSellingServices(tam); //Se prueba el topBestSellingService
			else if (method.equals("findServicesForRequetsByRendezvousId")) {
				if (falseId == false) {
					rendezvousId = super.getEntityId(bean);
					if (expected == null) { //Si no va a salar excepción
						creator = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
						if (!user.equals("user4")) {
							pagesNumber = this.rendezvousService.countByCreatorId(creator.getId());
							pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
							for (int i = 1; i <= pagesNumber; i++)
								for (final Rendezvous r : this.rendezvousService.findByCreatorId(creator.getId(), i, 5))
									//Se coge el rendezvous al que le haremos el request mirando los rendezvouses del creador
									if (rendezvousId == r.getId()) {
										rendezvousId = r.getId();
										break;
									}
						} else {
							pagesNumber = this.rendezvousService.countByCreatorIdAllPublics(creator.getId());
							pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
							for (int i = 1; i <= pagesNumber; i++)
								for (final Rendezvous r : this.rendezvousService.findByCreatorIdAllPublics(creator.getId(), i, 5))
									//Se coge el rendezvous al que le haremos el request mirando los rendezvouses publicos del creador si es el user4
									if (rendezvousId == r.getId()) {
										rendezvousId = r.getId();
										break;
									}
						}
					}
				} else
					rendezvousId = 0;
				services = this.serviceService.findServicesForRequetsByRendezvousId(rendezvousId, page); //Se cogen los servicios para hacer request
				totalPages = this.serviceService.countFindServicesForRequetsByRendezvousId(rendezvousId);
				totalPages = (int) Math.floor(((totalPages / (5 + 0.0)) - 0.1) + 1);
				Assert.isTrue(totalPages == tam);
			}
			Assert.isTrue(services.size() == size); //Se compara el tamaño con el esperado
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
				super.authenticate(username); //Nos logeamos si es necesario

			if (method.equals("findAllPaginated"))
				services = this.serviceService.findAllPaginated(page, tam); //Cogemos todos los servicios paginados
			else if (method.equals("findByManagerUserAccountId")) {
				managerId = super.getEntityId(bean);
				manager = this.managerService.findOne(managerId);
				if (falseId == false)
					services = this.serviceService.findByManagerUserAccountId(manager.getUserAccount().getId(), page, tam); //Si estamos como un manager cogemos todos sus sevicios
				else
					services = this.serviceService.findByManagerUserAccountId(0, page, tam);
			} else if (method.equals("findByCategoryId")) {
				categoryId = super.getEntityId(bean);
				if (falseId == false)
					services = this.serviceService.findByCategoryId(categoryId, page, tam); //Cogemos todos los servicios de una categoria
				else
					services = this.serviceService.findByCategoryId(0, page, tam);
			} else if (method.equals("findByRendezvousId")) {
				rendezvousId = super.getEntityId(bean);
				if (falseId == false) {
					if (expected == null)
						if (user == null || user.equals("user4")) {
							pagesNumber = this.rendezvousService.countAllPublics();
							pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
							for (int i = 1; i <= pagesNumber; i++)
								for (final Rendezvous r : this.rendezvousService.findAllPublics(i, 5))
									//Cogemos el rendezvous entre todos los públicos si no está logeado o es menor de edad
									if (rendezvousId == r.getId()) {
										rendezvousId = r.getId();
										break;
									}
						} else {
							pagesNumber = this.rendezvousService.countAllPaginated();
							pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
							for (int i = 1; i <= pagesNumber; i++)
								for (final Rendezvous r : this.rendezvousService.findAllPaginated(i, 5))
									//Si no se coge entre todos
									if (rendezvousId == r.getId()) {
										rendezvousId = r.getId();
										break;
									}
						}
					services = this.serviceService.findByRendezvousId(rendezvousId, page, tam); //Se cogen los servicios
				} else
					services = this.serviceService.findByRendezvousId(0, page, tam);
			}

			Assert.isTrue(services.getContent().size() == size); //Se compara el tamaño con el esperado
			Assert.isTrue(services.getTotalPages() == totalPage);//Se compara el total de páginas con las esperadas

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
