
package usecases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ReorganisingCategoryTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private CategoryService	categoryService;


	// Tests ------------------------------------------------------------------
	//Pruebas
	/*
	 * 1. Reorganizar Category sin padre y sin hijos, a una Category padre sin padre y sin hijos. Excepci�n no esperada.
	 * 2. Reorganizar Category sin padre y con hijos, a una Category padre sin padre y sin hijos. Excepci�n no esperada.
	 * 3. Reorganizar Category con padre y sin hijos, a una Category padre sin padre y sin hijos. Excepci�n no esperada.
	 * 4. Reorganizar Category con padre y con hijos, a una Category padre sin padre y sin hijos. Excepci�n no esperada.
	 */

	//CU 2.0:
	//11. An actor who is authenticated as an administrator must be able to:
	//	1. Manage the categories of services, which includes listing, creating, updating, deleting,	and re-organising them in the category hierarchies.

	@Test
	public void driverReorganisingNewFatherWithoutFatherAndWithoutChildren() {

		//Rol, categoryToMove, categoryNewFather, ExpectedException
		final Object testingData[][] = {
			{
				"admin", "category7", "category9", null
			}, {
				"admin", "category1", "category3", null
			}, {
				"admin", "category6", "category7", null
			}, {
				"admin", "category4", "category8", null
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateReorganising((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	//Pruebas
	/*
	 * 1. Reorganizar Category sin padre y sin hijos, a una Category padre sin padre y con hijos. Excepci�n no esperada.
	 * 2. Reorganizar Category sin padre y con hijos, a una Category padre sin padre y con hijos. Excepci�n no esperada.
	 * 3. Reorganizar Category con padre y sin hijos, a una Category padre sin padre y con hijos. Excepci�n no esperada.
	 * 4. Reorganizar Category con padre y con hijos, a una Category padre sin padre y con hijos. Excepci�n no esperada.
	 */

	//CU 2.0:
	//11. An actor who is authenticated as an administrator must be able to:
	//	1. Manage the categories of services, which includes listing, creating, updating, deleting,	and re-organising them in the category hierarchies.

	@Test
	public void driverReorganisingNewFatherWithoutFatherAndWithChildren() {

		//Rol, categoryToMove, categoryNewFather, ExpectedException
		final Object testingData[][] = {
			{
				"admin", "category8", "category1", null
			}, {
				"admin", "category1", "category2", null
			}, {
				"admin", "category5", "category1", null
			}, {
				"admin", "category4", "category1", null
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateReorganising((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	//Pruebas
	/*
	 * 1. Reorganizar Category sin padre y sin hijos, a una Category padre con padre y sin hijos. Excepci�n no esperada.
	 * 2. Reorganizar Category sin padre y con hijos, a una Category padre con padre y sin hijos. Excepci�n no esperada.
	 * 3. Reorganizar Category con padre y sin hijos, a una Category padre con padre y sin hijos. Excepci�n no esperada.
	 * 4. Reorganizar Category con padre y con hijos, a una Category padre con padre y sin hijos. Excepci�n no esperada.
	 */

	//CU 2.0:
	//11. An actor who is authenticated as an administrator must be able to:
	//	1. Manage the categories of services, which includes listing, creating, updating, deleting,	and re-organising them in the category hierarchies.

	@Test
	public void driverReorganisingNewFatherWithFatherAndWithoutChildren() {

		//Rol, categoryToMove, categoryNewFather, ExpectedException
		final Object testingData[][] = {
			{
				"admin", "category7", "category6", null
			}, {
				"admin", "category1", "category6", null
			}, {
				"admin", "category6", "category5", null
			}, {
				"admin", "category4", "category5", null
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateReorganising((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	//Pruebas
	/*
	 * 1. Reorganizar Category sin padre y sin hijos, a una Category padre con padre y con hijos. Excepci�n no esperada.
	 * 2. Reorganizar Category sin padre y con hijos, a una Category padre con padre y con hijos. Excepci�n no esperada.
	 * 3. Reorganizar Category con padre y sin hijos, a una Category padre con padre y con hijos. Excepci�n no esperada.
	 * 4. Reorganizar Category con padre y con hijos, a una Category padre con padre y con hijos. Excepci�n no esperada.
	 */

	//CU 2.0:
	//11. An actor who is authenticated as an administrator must be able to:
	//	1. Manage the categories of services, which includes listing, creating, updating, deleting,	and re-organising them in the category hierarchies.

	@Test
	public void driverReorganisingNewFatherWithFatherAndWithChildren() {

		//Rol, categoryToMove, categoryNewFather, ExpectedException
		final Object testingData[][] = {
			{
				"admin", "category8", "category4", null
			}, {
				"admin", "category1", "category4", null
			}, {
				"admin", "category5", "category4", null
			}, {
				"admin", "category4", "category10", null
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateReorganising((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	//Pruebas
	/*
	 * 1. Reorganizar Category coloc�ndola en la ra�z sin hijos. Excepci�n no esperada.
	 * 2. Reorganizar Category siendo su nuevo padre ella misma. IllegalArgumentException esperada.
	 * 3. Reorganizar Category null. IllegalArgumentException esperada.
	 * 4. Reorganizar Category autenticados como user. IllegalArgumentException esperada.
	 * 5. Reorganizar Category sin autenticar. IllegalArgumentException esperada.
	 * 6. Reorganizar Category coloc�ndola en la ra�z con hijos. Excepci�n no esperada.
	 * 7. Reorganizar default Category. Excepci�n IllegalArgumentException esperada.
	 */

	//CU 2.0:
	//11. An actor who is authenticated as an administrator must be able to:
	//	1. Manage the categories of services, which includes listing, creating, updating, deleting,	and re-organising them in the category hierarchies.

	@Test
	public void driverReorganising() {

		//Rol, categoryToMove, categoryNewFather, ExpectedException
		final Object testingData[][] = {
			{
				"admin", "category8", null, null
			}, {
				"admin", "category1", "category1", IllegalArgumentException.class
			}, {
				"admin", null, "category4", IllegalArgumentException.class
			}, {
				"user1", "category4", "category10", IllegalArgumentException.class
			}, {
				null, "category4", "category10", IllegalArgumentException.class
			}, {
				"admin", "category4", null, null
			}, {
				"admin", "category12", "category1", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateReorganising((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	//Plantilla para reorganizar y checkear posibles fallos con dos m�todos auxiliares
	/*
	 * 1.Autenticarnos.
	 * 2.Buscar category a mover.
	 * 3.Seleccionar la category.
	 * 4.Buscar la nueva category padre.
	 * 5.Seleccionar la category
	 * 6.Reorganizar
	 */
	protected void templateReorganising(final String userName, final String categoryToMoveName, final String categoryNewFatherName, final Class<?> expected) {
		Class<?> caught;
		Category categoryToMove;
		Category fatherCategoryToMove;
		Category grandfatherCategoryToMove;
		Category fatherNewCategory;
		Category grandfatherNewCategory;
		Category categoryNewFather;
		Category categoryToMoveCopy;
		List<Category> childrenCategoriesAfterChange;
		Collection<Category> childrenCategoriesAux;
		List<Category> categories;

		caught = null;

		try {
			super.startTransaction();
			this.authenticate(userName);

			//Inicializamos
			fatherCategoryToMove = null;
			grandfatherCategoryToMove = null;
			grandfatherNewCategory = null;
			fatherNewCategory = null;

			//Sacamos la categoria a mover, si es null, evitamos NullPointerException
			if (categoryToMoveName != null) {
				categoryToMove = this.categoryService.findOne(this.getEntityId(categoryToMoveName));

				if (categoryToMove.getFatherCategory() != null) {
					fatherCategoryToMove = categoryToMove.getFatherCategory();

					if (fatherCategoryToMove.getFatherCategory() != null)
						grandfatherCategoryToMove = fatherCategoryToMove.getFatherCategory();
				}
				categories = new ArrayList<Category>();
				categories.add(grandfatherCategoryToMove);
				categories.add(fatherCategoryToMove);
				categories.add(categoryToMove);

				categoryToMove = this.searchCategory(categories);

			} else
				categoryToMove = null;

			//Sacamos la nueva categoria padre, si es null, evitamos NullPointerException
			if (categoryNewFatherName != null) {
				categoryNewFather = this.categoryService.findOne(this.getEntityId(categoryNewFatherName));

				if (categoryNewFather.getFatherCategory() != null) {
					fatherNewCategory = categoryNewFather.getFatherCategory();

					if (fatherNewCategory.getFatherCategory() != null)
						grandfatherNewCategory = fatherNewCategory.getFatherCategory();
				}
				categories = new ArrayList<Category>();
				categories.add(grandfatherNewCategory);
				categories.add(fatherNewCategory);
				categories.add(categoryNewFather);

				categoryNewFather = this.searchCategory(categories);

			} else
				categoryNewFather = null;

			//Copiamos la categoria para que no cambie
			categoryToMoveCopy = this.copyCategory(categoryToMove);

			//Cogemos los hijos de la categoria antes de moverla
			childrenCategoriesAfterChange = new ArrayList<Category>();
			if (categoryToMove != null) {
				childrenCategoriesAux = this.categoryService.findAllByFatherCategoryId(categoryToMove.getId());
				childrenCategoriesAfterChange = new ArrayList<Category>(childrenCategoriesAux);
			}

			this.categoryService.reorganising(categoryToMove, categoryNewFather);
			this.categoryService.flush();

			//Vemos que cumple las restricciones
			this.checkChildrenCategory(categoryToMoveCopy, childrenCategoriesAfterChange);
			this.checkFatherCategoryToMove(categoryToMove, categoryNewFather, categoryToMoveCopy.getFatherCategory());

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		this.checkExceptions(expected, caught);

	}
	//Auxiliary Methods
	//Miramos que el padre antiguo de la categor�a y el padre nuevo no tenga ninguna inconsistencia
	private void checkFatherCategoryToMove(final Category categoryAfterMove, final Category categoryNewFather, final Category categoryOldFather) {
		Collection<Category> childrenCategories;

		//Si el padre no era null, el nuevo padre debe ser ese
		if (categoryNewFather != null)
			Assert.isTrue(categoryAfterMove.getFatherCategory().equals(categoryNewFather));
		else
			Assert.isNull(categoryAfterMove.getFatherCategory());

		//Si el padre no era null, el nuevo padre debe contener como hija la categor�a que se ha modificado
		if (categoryNewFather != null) {
			childrenCategories = this.categoryService.findAllByFatherCategoryId(categoryNewFather.getId());
			Assert.isTrue(childrenCategories.contains(categoryAfterMove));

			//Si el padre es null, debe estar contenida en la ra�z	
		} else {
			childrenCategories = this.categoryService.findAllWithoutFather();
			Assert.isTrue(childrenCategories.contains(categoryAfterMove));
		}

		//Vemos si el padre ha cambiado
		if (!(categoryNewFather == null && categoryOldFather == null))
			if (categoryNewFather == null || categoryOldFather == null || !categoryNewFather.equals(categoryOldFather))
				//Si ha cambiado vemos si el anterior no es null y en ese caso vemos que no este en sus hijas
				if (categoryOldFather != null) {
					childrenCategories = this.categoryService.findAllByFatherCategoryId(categoryOldFather.getId());
					Assert.isTrue(!childrenCategories.contains(categoryAfterMove));

					//Si el anterior padre es null, como el nuevo no es igual, la categor�a no debe estar contenida en la ra�z	
				} else {
					childrenCategories = this.categoryService.findAllWithoutFather();
					Assert.isTrue(!childrenCategories.contains(categoryAfterMove));
				}

	}

	//Miramos que a los hijos que ten�a la categor�a se le haya actualizado su padre
	private void checkChildrenCategory(final Category categoryBeforeMove, final List<Category> childrenCategories) {

		//Si la categor�a que se ha movido ten�a hijos y padre, estos ser�n hijos de su padre
		if (categoryBeforeMove.getFatherCategory() != null)
			for (final Category category : childrenCategories)
				Assert.isTrue(category.getFatherCategory().equals(categoryBeforeMove.getFatherCategory()));
		else
			for (final Category category : childrenCategories)
				Assert.isTrue(category.getFatherCategory() == null);
	}

	//M�todo copia de category
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

	private Category searchCategory(final List<Category> categoriesName) {
		Category category;
		Collection<Category> categories;
		Category result;

		result = null;

		//Categor�a a buscar
		for (final Category categoryName : categoriesName)
			if (categoryName != null) {

				category = this.categoryService.findOne(categoryName.getId());

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
