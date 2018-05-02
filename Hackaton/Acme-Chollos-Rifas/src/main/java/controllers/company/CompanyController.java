
package controllers.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CompanyService;
import controllers.AbstractController;
import domain.Company;
import forms.ActorForm;
import forms.CompanyForm;

@Controller
@RequestMapping(value = "/actor/company")
public class CompanyController extends AbstractController {

	// Services
	@Autowired
	private CompanyService	companyService;

	// Constructor
	public CompanyController() {
		super();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final CompanyForm actorForm, final BindingResult binding, @RequestParam final String model) {
		ModelAndView result;
		Company company;
		boolean next;

		next = true;
		result = null;
		company = null;
		try {
			company = this.companyService.reconstruct(actorForm, binding);
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
					this.companyService.save(company);
					result = new ModelAndView("redirect:/");
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(actorForm, "actor.commit.error");
				}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final ActorForm actorForm) {
		ModelAndView result;

		result = this.createEditModelAndView(actorForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final ActorForm actorForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");

		result.addObject("model", "company");
		result.addObject("actorForm", actorForm);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "actor/company/edit.do");

		return result;
	}

}
