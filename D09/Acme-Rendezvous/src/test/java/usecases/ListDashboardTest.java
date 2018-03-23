
package usecases;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import security.Authority;
import security.LoginService;
import services.AnnouncementService;
import services.AnswerService;
import services.CategoryService;
import services.CommentService;
import services.ManagerService;
import services.QuestionService;
import services.RendezvousService;
import services.ServiceService;
import services.UserService;
import utilities.AbstractTest;
import domain.Manager;
import domain.Rendezvous;
import domain.Service;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ListDashboardTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private UserService			userService;

	@Autowired
	private AnnouncementService	announcementService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private QuestionService		questionService;

	@Autowired
	private CommentService		commentService;

	@Autowired
	private AnswerService		answerService;
	
	@Autowired
	private ServiceService 		serviceService;
	
	@Autowired
	private ManagerService 		managerService;

	@Autowired
	private CategoryService 	categoryService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 		1. Un usuario autenticado como ADMIN entra en el dashboard en la primera página
	 * 		2. Un usuario autenticado como ADMIN entra en el dashboard en la segunda página
	 * 		3. Un usuario autenticado como ADMIN entra en el dashboard con tamaño diez
	 * 		4. Un usuario autenticado como ADMIN entra en el dashboard con tamaño veinte
	 */
	@Test
	public void testPositiveTest() {
		final Object testingData[][] = {
			{
				"admin", 1, 5, null
			}, {
				"admin", 2, 5, null
			}, {
				"admin", 1, 10, null
			}, {
				"admin", 1, 20, null
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (Integer) testingData[i][1], (Integer) testingData[i][2], (Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	/*
	 * Pruebas:
	 * 		1. Un usuario autenticado como MANAGER trata de entrar en la vista de dashboard
	 * 		2. Un usuario autenticado como USER trata de entrar en la vista de dashboard
	 * 		3. Un usuario autenticado como ADMIN entra en el dashboard con tamaño treinta, cosa que no está disponible
	 */
	@Test
	public void testNegativeTest() {
		final Object testingData[][] = {
			{
				"manager1", 0, 0, IllegalArgumentException.class
			}, {
				"user1", 0, 0, IllegalArgumentException.class
			}, {
				"admin", 1, 30, IllegalArgumentException.class
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (Integer) testingData[i][1], (Integer) testingData[i][2], (Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	// Ancillary methods ------------------------------------------------------

	/*
	 * Listar las queries en Dashboard. Pasos:
	 * 1. Autenticar administrador
	 * 2. Obtener todos los datos
	 */
	protected void template(final String user, final Integer page, final Integer sizeTopSellingServices, final Class<?> expected) {
		Class<?> caught;
		Authority authority;
		
		Double[] rendezvousesPerUser, avgMinMaxStandardDesviationServicesPerRendezvous, usersPerRendezvous, rendezvousesRsvpdPerUser, announcementsPerRendezvous, questionsPerRendezvous, answersPerRendezvous ,repliesPerComment;
		Collection<Rendezvous> rendezvousesTop;
		Page<Service> bestSellingServices;
		Collection<Service> topSellingServices;
		Collection<Manager> managerMoreServicesAverage, managerMoreServicesCancelled;
		Double ratioUserRendezvousVsNo, avgNumberCategoriesPerRendezvous, avgRatioServicesCategory;
		Integer size;
		
		size = 5;
		caught = null;
		try {
			
			authority = new Authority();
			authority.setAuthority("ADMIN");

			// 1. Autenticar administrador
			super.authenticate(user);
			// Comprobar que el usuario autenticado es un administrador
			Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
			
			// 2. Obtener todos los datos
			rendezvousesPerUser = this.rendezvousService.avgStandardDRsvpdCreatedPerUser();
			Assert.isTrue(rendezvousesPerUser[0] == 1.6667); // The average of rendezvouses created per user
			Assert.isTrue(rendezvousesPerUser[1] == 2.4944382582947298); // The standard deviation of rendezvouses created per user 
			
			ratioUserRendezvousVsNo = this.rendezvousService.ratioCreatorsVsTotal();
			Assert.isTrue(ratioUserRendezvousVsNo == 1.0); // The ratio of users who have ever created a rendezvous versus the users who have never created any rendezvouses
			
			usersPerRendezvous = this.userService.avgStandardDUsersPerRendezvous();
			Assert.isTrue(usersPerRendezvous[0] == 1.6667); // The average of users per rendezvous 
			Assert.isTrue(usersPerRendezvous[1] == 0.7453559937670351); // The standard deviation of users per rendezvous
			
			rendezvousesRsvpdPerUser = this.rendezvousService.avgStandardDRendezvousesRsvpdPerUser();
			Assert.isTrue(rendezvousesRsvpdPerUser[0] == 1.6667); // The average of rendezvouses that are RSVPd per user
			Assert.isTrue(rendezvousesRsvpdPerUser[1] == 0.7453559937670351); // The standard deviation of rendezvouses that are RSVPd per user
			
			rendezvousesTop = this.rendezvousService.top10Rendezvouses();
			Assert.isTrue(rendezvousesTop.size() == 10); // The top-10 rendezvouses in terms of users who have RSVPd them
			
			announcementsPerRendezvous = this.announcementService.avgStandartDerivationAnnouncementPerRendezvous();
			Assert.isTrue(announcementsPerRendezvous[0] == 1.1); // The average of announcements per rendezvous
			Assert.isTrue(announcementsPerRendezvous[1] == 1.7578395831246945); // The standard deviation of announcements per rendezvous
			
			questionsPerRendezvous = this.questionService.avgStandartDerivationQuestionsPerRendezvous();
			Assert.isTrue(questionsPerRendezvous[0] == 1.2); // The average of the number of questions per rendezvous
			Assert.isTrue(questionsPerRendezvous[1] == 1.7776388834631178); // The standard deviation of the number of questions per rendezvous
			
			answersPerRendezvous = this.answerService.avgStandardAnswerPerRendezvous();
			Assert.isTrue(answersPerRendezvous[0] == 1.7); // The average of the number of answers to the questions per rendezvous
			Assert.isTrue(answersPerRendezvous[1] == 2.2825424421026654); // The standard deviation of the number of answers to the questions per rendezvous
			
			repliesPerComment = this.commentService.avgStandardRepliesPerComment();
			Assert.isTrue(repliesPerComment[0] == 0.2); // The average of replies per comment
			Assert.isTrue(repliesPerComment[1] == 0.4); // The standard deviation of replies per comment
			
			bestSellingServices = this.serviceService.bestSellingServices(page, size);
			if(page == 1) Assert.isTrue(bestSellingServices.getContent().size() == 2);
			if(page == 2) Assert.isTrue(bestSellingServices.getContent().size() == 0);

			managerMoreServicesAverage = this.managerService.managerMoreServicesAverage();
			Assert.isTrue(managerMoreServicesAverage.size() == 1);
			for(Manager m: managerMoreServicesAverage) {
				Assert.isTrue(m.getName().equals("José María"));
			}
			
			managerMoreServicesCancelled = this.managerService.managerMoreServicesCancelled();
			Assert.isTrue(managerMoreServicesCancelled.size() == 1);
			for(Manager m: managerMoreServicesCancelled) {
				Assert.isTrue(m.getName().equals("José María"));
			}
			
			avgNumberCategoriesPerRendezvous = this.categoryService.avgNumberCategoriesPerRendezvous();
			Assert.isTrue(avgNumberCategoriesPerRendezvous == 0.8);
			
			avgRatioServicesCategory = this.serviceService.ratioServicesEachCategory();
			Assert.isTrue(avgRatioServicesCategory == 0.0952381);
			
			avgMinMaxStandardDesviationServicesPerRendezvous = this.serviceService.avgMinMaxStandartDerivationServicesPerRendezvous();		
			Assert.isTrue(avgMinMaxStandardDesviationServicesPerRendezvous[0] == 0.8);
			Assert.isTrue(avgMinMaxStandardDesviationServicesPerRendezvous[1] == 0.00);
			Assert.isTrue(avgMinMaxStandardDesviationServicesPerRendezvous[2] == 2.00);
			Assert.isTrue(avgMinMaxStandardDesviationServicesPerRendezvous[3] == 0.7483314773547883);
			
			topSellingServices = this.serviceService.topBestSellingServices(sizeTopSellingServices);
			Assert.isTrue(topSellingServices.size() == 5 || topSellingServices.size() == 7);
			
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		
		super.checkExceptions(expected, caught);
	}
}
