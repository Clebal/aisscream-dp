
package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("/servicio/administrator")
public class ServicioAdministratorController extends AbstractController {

	// Services
	@Autowired
	private ServicioService	servicioService;


	// Constructor
	public ServicioAdministratorController() {
		super();
	}

	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam final int servicioId) {
		ModelAndView result;
		Servicio servicio;

		servicio = this.servicioService.findOne(servicioId);
		Assert.notNull(servicio);

		this.servicioService.acceptServicio(servicio);

		result = new ModelAndView("redirect:/servicio/list.do");

		return result;
	}

	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView cancel(@RequestParam final int servicioId) {
		ModelAndView result;
		Servicio servicio;

		servicio = this.servicioService.findOne(servicioId);
		Assert.notNull(servicio);

		this.servicioService.cancelServicio(servicio);

		result = new ModelAndView("redirect:/servicio/list.do");

		return result;
	}

	@RequestMapping(value = "/bestSellingServices", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false, defaultValue = "1") final Integer page) {
		ModelAndView result;
		Page<Servicio> servicios;

		servicios = this.servicioService.bestSellingServices(page, 5);
		Assert.notNull(servicios);

		result = new ModelAndView("servicio/list");
		result.addObject("pageNumber", servicios.getTotalPages());
		result.addObject("page", page);
		result.addObject("servicios", servicios.getContent());
		result.addObject("requestURI", "servicio/administrator/bestSellingServices.do");

		return result;
	}

}
