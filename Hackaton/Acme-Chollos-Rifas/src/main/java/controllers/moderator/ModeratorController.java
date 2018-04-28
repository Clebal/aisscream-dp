
package controllers.moderator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ModeratorService;
import controllers.AbstractController;
import domain.Moderator;
import forms.ModeratorForm;

@Controller
@RequestMapping(value = "/actor/moderator")
public class ModeratorController extends AbstractController {

	// Services
	@Autowired
	private ModeratorService	moderatorService;

	// Constructor
	public ModeratorController() {
		super();
	}

}
