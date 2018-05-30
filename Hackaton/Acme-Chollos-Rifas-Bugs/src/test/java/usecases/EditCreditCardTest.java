package usecases;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.DataBinder;

import domain.CreditCard;
import domain.User;
import security.LoginService;
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
	private UserService 		userService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 		1.
	 * 		2.
	 *
	 * Requisitos:
	 * 		
	 * 
	 */
	@Test
	public void driverPositiveTest() {		
		final Object testingData[][] = {
			{
				"user1", "creditCard1", "Alejandro Martín", "MasterCard", "5256332484910150", 05, 20, 533, null
			}, {
				"user3", "creditCard4", "Manuel Macías", "Visa", "4811183747278021", 12, 30, 432, null
			}
		};
			
	for (int i = 0; i < testingData.length; i++)
		try {
			super.startTransaction();
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (int) testingData[i][5], (int) testingData[i][6], (int) testingData[i][7], (Class<?>) testingData[i][8]);
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		} finally {
			super.rollbackTransaction();
		}
	}
	
	/*
	 * Pruebas:
	 * 		1. 
	 * 		2. 
	 * 		3. 
	 * 		4.
	 * 		5.
	 * 		6.
	 * 		7.
	 * 		8.
	 * 		9.
	 * 
	 * Requisitos:
	 * 		
	 * 
	 */
	@Test
	public void driverNegativeTest() {		
		final Object testingData[][] = {
				{
					"user1", "creditCard4", "Alejandro Martín", "MasterCard", "5256332484910150", 05, 20, 533, IllegalArgumentException.class
				}, {
					"user3", "creditCard1", "Manuel Macías", "Visa", "4811183747278021", 12, 30, 432, IllegalArgumentException.class
				}, {
					"user1", "creditCard1", "Alejandro Martín", "MasterCard", "asdf", 05, 20, 533, ConstraintViolationException.class
				}, {
					"user1", "creditCard1", "Alejandro Martín", "MasterCard", "5256332484910150", 03, 18, 533, IllegalArgumentException.class
				}, {
					"user1", "creditCard1", "Alejandro Martín", "MasterCard", "5256332484910150", 05, 18, 533, IllegalArgumentException.class
				}, {
					"user1", "creditCard1", "Alejandro Martín", "MasterCard", "5256332484910150", 9, 18, 99, ConstraintViolationException.class
				}, {
					"company1", "creditCard1", "Alejandro Martín", "MasterCard", "5256332484910150", 9, 18, 101, IllegalArgumentException.class
				}, {
					"sponsor1", "creditCard1", "Alejandro Martín", "MasterCard", "5256332484910150", 9, 18, 101, IllegalArgumentException.class
				}, {
					"moderator1", "creditCard1", "Alejandro Martín", "MasterCard", "5256332484910150", 9, 18, 101, IllegalArgumentException.class
				}
			};
		
		for (int i = 0; i < testingData.length; i++)
			try {
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
	 * Crear una tarjeta de crédito
	 * Pasos:
	 * 		1. Autenticar como usuario
	 * 		2. Listar tarjeta de crédito
	 * 		3. Entrar en vista de crear tarjeta de crédito
	 * 		4. Crear tarjeta de crédito
	 * 		5. Volver al listado de rifas
	 */
	protected void template(final String actorBean, final String creditCardBean, final String holderName, final String brandName, final String number, final int expirationMonth, final int expirationYear, final int cvvcode, final Class<?> expected) {
		Class<?> caught;
		Page<CreditCard> creditCardPage;
		Integer userId, creditCardId;
		DataBinder binder;
		User user;
		CreditCard creditCard, newCreditCard, reconstructedCreditCard;
		
		caught = null;
		try {
			
			// 1. Autenticar como usuario
			super.authenticate(actorBean);
			
			userId = super.getEntityId(actorBean);
			Assert.notNull(userId);
			
			user = this.userService.findOne(userId);
			Assert.notNull(user);
			
			creditCardId = super.getEntityId(creditCardBean);
			Assert.notNull(creditCardId);
			
			// 2. Listar tarjeta de crédito
			creditCardPage = this.creditCardService.findByUserAccountId(LoginService.getPrincipal().getId(), 1, 5);
			Assert.notNull(creditCardPage);
						
			// 3. EEntrar en vista de crear tarjeta de crédito
			creditCard = this.creditCardService.findOneToDisplayEdit(creditCardId);
			creditCard.setHolderName(holderName);
			creditCard.setBrandName(brandName);
			creditCard.setNumber(number);
			creditCard.setExpirationMonth(expirationMonth);
			creditCard.setExpirationYear(expirationYear);
			creditCard.setCvvcode(cvvcode);
			
			// 4. Crear tarjeta de crédito
			binder = new DataBinder(creditCard);
			reconstructedCreditCard = this.creditCardService.reconstruct(creditCard, binder.getBindingResult());
			this.creditCardService.save(reconstructedCreditCard);
			
			this.creditCardService.flush();
									
			// 5. Volver al listado de rifas
			creditCardPage = this.creditCardService.findByUserAccountId(LoginService.getPrincipal().getId(), 1, 5);
			Assert.notNull(creditCardPage);
			newCreditCard = this.creditCardService.findOne(creditCardId);
			Assert.notNull(newCreditCard);
			Assert.isTrue(newCreditCard.getHolderName().equals(holderName));
			Assert.isTrue(newCreditCard.getBrandName().equals(brandName));
			Assert.isTrue(newCreditCard.getNumber().equals(number));
			Assert.isTrue(newCreditCard.getExpirationMonth() == expirationMonth);
			Assert.isTrue(newCreditCard.getExpirationYear() == expirationYear);
			Assert.isTrue(newCreditCard.getCvvcode() == cvvcode);
			
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		
		super.checkExceptions(expected, caught);
	}

}

