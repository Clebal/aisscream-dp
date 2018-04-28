
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.LoginService;
import services.ActorService;
import services.ModeratorService;
import services.UserService;
import domain.Actor;
import domain.Moderator;
import domain.User;
import forms.CompanyForm;
import forms.ModeratorForm;
import forms.SponsorForm;
import forms.UserForm;

@Controller
@RequestMapping("/actor/{actor}")
public class ActorController extends AbstractController {

	// Services
	@Autowired
	private ActorService	actorService;

	@Autowired
	private UserService		userService;

	@Autowired
	private ModeratorService	moderatorService;


	// Constructors
	public ActorController() {
		super();
	}

	// Profile
//	@RequestMapping(value = "/profile", method = RequestMethod.GET)
//	public ModelAndView edit() {
//		ModelAndView result;
//		Actor actor;
//
//		actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());
//		Assert.notNull(actor);
//
//		result = this.createEditModelAndView(actor);
//
//		return result;
//	}
	
	// Creation
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@PathVariable(value="actor") final String actor) {
		ModelAndView result;
		
		result = new ModelAndView("actor/edit");
		
		result.addObject("requestURI", "actor/"+actor+"/edit.do");
		
		if(actor.equals("user")) {
			UserForm actorForm;
			actorForm = new UserForm();
			result.addObject("actorForm", actorForm);
		}
		
		if(actor.equals("sponsor")) {
			SponsorForm actorForm;
			actorForm = new SponsorForm();
			result.addObject("actorForm", actorForm);
		}
		
		if(actor.equals("company")) {
			CompanyForm actorForm;
			actorForm = new CompanyForm();
			result.addObject("actorForm", actorForm);
		}
		
		if(actor.equals("moderator")) {
			ModeratorForm actorForm;
			actorForm = new ModeratorForm();
			result.addObject("actorForm", actorForm);
		}
		
		result.addObject("model", actor);

		return result;
	}

//	// Ancillary methods
//	protected ModelAndView createEditModelAndView(final Actor actor) {
//		ModelAndView result;
//
//		result = this.createEditModelAndView(actor, null);
//
//		return result;
//	}
//
//	protected ModelAndView createEditModelAndView(final Actor actor, final String messageCode) {
//		ModelAndView result;
//		boolean canEdit;
//		String requestURI;
//		String tipoActor;
//		UserForm userForm;
//		ModeratorForm moderatorForm;
//		Moderator moderator;
//		Authority authorityUser, authorityModerator;
//
//		//Solo puede acceder admin
//		authorityUser = new Authority();
//		authorityUser.setAuthority("USER");
//		authorityModerator = new Authority();
//		authorityModerator.setAuthority("MANAGER");
//
//		//Creamos la URI
//		tipoActor = actor.getClass().getSimpleName().toLowerCase();
//		requestURI = "actor/" + tipoActor + "/edit.do";
//
//		canEdit = false;
//		result = new ModelAndView(tipoActor + "/edit");
//
//		if (actor.getUserAccount().getId() == LoginService.getPrincipal().getId())
//			canEdit = true;
//
//		//Añadimos los parámetros
//		if (actor.getUserAccount().getAuthorities().contains(authorityUser)) {
//
//			userForm = new UserForm();
//
//			userForm.setAddress(actor.getAddress());
//			userForm.setBirthdate(actor.getBirthdate());
//			userForm.setEmail(actor.getEmail());
//			userForm.setId(actor.getId());
//			userForm.setName(actor.getName());
//			userForm.setPhone(actor.getPhone());
//			userForm.setSurname(actor.getSurname());
//			userForm.setUsername(actor.getUserAccount().getUsername());
//
//			result.addObject("userForm", userForm);
//
//		} else if (actor.getUserAccount().getAuthorities().contains(authorityModerator)) {
//
//			moderatorForm = new ModeratorForm();
//
//			moderatorForm.setAddress(actor.getAddress());
//			moderatorForm.setBirthdate(actor.getBirthdate());
//			moderatorForm.setEmail(actor.getEmail());
//			moderatorForm.setId(actor.getId());
//			moderatorForm.setName(actor.getName());
//			moderatorForm.setPhone(actor.getPhone());
//			moderatorForm.setSurname(actor.getSurname());
//			moderatorForm.setUsername(actor.getUserAccount().getUsername());
//
//			if (LoginService.isAuthenticated()) {
//				moderator = this.moderatorService.findByUserAccountId(LoginService.getPrincipal().getId());
//				Assert.notNull(moderator);
////				moderatorForm.setVat(moderator.getVat());
//			}
//
//			result.addObject("moderatorForm", moderatorForm);
//
//		} else
//			result.addObject("administrator", actor);
//
//		//Añadimos objetos comunes
//		result.addObject("message", messageCode);
//		result.addObject("canEdit", canEdit);
//		result.addObject("requestURI", requestURI);
//
//		return result;
//	}

}
