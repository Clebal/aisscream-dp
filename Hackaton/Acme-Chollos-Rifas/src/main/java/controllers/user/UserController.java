
package controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.UserService;
import controllers.AbstractController;
import domain.User;
import forms.UserForm;

@Controller
@RequestMapping(value = "/actor/user")
public class UserController extends AbstractController {

	// Services
	@Autowired
	private UserService	userService;

	// Constructor
	public UserController() {
		super();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final UserForm actorForm, final BindingResult binding) {
		ModelAndView result;
		User user;
		boolean next;

		next = true;
		result = null;
		user = null;
		try {
			user = this.userService.reconstruct(actorForm, binding);
		} catch (final Throwable e) {

			if (binding.hasErrors())
				result = this.createEditModelAndView(actorForm);
			else
				result = this.createEditModelAndView(actorForm, "actor.commit.error");

			next = false;
		}

		if (next)
			if (binding.hasErrors())
				result = this.createEditModelAndView(actorForm);
			else
				try {
					this.userService.save(user);
					result = new ModelAndView("redirect:/");
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(actorForm, "actor.commit.error");
				}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final UserForm actorForm) {
		ModelAndView result;

		result = this.createEditModelAndView(actorForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final UserForm actorForm, final String messageCode) {
		ModelAndView result;
		String requestURI;

		requestURI = "actor/user/edit.do";

		result = new ModelAndView("actor/edit");

		result.addObject("model", "user");
		result.addObject("actorForm", actorForm);
		result.addObject("message", messageCode);
		result.addObject("requestURI", requestURI);

		return result;
	}

}
