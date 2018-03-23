
package usecases;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.LoginService;
import services.QuestionService;
import utilities.AbstractTest;
import domain.Question;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ListQuestionTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private QuestionService	questionService;


	// Tests List ------------------------------------------------------------------

	//Pruebas: Listar questions.
	/*
	 * 1. Listar cuestiones para user1. Excepción no esperada.
	 * 2. Listar cuestiones para user4. Excepción no esperada.
	 * 3. Listar cuestiones para rendezvous 1 con un número incorrecto. Excepción IllegalArgumentException esperada.
	 */

	//CU:
	//21. An actor who is authenticated as a user must be able to:
	//1. Manage the questions that are associated with a rendezvous that he or she's created previously.
	@Test
	public void driverList() {

		//Rol, rendezvous, text, ExpectedException
		final Object testingData[][] = {
			{
				"user1", 9, null
			}, {
				"user4", 0, null
			}, {
				"user2", 3, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateList((String) testingData[i][0], (Integer) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	/*
	 * Listamos cuestión de un usuario. Pasos:
	 * 1. Autenticarnos como usuario.
	 * 2. Listar las cuestiones.
	 */

	protected void templateList(final String userName, final Integer numberExpected, final Class<?> expected) {
		Class<?> caught;
		Collection<Question> questions;

		caught = null;

		try {
			super.startTransaction();

			//1. Autenticarnos como usuario.
			this.authenticate(userName);

			//2. Listar las cuestiones.
			questions = this.questionService.findByCreatorUserAccountId(LoginService.getPrincipal().getId(), 1, 5);

			Assert.isTrue(this.questionService.countByCreatorUserAccountId(LoginService.getPrincipal().getId()).equals(numberExpected));
			if (numberExpected >= 5)
				Assert.isTrue(questions.size() == 5);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		this.checkExceptions(expected, caught);

	}
}
