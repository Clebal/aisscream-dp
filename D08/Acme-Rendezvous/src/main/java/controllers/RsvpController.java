
package controllers;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AnswerService;
import services.QuestionService;
import services.RsvpService;
import domain.Answer;
import domain.Question;
import domain.Rsvp;

@Controller
@RequestMapping("/rsvp")
public class RsvpController extends AbstractController {

	// Services
	@Autowired
	private RsvpService		rsvpService;

	@Autowired
	private QuestionService	questionService;

	@Autowired
	private AnswerService	answerService;


	// Constructor
	public RsvpController() {
		super();
	}

	// List
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int rendezvousId, @RequestParam(required = false) Integer page) {
		ModelAndView result;
		Collection<Rsvp> rsvps;
		Integer size;
		Date today;

		today = new Date();

		size = 5;
		if (page == null)
			page = 1;

		rsvps = this.rsvpService.findByRendezvousIdToDisplay(rendezvousId, page, size);
		Assert.notNull(rsvps);

		result = super.paginateModelAndView("rsvp/list", this.rsvpService.countByRendezvousId(rendezvousId), page, size);
		result.addObject("requestURI", "rsvp/list.do");
		result.addObject("rsvps", rsvps);
		result.addObject("today", today);

		return result;
	}

	// Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int rsvpId) {
		ModelAndView result;
		Rsvp rsvp;
		Map<Question, Answer> questionAnswer;

		questionAnswer = new HashMap<Question, Answer>();

		rsvp = this.rsvpService.findOneToDisplay(rsvpId);
		Assert.notNull(rsvp);

		for (final Question q : this.questionService.findByRendezvousId(rsvp.getRendezvous().getId()))
			questionAnswer.put(q, this.answerService.findByRSVPIdAndQuestionId(rsvpId, q.getId()));

		result = new ModelAndView("rsvp/display");
		result.addObject("rsvp", rsvp);
		result.addObject("questionAnswer", questionAnswer);

		return result;
	}

}
