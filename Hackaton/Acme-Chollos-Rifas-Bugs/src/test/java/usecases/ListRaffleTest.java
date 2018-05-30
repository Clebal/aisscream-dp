
package usecases;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.LoginService;
import services.RaffleService;
import services.TicketService;
import utilities.AbstractTest;
import domain.Raffle;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ListRaffleTest extends AbstractTest {
		
	// System under test ------------------------------------------------------

	@Autowired
	private RaffleService	raffleService;
	
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
	public void findAvailablesTest() {
		final Object testingData[][] = {
			{
				"user1", "findAvailables", 5, 0, 5, null, null
			}, {
				null, "findAvailables", 1, 2, 5, null, null
			}, {
				"moderator2", "findAvailables", 5, 0, 5, null, null
			}, {
				"sponsor3", "findAvailables", 5, 0, 5, null, null
			}, {
				"company1", "findAvailables", 5, 0, 5, null, null
			}, {
				"user1", "findAvailables", 2, 3, 5, null, IllegalArgumentException.class
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

	/*
	 * Pruebas:
	 * 		1. 
	 * 		2.
	 * 		3. 
	 * 		4.
	 * 
	 * Requisitos:
	 * 
	 */
	@Test
	public void findByUserAccountIdTest() {
		final Object testingData[][] = {
			{
				"user1", "findByUserAccountId", 5, 1, 5, null, null
			}, {
				"user1", "findByUserAccountId", 2, 2, 5, null, null
			}, {
				null, "findByUserAccountId", 4, 0, 0, null, IllegalArgumentException.class
			}, {
				"moderator2", "findByUserAccountId", 1, 2, 1, null, IllegalArgumentException.class
			}, {
				"sponsor2", "findByUserAccountId", 2, 0, 0, null, IllegalArgumentException.class				
			}, {
				"company1", "findByUserAccountId", 6, 0, 0, null, IllegalArgumentException.class
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

	/*
	 * Pruebas:
	 * 		1. 
	 * 		2. 
	 * 		3.
	 * 		4.
	 * 
	 * Requisitos:
	 * 
	 */
	@Test
	public void findByCompanyAccountIdTest() {
		final Object testingData[][] = {
			{
				"company1", "findByCompanyAccountId", 1, 2, 1, null, null
			}, {
				"user1", "findByCompanyAccountId", 4, 0, 0, null, IllegalArgumentException.class
			}, {
				null, "findByCompanyAccountId", 5, 0, 0, null, IllegalArgumentException.class
			}, {
				"moderator2", "findByCompanyAccountId", 5, 1, 1, null, IllegalArgumentException.class
			}, {
				"sponsor1", "findByCompanyAccountId", 1, 1, 1, null, IllegalArgumentException.class
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
	
	@Test
	public void findOrderedByMaxDateTest() {
		final Object testingData[][] = {
			{
				"user1", "findOrderedByMaxDate", 5, 0, 5, null, null
			}, {
				null, "findOrderedByMaxDate", 1, 2, 5, null, null
			}, {
				"moderator2", "findOrderedByMaxDate", 5, 0, 5, null, null
			}, {
				"sponsor3", "findOrderedByMaxDate", 5, 0, 5, null, null
			}, {
				"company1", "findOrderedByMaxDate", 5, 0, 5, null, null
			}, {
				"user1", "findOrderedByMaxDate", 2, 3, 5, null, IllegalArgumentException.class
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
	
	@Test
	public void findAllPaginatedTest() {
		final Object testingData[][] = {
			{
				"moderator2", "findAllPaginated", 5, 0, 5, null, null
			}, {
				null, "findAllPaginated", 1, 2, 5, null, IllegalArgumentException.class
			}, {
				"user1", "findAllPaginated", 5, 0, 5, null, IllegalArgumentException.class				
			}, {
				"sponsor3", "findAllPaginated", 5, 0, 5, null, IllegalArgumentException.class
			}, {
				"company1", "findAllPaginated", 5, 0, 5, null, IllegalArgumentException.class
			}, {
				"user1", "findAllPaginated", 2, 3, 5, null, IllegalArgumentException.class
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
	 * 		2. Comprobamos si el método es findValidByUserAccountId ó findByUserAccountId (Page) o findByUserAccountId (Collection)
	 * 		3. En el caso de que sea findByUserAccountId, obtenemos las entidades correspondientes al user para usar el método
	 * 		3. Según el método que sea, se llama a su método y se guarda en la variable sizeCreditCard el tamaño de los resultados de cada método
	 * 		4. Comprobamos que devuelve el valor esperado
	 * 		5. Cerramos sesión
	 */
	protected void template(final String userBean, final String method, final Integer tamano, final int page, final int size, final String raffleBean, final Class<?> expected) {
		Class<?> caught;
		Collection<Raffle> raffleCollection;
		Integer sizeRaffle, countTicket, raffleId;
		Raffle raffle;

		sizeRaffle = 0;
		caught = null;
		raffleCollection = null;
		try {
			
			if(userBean != null)
				super.authenticate(userBean);
			
			if (method.equals("findAvailables")) {
				raffleCollection = this.raffleService.findAvailables(page, size).getContent();
				Assert.notNull(raffleCollection);
				for(Raffle r: raffleCollection)
					Assert.isTrue(r.getMaxDate().compareTo(new Date()) > 0);
			} else if (method.equals("findByUserAccountId")) {
				raffleCollection = this.raffleService.findByUserAccountId(LoginService.getPrincipal().getId(), page, size).getContent();
				Assert.notNull(raffleCollection);
				for(Raffle r: raffleCollection) {
					countTicket = this.ticketService.countByRaffleId(r.getId());
					Assert.notNull(countTicket);
					Assert.isTrue(countTicket != 0);
				}
			} else if (method.equals("findByCompanyAccountId")){
				raffleCollection = this.raffleService.findByCompanyAccountId(LoginService.getPrincipal().getId(), page, size).getContent();
				Assert.notNull(raffleCollection);
				for(Raffle c: raffleCollection)
					Assert.isTrue(c.getCompany().getUserAccount().equals(LoginService.getPrincipal()));
			} else if (method.equals("findOrderedByMaxDate")) {
				raffleCollection = this.raffleService.findOrderedByMaxDate(page, size).getContent();
				Assert.notNull(raffleCollection);
			} else if (method.equals("findAllPaginated")) {
				raffleCollection = this.raffleService.findAllPaginated(page, size).getContent();
				Assert.notNull(raffleCollection);
			}
			
			sizeRaffle = raffleCollection.size();
			Assert.isTrue(sizeRaffle == tamano); 
			
			if (method.equals("findOneToEdit")) {
				raffleId = super.getEntityId(raffleBean);
				Assert.notNull(raffleId);
				raffle = this.raffleService.findOneToEdit(raffleId);
				Assert.notNull(raffle);
			} else if (method.equals("findOneToDisplay")) {
				raffleId = super.getEntityId(raffleBean);
				Assert.notNull(raffleId);
				raffle = this.raffleService.findOneToDisplay(raffleId);
				Assert.notNull(raffle);
			} else if (method.equals("findOneToDelete")) {
				raffleId = super.getEntityId(raffleBean);
				Assert.notNull(raffleId);
				raffle = this.raffleService.findOneToDelete(raffleId);
				Assert.notNull(raffle);
			}
			
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
