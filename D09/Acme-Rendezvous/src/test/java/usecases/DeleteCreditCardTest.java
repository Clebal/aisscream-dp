package usecases;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.CreditCard;
import domain.User;
import services.CreditCardService;
import services.UserService;
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
	
	@Autowired
	private UserService				userService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * 1. Probando que el user2 borra la creditCard2
	 * 	2. Probando que el user4 borra la creditCard6
	 */
	@Test
	public void positiveDeleteCreditCardTest() {
		final Object testingData[][] = {
			{
				"user2", "creditCard2", null
			}, {
				"user4", "creditCard6", null
			}
		};
			
	for (int i = 0; i < testingData.length; i++)
			try {
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
				"administrator", "creditCard1", IllegalArgumentException.class
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
		int userId, creditCardId;
		User userEntity;
		CreditCard creditCardEntity;
		Collection<CreditCard> creditCards;

		caught = null;
		try {
			super.authenticate(user);
			Assert.notNull(user);
			userId = super.getEntityId(user);
			userEntity = this.userService.findOne(userId);
			Assert.notNull(userEntity);
			creditCardId = super.getEntityId(creditCard);
			Assert.notNull(creditCardId);
			creditCards = this.creditCardService.findByUserAccountId(userEntity.getUserAccount().getId(), 1, 5).getContent();
			for (CreditCard c : creditCards) {
				if(c.getId() == creditCardId){
					creditCardEntity = c;
					break;
				}
			}
			creditCardEntity = this.creditCardService.findOneToEdit(creditCardId);
			this.creditCardService.delete(creditCardEntity);
			super.unauthenticate();
			
			Assert.isTrue(!this.creditCardService.findAll().contains(creditCardEntity));

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}

