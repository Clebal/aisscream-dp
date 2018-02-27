
package controllers.user;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.RendezvousService;
import services.UserService;
import controllers.AbstractController;
import domain.Actor;
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/rendezvous/user")
public class RendezvousUserController extends AbstractController {

	// Services
	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private UserService			userService;

	@Autowired
	private ActorService		actorService;


	// Constructor
	public RendezvousUserController() {
		super();
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Rendezvous rendezvous;
		User creator;

		creator = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		rendezvous = this.rendezvousService.create(creator);

		result = this.createEditModelAndView(rendezvous);

		return result;

	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final Integer page) {
		ModelAndView result;
		Collection<Rendezvous> rendezvouses;
		User creator;
		Calendar birthDatePlus18Years;
		Boolean canPermit;
		Boolean canLink;
		Boolean canUnLink;
		Integer pageNumber;
		Integer size;
		Boolean haveRendezvousId;
		Integer pageAux;

		if (page == null)
			pageAux = 1;
		else
			pageAux = page;

		haveRendezvousId = false;
		canPermit = false;
		canLink = false;
		canUnLink = false;
		rendezvouses = new ArrayList<Rendezvous>();
		size = 5;
		pageNumber = 0;
		creator = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());

		birthDatePlus18Years = Calendar.getInstance();
		birthDatePlus18Years.setTime(creator.getBirthdate());
		birthDatePlus18Years.add(Calendar.YEAR, 18);
		if (birthDatePlus18Years.getTime().compareTo(new Date()) <= 0)
			canPermit = true;

		if (canPermit == true)
			rendezvouses = this.rendezvousService.findByCreatorId(creator.getId(), pageAux, size);
		else
			rendezvouses = this.rendezvousService.findByCreatorIdAllPublics(creator.getId(), pageAux, size);

		if (rendezvouses.size() != 0)
			if (canPermit == true)
				pageNumber = this.rendezvousService.countByCreatorId(creator.getId());
			else
				pageNumber = this.rendezvousService.countByCreatorIdAllPublics(creator.getId());

		result = new ModelAndView("rendezvous/list");
		pageNumber = (int) Math.floor(((pageNumber / (size + 0.0)) - 0.1) + 1);

		result.addObject("pageNumber", pageNumber);
		result.addObject("page", pageAux);
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestURI", "rendezvous/user/list.do");
		result.addObject("canPermit", canPermit);
		result.addObject("canLink", canLink);
		result.addObject("canUnLink", canUnLink);
		result.addObject("haveRendezvousId", haveRendezvousId);

		return result;
	}
	@RequestMapping(value = "/listByAttendant", method = RequestMethod.GET)
	public ModelAndView listByAttendant(@RequestParam(required = false) final Integer page) {
		ModelAndView result;
		Collection<Rendezvous> rendezvouses;
		User attendant;
		Boolean canPermit;
		Boolean canLink;
		Boolean canUnLink;
		Calendar birthDatePlus18Years;
		Actor actor;
		Integer pageNumber;
		Integer size;
		Boolean haveRendezvousId;
		Integer pageAux;

		if (page == null)
			pageAux = 1;
		else
			pageAux = page;

		haveRendezvousId = false;

		canPermit = false;
		canLink = false;
		canUnLink = false;
		rendezvouses = new ArrayList<Rendezvous>();
		size = 5;
		pageNumber = 0;
		attendant = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());

		if (LoginService.isAuthenticated()) {
			actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());
			birthDatePlus18Years = Calendar.getInstance();
			birthDatePlus18Years.setTime(actor.getBirthdate());
			birthDatePlus18Years.add(Calendar.YEAR, 18);
			if (birthDatePlus18Years.getTime().compareTo(new Date()) <= 0)
				canPermit = true;
		}

		if (canPermit == true)
			rendezvouses = this.rendezvousService.findByAttendantId(attendant.getId(), pageAux, size);
		else
			rendezvouses = this.rendezvousService.findByAttendantIdAllPublics(attendant.getId(), pageAux, size);

		if (rendezvouses.size() != 0)
			if (canPermit == true)
				pageNumber = this.rendezvousService.countByAttendantId(attendant.getId());
			else
				pageNumber = this.rendezvousService.countByAttendantIdAllPublics(attendant.getId());

		result = new ModelAndView("rendezvous/list");
		pageNumber = (int) Math.floor(((pageNumber / (size + 0.0)) - 0.1) + 1);

		result.addObject("pageNumber", pageNumber);
		result.addObject("page", pageAux);
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestURI", "rendezvous/user/listByAttendant.do");
		result.addObject("canPermit", canPermit);
		result.addObject("canLink", canLink);
		result.addObject("canUnLink", canUnLink);
		result.addObject("haveRendezvousId", haveRendezvousId);

		return result;
	}
	@RequestMapping(value = "/listRendezvousesForLink", method = RequestMethod.GET)
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
		Integer pageAux;
		Rendezvous rendezvousAux;

		if (page == null)
			pageAux = 1;
		else
			pageAux = page;

		haveRendezvousId = true;

		canPermit = false;
		canLink = false;
		canUnLink = false;
		rendezvouses = new ArrayList<Rendezvous>();
		size = 5;
		pageNumber = 0;

		Assert.isTrue(rendezvousId != 0);

		rendezvousAux = this.rendezvousService.findOne(rendezvousId);
		Assert.notNull(rendezvousAux);
		Assert.isTrue(rendezvousAux.getCreator().getUserAccount().getId() == LoginService.getPrincipal().getId() && !rendezvousAux.getIsDeleted());

		birthDatePlus18Years = null;
		if (LoginService.isAuthenticated()) {
			actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());
			if (actor.getUserAccount().getId() == this.rendezvousService.findOne(rendezvousId).getCreator().getUserAccount().getId())
				canLink = true;
			birthDatePlus18Years = Calendar.getInstance();
			birthDatePlus18Years.setTime(actor.getBirthdate());
			birthDatePlus18Years.add(Calendar.YEAR, 18);
			if (birthDatePlus18Years.getTime().compareTo(new Date()) <= 0)
				canPermit = true;
		}

		if (canPermit == false)
			rendezvouses = this.rendezvousService.findNotLinkedByRendezvousAllPublics(this.rendezvousService.findOne(rendezvousId), pageAux, size);
		else
			rendezvouses = this.rendezvousService.findNotLinkedByRendezvous(this.rendezvousService.findOne(rendezvousId), pageAux, size);

		if (this.rendezvousService.findOne(rendezvousId).getIsDeleted() == false)
			myRendezvousIsDeleted = false;
		else
			myRendezvousIsDeleted = true;

		if (rendezvouses.size() != 0)
			if (canPermit == true)
				pageNumber = this.rendezvousService.countNotLinkedByRendezvous(this.rendezvousService.findOne(rendezvousId));
			else
				pageNumber = this.rendezvousService.countNotLinkedByRendezvousAllPublics(this.rendezvousService.findOne(rendezvousId));

		result = new ModelAndView("rendezvous/list");
		pageNumber = (int) Math.floor(((pageNumber / (size + 0.0)) - 0.1) + 1);

		result.addObject("pageNumber", pageNumber);
		result.addObject("page", pageAux);
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestURI", "rendezvous/user/listRendezvousesForLink.do");
		result.addObject("canPermit", canPermit);
		result.addObject("canLink", canLink);
		result.addObject("canUnLink", canUnLink);
		result.addObject("rendezvousId", rendezvousId);
		result.addObject("myRendezvousIsDeleted", myRendezvousIsDeleted);
		result.addObject("haveRendezvousId", haveRendezvousId);

		return result;
	}
	@RequestMapping(value = "/linkRendezvous", method = RequestMethod.GET)
	public ModelAndView linkRendezvous(@RequestParam final int myRendezvousId, @RequestParam final int linkedRendezvousId) {
		ModelAndView result;
		Rendezvous myRendezvous;
		Rendezvous linkedRendezvous;

		Assert.isTrue(myRendezvousId != 0);
		Assert.isTrue(linkedRendezvousId != 0);

		myRendezvous = this.rendezvousService.findOne(myRendezvousId);
		linkedRendezvous = this.rendezvousService.findOne(linkedRendezvousId);

		this.rendezvousService.addLink(myRendezvous, linkedRendezvous);

		result = new ModelAndView("redirect:/rendezvous/display.do?rendezvousId=" + myRendezvousId);

		return result;

	}

	@RequestMapping(value = "/unLinkRendezvous", method = RequestMethod.GET)
	public ModelAndView unListRegSur(@RequestParam final int myRendezvousId, @RequestParam final int linkedRendezvousId) {
		ModelAndView result;
		Rendezvous myRendezvous;
		Rendezvous linkedRendezvous;

		Assert.isTrue(myRendezvousId != 0);
		Assert.isTrue(linkedRendezvousId != 0);

		myRendezvous = this.rendezvousService.findOne(myRendezvousId);
		linkedRendezvous = this.rendezvousService.findOne(linkedRendezvousId);

		this.rendezvousService.removeLink(myRendezvous, linkedRendezvous);

		result = new ModelAndView("redirect:/rendezvous/display.do?rendezvousId=" + myRendezvousId);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Rendezvous rendezvous;

		rendezvous = this.rendezvousService.findOneToEdit(rendezvousId);
		Assert.notNull(rendezvous);

		result = this.createEditModelAndView(rendezvous);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Rendezvous rendezvous, final BindingResult binding) {
		ModelAndView result;
		rendezvous = this.rendezvousService.reconstruct(rendezvous, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(rendezvous);
		else
			try {
				this.rendezvousService.save(rendezvous);
				result = new ModelAndView("redirect:list.do?");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(rendezvous, "rendezvous.commit.error");
			}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Rendezvous rendezvous, final BindingResult binding) {
		ModelAndView result;
		rendezvous = this.rendezvousService.reconstruct(rendezvous, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(rendezvous);
		else
			try {
				this.rendezvousService.virtualDelete(rendezvous);
				result = new ModelAndView("redirect:list.do?");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(rendezvous, "rendezvous.commit.error");
			}

		return result;
	}
	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Rendezvous rendezvous) {
		ModelAndView result;

		result = this.createEditModelAndView(rendezvous, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Rendezvous rendezvous, final String messageCode) {
		ModelAndView result;
		Actor actor;
		Calendar birthDatePlus18Years;
		Boolean canPermit;

		canPermit = false;
		actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());
		birthDatePlus18Years = Calendar.getInstance();
		birthDatePlus18Years.setTime(actor.getBirthdate());
		birthDatePlus18Years.add(Calendar.YEAR, 18);
		if (birthDatePlus18Years.getTime().compareTo(new Date()) <= 0)
			canPermit = true;

		if (rendezvous.getId() > 0)
			result = new ModelAndView("rendezvous/edit");
		else
			result = new ModelAndView("rendezvous/create");

		result.addObject("canPermit", canPermit);
		result.addObject("rendezvous", rendezvous);
		result.addObject("message", messageCode);

		return result;
	}
}
