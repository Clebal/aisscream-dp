
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
import utilities.AbstractTest;
import domain.Question;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EditQuestionTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private QuestionService	questionService;


	// Tests Edit ------------------------------------------------------------------
	//Pruebas: Con pregunta que tiene un número mayor a 1.
	/*
	 * 1. Editar question con usuario del rendezvous(y número diferente). Excepción IllegalArgumentException esperada.
	 * 2. Editar question con usuario del rendezvous. Excepción no esperada.
	 * 3. Editar question con usuario del rendezvous y pregunta vacía. ConstraintViolationException esperada.
	 * 4. Editar question con usuario del rendezvous y pregunta null. ConstraintViolationException esperada.
	 */
	//CU:
	//21. An actor who is authenticated as a user must be able to:
	//1. Manage the questions that are associated with a rendezvous that he or she's created previously.
	@Test
	public void driverEditWithNumberBigThanOne() {

		//Rol, rendezvous, text, number, numberExpected, ExpectedException
		final Object testingData[][] = {
			{
				"user1", "question10", "¿Te gusta esta cita?", 3, 4, IllegalArgumentException.class
			}, {
				"user1", "question10", "¿Necesitas más preguntas?", 4, 4, null
			}, {
				"user1", "question10", "", 4, 4, ConstraintViolationException.class
			}, {
				"user1", "question10", null, 4, 4, ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	//Pruebas: Con pregunta cuyo número es 1.
	/*
	 * 1. Editar question con usuario del rendezvous(y número diferente). Excepción IllegalArgumentException esperada.
	 * 2. Editar question con usuario del rendezvous. Excepción no esperada.
	 * 3. Editar question con usuario del rendezvous y pregunta vacía. ConstraintViolationException esperada.
	 * 4. Editar question con usuario del rendezvous y pregunta null. ConstraintViolationException esperada.
	 */
	//CU:
	//21. An actor who is authenticated as a user must be able to:
	//1. Manage the questions that are associated with a rendezvous that he or she's created previously.
	@Test
	public void driverEditWithNumberEqualOne() {

		//Rol, rendezvous, text, number, numberExpected, ExpectedException
		final Object testingData[][] = {
			{
				"user3", "question6", "¿Te gusta esta cita?", 2, 1, IllegalArgumentException.class
			}, {
				"user3", "question6", "¿Te gusta esta cita más que la anterior?", 1, 1, null
			}, {
				"user3", "question6", "", 1, 1, ConstraintViolationException.class
			}, {
				"user3", "question6", null, 1, 1, ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	//Pruebas: Editar question para rendezvous que no es del usuario.
	/*
	 * 1. Editar question por un usuario que no es del rendezvous. IllegalArgumentException esperada.
	 * 2. Crear question por un admin. IllegalArgumentException esperada.
	 * 3. Crear question sin autenticarse como usuario. IllegalArgumentException esperada.
	 */
	//CU:
	//21. An actor who is authenticated as a user must be able to:
	//1. Manage the questions that are associated with a rendezvous that he or she's created previously.
	@Test
	public void driverEditUserNoCreatorOfRendezvous() {

		//Rol, rendezvous, text, ExpectedException
		final Object testingData[][] = {
			{
				"user2", "question4", "¿Te gusta esta cita?", IllegalArgumentException.class
			}, {
				"admin", "question6", "¿Te gusta esta cita?", IllegalArgumentException.class
			}, {
				null, "question5", "¿Te gusta esta cita?", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateEditForNoUserRendezvous((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	/*
	 * Editamos una cuestión de un rendezvous. Pasos:
	 * 1. Autenticarnos como usuario.
	 * 2. Listar las cuestiones.
	 * 3. Navegar hasta la cuestión que queremos editar.
	 * 4. Escoger la cuestión.
	 * 5. Cambiar la cuestión.
	 * 6. Volver al listado de las cuestiones.
	 */
	protected void templateEdit(final String userName, final String questionName, final String text, final Integer number, final Integer expectedNumber, final Class<?> expected) {
		Class<?> caught;
		Question question;
		Collection<Question> questions;
		Question questionChoosen;
		Question saved;
		int countQuestions;
		Integer countQuestionPerUserBefore;
		final Integer countQuestionPerUserAfter;
		Integer numberExpected;
		DataBinder binder;

		caught = null;

		try {
			super.startTransaction();

			//1. Autenticarnos como usuario.
			this.authenticate(userName);

			//Guardamos el número de cuestiones para comprobarlo después
			questionChoosen = null;

			question = this.questionService.findOne(this.getEntityId(questionName));

			countQuestionPerUserBefore = this.questionService.countByCreatorUserAccountId(LoginService.getPrincipal().getId());

			//2. Listar las cuestiones.
			countQuestions = this.questionService.countByCreatorUserAccountId(LoginService.getPrincipal().getId());

			for (int i = 0; i < countQuestions; i++) {
				questions = this.questionService.findByCreatorUserAccountId(LoginService.getPrincipal().getId(), i + 1, 5);

				//Si estamos pidiendo una página mayor
				if (questions.size() == 0)
					break;

				//3. Navegar hasta la cuestión que queremos editar.
				for (final Question editQuestion : questions)
					if (editQuestion.equals(question)) {
						//4. Escoger la cuestión.
						questionChoosen = question;
						break;
					}

				if (questionChoosen != null)
					break;
			}

			//5. Cambiar la cuestión.
			question = this.questionService.findOne(questionChoosen.getId());
			numberExpected = question.getNumber();
			question.setText(text);
			question.setNumber(number);

			binder = new DataBinder(question);
			saved = this.questionService.reconstruct(question, binder.getBindingResult());
			saved = this.questionService.save(saved);
			this.questionService.flush();

			//6. Volver al listado de las cuestiones.

			//Comprobamos el número y que se haya creado
			countQuestionPerUserAfter = this.questionService.countByCreatorUserAccountId(LoginService.getPrincipal().getId());
			Assert.isTrue(countQuestionPerUserBefore.equals(countQuestionPerUserAfter));
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
	 * Intentamos editar una question a un rendezvous distinto al del usuario autenticado (simulamos ataque por URL). Pasos:
	 * 1. Autenticarnos como usuario.
	 * 2. Editar una cuestión conocido el rendezvous(su id)
	 * 3. Volver al listado de las cuestiones. (no se ha creado)
	 */
	protected void templateEditForNoUserRendezvous(final String userName, final String questionName, final String text, final Class<?> expected) {
		Class<?> caught;
		final Question question;
		Integer countQuestionPerUserBefore;
		final Integer countQuestionPerUserAfter;
		DataBinder binder;
		Question saved;

		caught = null;

		try {
			super.startTransaction();

			//1. Autenticarnos como usuario.
			this.authenticate(userName);

			//Guardamos el número de cuestiones para comprobarlo después

			countQuestionPerUserBefore = this.questionService.countByCreatorUserAccountId(LoginService.getPrincipal().getId());

			//2. Editar una cuestión conocido el rendezvous(su id)
			question = this.questionService.findOne(this.getEntityId(questionName));
			question.setText(text);

			binder = new DataBinder(question);
			saved = this.questionService.reconstruct(question, binder.getBindingResult());
			this.questionService.save(saved);
			this.questionService.flush();

			//3. Volver al listado de las cuestiones. (no se ha creado)

			//Comprobamos el número y que se haya creado
			countQuestionPerUserAfter = this.questionService.countByCreatorUserAccountId(LoginService.getPrincipal().getId());
			Assert.isTrue(countQuestionPerUserBefore.equals(countQuestionPerUserAfter));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		this.checkExceptions(expected, caught);

	}

}
