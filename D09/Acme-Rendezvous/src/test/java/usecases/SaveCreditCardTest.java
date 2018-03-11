package usecases;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

	@Test
	public void positiveTest() {
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
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (int) testingData[i][4], (int) testingData[i][5], (int) testingData[i][6], (String) testingData[i][7],(Class<?>) testingData[i][8]);
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
				null, "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, "user1", IllegalArgumentException.class // Solo puede crearlo un user
			}, 	{
				"manager1", "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, "user1", IllegalArgumentException.class // Solo puede crearlo un user
			}, {
				"user2", "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, "user1", IllegalArgumentException.class// Solo puede crearlo su user
			}, {
				"user1", "", "MasterCard", "5471664286416252", 9, 2019, 258, "user1", ConstraintViolationException.class // HolderName no puede estar vacío
			}, {
				"user1", null, "MasterCard", "5471664286416252", 9, 2019, 258, "user1", ConstraintViolationException.class // HolderName no puede ser null
			}, {
				"user5", "Estefania", "", "5429007233826913", 2, 2021, 258, "user5", ConstraintViolationException.class // BrandName no peude estar vacío
			}, {
				"user5", "Estefania", null, "5429007233826913", 2, 2021, 258, "user5", ConstraintViolationException.class // BrandName no peude ser null
			}, {
				"user4", "Manuel", "Credit Links", "1005", 5, 2017, 365, "user4", ConstraintViolationException.class // Number debe ser un número de tarjeta de crédito válido
			}, {
				"user4", "Manuel", "Credit Links", "5429007233826913", 0, 2017, 365, "user4", ConstraintViolationException.class // El mes de expiración debe estar comprendido entre 1 y 12
			}, {
				"user4", "Manuel", "Credit Links", "5429007233826913", 13, 2017, 365, "user4", ConstraintViolationException.class // El mes de expiración debe estar comprendido entre 1 y 12
			}, {
				"user3", "Paco", "American Express", "345035739479236", 4, -52, 147, "user3", ConstraintViolationException.class // El año de expiración debe ser mayor que 0 
			}, {
				"user2", "Alejandro", "Visa", "4929231012264199", 8, 2020, 50, "user2", ConstraintViolationException.class // El código CVV debe estar comprendido entre 100 y 999
			}, {
				"user2", "Alejandro", "Visa", "4929231012264199", 8, 2020, 5000, "user2", ConstraintViolationException.class // El código CVV debe estar comprendido entre 100 y 999
			}, {
				"user1", "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, "manager1", NullPointerException.class // El user debe ser válido 
			}, {
				"user1", "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, null, NullPointerException.class // El user no puede ser nulo
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (int) testingData[i][4], (int) testingData[i][5], (int) testingData[i][6], (String) testingData[i][7],(Class<?>) testingData[i][8]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void template(final String user, final String holderName, final String brandName, final String number, final int expirationMonth, final int expirationYear, final int cvvcode, final String userCredit, final Class<?> expected) {
		Class<?> caught;
		int userId;
		User userEntity;
		CreditCard creditCard;

		caught = null;
		try {
			super.authenticate(user);
			userId = super.getEntityId(userCredit);
			userEntity = this.userService.findOne(userId);
			creditCard = this.creditCardService.create(userEntity);
			creditCard.setHolderName(holderName);
			creditCard.setBrandName(brandName);
			creditCard.setNumber(number);
			creditCard.setExpirationMonth(expirationMonth);
			creditCard.setExpirationYear(expirationYear);
			creditCard.setCvvcode(cvvcode);
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

