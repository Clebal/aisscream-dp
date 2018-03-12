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

	/*
	 * 1. Probando que el user2 borra la creditCard2
	 */
	@Test
	public void positiveDeleteCreditCardTest() {
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
	
	/*
	 * 1. Solo puede borrarlo un user
	 * 2. Solo puede borrarlo un user
	 * 3. Solo puede borrarlo un user
	 * 4. El user logueado debe ser el creador del rendezvous del request
	 * 5. El rendezvous no puede estar borrado
	 * 6. El rendezvous no puede estar borrado
	 */
	@Test()
	public void negativeDeleteCreditCardTest() {
		final Object testingData[][] = {
			{
				null, "creditCard1", IllegalArgumentException.class 
			}, 	{
				"admin", "creditCard1", IllegalArgumentException.class
			}, {
				"manager1", "creditCard2", IllegalArgumentException.class 
			}, {
				"user1", "creditCard2", IllegalArgumentException.class 
			}, {
				"user3", "creditCard1", IllegalArgumentException.class 
			}, {
				"user4", "creditCard3", IllegalArgumentException.class 
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

	/*
	 * An actor who is authenticated as a user must be able to request a service for 
	 * one of the rendezvouses that he or she’s created. He or she must specify a 
	 * valid credit card in every request for a service. Optionally, he or she can 
	 * provide some comments in the request. 
	 */
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

