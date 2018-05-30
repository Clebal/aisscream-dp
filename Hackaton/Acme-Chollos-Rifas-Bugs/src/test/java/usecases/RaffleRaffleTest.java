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
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RaffleRaffleTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private RaffleService		raffleService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 		1. Un moderador trata de sortear una rifa
	 *
	 * Requisitos:
	 * 		
	 * 
	 */
	@Test
	public void driverPositiveTest() {		
		final Object testingData[][] = {
			{
				"moderator1", "raffle11", null
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
					"moderator1", "raffle10", IllegalArgumentException.class
				}, {
					"company3", "raffle11", IllegalArgumentException.class
				}, {
					"user1", "raffle11", IllegalArgumentException.class
				}, {
					"sponsor1", "raffle11", IllegalArgumentException.class
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
	 * Sortear una rifa
	 * Pasos:
	 * 		1. Autenticar como moderador
	 * 		2. Listar rifas
	 * 		3. Pulsar el bot�n de sortear
	 * 		4. Volver al listado de rifas
	 */
	protected void template(final String actorBean, final String raffleBean, final Class<?> expected) {
		Class<?> caught;
		Raffle raffle;
		Page<Raffle> rafflePage;
		Integer raffleId;

		caught = null;
		try {
			
			// 1. Autenticar como moderador
			super.authenticate(actorBean);
			
			raffleId = super.getEntityId(raffleBean);
			Assert.notNull(raffleId);
			
			// 2. Listar rifas
			rafflePage = this.raffleService.findAllPaginated(1, 5);
			Assert.notNull(rafflePage);
			
			// 3. Pulsar el bot�n de sortear
			this.raffleService.toRaffle(raffleId);
			
			this.raffleService.flush();
									
			// 4. Volver al listado de rifas
			rafflePage = this.raffleService.findAllPaginated(1, 5);
			Assert.notNull(rafflePage);
			raffle = this.raffleService.findOne(raffleId);
			Assert.isTrue(raffle.getWinner() != null);
			
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		
		super.checkExceptions(expected, caught);
	}

}

