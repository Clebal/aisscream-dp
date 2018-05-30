package usecases;

import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import domain.Raffle;
import services.RaffleService;
import services.TicketService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DeleteRaffleTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private RaffleService		raffleService;
	
	@Autowired
	private TicketService		ticketService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 		1.
	 *
	 * Requisitos:
	 * 		
	 * 
	 */
	@Test
	public void driverPositiveTest() {		
		final Object testingData[][] = {
			{
				"moderator1", "raffle1", null
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
	}
	
	/*
	 * Pruebas:
	 * 		1. 
	 * 
	 * Requisitos:
	 * 		
	 * 
	 */
	@Test
	public void driverNegativeTest() {		
		final Object testingData[][] = {
				{
					"company4", "raffle1", IllegalArgumentException.class
				}, {
					"user1", "raffle1", IllegalArgumentException.class
				}, {
					"sponsor2", "raffle1", IllegalArgumentException.class
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
	}

	// Ancillary methods ------------------------------------------------------

	/*
	 * Crear una rifa
	 * Pasos:
	 * 		1. Autenticar como compañía
	 * 		2. Listar rifas
	 * 		3. Entrar en vista de crear rifa
	 * 		4. Crear rifa
	 * 		5. Volver al listado de rifas
	 */
	protected void template(final String actorBean, final String raffleBean, final Class<?> expected) {
		Class<?> caught;
		Raffle raffle;
		Page<Raffle> rafflePage;
		Integer raffleId, oldTickets;
		Long oldTotalElements;

		caught = null;
		try {
			
			// 1. Autenticar como compañía
			super.authenticate(actorBean);
			
			raffleId = super.getEntityId(raffleBean);
			Assert.notNull(raffleId);
			
			raffle = this.raffleService.findOneToDelete(raffleId);
			Assert.notNull(raffle);
			
			// 2. Listar rifas
			rafflePage = this.raffleService.findAllPaginated(1, 5);
			Assert.notNull(rafflePage);
			
			oldTotalElements = rafflePage.getTotalElements();
			oldTickets = this.ticketService.findAll().size();
			
			// 3. Eliminar rifa
			this.raffleService.delete(raffle);
			
			this.raffleService.flush();
			this.ticketService.flush();
									
			// 4. Volver al listado de rifas
			rafflePage = this.raffleService.findAllPaginated(1, 5);
			Assert.notNull(rafflePage);
			Assert.isTrue(oldTotalElements != rafflePage.getTotalElements());
			Assert.isTrue(oldTickets != this.ticketService.findAll().size());
			
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		
		super.checkExceptions(expected, caught);
	}

}

