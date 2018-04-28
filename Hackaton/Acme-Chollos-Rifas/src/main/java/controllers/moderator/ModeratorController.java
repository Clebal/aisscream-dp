
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

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final ModeratorForm actorForm, final BindingResult binding) {
		ModelAndView result;
		Moderator moderator;
		boolean next;

		next = true;
		result = null;
		moderator = null;
		try {
			moderator = this.moderatorService.reconstruct(actorForm, binding);
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
					this.moderatorService.save(moderator);
					result = new ModelAndView("redirect:/");
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(actorForm, "actor.commit.error");
				}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final ModeratorForm actorForm) {
		ModelAndView result;

		result = this.createEditModelAndView(actorForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final ModeratorForm actorForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");

		result.addObject("modelo", "moderator");
		result.addObject("actorForm", actorForm);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "actor/moderator/edit.do");

		return result;
	}

}
