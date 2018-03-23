
package usecases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.LoginService;
import services.AnswerService;
import services.QuestionService;
import services.RendezvousService;
import utilities.AbstractTest;
import domain.Answer;
import domain.Question;
import domain.Rendezvous;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DeleteQuestionTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private QuestionService		questionService;

	//Fixtures
	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private AnswerService		answerService;


	// Tests Delete ------------------------------------------------------------------

	//Pruebas: Con pregunta que tiene un número mayor a 1.
	/*
	 * 1. Borrar question con usuario del rendezvous. Excepción no esperada.
	 * 2. Borrar question con usuario del rendezvous. Excepción no esperada.
	 */

	//CU:
	//21. An actor who is authenticated as a user must be able to:
	//1. Manage the questions that are associated with a rendezvous that he or she's created previously.
	@Test
	public void driverDeleteWithNumberBigThanOne() {

		//Rol, question, ExpectedException
		final Object testingData[][] = {
			{
				"user1", "question10", null
			}, {
				"user1", "question2", null
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateDelete((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	//Pruebas: Con pregunta cuyo número es 1.
	/*
	 * 1. Borrar question con usuario del rendezvous. Excepción no esperada.
	 * 2. Borrar question con usuario del rendezvous. Excepción no esperada.
	 */

	//CU:
	//21. An actor who is authenticated as a user must be able to:
	//1. Manage the questions that are associated with a rendezvous that he or she's created previously.
	@Test
	public void driverDeleteWithNumberEqualOne() {

		//Rol, question, ExpectedException
		final Object testingData[][] = {
			{
				"user3", "question6", null
			}, {
				"user1", "question3", null
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateDelete((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	//Pruebas: Con pregunta cuyo número es 1.
	/*
	 * 1. Borrar question(que no es del usuario) simulando actualizar el rendezvous (por uno que sí es del usuario) desde postman. Excepción IllegalArgumentException esperada.
	 * 2. Borrar question con otro usuario distinto al que creó el rendezvous. Excepción IllegalArgumentException esperada.
	 * 3. Borrar question con un manager. Excepción IllegalArgumentException esperada.
	 * 4. Borrar question sin estar autenticado. Excepción IllegalArgumentException esperada.
	 */

	//CU:
	//21. An actor who is authenticated as a user must be able to:
	//1. Manage the questions that are associated with a rendezvous that he or she's created previously.
	@Test
	public void driverDeleteByOtherRol() {

		//Rol, question, rendezvous, ExpectedException
		final Object testingData[][] = {
			{
				"user1", "question3", "rendezvous2", IllegalArgumentException.class
			}, {
				"user3", "question1", null, IllegalArgumentException.class
			}, {
				"manager1", "question1", null, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateDeleteByOtherRol((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	/*
	 * Borramos una cuestión de un rendezvous. Pasos:
	 * 1. Autenticarnos como usuario.
	 * 2. Listar las cuestiones.
	 * 3. Navegar hasta la cuestión que queremos borrar.
	 * 4. Escoger la cuestión.
	 * 5. Borrar la cuestión.
	 * 6. Volver al listado de las cuestiones.
	 */
	protected void templateDelete(final String userName, final String questionName, final Class<?> expected) {
		Class<?> caught;
		Question question;
		Question questionCopy;
		List<Question> questionsBefore;
		final List<Question> questionsAfter;
		Collection<Question> questions;
		final Collection<Answer> answers;
		Answer answerCopy;
		Question questionChoosen;
		int countQuestions;
		int rendezvousId;
		final int countQuestionPerUserBefore;
		final int countQuestionPerUserAfter;
		final int countQuestionPerRendezvousBefore;
		final int countQuestionPerRendezvousAfter;
		int number;

		caught = null;

		try {
			super.startTransaction();
			//1. Autenticarnos como usuario.
			this.authenticate(userName);

			//Guardamos el número de cuestiones para comprobarlo después
			questionChoosen = null;

			question = this.questionService.findOne(this.getEntityId(questionName));
			rendezvousId = question.getRendezvous().getId();
			number = question.getNumber();

			countQuestionPerUserBefore = this.questionService.countByCreatorUserAccountId(LoginService.getPrincipal().getId());
			countQuestionPerRendezvousBefore = this.questionService.countByRendezvousId(rendezvousId);

			//Guardamos las cuestiones superiores para comparar su número después de borrar
			questionsBefore = new ArrayList<Question>();
			for (final Question q : this.questionService.findByHigherNumber(0, rendezvousId)) {
				questionCopy = this.copyOfQuestion(q);
				questionsBefore.add(questionCopy);
			}

			//Guardamos las answers para comprobar que se han borrado
			answers = new ArrayList<Answer>();
			for (final Answer a : this.answerService.findByQuestionId(question.getId())) {
				answerCopy = this.copyOfAnswer(a);
				answers.add(answerCopy);
			}

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

			//5. Borrar la cuestión.
			question = this.questionService.findOne(questionChoosen.getId());

			this.questionService.delete(question);
			this.questionService.flush();

			//6. Volver al listado de las cuestiones.

			//Comprobamos el número y que se haya borrado
			countQuestionPerUserAfter = this.questionService.countByCreatorUserAccountId(LoginService.getPrincipal().getId());
			countQuestionPerRendezvousAfter = this.questionService.countByRendezvousId(rendezvousId);
			Assert.isTrue(countQuestionPerUserBefore == countQuestionPerUserAfter + 1);
			Assert.isTrue(countQuestionPerRendezvousBefore == countQuestionPerRendezvousAfter + 1);
			questionsAfter = new ArrayList<Question>(this.questionService.findByHigherNumber(0, rendezvousId));

			//Comprobamos que se hayan actualizado los números de las demás cuestiones
			int iBefore = 0;
			int iAfter = 0;
			int count = 0;
			while (iBefore < questionsAfter.size()) {

				if (count + 1 != number) {

					//Si los dos son iguales, no deberían ser los números distintos
					if (iAfter == iBefore)
						Assert.isTrue((questionsBefore.get(iBefore).getNumber()) == questionsAfter.get(iAfter).getNumber());

					//Si no son iguales es porque ya se ha borrado uno
					else
						Assert.isTrue((questionsBefore.get(iBefore).getNumber() - 1) == questionsAfter.get(iAfter).getNumber());

					Assert.isTrue(questionsBefore.get(iBefore).getId() == questionsAfter.get(iAfter).getId());
					iAfter++;
				}

				iBefore++;
				count++;
			}

			//Comprobamos que se han borrado todas las respuestas
			for (final Answer answer : answers)
				Assert.isNull(this.answerService.findOne(answer.getId()));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		this.checkExceptions(expected, caught);

	}
	/*
	 * Intentamos borrar una question con otro rol (simulamos ataque por URL). Pasos:
	 * 1. Autenticarnos como usuario.
	 * 2. Borrar una cuestión conocido el rendezvous(su id)
	 * 3. Volver al listado de las cuestiones. (no se ha borrado)
	 */
	protected void templateDeleteByOtherRol(final String userName, final String questionName, final String rendezvousName, final Class<?> expected) {
		Class<?> caught;
		final Question question;
		Integer countQuestionPerRendezvousBefore;
		Rendezvous rendezvous;
		final Integer countQuestionPerUserAfter;
		int rendezvousId;

		caught = null;

		try {
			super.startTransaction();

			//1. Autenticarnos como usuario.
			this.authenticate(userName);

			//2. Editar una cuestión conocido el rendezvous(su id)
			question = this.questionService.findOne(this.getEntityId(questionName));
			rendezvousId = question.getRendezvous().getId();

			//Si queremos cambiar el rendezvous
			if (rendezvousName != null) {
				rendezvous = this.rendezvousService.findOne(this.getEntityId(rendezvousName));
				question.setRendezvous(rendezvous);
				rendezvousId = rendezvous.getId();
			}

			//Guardamos el número de cuestiones para comprobarlo después

			countQuestionPerRendezvousBefore = this.questionService.countByRendezvousId(rendezvousId);

			this.questionService.delete(question);
			this.questionService.flush();

			//3. Volver al listado de las cuestiones. (no se ha creado)

			//Comprobamos el número y que se haya creado
			countQuestionPerUserAfter = this.questionService.countByRendezvousId(rendezvousId);
			Assert.isTrue(countQuestionPerRendezvousBefore.equals(countQuestionPerUserAfter));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		this.checkExceptions(expected, caught);

	}
	private Question copyOfQuestion(final Question question) {
		Question result;

		result = new Question();

		result.setRendezvous(question.getRendezvous());
		result.setId(question.getId());
		result.setNumber(question.getNumber());
		result.setText(question.getText());
		result.setVersion(question.getVersion());

		return result;
	}

	private Answer copyOfAnswer(final Answer answer) {
		Answer result;

		result = new Answer();

		result.setId(answer.getId());
		result.setQuestion(answer.getQuestion());
		result.setRsvp(answer.getRsvp());
		result.setText(answer.getText());
		result.setVersion(answer.getVersion());

		return result;
	}

}
