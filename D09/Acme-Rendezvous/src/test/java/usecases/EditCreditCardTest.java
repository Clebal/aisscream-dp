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
public class EditCreditCardTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private CreditCardService		creditCardService;

	@Autowired
	private UserService				userService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * Probando la edición de varias creditCards por parte de diferentes usuarios
	 */
	@Test
	public void positiveEditCreditCardTest() {
		final Object testingData[][] = {
			{
				"user1", "creditCard1", "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, null //TODO: EL TESTS DA ERRORES NO DETECTADOS AÚN
			}, {
				"user2", "creditCard2", "Alejandro", "Visa", "4929231012264199", 8, 2020, 317, null
			}, {
				"user3", "creditCard1", "Paco", "American Express", "345035739479236", 4, 2018, 147, null
			}, {
				"user4", "creditCard4", "Manuel", "Credit Links", "6011516686715805", 5, 2017, 365, null
			}, {
				"user4", "creditCard5", "Estefania", "MasterCard", "5429007233826913", 2, 2021, 258, null
			}
		};
			
	for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (int) testingData[i][5], (int) testingData[i][6], (int) testingData[i][7], (Class<?>) testingData[i][8]);
			} catch (final Throwable oops) {
				System.out.println(oops.getStackTrace());
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	/*
	 * 1. Solo puede editarlo un user
	 * 2. Solo puede editarlo un user
	 * 3. Solo puede editarlo su user
	 * 4. HolderName no puede estar vacío
	 * 5. HolderName no puede ser null
	 * 6. BrandName no puede estar vacío
	 * 7. BrandName no peude ser null
	 * 8. Number debe ser un número de tarjeta de crédito válido
	 * 9. El mes de expiración debe estar comprendido entre 1 y 12
	 * 10. El mes de expiración debe estar comprendido entre 1 y 12
	 * 11. El año de expiración debe ser mayor que 0 
	 * 12. El código CVV debe estar comprendido entre 100 y 999
	 * 13. El código CVV debe estar comprendido entre 100 y 999
	 */
	//@Test()
	public void negativeEditCreditCardTest() {
		final Object testingData[][] = {
			{
				null, "creditCard1", "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, IllegalArgumentException.class 
			}, 	{
				"manager1", "creditCard1", "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, IllegalArgumentException.class 
			}, {
				"user2", "creditCard1", "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, IllegalArgumentException.class
			}, {
				"user1", "creditCard1", "", "MasterCard", "5471664286416252", 9, 2019, 258, IllegalArgumentException.class 
			}, {
				"user1", "creditCard1", null, "MasterCard", "5471664286416252", 9, 2019, 258, IllegalArgumentException.class 
			}, {
				"user2", "creditCard2", "Estefania", "", "5429007233826913", 2, 2021, 258, IllegalArgumentException.class
			}, {
				"user2", "creditCard2", "Estefania", null, "5429007233826913", 2, 2021, 258, IllegalArgumentException.class 
			}, {
				"user4", "creditCard4", "Manuel", "Credit Links", "1005", 5, 2017, 365, IllegalArgumentException.class 
			}, {
				"user4", "creditCard4", "Manuel", "Credit Links", "5429007233826913", 0, 2017, 365, IllegalArgumentException.class
			}, {
				"user2", "creditCard2", "Manuel", "Credit Links", "5429007233826913", 13, 2017, 365, IllegalArgumentException.class 
			}, {
				"user1", "creditCard1", "Paco", "American Express", "345035739479236", 4, -52, 147, IllegalArgumentException.class 
			}, {
				"user2", "creditCard2", "Alejandro", "Visa", "4929231012264199", 8, 2020, 50, IllegalArgumentException.class 
			}, {
				"user2", "creditCard2", "Alejandro", "Visa", "4929231012264199", 8, 2020, 5000, IllegalArgumentException.class 
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (int) testingData[i][5], (int) testingData[i][6], (int) testingData[i][7], (Class<?>) testingData[i][8]);
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
	protected void template(final String user, final String creditCardEdit, final String holderName, final String brandName, final String number, final int expirationMonth, final int expirationYear, final int cvvcode, final Class<?> expected) {
		Class<?> caught;
		Integer userId, creditCardId;
		User userEntity;
		Collection<CreditCard> creditCards;
		CreditCard creditCard, creditCardEntity;

		creditCard = null;
		caught = null;
		try {
			super.authenticate(user);
			Assert.notNull(user);
			userId = super.getEntityId(user);
			userEntity = this.userService.findOne(userId);
			Assert.notNull(userEntity);
			Assert.notNull(creditCard);
			creditCardId = super.getEntityId(creditCardEdit);
			Assert.notNull(creditCardId);
			creditCardEntity = this.creditCardService.findOne(creditCardId);
			Assert.notNull(creditCardEntity);
			creditCards = this.creditCardService.findByUserAccountId(userEntity.getUserAccount().getId(), 1, 10).getContent();
			System.out.println(creditCards);
			for (CreditCard c : creditCards) {
				if(c.equals(creditCardEntity)){
					creditCard = c;
					break;
				}
			}
			System.out.println(creditCard);
			creditCard.setHolderName(holderName);
			creditCard.setBrandName(brandName);
			creditCard.setNumber(number);
			creditCard.setExpirationMonth(expirationMonth);
			creditCard.setExpirationYear(expirationYear);
			creditCard.setCvvcode(cvvcode);
			creditCard.setUser(userEntity);
			this.creditCardService.save(creditCard);
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
