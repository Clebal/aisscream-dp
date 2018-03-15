
package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/service/user")
public class ServiceUserController extends AbstractController {

	// Services
	@Autowired
	private ServiceService	serviceService;


	// Constructor
	public ServiceUserController() {
		super();
	}

	@RequestMapping(value = "/listForRequestByRendezvous", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int rendezvousId, @RequestParam(required = false, defaultValue = "1") final Integer page) {
		ModelAndView result;
		Collection<Service> services;
		Integer pageNumber;

		services = this.serviceService.findServicesForRequetsByRendezvousId(rendezvousId, page);
		Assert.notNull(services);
		pageNumber = this.serviceService.countFindServicesForRequetsByRendezvousId(rendezvousId);

		pageNumber = (int) Math.floor(((pageNumber / (5 + 0.0)) - 0.1) + 1);

		result = new ModelAndView("service/list");
		result.addObject("pageNumber", pageNumber);
		result.addObject("page", page);
		result.addObject("services", services);
		result.addObject("requestURI", "service/user/listForRequestByRendezvous.do");
		result.addObject("rendezvousId", rendezvousId);

		return result;
	}
}
