package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.UserService;
import domain.Actor;
import domain.User;

@Controller
@RequestMapping("/actor")
public class ActorController extends AbstractController {

	// Services
	@Autowired
	private ActorService actorService;

	@Autowired
	private UserService userService;

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
	public ModelAndView list(@RequestParam final int page) {
		ModelAndView result;
		Collection<User> users;
		Actor actor;
		Boolean puedeCrear;
		Integer pageNumber, size;

		size=5;
		pageNumber=0;
		users = this.userService.findAllPaginated(page, size);
		puedeCrear = true;

		if(users.size()!=0){
			pageNumber = this.userService.countAllPaginated();
		}
		
		if (LoginService.isAuthenticated()) {
			actor = this.actorService.findByUserAccountId(LoginService
					.getPrincipal().getId());
			if (actor.getClass().getSimpleName().equals("Administrator")) {
				puedeCrear = false;
			}
		}

		result = new ModelAndView("actor/list");
		
		pageNumber = (int) Math.floor(((pageNumber / size + 0.0) - 0.1) + 1);
		
		result.addObject("users", users);
		result.addObject("puedeCrear", puedeCrear);
		result.addObject("pageNumber", pageNumber);
		result.addObject("page", page);

		return result;
	}
	
	// List attendans
		@RequestMapping(value = "/listAttendants", method = RequestMethod.GET)
		public ModelAndView list(@RequestParam final int rendezvousId, @RequestParam final int page) {
			ModelAndView result;
			Collection<User> users;
			Actor actor;
			Boolean puedeCrear;
			Integer pageNumber, size;

			size=5;
			pageNumber=0;
			users = this.userService.findAttendantsPaginated(page, size, rendezvousId);
			puedeCrear = true;

			if(users.size()!=0){
				pageNumber = this.userService.countAttendatsPaginated(rendezvousId);
			}
			
			if (LoginService.isAuthenticated()) {
				actor = this.actorService.findByUserAccountId(LoginService
						.getPrincipal().getId());
				if (actor.getClass().getSimpleName().equals("Administrator")) {
					puedeCrear = false;
				}
			}

			result = new ModelAndView("actor/list");
			
			pageNumber = (int) Math.floor(((pageNumber / size + 0.0) - 0.1) + 1);
			
			result.addObject("users", users);
			result.addObject("puedeCrear", puedeCrear);
			result.addObject("pageNumber", pageNumber);
			result.addObject("page", page);

			return result;
		}

	// Profile

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Actor actor;

		actor = this.actorService.findByUserAccountId(LoginService
				.getPrincipal().getId());
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

	protected ModelAndView createEditModelAndView(final Actor actor,
			final String messageCode) {
		ModelAndView result;
		boolean canEdit;
		String requestURI;
		String tipoActor;
		String modelo;

		tipoActor = actor.getClass().getSimpleName().toLowerCase();
		modelo = tipoActor;
		requestURI = "actor/" + tipoActor + "/edit.do";

		canEdit = false;

		if (actor.getUserAccount().getId() == LoginService.getPrincipal()
				.getId())
			canEdit = true;
		result = new ModelAndView(tipoActor + "/edit");

		result.addObject("modelo", modelo);
		result.addObject(modelo, actor);
		result.addObject("message", messageCode);
		result.addObject("canEdit", canEdit);
		result.addObject("requestURI", requestURI);

		return result;
	}

}
