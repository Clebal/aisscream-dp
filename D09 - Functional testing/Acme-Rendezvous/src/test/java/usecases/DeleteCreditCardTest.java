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
	 * Pruebas:
	 * 
	 * 	Primero se realizarán las pruebas desde un listado y luego
	 * como si accedemos a la entidad desde getEntityId:
	 * 1. Probando que el user2 borra la creditCard2
	 * 	2. Probando que el user4 borra la creditCard6
	 * 
	 * Requisitos:
	 * 
	 * An actor who is authenticated as a user must be able to request a service for 
	 * one of the rendezvouses that he or sheâ€™s created. He or she must specify a 
	 * valid credit card in every request for a service. Optionally, he or she can 
	 * provide some comments in the request. 
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
	
	for (int i = 0; i < testingData.length; i++)
		try {
			super.startTransaction();
			this.templateNoList((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		} finally {
			super.rollbackTransaction();
		}
	}
	
	/*
	 * Pruebas:
	 * 
	 * Primero se realizarán las pruebas desde un listado y luego
	 * como si accedemos a la entidad desde getEntityId:
	 * 1. Solo puede borrarlo un user
	 * 2. Solo puede borrarlo un user
	 * 3. Solo puede borrarlo un user
	 * 4. El user logueado debe ser el creador del rendezvous del request
	 * 5. El rendezvous no puede estar borrado
	 * 6. El rendezvous no puede estar borrado
	 * 
	 * Requisitos:
	 * 
	 * An actor who is authenticated as a user must be able to request a service for 
	 * one of the rendezvouses that he or sheâ€™s created. He or she must specify a 
	 * valid credit card in every request for a service. Optionally, he or she can 
	 * provide some comments in the request. 
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
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateNoList((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

		/*
		 * 	Pasos:
		 * 
		 * 1. Nos autentificamos como user
		 * 2. Tomamos el id y la entidad de user
		 * 3. Accedemos a la lista de creditCards y tomamos la que nos interesa
		 * 4. Borramos la creditCard
		 * 5. Nos desautentificamos
		 * 6. Comprobamos que no existe la creditCard borrada
		 */
	protected void template(final String user, final String creditCard, final Class<?> expected) {
		Class<?> caught;
		int userId, creditCardId;
		User userEntity;
		CreditCard creditCardEntity;
		Collection<CreditCard> creditCards;

		creditCardEntity = null;
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
			this.creditCardService.delete(creditCardEntity);
			super.unauthenticate();
			
			Assert.isTrue(!this.creditCardService.findAll().contains(creditCardEntity));

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	/*
	 * 	Pasos:
	 * 
	 * 1. Nos autentificamos como user
	 * 2. Tomamos el id y la entidad de user y de creditCard
	 * 3. Borramos la creditCard
	 * 4. Nos desautentificamos
	 * 5. Comprobamos que no existe la creditCard borrada
	 */
	protected void templateNoList(final String user, final String creditCard, final Class<?> expected) {
		Class<?> caught;
		int creditCardId;
		CreditCard creditCardEntity = null;

		caught = null;
		try {
			super.authenticate(user);
			creditCardId = super.getEntityId(creditCard);
			Assert.notNull(creditCardId);

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

