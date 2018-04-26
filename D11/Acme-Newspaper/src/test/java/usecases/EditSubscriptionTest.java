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

import domain.Customer;
import domain.Subscription;

import services.CustomerService;
import services.SubscriptionService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EditSubscriptionTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private SubscriptionService		subscriptionService;

	@Autowired
	private CustomerService				customerService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 
	 * 	Primero se realizarán las pruebas desde un listado y luego
	 * como si accedemos a la entidad desde getEntityId:
	 * 
	 * Probamos la edicion de varias subscriptions por parte de diferentes customers
	 * 
	 * Requsitos:
	 * 	22. An actor who is authenticated as a customer can:
		1. Subscribe to a private newspaper by providing a valid credit card.
	 * 
	 */
	@Test
	public void positiveEditSubscriptionTest() {
		final Object testingData[][] = {
			{	
				"customer1", "subscription1", "Antonio", "MasterCard", "5212472747907073", 9, 2019, 258, null
			}, {
				"customer2", "subscription2", "Alejandro", "Visa", "377564788646263", 8, 2020, 317, null
			}, {
				"customer3", "subscription4", "Paco", "American Express", "4929254799279560", 4, 2018, 147, null
			}, {
				"customer1", "subscription3", "Manuel", "Credit Links", "5212472747907073", 5, 2017, 365, null
			}, {
				"customer2", "subscription5", "Estefania", "MasterCard", "377564788646263", 2, 2021, 258, null
			}
		};
			
	for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6], (Integer) testingData[i][7], (Class<?>) testingData[i][8]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	
	for (int i = 0; i < testingData.length; i++)
		try {
			super.startTransaction();
			this.templateNoList((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6], (Integer) testingData[i][7], (Class<?>) testingData[i][8]);
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
	 * 1. Solo puede editarlo un customer
	 * 2. Solo puede editarlo un customer
	 * 3. Solo puede editarlo su customer
	 * 4. HolderName no puede estar vacio
	 * 5. HolderName no puede ser null
	 * 6. BrandName no puede estar vacio
	 * 7. BrandName no puede ser null
	 * 8. Number debe ser un numero de tarjeta de credito valido
	 * 9. El mes de expiracion debe estar comprendido entre 1 y 12
	 * 10. El mes de expiracion debe estar comprendido entre 1 y 12
	 * 11. El año de expiracion debe ser mayor que 0 
	 * 12. El codigo CVV debe estar comprendido entre 100 y 999
	 * 13. El codigo CVV debe estar comprendido entre 100 y 999
	 * 
	 * Requisitos:
	 * 	22. An actor who is authenticated as a customer can:
		1. Subscribe to a private newspaper by providing a valid credit card.
	 *
	 */
	@Test()
	public void negativeEditSubscriptionTest() {
		final Object testingData[][] = {
			{
				null, "subscription1", "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, IllegalArgumentException.class 
			}, 	{
				"user1", "subscription1", "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, IllegalArgumentException.class 
			}, {
				"customer2", "subscription1", "Antonio", "MasterCard", "5471664286416252", 9, 2019, 258, IllegalArgumentException.class
			}, {
				"customer1", "subscription1", "", "MasterCard", "5471664286416252", 9, 2019, 258, ConstraintViolationException.class 
			}, {
				"customer1", "subscription1", null, "MasterCard", "5471664286416252", 9, 2019, 258, ConstraintViolationException.class 
			}, {
				"customer2", "subscription2", "Estefania", "", "5429007233826913", 2, 2021, 258, ConstraintViolationException.class
			}, {
				"customer2", "subscription2", "Estefania", null, "5429007233826913", 2, 2021, 258, ConstraintViolationException.class 
			}, {
				"customer3", "subscription6", "Manuel", "Credit Links", "1005", 5, 2017, 365, ConstraintViolationException.class 
			}, {
				"customer3", "subscription4", "Manuel", "Credit Links", "5429007233826913", 0, 2017, 365, ConstraintViolationException.class
			}, {
				"customer2", "subscription2", "Manuel", "Credit Links", "5429007233826913", 13, 2017, 365, ConstraintViolationException.class 
			}, {
				"customer1", "subscription1", "Paco", "American Express", "345035739479236", 4, -52, 147, ConstraintViolationException.class 
			}, {
				"customer2", "subscription2", "Alejandro", "Visa", "4929231012264199", 8, 2020, 50, ConstraintViolationException.class 
			}, {
				"customer2", "subscription2", "Alejandro", "Visa", "4929231012264199", 8, 2020, 5000, ConstraintViolationException.class 
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6], (Integer) testingData[i][7], (Class<?>) testingData[i][8]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateNoList((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6], (Integer) testingData[i][7], (Class<?>) testingData[i][8]);
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
	 * 1. Nos autentificamos como customer
	 * 2. Tomamos el id y la entidad de customer
	 customer Accedemos a la lista de subscriptions y tomamos la que nos interesa
	 * 4. Le creamos una copia para que no se guarde solo con un set
	 * 5. Le asignamos el holderName, el brandName, el number, la expirationMonth y el cvvCode correspondientes
	 * 6. Guardamos la subscription copiada con los parámetros
	 * 7. Nos desautentificamos
	 */
	protected void template(final String customer, final String subscriptionEdit, final String holderName, final String brandName, final String number, final Integer expirationMonth, final Integer expirationYear, final Integer cvvcode, final Class<?> expected) {
		Class<?> caught;
		Integer customerId, subscriptionId;
		Customer customerEntity;
		Collection<Subscription> subscriptions;
		Subscription subscription, subscriptionEntity;

		subscription = null;
		caught = null;
		try {
			super.authenticate(customer);
			Assert.notNull(customer);

			customerId = super.getEntityId(customer);
			customerEntity = this.customerService.findOne(customerId);
			Assert.notNull(customerEntity);
			subscriptionId = super.getEntityId(subscriptionEdit);
			Assert.notNull(subscriptionId);
			subscriptions = this.subscriptionService.findByUserAccountId(customerEntity.getUserAccount().getId(), 1, 5).getContent();
			for (Subscription c : subscriptions) {
				if(c.getId() == subscriptionId){
					subscription = c;
					break;
				}
			}
			Assert.notNull(subscription);
			subscriptionEntity = this.copySubscription(subscription);
			subscriptionEntity.setHolderName(holderName);
			subscriptionEntity.setBrandName(brandName);
			subscriptionEntity.setNumber(number);
			subscriptionEntity.setExpirationMonth(expirationMonth);
			subscriptionEntity.setExpirationYear(expirationYear);
			subscriptionEntity.setCvvcode(cvvcode);
			subscriptionEntity.setCustomer(customerEntity);

			this.subscriptionService.save(subscriptionEntity);

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
	 * 1. Nos autentificamos como customer
	 * 2. Tomamos el id y la entidad de customer y subscription
	 * 3. Le creamos una copia para que no se guarde solo con un set
	 * 4. Le asignamos el holderName, el brandName, el number, la expirationMonth y el cvvCode correspondientes
	 * 5. Guardamos la subscription copiada con los parámetros
	 * 6. Nos desautentificamos
	 */
	protected void templateNoList(final String customer, final String subscriptionEdit, final String holderName, final String brandName, final String number, final int expirationMonth, final int expirationYear, final int cvvcode, final Class<?> expected) {
		Class<?> caught;
		Integer customerId, subscriptionId;
		Customer customerEntity;
		Subscription subscription, subscriptionEntity;

		subscription = null;
		caught = null;
		try {
			super.authenticate(customer);
			Assert.notNull(customer);
			customerId = super.getEntityId(customer);
			customerEntity = this.customerService.findOne(customerId);
			Assert.notNull(customerEntity);
			subscriptionId = super.getEntityId(subscriptionEdit);
			Assert.notNull(subscriptionId);
			subscription = this.subscriptionService.findOneToEdit(subscriptionId);
			Assert.notNull(subscription);
			subscriptionEntity = this.copySubscription(subscription);
			subscriptionEntity.setHolderName(holderName);
			subscriptionEntity.setBrandName(brandName);
			subscriptionEntity.setNumber(number);
			subscriptionEntity.setExpirationMonth(expirationMonth);
			subscriptionEntity.setExpirationYear(expirationYear);
			subscriptionEntity.setCvvcode(cvvcode);
			subscriptionEntity.setCustomer(customerEntity);
			this.subscriptionService.save(subscriptionEntity);
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private Subscription copySubscription(final Subscription subscription) {
		Subscription result;
	
		result = new Subscription();
		result.setId(subscription.getId());
		result.setVersion(subscription.getVersion());
		result.setBrandName(subscription.getBrandName());
		result.setCvvcode(subscription.getCvvcode());
		result.setExpirationMonth(subscription.getExpirationMonth());
		result.setExpirationYear(subscription.getExpirationYear());
		result.setNumber(subscription.getNumber());
		result.setCustomer(subscription.getCustomer());
		result.setNewspaper(subscription.getNewspaper());
		result.setHolderName(subscription.getHolderName());
		result.setCustomer(subscription.getCustomer());
		
		return result;
	}
	
}
