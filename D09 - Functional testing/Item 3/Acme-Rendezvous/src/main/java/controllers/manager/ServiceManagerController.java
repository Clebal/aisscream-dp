
package controllers.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.CategoryService;
import services.RequestService;
import services.ServiceService;
import controllers.AbstractController;
import domain.Category;
import domain.Service;

@Controller
@RequestMapping("/service/manager")
public class ServiceManagerController extends AbstractController {

	// Services
	@Autowired
	private ServiceService	serviceService;

	@Autowired
	private CategoryService	categoryService;

	@Autowired
	private RequestService	requestService;


	// Constructor
	public ServiceManagerController() {
		super();
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int categoryId) {
		ModelAndView result;
		Service service;

		Assert.isTrue(categoryId != 0);

		service = this.serviceService.create(categoryId);

		result = this.createEditModelAndView(service);

		return result;

	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false, defaultValue = "1") final Integer page) {
		ModelAndView result;
		Page<Service> services;

		services = this.serviceService.findByManagerUserAccountId(LoginService.getPrincipal().getId(), page, 5);
		Assert.notNull(services);

		result = new ModelAndView("service/list");
		result.addObject("pageNumber", services.getTotalPages());
		result.addObject("page", page);
		result.addObject("services", services.getContent());
		result.addObject("requestURI", "service/manager/list.do");

		return result;
	}

	@RequestMapping(value = "/addCategory", method = RequestMethod.GET)
	public ModelAndView addCategory(@RequestParam final int serviceId, @RequestParam final int categoryId) {
		ModelAndView result;
		Service service;
		Category category;

		Assert.isTrue(serviceId != 0);
		Assert.isTrue(categoryId != 0);

		service = this.serviceService.findOne(serviceId);
		category = this.categoryService.findOne(categoryId);

		this.serviceService.addCategory(service, category);

		result = new ModelAndView("redirect:/service/display.do?serviceId=" + serviceId);

		return result;

	}

	@RequestMapping(value = "/removeCategory", method = RequestMethod.GET)
	public ModelAndView removeCategory(@RequestParam final int serviceId, @RequestParam final int categoryId) {
		ModelAndView result;
		Service service;
		Category category;

		Assert.isTrue(serviceId != 0);
		Assert.isTrue(categoryId != 0);

		service = this.serviceService.findOne(serviceId);
		category = this.categoryService.findOne(categoryId);

		this.serviceService.removeCategory(service, category);

		result = new ModelAndView("redirect:/service/display.do?serviceId=" + serviceId);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int serviceId) {
		ModelAndView result;
		Service service;

		service = this.serviceService.findOneToEdit(serviceId);
		Assert.notNull(service);

		result = this.createEditModelAndView(service);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Service service, final BindingResult binding) {
		ModelAndView result;
		service = this.serviceService.reconstruct(service, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(service);
		else
			try {
				this.serviceService.save(service);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(service, "service.commit.error");
			}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Service service, final BindingResult binding) {
		ModelAndView result;

		try {
			this.serviceService.delete(service);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(service, "service.commit.error");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Service service) {
		ModelAndView result;

		result = this.createEditModelAndView(service, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Service service, final String messageCode) {
		ModelAndView result;
		Boolean canDelete;

		canDelete = false;

		if (service.getId() > 0)
			result = new ModelAndView("service/edit");
		else
			result = new ModelAndView("service/create");

		if (service.getId() > 0 && this.requestService.countByServiceId(service.getId()) == 0)
			canDelete = true;

		result.addObject("canDelete", canDelete);
		result.addObject("service", service);
		result.addObject("message", messageCode);

		return result;
	}

}
