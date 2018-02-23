
package controllers;

import java.util.ArrayList;
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
import services.RendezvousService;
import services.RsvpService;
import services.UserService;
import domain.Actor;
import domain.Comment;
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
	private ActorService		actorService;

	@Autowired
	private RsvpService			rsvpService;


	// Constructor
	public RendezvousController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int rendezvousId, @RequestParam final int page) {
		ModelAndView result;
		Rendezvous rendezvous;

		rendezvous = this.rendezvousService.findOneToDisplay(rendezvousId);

		Assert.notNull(rendezvous);
		result = this.createEditModelAndView(rendezvous, page);

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int page) {
		ModelAndView result;
		Collection<Rendezvous> rendezvouses;
		Boolean canPermit;
		Boolean canLink;
		Boolean canUnLink;
		Calendar birthDatePlus18Years;
		Actor actor;
		Integer pageNumber;
		Integer size;
		Boolean haveRendezvousId;

		haveRendezvousId = false;

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
		rendezvouses = new ArrayList<Rendezvous>();
		size = 5;
		pageNumber = 0;

		if (canPermit == true)
			rendezvouses = this.rendezvousService.findAllPaginated(page, size);
		else
			rendezvouses = this.rendezvousService.findAllPublics(page, size);

		if (rendezvouses.size() != 0)
			if (canPermit == true)
				pageNumber = this.rendezvousService.countAllPaginated();
			else
				pageNumber = this.rendezvousService.countAllPublics();

		result = new ModelAndView("rendezvous/list");

		pageNumber = (int) Math.floor(((pageNumber / size) - 0.1) + 1);

		result.addObject("pageNumber", pageNumber);
		result.addObject("page", page);
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestURI", "rendezvous/list.do");
		result.addObject("canPermit", canPermit);
		result.addObject("canLink", canLink);
		result.addObject("canUnLink", canUnLink);
		result.addObject("haveRendezvousId", haveRendezvousId);

		return result;
	}

	@RequestMapping(value = "/listLinkerRendezvouses", method = RequestMethod.GET)
	public ModelAndView listLinkerRendezvouses(@RequestParam final int rendezvousId, @RequestParam final int page) {
		ModelAndView result;
		Collection<Rendezvous> rendezvouses;
		Boolean canPermit;
		Boolean canLink;
		Boolean canUnLink;
		Calendar birthDatePlus18Years;
		Actor actor;
		Integer pageNumber;
		Integer size;
		Boolean haveRendezvousId;

		haveRendezvousId = true;

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

		rendezvouses = new ArrayList<Rendezvous>();
		size = 5;
		pageNumber = 0;
		if (canPermit == true)
			rendezvouses = this.rendezvousService.findLinkerRendezvousesByRendezvousId(rendezvousId, page, size);
		else
			rendezvouses = this.rendezvousService.findLinkerRendezvousesAllPublicsByRendezvousId(rendezvousId, page, size);

		if (rendezvouses.size() != 0)
			if (canPermit == true)
				pageNumber = this.rendezvousService.countLinkerRendezvousesByRendezvousId(rendezvousId);
			else
				pageNumber = this.rendezvousService.countLinkerRendezvousesAllPublicsByRendezvousId(rendezvousId);

		result = new ModelAndView("rendezvous/list");

		pageNumber = (int) Math.floor(((pageNumber / size) - 0.1) + 1);

		result.addObject("pageNumber", pageNumber);
		result.addObject("page", page);
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestURI", "rendezvous/listLinkerRendezvouses.do");
		result.addObject("canPermit", canPermit);
		result.addObject("canLink", canLink);
		result.addObject("canUnLink", canUnLink);
		result.addObject("haveRendezvousId", haveRendezvousId);
		result.addObject("rendezvousId", rendezvousId);

		return result;
	}
	@RequestMapping(value = "/listLinkedRendezvouses", method = RequestMethod.GET)
	public ModelAndView listLinkedRendezvouses(@RequestParam final int rendezvousId, @RequestParam final int page) {
		ModelAndView result;
		Collection<Rendezvous> rendezvouses;
		Boolean canPermit;
		Boolean canLink;
		Boolean canUnLink;
		Calendar birthDatePlus18Years;
		Actor actor;
		Boolean myRendezvousIsDeleted;
		Integer pageNumber;
		Integer size;
		Boolean haveRendezvousId;

		haveRendezvousId = true;

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

		rendezvouses = new ArrayList<Rendezvous>();
		size = 5;
		pageNumber = 0;
		if (canPermit == true)
			rendezvouses = this.rendezvousService.findByLinkerRendezvousId(rendezvousId, page, size);
		else
			rendezvouses = this.rendezvousService.findByLinkerRendezvousIdAndAllpublics(rendezvousId, page, size);

		if (rendezvouses.size() != 0)
			if (canPermit == true)
				pageNumber = this.rendezvousService.countByLinkerRendezvousId(rendezvousId);
			else
				pageNumber = this.rendezvousService.countByLinkerRendezvousIdAndAllpublics(rendezvousId);

		if (this.rendezvousService.findOne(rendezvousId).getIsDeleted() == false)
			myRendezvousIsDeleted = false;
		else
			myRendezvousIsDeleted = true;

		result = new ModelAndView("rendezvous/list");

		pageNumber = (int) Math.floor(((pageNumber / size) - 0.1) + 1);

		result.addObject("pageNumber", pageNumber);
		result.addObject("page", page);
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestURI", "rendezvous/listLinkedRendezvouses.do");
		result.addObject("canPermit", canPermit);
		result.addObject("canLink", canLink);
		result.addObject("canUnLink", canUnLink);
		result.addObject("rendezvousId", rendezvousId);
		result.addObject("myRendezvousIsDeleted", myRendezvousIsDeleted);
		result.addObject("haveRendezvousId", haveRendezvousId);

		return result;
	}
	//Ancillary methods -----------------------
	protected ModelAndView createEditModelAndView(final Rendezvous rendezvous, final int page) {
		ModelAndView result;
		Boolean canPermit;
		Calendar birthDatePlus18Years;
		//final Collection<Question> questions;
		Boolean canCreateRSVP;
		Boolean canCreateComment;
		Authority authority;
		User user;
		Collection<Comment> comments;
		Actor actor;
		Integer pageNumber;
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

		//questions = this.questionService.findByRendezvousId(rendezvous.getId());
		canCreateRSVP = false;
		canCreateComment = false;

		if (LoginService.isAuthenticated() && LoginService.getPrincipal().getAuthorities().contains(authority)) {
			user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
			if (this.rsvpService.findByAttendantUserIdAndRendezvousId(user.getId(), rendezvous.getId()) == null)
				canCreateRSVP = true;
			else
				canCreateComment = true;

		}

		if (LoginService.isAuthenticated() && LoginService.getPrincipal().getId() == rendezvous.getCreator().getUserAccount().getId()) {
			canCreateRSVP = false;
			canCreateComment = true;

		}

		comments = new ArrayList<Comment>();
		size = 5;
		pageNumber = 0;

		comments = this.commentService.findByRendezvousIdAndNoRepliedComment(rendezvous.getId(), page, size);
		if (comments.size() != 0)
			pageNumber = this.commentService.countByRendezvousIdAndNoRepliedComment(rendezvous.getId());

		result = new ModelAndView("rendezvous/display");

		pageNumber = (int) Math.floor(((pageNumber / size) - 0.1) + 1);

		result.addObject("pageNumber", pageNumber);
		result.addObject("page", page);
		result.addObject("rendezvous", rendezvous);
		result.addObject("canPermit", canPermit);
		//result.addObject("questions", questions);
		result.addObject("canCreateRSVP", canCreateRSVP);
		result.addObject("canCreateComment", canCreateComment);
		result.addObject("comments", comments);

		return result;

	}
}
