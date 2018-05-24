package usecases;

import java.util.Collection;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.DataBinder;

import domain.CreditCard;
import domain.Raffle;
import domain.Ticket;
import domain.User;
import forms.TicketForm;
import security.LoginService;
import services.CreditCardService;
import services.RaffleService;
import services.TicketService;
import services.UserService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BuyTicketTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private RaffleService		raffleService;
	
	@Autowired
	private TicketService		ticketService;
	
	@Autowired
	private UserService 		userService;
	
	@Autowired
	private CreditCardService	creditCardService;
	
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
				"user1", "raffle12", "creditCard1", 10, null
			}, {
				"user1", "raffle13", null, 1, null
			}
		};
			
	for (int i = 0; i < testingData.length; i++)
		try {
			super.startTransaction();
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (int) testingData[i][3], (Class<?>) testingData[i][4]);
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
	 * 
	 * Requisitos:
	 * 		
	 * 
	 */
	@Test
	public void driverNegativeTest() {		
		final Object testingData[][] = {
				{
					"user1", "raffle10", "creditCard1", 10, IllegalArgumentException.class
				}, {
					"user1", "raffle13", "creditCard1", 1, IllegalArgumentException.class
				}, {
					"user1", "raffle13", "creditCard4", 1, IllegalArgumentException.class
				}, {
					"sponsor1", "raffle12", "creditCard1", 10, IllegalArgumentException.class
				}, {
					"company1", "raffle12", "creditCard1", 10, IllegalArgumentException.class
				}, {
					"moderator1", "raffle12", "creditCard1", 10, IllegalArgumentException.class
				}
			};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (int) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	/*
	 * Comprar ticket
	 * Pasos:
	 * 		1. Autenticar como usuario
	 * 		2. Listar rifas
	 * 		3. Entrar display de rifa
	 * 		4. Comprar ticket con tarjeta de crédito
	 * 		5. Listar tickets
	 */
	protected void template(final String actorBean, final String raffleBean, final String creditCardBean, final int amount, final Class<?> expected) {
		Class<?> caught;
		Raffle raffle;
		Page<Raffle> rafflePage;
		Page<Ticket> ticketPage;
		User user;
		Integer userId, raffleId, creditCardId, oldTicketsNumber;
		Collection<Ticket> ticketsToCreate;
		DataBinder binder;
		TicketForm ticketForm;
		CreditCard creditCard;

		caught = null;
		creditCard = null;
		try {
			
			// 1. Autenticar como usuario
			super.authenticate(actorBean);
			
			if(creditCardBean != null) {
				creditCardId = super.getEntityId(creditCardBean);
				Assert.notNull(creditCardId);
	
				creditCard = this.creditCardService.findOneToDisplayEdit(creditCardId);
				Assert.notNull(creditCard);
			}
			
			raffleId = super.getEntityId(raffleBean);
			Assert.notNull(raffleId);
			
			userId = super.getEntityId(actorBean);
			Assert.notNull(userId);
			
			user = this.userService.findOne(userId);
			Assert.notNull(user);
			
			oldTicketsNumber = this.ticketService.findAll().size();
			
			// 2. Listar rifas
			rafflePage = this.raffleService.findAvailables(1, 5);
			Assert.notNull(rafflePage);
						
			// 3. Entrar display de rifa
			raffle = this.raffleService.findOneToDisplay(raffleId);
			Assert.notNull(raffle);
			
			// 4. Comprar ticket con tarjeta de crédito
			ticketForm = new TicketForm();
			ticketForm.setAmount(amount);
			if(creditCardBean != null) ticketForm.setCreditCard(creditCard);
			ticketForm.setRaffle(raffle);
			
			binder = new DataBinder(ticketForm);
			ticketsToCreate = this.ticketService.reconstruct(ticketForm, binder.getBindingResult());
			this.ticketService.save(ticketsToCreate);
			
			this.ticketService.flush();
									
			// 5. Listar tickets
			ticketPage = this.ticketService.findByUserAccountId(LoginService.getPrincipal().getId(), 1, 5);
			Assert.notNull(ticketPage);
			Assert.isTrue(oldTicketsNumber != ticketPage.getTotalElements());
			
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		
		super.checkExceptions(expected, caught);
	}

}

