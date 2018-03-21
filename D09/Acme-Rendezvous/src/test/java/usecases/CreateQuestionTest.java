
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
import org.springframework.validation.DataBinder;

import security.LoginService;
import services.QuestionService;
import services.RendezvousService;
import services.UserService;
import utilities.AbstractTest;
import domain.Question;
import domain.Rendezvous;
import domain.User;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CreateQuestionTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private QuestionService		questionService;

	//Fixtures
	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private UserService			userService;


	// Tests Create ------------------------------------------------------------------

	//Pruebas: Con rendezvous que tiene más de una pregunta.
	/*
	 * 1. Crear question con usuario del rendezvous(y número diferente). Excepción no esperada.
	 * 2. Crear question con usuario del rendezvous. Excepción no esperada.
	 * 3. Crear question con usuario del rendezvous y pregunta vacía. ConstraintViolationException esperada.
	 * 4. Crear question con usuario del rendezvous y pregunta null. ConstraintViolationException esperada.
	 */

	//CU:
	//21. An actor who is authenticated as a user must be able to:
	//1. Manage the questions that are associated with a rendezvous that he or she's created previously.
	@Test
	public void driverCreateRendezvousWithMoreThanOneQuestion() {

		//Rol, rendezvous, text, number, numberExpected, ExpectedException
		final Object testingData[][] = {
			{
				"user1", "rendezvous5", "¿Te gusta esta cita?", 3, 7, null
			}, {
				"user1", "rendezvous5", "¿Te gusta esta cita?", 7, 7, null
			}, {
				"user1", "rendezvous5", "", 7, 0, ConstraintViolationException.class
			}, {
				"user1", "rendezvous5", null, 7, 0, ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateCreateForUserOfRendezvous((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	//Pruebas: Con rendezvous que tiene una pregunta.
	/*
	 * 1. Crear question con usuario del rendezvous(y número diferente). Excepción no esperada.
	 * 2. Crear question con usuario del rendezvous. Excepción no esperada.
	 * 3. Crear question con usuario del rendezvous y pregunta vacía. ConstraintViolationException esperada.
	 * 4. Crear question con usuario del rendezvous y pregunta null. ConstraintViolationException esperada.
	 */

	//CU:
	//21. An actor who is authenticated as a user must be able to:
	//1. Manage the questions that are associated with a rendezvous that he or she's created previously.
	@Test
	public void driverCreateRendezvousWithOneQuestion() {

		//Rol, rendezvous, text, number, numberExpected, ExpectedException
		final Object testingData[][] = {
			{
				"user1", "rendezvous2", "¿Te gusta esta cita?", 1, 2, null
			}, {
				"user1", "rendezvous2", "¿Te gusta esta cita?", 2, 2, null
			}, {
				"user1", "rendezvous2", "", 0, 0, ConstraintViolationException.class
			}, {
				"user1", "rendezvous2", null, 0, 0, ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateCreateForUserOfRendezvous((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	//Pruebas: Con rendezvous que no tiene preguntas.
	/*
	 * 1. Crear question con usuario del rendezvous(y número diferente). Excepción no esperada.
	 * 2. Crear question con usuario del rendezvous. Excepción no esperada.
	 * 3. Crear question con usuario del rendezvous y pregunta vacía. ConstraintViolationException esperada.
	 * 4. Crear question con usuario del rendezvous y pregunta null. ConstraintViolationException esperada.
	 */
	//CU:
	//21. An actor who is authenticated as a user must be able to:
	//1. Manage the questions that are associated with a rendezvous that he or she's created previously.
	@Test
	public void driverCreateRendezvousWithOutQuestion() {

		//Rol, rendezvous, text, number, numberExpected, ExpectedException
		final Object testingData[][] = {
			{
				"user1", "rendezvous6", "¿Te gusta esta cita?", 3, 1, null
			}, {
				"user1", "rendezvous6", "¿Te gusta esta cita?", 1, 1, null
			}, {
				"user1", "rendezvous6", "", 0, 0, ConstraintViolationException.class
			}, {
				"user1", "rendezvous6", null, 0, 0, ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateCreateForUserOfRendezvous((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	//Pruebas: Crear question para rendezvous que no es del usuario.
	/*
	 * 1. Crear question por un usuario que no es del rendezvous(con más de una pregunta). IllegalArgumentException esperada.
	 * 2. Crear question por un usuario que no es del rendezvous(con una pregunta). IllegalArgumentException esperada.
	 * 3. Crear question por un usuario que no es del rendezvous(sin preguntas). IllegalArgumentException esperada.
	 * 4. Crear question por un admin. IllegalArgumentException esperada.
	 * 5. Crear question sin autenticarse como usuario. IllegalArgumentException esperada.
	 */
	//CU:
	//21. An actor who is authenticated as a user must be able to:
	//1. Manage the questions that are associated with a rendezvous that he or she's created previously.
	@Test
	public void driverCreateUserNoCreatorOfRendezvous() {

		//Rol, rendezvous, text, ExpectedException
		final Object testingData[][] = {
			{
				"user3", "rendezvous5", "¿Te gusta esta cita?", IllegalArgumentException.class
			}, {
				"user2", "rendezvous2", "¿Te gusta esta cita?", IllegalArgumentException.class
			}, {
				"user2", "rendezvous6", "¿Te gusta esta cita?", IllegalArgumentException.class
			}, {
				"admin", "rendezvous6", "¿Te gusta esta cita?", IllegalArgumentException.class
			}, {
				null, "rendezvous6", "¿Te gusta esta cita?", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateCreateForNoUserRendezvous((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	/*
	 * Creamos una question a un rendezvous. Pasos:
	 * 1. Autenticarnos como usuario.
	 * 2. Listar las citas del usuario.
	 * 3. Navegar hasta la cita que queremos.
	 * 4. Escoger una cita.
	 * 5. Crear una cuestión para esa cita.
	 * 6. Volver al listado de las cuestiones.
	 */
	protected void templateCreateForUserOfRendezvous(final String userName, final String rendezvousName, final String text, final Integer number, final Integer numberExpected, final Class<?> expected) {
		Class<?> caught;
		Rendezvous rendezvous;
		Collection<Rendezvous> rendezvouses;
		User user;
		Rendezvous rendezvousChoosen;
		final Question question;
		Question saved;
		int countRendezvous;
		int countQuestionPerRendezvousBefore;
		int countQuestionPerUserBefore;
		final int countQuestionPerRendezvousAfter;
		final int countQuestionPerUserAfter;
		DataBinder binder;

		caught = null;

		try {
			super.startTransaction();
			this.authenticate(userName);

			//Guardamos el número de cuestiones para comprobarlo después
			rendezvousChoosen = null;

			if (rendezvousName != null)
				rendezvous = this.rendezvousService.findOne(this.getEntityId(rendezvousName));
			else
				rendezvous = null;

			countQuestionPerUserBefore = this.questionService.countByCreatorUserAccountId(LoginService.getPrincipal().getId());
			countQuestionPerRendezvousBefore = this.questionService.countByRendezvousId(rendezvous.getId());

			//1. Autenticarnos como usuario.
			user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());

			//2. Listar las citas del usuario.
			countRendezvous = this.rendezvousService.countByCreatorId(user.getId());

			for (int i = 0; i < countRendezvous; i++) {
				rendezvouses = this.rendezvousService.findByCreatorId(user.getId(), i + 1, 5);

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

			//5. Crear una cuestión para esa cita.
			question = this.questionService.create(rendezvousChoosen);
			question.setText(text);
			question.setNumber(number);

			binder = new DataBinder(question);
			saved = this.questionService.reconstruct(question, binder.getBindingResult());
			saved = this.questionService.save(saved);
			this.questionService.flush();

			//6. Volver al listado de las cuestiones.

			//Comprobamos el número y que se haya creado
			countQuestionPerRendezvousAfter = this.questionService.countByRendezvousId(rendezvous.getId());
			countQuestionPerUserAfter = this.questionService.countByCreatorUserAccountId(LoginService.getPrincipal().getId());
			Assert.isTrue(countQuestionPerRendezvousBefore + 1 == countQuestionPerRendezvousAfter);
			Assert.isTrue(countQuestionPerUserBefore + 1 == countQuestionPerUserAfter);
			Assert.isTrue(saved.getNumber() == numberExpected);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		this.checkExceptions(expected, caught);

	}

	/*
	 * Intentamos crear una question a un rendezvous distinto al del usuario autenticado (simulamos ataque por URL). Pasos:
	 * 1. Autenticarnos como usuario.
	 * 2. Crear una cuestión conocido el rendezvous(su id)
	 * 3. Volver al listado de las cuestiones. (no se ha creado)
	 */
	protected void templateCreateForNoUserRendezvous(final String userName, final String rendezvousName, final String text, final Class<?> expected) {
		Class<?> caught;
		Rendezvous rendezvous;
		final Question question;
		int countQuestionPerRendezvousBefore;
		int countQuestionPerUserBefore;
		final int countQuestionPerRendezvousAfter;
		final int countQuestionPerUserAfter;
		Question saved;
		DataBinder binder;

		caught = null;

		try {
			super.startTransaction();

			//1. Autenticarnos como usuario.
			this.authenticate(userName);

			//Guardamos el número de cuestiones para comprobarlo después

			rendezvous = this.rendezvousService.findOne(this.getEntityId(rendezvousName));

			countQuestionPerUserBefore = this.questionService.countByCreatorUserAccountId(LoginService.getPrincipal().getId());
			countQuestionPerRendezvousBefore = this.questionService.countByRendezvousId(rendezvous.getId());

			//2. Crear una cuestión conocido el rendezvous(su id)
			question = this.questionService.create(rendezvous);
			question.setText(text);

			binder = new DataBinder(question);
			saved = this.questionService.reconstruct(question, binder.getBindingResult());
			this.questionService.save(saved);
			this.questionService.flush();

			//3. Volver al listado de las cuestiones. (no se ha creado)

			//Comprobamos el número y que se haya creado
			countQuestionPerRendezvousAfter = this.questionService.countByRendezvousId(rendezvous.getId());
			countQuestionPerUserAfter = this.questionService.countByCreatorUserAccountId(LoginService.getPrincipal().getId());
			Assert.isTrue(countQuestionPerRendezvousBefore + 1 == countQuestionPerRendezvousAfter);
			Assert.isTrue(countQuestionPerUserBefore + 1 == countQuestionPerUserAfter);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		this.checkExceptions(expected, caught);

	}

}
