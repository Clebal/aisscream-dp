
package usecases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.DataBinder;

import security.LoginService;
import services.AnswerService;
import services.QuestionService;
import services.RendezvousService;
import services.RsvpService;
import utilities.AbstractTest;
import domain.Answer;
import domain.Question;
import domain.Rendezvous;
import domain.Rsvp;
import forms.RsvpForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CreateRsvpAnswerTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private RsvpService			rsvpService;

	@Autowired
	private AnswerService		answerService;

	//Fixtures
	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private QuestionService		questionService;


	// Tests Create ------------------------------------------------------------------
	//Pruebas: Positivas para crear un RSVP.
	/*
	 * 1. Crear RSVP para un rendezvous que no es tuyo. Excepción no esperada.
	 * 2. Crear RSVP para un rendezvous que no es tuyo(ambos menores de edad). Excepción no esperada.
	 * 3. Crear RSVP para rendezvous que no es tuyo y no tiene preguntas. Excepción no esperada.
	 * 4. Crear RSVP para rendezvous que no es tuyo y no tiene preguntas(ambos menores de edad). Excepción no esperada.
	 */

	//CU:
	//5. An actor who is authenticated as a user must be able to
	//4. RSVP a rendezvous or cancel it. "RSVP" is a French term that means "Réservez, s'ilvous plaît"; it's commonly used in the anglo-saxon world to mean "I will attend this rendezvous"; it's pronounced as "/ri:’serv/", "/ri:'serv'silvu'ple/", or "a:resvi:'pi:". When a user RSVPs a rendezvous, he or she is assumed to attend it.

	//CU:
	//21. An actor who is authenticated as a user must be able to:
	//2. Answer the questions that are associated with a rendezvous that he or she's RSVPing now

	@Test
	public void driverCreatePositive() {

		//Rol, rendezvous, olderThan18, responses, ExpectedException
		final Object testingData[][] = {
			{
				"user5", "rendezvous1", true, 2, null
			}, {
				"user4", "rendezvous5", false, 6, null
			}, {
				"user2", "rendezvous6", true, 0, null
			}, {
				"user4", "rendezvous6", false, 0, null
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateCreate((String) testingData[i][0], (String) testingData[i][1], (Boolean) testingData[i][2], (Integer) testingData[i][3], (Class<?>) testingData[i][4]);

	}

	//Pruebas: Crear RSVP saltando algunas restricciones.
	/*
	 * 1. Usuario menor de edad intenta crear RSVP para rendezvous mayor de edad. Excepción IllegalArgumentException esperada.
	 * 2. Usuario que ya tiene RSVP para un rendezvous, intenta crearlo de nuevo. Excepción IllegalArgumentException esperada.
	 * 3. Usuario que ha creado el rendezvous intenta crear un RSVP para él mismo. Excepción IllegalArgumentException esperada.
	 * 4. Usuario que quiere crear un RSVP sin contestar ninguna pregunta. Excepción ConstraintViolationException esperada.
	 * 5. Usuario que quiere crear un RSVP contestando una pregunta. Excepción ConstraintViolationException esperada.
	 * 6. Usuario que quiere crear un RSVP a un rendezvous borrado. Excepción IllegalArgumentException esperada.
	 */

	//CU:
	//5. An actor who is authenticated as a user must be able to
	//4. RSVP a rendezvous or cancel it. "RSVP" is a French term that means "Réservez, s'ilvous plaît"; it's commonly used in the anglo-saxon world to mean "I will attend this rendezvous"; it's pronounced as "/ri:’serv/", "/ri:'serv'silvu'ple/", or "a:resvi:'pi:". When a user RSVPs a rendezvous, he or she is assumed to attend it.

	//CU:
	//21. An actor who is authenticated as a user must be able to:
	//2. Answer the questions that are associated with a rendezvous that he or she's RSVPing now

	@Test
	public void driverCreateNegative() {

		//Rol, rendezvous, olderThan18, responses, ExpectedException
		final Object testingData[][] = {
			{
				"user4", "rendezvous2", true, 1, IllegalArgumentException.class
			}, {
				"user5", "rendezvous5", true, 6, IllegalArgumentException.class
			}, {
				"user1", "rendezvous1", true, 2, IllegalArgumentException.class
			}, {
				"user1", "rendezvous3", true, 0, ConstraintViolationException.class
			}, {
				"user1", "rendezvous3", true, 1, ConstraintViolationException.class
			}, {
				"user1", "rendezvous4", true, 1, IllegalArgumentException.class
			}, {
				"user2", "rendezvous8", true, 0, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateCreate((String) testingData[i][0], (String) testingData[i][1], (Boolean) testingData[i][2], (Integer) testingData[i][3], (Class<?>) testingData[i][4]);

	}

	//Pruebas: Crear un RSVP por URL.
	/*
	 * 1. Crear RSVP eliminando una question al mandarlo. Excepción IllegalArgumentException esperada.
	 * 2. Crear RSVP cambiando el id de una question. Excepción IllegalArgumentException esperada.
	 * 3. Crear RSVP cambiando el rendezvous(con las preguntas de otro rendezvous, mismo número). Excepción IllegalArgumentException esperada.
	 * 4. Crear RSVP cambiando el rendezvous(con las preguntas de otro rendezvous, distinto número). Excepción IllegalArgumentException esperada.
	 * 5. Crear RSVP con usuario sin autenticar. Excepción IllegalArgumentException esperada.
	 */

	//CU:
	//5. An actor who is authenticated as a user must be able to
	//4. RSVP a rendezvous or cancel it. "RSVP" is a French term that means "Réservez, s'ilvous plaît"; it's commonly used in the anglo-saxon world to mean "I will attend this rendezvous"; it's pronounced as "/ri:’serv/", "/ri:'serv'silvu'ple/", or "a:resvi:'pi:". When a user RSVPs a rendezvous, he or she is assumed to attend it.

	//CU:
	//21. An actor who is authenticated as a user must be able to:
	//2. Answer the questions that are associated with a rendezvous that he or she's RSVPing now

	@Test
	public void driverCreateURL() {

		//Rol, rendezvous, rendezvousNew, removeOne, changeId, ExpectedException
		final Object testingData[][] = {
			{
				"user5", "rendezvous1", null, true, false, IllegalArgumentException.class
			}, {
				"user5", "rendezvous1", null, false, true, IllegalArgumentException.class
			}, {
				"user6", "rendezvous1", "rendezvous3", false, false, IllegalArgumentException.class
			}, {
				"user6", "rendezvous1", "rendezvous2", false, false, IllegalArgumentException.class
			}, {
				null, "rendezvous1", null, false, false, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateCreateURL((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (Boolean) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	/*
	 * Creamos un RSVP a un rendezvous. Pasos:
	 * 1. Autenticarnos como usuario.
	 * 2. Listar las citas del usuario.
	 * 3. Navegar hasta la cita que queremos.
	 * 4. Escoger una cita.
	 * 5. Crear un RSVP para esa cita.
	 * 6. Ir al listado de mis RSVP.
	 */
	protected void templateCreate(final String userName, final String rendezvousName, final Boolean olderThan18, final Integer responses, final Class<?> expected) {
		Class<?> caught;
		Rendezvous rendezvous;
		Collection<Rendezvous> rendezvouses;
		Collection<Question> questions;
		Collection<Answer> answers;
		Rsvp rsvp;
		final RsvpForm rsvpForm;
		Rendezvous rendezvousChoosen;
		int countRendezvous;
		int countAnswersBefore;
		int countRsvpBefore;
		final int countAnswersAfter;
		final int countRsvpAfter;
		final DataBinder binder;

		caught = null;

		try {
			super.startTransaction();

			//1. Autenticarnos como usuario.
			this.authenticate(userName);

			//Guardamos el número de cuestiones para comprobarlo después
			rendezvousChoosen = null;

			if (rendezvousName != null)
				rendezvous = this.rendezvousService.findOne(this.getEntityId(rendezvousName));
			else
				rendezvous = null;

			//Contamos los rsvp y answers que había al principio
			countRsvpBefore = this.rsvpService.countByRendezvousId(rendezvous.getId());
			countAnswersBefore = this.answerService.findAll().size();

			//2. Listar las citas.
			if (olderThan18)
				countRendezvous = this.rendezvousService.countAllPaginated();
			else
				countRendezvous = this.rendezvousService.countAllPublics();

			for (int i = 0; i < countRendezvous; i++) {

				if (olderThan18)
					rendezvouses = this.rendezvousService.findAllPaginated(i + 1, 5);
				else
					rendezvouses = this.rendezvousService.findAllPublics(i + 1, 5);

				//Si estamos pidiendo una página mayor
				if (rendezvouses.size() == 0)
					break;

				//3. Navegar hasta la cita que queremos.
				for (final Rendezvous newRendezvous : rendezvouses)
					if (newRendezvous.equals(rendezvous)) {
						rendezvousChoosen = rendezvous;
						break;
					}

				if (rendezvousChoosen != null)
					break;
			}

			//5. Crear un rsvp para esa cita.
			questions = this.questionService.findByRendezvousId(rendezvousChoosen.getId());

			//Si la cita no tiene preguntas para responder
			if (questions.size() == 0) {
				rsvp = this.rsvpService.create(rendezvousChoosen);
				this.rsvpService.save(rsvp);

				//Si la cita tiene preguntas que responder	
			} else {
				rsvpForm = this.createRsvpForm(questions, rendezvousChoosen.getId());

				//Simulamos la respuesta a las preguntas
				int count = 1;
				for (final Integer questionId : rsvpForm.getQuestions().keySet()) {
					if (count <= responses)
						rsvpForm.getAnswers().put(questionId, "Response " + count);

					count++;
				}

				//Guardamos rsvp y las respuestas
				binder = new DataBinder(rsvpForm);
				answers = this.answerService.reconstruct(rsvpForm, binder.getBindingResult());
				this.answerService.saveAnswers(answers);
			}

			this.rsvpService.flush();
			this.answerService.flush();

			//6. Volver al listado de mis rsvp.
			this.rsvpService.findByAttendantUserAccountId(LoginService.getPrincipal().getId(), 1, 5);

			//Contamos los rsvp y answers al final
			countRsvpAfter = this.rsvpService.countByRendezvousId(rendezvous.getId());
			countAnswersAfter = this.answerService.findAll().size();
			Assert.isTrue((countAnswersBefore + questions.size()) == countAnswersAfter);
			Assert.isTrue(countRsvpBefore + 1 == countRsvpAfter);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		this.checkExceptions(expected, caught);

	}

	/*
	 * Creamos un RSVP a un rendezvous simulando acceder a la URL directamente. Pasos:
	 * 1. Autenticarnos.
	 * 2. Solicitar crear un RSVP.
	 * 3. Crear un RSVP para una cita.
	 */
	protected void templateCreateURL(final String userName, final String rendezvousName, final String rendezvousChangeName, final Boolean removeOne, final Boolean changeId, final Class<?> expected) {
		Class<?> caught;
		Rendezvous rendezvous;
		Collection<Question> questions;
		Collection<Answer> answers;
		final RsvpForm rsvpForm;
		final Rendezvous rendezvousChange;
		List<Integer> keys;

		caught = null;

		try {
			super.startTransaction();

			//1. Autenticarnos.
			this.authenticate(userName);

			rendezvous = this.rendezvousService.findOne(this.getEntityId(rendezvousName));

			//2. Solicitar crear un RSVP.
			questions = this.questionService.findByRendezvousId(rendezvous.getId());

			rsvpForm = this.createRsvpForm(questions, rendezvous.getId());

			//Simulamos la respuesta a las preguntas
			keys = new ArrayList<Integer>();
			for (final Integer questionId : rsvpForm.getQuestions().keySet())
				keys.add(new Integer(questionId));

			int count = 1;
			for (final Integer questionId : keys) {

				//Cambiamos el id a una question
				if (count == 1 && changeId) {
					rsvpForm.getAnswers().put(0, "Response " + count);
					rsvpForm.getQuestions().put(0, rsvpForm.getQuestions().get(questionId));
					rsvpForm.getAnswers().remove(questionId);
					rsvpForm.getQuestions().remove(questionId);

					//Eliminamos una question
				} else if (count == 1 && removeOne) {
					rsvpForm.getAnswers().remove(questionId);
					rsvpForm.getQuestions().remove(questionId);

					//Contestamos la question
				} else
					rsvpForm.getAnswers().put(questionId, "Response " + count);

				count++;
			}

			if (rendezvousChangeName != null) {
				rendezvousChange = this.rendezvousService.findOne(this.getEntityId(rendezvousChangeName));
				rsvpForm.setRendezvousId(rendezvousChange.getId());
			}

			//3. Crear un RSVP para una cita.
			final DataBinder binder = new DataBinder(rsvpForm);
			answers = this.answerService.reconstruct(rsvpForm, binder.getBindingResult());
			this.answerService.saveAnswers(answers);

			this.rsvpService.flush();
			this.answerService.flush();

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		this.checkExceptions(expected, caught);

	}

	//Auxiliary Methods
	private RsvpForm createRsvpForm(final Collection<Question> questions, final int rendezvousId) {
		Map<Integer, String> questionsMap;
		Map<Integer, String> answersMap;
		RsvpForm result;

		questionsMap = new HashMap<Integer, String>();
		answersMap = new HashMap<Integer, String>();

		result = new RsvpForm();

		for (final Question question : questions) {
			questionsMap.put(question.getId(), question.getText());
			answersMap.put(question.getId(), "");
		}

		result.setQuestions(questionsMap);
		result.setAnswers(answersMap);
		result.setRendezvousId(rendezvousId);

		return result;

	}
}
