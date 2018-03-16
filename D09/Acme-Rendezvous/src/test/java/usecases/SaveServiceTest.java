
package usecases;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.CategoryService;
import services.ManagerService;
import services.ServiceService;
import utilities.AbstractTest;
import domain.Category;
import domain.Manager;
import domain.Service;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SaveServiceTest extends AbstractTest {

	@Autowired
	private ServiceService	servicioService;

	@Autowired
	private ManagerService	managerService;

	@Autowired
	private CategoryService	categoryService;


	/*
	 * Pruebas
	 * 1. Probamos a crear un servicio de forma normal, dándole valores a todos los parámetros
	 * 2. Probamos a crear un servicio dándole valores a todo menos a picture que es nulo
	 * 3. Probamos a crear un servicio sobreescribiendo el status a Accepted
	 * 4. Probamos a crear un servicio sobreescribiendo el manager del servicio por el que está logeado y el status a Accepted
	 * 5. Probamos a crear un servicio sobreescribiendo el manager del servicio por el que está logeado
	 * 
	 * Requisitos
	 * C.5.2: An actor who is registered as a manager must be able to Manage his or her services, which includes creating them
	 */
	@Test()
	public void testCreatePositive() {
		final Object testingData[][] = {
			{
				"manager", "manager1", "Servicio 1", "Descripción 1", "http://www.imagenes.com/imagen1", "category1", null, null, null, null
			}, {
				"manager", "manager1", "Servicio 2", "Descripción 2", null, "category1", null, null, null, null
			}, {
				"manager", "manager1", "Servicio 3", "Descripción 3", "http://www.imagenes.com/imagen3", "category1", "ACCEPTED", null, null, null
			}, {
				"manager", "manager1", "Servicio 4", "Descripción 4", "http://www.imagenes.com/imagen4", "category1", "ACCEPTED", "manager1", null, null
			}, {
				"manager", "manager2", "Servicio 5", "Descripción 5", "http://www.imagenes.com/imagen5", "category1", null, "manager2", null, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
					(String) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1. Probamos a crear un servicio con todo a nulo (salta una ConstraintViolationException)
	 * 2. Probamos a crear un servicio dándole valores a nulo a description y picture (salta una ConstraintViolationException)
	 * 3. Probamos a crear un servicio dejando en blanco el campo name (salta una ConstraintViolationException)
	 * 4. Probamos a crear un servicio dejando a nulo el campo name y pcture (salta una ConstraintViolationException)
	 * 5. Probamos a crear un servicio dejando en vacío el campo description (salta una ConstraintViolationException)
	 * 6. Probamos a crear un servicio dando un formato de email incorrecto (salta una ConstraintViolationException)
	 * 7. Probamos a crear un servicio poniendo el status en Cancelled (salta una IllegalArgumentException)
	 * 8. Probamos a crear un servicio para un manager que no somos nosotros (salta una IllegalArgumentException)
	 * 9. Probamos a crear un servicio sin estar logeados (salta una IllegalArgumentException)
	 * 10. Probamos a crear un servicio no siendo un manager (salta una IllegalArgumentException)
	 * 11. Probamos a crear un servicio directamente con la lista de categorias no vacía (salta una IllegalArgumentException)
	 */
	@Test()
	public void testCreateNegative() {
		final Object testingData[][] = {
			{
				"manager", "manager1", null, null, null, "category1", null, null, null, ConstraintViolationException.class
			}, {
				"manager", "manager1", "Servicio1", null, null, "category1", null, null, null, ConstraintViolationException.class
			}, {
				"manager", "manager1", "", "Descripción 1", null, "category1", null, null, null, ConstraintViolationException.class
			}, {
				"manager", "manager1", null, "Descripción 2", null, "category1", null, null, null, ConstraintViolationException.class
			}, {
				"manager", "manager1", "Servicio1", "", null, "category1", null, null, null, ConstraintViolationException.class
			}, {
				"manager", "manager1", "Servicio 3", "Descripción 3", "ndiwdiowq", "category1", null, null, null, ConstraintViolationException.class
			}, {
				"manager", "manager1", "Servicio 4", "Descripción 4", "http://www.imagenes.com/imagen4", "category1", "CANCELLED", null, null, IllegalArgumentException.class
			}, {
				"manager", "manager1", "Servicio 5", "Descripción 5", "http://www.imagenes.com/imagen5", "category1", null, "manager2", null, IllegalArgumentException.class
			}, {
				null, null, "Servicio 5", "Descripción 5", "http://www.imagenes.com/imagen5", "category1", null, null, null, IllegalArgumentException.class
			}, {
				"user", "user1", "Servicio 1", "Descripción 1", "http://www.imagenes.com/imagen1", "category1", null, null, null, IllegalArgumentException.class
			}, {
				"manager", "manager1", "Servicio 6", "Descripción 6", "http://www.imagenes.com/imagen6", "category1", null, null, "category3", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
					(String) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	//Con el test nos logeamos si lo indicamos, creamos un nuevo servicio y le vamos haciendo el set de los diferentes parámetros. El name, description y el picture es siempre al igual
	//que pasa en la aplicación y los otros parámetros realmente no deberías poder modificarlo en el form pero se ponen para realizar las pruebas de los casos negativos. Una vez modificados todos los
	// parámetros hacemos el save y comprobamos después si el servicio creado se encuentra entre todos los servicios
	protected void template(final String user, final String username, final String name, final String description, final String picture, final String categoryBean, final String status, final String manager, final String category, final Class<?> expected) {
		Class<?> caught;
		Service service;
		Service saved;
		int managerId;
		Manager managerEntity;
		int categoryId;
		Category categoryEntity;
		int categoryAddedId;

		caught = null;
		try {
			if (user != null)
				super.authenticate(username);

			categoryAddedId = super.getEntityId(categoryBean);
			for (int i = 1; i <= this.categoryService.findAllPaginated(1, 5).getTotalPages(); i++)
				for (final Category c : this.categoryService.findAllPaginated(i, 5))
					if (c.getId() == categoryAddedId)
						categoryAddedId = c.getId();

			service = this.servicioService.create(categoryAddedId);
			service.setName(name);
			service.setDescription(description);
			service.setPicture(picture);
			if (status != null)
				service.setStatus(status);
			if (manager != null) {
				managerId = super.getEntityId(manager);
				managerEntity = this.managerService.findOne(managerId);
				service.setManager(managerEntity);

			}
			if (category != null) {
				categoryId = super.getEntityId(category);
				categoryEntity = this.categoryService.findOne(categoryId);
				service.getCategories().add(categoryEntity);
			}
			saved = this.servicioService.save(service);
			super.flushTransaction();

			Assert.isTrue(this.servicioService.findAll().contains(saved));

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
