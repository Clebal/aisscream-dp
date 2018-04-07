
package controllers.user;

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
import services.NewspaperService;
import services.UserService;
import controllers.AbstractController;
import domain.Newspaper;

@Controller
@RequestMapping("/newspaper/user")
public class NewspaperUserController extends AbstractController {

	// Services
	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private UserService			userService;


	// Constructor
	public NewspaperUserController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false, defaultValue = "1") final Integer page) {
		ModelAndView result;
		Page<Newspaper> newspapers;

		newspapers = this.newspaperService.findByUserId(this.userService.findByUserAccountId(LoginService.getPrincipal().getId()).getId(), page, 5);
		Assert.notNull(newspapers);

		result = new ModelAndView("newspaper/list");
		result.addObject("pageNumber", newspapers.getTotalPages());
		result.addObject("page", page);
		result.addObject("newspapers", newspapers.getContent());
		result.addObject("requestURI", "newspaper/user/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Newspaper newspaper;

		newspaper = this.newspaperService.create();

		result = this.createEditModelAndView(newspaper);

		return result;

	}

	@RequestMapping(value = "/publish", method = RequestMethod.GET)
	public ModelAndView addCategory(@RequestParam final int newspaperId) {
		ModelAndView result;

		Assert.isTrue(newspaperId != 0);

		this.newspaperService.publish(newspaperId);

		result = new ModelAndView("redirect:/newspaper/display.do?newspaperId=" + newspaperId);

		return result;

	}

	@RequestMapping(value = "/putPublic", method = RequestMethod.GET)
	public ModelAndView putPublic(@RequestParam final int newspaperId) {
		ModelAndView result;

		Assert.isTrue(newspaperId != 0);

		this.newspaperService.putPublic(newspaperId);

		result = new ModelAndView("redirect:/newspaper/display.do?newspaperId=" + newspaperId);

		return result;

	}

	@RequestMapping(value = "/putPrivate", method = RequestMethod.GET)
	public ModelAndView putPrivate(@RequestParam final int newspaperId) {
		ModelAndView result;

		Assert.isTrue(newspaperId != 0);

		this.newspaperService.putPrivate(newspaperId);

		result = new ModelAndView("redirect:/newspaper/display.do?newspaperId=" + newspaperId);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Newspaper newspaper, final BindingResult binding) {
		ModelAndView result;
		newspaper = this.newspaperService.reconstruct(newspaper, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(newspaper);
		else
			try {
				this.newspaperService.save(newspaper);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(newspaper, "newspaper.commit.error");
			}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Newspaper newspaper) {
		ModelAndView result;

		result = this.createEditModelAndView(newspaper, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Newspaper newspaper, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("newspaper/create");

		result.addObject("newspaper", newspaper);
		result.addObject("message", messageCode);

		return result;
	}

}
