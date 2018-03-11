package usecases;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.Request;
import services.RequestService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DeleteRequestTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private RequestService		requestService;

	// Tests ------------------------------------------------------------------

	@Test
	public void positiveTest() {
		final Object testingData[][] = {
			{
				"user1", "request1", null
			}, {
				"user3", "request4", null
			}, {
				"user1", "request7", null
			}, {
				"user3", "request2", null
			}
		};
			
	for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	
	@Test()
	public void negativeTest() {
		final Object testingData[][] = {
			{
				"admin", "request1", IllegalArgumentException.class // Solo puede borrarlo un user
			}, {
				"manager1", "request2", IllegalArgumentException.class // Solo puede borrarlo un user
			}, {
				"user1", "request5", IllegalArgumentException.class // El user logueado debe ser el creador del rendezvous del request
			}, {
				"user3", "request1", IllegalArgumentException.class // El rendezvous no puede estar borrado
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void template(final String user, final String request, final Class<?> expected) {
		Class<?> caught;
		int requestId;
		Request requestEntity;

		caught = null;
		try {
			super.authenticate(user);
			requestId = super.getEntityId(request);
			requestEntity = this.requestService.findOne(requestId);
			this.requestService.delete(requestEntity);
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
		super.checkExceptions(expected, caught);
	}

}

