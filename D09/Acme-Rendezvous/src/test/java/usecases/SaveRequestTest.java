package usecases;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.CreditCard;
import domain.Rendezvous;
import domain.Request;
import domain.Service;

import services.CreditCardService;
import services.RendezvousService;
import services.RequestService;
import services.ServiceService;
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
	private ServiceService			serviceService;
	
	@Autowired
	private CreditCardService		creditCardService;

	// Tests ------------------------------------------------------------------	
	
	/*
	 * 1. Probando crear request con comentario
	 * 2. Probando crear request con comentario vacio
	 * 3. Probando crear request con comentario
	 * 4. Probando crear request con comentario a null
	 * 5. Probando crear request con comentario
	 */
	@Test
	public void positiveSaveRequestTest() {
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
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	/*
	 * 1. Solo puede crearlo un user
	 * 2. Solo puede crearlo un user
	 * 3. Solo puede crearlo un user
	 * 4. La creditCard debe pertenecer al user logueado
	 * 5. El rendezvous no puede estar borrado
	 * 6. No puede existir un request con mismo rendezvous y service
	 * 7. No puede existir un request con mismo rendezvous y service
	 * 8. El user logueado debe ser el creador del rendezvous
	 */
	@Test()
	public void negativeSaveRequestTest() {
		final Object testingData[][] = {
			{
				null, "rendezvous1", "service1", "creditCard1", "", IllegalArgumentException.class 
			}, 	{
				"admin", "rendezvous1", "service1", "creditCard1", "", IllegalArgumentException.class 
			}, {
				"manager1", "rendezvous2", "service5", "creditCard4", "Otro comentario mas", IllegalArgumentException.class
			}, {
				"user1", "rendezvous5", "service3", "creditCard2", "", IllegalArgumentException.class 
			}, {
				"user3", "rendezvous4", "service6", "creditCard4", "", IllegalArgumentException.class 
			}, {
				"user3", "rendezvous9", "service7", "creditCard4", "Comentario poco util", IllegalArgumentException.class 
			}, {
				"user1", "rendezvous6", "service4", "creditCard1", "", IllegalArgumentException.class 
			}, {
				"user1", "rendezvous3", "service3", "creditCard3", null, IllegalArgumentException.class
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	/*
	 * An actor who is authenticated as a user must be able to request a service for one of the rendezvouses 
	 * that he or she’s created. He or she must specify a valid credit card in every request for a service. 
	 * Optionally, he or she can provide some comments in the request. 
	 */
	protected void template(final String user, final String rendezvous, final String servicio, final String creditCard, final String comments, final Class<?> expected) {
		Class<?> caught;
		int rendezvousId;
		int servicioId;
		int creditCardId;
		Rendezvous rendezvousEntity;
		Service servicioEntity;
		CreditCard creditCardEntity;
		Request request, requestSave;

		caught = null;
		try {
			super.authenticate(user);
			rendezvousId = super.getEntityId(rendezvous);
			servicioId = super.getEntityId(servicio);
			creditCardId = super.getEntityId(creditCard);
			rendezvousEntity = this.rendezvousService.findOne(rendezvousId);
			servicioEntity = this.serviceService.findOne(servicioId);
			creditCardEntity = this.creditCardService.findOne(creditCardId);
			request = this.requestService.create(rendezvousEntity, servicioEntity);
			request.setCreditCard(creditCardEntity);
			request.setComments(comments);
			requestSave = this.requestService.save(request);
			super.unauthenticate();
			
			Assert.isTrue(this.requestService.findAll().contains(requestSave));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}

