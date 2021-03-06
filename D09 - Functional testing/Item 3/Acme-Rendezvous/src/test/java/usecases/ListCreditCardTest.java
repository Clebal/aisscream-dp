
package usecases;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.CreditCardService;
import services.UserService;
import utilities.AbstractTest;
import domain.CreditCard;
import domain.User;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ListCreditCardTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private CreditCardService	creditCardService;

	@Autowired
	private UserService 		userService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 
	 * 1. Probamos obtener el resultado previsto para el metodo findAll logueados como user1
	 * 	2. Probamos obtener el resultado previsto para el metodo findAll sin loguear
	 * 3. Probamos obtener el resultado previsto para el metodo findAll logueados como manager2
	 * 
	 * Requisitos:
	 * 
	 * 	He or she must specify a valid credit card in every request for a service.
	 */
	@Test()
	public void testFindAll() {
		final Object testingData[][] = {
			{
				"user1", "findAll", 6, 0, 0, null
			}, {
				null, "findAll", 6, 0, 0, null
			}, {
				"manager2", "findAll", 6, 0, 0, null
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (Integer) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Class<?>) testingData[i][5]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas:
	 * 
	 * 1. Probamos obtener el resultado previsto para el metodo findByUserAccountId logueados como user1, para la pagina 1 y el tamano 5
	 * 	2. Probamos obtener el resultado previsto para el metodo findByUserAccountId sin loguear, para la pagina 2 y el tamano 4
	 * 3. Probamos obtener el resultado previsto para el metodo findByUserAccountId logueados como user2, para la pagina 2 y el tamano 3
	 * 4. Probamos no poder obtener el resultado previsto para el metodo findByUserAccountId logueados como un manager
	 * 5. Probamos no poder obtener el resultado previsto para el metodo findByUserAccountId logueados como un admin
	 * 6. Probamos no poder obtener el resultado previsto para el metodo findByUserAccountId logueados como user4 y la pagina a null
	 * 7. Probamos no poder obtener el resultado previsto para el metodo findByUserAccountId logueados como user3 y el tamano a null
	 * 
	 * Requisitos:
	 * 
	 * 	He or she must specify a valid credit card in every request for a service.
	 */
	@Test()
	public void testFindByUserAccountId() {
		final Object testingData[][] = {
				{
					"user1", "findByUserAccountId", 2, 1, 5, null
				}, {
					null, "findByUserAccountId", null, 2, 4, IllegalArgumentException.class
				}, {
					"user2", "findByUserAccountId", 1, 1, 1, null
				}, {
					"manager2", "findByUserAccountId", 1, 2, 1, IllegalArgumentException.class
				}, {
					"administrator", "findByUserAccountId", 1, 1, 5, IllegalArgumentException.class
				}

		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (Integer) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Class<?>) testingData[i][5]);
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
	 * 2. Comprobamos si el m�todo es findAll � findByUserAccountId
	 * 3. En el caso de que sea findByUserAccountId, obtenemos las entidades correspondientes al user para usar el m�todo
	 * 3. Seg�n el m�todo que sea, se llama a su m�todo y se guarda en la variable sizeCreditCard el tama�o de los resultados de cada m�todo
	 * 4. Comprobamos que devuelve el valor esperado
	 * 5. Nos desautentificamos
	 */
	protected void template(final String user, final String method, final Integer tamano, final int page, final int size, final Class<?> expected) {
		Class<?> caught;
		Collection<CreditCard> creditCardsCollection;
		List<CreditCard> creditCardsList;
		int sizeCreditCard;
		int userId;
		int userAccountId;
		User userEntity;

		caught = null;
		try {
			super.authenticate(user);

			if (method.equals("findAll")) {
				creditCardsCollection = this.creditCardService.findAll();
				sizeCreditCard = creditCardsCollection.size();
			} else {
				Assert.notNull(user);
				userId = super.getEntityId(user);
				Assert.notNull(userId);
				userEntity = this.userService.findOne(userId);
				Assert.notNull(userEntity);
				userAccountId = userEntity.getUserAccount().getId();
				creditCardsList = this.creditCardService.findByUserAccountId(userAccountId, page, size).getContent();
				sizeCreditCard = creditCardsList.size();
			}
			Assert.isTrue(sizeCreditCard == tamano); 
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
