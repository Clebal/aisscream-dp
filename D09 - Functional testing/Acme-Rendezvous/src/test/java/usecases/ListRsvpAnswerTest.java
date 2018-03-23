
package usecases;

import java.util.ArrayList;
import java.util.Collection;
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

import security.LoginService;
import services.AnswerService;
import services.QuestionService;
import services.RendezvousService;
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
public class ListRsvpAnswerTest extends AbstractTest {

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


	// Tests List ------------------------------------------------------------------

	//Pruebas: Listar RSVP por usuario.
	/*
	 * 1. Listar RSVP para user3. Excepción no esperada.
	 * 2. Listar RSVP para user 2 con un número incorrecto. Excepción IllegalArgumentException esperada.
	 * 3. Listar RSVP sin estar autencicado. Excepción IllegalArgumentException esperada.
	 */

	//NO CU: List Rsvp
	@Test
	public void driverListByAttendant() {

		//Rol, numberExpected, ExpectedException
		final Object testingData[][] = {
			{
				"user3", 2, null
			}, {
				"user2", 2, IllegalArgumentException.class
			}, {
				null, 2, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateListByAttendant((String) testingData[i][0], (Integer) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	//Pruebas: Listar RSVP por rendezvous con user.
	/*
	 * 1. Listar RSVP de rendezvous de menores de edad con usuario mayor de edad. Excepción no esperada.
	 * 2. Listar RSVP de rendezvous de mayores de edad con usuario mayor de edad. Excepción no esperada.
	 * 3. Listar RSVP de rendezvous de menores de edad con usuario menor de edad. Excepción no esperada.
	 * 4. Listar RSVP de rendezvous de menores de edad con usuario mayor de edad. Excepción IllegalArgumentException esperada.
	 */

	//NO CU: List Rsvp, choose one and list it questions and answers.

	@Test
	public void driverListRendezvousPerUser() {

		//Rol, rendezvous, rsvp, questionNumberExpected, numberExpected, ExpectedException
		final Object testingData[][] = {
			{
				"user2", "rendezvous5", "rsvp9", 6, 1, null
			}, {
				"user2", "rendezvous2", "rsvp2", 1, 2, null
			}, {
				"user4", "rendezvous8", null, 0, 0, null
			}, {
				"user4", "rendezvous2", "rsvp4", 1, 2, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateListByRendezvous((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	//Pruebas: Listar RSVP por rendezvous con no autenticado.
	/*
	 * 1. Listar RSVP de rendezvous de menores de edad con no autenticado. Excepción no esperada.
	 * 2. Listar RSVP de rendezvous de mayores de edad con no autenticado . Excepción IllegalArgumentException esperada.
	 */

	//NO CU: List Rsvp, choose one and list it questions and answers.

	@Test
	public void driverListRendezvousPerNotAutenticated() {

		//Rol, rendezvous, rsvp, questionNumberExpected, numberExpected, ExpectedException
		final Object testingData[][] = {
			{
				null, "rendezvous5", "rsvp9", 6, 1, null
			}, {
				null, "rendezvous2", "rsvp2", 1, 2, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateListByRendezvous((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	//Pruebas: Listar RSVP por rendezvous con manager.
	/*
	 * 1. Listar RSVP de rendezvous de menores de edad con manager mayor de edad. Excepción no esperada.
	 * 2. Listar RSVP de rendezvous de mayores de edad con manager mayor de edad. Excepción no esperada.
	 * 3. Listar RSVP de rendezvous de menores de edad con manager menor de edad. Excepción no esperada.
	 * 4. Listar RSVP de rendezvous de mayores de edad con manager menor de edad. Excepción IllegalArgumentException esperada.
	 */
	//NO CU: List Rsvp, choose one and list it questions and answers.

	@Test
	public void driverListRendezvousPerManager() {

		//Rol, rendezvous, rsvp, questionNumberExpected, numberExpected, ExpectedException
		final Object testingData[][] = {
			{
				"manager2", "rendezvous5", "rsvp9", 6, 1, null
			}, {
				"manager1", "rendezvous2", "rsvp2", 1, 2, null
			}, {
				"manager3", "rendezvous5", "rsvp9", 6, 1, null
			}, {
				"manager3", "rendezvous2", "rsvp2", 1, 2, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateListByRendezvous((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	//Pruebas: Listar RSVP por rendezvous con admin.
	/*
	 * 1. Listar RSVP de rendezvous de menores de edad con admin mayor de edad. Excepción no esperada.
	 * 2. Listar RSVP de rendezvous de mayores de edad con admin mayor de edad. Excepción no esperada.
	 */

	//NO CU: List Rsvp, choose one and list it questions and answers.

	@Test
	public void driverListRendezvousPerAdmin() {

		//Rol, rendezvous, rsvp, questionNumberExpected, numberExpected, ExpectedException
		final Object testingData[][] = {
			{
				"admin", "rendezvous5", "rsvp9", 6, 1, null
			}, {
				"admin", "rendezvous2", "rsvp2", 1, 2, null
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateListByRendezvous((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	/*
	 * Listamos los RSVP de un user. Pasos:
	 * 1. Autenticarnos como usuario.
	 * 2. Listar los rsvps.
	 */
	protected void templateListByAttendant(final String userName, final Integer numberExpected, final Class<?> expected) {
		Class<?> caught;
		Collection<Rsvp> rsvps;

		caught = null;

		try {
			super.startTransaction();

			//1. Autenticarnos como usuario.
			this.authenticate(userName);

			//2. Listar los rsvp.
			rsvps = this.rsvpService.findByAttendantUserAccountId(LoginService.getPrincipal().getId(), 1, 5);

			Assert.isTrue(this.rsvpService.countByAttendantUserAccountId(LoginService.getPrincipal().getId()).equals(numberExpected));
			if (numberExpected >= 5)
				Assert.isTrue(rsvps.size() == 5);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		this.checkExceptions(expected, caught);

	}

	/*
	 * Lis RSVPs de un rendezvous. Pasos:
	 * 1. Autenticarnos como usuario.
	 * 2. Listar los rsvps.
	 * 3. Escoger un rsvp.
	 */
	protected void templateListByRendezvous(final String userName, final String rendezvousName, final String rsvpName, final Integer numberQuestionExpected, final Integer numberExpected, final Class<?> expected) {
		Class<?> caught;
		Collection<Rsvp> rsvps;
		int rendezvousId;
		Rsvp rsvp;
		List<Question> questions;
		int count;
		Map<Question, Answer> questionAnswer;
		int countRendezvous;
		Rsvp rsvpChoosen;

		caught = null;

		try {
			super.startTransaction();

			//1. Autenticarnos como usuario.
			this.authenticate(userName);

			//2. Listar los rsvp.
			rsvpChoosen = null;
			rsvp = null;
			rendezvousId = this.rendezvousService.findOne(this.getEntityId(rendezvousName)).getId();

			if (rsvpName != null)
				rsvp = this.rsvpService.findOneToDisplay(this.getEntityId(rsvpName));

			countRendezvous = this.rsvpService.countByRendezvousId(rendezvousId);
			Assert.isTrue(countRendezvous == numberExpected);

			for (int i = 0; i < countRendezvous; i++) {

				rsvps = this.rsvpService.findByRendezvousIdToDisplay(rendezvousId, i + 1, 5);

				if (numberExpected >= 5 && i == 0)
					Assert.isTrue(rsvps.size() == 5);

				//Si estamos pidiendo una página mayor
				if (rsvps.size() == 0)
					break;

				for (final Rsvp newRsvp : rsvps)
					if (newRsvp.equals(rsvp)) {
						//4. Escoger un RSVP.
						rsvpChoosen = rsvp;
						break;
					}

				if (rsvpChoosen != null)
					break;
			}

			//Vemos el display de rsvp

			if (rsvpChoosen != null) {
				questions = new ArrayList<Question>(this.questionService.findByRendezvousId(rsvpChoosen.getRendezvous().getId()));

				questionAnswer = new HashMap<Question, Answer>();

				count = 1;

				for (final Question q : questions) {
					Assert.isTrue(count == q.getNumber());
					questionAnswer.put(q, this.answerService.findByRSVPIdAndQuestionId(rsvpChoosen.getId(), q.getId()));
					count++;
				}
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
