package usecases;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.CreditCard;
import services.CreditCardService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DeleteCreditCardTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private CreditCardService		creditCardService;

	// Tests ------------------------------------------------------------------

	@Test
	public void positiveTest() {
		final Object testingData[][] = {
			{
				"user2", "creditCard2", null
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
				null, "creditCard1", IllegalArgumentException.class // Solo puede borrarlo un user
			}, 	{
				"admin", "creditCard1", IllegalArgumentException.class // Solo puede borrarlo un user
			}, {
				"manager1", "creditCard2", IllegalArgumentException.class // Solo puede borrarlo un user
			}, {
				"user1", "creditCard2", IllegalArgumentException.class // El user logueado debe ser el creador del rendezvous del request
			}, {
				"user3", "creditCard1", IllegalArgumentException.class // El rendezvous no puede estar borrado
			}, {
				"user4", "creditCard3", IllegalArgumentException.class // El rendezvous no puede estar borrado
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

	protected void template(final String user, final String creditCard, final Class<?> expected) {
		Class<?> caught;
		int creditCardId;
		CreditCard creditCardEntity;

		caught = null;
		try {
			super.authenticate(user);
			creditCardId = super.getEntityId(creditCard);
			creditCardEntity = this.creditCardService.findOneToEdit(creditCardId);
			this.creditCardService.delete(creditCardEntity);
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

