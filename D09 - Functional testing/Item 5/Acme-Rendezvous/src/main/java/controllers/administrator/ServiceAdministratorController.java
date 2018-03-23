
package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ServiceService;
import controllers.AbstractController;
import domain.Service;

@Controller
@RequestMapping("/service/administrator")
public class ServiceAdministratorController extends AbstractController {

	// Services
	@Autowired
	private ServiceService	serviceService;


	// Constructor
	public ServiceAdministratorController() {
		super();
	}

	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam final int serviceId) {
		ModelAndView result;
		Service service;

		service = this.serviceService.findOne(serviceId);
		Assert.notNull(service);

		this.serviceService.acceptService(service);

		result = new ModelAndView("redirect:/service/list.do");

		return result;
	}

	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView cancel(@RequestParam final int serviceId) {
		ModelAndView result;
		Service service;

		service = this.serviceService.findOne(serviceId);
		Assert.notNull(service);

		this.serviceService.cancelService(service);

		result = new ModelAndView("redirect:/service/list.do");

		return result;
	}

	@RequestMapping(value = "/bestSellingServices", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false, defaultValue = "1") final Integer page) {
		ModelAndView result;
		Page<Service> services;

		services = this.serviceService.bestSellingServices(page, 5);
		Assert.notNull(services);

		result = new ModelAndView("service/list");
		result.addObject("pageNumber", services.getTotalPages());
		result.addObject("page", page);
		result.addObject("services", services.getContent());
		result.addObject("requestURI", "service/administrator/bestSellingServices.do");

		return result;
	}

}
