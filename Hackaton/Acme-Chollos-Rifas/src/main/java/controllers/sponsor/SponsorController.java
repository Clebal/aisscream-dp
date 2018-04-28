
package controllers.sponsor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.SponsorService;
import controllers.AbstractController;
import domain.Sponsor;
import forms.SponsorForm;

@Controller
@RequestMapping(value = "/actor/sponsor")
public class SponsorController extends AbstractController {

	// Services
	@Autowired
	private SponsorService	sponsorService;

	// Constructor
	public SponsorController() {
		super();
	}

}
