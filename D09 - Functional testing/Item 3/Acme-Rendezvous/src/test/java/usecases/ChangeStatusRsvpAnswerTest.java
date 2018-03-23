
package usecases;

import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.LoginService;
import services.RsvpService;
import utilities.AbstractTest;
import domain.Rsvp;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ChangeStatusRsvpAnswerTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private RsvpService	rsvpService;


	// Tests Change CANCEL/ACCEPTED ------------------------------------------------------------------
	//Es la única forma que se permite desde la aplicación para editar RSVP
	//Pruebas: Positivas para crear un RSVP.
	/*
	 * 1. Cambiar status RSVP que es tuyo a CANCELLED. Excepción no esperada.
	 * 2. Cambiar status RSVP que es tuyo a ACCEPTED. Excepción no esperada.
	 * 3. Crear RSVP para rendezvous que no es tuyo y no tiene preguntas. Excepción no esperada.
	 * 4. Crear RSVP para rendezvous que no es tuyo y no tiene preguntas(ambos menores de edad). Excepción no esperada.
	 */

	//CU:
	//5. An actor who is authenticated as a user must be able to
	//4. RSVP a rendezvous or cancel it. "RSVP" is a French term that means "Réservez, s'ilvous plaît"; it's commonly used in the anglo-saxon world to mean "I will attend this rendezvous"; it's pronounced as "/ri:’serv/", "/ri:'serv'silvu'ple/", or "a:resvi:'pi:". When a user RSVPs a rendezvous, he or she is assumed to attend it.

	@Test
	public void driverChangeStatus() {

		//Rol, rsvp, action, ExpectedException
		final Object testingData[][] = {
			{
				"user2", "rsvp1", "CANCELLED", null
			}, {
				"user2", "rsvp3", "ACCEPTED", null
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateCancelledAccepted((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	//Pruebas: Negativas para crear un RSVP.
	/*
	 * 1. Cambiar status RSVP no es tuyo a CANCELLED. Excepción IllegalArgumentException esperada.
	 * 2. Cambiar status RSVP no es tuyo a ACCEPTED. Excepción IllegalArgumentException esperada.
	 * 3. Cambiar status RSVP de rendezvous que ya ha pasado. Excepción IllegalArgumentException esperada.
	 * 4. Cambiar status RSVP a un rendezvous borrado. Excepción IllegalArgumentException esperada.
	 * 5. Cambiar status RSVP a un status que no existe. Excepción ConstraintViolationException esperada.
	 */

	//CU:
	//5. An actor who is authenticated as a user must be able to
	//4. RSVP a rendezvous or cancel it. "RSVP" is a French term that means "Réservez, s'ilvous plaît"; it's commonly used in the anglo-saxon world to mean "I will attend this rendezvous"; it's pronounced as "/ri:’serv/", "/ri:'serv'silvu'ple/", or "a:resvi:'pi:". When a user RSVPs a rendezvous, he or she is assumed to attend it.

	@Test
	public void driverChangeNegative() {

		//Rol, rsvp, action, ExpectedException
		final Object testingData[][] = {
			{
				"user3", "rsvp1", "CANCELLED", IllegalArgumentException.class
			}, {
				"user3", "rsvp3", "ACCEPTED", IllegalArgumentException.class
			}, {
				"user6", "rsvp10", "CANCELLED", IllegalArgumentException.class
			}, {
				"user1", "rsvp8", "ACCEPTED", IllegalArgumentException.class
			}, {
				"user2", "rsvp3", "ACCEPTEDD", ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateCancelledAcceptedForOtherUser((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	/*
	 * Creamos un RSVP a un rendezvous. Pasos:
	 * 1. Autenticarnos como usuario.
	 * 2. Listar los RSVP del usuario.
	 * 3. Navegar hasta el RSVP que queremos.
	 * 4. Escoger un RSVP.
	 * 5. Cambiar su status.
	 * 6. Ir al listado de mis RSVP.
	 */
	protected void templateCancelledAccepted(final String userName, final String rsvpName, final String action, final Class<?> expected) {
		Class<?> caught;
		Collection<Rsvp> rsvps;
		Rsvp rsvp;
		Rsvp rsvpChoosen;
		Rsvp rsvpSaved;
		int countRendezvous;
		int countRsvpBefore;
		final int countRsvpAfter;

		caught = null;

		try {
			super.startTransaction();

			//1. Autenticarnos como usuario.
			this.authenticate(userName);

			//Guardamos el número de cuestiones para comprobarlo después
			rsvpChoosen = null;

			if (rsvpName != null)
				rsvp = this.rsvpService.findOne(this.getEntityId(rsvpName));
			else
				rsvp = null;

			//Contamos los rsvp que había al principio
			countRsvpBefore = this.rsvpService.countByAttendantUserAccountId(LoginService.getPrincipal().getId());

			//2. Listar los RSVP del usuario.
			countRendezvous = this.rsvpService.countByAttendantUserAccountId(LoginService.getPrincipal().getId());

			for (int i = 0; i < countRendezvous; i++) {

				rsvps = this.rsvpService.findByAttendantUserAccountId(LoginService.getPrincipal().getId(), i + 1, 5);

				//Si estamos pidiendo una página mayor
				if (rsvps.size() == 0)
					break;

				//3. Navegar hasta el RSVP que queremos.
				for (final Rsvp newRsvp : rsvps)
					if (newRsvp.equals(rsvp)) {
						//4. Escoger un RSVP.
						rsvpChoosen = rsvp;
						break;
					}

				if (rsvpChoosen != null)
					break;
			}

			//5. Cambiar su status.

			rsvpChoosen.setStatus(action);

			rsvpSaved = this.rsvpService.save(rsvpChoosen);

			this.rsvpService.flush();

			//6. Ir al listado de mis RSVP.
			this.rsvpService.findByAttendantUserAccountId(LoginService.getPrincipal().getId(), 1, 5);

			//Comprobamos que haya cambiado
			if (action.equals("CANCELLED"))
				Assert.isTrue(rsvpSaved.getStatus().equals("CANCELLED"));
			else
				Assert.isTrue(rsvpSaved.getStatus().equals("ACCEPTED"));

			//Contamos los rsvp
			countRsvpAfter = this.rsvpService.countByAttendantUserAccountId(LoginService.getPrincipal().getId());
			Assert.isTrue(countRsvpBefore == countRsvpAfter);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		this.checkExceptions(expected, caught);

	}

	/*
	 * Actualizamos un RSVP conocida su ID. Pasos:
	 * 1. Autenticarnos como usuario.
	 * 2. Cambiar su status.
	 * 3. Ir al listado de mis RSVP.
	 */
	protected void templateCancelledAcceptedForOtherUser(final String userName, final String rsvpName, final String action, final Class<?> expected) {
		Class<?> caught;
		Rsvp rsvp;
		Rsvp rsvpSaved;

		caught = null;

		try {
			super.startTransaction();

			//1. Autenticarnos como usuario.
			this.authenticate(userName);

			if (rsvpName != null)
				rsvp = this.rsvpService.findOne(this.getEntityId(rsvpName));
			else
				rsvp = null;

			//2. Cambiar su status.

			rsvp.setStatus(action);

			rsvpSaved = this.rsvpService.save(rsvp);

			this.rsvpService.flush();

			//3. Ir al listado de mis RSVP.
			this.rsvpService.findByAttendantUserAccountId(LoginService.getPrincipal().getId(), 1, 5);

			//Comprobamos que haya cambiado
			if (action.equals("CANCELLED"))
				Assert.isTrue(rsvpSaved.getStatus().equals("CANCELLED"));
			else
				Assert.isTrue(rsvpSaved.getStatus().equals("ACCEPTED"));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		this.checkExceptions(expected, caught);

	}
}
