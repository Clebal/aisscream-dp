package usecases;

import java.util.Collection;

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
public class EditCreditCardTest extends AbstractTest {

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
	 * Probamos la edicion de varias creditCards por parte de diferentes usuarios
	 * 
	 * Requsitos:
	 * 
	 * 	An actor who is authenticated as a user must be able to request a service for 
	 * one of the rendezvouses that he or shes created. He or she must specify a 
	 * valid credit card in every request for a service. Optionally, he or she can 
	 * provide some comments in the request. 
	 */
	@Test
	public void positiveEditCreditCardTest() {
		final Object testingData[][] = {
			{	
				"user1", "creditCard1", "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, null
			}, {
				"user2", "creditCard2", "Alejandro", "Visa", "4929231012264199", 8, 2020, 317, null
			}, {
				"user3", "creditCard4", "Paco", "American Express", "345035739479236", 4, 2018, 147, null
			}, {
				"user4", "creditCard5", "Manuel", "Credit Links", "6011516686715805", 5, 2017, 365, null
			}, {
				"user4", "creditCard5", "Estefania", "MasterCard", "5429007233826913", 2, 2021, 258, null
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
	
	for (int i = 0; i < testingData.length; i++)
		try {
			super.startTransaction();
			this.templateNoList((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (int) testingData[i][5], (int) testingData[i][6], (int) testingData[i][7], (Class<?>) testingData[i][8]);
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		} finally {
			super.rollbackTransaction();
		}
	}
	
	/*
	 * Pruebas:
	 * 
	 * 	Primero se realizarán las pruebas desde un listado y luego
	 * como si accedemos a la entidad desde getEntityId:
	 * 1. Solo puede editarlo un user
	 * 2. Solo puede editarlo un user
	 * 3. Solo puede editarlo su user
	 * 4. HolderName no puede estar vacio
	 * 5. HolderName no puede ser null
	 * 6. BrandName no puede estar vacio
	 * 7. BrandName no peude ser null
	 * 8. Number debe ser un numero de tarjeta de credito valido
	 * 9. El mes de expiracion debe estar comprendido entre 1 y 12
	 * 10. El mes de expiracion debe estar comprendido entre 1 y 12
	 * 11. El ano de expiracion debe ser mayor que 0 
	 * 12. El codigo CVV debe estar comprendido entre 100 y 999
	 * 13. El codigo CVV debe estar comprendido entre 100 y 999
	 * 
	 * Requisitos:
	 * 
	 * 	An actor who is authenticated as a user must be able to request a service for 
	 * one of the rendezvouses that he or shes created. He or she must specify a 
	 * valid credit card in every request for a service. Optionally, he or she can 
	 * provide some comments in the request. 
	 */
	@Test()
	public void negativeEditCreditCardTest() {
		final Object testingData[][] = {
			{
				null, "creditCard1", "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, IllegalArgumentException.class 
			}, 	{
				"manager1", "creditCard1", "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, IllegalArgumentException.class 
			}, {
				"user2", "creditCard1", "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, IllegalArgumentException.class
			}, {
				"user1", "creditCard1", "", "MasterCard", "5471664286416252", 9, 2019, 258, ConstraintViolationException.class 
			}, {
				"user1", "creditCard1", null, "MasterCard", "5471664286416252", 9, 2019, 258, ConstraintViolationException.class 
			}, {
				"user2", "creditCard2", "Estefania", "", "5429007233826913", 2, 2021, 258, ConstraintViolationException.class
			}, {
				"user2", "creditCard2", "Estefania", null, "5429007233826913", 2, 2021, 258, ConstraintViolationException.class 
			}, {
				"user4", "creditCard5", "Manuel", "Credit Links", "1005", 5, 2017, 365, ConstraintViolationException.class 
			}, {
				"user4", "creditCard5", "Manuel", "Credit Links", "5429007233826913", 0, 2017, 365, ConstraintViolationException.class
			}, {
				"user2", "creditCard2", "Manuel", "Credit Links", "5429007233826913", 13, 2017, 365, ConstraintViolationException.class 
			}, {
				"user1", "creditCard1", "Paco", "American Express", "345035739479236", 4, -52, 147, ConstraintViolationException.class 
			}, {
				"user2", "creditCard2", "Alejandro", "Visa", "4929231012264199", 8, 2020, 50, ConstraintViolationException.class 
			}, {
				"user2", "creditCard2", "Alejandro", "Visa", "4929231012264199", 8, 2020, 5000, ConstraintViolationException.class 
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
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateNoList((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (int) testingData[i][5], (int) testingData[i][6], (int) testingData[i][7], (Class<?>) testingData[i][8]);
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
	 * 4. Le creamos una copia para que no se guarde solo con un set
	 * 5. Le asignamos el holderName, el brandName, el number, la expirationMonth y el cvvCode correspondientes
	 * 6. Guardamos la creditCard copiada con los parámetros
	 * 7. Nos desautentificamos
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
			creditCardId = super.getEntityId(creditCardEdit);
			Assert.notNull(creditCardId);
			creditCards = this.creditCardService.findByUserAccountId(userEntity.getUserAccount().getId(), 1, 5).getContent();
			for (CreditCard c : creditCards) {
				if(c.getId() == creditCardId){
					creditCard = c;
					break;
				}
			}
			Assert.notNull(creditCard);
			creditCardEntity = this.copyCreditCard(creditCard);
			creditCardEntity.setHolderName(holderName);
			creditCardEntity.setBrandName(brandName);
			creditCardEntity.setNumber(number);
			creditCardEntity.setExpirationMonth(expirationMonth);
			creditCardEntity.setExpirationYear(expirationYear);
			creditCardEntity.setCvvcode(cvvcode);
			creditCardEntity.setUser(userEntity);
			this.creditCardService.save(creditCardEntity);
			super.unauthenticate();
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
	 * 2. Tomamos el id y la entidad de user y creditCard
	 * 3. Le creamos una copia para que no se guarde solo con un set
	 * 4. Le asignamos el holderName, el brandName, el number, la expirationMonth y el cvvCode correspondientes
	 * 5. Guardamos la creditCard copiada con los parámetros
	 * 6. Nos desautentificamos
	 */
	protected void templateNoList(final String user, final String creditCardEdit, final String holderName, final String brandName, final String number, final int expirationMonth, final int expirationYear, final int cvvcode, final Class<?> expected) {
		Class<?> caught;
		Integer userId, creditCardId;
		User userEntity;
		CreditCard creditCard, creditCardEntity;

		creditCard = null;
		caught = null;
		try {
			super.authenticate(user);
			Assert.notNull(user);
			userId = super.getEntityId(user);
			userEntity = this.userService.findOne(userId);
			Assert.notNull(userEntity);
			creditCardId = super.getEntityId(creditCardEdit);
			Assert.notNull(creditCardId);
			creditCard = this.creditCardService.findOneToEdit(creditCardId);
			Assert.notNull(creditCard);
			creditCardEntity = this.copyCreditCard(creditCard);
			creditCardEntity.setHolderName(holderName);
			creditCardEntity.setBrandName(brandName);
			creditCardEntity.setNumber(number);
			creditCardEntity.setExpirationMonth(expirationMonth);
			creditCardEntity.setExpirationYear(expirationYear);
			creditCardEntity.setCvvcode(cvvcode);
			creditCardEntity.setUser(userEntity);
			this.creditCardService.save(creditCardEntity);
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private CreditCard copyCreditCard(final CreditCard creditCard) {
		CreditCard result;
	
		result = new CreditCard();
		result.setId(creditCard.getId());
		result.setVersion(creditCard.getVersion());
		result.setBrandName(creditCard.getBrandName());
		result.setCvvcode(creditCard.getCvvcode());
		result.setExpirationMonth(creditCard.getExpirationMonth());
		result.setExpirationYear(creditCard.getExpirationYear());
		result.setNumber(creditCard.getNumber());
		result.setUser(creditCard.getUser());
		result.setHolderName(creditCard.getHolderName());
		
		return result;
	}
	
}
