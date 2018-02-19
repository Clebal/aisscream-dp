
package controllers.user;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.RendezvousService;
import controllers.AbstractController;
import domain.Actor;
import domain.Rendezvous;
import domain.TermCondition;
import domain.User;

@Controller
@RequestMapping("/rendezvous/user")
public class RendezvousUserController extends AbstractController {

	// Services
	@Autowired
	private RendezvousService		rendezvousService;

	@Autowired
	private UserService				userService;

	@Autowired
	private TermConditionService	termConditionService;


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

		result = this.createEditModelAndView(creator);

		return result;

	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Rendezvous> rendezvouses;
		User creator;
		Boolean canPermit;
		Boolean canLink;
		Boolean canUnLink;

		canPermit = true;
		canLink = false;
		canUnLink = false;
		creator = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		rendezvouses = this.rendezvousService.findByCreatorId(creator.getId());

		result = new ModelAndView("rendezvous/list");
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestURI", "rendezvous/user/list.do");
		result.addObject("canPermit", canPermit);
		result.addObject("canLink", canLink);
		result.addObject("canUnLink", canUnLink);

		return result;
	}

	@RequestMapping(value = "/listRendezvousesForLink", method = RequestMethod.GET)
	public ModelAndView listLinkedRendezvouses(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Collection<Rendezvous> rendezvouses;
		Boolean canPermit;
		Boolean canLink;
		Boolean canUnLink;
		Calendar birthDatePlus18Years;
		Actor actor;
		Collection<Rendezvous> linkedRendezvouses;

		canLink = false;
		canUnLink = false;

		Assert.isTrue(rendezvousId != 0);

		if (LoginService.isAuthenticated()) {
			actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());
			if (actor.getUserAccount().getId() == this.rendezvousService.findOne(rendezvousId).getCreator().getUserAccount().getId())
				canLink = true;
			birthDatePlus18Years = Calendar.getInstance();
			birthDatePlus18Years.setTime(actor.getBirthdate());
			birthDatePlus18Years.add(Calendar.YEAR, 18);
			if (birthDatePlus18Years.getTime().compareTo(new Date()) <= 0)
				canPermit = true;
			else
				canPermit = false;
		} else
			canPermit = false;
		rendezvouses = this.rendezvousService.findAll();
		linkedRendezvouses = this.rendezvousService.findByLinkerRendezvousId(rendezvousId);
		rendezvouses.removeAll(linkedRendezvouses);

		result = new ModelAndView("rendezvous/list");
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestURI", "rendezvous/user/listRendezvousesForLink.do?rendezvousId=" + rendezvousId);
		result.addObject("canPermit", canPermit);
		result.addObject("canLink", canLink);
		result.addObject("canUnLink", canUnLink);
		result.addObject("rendezvousId", rendezvousId);

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

		rendezvous = this.rendezvousService.findOne(rendezvousId);
		Assert.notNull(rendezvous);

		result = this.createEditModelAndView(rendezvous);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Rendezvous rendezvous, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(rendezvous);
		else
			try {
				this.rendezvousService.save(rendezvous);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(rendezvous, "rendezvous.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@Valid final Rendezvous rendezvous, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(rendezvous);
		else
			try {
				this.rendezvousService.virtualDelete(rendezvous);
				result = new ModelAndView("redirect:list.do");
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
		Collection<TermCondition> termsConditions;

		if (rendezvous.getId() > 0)
			result = new ModelAndView("rendezvous/edit");
		else
			result = new ModelAndView("rendezvous/create");

		termsConditions = this.termConditionService.findAll();

		result.addObject("rendezvous", rendezvous);
		result.addObject("termConditions", termsConditions);
		result.addObject("message", messageCode);

		return result;
	}
}
