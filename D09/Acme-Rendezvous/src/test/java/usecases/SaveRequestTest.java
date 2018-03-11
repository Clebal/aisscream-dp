package usecases;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.CreditCard;
import domain.Rendezvous;
import domain.Request;
import domain.Servicio;

import services.CreditCardService;
import services.RendezvousService;
import services.RequestService;
import services.ServicioService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SaveRequestTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private RequestService		requestService;

	@Autowired
	private RendezvousService	rendezvousService;
	
	@Autowired
	private ServicioService			servicioService;
	
	@Autowired
	private CreditCardService		creditCardService;

	// Tests ------------------------------------------------------------------

	@Test
	public void positiveTest() {
		final Object testingData[][] = {
			{
				"user1", "rendezvous1", "service3", "creditCard3", "Hola", null
			}, {
				"user3", "rendezvous3", "service1", "creditCard4", "", null
			}, {
				"user1", "rendezvous5", "service3", "creditCard1", "Un comentario", null
			}, {
				"user3", "rendezvous3", "service6", "creditCard4", null, null
			}, {
				"user1", "rendezvous8", "service2", "creditCard3", "Nueva prueba", null
			}
		};
			
	for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	
	@Test()
	public void negativeTest() {
		final Object testingData[][] = {
			{
				null, "rendezvous1", "service1", "creditCard1", "", IllegalArgumentException.class // Solo puede crearlo un user
			}, 	{
				"admin", "rendezvous1", "service1", "creditCard1", "", NullPointerException.class // Solo puede crearlo un user
			}, {
				"manager1", "rendezvous2", "service5", "creditCard4", "Otro comentario más", NullPointerException.class // Solo puede crearlo un user
			}, {
				"user1", "rendezvous5", "service3", "creditCard2", "", IllegalArgumentException.class // La creditCard debe pertenecer al user logueado
			}, {
				"user3", "rendezvous4", "service6", "creditCard4", "", IllegalArgumentException.class // El rendezvous no puede estar borrado
			}, {
				"user3", "rendezvous9", "service7", "creditCard4", "Comentario poco útil", IllegalArgumentException.class // No puede existir un request con mismo rendezvous y service
			}, {
				"user1", "rendezvous6", "service4", "creditCard1", "", IllegalArgumentException.class // No puede existir un request con mismo rendezvous y service
			}, {
				"user1", "rendezvous3", "service3", "creditCard3", null, IllegalArgumentException.class // El user logueado debe ser el creador del rendezvous
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void template(final String user, final String rendezvous, final String servicio, final String creditCard, final String comments, final Class<?> expected) {
		Class<?> caught;
		int rendezvousId;
		int servicioId;
		int creditCardId;
		Rendezvous rendezvousEntity;
		Servicio servicioEntity;
		CreditCard creditCardEntity;
		Request request;
//		int countPre, countPost;

		caught = null;
		try {
//			countPre = this.requestService.findAll().size();
			super.authenticate(user);
			rendezvousId = super.getEntityId(rendezvous);
			servicioId = super.getEntityId(servicio);
			creditCardId = super.getEntityId(creditCard);
			rendezvousEntity = this.rendezvousService.findOne(rendezvousId);
			servicioEntity = this.servicioService.findOne(servicioId);
			creditCardEntity = this.creditCardService.findOne(creditCardId);
			request = this.requestService.create(rendezvousEntity, servicioEntity);
			request.setCreditCard(creditCardEntity);
			request.setComments(comments);
			this.requestService.save(request);
			super.unauthenticate();
			super.flushTransaction();
//			countPost = this.requestService.findAll().size();
//			Assert.isTrue(countPost == countPre + 1);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
		super.checkExceptions(expected, caught);
	}

}

