package usecases;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Request;
import services.RequestService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DeleteRequestTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private RequestService		requestService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 
	 * 	Primero se realizar�n las pruebas desde un listado y luego
	 * como si accedemos a la entidad desde getEntityId:
	 * 1. Probando que el user1 borra el request1
	 * 2. Probando que el user3 borra el request4
	 * 3. Probando que el user1 borra el request7
	 * 4. Probando que el user3 borra el request2
	 * 
	 * Requisitos:
	 * 
	 * 	An actor who is authenticated as a user must be able to request 
	 * a service for one of the rendezvouses that he or she’s created. He 
	 * or she must specify a valid credit card in every request for a service. 
	 * Optionally, he or she can provide some comments in the request. 
	*/
	@Test
	public void positiveDeleteRequestTest() {
		final Object testingData[][] = {
			{
				"user1", "request1", null 
			}, {
				"user3", "request4", null 
			}, {
				"user1", "request7", null 
			}, {
				"user3", "request2", null 
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
	 * 	Primero se realizar�n las pruebas desde un listado y luego
	 * como si accedemos a la entidad desde getEntityId:
	 * 1. Solo puede borrarlo un user
	 * 2. Solo puede borrarlo un user
	 * 3. Solo puede borrarlo un user
	 * 4. El user logueado debe ser el creador del rendezvous del request
	 * 5. El rendezvous no puede estar borrado
	 * 
	 * Requisitos:
	 * 
	 * An actor who is authenticated as a user must be able to request 
	 * a service for one of the rendezvouses that he or she’s created. He 
	 * or she must specify a valid credit card in every request for a service. 
	 * Optionally, he or she can provide some comments in the request. 
	*/
	@Test()
	public void negativeDeleteRequestTest() {
		final Object testingData[][] = {
			{
				null, "request1", IllegalArgumentException.class 
			}, 	{
				"administrator", "request1", IllegalArgumentException.class 
			}, {
				"manager1", "request2", IllegalArgumentException.class
			}, {
				"user1", "request5", IllegalArgumentException.class 
			}, {
				"user3", "request1", IllegalArgumentException.class 
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
	 * 1. Nos autentificamos como user
	 * 2. Tomamos el id y la entidad de user
	 * 3. Accedemos a la lista de requests y tomamos la que nos interesa
	 * 4. Borramos la request
	 * 5. Nos desautentificamos
	 * 6. Comprobamos que no existe la request borrada
	 */
	protected void template(final String user, final String request, final Class<?> expected) {
		Class<?> caught;
		int requestId, userId;
		Collection<Request> requests;
		Request requestEntity;

		requestEntity = null;
		caught = null;
		try {
			super.authenticate(user);
			Assert.notNull(user);
			userId = super.getEntityId(user);
			requests = this.requestService.findAllPaginated(userId, 1, 5);
			requestId = super.getEntityId(request);
			for (Request r : requests) {
				if(r.getId() == requestId){
					requestEntity = r;
					break;
				}
			}
			Assert.notNull(requestEntity);
			this.requestService.delete(requestEntity);
			super.unauthenticate();
			super.flushTransaction();
			
			Assert.isTrue(!this.requestService.findAll().contains(requestEntity));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * 	Pasos:
	 * 
	 * 1. Nos autentificamos como user
	 * 2. Tomamos el id y la entidad de user y de request
	 * 3. Borramos la request
	 * 4. Nos desautentificamos
	 * 5. Comprobamos que no existe la request borrada
	 */
	protected void templateNoList(final String user, final String request, final Class<?> expected) {
		Class<?> caught;
		int requestId;
		Request requestEntity;

		requestEntity = null;
		caught = null;
		try {
			super.authenticate(user);
			requestId = super.getEntityId(request);
			requestEntity = this.requestService.findOne(requestId);
			this.requestService.delete(requestEntity);
			super.unauthenticate();
			super.flushTransaction();
			
			Assert.isTrue(!this.requestService.findAll().contains(requestEntity));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}

