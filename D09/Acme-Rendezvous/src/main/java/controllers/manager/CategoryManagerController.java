
package controllers.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import services.ServicioService;
import controllers.AbstractController;
import domain.Category;
import domain.Servicio;

@Controller
@RequestMapping("/category/manager")
public class CategoryManagerController extends AbstractController {

	//Services
	@Autowired
	private ServicioService	servicioService;

	@Autowired
	private CategoryService	categoryService;


	// Constructor
	public CategoryManagerController() {
		super();
	}

	@RequestMapping(value = "/addCategory", method = RequestMethod.GET)
	public ModelAndView addCategory(@RequestParam(required = false, defaultValue = "1") final Integer page, @RequestParam final int servicioId) {
		ModelAndView result;
		Page<Category> categories;

		categories = this.categoryService.findByServicioId(servicioId, page, 5);
		Assert.notNull(categories);

		result = new ModelAndView("category/list");
		result.addObject("pageNumber", categories.getTotalPages());
		result.addObject("page", page);
		result.addObject("servicioId", servicioId);
		result.addObject("action", "remove");
		result.addObject("categories", categories.getContent());
		result.addObject("requestURI", "category/manager/addCategory.do");

		return result;
	}

	//TODO Falta paginar
	@RequestMapping(value = "/removeCategory", method = RequestMethod.GET)
	public ModelAndView removeCategory(@RequestParam(required = false, defaultValue = "1") final Integer page, @RequestParam final int servicioId) {
		ModelAndView result;
		Servicio servicio;

		servicio = this.servicioService.findOne(servicioId);
		Assert.notNull(servicio);

		result = new ModelAndView("category/list");
		result.addObject("pageNumber", servicio.getCategories().size());
		result.addObject("page", page);
		result.addObject("servicioId", servicioId);
		result.addObject("action", "add");
		result.addObject("categories", servicio.getCategories());
		result.addObject("requestURI", "category/manager/removeCategory.do");

		return result;
	}

}
