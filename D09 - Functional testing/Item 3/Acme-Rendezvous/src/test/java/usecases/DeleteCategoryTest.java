
package usecases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.CategoryService;
import services.ServiceService;
import utilities.AbstractTest;
import domain.Category;
import domain.Service;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DeleteCategoryTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private CategoryService	categoryService;

	//Fixtures
	@Autowired
	private ServiceService	serviceService;


	// Tests ------------------------------------------------------------------
	//Pruebas
	/*
	 * 1. Borrar Category sin padre, sin hijos excepción no esperada.
	 * 2. Borrar Category sin padre, con hijos excepción no esperada.
	 * 3. Borrar Category con padre, con hijos excepción no esperada.
	 * 4. Borrar Category con padre, sin hijos excepción no esperada.
	 */

	//CU 2.0:
	//11. An actor who is authenticated as an administrator must be able to:
	//	1. Manage the categories of services, which includes listing, creating, updating, deleting,	and re-organising them in the category hierarchies.

	@Test
	public void driverDeleteFatherAndChildrenAllCases() {

		//Rol, grandFatherCategory, fatherCategory, category, hasFather, hasChildren, ExpectedException
		final Object testingData[][] = {
			{
				"admin", null, null, "category2", false, false, null
			}, {
				"admin", null, null, "category1", false, true, null
			}, {
				"admin", null, "category1", "category4", true, true, null
			}, {
				"admin", "category1", "category4", "category6", true, false, null
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateDeleteFatherAndChildrenAllCases((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Boolean) testingData[i][4], (Boolean) testingData[i][5], (Class<?>) testingData[i][6]);

	}

	/*
	 * Borrar Category comprobando borrado de esta. Pasos:
	 * 1. Autenticarnos.
	 * 2. Listar las category.
	 * 3. Navegar hasta la category que queramos.
	 * 4. Escoger category.
	 */
	protected void templateDeleteFatherAndChildrenAllCases(final String userName, final String grandfatherCategoryName, final String fatherCategoryName, final String categoryName, final Boolean hasFather, final Boolean hasChildren, final Class<?> expected) {
		Class<?> caught;
		Category category;
		Collection<Category> categoriesIfHasChildren;
		Collection<Category> categoriesIfHasFather;
		Category fatherCategory;
		List<String> categoriesSearch;

		caught = null;

		try {
			super.startTransaction();
			this.authenticate(userName);

			//Navegamos para seleccionar la categoría
			categoriesSearch = new ArrayList<String>();
			categoriesSearch.add(grandfatherCategoryName);
			categoriesSearch.add(fatherCategoryName);
			categoriesSearch.add(categoryName);

			category = this.searchCategory(categoriesSearch);

			category = this.categoryService.findOne(this.getEntityId(categoryName));
			fatherCategory = null;
			categoriesIfHasChildren = new ArrayList<Category>();

			//Comprobamos que al borrar, sus hijos se borren y no sea hija de su padre
			//Si tenia hijos
			if (hasChildren)
				categoriesIfHasChildren = this.getAllDescendant(category);

			//Si tenia padres
			if (hasFather)
				fatherCategory = category.getFatherCategory();

			this.categoryService.delete(category);
			this.categoryService.flush();

			//Vemos lo ocurrido después

			Assert.isNull(this.categoryService.findOne(category.getId()));

			if (hasChildren)
				for (final Category c : categoriesIfHasChildren)
					Assert.isNull(this.categoryService.findOne(c.getId()));

			if (hasFather) {
				categoriesIfHasFather = this.getAllDescendant(fatherCategory);
				Assert.isTrue(!categoriesIfHasFather.contains(category));
			}

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		this.checkExceptions(expected, caught);

	}

	//Pruebas
	/*
	 * 1. Borrar Category que no tiene servicios asociados, excepción no esperada.
	 * 2. Borrar Category que tiene más de un servicios asociados, excepción no esperada.
	 * 3. Borrar Category que tiene un solo servicio asociado (con dos categorías asociadas), excepción no esperada.
	 */

	//CU 2.0:
	//11. An actor who is authenticated as an administrator must be able to:
	//	1. Manage the categories of services, which includes listing, creating, updating, deleting,	and re-organising them in the category hierarchies.

	@Test
	public void driverDeleteAndUpdateService() {

		//Rol,granfatherCategory, fatherCategory, category, ExpectedException
		final Object testingData[][] = {
			{
				"admin", "category1", "category4", "category5", null
			}, {
				"admin", null, null, "category2", null
			}, {
				"admin", null, null, "category8", null
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateDeleteAndUpdateService((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);

	}

	/*
	 * Borrar Category comprobando cambio services. Pasos:
	 * 1. Autenticarnos.
	 * 2. Listar las category.
	 * 3. Navegar hasta la category que queramos.
	 * 4. Escoger category.
	 * 5. Borrar category.
	 */
	protected void templateDeleteAndUpdateService(final String userName, final String grandfatherCategoryName, final String fatherCategoryName, final String categoryName, final Class<?> expected) {
		Class<?> caught;
		final Category categoryChoosen;
		Collection<Service> services;
		List<String> categoriesSearch;
		List<Category> categories;
		Category defaultCategory;
		Service serviceAux;
		Map<Integer, Integer> serviceIdPerCategoryNumber;

		caught = null;

		try {
			super.startTransaction();
			this.authenticate(userName);

			defaultCategory = this.categoryService.findOne(this.getEntityId("category12"));

			//Añadimos el número de categorías actual por cada Servicio
			serviceIdPerCategoryNumber = new HashMap<Integer, Integer>();

			//Navegamos para seleccionar la categoría
			categoriesSearch = new ArrayList<String>();
			categoriesSearch.add(grandfatherCategoryName);
			categoriesSearch.add(fatherCategoryName);
			categoriesSearch.add(categoryName);

			categoryChoosen = this.searchCategory(categoriesSearch);

			//Sacamos los servicios de la categoría
			services = new ArrayList<Service>(this.serviceService.findByCategoryId(categoryChoosen.getId()));
			for (final Service service : services)
				serviceIdPerCategoryNumber.put(service.getId(), service.getCategories().size());

			this.categoryService.delete(categoryChoosen);
			this.categoryService.flush();

			//Vemos que se haya borrado
			Assert.isNull(this.categoryService.findOne(categoryChoosen.getId()));

			//Si tenía servicios vemos que ya no tengan dicha categoría
			for (final Service service : services) {

				serviceAux = this.serviceService.findOne(service.getId());
				//Comprobamos que el servicio tenga una categoría menos asociada o una (la por defecto) si tenia solo una antes
				if (serviceIdPerCategoryNumber.get(serviceAux.getId()) != 1)
					Assert.isTrue(serviceIdPerCategoryNumber.get(serviceAux.getId()).equals(serviceAux.getCategories().size() + 1));
				else {
					Assert.isTrue(serviceIdPerCategoryNumber.get(serviceAux.getId()).equals(serviceAux.getCategories().size()));
					categories = new ArrayList<Category>(serviceAux.getCategories());
					Assert.isTrue(categories.get(0).equals(defaultCategory));
				}

				//Comprobamos que falte la categoría borrada
				Assert.isTrue(!serviceAux.getCategories().contains(categoryChoosen));
			}

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		this.checkExceptions(expected, caught);

	}

	//Pruebas
	/*
	 * 1. Borrar Category estando autenticados como un manager, excepción IllegalArgumentException.
	 * 2. Borrar Category sin estar autenticados, excepción IllegalArgumentException.
	 * 3. Borrar default Category, excepción IllegalArgumentException.
	 */

	//CU 2.0:
	//11. An actor who is authenticated as an administrator must be able to:
	//	1. Manage the categories of services, which includes listing, creating, updating, deleting,	and re-organising them in the category hierarchies.

	@Test
	public void driverDeleteNegativeTest() {

		//Rol, category, ExpectedException
		final Object testingData[][] = {
			{
				"manager1", "category5", IllegalArgumentException.class
			}, {
				null, "category2", IllegalArgumentException.class
			}, {
				"admin", "category12", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateDeleteNegativeTest((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	/*
	 * Borramos una category. Pasos:
	 * 1. Autenticarnos.
	 * 2. Intentar borrar esa category
	 */
	protected void templateDeleteNegativeTest(final String userName, final String categoryName, final Class<?> expected) {
		Class<?> caught;
		Category category;

		caught = null;

		try {
			super.startTransaction();

			//1. Autenticarnos
			this.authenticate(userName);

			//2. Intentar borrar esa category
			category = this.categoryService.findOne(this.getEntityId(categoryName));

			this.categoryService.delete(category);
			this.categoryService.flush();

			//Vemos que se haya borrado
			Assert.isNull(this.categoryService.findOne(category.getId()));

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		this.checkExceptions(expected, caught);

	}

	//Auxilary Methods
	private Collection<Category> getAllDescendant(final Category category) {
		Collection<Category> result;
		Collection<Category> categoriesAux;
		Collection<Category> categoriesAuxChildren;

		result = new ArrayList<Category>();
		categoriesAuxChildren = new ArrayList<Category>();

		//Cogemos los primeros hijos
		categoriesAux = this.categoryService.findAllByFatherCategoryId(category.getId());
		do {

			//Si no es la primera vez, actualizamos el bucle
			if (categoriesAux.size() == 0) {
				categoriesAux = new ArrayList<Category>(categoriesAuxChildren);
				categoriesAuxChildren.clear();
			}

			//Lo añadimos al general
			result.addAll(categoriesAux);

			//Cogemos categoryAux, las que aún no hemos recorrido y añadimos sus hijos para recorrerlos, eliminamos las que recorremos
			for (final Category c : categoriesAux)
				categoriesAuxChildren.addAll(this.categoryService.findAllByFatherCategoryId(c.getId()));

			categoriesAux.clear();
		} while (categoriesAuxChildren.size() != 0);

		return result;
	}

	private Category searchCategory(final List<String> categoriesName) {
		Category category;
		Collection<Category> categories;
		Category result;

		result = null;

		//Categoría a buscar
		for (final String categoryName : categoriesName)
			if (categoryName != null) {

				category = this.categoryService.findOne(this.getEntityId(categoryName));

				//Cojo el padre donde debe estar la category a buscar
				if (category.getFatherCategory() == null)
					categories = this.categoryService.findAllWithoutFather();
				else
					categories = this.categoryService.findAllByFatherCategoryId(category.getFatherCategory().getId());

				//Recogemos el resultado cuando se encuentre
				for (final Category categoryIterator : categories)
					if (categoryIterator.equals(category)) {
						result = categoryIterator;
						break;
					}
			}

		return result;
	}

}
