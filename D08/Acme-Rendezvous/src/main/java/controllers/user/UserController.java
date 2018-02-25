package controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import security.UserAccount;
import services.UserService;
import controllers.AbstractController;
import domain.User;
import forms.UserForm;

@Controller
@RequestMapping(value = "/actor/user")
public class UserController extends AbstractController {

	// Services
	@Autowired
	private UserService userService;

	// Constructor
	public UserController() {
		super();
	}

	// Creation
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		UserForm userForm;
		User user;

		userForm = new UserForm();

		user = this.userService.create();

		userForm.setUserId(user.getId());

		result = this.createEditModelAndView(userForm);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(forms.UserForm userForm,
			final BindingResult binding) {
		ModelAndView result;
		User user;
		boolean next;

		next = true;
		user = new User();
		result = null;
		
		try {
			user = this.userService.reconstruct(userForm, binding);
		} catch (final Throwable e) {
			result = this
					.createEditModelAndView(userForm, "actor.commit.error");
			next = false;
		}

		if (next) {
			if (binding.hasErrors()) {
				result = this.createEditModelAndView(userForm);
			} else
				try {
					this.userService.save(user);
					result = new ModelAndView("redirect:/");
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(userForm,
							"actor.commit.error");
				}
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final UserForm userForm) {
		ModelAndView result;

		result = this.createEditModelAndView(userForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final UserForm userForm,
			final String messageCode) {
		ModelAndView result;
		boolean canEdit;
		UserAccount userAccount;
		int userAccountId;
		String requestURI;

		requestURI = "actor/user/edit.do";

		userAccountId = 0;
		canEdit = false;
		if (userForm.getUserId() == null || userForm.getUserId() == 0)
			canEdit = true;
		else {

			if (LoginService.isAuthenticated() == true) {
				userAccount = LoginService.getPrincipal();
				userAccountId = userAccount.getId();
			}
			if (userForm.getUserId() == userAccountId)
				canEdit = true;
		}

		if (userForm.getUserId() == null || userForm.getUserId() == 0)
			result = new ModelAndView("user/create");
		else
			result = new ModelAndView("user/edit");

		result.addObject("modelo", "user");
		result.addObject("user", userForm);
		result.addObject("message", messageCode);
		result.addObject("canEdit", canEdit);
		result.addObject("requestURI", requestURI);

		return result;
	}

}
