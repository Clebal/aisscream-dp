
package usecases;

import java.util.Collection;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.LoginService;
import services.TicketService;
import utilities.AbstractTest;
import domain.Ticket;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ListTicketTest extends AbstractTest {
		
	// System under test ------------------------------------------------------
	
	@Autowired
	private TicketService ticketService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 		1. 
	 *		2.
	 * 		3.
	 * 		4.
	 * 
	 * Requisitos:
	 * 
	 * 
	 */
	@Test
	public void findByUserAccountIdTest() {
		final Object testingData[][] = {
			{
				"user1", "findByUserAccountId", 5, 0, 5, null, null
			}, {
				null, "findByUserAccountId", 0, 2, 5, null, IllegalArgumentException.class
			}, {
				"moderator2", "findByUserAccountId", 5, 0, 5, null, IllegalArgumentException.class
			}, {
				"sponsor3", "findByUserAccountId", 5, 0, 5, null, IllegalArgumentException.class
			}, {
				"company1", "findByUserAccountId", 5, 0, 5, null, IllegalArgumentException.class
			}, {
				"user1", "findByUserAccountId", 2, 3, 5, null, IllegalArgumentException.class
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (Integer) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	/*
	 * 	Pasos:
	 * 		1. Operacionalmente nos autenticamos
	 * 		2. Comprobamos si el m�todo es findValidByUserAccountId � findByUserAccountId (Page) o findByUserAccountId (Collection)
	 * 		3. En el caso de que sea findByUserAccountId, obtenemos las entidades correspondientes al user para usar el m�todo
	 * 		3. Seg�n el m�todo que sea, se llama a su m�todo y se guarda en la variable sizeCreditCard el tama�o de los resultados de cada m�todo
	 * 		4. Comprobamos que devuelve el valor esperado
	 * 		5. Cerramos sesi�n
	 */
	protected void template(final String userBean, final String method, final Integer tamano, final int page, final int size, final String raffleBean, final Class<?> expected) {
		Class<?> caught;
		Collection<Ticket> ticketCollection;
		Integer sizeRaffle;

		sizeRaffle = 0;
		caught = null;
		ticketCollection = null;
		try {
			
			if(userBean != null) super.authenticate(userBean);
			
			if (method.equals("findByUserAccountId")) {
				ticketCollection = this.ticketService.findByUserAccountId(LoginService.getPrincipal().getId(), page, size).getContent();
				Assert.notNull(ticketCollection);
				for(Ticket t: ticketCollection)
					Assert.isTrue(t.getUser().getUserAccount().equals(LoginService.getPrincipal()));
			}
			
			sizeRaffle = ticketCollection.size();
			Assert.isTrue(sizeRaffle == tamano); 
			
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
