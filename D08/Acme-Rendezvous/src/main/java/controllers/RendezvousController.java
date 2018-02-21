
package controllers;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.LoginService;
import services.ActorService;
import services.CommentService;
import services.QuestionService;
import services.RendezvousService;
import services.RsvpService;
import services.UserService;
import domain.Actor;
import domain.Comment;
import domain.Question;
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/rendezvous")
public class RendezvousController extends AbstractController {

	// Services
	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private UserService			userService;

	@Autowired
	private CommentService		commentService;

	@Autowired
	private QuestionService		questionService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private RsvpService			rsvpService;


	// Constructor
	public RendezvousController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Rendezvous rendezvous;

		rendezvous = this.rendezvousService.findOne(rendezvousId);

		Assert.notNull(rendezvous);
		result = this.createEditModelAndView(rendezvous);

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Rendezvous> rendezvouses;
		Boolean canPermit;
		Boolean canLink;
		Boolean canUnLink;
		Calendar birthDatePlus18Years;
		Actor actor;

		if (LoginService.isAuthenticated()) {
			actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());
			birthDatePlus18Years = Calendar.getInstance();
			birthDatePlus18Years.setTime(actor.getBirthdate());
			birthDatePlus18Years.add(Calendar.YEAR, 18);
			if (birthDatePlus18Years.getTime().compareTo(new Date()) <= 0)
				canPermit = true;
			else
				canPermit = false;
		} else
			canPermit = false;

		canLink = false;
		canUnLink = false;
		rendezvouses = this.rendezvousService.findAll();

		result = new ModelAndView("rendezvous/list");
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestURI", "rendezvous/list.do");
		result.addObject("canPermit", canPermit);
		result.addObject("canLink", canLink);
		result.addObject("canUnLink", canUnLink);

		return result;
	}

	@RequestMapping(value = "/listLinkerRendezvouses", method = RequestMethod.GET)
	public ModelAndView listLinkerRendezvouses(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Collection<Rendezvous> rendezvouses;
		Boolean canPermit;
		Boolean canLink;
		Boolean canUnLink;
		Calendar birthDatePlus18Years;
		Actor actor;

		canLink = false;
		canUnLink = false;

		Assert.isTrue(rendezvousId != 0);

		if (LoginService.isAuthenticated()) {
			actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());
			birthDatePlus18Years = Calendar.getInstance();
			birthDatePlus18Years.setTime(actor.getBirthdate());
			birthDatePlus18Years.add(Calendar.YEAR, 18);
			if (birthDatePlus18Years.getTime().compareTo(new Date()) <= 0)
				canPermit = true;
			else
				canPermit = false;
		} else
			canPermit = false;
		rendezvouses = this.rendezvousService.findOne(rendezvousId).getLinkerRendezvouses();

		result = new ModelAndView("rendezvous/list");
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestURI", "rendezvous/listLinkerRendezvouses.do?rendezvousId=" + rendezvousId);
		result.addObject("canPermit", canPermit);
		result.addObject("canLink", canLink);
		result.addObject("canUnLink", canUnLink);

		return result;
	}

	@RequestMapping(value = "/listLinkedRendezvouses", method = RequestMethod.GET)
	public ModelAndView listLinkedRendezvouses(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Collection<Rendezvous> rendezvouses;
		Boolean canPermit;
		Boolean canLink;
		Boolean canUnLink;
		Calendar birthDatePlus18Years;
		Actor actor;

		canLink = false;
		canUnLink = false;

		Assert.isTrue(rendezvousId != 0);

		if (LoginService.isAuthenticated()) {
			actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());
			if (actor.getUserAccount().getId() == this.rendezvousService.findOne(rendezvousId).getCreator().getUserAccount().getId())
				canUnLink = true;
			birthDatePlus18Years = Calendar.getInstance();
			birthDatePlus18Years.setTime(actor.getBirthdate());
			birthDatePlus18Years.add(Calendar.YEAR, 18);
			if (birthDatePlus18Years.getTime().compareTo(new Date()) <= 0)
				canPermit = true;
			else
				canPermit = false;
		} else
			canPermit = false;
		rendezvouses = this.rendezvousService.findByLinkerRendezvousId(rendezvousId);

		result = new ModelAndView("rendezvous/list");
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestURI", "rendezvous/listLinkedRendezvouses.do?rendezvousId=" + rendezvousId);
		result.addObject("canPermit", canPermit);
		result.addObject("canLink", canLink);
		result.addObject("canUnLink", canUnLink);
		result.addObject("rendezvousId", rendezvousId);

		return result;
	}

	//Ancillary methods -----------------------
	protected ModelAndView createEditModelAndView(final Rendezvous rendezvous) {
		ModelAndView result;
		Boolean canPermit;
		Calendar birthDatePlus18Years;
		Collection<Question> questions;
		Boolean canCreateRSVP;
		Boolean canCreateComment;
		Authority authority;
		User user;
		Collection<Comment> comments;
		Actor actor;
		Integer size;

		authority = new Authority();
		authority.setAuthority("USER");
		if (LoginService.isAuthenticated()) {
			actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());
			birthDatePlus18Years = Calendar.getInstance();
			birthDatePlus18Years.setTime(actor.getBirthdate());
			birthDatePlus18Years.add(Calendar.YEAR, 18);
			if (birthDatePlus18Years.getTime().compareTo(new Date()) <= 0 || rendezvous.getCreator().getUserAccount().getId() == LoginService.getPrincipal().getId())
				canPermit = true;
			else
				canPermit = false;
		} else
			canPermit = false;

		questions = this.questionService.findByRendezvousId(rendezvous.getId());
		canCreateRSVP = false;
		canCreateComment = false;

		if (LoginService.isAuthenticated() && LoginService.getPrincipal().getAuthorities().contains(authority)) {
			user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
			if (this.rsvpService.findByAttendandUserIdAndRendezvousId(user.getId(), rendezvous.getId()) == null)
				canCreateRSVP = true;
			else
				canCreateComment = true;

		}

		size = this.commentService.countByRendezvousIdAndNoRepliedComment(rendezvous.getId());
		comments = this.commentService.findByRendezvousIdAndNoRepliedComment(rendezvous.getId(), 1, size);
		result = new ModelAndView("rendezvous/display");

		result.addObject("rendezvous", rendezvous);
		result.addObject("canPermit", canPermit);
		result.addObject("questions", questions);
		result.addObject("canCreateRSVP", canCreateRSVP);
		result.addObject("canCreateComment", canCreateComment);
		result.addObject("comments", comments);

		return result;

	}
}
