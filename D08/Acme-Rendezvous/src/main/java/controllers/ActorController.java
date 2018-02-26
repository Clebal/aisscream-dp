
package controllers;

import java.util.Collection;

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
import services.UserService;
import domain.Actor;
import domain.User;
import forms.UserForm;

@Controller
@RequestMapping("/actor")
public class ActorController extends AbstractController {

	// Services
	@Autowired
	private ActorService	actorService;

	@Autowired
	private UserService		userService;


	// Constructors
	public ActorController() {
		super();
	}

	// Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int actorId) {
		ModelAndView result;
		Actor actor;

		actor = this.actorService.findOne(actorId);
		Assert.notNull(actor);

		result = new ModelAndView("actor/display");
		result.addObject("actor", actor);

		return result;
	}

	// List
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final Integer page) {
		ModelAndView result;
		Collection<User> users;
		Integer pageNumber, size, pageAux;

		size = 5;
		pageNumber = 0;
		
		if (page == null)
			pageAux = 1;
		else
			pageAux = page;
		
		users = this.userService.findAllPaginated(pageAux, size);

		if (users.size() != 0)
			pageNumber = this.userService.countAllPaginated();

		result = new ModelAndView("actor/list");

		pageNumber = (int) Math.floor(((pageNumber / (size + 0.0)) - 0.1) + 1);

		result.addObject("users", users);
		result.addObject("pageNumber", pageNumber);
		result.addObject("page", pageAux);
		result.addObject("requestURI", "actor/list.do");

		return result;
	}

	// List attendans
	@RequestMapping(value = "/listAttendants", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int rendezvousId, @RequestParam(required = false) final Integer page) {
		ModelAndView result;
		Collection<User> users;
		Integer pageNumber, size, pageAux;

		size = 5;
		pageNumber = 0;
		
		if (page == null)
			pageAux = 1;
		else
			pageAux = page;
		
		
		users = this.userService.findAttendantsPaginated(pageAux, size, rendezvousId);

		if (users.size() != 0)
			pageNumber = this.userService.countAttendatsPaginated(rendezvousId);

		result = new ModelAndView("actor/list");

		pageNumber = (int) Math.floor(((pageNumber / size + 0.0) - 0.1) + 1);

		result.addObject("users", users);
		result.addObject("pageNumber", pageNumber);
		result.addObject("page", pageAux);

		return result;
	}

	// Profile

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Actor actor;

		actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(actor);

		result = this.createEditModelAndView(actor);

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Actor actor) {
		ModelAndView result;

		result = this.createEditModelAndView(actor, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Actor actor, final String messageCode) {
		ModelAndView result;
		boolean canEdit;
		String requestURI;
		String tipoActor;
		UserForm userForm;
		Authority authority;

		//Solo puede acceder admin
		authority = new Authority();
		authority.setAuthority("USER");

		//Creamos la URI
		tipoActor = actor.getClass().getSimpleName().toLowerCase();
		requestURI = "actor/" + tipoActor + "/edit.do";

		canEdit = false;
		result = new ModelAndView(tipoActor + "/edit");

		if (actor.getUserAccount().getId() == LoginService.getPrincipal().getId())
			canEdit = true;

		//Añadimos los parámetros
		if (actor.getUserAccount().getAuthorities().contains(authority)) {

			userForm = new UserForm();

			userForm.setAddress(actor.getAddress());
			userForm.setBirthdate(actor.getBirthdate());
			userForm.setEmail(actor.getEmail());
			userForm.setId(actor.getId());
			userForm.setName(actor.getName());
			userForm.setPhone(actor.getPhone());
			userForm.setSurname(actor.getSurname());
			userForm.setUsername(actor.getUserAccount().getUsername());

			result.addObject("userForm", userForm);

		} else
			result.addObject("administrator", actor);

		//Añadimos objetos comunes
		result.addObject("message", messageCode);
		result.addObject("canEdit", canEdit);
		result.addObject("requestURI", requestURI);

		return result;
	}

}
