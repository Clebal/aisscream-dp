
package controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.UserService;
import controllers.AbstractController;
import domain.User;
import forms.ActorForm;

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

}
