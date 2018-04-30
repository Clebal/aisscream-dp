package usecases;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Customer;
import domain.SubscriptionNewspaper;
import services.SubscriptionNewspaperService;
import services.CustomerService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DeleteSubscriptionNewspaperTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private SubscriptionNewspaperService		subscriptionNewspaperService;
	
	@Autowired
	private CustomerService				customerService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 
	 * 	Primero se realizarán las pruebas desde un listado y luego
	 * como si accedemos a la entidad desde getEntityId:
	 * 1. Probando que el customer2 borra la subscription2
	 * 	2. Probando que el customer3 borra la subscription6
	 * 	3. Probando que el customer1 borra la subscription3
	 * 
	 * Requisitos:
	 * 	22. An actor who is authenticated as a customer can:
		1. Subscribe to a private newspaper by providing a valid credit card.
	 *
	 */
	@Test
	public void positiveDeleteSubscriptionTest() {
		final Object testingData[][] = {
			{
				"customer2", "subscription2", null
			}, {
				"customer3", "subscription6", null
			} , {
				"customer1", "subscription3", null
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
	 * 
	 * 1. No puede borrarlo un usuario no logueado
	 * 2. Solo puede borrarlo un customer
	 * 3. Solo puede borrarlo un customer
	 * 
	 * Requisitos:
	 * 	22. An actor who is authenticated as a customer can:
		1. Subscribe to a private newspaper by providing a valid credit card.
	 *
	 */
	 @Test()
	public void negativeDeleteSubscriptionTest() {
		final Object testingData[][] = {
			{
				null, "subscription1", IllegalArgumentException.class 
			}, 	{
				"administrator", "subscription1", IllegalArgumentException.class
			}, {
				"user1", "subscription2", IllegalArgumentException.class 
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
		 * 1. Nos autentificamos como customer
		 * 2. Tomamos el id y la entidad de customer
		 * 3. Accedemos a la lista de subscriptions y tomamos la que nos interesa
		 * 4. Borramos la subscription
		 * 5. Nos desautentificamos
		 * 6. Comprobamos que no existe la subscription borrada
		 */
	protected void template(final String customer, final String subscription, final Class<?> expected) {
		Class<?> caught;
		int customerId, subscriptionId;
		Customer customerEntity;
		SubscriptionNewspaper subscriptionEntity;
		Collection<SubscriptionNewspaper> subscriptions;

		subscriptionEntity = null;
		caught = null;
		try {
			super.authenticate(customer);
			Assert.notNull(customer);
			customerId = super.getEntityId(customer);
			customerEntity = this.customerService.findOne(customerId);
			Assert.notNull(customerEntity);
			subscriptionId = super.getEntityId(subscription);
			Assert.notNull(subscriptionId);
			subscriptions = this.subscriptionNewspaperService.findByUserAccountId(customerEntity.getUserAccount().getId(), 1, 5).getContent();
			for (SubscriptionNewspaper c : subscriptions) {
				if(c.getId() == subscriptionId){
					subscriptionEntity = c;
					break;
				}
			}
			this.subscriptionNewspaperService.delete(subscriptionEntity);
			super.unauthenticate();
			
			Assert.isTrue(!this.subscriptionNewspaperService.findAll().contains(subscriptionEntity));

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
	 * 2. Tomamos el id y la entidad de customer y de subscription
	 * 3. Borramos la subscription
	 * 4. Nos desautentificamos
	 * 5. Comprobamos que no existe la subscription borrada
	 */
	protected void templateNoList(final String customer, final String subscription, final Class<?> expected) {
		Class<?> caught;
		int subscriptionId;
		SubscriptionNewspaper subscriptionEntity = null;

		caught = null;
		try {
			super.authenticate(customer);
			subscriptionId = super.getEntityId(subscription);
			Assert.notNull(subscriptionId);

			subscriptionEntity = this.subscriptionNewspaperService.findOneToEdit(subscriptionId);
			this.subscriptionNewspaperService.delete(subscriptionEntity);
			super.unauthenticate();
			
			Assert.isTrue(!this.subscriptionNewspaperService.findAll().contains(subscriptionEntity));

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}

