
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AnnouncementService;
import services.AnswerService;
import services.CommentService;
import services.QuestionService;
import services.RendezvousService;
import services.UserService;
import controllers.AbstractController;
import domain.Rendezvous;

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


	//Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
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

		rendezvousesPerUser = this.rendezvousService.avgStandardDRsvpdCreatedPerUser();
		ratioUserRendezvousVsNo = this.rendezvousService.ratioCreatorsVsTotal();
		usersPerRendezvous = this.userService.avgStandardDUsersPerRendezvous();
		rendezvousesRsvpdPerUser = this.rendezvousService.avgStandardDRendezvousesRsvpdPerUser();
		rendezvousesTop = this.rendezvousService.top10Rendezvouses();
		announcementsPerRendezvous = this.announcementService.avgStandartDerivationAnnouncementPerRendezvous();
		questionsPerRendezvous = this.questionService.avgStandartDerivationQuestionsPerRendezvous();
		answersPerRendezvous = this.answerService.avgStandardAnswerPerRendezvous();
		repliesPerComment = this.commentService.avgStandardRepliesPerComment();

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

		return result;

	}
}
