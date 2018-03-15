
package controllers.manager;

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

import services.CategoryService;
import services.ServiceService;
import controllers.AbstractController;
import domain.Category;
import domain.Service;

@Controller
@RequestMapping("/category/manager")
public class CategoryManagerController extends AbstractController {

	//Services
	@Autowired
	private ServiceService	serviceService;

	@Autowired
	private CategoryService	categoryService;


	// Constructor
	public CategoryManagerController() {
		super();
	}

	@RequestMapping(value = "/addCategory", method = RequestMethod.GET)
	public ModelAndView addCategory(@RequestParam(required = false, defaultValue = "1") final Integer page, @RequestParam final int serviceId) {
		ModelAndView result;
		Page<Category> categories;

		categories = this.categoryService.findByServiceId(serviceId, page, 5);
		Assert.notNull(categories);

		result = new ModelAndView("category/list");
		result.addObject("pageNumber", categories.getTotalPages());
		result.addObject("page", page);
		result.addObject("serviceId", serviceId);
		result.addObject("action", "add");
		result.addObject("categories", categories.getContent());
		result.addObject("requestURI", "category/manager/addCategory.do");

		return result;
	}

	//TODO Falta paginar
	@RequestMapping(value = "/removeCategory", method = RequestMethod.GET)
	public ModelAndView removeCategory(@RequestParam(required = false, defaultValue = "1") final Integer page, @RequestParam final int serviceId) {
		ModelAndView result;
		Service service;
		List<Category> categories;
		Integer fromId, toId, pageNumber;

		service = this.serviceService.findOne(serviceId);
		Assert.notNull(service);
		categories = new ArrayList<Category>(service.getCategories());
		fromId = this.fromIdAndToId(categories.size(), page)[0];
		toId = this.fromIdAndToId(categories.size(), page)[1];

		pageNumber = categories.size();

		pageNumber = (int) Math.floor(((pageNumber / (5 + 0.0)) - 0.1) + 1);

		result = new ModelAndView("category/list");
		result.addObject("pageNumber", pageNumber);
		result.addObject("page", page);
		result.addObject("serviceId", serviceId);
		result.addObject("action", "remove");
		result.addObject("categories", categories.subList(fromId, toId));
		result.addObject("requestURI", "category/manager/removeCategory.do");

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
