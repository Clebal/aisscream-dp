
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

import services.NewspaperService;
import utilities.AbstractTest;
import domain.Newspaper;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ListNewspaperTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private NewspaperService	newspaperService;


	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 1. Probamos el findAll estando logeados como user
	 * 2. Probamos el findAll estando logeados como customer
	 * 3. Probamos el findAll estando logeados como admin
	 */
	@Test()
	public void testFindAll() {
		final Object testingData[][] = {
			{
				"user", "user1", "findAll", false, null, null, 6, null, null, null
			}, {
				"customer", "customer1", "findAll", false, null, null, 6, null, null, null
			}, {
				"admin", "admin", "findAll", false, null, null, 6, null, null, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][8]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Test
	 * 1. Listamos los newspapers donde el suscrito el customer2 viendo la primera p�gina con un tama�o de 2
	 * 2. Listamos los newspapers donde el suscrito el customer2 viendo la segunda p�gina con un tama�o de 1
	 * 3. Listamos los newspapers donde el suscrito el customer2 viendo la segunda p�gina con un tama�o de 1
	 * 4. Listamos los newspapers donde el suscrito el customer2 viendo la tercera p�gina con un tama�o de 1
	 * 5. Listamos los newspapers donde el suscrito el customer2 viendo la primera p�gina con un tama�o de 5
	 * 6. Intentamos listar los newspapers a los que est� suscrito el customer2 logeados como user1 (salta un IllegalArgumentException)
	 * 7. Intentamos listar los newspapers a los que est� suscrito el customer2 logeados como admin (salta un IllegalArgumentException)
	 * 8. Intentamos listar los newspapers a los que est� suscrito el customer2 sin estar logeado (salta un IllegalArgumentException)
	 * 9. Intentamos listar los newspapers a los que est� suscrito el customer2 usando la id cero (salta un IllegalArgumentException)
	 */
	@Test()
	public void testFindForSubscribe() {
		final Object testingData[][] = {
			{
				"customer", "customer2", "findForSubscribe", false, null, 1, 2, 2, 1, null
			}, {
				"customer", "customer2", "findForSubscribe", false, null, 1, 1, 1, 2, null
			}, {
				"customer", "customer2", "findForSubscribe", false, null, 2, 1, 1, 2, null
			}, {
				"customer", "customer2", "findForSubscribe", false, null, 3, 0, 1, 2, null
			}, {
				"customer", "customer2", "findForSubscribe", false, null, 1, 2, 5, 1, null
			}, {
				"user", "user1", "findForSubscribe", false, "customer2", 3, 0, 1, 2, IllegalArgumentException.class
			}, {
				"admin", "admin", "findForSubscribe", false, "customer2", 3, 0, 1, 2, IllegalArgumentException.class
			}, {
				null, null, "findForSubscribe", false, "customer2", 3, 0, 1, 2, IllegalArgumentException.class
			}, {
				"customer", "customer2", "findForSubscribe", true, null, 1, 2, 2, 1, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Test
	 * 1. Listamos los newspapers del user1 con tama�o 2 y la p�gina 1 (no salta excepci�n)
	 * 2. Listamos los newspapers del user1 con tama�o 1 y la p�gina 1 (no salta excepci�n)
	 * 3. Listamos los newspapers del user1 con tama�o 1 y la p�gina 2 (no salta excepci�n)
	 * 4. Listamos los newspapers del user1 con tama�o 1 y la p�gina 3 (no salta excepci�n)
	 * 5. Listamos los newspapers del user1 con tama�o 5 y la p�gina 1 (no salta excepci�n)
	 * 6. Listamos los newspapers del user1 logeados como customer (salta un IllegalArgumentException)
	 * 7. Listamos los newspapers del user1 logeados como admin (salta un IllegalArgumentException)
	 * 8. Listamos los newspapers del user1 sin estar logeado (salta un IllegalArgumentException)
	 * 9. Listamos los newspapers del user1 con id cero (salta un IllegalArgumentException)
	 */
	@Test()
	public void testFindByUserId() {
		final Object testingData[][] = {
			{
				"user", "user1", "findByUserId", false, null, 1, 2, 1, 2, null
			}, {
				"user", "user1", "findByUserId", false, null, 1, 1, 2, 1, null
			}, {
				"user", "user1", "findByUserId", false, null, 2, 1, 2, 1, null
			}, {
				"user", "user1", "findByUserId", false, null, 3, 0, 2, 1, null
			}, {
				"user", "user1", "findByUserId", false, null, 1, 2, 1, 5, null
			}, {
				"customer", "customer1", "findByUserId", false, "user1", 1, 2, 1, 5, IllegalArgumentException.class
			}, {
				"admin", "admin", "findByUserId", false, "user1", 1, 2, 1, 5, IllegalArgumentException.class
			}, {
				null, null, "findByUserId", false, "user1", 1, 2, 1, 5, IllegalArgumentException.class
			}, {
				"user", "user1", "findByUserId", true, null, 1, 2, 1, 2, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
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
	 * Test
	 * 1. Listamos los newspapers del customer1 con tama�o 2 y la p�gina 1 (no salta excepci�n)
	 * 2. Listamos los newspapers del customer1 con tama�o 1 y la p�gina 1 (no salta excepci�n)
	 * 3. Listamos los newspapers del customer1 con tama�o 1 y la p�gina 2 (no salta excepci�n)
	 * 4. Listamos los newspapers del customer1 con tama�o 1 y la p�gina 3 (no salta excepci�n)
	 * 5. Listamos los newspapers del customer1 con tama�o 5 y la p�gina 1 (no salta excepci�n)
	 * 6. Listamos los newspapers del customer1 logeados como user1 (salta un IllegalArgumentException)
	 * 7. Listamos los newspapers del customer1 logeados como admin (salta un IllegalArgumentException)
	 * 8. Listamos los newspapers del customer1 logeados como sin estar logeado (salta un IllegalArgumentException)
	 * 9. Listamos los newspapers de un customer con id cero logeados como user1 (salta un IllegalArgumentException)
	 */
	@Test()
	public void testFindByCustomerId() {
		final Object testingData[][] = {
			{
				"customer", "customer1", "findByCustomerId", false, null, 1, 2, 1, 2, null
			}, {
				"customer", "customer1", "findByCustomerId", false, null, 1, 1, 2, 1, null
			}, {
				"customer", "customer1", "findByCustomerId", false, null, 2, 1, 2, 1, null
			}, {
				"customer", "customer1", "findByCustomerId", false, null, 3, 0, 2, 1, null
			}, {
				"customer", "customer1", "findByCustomerId", false, null, 1, 2, 1, 5, null
			}, {
				"user", "user1", "findByCustomerId", false, "customer1", 1, 2, 1, 5, IllegalArgumentException.class
			}, {
				"admin", "admin", "findByCustomerId", false, "customer1", 1, 2, 1, 5, IllegalArgumentException.class
			}, {
				null, null, "findByCustomerId", false, "customer1", 1, 2, 1, 5, IllegalArgumentException.class
			}, {
				"customer", "customer1", "findByCustomerId", true, null, 1, 2, 1, 2, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
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
	 * Test
	 * 1. Hacemos el findPublicsAndPublicated logeados como customer1 viendo la primera p�gina con tama�o 2 (no salta excepci�n)
	 * 2. Hacemos el findPublicsAndPublicated logeados como customer1 viendo la primera p�gina con tama�o 1 (no salta excepci�n)
	 * 3. Hacemos el findPublicsAndPublicated logeados como customer1 viendo la segunda p�gina con tama�o 1 (no salta excepci�n)
	 * 4. Hacemos el findPublicsAndPublicated logeados como customer1 viendo la tercera p�gina con tama�o 1 (no salta excepci�n)
	 * 5. Hacemos el findPublicsAndPublicated logeados como customer1 viendo la primera p�gina con tama�o 5 (no salta excepci�n)
	 * 6. Hacemos el findPublicsAndPublicated logeados como user1 viendo la primera p�gina con tama�o 2 (no salta excepci�n)
	 * 7. Hacemos el findPublicsAndPublicated logeados como admin viendo la primera p�gina con tama�o 2 (no salta excepci�n)
	 * 8. Hacemos el findPublicsAndPublicated sin estar logeado viendo la primera p�gina con tama�o 2 (no salta excepci�n)
	 * 
	 * Requisitos:
	 * C.4.2: An actor who is not authenticated must be able to list the newspapers that are published and browse their articles.
	 */
	@Test()
	public void testFindPublicsAndPublicated() {
		final Object testingData[][] = {
			{
				"customer", "customer1", "findPublicsAndPublicated", false, null, 1, 2, 1, 2, null
			}, {
				"customer", "customer1", "findPublicsAndPublicated", false, null, 1, 1, 2, 1, null
			}, {
				"customer", "customer1", "findPublicsAndPublicated", false, null, 2, 1, 2, 1, null
			}, {
				"customer", "customer1", "findPublicsAndPublicated", false, null, 3, 0, 2, 1, null
			}, {
				"customer", "customer1", "findPublicsAndPublicated", false, null, 1, 2, 1, 5, null
			}, {
				"user", "user1", "findPublicsAndPublicated", false, null, 1, 2, 1, 2, null
			}, {
				"admin", "admin", "findPublicsAndPublicated", false, null, 1, 2, 1, 2, null
			}, {
				null, null, "findPublicsAndPublicated", false, null, 1, 2, 1, 2, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
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
	 * Test
	 * 1. Hacemos el findAllPaginated logeados como customer1 viendo la primera p�gina con tama�o 6 (no salta excepci�n)
	 * 2. Hacemos el findAllPaginated logeados como customer1 viendo la primera p�gina con tama�o 3 (no salta excepci�n)
	 * 3. Hacemos el findAllPaginated logeados como customer1 viendo la segunda p�gina con tama�o 3 (no salta excepci�n)
	 * 4. Hacemos el findAllPaginated logeados como customer1 viendo la tercera p�gina con tama�o 3 (no salta excepci�n)
	 * 5. Hacemos el findAllPaginated logeados como customer1 viendo la primera p�gina con tama�o 7 (no salta excepci�n)
	 * 6. Hacemos el findAllPaginated logeados como user1 viendo la primera p�gina con tama�o 6 (no salta excepci�n)
	 * 7. Hacemos el findAllPaginated logeados como admin viendo la primera p�gina con tama�o 6 (no salta excepci�n)
	 * 8. Hacemos el findAllPaginated sin estar logeadps (salta un IllegalArgumentException)
	 * 
	 * Requisitos
	 * C.4.2: An actor who is not authenticated must be able to list the newspapers that are published and browse their articles.
	 */
	@Test()
	public void testFindAllPaginated() {
		final Object testingData[][] = {
			{
				"customer", "customer1", "findAllPaginated", false, null, 1, 6, 1, 6, null
			}, {
				"customer", "customer1", "findAllPaginated", false, null, 1, 3, 2, 3, null
			}, {
				"customer", "customer1", "findAllPaginated", false, null, 2, 3, 2, 3, null
			}, {
				"customer", "customer1", "findAllPaginated", false, null, 3, 0, 2, 3, null
			}, {
				"customer", "customer1", "findAllPaginated", false, null, 1, 6, 1, 7, null
			}, {
				"user", "user1", "findAllPaginated", false, null, 1, 6, 1, 6, null
			}, {
				"admin", "admin", "findAllPaginated", false, null, 1, 6, 1, 6, null
			}, {
				null, null, "findAllPaginated", false, null, 1, 6, 1, 6, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
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
	 * Test
	 * 1. Hacemos el findTaboos logeados como admin viendo la primera p�gina con tama�o 2 (no salta excepci�n)
	 * 2. Hacemos el findTaboos logeados como admin viendo la primera p�gina con tama�o 1 (no salta excepci�n)
	 * 3. Hacemos el findTaboos logeados como admin viendo la segunda p�gina con tama�o 1 (no salta excepci�n)
	 * 4. Hacemos el findTaboos logeados como admin viendo la tercera p�gina con tama�o 1 (no salta excepci�n)
	 * 5. Hacemos el findTaboos logeados como admin viendo la primera p�gina con tama�o 3 (no salta excepci�n)
	 * 6. Hacemos el findTaboos logeados como user1 (salta un IllegalArgumentException)
	 * 7. Hacemos el findTaboos logeados como customer1 (salta un IllegalArgumentException)
	 * 8. Hacemos el findTaboos sin estar logeado (salta un IllegalArgumentException)
	 * 
	 * Requisitos
	 * 1.B.17.3: An actor who is authenticated as an administrator must be able to list the newspapers that contain taboo words.
	 */
	@Test()
	public void testFindTaboos() {
		final Object testingData[][] = {
			{
				"admin", "admin", "findTaboos", false, null, 1, 2, 1, 2, null
			}, {
				"admin", "admin", "findTaboos", false, null, 1, 1, 2, 1, null
			}, {
				"admin", "admin", "findTaboos", false, null, 2, 1, 2, 1, null
			}, {
				"admin", "admin", "findTaboos", false, null, 3, 0, 2, 1, null
			}, {
				"admin", "admin", "findTaboos", false, null, 1, 2, 1, 3, null
			}, {
				"user", "user1", "findTaboos", false, null, 1, 2, 1, 2, IllegalArgumentException.class
			}, {
				"customer", "customer1", "findTaboos", false, null, 1, 2, 1, 2, IllegalArgumentException.class
			}, {
				null, null, "findTaboos", false, null, 1, 2, 1, 2, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
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
	 * Test
	 * 1. Hacemos el findPublicsPublishedSearch sin logear buscando la keyword "a" con tama�o 2 la p�gina 1 (no salta excepci�n)
	 * 2. Hacemos el findPublicsPublishedSearch sin logear buscando la keyword "a" con tama�o 1 la p�gina 1 (no salta excepci�n)
	 * 3. Hacemos el findPublicsPublishedSearch sin logear buscando la keyword "a" con tama�o 1 la p�gina 2 (no salta excepci�n)
	 * 4. Hacemos el findPublicsPublishedSearch sin logear buscando la keyword "a" con tama�o 1 la p�gina 3 (no salta excepci�n)
	 * 5. Hacemos el findPublicsPublishedSearch sin logear buscando la keyword "a" con tama�o 3 la p�gina 1 (no salta excepci�n)
	 * 6. Hacemos el findPublicsPublishedSearch sin logear buscando la keyword "proximo" con tama�o 1 la p�gina 1 (no salta excepci�n)
	 * 
	 * Requisitos
	 * C.4.5: An actor who is not authenticated must be able to search for a published newspaper using a single keyword that must appear somewhere
	 * in its title or its description
	 */
	@Test()
	public void testFindPublicsPublishedSearch() {
		final Object testingData[][] = {
			{
				null, null, "findPublicsPublishedSearch", false, "a", 1, 2, 1, 2, null
			}, {
				null, null, "findPublicsPublishedSearch", false, "a", 1, 1, 2, 1, null
			}, {
				null, null, "findPublicsPublishedSearch", false, "a", 2, 1, 2, 1, null
			}, {
				null, null, "findPublicsPublishedSearch", false, "a", 3, 0, 2, 1, null
			}, {
				null, null, "findPublicsPublishedSearch", false, "a", 1, 2, 1, 3, null
			}, {
				null, null, "findPublicsPublishedSearch", false, "proximo", 1, 1, 1, 1, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
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
	 * Test
	 * 1. Hacemos el findPublishedSearch logeados como user1 buscando la keyword "a" con tama�o 3 la p�gina 1 (no salta excepci�n)
	 * 2. Hacemos el findPublishedSearch logeados como user1 buscando la keyword "a" con tama�o 2 la p�gina 1 (no salta excepci�n)
	 * 3. Hacemos el findPublishedSearch logeados como user1 buscando la keyword "a" con tama�o 2 la p�gina 2 (no salta excepci�n)
	 * 4. Hacemos el findPublishedSearch logeados como user1 buscando la keyword "a" con tama�o 2 la p�gina 3 (no salta excepci�n)
	 * 5. Hacemos el findPublishedSearch logeados como user1 buscando la keyword "a" con tama�o 4 la p�gina 1 (no salta excepci�n)
	 * 6. Hacemos el findPublishedSearch logeados como customer1 buscando la keyword "a" con tama�o 3 la p�gina 1 (no salta excepci�n)
	 * 7. Hacemos el findPublishedSearch logeados como admin buscando la keyword "a" con tama�o 3 la p�gina 1 (no salta excepci�n)
	 * 8. Hacemos el findPublishedSearch sin estar logeado buscando la keyword "a" con tama�o 3 la p�gina 1 (salta un IllegalArgumentException)
	 * 
	 * Requisitos
	 * C.4.5: An actor who is not authenticated must be able to search for a published newspaper using a single keyword that must appear somewhere
	 * in its title or its description
	 */
	@Test()
	public void testFindPublishedSearch() {
		final Object testingData[][] = {
			{
				"user", "user1", "findPublishedSearch", false, "a", 1, 3, 1, 3, null
			}, {
				"user", "user1", "findPublishedSearch", false, "a", 1, 2, 2, 2, null
			}, {
				"user", "user1", "findPublishedSearch", false, "a", 2, 1, 2, 2, null
			}, {
				"user", "user1", "findPublishedSearch", false, "a", 3, 0, 2, 2, null
			}, {
				"user", "user1", "findPublishedSearch", false, "a", 1, 3, 1, 4, null
			}, {
				"customer", "customer1", "findPublishedSearch", false, "a", 1, 3, 1, 3, null
			}, {
				"admin", "admin", "findPublishedSearch", false, "a", 1, 3, 1, 3, null
			}, {
				null, null, "findPublishedSearch", false, "a", 1, 3, 1, 3, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
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

	protected void template(final String user, final String username, final String method, final boolean falseId, final String bean, final Integer page, final Integer size, final Integer tam, final Integer numPages, final Class<?> expected) {
		Class<?> caught;
		Collection<Newspaper> newspapers;
		int totalPages;
		int customerId;

		caught = null;
		newspapers = null;
		customerId = 0;
		try {
			if (user != null)
				super.authenticate(username); //Nos logeamos si es necesario

			if (method.equals("findAll"))
				newspapers = this.newspaperService.findAll(); //Cogemos todos los newspapers usando el findAll
			else if (method.equals("findForSubscribe")) {
				if (user != null && user.equals("customer"))
					customerId = super.getEntityId(username);
				if (falseId == false) {
					if (user != null && user.equals("customer"))
						newspapers = this.newspaperService.findForSubscribe(customerId, page, tam); //Si estamos como customer pillar los del logeado
					else
						newspapers = this.newspaperService.findForSubscribe(super.getEntityId(bean), page, tam); //Si no pillar la del customer que le pasas
				} else
					newspapers = this.newspaperService.findForSubscribe(0, page, tam);

				totalPages = this.newspaperService.countFindForSubscribe(customerId);
				totalPages = (int) Math.floor(((totalPages / (tam + 0.0)) - 0.1) + 1);
				Assert.isTrue(totalPages == numPages);
			}

			Assert.isTrue(newspapers.size() == size); //Se compara el tama�o con el esperado
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

	protected void template2(final String user, final String username, final String method, final boolean falseId, final String bean, final Integer page, final Integer size, final Integer totalPage, final Integer tam, final Class<?> expected) {
		Class<?> caught;
		Page<Newspaper> newspapers;
		int userId;
		int customerId;

		caught = null;
		newspapers = null;
		userId = 0;
		customerId = 0;
		try {
			if (user != null)
				super.authenticate(username); //Nos logeamos si es necesario

			if (method.equals("findByUserId")) {
				if (user != null && user.equals("user"))
					userId = super.getEntityId(username);
				if (falseId == false) {
					if (user != null && user.equals("user"))
						newspapers = this.newspaperService.findByUserId(userId, page, tam); //Todos los newspapers del user logeado
					else
						newspapers = this.newspaperService.findByUserId(super.getEntityId(bean), page, tam); //Los newspapers de user que paas
				} else
					newspapers = this.newspaperService.findByUserId(0, page, tam); //Los newspapers con id cero
			} else if (method.equals("findByCustomerId")) {
				if (falseId == false) {
					if (user != null && user.equals("customer")) {
						customerId = super.getEntityId(username);
						newspapers = this.newspaperService.findByCustomerId(customerId, page, tam); //Los newspapers del customer logeado
					} else
						newspapers = this.newspaperService.findByUserId(super.getEntityId(bean), page, tam); //LOs newspapers del customer dado
				} else
					newspapers = this.newspaperService.findByUserId(0, page, tam);
			} else if (method.equals("findPublicsAndPublicated"))
				newspapers = this.newspaperService.findPublicsAndPublicated(page, tam); //FindAllPublicated
			else if (method.equals("findAllPaginated"))
				newspapers = this.newspaperService.findAllPaginated(page, tam); //FindAllPaginated
			else if (method.equals("findTaboos"))
				newspapers = this.newspaperService.findTaboos(page, tam); //FindTaboos
			else if (method.equals("findPublicsPublishedSearch"))
				newspapers = this.newspaperService.findPublicsPublishedSearch(bean, page, tam); //FindPublicsPublishedSearch
			else if (method.equals("findPublishedSearch"))
				newspapers = this.newspaperService.findPublishedSearch(bean, page, tam); //FindPublishedSearch

			Assert.isTrue(newspapers.getContent().size() == size); //Se compara el tama�o con el esperado
			Assert.isTrue(newspapers.getTotalPages() == totalPage);//Se compara el total de p�ginas con las esperadas

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

}
