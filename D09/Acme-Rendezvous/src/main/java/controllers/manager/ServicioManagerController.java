
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
import services.ServicioService;
import controllers.AbstractController;
import domain.Category;
import domain.Servicio;

@Controller
@RequestMapping("/servicio/manager")
public class ServicioManagerController extends AbstractController {

	// Services
	@Autowired
	private ServicioService	servicioService;

	@Autowired
	private CategoryService	categoryService;

	@Autowired
	private RequestService	requestService;


	// Constructor
	public ServicioManagerController() {
		super();
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Servicio servicio;

		servicio = this.servicioService.create();

		result = this.createEditModelAndView(servicio);

		return result;

	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false, defaultValue = "1") final Integer page) {
		ModelAndView result;
		Page<Servicio> servicios;

		servicios = this.servicioService.findByManagerUserAccountId(LoginService.getPrincipal().getId(), page, 5);
		Assert.notNull(servicios);

		result = new ModelAndView("servicio/list");
		result.addObject("pageNumber", servicios.getTotalPages());
		result.addObject("page", page);
		result.addObject("servicios", servicios.getContent());
		result.addObject("requestURI", "servicio/manager/list.do");

		return result;
	}

	@RequestMapping(value = "/addCategory", method = RequestMethod.GET)
	public ModelAndView addCategory(@RequestParam final int servicioId, @RequestParam final int categoryId) {
		ModelAndView result;
		Servicio servicio;
		Category category;

		Assert.isTrue(servicioId != 0);
		Assert.isTrue(categoryId != 0);

		servicio = this.servicioService.findOne(servicioId);
		category = this.categoryService.findOne(categoryId);

		this.servicioService.addCategory(servicio, category);

		result = new ModelAndView("redirect:/servicio/display.do?servicioId=" + servicioId);

		return result;

	}

	@RequestMapping(value = "/removeCategory", method = RequestMethod.GET)
	public ModelAndView removeCategory(@RequestParam final int servicioId, @RequestParam final int categoryId) {
		ModelAndView result;
		Servicio servicio;
		Category category;

		Assert.isTrue(servicioId != 0);
		Assert.isTrue(categoryId != 0);

		servicio = this.servicioService.findOne(servicioId);
		category = this.categoryService.findOne(categoryId);

		this.servicioService.removeCategory(servicio, category);

		result = new ModelAndView("redirect:/servicio/display.do?servicioId=" + servicioId);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int servicioId) {
		ModelAndView result;
		Servicio servicio;

		servicio = this.servicioService.findOneToEdit(servicioId);
		Assert.notNull(servicio);

		result = this.createEditModelAndView(servicio);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Servicio servicio, final BindingResult binding) {
		ModelAndView result;
		servicio = this.servicioService.reconstruct(servicio, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(servicio);
		else
			try {
				this.servicioService.save(servicio);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(servicio, "servicio.commit.error");
			}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Servicio servicio, final BindingResult binding) {
		ModelAndView result;
		servicio = this.servicioService.reconstruct(servicio, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(servicio);
		else
			try {
				this.servicioService.delete(servicio);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(servicio, "servicio.commit.error");
			}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Servicio servicio) {
		ModelAndView result;

		result = this.createEditModelAndView(servicio, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Servicio servicio, final String messageCode) {
		ModelAndView result;
		Boolean canDelete;

		canDelete = false;

		if (servicio.getId() > 0)
			result = new ModelAndView("servicio/edit");
		else
			result = new ModelAndView("servicio/create");

		if (servicio.getId() > 0 && this.requestService.countByServicioId(servicio.getId()) == 0)
			canDelete = true;

		result.addObject("canDelete", canDelete);
		result.addObject("servicio", servicio);
		result.addObject("message", messageCode);

		return result;
	}

}
