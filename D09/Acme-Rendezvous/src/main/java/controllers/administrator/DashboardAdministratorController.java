
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AnnouncementService;
import services.AnswerService;
import services.CategoryService;
import services.CommentService;
import services.ManagerService;
import services.QuestionService;
import services.RendezvousService;
import services.ServicioService;
import services.UserService;
import controllers.AbstractController;
import domain.Manager;
import domain.Rendezvous;
import domain.Servicio;

@Controller
@RequestMapping("/dashboard/administrator")
public class DashboardAdministratorController extends AbstractController {

	//Services
	@Autowired
	UserService			userService;

	@Autowired
	AnnouncementService	announcementService;

	@Autowired
	RendezvousService	rendezvousService;

	@Autowired
	QuestionService		questionService;

	@Autowired
	CommentService		commentService;

	@Autowired
	AnswerService		answerService;
	
	@Autowired
	ServicioService servicioService;
	
	@Autowired
	ManagerService managerService;

	@Autowired
	CategoryService categoryService;
	
	//Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required=false) Integer page, @RequestParam(required=false) Integer size) {
		Integer pageAux, sizeAux;
		final ModelAndView result;
		Double[] rendezvousesPerUser;
		final Double ratioUserRendezvousVsNo;
		final Double[] usersPerRendezvous;
		final Double[] rendezvousesRsvpdPerUser;
		Collection<Rendezvous> rendezvousesTop;
		final Double[] announcementsPerRendezvous;
		final Double[] questionsPerRendezvous;
		final Double[] answersPerRendezvous;
		final Double[] repliesPerComment;
		Page<Servicio> bestSellingServices;
		Collection<Servicio> topSellingServices;
		Collection<Manager> managerMoreServicesAverage, managerMoreServicesCancelled;
		final Double avgNumberCategoriesPerRendezvous;
		final Double avgRatioServicesCategory;
		final Double[] avgMinMaxStandardDesviationServicesPerRendezvous;

		if (page == null)
			pageAux = 1;
		else
			pageAux = page;
		
		if (size == null)
			sizeAux = 5;
		else
			sizeAux = size;

		rendezvousesPerUser = this.rendezvousService.avgStandardDRsvpdCreatedPerUser();
		ratioUserRendezvousVsNo = this.rendezvousService.ratioCreatorsVsTotal();
		usersPerRendezvous = this.userService.avgStandardDUsersPerRendezvous();
		rendezvousesRsvpdPerUser = this.rendezvousService.avgStandardDRendezvousesRsvpdPerUser();
		rendezvousesTop = this.rendezvousService.top10Rendezvouses();
		announcementsPerRendezvous = this.announcementService.avgStandartDerivationAnnouncementPerRendezvous();
		questionsPerRendezvous = this.questionService.avgStandartDerivationQuestionsPerRendezvous();
		answersPerRendezvous = this.answerService.avgStandardAnswerPerRendezvous();
		repliesPerComment = this.commentService.avgStandardRepliesPerComment();
		bestSellingServices = this.servicioService.bestSellingServices(pageAux, sizeAux);
		managerMoreServicesAverage = this.managerService.managerMoreServicesAverage();
		managerMoreServicesCancelled = this.managerService.managerMoreServicesCancelled();
		avgNumberCategoriesPerRendezvous = this.categoryService.avgNumberCategoriesPerRendezvous();
		avgRatioServicesCategory = this.servicioService.ratioServicesEachCategory();
		avgMinMaxStandardDesviationServicesPerRendezvous = this.servicioService.avgMinMaxStandartDerivationServicesPerRendezvous();		
		topSellingServices = this.servicioService.topBestSellingServices(sizeAux);
		
		result = new ModelAndView("dashboard/display");

		result.addObject("rendezvousesPerUser", rendezvousesPerUser);
		result.addObject("ratioUserRendezvousVsNo", ratioUserRendezvousVsNo);
		result.addObject("usersPerRendezvous", usersPerRendezvous);
		result.addObject("rendezvousesRsvpdPerUser", rendezvousesRsvpdPerUser);
		result.addObject("rendezvousesTop", rendezvousesTop);
		result.addObject("announcementsPerRendezvous", announcementsPerRendezvous);
		result.addObject("questionsPerRendezvous", questionsPerRendezvous);
		result.addObject("answersPerRendezvous", answersPerRendezvous);
		result.addObject("repliesPerComment", repliesPerComment);
		result.addObject("bestSellingServices", bestSellingServices.getContent());
		result.addObject("managerMoreServicesAverage", managerMoreServicesAverage);
		result.addObject("managerMoreServicesCancelled", managerMoreServicesCancelled);
		result.addObject("avgNumberCategoriesPerRendezvous", avgNumberCategoriesPerRendezvous);
		result.addObject("avgRatioServicesCategory", avgRatioServicesCategory);
		result.addObject("avgMinMaxStandardDesviationServicesPerRendezvous", avgMinMaxStandardDesviationServicesPerRendezvous);
		result.addObject("topSellingServices", topSellingServices);
		result.addObject("page", pageAux);
		result.addObject("size", sizeAux);
		result.addObject("pageNumber", bestSellingServices.getTotalPages());
		
		return result;

	}
}
