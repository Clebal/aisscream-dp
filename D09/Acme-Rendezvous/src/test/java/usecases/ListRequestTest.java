
package usecases;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.RequestService;
import utilities.AbstractTest;
import domain.Request;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ListRequestTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private RequestService		requestService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * 1. Probamos obtener el resultado previsto para el metodo findAll logueados como user1
	 * 	2. Probamos obtener el resultado previsto para el metodo findAll sin loguear
	 * 3. Probamos obtener el resultado previsto para el metodo findAll logueados como manager2
	 */
	@Test()
	public void testFindAll() {
		final Object testingData[][] = {
			{
				"user1", "findAll", 8, 0, 0, null
			}, {
				null, "findAll", 8, 0, 0, null
			}, {
				"manager2", "findAll", 8, 0, 0, null
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (Integer) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Class<?>) testingData[i][5]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * 1. Probamos obtener el resultado previsto para el metodo findAllPaginated logueados como user1, para la página 1 y el tamaño 5
	 * 	2. Probamos obtener el resultado previsto para el metodo findAllPaginated sin loguear, para la página 2 y el tamaño 4
	 * 3. Probamos obtener el resultado previsto para el metodo findAllPaginated logueados como user2, para la página 2 y el tamaño 3
	 * 4. Probamos no poder obtener el resultado previsto para el metodo findAllPaginated logueados como un manager
	 * 5. Probamos no poder obtener el resultado previsto para el metodo findAllPaginated logueados como un admin
	 */
	@Test()
	public void testFindByUserAccountId() {
		final Object testingData[][] = {
				{
					"user1", "findAllPaginated", 4, 1, 5, null
				}, {
					null, "findAllPaginated", 0, 0, 4, IllegalArgumentException.class
				}, {
					"user3", "findAllPaginated", 4, 1, 10, null
				}, {
					"manager2", "findAllPaginated", 0, 2, 1, IllegalArgumentException.class
				}, {
					"administrator", "findAllPaginated", 1, 1, 5, IllegalArgumentException.class
				}

		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (Integer) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Class<?>) testingData[i][5]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}

	}

	// Ancillary methods ------------------------------------------------------

	/*
	 * An actor who is authenticated as a user must be able to request 
	 * a service for one of the rendezvouses that he or she’s created. He 
	 * or she must specify a valid credit card in every request for a service. 
	 * Optionally, he or she can provide some comments in the request. 
	*/
	protected void template(final String user, final String method, final Integer tamano, final int page, final int size, final Class<?> expected) {
		Class<?> caught;
		Collection<Request> requests;
		int userId;

		caught = null;
		try {
			super.authenticate(user);

			if (method.equals("findAll")) {
				requests = this.requestService.findAll();
			} else {
				Assert.notNull(user);
				userId = super.getEntityId(user);
				requests = this.requestService.findAllPaginated(userId, page, size);
			}
			Assert.isTrue(requests.size() == tamano); 
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
