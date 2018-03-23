
package usecases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.DataBinder;

import services.CategoryService;
import utilities.AbstractTest;
import domain.Category;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CreateCategoryTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private CategoryService	categoryService;


	// Tests ------------------------------------------------------------------

	//Pruebas
	/*
	 * 1. Crear Category con padre, excepción no esperada.
	 * 2. Crear Category sin padre, excepción no esperada.
	 */

	//CU 2.0:
	//11. An actor who is authenticated as an administrator must be able to:
	//	1. Manage the categories of services, which includes listing, creating, updating, deleting,	and re-organising them in the category hierarchies.

	@Test
	public void driverCreatePositive() {

		//Rol, grandfatherCategoryName, fatherName, categorName, name, description, ExpectedException
		final Object testingData[][] = {
			{
				"admin", null, null, "category1", "Name Category28", "Description category28", null
			}, {
				"admin", null, null, null, "Name Category28", "Description category28", null

			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateCreate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);

	}

	//Pruebas
	/*
	 * 1. Crear Category sin estar autenticado, IllegalArgumentException esperada.
	 * 2. Crear Category con nombre a null, ConstraintViolationException esperada.
	 * 3. Crear Category con nombre vacío, ConstraintViolationException esperada.
	 * 4. Crear Category con description a null, ConstraintViolationException esperada.
	 * 5. Crear Category con description vacío, ConstraintViolationException esperada.
	 * 6. Crear Category con rol manager, IllegalArgumentException esperada.
	 * MISMOS CASOS SIN PADRE
	 */

	//CU 2.0:
	//11. An actor who is authenticated as an administrator must be able to:
	//	1. Manage the categories of services, which includes listing, creating, updating, deleting,	and re-organising them in the category hierarchies.

	@Test
	public void driverCreateNegative() {

		//Rol, grandfatherName, fatherName, categoryName, name, description, ExpectedException
		final Object testingData[][] = {
			{
				null, null, null, "category1", "Name Category28", "Description category28", IllegalArgumentException.class
			}, {
				"admin", null, null, "category1", null, "Description category28", ConstraintViolationException.class
			}, {
				"admin", null, null, "category1", "", "Description category28", ConstraintViolationException.class
			}, {
				"admin", null, null, "category1", "Name Category28", null, ConstraintViolationException.class
			}, {
				"admin", null, null, "category1", "Name Category28", " ", ConstraintViolationException.class
			}, {
				"manager1", null, null, "category1", "Name Category28", "Description category28", IllegalArgumentException.class
			}, {
				null, null, null, null, "Name Category28", "Description category28", IllegalArgumentException.class
			}, {
				"admin", null, null, null, null, "Description category28", ConstraintViolationException.class
			}, {
				"admin", null, null, null, "", "Description category28", ConstraintViolationException.class
			}, {
				"admin", null, null, null, "Name Category28", null, ConstraintViolationException.class
			}, {
				"admin", null, null, null, "Name Category28", " ", ConstraintViolationException.class
			}, {
				"manager1", null, null, null, "Name Category28", "Description category28", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateCreate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);

	}

	/*
	 * 1. Autenticarnos.
	 * 2. Listar las categorías de la raíz.
	 * 3. Navegar hasta la categry que queremos seleccionar.
	 * 3. Escoger una y actualizarla.
	 */
	protected void templateCreate(final String userName, final String grandfatherCategoryName, final String fatherCategoryName, final String categoryName, final String name, final String description, final Class<?> expected) {
		Class<?> caught;
		Category category;
		Category fatherCategory;
		Category saved;
		List<String> categoriesSearch;
		DataBinder binder;

		caught = null;

		try {
			super.startTransaction();
			this.authenticate(userName);

			//Comprobamos si la categoria padre es null para evitar NullPointerException
			fatherCategory = null;
			if (categoryName != null) {
				//Navegamos para seleccionar la categoría
				categoriesSearch = new ArrayList<String>();
				categoriesSearch.add(grandfatherCategoryName);
				categoriesSearch.add(fatherCategoryName);
				categoriesSearch.add(categoryName);

				fatherCategory = this.searchCategory(categoriesSearch);

			}

			category = this.categoryService.create(fatherCategory);
			category.setDescription(description);
			category.setName(name);
			category.setDefaultCategory(true);

			binder = new DataBinder(category);

			saved = this.categoryService.reconstruct(category, binder.getBindingResult());
			saved = this.categoryService.save(saved);
			this.categoryService.flush();

			Assert.isTrue(!saved.isDefaultCategory());

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		this.checkExceptions(expected, caught);

	}

	//Auxiliary Methods
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
