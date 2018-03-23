
package controllers.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ManagerService;
import controllers.AbstractController;
import domain.Manager;
import forms.ManagerForm;

@Controller
@RequestMapping(value = "/actor/manager")
public class ManagerController extends AbstractController {

	// Services
	@Autowired
	private ManagerService	managerService;


	// Constructor
	public ManagerController() {
		super();
	}

	// Creation
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		ManagerForm managerForm;

		managerForm = new ManagerForm();

		result = this.createEditModelAndView(managerForm);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final ManagerForm managerForm, final BindingResult binding) {
		ModelAndView result;
		Manager manager;
		boolean next;

		next = true;
		result = null;
		manager = null;
		try {
			manager = this.managerService.reconstruct(managerForm, binding);
		} catch (final Throwable e) {

			if (binding.hasErrors())
				result = this.createEditModelAndView(managerForm);
			else
				result = this.createEditModelAndView(managerForm, "actor.commit.error");

			next = false;
		}

		if (next)
			if (binding.hasErrors())
				result = this.createEditModelAndView(managerForm);
			else
				try {
					this.managerService.save(manager);
					result = new ModelAndView("redirect:/");
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(managerForm, "actor.commit.error");
				}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final ManagerForm managerForm) {
		ModelAndView result;

		result = this.createEditModelAndView(managerForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final ManagerForm managerForm, final String messageCode) {
		ModelAndView result;
		String requestURI;

		requestURI = "actor/manager/edit.do";

		if (managerForm.getId() == 0)
			result = new ModelAndView("manager/create");
		else
			result = new ModelAndView("manager/edit");

		result.addObject("modelo", "manager");
		result.addObject("managerForm", managerForm);
		result.addObject("message", messageCode);
		result.addObject("requestURI", requestURI);

		return result;
	}

}
