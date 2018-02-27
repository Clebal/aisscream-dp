
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
import services.AnnouncementService;
import services.CommentService;
import services.RendezvousService;
import services.RsvpService;
import services.UserService;
import domain.Actor;
import domain.Announcement;
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

	@Autowired
	private AnnouncementService	announcementService;


	// Constructor
	public RendezvousController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int rendezvousId, @RequestParam(required = false) final Integer page, @RequestParam(required = false) final Integer page2, @RequestParam(required = false) final Integer page3, @RequestParam(
		required = false) final Integer page4) {
		ModelAndView result;
		Rendezvous rendezvous;
		Integer pageAux;
		Integer pageAux2;
		Integer pageAux3;
		Integer pageAux4;

		if (page == null)
			pageAux = 1;
		else
			pageAux = page;

		if (page2 == null)
			pageAux2 = 1;
		else
			pageAux2 = page2;

		if (page3 == null)
			pageAux3 = 1;
		else
			pageAux3 = page3;

		if (page4 == null)
			pageAux4 = 1;
		else
			pageAux4 = page4;

		rendezvous = this.rendezvousService.findOneToDisplay(rendezvousId);

		Assert.notNull(rendezvous);
		result = this.createEditModelAndView(rendezvous, pageAux, pageAux2, pageAux3, pageAux4);

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final Integer page) {
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
		Authority authority;
		Authority authority2;
		Integer pageAux;
		Date currentMomentVar;

		currentMomentVar = new Date();

		if (page == null)
			pageAux = 1;
		else
			pageAux = page;

		authority = new Authority();
		authority.setAuthority("USER");

		authority2 = new Authority();
		authority2.setAuthority("ADMIN");

		haveRendezvousId = false;
		canPermit = false;
		if (LoginService.isAuthenticated())
			if (LoginService.getPrincipal().getAuthorities().contains(authority)) {
				actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());
				birthDatePlus18Years = Calendar.getInstance();
				birthDatePlus18Years.setTime(actor.getBirthdate());
				birthDatePlus18Years.add(Calendar.YEAR, 18);
				if (birthDatePlus18Years.getTime().compareTo(new Date()) <= 0)
					canPermit = true;
			} else if (LoginService.getPrincipal().getAuthorities().contains(authority2))
				canPermit = true;

		canLink = false;
		canUnLink = false;
		rendezvouses = new ArrayList<Rendezvous>();
		size = 5;
		pageNumber = 0;

		if (canPermit == true)
			rendezvouses = this.rendezvousService.findAllPaginated(pageAux, size);
		else
			rendezvouses = this.rendezvousService.findAllPublics(pageAux, size);

		if (rendezvouses.size() != 0)
			if (canPermit == true)
				pageNumber = this.rendezvousService.countAllPaginated();
			else
				pageNumber = this.rendezvousService.countAllPublics();

		result = new ModelAndView("rendezvous/list");

		pageNumber = (int) Math.floor(((pageNumber / (size + 0.0)) - 0.1) + 1);

		result.addObject("pageNumber", pageNumber);
		result.addObject("page", pageAux);
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestURI", "rendezvous/list.do");
		result.addObject("canPermit", canPermit);
		result.addObject("canLink", canLink);
		result.addObject("canUnLink", canUnLink);
		result.addObject("haveRendezvousId", haveRendezvousId);
		result.addObject("currentMomentVar", currentMomentVar);

		return result;
	}

	@RequestMapping(value = "/listByUser", method = RequestMethod.GET)
	public ModelAndView listByUser(@RequestParam final int userId, @RequestParam(required = false) final Integer page) {
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
		Authority authority;
		Authority authority2;
		Integer pageAux;
		Date currentMomentVar;

		currentMomentVar = new Date();

		if (page == null)
			pageAux = 1;
		else
			pageAux = page;

		authority = new Authority();
		authority.setAuthority("USER");

		authority2 = new Authority();
		authority2.setAuthority("ADMIN");

		haveRendezvousId = false;
		canPermit = false;
		if (LoginService.isAuthenticated())
			if (LoginService.getPrincipal().getAuthorities().contains(authority)) {
				actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());
				birthDatePlus18Years = Calendar.getInstance();
				birthDatePlus18Years.setTime(actor.getBirthdate());
				birthDatePlus18Years.add(Calendar.YEAR, 18);
				if (birthDatePlus18Years.getTime().compareTo(new Date()) <= 0)
					canPermit = true;
			} else if (LoginService.getPrincipal().getAuthorities().contains(authority2))
				canPermit = true;

		canLink = false;
		canUnLink = false;
		rendezvouses = new ArrayList<Rendezvous>();
		size = 5;
		pageNumber = 0;

		if (canPermit == true)
			rendezvouses = this.rendezvousService.findByCreatorId(userId, pageAux, size);
		else
			rendezvouses = this.rendezvousService.findByCreatorIdAllPublics(userId, pageAux, size);

		if (rendezvouses.size() != 0)
			if (canPermit == true)
				pageNumber = this.rendezvousService.countByCreatorId(userId);
			else
				pageNumber = this.rendezvousService.countByCreatorIdAllPublics(userId);

		result = new ModelAndView("rendezvous/list");

		pageNumber = (int) Math.floor(((pageNumber / (size + 0.0)) - 0.1) + 1);

		result.addObject("pageNumber", pageNumber);
		result.addObject("page", pageAux);
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestURI", "rendezvous/listByUser.do");
		result.addObject("canPermit", canPermit);
		result.addObject("canLink", canLink);
		result.addObject("canUnLink", canUnLink);
		result.addObject("haveRendezvousId", haveRendezvousId);
		result.addObject("userId", userId);
		result.addObject("currentMomentVar", currentMomentVar);

		return result;
	}

	@RequestMapping(value = "/listByAttendant", method = RequestMethod.GET)
	public ModelAndView listByAttendant(@RequestParam final int attendantId, @RequestParam(required = false) final Integer page) {
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
		Authority authority;
		Authority authority2;
		Integer pageAux;
		Date currentMomentVar;

		currentMomentVar = new Date();

		if (page == null)
			pageAux = 1;
		else
			pageAux = page;

		authority = new Authority();
		authority.setAuthority("USER");

		authority2 = new Authority();
		authority2.setAuthority("ADMIN");

		haveRendezvousId = false;
		canPermit = false;
		if (LoginService.isAuthenticated())
			if (LoginService.getPrincipal().getAuthorities().contains(authority)) {
				actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());
				birthDatePlus18Years = Calendar.getInstance();
				birthDatePlus18Years.setTime(actor.getBirthdate());
				birthDatePlus18Years.add(Calendar.YEAR, 18);
				if (birthDatePlus18Years.getTime().compareTo(new Date()) <= 0)
					canPermit = true;
			} else if (LoginService.getPrincipal().getAuthorities().contains(authority2))
				canPermit = true;

		canLink = false;
		canUnLink = false;
		rendezvouses = new ArrayList<Rendezvous>();
		size = 5;
		pageNumber = 0;

		if (canPermit == true)
			rendezvouses = this.rendezvousService.findByAttendantId(attendantId, pageAux, size);
		else
			rendezvouses = this.rendezvousService.findByAttendantIdAllPublics(attendantId, pageAux, size);

		if (rendezvouses.size() != 0)
			if (canPermit == true)
				pageNumber = this.rendezvousService.countByAttendantId(attendantId);
			else
				pageNumber = this.rendezvousService.countByAttendantIdAllPublics(attendantId);

		result = new ModelAndView("rendezvous/list");

		pageNumber = (int) Math.floor(((pageNumber / (size + 0.0)) - 0.1) + 1);

		result.addObject("pageNumber", pageNumber);
		result.addObject("page", pageAux);
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestURI", "rendezvous/listByAttendant.do");
		result.addObject("canPermit", canPermit);
		result.addObject("canLink", canLink);
		result.addObject("canUnLink", canUnLink);
		result.addObject("haveRendezvousId", haveRendezvousId);
		result.addObject("attendantId", attendantId);
		result.addObject("currentMomentVar", currentMomentVar);

		return result;
	}

	@RequestMapping(value = "/listLinkerRendezvouses", method = RequestMethod.GET)
	public ModelAndView listLinkerRendezvouses(@RequestParam final int rendezvousId, @RequestParam(required = false) final Integer page) {
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
		Authority authority;
		Authority authority2;
		Integer pageAux;
		Date currentMomentVar;

		currentMomentVar = new Date();

		if (page == null)
			pageAux = 1;
		else
			pageAux = page;

		authority = new Authority();
		authority.setAuthority("USER");

		authority2 = new Authority();
		authority2.setAuthority("ADMIN");

		haveRendezvousId = true;

		canLink = false;
		canUnLink = false;
		canPermit = false;

		Assert.isTrue(rendezvousId != 0);

		if (LoginService.isAuthenticated())
			if (LoginService.getPrincipal().getAuthorities().contains(authority)) {
				actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());
				birthDatePlus18Years = Calendar.getInstance();
				birthDatePlus18Years.setTime(actor.getBirthdate());
				birthDatePlus18Years.add(Calendar.YEAR, 18);
				if (birthDatePlus18Years.getTime().compareTo(new Date()) <= 0)
					canPermit = true;
			} else if (LoginService.getPrincipal().getAuthorities().contains(authority2))
				canPermit = true;

		rendezvouses = new ArrayList<Rendezvous>();
		size = 5;
		pageNumber = 0;
		if (canPermit == true)
			rendezvouses = this.rendezvousService.findLinkerRendezvousesByRendezvousId(rendezvousId, pageAux, size);
		else
			rendezvouses = this.rendezvousService.findLinkerRendezvousesAllPublicsByRendezvousId(rendezvousId, pageAux, size);

		if (rendezvouses.size() != 0)
			if (canPermit == true)
				pageNumber = this.rendezvousService.countLinkerRendezvousesByRendezvousId(rendezvousId);
			else
				pageNumber = this.rendezvousService.countLinkerRendezvousesAllPublicsByRendezvousId(rendezvousId);

		result = new ModelAndView("rendezvous/list");

		pageNumber = (int) Math.floor(((pageNumber / (size + 0.0)) - 0.1) + 1);

		result.addObject("pageNumber", pageNumber);
		result.addObject("page", pageAux);
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestURI", "rendezvous/listLinkerRendezvouses.do");
		result.addObject("canPermit", canPermit);
		result.addObject("canLink", canLink);
		result.addObject("canUnLink", canUnLink);
		result.addObject("haveRendezvousId", haveRendezvousId);
		result.addObject("rendezvousId", rendezvousId);
		result.addObject("currentMomentVar", currentMomentVar);

		return result;
	}
	@RequestMapping(value = "/listLinkedRendezvouses", method = RequestMethod.GET)
	public ModelAndView listLinkedRendezvouses(@RequestParam final int rendezvousId, @RequestParam(required = false) final Integer page) {
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
		Authority authority;
		Authority authority2;
		Integer pageAux;
		Date currentMomentVar;

		currentMomentVar = new Date();

		if (page == null)
			pageAux = 1;
		else
			pageAux = page;

		authority = new Authority();
		authority.setAuthority("USER");

		authority2 = new Authority();
		authority2.setAuthority("ADMIN");

		haveRendezvousId = true;

		canLink = false;
		canUnLink = false;
		canPermit = false;

		Assert.isTrue(rendezvousId != 0);

		if (LoginService.isAuthenticated())
			if (LoginService.getPrincipal().getAuthorities().contains(authority)) {
				actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());
				if (actor.getUserAccount().getId() == this.rendezvousService.findOne(rendezvousId).getCreator().getUserAccount().getId())
					canUnLink = true;
				birthDatePlus18Years = Calendar.getInstance();
				birthDatePlus18Years.setTime(actor.getBirthdate());
				birthDatePlus18Years.add(Calendar.YEAR, 18);
				if (birthDatePlus18Years.getTime().compareTo(new Date()) <= 0)
					canPermit = true;
			} else if (LoginService.getPrincipal().getAuthorities().contains(authority2))
				canPermit = true;

		rendezvouses = new ArrayList<Rendezvous>();
		size = 5;
		pageNumber = 0;
		if (canPermit == true)
			rendezvouses = this.rendezvousService.findByLinkerRendezvousId(rendezvousId, pageAux, size);
		else
			rendezvouses = this.rendezvousService.findByLinkerRendezvousIdAndAllpublics(rendezvousId, pageAux, size);

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

		pageNumber = (int) Math.floor(((pageNumber / (size + 0.0)) - 0.1) + 1);

		result.addObject("pageNumber", pageNumber);
		result.addObject("page", pageAux);
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestURI", "rendezvous/listLinkedRendezvouses.do");
		result.addObject("canPermit", canPermit);
		result.addObject("canLink", canLink);
		result.addObject("canUnLink", canUnLink);
		result.addObject("rendezvousId", rendezvousId);
		result.addObject("myRendezvousIsDeleted", myRendezvousIsDeleted);
		result.addObject("haveRendezvousId", haveRendezvousId);
		result.addObject("currentMomentVar", currentMomentVar);

		return result;
	}
	//Ancillary methods -----------------------
	protected ModelAndView createEditModelAndView(final Rendezvous rendezvous, final Integer page, final Integer page2, final Integer page3, final Integer page4) {
		ModelAndView result;
		Boolean canPermit;
		Calendar birthDatePlus18Years;
		Collection<Announcement> announcements;
		Boolean canCreateRSVP;
		Boolean canCreateComment;
		Boolean canStreamAnnouncements;
		User user;
		Collection<Comment> comments;
		Actor actor;
		Integer pageNumber;
		Integer size;
		Authority authority;
		Authority authority2;
		Integer pageNumber2;
		Integer pageNumber3;
		Integer pageNumber4;
		Collection<Rendezvous> rendezvousesLinked;
		Collection<Rendezvous> rendezvousesListForLink;
		Date currentMomentVar;

		currentMomentVar = new Date();

		authority = new Authority();
		authority.setAuthority("USER");

		authority2 = new Authority();
		authority2.setAuthority("ADMIN");

		canStreamAnnouncements = false;
		canPermit = false;
		if (LoginService.isAuthenticated())
			if (LoginService.getPrincipal().getAuthorities().contains(authority)) {
				actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());
				birthDatePlus18Years = Calendar.getInstance();
				birthDatePlus18Years.setTime(actor.getBirthdate());
				birthDatePlus18Years.add(Calendar.YEAR, 18);
				if (birthDatePlus18Years.getTime().compareTo(new Date()) <= 0)
					canPermit = true;
			} else if (LoginService.getPrincipal().getAuthorities().contains(authority2))
				canPermit = true;

		canCreateRSVP = false;
		canCreateComment = false;

		if (LoginService.isAuthenticated() && LoginService.getPrincipal().getAuthorities().contains(authority)) {
			user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
			if (this.rsvpService.findByAttendantUserIdAndRendezvousId(user.getId(), rendezvous.getId()) == null) {
				canCreateRSVP = true;
				canStreamAnnouncements = false;
			} else {
				canCreateComment = true;
				if (this.rsvpService.findByAttendantUserIdAndRendezvousId(user.getId(), rendezvous.getId()).getStatus().equals("ACCEPTED"))
					canStreamAnnouncements = true;
			}

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

		announcements = new ArrayList<Announcement>();
		pageNumber2 = 0;

		announcements = this.announcementService.findByRendezvousId(rendezvous.getId(), page2, size);
		if (announcements.size() != 0)
			pageNumber2 = this.announcementService.countByRendezvousId(rendezvous.getId());

		rendezvousesLinked = new ArrayList<Rendezvous>();
		pageNumber3 = 0;

		if (canPermit == true)
			rendezvousesLinked = this.rendezvousService.findByLinkerRendezvousId(rendezvous.getId(), page3, size);
		else
			rendezvousesLinked = this.rendezvousService.findByLinkerRendezvousIdAndAllpublics(rendezvous.getId(), page3, size);

		if (rendezvousesLinked.size() != 0)
			if (canPermit == true)
				pageNumber3 = this.rendezvousService.countByLinkerRendezvousId(rendezvous.getId());
			else
				pageNumber3 = this.rendezvousService.countByLinkerRendezvousIdAndAllpublics(rendezvous.getId());

		rendezvousesListForLink = new ArrayList<Rendezvous>();
		pageNumber4 = 0;
		if (canPermit == false)
			rendezvousesListForLink = this.rendezvousService.findNotLinkedByRendezvousAllPublics(this.rendezvousService.findOne(rendezvous.getId()), page4, size);
		else
			rendezvousesListForLink = this.rendezvousService.findNotLinkedByRendezvous(this.rendezvousService.findOne(rendezvous.getId()), page4, size);

		if (rendezvousesListForLink.size() != 0)
			if (canPermit == true)
				pageNumber4 = this.rendezvousService.countNotLinkedByRendezvous(this.rendezvousService.findOne(rendezvous.getId()));
			else
				pageNumber4 = this.rendezvousService.countNotLinkedByRendezvousAllPublics(this.rendezvousService.findOne(rendezvous.getId()));

		result = new ModelAndView("rendezvous/display");

		pageNumber = (int) Math.floor(((pageNumber / (size + 0.0)) - 0.1) + 1);
		pageNumber2 = (int) Math.floor(((pageNumber2 / (size + 0.0)) - 0.1) + 1);
		pageNumber3 = (int) Math.floor(((pageNumber3 / (size + 0.0)) - 0.1) + 1);
		pageNumber4 = (int) Math.floor(((pageNumber4 / (size + 0.0)) - 0.1) + 1);

		result.addObject("pageNumber", pageNumber);
		result.addObject("page", page);
		result.addObject("pageNumber2", pageNumber2);
		result.addObject("page2", page2);
		result.addObject("pageNumber3", pageNumber3);
		result.addObject("page3", page3);
		result.addObject("pageNumber4", pageNumber4);
		result.addObject("page4", page4);
		result.addObject("rendezvous", rendezvous);
		result.addObject("canPermit", canPermit);
		result.addObject("canCreateRSVP", canCreateRSVP);
		result.addObject("canCreateComment", canCreateComment);
		result.addObject("canStreamAnnouncements", canStreamAnnouncements);
		result.addObject("comments", comments);
		result.addObject("announcements", announcements);
		result.addObject("rendezvousesLinked", rendezvousesLinked);
		result.addObject("rendezvousesListForLink", rendezvousesListForLink);
		result.addObject("currentMomentVar", currentMomentVar);

		return result;

	}
}
