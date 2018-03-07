
package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ServicioService;
import controllers.AbstractController;
import domain.Servicio;

@Controller
@RequestMapping("/servicio/user")
public class ServicioUserController extends AbstractController {

	// Services
	@Autowired
	private ServicioService	servicioService;


	// Constructor
	public ServicioUserController() {
		super();
	}

	@RequestMapping(value = "/listForRequestByRendezvous", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int rendezvousId, @RequestParam(required = false, defaultValue = "1") final Integer page) {
		ModelAndView result;
		Collection<Servicio> servicios;
		Integer pageNumber;

		servicios = this.servicioService.findServicesForRequetsByRendezvousId(rendezvousId, page);
		Assert.notNull(servicios);
		pageNumber = this.servicioService.countFindServicesForRequetsByRendezvousId(rendezvousId);

		pageNumber = (int) Math.floor(((pageNumber / (5 + 0.0)) - 0.1) + 1);

		result = new ModelAndView("servicio/list");
		result.addObject("pageNumber", pageNumber);
		result.addObject("page", page);
		result.addObject("servicios", servicios);
		result.addObject("requestURI", "servicio/user/listForRequestByRendezvous.do");
		result.addObject("rendezvousId", rendezvousId);

		return result;
	}
}
