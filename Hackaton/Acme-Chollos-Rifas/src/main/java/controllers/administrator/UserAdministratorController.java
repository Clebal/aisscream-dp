package controllers.administrator;

import java.util.LinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import domain.User;
import domain.Level;
import services.LevelService;
import services.UserService;

@Controller
@RequestMapping(value="/user/administrator")
public class UserAdministratorController extends AbstractController {

	// Supporting services
	@Autowired
	private UserService userService;
	
	@Autowired
	private LevelService levelService;
	
	// Constructor
	public UserAdministratorController() {
		super();
	}
	
	// List
	@RequestMapping(value="/topFiveUsersMoreValorations", method=RequestMethod.GET)
	public ModelAndView topFiveUsersMoreValorations(@RequestParam(required=false, defaultValue="1") final int page) {
		ModelAndView result;
		Page<User> userPage;
		LinkedHashMap<User, Level> userLevel;
		Level level;
		
		userPage = this.userService.topFiveUsersMoreValorations(page, 5);
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
	
	// List
	@RequestMapping(value="/purchaseMoreTickets", method=RequestMethod.GET)
	public ModelAndView purchaseMoreTickets(@RequestParam(required=false, defaultValue="1") final int page) {
		ModelAndView result;
		Page<User> userPage;
		LinkedHashMap<User, Level> userLevel;
		Level level;
		
		userPage = this.userService.purchaseMoreTickets(page, 5);
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

	// List
	@RequestMapping(value="/purchaseLessTickets", method=RequestMethod.GET)
	public ModelAndView purchaseLessTickets(@RequestParam(required=false, defaultValue="1") final int page) {
		ModelAndView result;
		Page<User> userPage;
		LinkedHashMap<User, Level> userLevel;
		Level level;
		
		userPage = this.userService.purchaseLessTickets(page, 5);
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

	
}
