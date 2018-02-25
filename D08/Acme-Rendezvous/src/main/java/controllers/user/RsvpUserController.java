
package controllers.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.AnswerService;
import services.QuestionService;
import services.RendezvousService;
import services.RsvpService;
import services.UserService;
import controllers.AbstractController;
import domain.Answer;
import domain.Question;
import domain.Rendezvous;
import domain.Rsvp;
import domain.User;
import forms.RsvpForm;

@Controller
@RequestMapping("/rsvp/user")
public class RsvpUserController extends AbstractController {

	// Services
	@Autowired
	private RsvpService			rsvpService;

	@Autowired
	private AnswerService		answerService;

	@Autowired
	private UserService			userService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private QuestionService		questionService;


	// Constructor
	public RsvpUserController() {
		super();
	}

	// List
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required=false) Integer page) {
		ModelAndView result;
		Collection<Rsvp> rsvps;
		Integer size;
		
		size = 5;
		if(page == null) page = 1;

		rsvps = this.rsvpService.findByAttendantUserAccountId(LoginService.getPrincipal().getId(), page, size);
		Assert.notNull(rsvps);
		
		result = super.paginateModelAndView("rsvp/list", this.rsvpService.countByAttendantUserAccountId(LoginService.getPrincipal().getId()), page, size);
		result.addObject("rsvps", rsvps);
		result.addObject("requestURI", "rsvp/user/list.do");

		return result;
	}
	
	// Disply
	@RequestMapping(value="/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required=false) final int rsvpId) {
		ModelAndView result;
		Rsvp rsvp;
		
		rsvp = this.rsvpService.findOne(rsvpId);
		Assert.notNull(rsvp);
		
		result = new ModelAndView("rsvp/display");
		result.addObject("rsvp", rsvp);
		result.addObject("requestURI", "rsvp/user/display.do");
		
		return result;
	}
	
	// Cancel
	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView editCancel(@RequestParam final int rsvpId) {
		ModelAndView result;
		Rsvp rsvp;

		rsvp = this.rsvpService.findOne(rsvpId);

		rsvp.setStatus("CANCELLED");
		this.rsvpService.save(rsvp);
		result = new ModelAndView("redirect:list.do");

		return result;
	}

	// Accept
	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView editAccept(@RequestParam final int rsvpId) {
		ModelAndView result;
		Rsvp rsvp;

		rsvp = this.rsvpService.findOne(rsvpId);

		rsvp.setStatus("ACCEPTED");
		this.rsvpService.save(rsvp);
		result = new ModelAndView("redirect:list.do");

		return result;
	}


	// Create-------------------------------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Rendezvous rendezvous;
		Collection<Question> questions;
		Map<Integer, String> questionsMap;
		Map<Integer, String> answersMap;
		RsvpForm rsvpForm;

		rendezvous = this.rendezvousService.findOne(rendezvousId);
		Assert.notNull(rendezvous);

		questions = this.questionService.findByRendezvousId(rendezvousId);

		questionsMap = new HashMap<Integer, String>();
		answersMap = new HashMap<Integer, String>();

		rsvpForm = new RsvpForm();

		for (final Question question : questions) {
			questionsMap.put(question.getId(), question.getText());
			answersMap.put(question.getId(), "");
		}

		rsvpForm.setQuestions(questionsMap);
		rsvpForm.setAnswers(answersMap);
		rsvpForm.setRendezvousId(rendezvousId);

		result = this.createEditModelAndView2(rsvpForm);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final RsvpForm rsvpForm, final BindingResult binding) {
		ModelAndView result;
		final Rsvp rsvp;
		Collection<Answer> answers;
		User user;
		Rendezvous rendezvous;
		boolean next;
		boolean deleteRsvp;

		next = true;
		answers = new ArrayList<Answer>();
		deleteRsvp = false;
		result = null;

		//Reconstruimos las respuestas y creamos el rsvp
		try {
			answers = this.answerService.reconstruct(rsvpForm, binding);

		} catch (final Throwable e) {
			next = false;
			result = this.createEditModelAndView2(rsvpForm, "rsvp.commit.error", new ArrayList<Answer>());
		}

		//Si todo ha ido bien anteriormente vemos si hay errores y si no guardamos las respuestas
		if (next)
			if (binding.hasErrors()) {
				result = this.createEditModelAndView2(rsvpForm, null, answers);
				deleteRsvp = true;

			} else
				try {
					//Si hay preguntas las guardamos
					if (answers.size() != 0)
						this.answerService.saveAnswers(answers);
					result = new ModelAndView("redirect:list.do");

					//Si se produce algún fallo en este momento, borramos el rsvp creado anteriormente	
				} catch (final Throwable oops) {

					deleteRsvp = true;
					result = this.createEditModelAndView2(rsvpForm, "rsvp.commit.error", new ArrayList<Answer>());
				}

		if (deleteRsvp) {
			user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
			rendezvous = this.rendezvousService.findOne(rsvpForm.getRendezvousId());

			rsvp = this.rsvpService.findByAttendantUserIdAndRendezvousId(user.getId(), rendezvous.getId());

			if (rsvp != null)
				this.rsvpService.delete(rsvp);
		}

		return result;
	}

	protected ModelAndView createEditModelAndView2(final RsvpForm rsvpForm) {
		ModelAndView result;

		result = this.createEditModelAndView2(rsvpForm, null, new ArrayList<Answer>());

		return result;
	}

	protected ModelAndView createEditModelAndView2(final RsvpForm rsvpForm, final String messageCode, final Collection<Answer> answers) {
		ModelAndView result;

		result = new ModelAndView("rsvp/edit");

		result.addObject("rsvpForm", rsvpForm);
		result.addObject("answers", answers);
		result.addObject("message", messageCode);

		return result;
	}
}
