
package controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ServicioService;
import domain.Category;
import domain.Servicio;

@Controller
@RequestMapping("/servicio")
public class ServicioController extends AbstractController {

	// Services
	@Autowired
	private ServicioService	servicioService;


	// Constructor
	public ServicioController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int servicioId, @RequestParam(required = false, defaultValue = "1") final Integer page) {
		ModelAndView result;
		Servicio servicio;

		servicio = this.servicioService.findOne(servicioId);

		Assert.notNull(servicio);
		result = this.createDisplayModelAndView(servicio, page);

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false, defaultValue = "1") final Integer page) {
		ModelAndView result;
		Page<Servicio> servicios;

		servicios = this.servicioService.findAllPaginated(page, 5);
		Assert.notNull(servicios);

		result = new ModelAndView("servicio/list");
		result.addObject("pageNumber", servicios.getTotalPages());
		result.addObject("page", page);
		result.addObject("servicios", servicios.getContent());
		result.addObject("requestURI", "servicio/list.do");

		return result;
	}

	@RequestMapping(value = "/listByCategoryId", method = RequestMethod.GET)
	public ModelAndView listByCategoryId(@RequestParam final int categoryId, @RequestParam(required = false, defaultValue = "1") final Integer page) {
		ModelAndView result;
		Page<Servicio> servicios;

		servicios = this.servicioService.findByCategoryId(categoryId, page, 5);
		Assert.notNull(servicios);

		result = new ModelAndView("servicio/list");
		result.addObject("pageNumber", servicios.getTotalPages());
		result.addObject("page", page);
		result.addObject("servicios", servicios.getContent());
		result.addObject("requestURI", "servicio/listByCategoryId.do");
		result.addObject("categoryId", categoryId);

		return result;
	}

	@RequestMapping(value = "/listByRendezvousId", method = RequestMethod.GET)
	public ModelAndView listByRendezvousId(@RequestParam final int rendezvousId, @RequestParam(required = false, defaultValue = "1") final Integer page) {
		ModelAndView result;
		Page<Servicio> servicios;

		servicios = this.servicioService.findByRendezvousId(rendezvousId, page, 5);
		Assert.notNull(servicios);

		result = new ModelAndView("servicio/list");
		result.addObject("pageNumber", servicios.getTotalPages());
		result.addObject("page", page);
		result.addObject("servicios", servicios.getContent());
		result.addObject("requestURI", "servicio/listByRendezvousId.do");
		result.addObject("rendezvousId", rendezvousId);

		return result;
	}
	//Ancillary methods -----------------------
	protected ModelAndView createDisplayModelAndView(final Servicio servicio, final int page) {
		ModelAndView result;
		List<Category> categories;
		Integer pageNumber, fromId, toId;

		categories = new ArrayList<Category>(servicio.getCategories());
		fromId = this.fromIdAndToId(categories.size(), page)[0];
		toId = this.fromIdAndToId(categories.size(), page)[1];

		pageNumber = categories.size();

		result = new ModelAndView("servicio/display");

		pageNumber = (int) Math.floor(((pageNumber / (5 + 0.0)) - 0.1) + 1);

		result.addObject("pageNumber", pageNumber);
		result.addObject("page", page);
		result.addObject("servicio", servicio);
		result.addObject("categories", categories.subList(fromId, toId));

		return result;

	}

	private Integer[] fromIdAndToId(final Integer tamañoAux, final Integer page) {
		Integer tamaño, pageAux, fromId, toId;
		tamaño = tamañoAux;
		Integer[] result;

		result = new Integer[2];

		pageAux = page;
		if (page <= 0)
			pageAux = 1;

		fromId = (pageAux - 1) * 5;
		if (fromId > tamaño)
			fromId = 0;
		toId = (pageAux * 5);
		if (tamaño > 5) {
			if (toId > tamaño && fromId == 0)
				toId = 5;
			else if (toId > tamaño && fromId != 0)
				toId = tamaño;
		} else
			toId = tamaño;

		result[0] = fromId;
		result[1] = toId;

		return result;
	}
}
