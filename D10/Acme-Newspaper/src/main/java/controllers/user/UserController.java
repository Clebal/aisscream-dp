
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

import services.ArticleService;
import services.UserService;
import controllers.AbstractController;
import domain.Article;
import domain.User;
import forms.UserForm;

@Controller
@RequestMapping(value = "/actor/user")
public class UserController extends AbstractController {

	// Services
	@Autowired
	private UserService	userService;

	@Autowired
	private ArticleService articleService;
	
	// Constructor
	public UserController() {
		super();
	}

	// Creation
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		UserForm userForm;

		userForm = new UserForm();

		result = this.createEditModelAndView(userForm);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final UserForm userForm, final BindingResult binding) {
		ModelAndView result;
		User user;
		boolean next;

		next = true;
		result = null;
		user = null;
		try {
			user = this.userService.reconstruct(userForm, binding);
		} catch (final Throwable e) {
			
			if (binding.hasErrors()) {
				result = this.createEditModelAndView(userForm);
			} else
				result = this.createEditModelAndView(userForm, "actor.commit.error");

			next = false;
		}

		if (next)
			if (binding.hasErrors()) {
				result = this.createEditModelAndView(userForm);
			}else
				try {
					this.userService.save(user);
					result = new ModelAndView("redirect:/");
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(userForm, "actor.commit.error");
				}

		return result;
	}
	
	// List
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public ModelAndView list(@RequestParam(required=false, defaultValue="0") final int page) {
		ModelAndView result;
		Page<User> pageUser;
		
		pageUser = this.userService.findAllPaginated(page, 5);
		Assert.notNull(pageUser);
		
		result = new ModelAndView("user/list");
		result.addObject("users", pageUser.getContent());
		result.addObject("pageNumber", pageUser.getTotalPages());
		result.addObject("page", page);
		
		return result;
	}
	
	// Display
	@RequestMapping(value="/display", method=RequestMethod.GET)
	public ModelAndView display(@RequestParam final int userId) {
		ModelAndView result;
		User user;
		Page<Article> articles;
		
		user = this.userService.findOneToDisplay(userId);
		Assert.notNull(user);
		
		articles = this.articleService.findByWritterId(userId, 0, 5);
		Assert.notNull(articles);
		
		result = new ModelAndView("user/display");
		result.addObject("user", user);
		result.addObject("articles", articles.getContent());
		
		return result;
	}
	
	// Followers
	@RequestMapping(value="/followers", method=RequestMethod.GET)
	public ModelAndView followers(@RequestParam final int userId, @RequestParam(required=false, defaultValue="0") final int page) {
		ModelAndView result;
		Page<User> pageFollowers;
		
		pageFollowers = this.userService.findFollowersByUserId(userId, page, 5);
		Assert.notNull(pageFollowers);

		result = new ModelAndView("user/list");
		result.addObject("users", pageFollowers.getContent());
		result.addObject("pageNumber", pageFollowers.getTotalPages());
		result.addObject("page", page);
		
		return result;
	}
	
	// Followeds
	@RequestMapping(value="/followeds", method=RequestMethod.GET)
	public ModelAndView followeds(@RequestParam final int userId, @RequestParam(required=false, defaultValue="0") final int page) {
		ModelAndView result;
		Page<User> pageFolloweds;
		
		pageFolloweds = this.userService.findFollowedsByUserId(userId, page, 5);
		Assert.notNull(pageFolloweds);

		result = new ModelAndView("user/list");
		result.addObject("users", pageFolloweds.getContent());
		result.addObject("pageNumber", pageFolloweds.getTotalPages());
		result.addObject("page", page);
		
		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final UserForm userForm) {
		ModelAndView result;

		result = this.createEditModelAndView(userForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final UserForm userForm, final String messageCode) {
		ModelAndView result;
		String requestURI;

		requestURI = "actor/user/edit.do";

		if (userForm.getId() == 0)
			result = new ModelAndView("user/create");
		else
			result = new ModelAndView("user/edit");

		result.addObject("modelo", "user");
		result.addObject("userForm", userForm);
		result.addObject("message", messageCode);
		result.addObject("requestURI", requestURI);

		return result;
	}

}
