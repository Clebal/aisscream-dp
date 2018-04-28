
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

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final SponsorForm actorForm, final BindingResult binding) {
		ModelAndView result;
		Sponsor sponsor;
		boolean next;

		next = true;
		result = null;
		sponsor = null;
		try {
			sponsor = this.sponsorService.reconstruct(actorForm, binding);
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
					this.sponsorService.save(sponsor);
					result = new ModelAndView("redirect:/");
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(actorForm, "actor.commit.error");
				}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final SponsorForm actorForm) {
		ModelAndView result;

		result = this.createEditModelAndView(actorForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final SponsorForm actorForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");

		result.addObject("model", "sponsor");
		result.addObject("actorForm", actorForm);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "actor/sponsor/edit.do");

		return result;
	}

}
