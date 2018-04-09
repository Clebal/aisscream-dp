
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
				System.out.println(i);
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

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
				System.out.println(i);
				this.template2((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

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
				System.out.println(i);
				this.template2((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

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
				System.out.println(i);
				this.template2((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
					(Integer) testingData[i][7], (Integer) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

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
				System.out.println(i);
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
				newspapers = this.newspaperService.findAll(); //Cogemos todos los servicios usando el findAll
			else if (method.equals("findForSubscribe")) {
				if (user != null && user.equals("customer"))
					customerId = super.getEntityId(username);
				if (falseId == false) {
					if (user != null && user.equals("customer"))
						newspapers = this.newspaperService.findForSubscribe(customerId, page, tam); //Si estamos como un manager cogemos todos sus sevicios
					else
						newspapers = this.newspaperService.findForSubscribe(super.getEntityId(bean), page, tam); //Si estamos como un manager cogemos todos sus sevicios
				} else
					newspapers = this.newspaperService.findForSubscribe(0, page, tam);

				totalPages = this.newspaperService.countFindForSubscribe(customerId);
				totalPages = (int) Math.floor(((totalPages / (tam + 0.0)) - 0.1) + 1);
				Assert.isTrue(totalPages == numPages);
			}

			Assert.isTrue(newspapers.size() == size); //Se compara el tamaño con el esperado
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println(caught);
		System.out.println(expected);
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
						newspapers = this.newspaperService.findByUserId(userId, page, tam);
					else
						newspapers = this.newspaperService.findByUserId(super.getEntityId(bean), page, tam);
				} else
					newspapers = this.newspaperService.findByUserId(0, page, tam);
			} else if (method.equals("findByCustomerId")) {
				if (falseId == false) {
					if (user != null && user.equals("customer")) {
						customerId = super.getEntityId(username);
						newspapers = this.newspaperService.findByCustomerId(customerId, page, tam);
					} else
						newspapers = this.newspaperService.findByUserId(super.getEntityId(bean), page, tam);
				} else
					newspapers = this.newspaperService.findByUserId(0, page, tam);
			} else if (method.equals("findPublicsAndPublicated"))
				newspapers = this.newspaperService.findPublicsAndPublicated(page, tam);
			else if (method.equals("findAllPaginated"))
				newspapers = this.newspaperService.findAllPaginated(page, tam);

			Assert.isTrue(newspapers.getContent().size() == size); //Se compara el tamaño con el esperado
			Assert.isTrue(newspapers.getTotalPages() == totalPage);//Se compara el total de páginas con las esperadas

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		System.out.println(caught);
		System.out.println(expected);
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

}
