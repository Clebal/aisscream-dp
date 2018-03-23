
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
public class EditCategoryTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private CategoryService	categoryService;


	// Tests ------------------------------------------------------------------

	//Pruebas
	/*
	 * 1. Actualizar Category con padre, excepción no esperada.
	 */

	//CU 2.0:
	//11. An actor who is authenticated as an administrator must be able to:
	//	1. Manage the categories of services, which includes listing, creating, updating, deleting,	and re-organising them in the category hierarchies.

	@Test
	public void driverUpdatePositive() {

		//Rol, granFatherCategory, fatherCategory, categoryForUpdate, name, description, ExpectedException
		final Object testingData[][] = {
			{
				"admin", null, null, "category1", "Name Category28", "Description category28", null
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateUpdate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);

	}

	//Pruebas
	/*
	 * 1. Actualizar Category sin estar autenticado, IllegalArgumentException esperada.
	 * 2. Actualizar Category con nombre a null, ConstraintViolationException esperada.
	 * 3. Actualizar Category con nombre vacío, ConstraintViolationException esperada.
	 * 4. Actualizar Category con description a null, ConstraintViolationException esperada.
	 * 5. Actualizar Category con description vacío, ConstraintViolationException esperada.
	 * 6. Actualizar Category con rol manager, IllegalArgumentException esperada.
	 */

	//CU 2.0:
	//11. An actor who is authenticated as an administrator must be able to:
	//	1. Manage the categories of services, which includes listing, creating, updating, deleting,	and re-organising them in the category hierarchies.

	@Test
	public void driverUpdateNegative() {

		//Rol, grandfatherCategory, fatherCategory, categoryForUpdate, name, description, ExpectedException
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
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateUpdate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);

	}

	/*
	 * 1. Autenticarnos.
	 * 2. Listar las categorías de la raíz.
	 * 3. Navegar hasta la categry que queremos seleccionar.
	 * 3. Escoger una y actualizarla.
	 */
	protected void templateUpdate(final String userName, final String grandfatherCategoryName, final String fatherCategoryName, final String categoryName, final String name, final String description, final Class<?> expected) {
		Class<?> caught;
		Category category;
		Category saved;
		List<String> categoriesSearch;
		DataBinder binder;

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

			category = this.copyCategory(category);
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

	//Pruebas
	/*
	 * 1. Actualizar default category. Excepcion no esperada
	 * 2. Actualizar intentamos actualizar el father. Excepcion no esperada (no se actualiza por el reconstruct)
	 */

	//CU 2.0:
	//11. An actor who is authenticated as an administrator must be able to:
	//	1. Manage the categories of services, which includes listing, creating, updating, deleting,	and re-organising them in the category hierarchies.

	@Test
	public void driverUpdateDefaultCategory() {

		//Rol, newfatherCategory, categoryForUpdate, defaultCategory, name, description, ExpectedException
		final Object testingData[][] = {
			{
				"admin", null, "category12", true, "Updating default category", "Description category28", null
			}, {
				"admin", "category2", "category12", true, "Updating default category", "Description category28", null
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateUpdateDefaultCategory((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);

	}

	/*
	 * 1. Autenticarnos.
	 * 2. Listar las categorías de la raíz.
	 * 3. Escoger una y actualizarla.
	 */
	protected void templateUpdateDefaultCategory(final String userName, final String newFatherName, final String categoryName, final Boolean defaultCategory, final String name, final String description, final Class<?> expected) {
		Class<?> caught;
		Category category;
		Category fatherBefore;
		Category saved;
		DataBinder binder;
		Collection<Category> categories;

		caught = null;

		try {
			super.startTransaction();
			this.authenticate(userName);

			//Navegamos para seleccionar la categoría
			categories = this.categoryService.findAllWithoutFather();
			category = this.categoryService.findOne(this.getEntityId(categoryName));

			for (final Category c : categories)
				if (c.equals(category))
					category = this.copyCategory(category);

			category.setDescription(description);
			category.setName(name);
			category.setDefaultCategory(defaultCategory);

			fatherBefore = null;
			if (newFatherName != null) {
				fatherBefore = this.copyCategory(category.getFatherCategory());
				category.setFatherCategory(this.categoryService.findOne(this.getEntityId(newFatherName)));
			}

			binder = new DataBinder(category);

			saved = this.categoryService.reconstruct(category, binder.getBindingResult());
			saved = this.categoryService.save(saved);
			this.categoryService.flush();

			if (defaultCategory)
				Assert.isTrue(saved.isDefaultCategory());
			else
				Assert.isTrue(!saved.isDefaultCategory());

			Assert.isTrue((saved.getFatherCategory() == null && fatherBefore == null) || saved.getFatherCategory().equals(fatherBefore));

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

	private Category copyCategory(final Category category) {
		Category result;

		if (category == null)
			result = null;
		else {
			result = this.categoryService.create(category);

			result.setDescription(category.getDescription());
			result.setFatherCategory(category.getFatherCategory());
			result.setId(category.getId());
			result.setName(category.getName());
			result.setVersion(category.getVersion());
			result.setDefaultCategory(category.isDefaultCategory());

		}

		return result;
	}
}
