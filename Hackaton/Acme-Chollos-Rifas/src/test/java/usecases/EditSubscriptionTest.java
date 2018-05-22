
package usecases;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.DataBinder;

import security.LoginService;
import services.ActorService;
import services.CreditCardService;
import services.SubscriptionService;
import services.UserService;
import utilities.AbstractTest;
import domain.CreditCard;
import domain.Subscription;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EditSubscriptionTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private SubscriptionService	subscriptionService;

	@Autowired
	private CreditCardService	creditCardService;

	@Autowired
	private UserService			userService;

	@Autowired
	private ActorService		actorService;


	/*
	 * Pruebas:
	 * 1. Probamos a editar la suscripci�n1 logueados como user1 usando la creditcard3 (no salta excepci�n)
	 * 2. Probamos a editar la suscripci�n2 logueados como user2 usando la creditcard2 (no salta excepci�n)
	 * 3. Probamos a editar la suscripci�n3 logueados como user3 usando la creditcard4 (no salta excepci�n)
	 * 4. Probamos a editar la suscripci�n4 logueados como user4 usando la creditcard5 (no salta excepci�n)
	 * 5. Probamos a editar la suscripci�n2 logueados como user2 poniendo el pago de frecuencia vac�o (salta un ConstraintViolationException)
	 * 6. Probamos a editar la suscripci�n2 logueados como user2 poniendo el pago de frecuencia a nulo (salta un ConstraintViolationException)
	 * 7. Probamos a editar la suscripci�n2 logueados como user2 poniendo el pago de frecuencia con un formato incorrecto (salta un ConstraintViolationException)
	 * 8. Probamos a editar la suscripci�n2 logueados como user1 el cual no posee esa descripci�n (salta un IllegalArgumentException)
	 * 9. Probamos a editar la suscripci�n1 logueados como user1 usando una tarjeta de cr�dito caducada (salta un IllegalArgumentException)
	 * 10.Probamos a editar la suscripci�n2 logueados como moderador (salta un IllegalArgumentException)
	 * 11.Probamos a editar la suscripci�n2 logueados como company (salta un IllegalArgumentException)
	 * 12.Probamos a editar la suscripci�n2 logueados como sponsor (salta un IllegalArgumentException)
	 * 13.Probamos a editar la suscripci�n2 logueados como admin (salta un IllegalArgumentException)
	 * 14.Probamos a editar la suscripci�n2 sin estar logueado (salta un IllegalArgumentException)
	 * 
	 * Requisito 25.8: Un actor autenticado como user puede
	 * suscribirse a un plan de pago indicando la tarjeta de cr�dito a la que se realizar�
	 * el cargo y si quiere que el pago se realice de forma mensual, trimestral o anual. Adem�s, podr� anular
	 * su suscripci�n y editar tanto la tarjeta de cr�dito como la frecuencia de pago. En caso de querer cambiar el plan,
	 * tendr�a que cancelar el actual y contratar uno nuevo.
	 */
	@Test()
	public void testEdit() {
		final Object testingData[][] = {
			{
				"user", "user1", "subscription1", "Monthly", "creditCard3", false, null
			}, {
				"user", "user2", "subscription2", "Anually", "creditCard2", false, null
			}, {
				"user", "user3", "subscription3", "Anually", "creditCard4", false, null
			}, {
				"user", "user4", "subscription4", "Anually", "creditCard5", false, null
			}, {
				"user", "user2", "subscription2", "", "creditCard2", false, ConstraintViolationException.class
			}, {
				"user", "user2", "subscription2", null, "creditCard2", false, ConstraintViolationException.class
			}, {
				"user", "user2", "subscription2", "iejei", "creditCard2", false, ConstraintViolationException.class
			}, {
				"user", "user1", "subscription2", "Anually", "creditCard2", true, IllegalArgumentException.class
			}, {
				"user", "user1", "subscription1", "Anually", "creditCard10", false, IllegalArgumentException.class
			}, {
				"moderator", "moderator1", "subscription2", "Anually", "creditCard2", true, IllegalArgumentException.class
			}, {
				"company", "company1", "subscription2", "Anually", "creditCard2", true, IllegalArgumentException.class
			}, {
				"sponsor", "sponsor1", "subscription2", "Anually", "creditCard2", true, IllegalArgumentException.class
			}, {
				"admin", "admin", "subscription2", "Anually", "creditCard2", true, IllegalArgumentException.class
			}, {
				null, null, "subscription2", "Anually", "creditCard2", true, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (boolean) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas:
	 * 1. Vemos que la suscripcion1 es del user1
	 * 2. Vemos que la suscripcion2 es del user2
	 * 3. Vemos que la suscripcion3 es del user3
	 * 4. Vemos que la suscripcion4 es del user4
	 * 5. Vemos que el user5 no tiene suscripcion
	 * 6. Vemos que el user6 no tiene suscripcion
	 * 7. Vemos que los sponsors no tienen suscripci�n
	 * 8. Vemos que las compa�ias no tienen suscripci�n
	 * 9. Vemos que los moderadores no tienen suscripci�n
	 * 10.Vemos que los admin no tienen suscripci�n
	 * 11.Vemos que los no autenticados no tienen suscripci�n
	 */
	@Test()
	public void testFindByUser() {
		final Object testingData[][] = {
			{
				"user", "user1", "subscription1", null
			}, {
				"user", "user2", "subscription2", null
			}, {
				"user", "user3", "subscription3", null
			}, {
				"user", "user4", "subscription4", null
			}, {
				"user", "user5", null, null
			}, {
				"user", "user6", null, null
			}, {
				"sponsor", "sponsor1", null, null
			}, {
				"company", "company1", null, null
			}, {
				"moderator", "moderator1", null, null
			}, {
				"admin", "admin", null, null
			}, {
				null, null, null, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateFindByUser((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void template(final String user, final String username, final String subscriptionBean, final String payFrecuency, final String creditCardBean, final boolean falseId, final Class<?> expected) {
		Class<?> caught;
		Subscription subscription;
		Subscription saved;
		int subscriptionId;
		CreditCard creditCard;

		DataBinder binder;
		Subscription subscriptionReconstruct;

		caught = null;
		try {
			if (user != null)
				super.authenticate(username); //Nos logeamos si es necesario

			if (falseId == false)
				subscriptionId = this.subscriptionService.findByUserId(this.userService.findByUserAccountId(LoginService.getPrincipal().getId()).getId()).getId();
			else
				subscriptionId = super.getEntityId(subscriptionBean);

			subscription = this.subscriptionService.findOneToEdit(subscriptionId);
			if (creditCardBean != null)
				creditCard = this.creditCardService.findOne(super.getEntityId(creditCardBean));
			else
				creditCard = null;
			subscription.setCreditCard(creditCard);
			subscription.setPayFrecuency(payFrecuency);
			//Editamos los valores

			binder = new DataBinder(subscription);
			subscriptionReconstruct = this.subscriptionService.reconstruct(subscription, binder.getBindingResult()); //Lo reconstruimos
			saved = this.subscriptionService.save(subscriptionReconstruct); //Guardamos la suscripci�n
			super.flushTransaction();

			Assert.isTrue(this.subscriptionService.findAll().contains(saved)); //Miramos si est�n entre todos las suscripciones de la BD

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

	protected void templateFindByUser(final String user, final String username, final String subscriptionBean, final Class<?> expected) {
		Class<?> caught;
		Subscription subscription;

		caught = null;
		try {
			if (user != null)
				super.authenticate(username); //Nos logeamos si es necesario

			if (user != null)
				subscription = this.subscriptionService.findByUserId(this.actorService.findByUserAccountId(LoginService.getPrincipal().getId()).getId());
			else
				subscription = null;

			if (subscriptionBean != null)
				Assert.isTrue(subscription.equals(this.subscriptionService.findOne(super.getEntityId(subscriptionBean))));
			else
				Assert.isTrue(subscription == null);

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

}
