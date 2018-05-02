package controllers.user;

import java.util.LinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import domain.User;
import domain.Level;
import forms.ActorForm;
import forms.UserForm;

import services.LevelService;
import services.UserService;

@Controller
@RequestMapping(value="/actor/user")
public class UserController extends AbstractController {

	// Supporting services
	@Autowired
	private UserService userService;
	
	@Autowired
	private LevelService levelService;
	
	// Constructor
	public UserController() {
		super();
	}
	
	// List
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public ModelAndView list(@RequestParam(required=false, defaultValue="1") final int page) {
		ModelAndView result;
		Page<User> userPage;
		LinkedHashMap<User, Level> userLevel;
		Level level;
		
		userPage = this.userService.findOrderByPoints(page, 5);
		Assert.notNull(userPage);
		
		userLevel = new LinkedHashMap<User, Level>();
		for(User u: userPage.getContent()) {
			level = this.levelService.findByPoints(u.getPoints());
			Assert.notNull(level);
			userLevel.put(u, level);
		}
		
		result = new ModelAndView("user/list");
		result.addObject("page", page);
		result.addObject("pageNumber", userPage.getTotalPages());
		result.addObject("users", userLevel);
		return result;
	}
	
	// Display
	@RequestMapping(value="/display", method=RequestMethod.GET)
	public ModelAndView display(@RequestParam final int userId) {
		ModelAndView result;
		User user;
		Level level;
		
		user = this.userService.findOne(userId);
		Assert.notNull(user);
		
		level = this.levelService.findByPoints(user.getPoints());
		Assert.notNull(level);
		
		result = new ModelAndView("actor/display");
		result.addObject("actor", user);
		result.addObject("model", "user");
		result.addObject("level", level);
		result.addObject("isPublic", true);
		
		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final UserForm actorForm, final BindingResult binding, @RequestParam final String model) {
		ModelAndView result;
		User user;
		boolean next;

		next = true;
		result = null;
		user = null;
		try {
			user = this.userService.reconstruct(actorForm, binding);
		} catch (final Throwable e) {

			if (binding.hasErrors())
				result = this.createEditModelAndView(actorForm);
			else
				result = this.createEditModelAndView(actorForm, "actor.commit.error");

			next = false;
		}

		if (next)
			if (binding.hasErrors())
				result = this.createEditModelAndView(actorForm);
			else
				try {
					this.userService.save(user);
					result = new ModelAndView("redirect:/");
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(actorForm, "actor.commit.error");
				}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final ActorForm actorForm) {
		ModelAndView result;

		result = this.createEditModelAndView(actorForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final ActorForm actorForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");

		result.addObject("model", "user");
		result.addObject("actorForm", actorForm);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "actor/user/edit.do");

		return result;
	}
	
}
