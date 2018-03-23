
package usecases;

import java.util.ArrayList;
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

import security.LoginService;
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
public class EditServiceTest extends AbstractTest {

	@Autowired
	private ServiceService	serviceService;

	@Autowired
	private ManagerService	managerService;

	@Autowired
	private CategoryService	categoryService;


	/*
	 * Pruebas
	 * 1. Editamos un servicio siendo el manager1 con todos los parámetros inicializados
	 * 2. Editamos un servicio poniendo a null el picture y inicializando el resto.
	 * 3. Editamos un servicio Accepted sobreescribiendo Accepted por Accepted
	 * 4. Editamos un servicio Accepted sobreescribiendo el status a Accepted y el manager por sigo mismo
	 * 5. Editamos un servicio del manager2 sobreescribiéndose por sigo mismo.
	 * 
	 * Requisitos
	 * C.5.2: An actor who is registered as a manager must be able to Manage his or her services, which includes updating them
	 */
	@Test
	public void positiveEditTest() {
		final Object testingData[][] = {
			{
				"manager", "manager1", "service1", "Servicio 1", "Descripción 1", "http://www.imagenes.com/imagen1", null, null, null, false, null
			}, {
				"manager", "manager1", "service1", "Servicio 2", "Descripción 2", null, null, null, null, false, null
			}, {
				"manager", "manager1", "service1", "Servicio 3", "Descripción 3", "http://www.imagenes.com/imagen3", "ACCEPTED", null, null, false, null
			}, {
				"manager", "manager1", "service2", "Servicio 4", "Descripción 4", "http://www.imagenes.com/imagen4", "ACCEPTED", "manager1", null, false, null
			}, {
				"manager", "manager2", "service3", "Servicio 5", "Descripción 5", "http://www.imagenes.com/imagen5", null, "manager2", null, false, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
					(String) testingData[i][7], (String) testingData[i][8], (Boolean) testingData[i][9], (Class<?>) testingData[i][10]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1. Editamos el servicio1 poniendo todos los parámetros a nulo (salta un ConstraintViolationException)
	 * 2. Editamos el servicio1 poniendo el description y el picture a nulo (salta un ConstraintViolationException)
	 * 3. Editamos el servicio1 poniendo el nombre a vacía (salta un ConstraintViolationException)
	 * 4. Editamos el servicio1 poniendo el nombre a nulo (salta un ConstraintViolationException)
	 * 5. Editamos el servicio1 poniendo la descripción es vacía (salta un ConstraintViolationException)
	 * 6. Editamos el servicio1 poniendo la descripción a nulo (salta un ConstraintViolationException)
	 * 7. Editamos el servicio1 poniendo un formato de foto inválido (salta un ConstraintViolationException)
	 * 8. Editamos el servicio1 intentando sobreescribir el manager por otro (salta un IllegalArgumentException)
	 * 9.Editamos el servicio1 sin estar logeados (salta un IllegalArgumentException)
	 * 10.Editamos el servicio1 logeados como un user (salta un IllegalArgumentException)
	 */
	@Test()
	public void negativeEditTest() {
		final Object testingData[][] = {
			{
				"manager", "manager1", "service1", null, null, null, null, null, null, false, ConstraintViolationException.class
			}, {
				"manager", "manager1", "service1", "Servicio1", null, null, null, null, null, false, ConstraintViolationException.class
			}, {
				"manager", "manager1", "service1", "", "Descripción 1", null, null, null, null, false, ConstraintViolationException.class
			}, {
				"manager", "manager1", "service1", null, "Descripción 2", null, null, null, null, false, ConstraintViolationException.class
			}, {
				"manager", "manager1", "service1", "Servicio1", "", null, null, null, null, false, ConstraintViolationException.class
			}, {
				"manager", "manager1", "service2", "Servicio 3", "Descripción 3", "ndiwdiowq", null, null, null, false, ConstraintViolationException.class
			}, {
				null, null, "service1", "Servicio 5", "Descripción 5", "http://www.imagenes.com/imagen5", null, null, null, false, IllegalArgumentException.class
			}, {
				"user", "user1", "service1", "Servicio 1", "Descripción 1", "http://www.imagenes.com/imagen1", null, null, null, false, IllegalArgumentException.class
			}, {
				"manager", "manager1", "service3", "Servicio 6", "Descripción 6", "http://www.imagenes.com/imagen6", null, null, null, true, IllegalArgumentException.class
			}, {
				"manager", "manager1", "service3", "Servicio 6", "Descripción 6", "http://www.imagenes.com/imagen6", null, "manager1", null, true, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
					(String) testingData[i][7], (String) testingData[i][8], (Boolean) testingData[i][9], (Class<?>) testingData[i][10]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1. Logeados como administrados aceptamos el servicio3
	 * 2. Logeados como administrador cancelamos el servicio1
	 * 3. Intentamos aceptar el servicio1 sin estar logeados (salta un IllegalArgumentException)
	 * 4. Intentamos aceptar el servicio3 logeados como manager (salta un IllegalArgumentException)
	 * 5. Intentamos aceptar el servicio1 que ya está aceptado logeados como admin (salta un IllegalArgumentException)
	 * 6. Intentamos cancelar el servicio1 sin estar logeados (salta un IllegalArgumentException)
	 * 7. Intentamos cancelar el servicio1 logeados como manager (salta un IllegalArgumentException)
	 * 8. Intentamos cancelar el servicio3 que ya está cancelado logeados como admin (salta un IllegalArgumentException)
	 * 
	 * Requisitos
	 * C.6.1: An actor who is authenticated as an administrator must be able to cancel a service that he or she finds inappropriate. Such services cannot be re-quested for any rendezvous. They must be flagged appropriately when listed
	 */
	@Test
	public void acceptCancelTest() {
		final Object testingData[][] = {
			{
				"admin", "admin", "service3", true, null
			}, {
				"admin", "admin", "service1", false, null
			}, {
				null, null, "service3", true, IllegalArgumentException.class
			}, {
				"manager", "manager1", "service3", true, IllegalArgumentException.class
			}, {
				"admin", "admin", "service1", true, IllegalArgumentException.class
			}, {
				null, null, "service1", false, IllegalArgumentException.class
			}, {
				"manager", "manager1", "service1", false, IllegalArgumentException.class
			}, {
				"admin", "admin", "service3", false, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateAcceptCancel((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Prueba
	 * 1. Añadimos la categoria3 al servicio1 logeados como el manager del servicio.
	 * 2. Borramos la categoria1 al servicio1 logeados como el manager del servicio.
	 * 3. Intentamos añadir una categoria a un servicio con una id que no pertenece a ningún servicio (salta un IllegalArgumentException)
	 * 4. Intentamos añadir al servicio1 una categoria de id no existente (salta un IllegalArgumentException)
	 * 5. Intentamos añadir al servicio1 una categoria ya existente en él (salta un IllegalArgumentException)
	 * 6. Intentamos añadir al servicio1 una categoria sin estar logeados (salta un IllegalArgumentException)
	 * 7. Intentamos añadir una categoria a un servicio que no es del manager logeado (salta un IllegalArgumentException)
	 * 8. Intentamos añadir una categoria a un servicio logeados como admin (salta un IllegalArgumentException)
	 * 9. Intentamos borrar una categoria para un servicio de id no existente (salta un IllegalArgumentException)
	 * 10.Intentamos borrar una categoria no existente a un servicio (salta un IllegalArgumentException)
	 * 11.Intentamos borrar una categoria de un servicio el cual no tiene (salta un IllegalArgumentException)
	 * 12.Intentamos borrar una categoria de un servicio sin estar logeado (salta un IllegalArgumentException)
	 * 13.Intentamos borrar una categoria de un servicio que no es del manager logeado (salta un IllegalArgumentException)
	 * 14. Intentamos borrar una categoria de un servicio estando logeados como admin (salta un IllegalArgumentException)
	 * 
	 * Requisitos
	 * C.5.2: An actor who is registered as a manager must be able to manage his or her services, which includes updating them
	 */
	@Test
	public void addRemoveCategoryTest() {
		final Object testingData[][] = {
			{
				"manager", "manager1", "service1", "category3", true, true, false, null
			}, {
				"manager", "manager1", "service1", "category1", false, true, true, null
			}, {
				"manager", "manager2", "service3", "category1", false, true, false, null
			}, {
				"manager", "manager1", "category3", "category3", true, false, false, IllegalArgumentException.class
			}, {
				"manager", "manager1", "service1", "service1", true, false, false, IllegalArgumentException.class
			}, {
				"manager", "manager1", "service1", "category1", true, false, false, IllegalArgumentException.class
			}, {
				null, null, "service1", "category3", true, false, false, IllegalArgumentException.class
			}, {
				"manager", "manager2", "service1", "category3", true, false, false, IllegalArgumentException.class
			}, {
				"admin", "admin", "service1", "category3", true, false, false, IllegalArgumentException.class
			}, {
				"manager", "manager1", "category1", "category1", false, false, false, IllegalArgumentException.class
			}, {
				"manager", "manager1", "service1", "service1", false, false, false, IllegalArgumentException.class
			}, {
				"manager", "manager1", "service1", "category3", false, false, false, IllegalArgumentException.class
			}, {
				null, null, "service1", "category1", false, false, false, IllegalArgumentException.class
			}, {
				"manager", "manager2", "service1", "category1", false, false, false, IllegalArgumentException.class
			}, {
				"admin", "admin", "service1", "category1", false, false, false, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateAddDeleteCategory((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Boolean) testingData[i][4], (Boolean) testingData[i][5], (Boolean) testingData[i][6],
					(Class<?>) testingData[i][7]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Usamos el findOne para coger el servicio1 logeados como user
	 * 2.Usamos el findOne para coger el servicio1 logeados como manager
	 * 3.Usamos el findOne para coger el servicio1 logeados como admin
	 * 4.Usamos el findOneToEdit para coger el servicio1 logeados como el manager del servicio
	 * 5.Usamos el findOneToEdit para coger el servicio1 logeados como user (salta un IllegalArgumentException)
	 * 6.Usamos el findOneToEdit para coger el servicio1 logeados como manager con id del servicio cero (salta un IllegalArgumentException)
	 * 7.Usamos el findOneToEdit para coger el servicio1 sin estar logeados (salta un IllegalArgumentException)
	 * 8.Usamos el findoneToEdit para coger un servicio que no es del manager logeado (salta un IllegalArgumentException)
	 * 9.Usamos el findonetoEdit para coger un servicio logeados como admin (salta un IllegalArgumentException)
	 */
	@Test
	public void findOneFindOneToEditTest() {
		final Object testingData[][] = {
			{
				"user", "user1", "service1", false, false, false, null
			}, {
				"manager", "manager1", "service1", false, false, false, null
			}, {
				"admin", "admin", "service1", false, false, false, null
			}, {
				"manager", "manager1", "service1", true, false, true, null
			}, {
				"user", "user1", "service1", false, true, false, IllegalArgumentException.class
			}, {
				"manager", "manager1", "service1", true, true, true, IllegalArgumentException.class
			}, {
				null, null, "service1", true, false, false, IllegalArgumentException.class
			}, {
				"manager", "manager1", "service3", true, false, false, IllegalArgumentException.class
			}, {
				"admin", "admin", "service3", true, false, false, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateFindOneFindOneToEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (Boolean) testingData[i][4], (Boolean) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void templateEdit(final String user, final String username, final String serviceBean, final String name, final String description, final String picture, final String status, final String manager, final String category,
		final boolean isNotMyService, final Class<?> expected) {
		Class<?> caught;
		int serviceId;
		int serviceIdAux;
		Service serviceAux;
		Service service;
		Service saved;
		int managerId;
		Manager managerEntity;
		int categoryId;
		Category categoryEntity;
		DataBinder binder;
		Service serviceReconstruct;

		caught = null;
		try {
			if (user != null)
				super.authenticate(username); //Nos logeamos si es necesario

			serviceId = 0;
			serviceIdAux = super.getEntityId(serviceBean);

			if (user == "manager") {
				if (isNotMyService == false) {
					for (int i = 1; i <= this.serviceService.findByManagerUserAccountId(LoginService.getPrincipal().getId(), 1, 5).getTotalPages(); i++)
						//Si estamos como manager y es mi servicio buscamos el servicio entre todos los del manager
						for (final Service s : this.serviceService.findByManagerUserAccountId(LoginService.getPrincipal().getId(), i, 5).getContent())
							if (serviceIdAux == s.getId())
								serviceId = s.getId();
				} else
					serviceId = super.getEntityId(serviceBean);
			} else
				serviceId = super.getEntityId(serviceBean);
			//Si no es mi servicio o no estás logeado como manager se coge directamente para probar hackeos
			serviceAux = this.serviceService.findOneToEdit(serviceId);
			service = this.copyService(serviceAux); //Se hace una copia para evitar que Spring persista el servicio al hacerle los set
			service.setName(name);
			service.setDescription(description);
			service.setPicture(picture);
			//Se modifican los parámetros correspondientes
			if (status != null)
				service.setStatus(status); //Probamos a cambiar el status para probar hackeos
			if (manager != null) {
				managerId = super.getEntityId(manager);
				managerEntity = this.managerService.findOne(managerId);
				service.setManager(managerEntity); //Probamos a modificar el manager para probar hackeos

			}
			if (category != null) {
				categoryId = super.getEntityId(category);
				categoryEntity = this.categoryService.findOne(categoryId);
				service.getCategories().add(categoryEntity); //Probamos a añadir una nueva categoria para probar hackeos
			}
			binder = new DataBinder(service);
			serviceReconstruct = this.serviceService.reconstruct(service, binder.getBindingResult());
			saved = this.serviceService.save(serviceReconstruct); //Guardamos el servicio
			super.flushTransaction();

			Assert.isTrue(this.serviceService.findAll().contains(saved)); //Miramos que el servicio guardado esté entre la lista de servicios

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

	protected void templateAcceptCancel(final String user, final String username, final String serviceBean, final boolean accept, final Class<?> expected) {
		Class<?> caught;
		int serviceId;
		int serviceIdAux;
		Service service;

		caught = null;
		try {
			if (user != null)
				super.authenticate(username); //Nos logeamos si es necesario

			serviceId = 0;
			serviceIdAux = super.getEntityId(serviceBean);
			if (user != null) {
				for (int i = 1; i <= this.serviceService.findAllPaginated(1, 5).getTotalPages(); i++)
					//Buscamos el servicio entre todos los servicios existentes
					for (final Service s : this.serviceService.findAllPaginated(i, 5).getContent())
						if (serviceIdAux == s.getId())
							serviceId = s.getId();
			} else
				serviceId = super.getEntityId(serviceBean); //Si no estás logeado se pilla directamente para probar hackeos
			service = this.serviceService.findOne(serviceId);
			if (accept == true)
				this.serviceService.acceptService(service); //Se accepta el servicio
			else
				this.serviceService.cancelService(service); //Se cancela el servicio

			super.flushTransaction();

			if (accept == true)
				Assert.isTrue(this.serviceService.findOne(service.getId()).getStatus().equals("ACCEPTED")); //Si se ha aceptado se mira que el status sea ACCEPTED
			else
				Assert.isTrue(this.serviceService.findOne(service.getId()).getStatus().equals("CANCELLED"));//Si se ha cancelado el servicio se mira que el status sea CANCELLED

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

	protected void templateAddDeleteCategory(final String user, final String username, final String serviceBean, final String categoryBean, final boolean add, final boolean correctBeans, final boolean keepingDefaultCategory, final Class<?> expected) {
		Class<?> caught;
		int serviceId;
		final int serviceIdAux;
		Service service;
		int categoryId;
		final int categoryIdAux;
		Category category;

		caught = null;
		try {
			if (user != null)
				super.authenticate(username); //Nos logeamos si es necesario
			serviceId = 0;
			categoryId = 0;
			if (user == "manager") {
				if (correctBeans == false) {//Si los beans están puestos mal a posta para probar hackeos se cogen directamente
					serviceId = super.getEntityId(serviceBean);
					categoryId = super.getEntityId(categoryBean);
				} else {
					serviceIdAux = super.getEntityId(serviceBean);
					for (int i = 1; i <= this.serviceService.findByManagerUserAccountId(LoginService.getPrincipal().getId(), 1, 5).getTotalPages(); i++)
						//Se coge el servicio entre los servicios del manager logeado
						for (final Service s : this.serviceService.findByManagerUserAccountId(LoginService.getPrincipal().getId(), i, 5))
							if (serviceIdAux == s.getId()) {
								serviceId = s.getId();
								break;
							}
					if (add == true) {
						categoryIdAux = super.getEntityId(categoryBean);
						for (int i = 1; i <= this.categoryService.findByServiceId(serviceId, 1, 5).getTotalPages(); i++)
							//Si se añade se busca la categoria entre las que no tiene el servicio
							for (final Category c : this.categoryService.findByServiceId(serviceId, i, 5))
								if (categoryIdAux == c.getId()) {
									categoryId = c.getId();
									break;
								}
					} else {
						categoryIdAux = super.getEntityId(categoryBean);
						for (final Category c : this.serviceService.findOne(serviceId).getCategories())
							//Si se va a borrar se busca entre las categorias del servicio
							if (categoryIdAux == c.getId()) {
								categoryId = c.getId();
								break;
							}
					}
				}

			} else { //Si no estás como manager se intenta hackear y se coge directamente
				serviceId = super.getEntityId(serviceBean);
				categoryId = super.getEntityId(categoryBean);
			}

			service = this.serviceService.findOne(serviceId);
			category = this.categoryService.findOne(categoryId);
			if (add == true)
				this.serviceService.addCategory(service, category); //Se añade la categoria 
			else
				this.serviceService.removeCategory(service, category);//Se borra la categoria

			super.flushTransaction();

			if (add == true)
				Assert.isTrue(this.serviceService.findOne(service.getId()).getCategories().contains(category)); //Si se ha añadido se mira que esté en su lista de categorias
			else {
				Assert.isTrue(!this.serviceService.findOne(service.getId()).getCategories().contains(category));//Si se ha borrado se mira que no esté en su lista de categorias
				if (keepingDefaultCategory == true)
					Assert.isTrue(this.serviceService.findOne(service.getId()).getCategories().contains(this.categoryService.findByDefaultCategory()) && this.serviceService.findOne(serviceId).getCategories().size() == 1); //Si se ha borrado la última se mira que se mantiene la categoria por defecto
			}

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

	protected void templateFindOneFindOneToEdit(final String user, final String username, final String serviceBean, final boolean findOneToEdit, final boolean falseId, final boolean isMyService, final Class<?> expected) {
		Class<?> caught;
		int serviceId;
		Service service;
		final int serviceIdAux;

		caught = null;
		try {
			if (user != null)
				super.authenticate(username);//Nos logeamos si es necesario
			serviceId = 0;

			if (user == null)
				serviceId = super.getEntityId(serviceBean); //Si no estamos logeado lo cogemos directamente para probar hackeos
			else if (user.equals("manager")) {
				if (findOneToEdit == true && isMyService == true) { //Si se prueba el findOneToEdit para un servicio del manager logeado se busca el servicio entre sus serviciso
					serviceIdAux = super.getEntityId(serviceBean);
					for (int i = 1; i <= this.serviceService.findByManagerUserAccountId(LoginService.getPrincipal().getId(), 1, 5).getTotalPages(); i++)
						for (final Service s : this.serviceService.findByManagerUserAccountId(LoginService.getPrincipal().getId(), i, 5).getContent())
							if (serviceIdAux == s.getId())
								serviceId = s.getId();
				} else { //Si no se busca el servicio entre todos los servicios
					serviceIdAux = super.getEntityId(serviceBean);
					for (int i = 1; i <= this.serviceService.findAllPaginated(1, 5).getTotalPages(); i++)
						for (final Service s : this.serviceService.findAllPaginated(i, 5).getContent())
							if (serviceIdAux == s.getId())
								serviceId = s.getId();
				}

			} else { //Si no estás como manager se busca entre todos los servicios existente
				serviceIdAux = super.getEntityId(serviceBean);
				for (int i = 1; i <= this.serviceService.findAllPaginated(1, 5).getTotalPages(); i++)
					for (final Service s : this.serviceService.findAllPaginated(i, 5).getContent())
						if (serviceIdAux == s.getId())
							serviceId = s.getId();
			}
			if (findOneToEdit == true)
				if (falseId == false)
					service = this.serviceService.findOneToEdit(serviceId); //Se prueba el findOneToEdit
				else
					service = this.serviceService.findOneToEdit(0);
			else if (falseId == false)
				service = this.serviceService.findOne(serviceId); //Se prueba el findOne
			else
				service = this.serviceService.findOne(0);

			Assert.notNull(service);
			super.flushTransaction();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

	private Service copyService(final Service servicio) {
		Service result;
		Collection<Category> categories;
		Category categoryCopy;

		categories = new ArrayList<Category>();
		result = new Service();
		result.setId(servicio.getId());
		result.setVersion(servicio.getVersion());
		result.setName(servicio.getName());
		result.setDescription(servicio.getDescription());
		result.setPicture(servicio.getPicture());
		result.setStatus(servicio.getStatus());
		result.setManager(servicio.getManager());
		for (final Category category : servicio.getCategories()) {
			categoryCopy = this.copyCategory(category);
			categories.add(categoryCopy);
		}
		result.setCategories(categories);

		return result;
	}

	private Category copyCategory(final Category category) {
		Category result;

		result = new Category();
		result.setId(category.getId());
		result.setVersion(category.getVersion());
		result.setName(category.getName());
		result.setDescription(category.getDescription());
		result.setFatherCategory(category.getFatherCategory());

		return result;
	}
}
