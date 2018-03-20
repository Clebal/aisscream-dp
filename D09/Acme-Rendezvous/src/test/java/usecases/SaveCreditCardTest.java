package usecases;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

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
public class SaveCreditCardTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private CreditCardService		creditCardService;

	@Autowired
	private UserService				userService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * Probando la creación de varias creditCards por parte de diferentes usuarios
	 */
	@Test
	public void positiveSaveCreditCardTest() {
		final Object testingData[][] = {
			{
				"user1", "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, "user1", null
			}, {
				"user2", "Alejandro", "Visa", "4929231012264199", 8, 2020, 317, "user2", null
			}, {
				"user3", "Paco", "American Express", "345035739479236", 4, 2018, 147, "user3", null
			}, {
				"user4", "Manuel", "Credit Links", "6011516686715805", 5, 2017, 365, "user4", null
			}, {
				"user5", "Estefania", "MasterCard", "5429007233826913", 2, 2021, 258, "user5", null
			}
		};
			
	for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (int) testingData[i][4], (int) testingData[i][5], (int) testingData[i][6], (String) testingData[i][7],(Class<?>) testingData[i][8]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	/*
	 * 1. Solo puede crearlo un user
	 * 2. Solo puede crearlo un user
	 * 3. Solo puede crearlo su user
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
	 * 14. El user debe ser válido 
	 * 15. El user no puede ser nulo
	 */
	@Test()
	public void negativeSaveCreditCardTest() {
		final Object testingData[][] = {
			{
				null, "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, "user1", IllegalArgumentException.class 
			}, 	{
				"manager1", "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, "user1", IllegalArgumentException.class 
			}, {
				"user2", "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, "user1", IllegalArgumentException.class
			}, {
				"user1", "", "MasterCard", "5471664286416252", 9, 2019, 258, "user1", ConstraintViolationException.class 
			}, {
				"user1", null, "MasterCard", "5471664286416252", 9, 2019, 258, "user1", ConstraintViolationException.class 
			}, {
				"user5", "Estefania", "", "5429007233826913", 2, 2021, 258, "user5", ConstraintViolationException.class
			}, {
				"user5", "Estefania", null, "5429007233826913", 2, 2021, 258, "user5", ConstraintViolationException.class 
			}, {
				"user4", "Manuel", "Credit Links", "1005", 5, 2017, 365, "user4", ConstraintViolationException.class 
			}, {
				"user4", "Manuel", "Credit Links", "5429007233826913", 0, 2017, 365, "user4", ConstraintViolationException.class
			}, {
				"user4", "Manuel", "Credit Links", "5429007233826913", 13, 2017, 365, "user4", ConstraintViolationException.class 
			}, {
				"user3", "Paco", "American Express", "345035739479236", 4, -52, 147, "user3", ConstraintViolationException.class 
			}, {
				"user2", "Alejandro", "Visa", "4929231012264199", 8, 2020, 50, "user2", ConstraintViolationException.class 
			}, {
				"user2", "Alejandro", "Visa", "4929231012264199", 8, 2020, 5000, "user2", ConstraintViolationException.class 
			}, {
				"user1", "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, "manager1", IllegalArgumentException.class 
			}, {
				"user1", "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, null, IllegalArgumentException.class 
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (int) testingData[i][4], (int) testingData[i][5], (int) testingData[i][6], (String) testingData[i][7],(Class<?>) testingData[i][8]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	/*
	 * An actor who is authenticated as a user must be able to request a service for 
	 * one of the rendezvouses that he or sheâ€™s created. He or she must specify a 
	 * valid credit card in every request for a service. Optionally, he or she can 
	 * provide some comments in the request. 
	 */
	protected void template(final String user, final String holderName, final String brandName, final String number, final int expirationMonth, final int expirationYear, final int cvvcode, final String userCredit, final Class<?> expected) {
		Class<?> caught;
		Integer userId;
		User userEntity;
		CreditCard creditCard, creditCardEntity;

		caught = null;
		try {
			super.authenticate(user);
			Assert.notNull(userCredit);
			userId = super.getEntityId(userCredit);
			Assert.notNull(userId);
			userEntity = this.userService.findOne(userId);
			Assert.notNull(userEntity);
			creditCard = this.creditCardService.create(userEntity);
			creditCard.setHolderName(holderName);
			creditCard.setBrandName(brandName);
			creditCard.setNumber(number);
			creditCard.setExpirationMonth(expirationMonth);
			creditCard.setExpirationYear(expirationYear);
			creditCard.setCvvcode(cvvcode);
			creditCardEntity = this.creditCardService.save(creditCard);
			super.unauthenticate();
			super.flushTransaction();
			
			Assert.isTrue(this.creditCardService.findAll().contains(creditCardEntity));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}

