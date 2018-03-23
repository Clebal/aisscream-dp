
package usecases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.AnswerService;
import services.QuestionService;
import services.RsvpService;
import utilities.AbstractTest;
import domain.Answer;
import domain.Question;
import domain.Rsvp;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DisplayRsvpAnswerTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private RsvpService		rsvpService;

	@Autowired
	private AnswerService	answerService;

	//Fixtures

	@Autowired
	private QuestionService	questionService;


	// Tests Display ------------------------------------------------------------------

	//Pruebas: Display RSVP con user.
	/*
	 * 1. Display RSVP de rendezvous de menores de edad con usuario mayor de edad. Excepción no esperada.
	 * 2. Display RSVP de rendezvous de mayores de edad con usuario mayor de edad. Excepción no esperada.
	 * 3. Display RSVP de rendezvous de menores de edad con usuario menor de edad. Excepción no esperada.
	 * 4. Display RSVP de rendezvous de menores de edad con usuario mayor de edad. Excepción IllegalArgumentException esperada.
	 */

	//CU:
	//20. An actor who is not authenticated must be able to:
	//1. Display information about the users who have RSVPd a rendezvous, which, in turn, must show their answers to the questions that the creator has registered.
	@Test
	public void driverDisplayRendezvousPerUser() {

		//Rol, rsvp, questionNumberExpected, ExpectedException
		final Object testingData[][] = {
			{
				"user2", "rsvp9", 6, null
			}, {
				"user2", "rsvp2", 1, null
			}, {
				"user4", "rsvp9", 6, null
			}, {
				"user4", "rsvp4", 1, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateDisplayByRendezvous((String) testingData[i][0], (String) testingData[i][1], (Integer) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	//Pruebas: Display RSVP por rendezvous con no autenticado.
	/*
	 * 1. Display RSVP de rendezvous de menores de edad con no autenticado. Excepción no esperada.
	 * 2. Display RSVP de rendezvous de mayores de edad con no autenticado . Excepción IllegalArgumentException esperada.
	 */

	//CU:
	//20. An actor who is not authenticated must be able to:
	//1. Display information about the users who have RSVPd a rendezvous, which, in turn, must show their answers to the questions that the creator has registered.

	@Test
	public void driverDisplayRendezvousPerNotAutenticated() {

		//Rol, rsvp, questionNumberExpected, ExpectedException
		final Object testingData[][] = {
			{
				null, "rsvp9", 6, null
			}, {
				null, "rsvp2", 2, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateDisplayByRendezvous((String) testingData[i][0], (String) testingData[i][1], (Integer) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	//Pruebas: Display RSVP por rendezvous con manager.
	/*
	 * 1. Display RSVP de rendezvous de menores de edad con manager mayor de edad. Excepción no esperada.
	 * 2. Display RSVP de rendezvous de mayores de edad con manager mayor de edad. Excepción no esperada.
	 * 3. Display RSVP de rendezvous de menores de edad con manager menor de edad. Excepción no esperada.
	 * 4. Display RSVP de rendezvous de mayores de edad con manager menor de edad. Excepción IllegalArgumentException esperada.
	 */

	//CU:
	//20. An actor who is not authenticated must be able to:
	//1. Display information about the users who have RSVPd a rendezvous, which, in turn, must show their answers to the questions that the creator has registered.

	@Test
	public void driverDisplayRendezvousPerManager() {

		//Rol, rsvp, questionNumberExpected, ExpectedException
		final Object testingData[][] = {
			{
				"manager2", "rsvp9", 6, null
			}, {
				"manager1", "rsvp2", 1, null
			}, {
				"manager3", "rsvp9", 6, null
			}, {
				"manager3", "rsvp2", 1, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateDisplayByRendezvous((String) testingData[i][0], (String) testingData[i][1], (Integer) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	//Pruebas: Display RSVP por rendezvous con admin.
	/*
	 * 1. Display RSVP de rendezvous de menores de edad con admin mayor de edad. Excepción no esperada.
	 * 2. Display RSVP de rendezvous de mayores de edad con admin mayor de edad. Excepción no esperada.
	 */

	//CU:
	//20. An actor who is not authenticated must be able to:
	//1. Display information about the users who have RSVPd a rendezvous, which, in turn, must show their answers to the questions that the creator has registered.

	@Test
	public void driverDisplayRendezvousPerAdmin() {

		//Rol, rsvp, questionNumberExpected, ExpectedException
		final Object testingData[][] = {
			{
				"admin", "rsvp9", 6, null
			}, {
				"admin", "rsvp2", 1, null
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateDisplayByRendezvous((String) testingData[i][0], (String) testingData[i][1], (Integer) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	/*
	 * Display RSVP de un rendezvous. Pasos:
	 * 1. Autenticarnos como usuario.
	 * 2. Escoger RSVP.
	 */
	protected void templateDisplayByRendezvous(final String userName, final String rsvpName, final Integer numberQuestionExpected, final Class<?> expected) {
		Class<?> caught;
		Rsvp rsvp;
		List<Question> questions;
		int count;
		Map<Question, Answer> questionAnswer;

		caught = null;

		try {
			super.startTransaction();

			//1. Autenticarnos como usuario.
			this.authenticate(userName);

			//2. Escoger RSVP.

			rsvp = this.rsvpService.findOneToDisplay(this.getEntityId(rsvpName));
			questions = new ArrayList<Question>(this.questionService.findByRendezvousId(rsvp.getRendezvous().getId()));

			questionAnswer = new HashMap<Question, Answer>();

			count = 1;

			for (final Question q : questions) {
				Assert.isTrue(count == q.getNumber());
				questionAnswer.put(q, this.answerService.findByRSVPIdAndQuestionId(rsvp.getId(), q.getId()));
				count++;
			}

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		this.checkExceptions(expected, caught);

	}

}
