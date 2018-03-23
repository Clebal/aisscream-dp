
package usecases;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.CategoryService;
import utilities.AbstractTest;
import domain.Category;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ListingCategoryTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private CategoryService	categoryService;


	//Pruebas
	/*
	 * 1. Listar hijos de Category que no tiene hijos. Excepción no esperada.
	 * 2. Listar hijos de Category que tiene un hijo. Excepción no esperada.
	 * 3. Listar hijos de Category que tiene más de un hijo. Excepción no esperada.
	 * 4. Listar Category de la raíz. Excepción no esperada.
	 * 5. Listar Category de la raíz con user. Excepción no esperada.
	 * 6. Listar Category de la raíz con manager. Excepción no esperada.
	 * 7. Listar Category de la raíz sin autenticar. Excepción no esperada.
	 */

	//CU 2.0:
	//11. An actor who is authenticated as an administrator must be able to:
	//	1. Manage the categories of services, which includes listing, creating, updating, deleting,	and re-organising them in the category hierarchies.

	@Test
	public void driverListCategory() {

		//Rol, grandfatherCategory, fatherCategory, category, level, ExpectedException
		final Object testingData[][] = {
			{
				"admin", "category2", "category10", "category11", 3, null
			}, {
				"admin", null, "category2", "category10", 2, null
			}, {
				"admin", null, "category1", "category4", 2, null
			}, {
				"admin", null, null, "category8", 1, null
			}, {
				"user1", null, null, "category8", 1, null
			}, {
				"manager1", null, null, "category8", 1, null
			}, {
				null, null, null, "category8", 1, null
			}
		};

		for (int i = 0; i < testingData.length; i++)

			this.templatePage((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Integer) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	//Pruebas
	/*
	 * 1. Listar hijos de Category que no existe. Excepción IllegalArgumentException esperada.
	 */

	//CU 2.0:
	//11. An actor who is authenticated as an administrator must be able to:
	//	1. Manage the categories of services, which includes listing, creating, updating, deleting,	and re-organising them in the category hierarchies.

	@Test
	public void driverListCategoryNegative() {

		//Rol, categoryId, ExpectedException
		final Object testingData[][] = {
			{
				"admin", 0, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templatePageNegative((String) testingData[i][0], (Integer) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	/*
	 * 1. Autenticarnos.
	 * 2. Navegar hasta la category deseada.
	 */
	protected void templatePage(final String userName, final String grandfatherCategoryName, final String fatherCategoryName, final String entityName, final Integer level, final Class<?> expected) {
		Class<?> caught;
		Category category;
		Page<Category> categories;
		List<String> categoriesSearch;
		int countLevel;
		Category categoryChoosen;

		caught = null;

		try {
			super.startTransaction();
			this.authenticate(userName);

			//Navegamos para seleccionar la categoría
			categoriesSearch = new ArrayList<String>();
			categoriesSearch.add(grandfatherCategoryName);
			categoriesSearch.add(fatherCategoryName);
			categoriesSearch.add(entityName);
			categoryChoosen = null;

			countLevel = 0;
			//Cogemos las categorias que vamos a buscar
			for (final String categoryName : categoriesSearch)
				if (categoryName != null) {

					category = this.categoryService.findOne(this.getEntityId(categoryName));

					//Cojo el padre donde debe estar la category a buscar
					if (category.getFatherCategory() == null)
						categories = this.categoryService.findWithoutFather(1, 5);
					else
						categories = this.categoryService.findByFatherCategoryId(category.getFatherCategory().getId(), 1, 5);

					//Recorremos en las diferentes páginas
					for (int i = 0; i < categories.getTotalPages() + 1; i++) {

						if (i == 0)
							i = +2;
						//Recogemos el resultado cuando se encuentre
						for (final Category categoryIterator : categories)
							if (categoryIterator.equals(category)) {
								categoryChoosen = categoryIterator;
								countLevel++;
								break;
							}

						//Hemos encontrado el objeto, pasamos al siguiente nivel
						if (categoryChoosen != null) {
							categoryChoosen = null;
							break;
						}

						//No se encuentra en la primera página, buscamos en las demás
						if (category.getFatherCategory() == null)
							categories = this.categoryService.findWithoutFather(i, 5);
						else
							categories = this.categoryService.findByFatherCategoryId(category.getFatherCategory().getId(), i, 5);
					}

				}

			//Miramos que la profundidad en el árbol sea la esperada
			Assert.isTrue(countLevel == level);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		this.checkExceptions(expected, caught);

	}

	/*
	 * 1. Autenticamos.
	 * 2. Accedemos a una category dado su id
	 */
	protected void templatePageNegative(final String userName, final Integer categoryId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			super.startTransaction();
			this.authenticate(userName);

			this.categoryService.findByFatherCategoryId(categoryId, 1, 5);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		this.checkExceptions(expected, caught);

	}

}
