
package controllers.user;

import javax.validation.Valid;

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

@Controller
@RequestMapping(value = "/actor/user")
public class UserController extends AbstractController {

	// Services
	@Autowired
	private UserService			userService;


	// Constructor
	public UserController() {
		super();
	}

	// Creation
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		User user;

		user = this.userService.create();

		result = this.createEditModelAndView(user);

		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final User user, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(user);
		} else
			try {
				this.userService.save(user);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(user, "actor.commit.error");
			}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final User user) {
		ModelAndView result;

		result = this.createEditModelAndView(user, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final User user, final String messageCode) {
		ModelAndView result;
		boolean canEdit;
		UserAccount userAccount;
		int userAccountId;
		String requestURI;

		requestURI = "actor/user/edit.do";

		userAccountId = 0;
		canEdit = false;
		if (user.getId() == 0)
			canEdit = true;
		else {

			if (LoginService.isAuthenticated() == true) {
				userAccount = LoginService.getPrincipal();
				userAccountId = userAccount.getId();
			}
			if (user.getUserAccount().getId() == userAccountId)
				canEdit = true;
		}

		if (user.getId() > 0)
			result = new ModelAndView("user/edit");
		else
			result = new ModelAndView("user/create");

		result.addObject("modelo", "user");
		result.addObject("user", user);
		result.addObject("message", messageCode);
		result.addObject("canEdit", canEdit);
		result.addObject("requestURI", requestURI);

		return result;
	}

}
