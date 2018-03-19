/*
 * AbstractController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import domain.Actor;

import security.LoginService;
import services.ConfigurationService;
import services.ManagerService;
import services.UserService;

@Controller
public class AbstractController {
	
	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ManagerService managerService;
	
	public ModelAndView paginateModelAndView(final String path, final double collectionSize, final Integer page, final Integer size){
		ModelAndView result;
		Integer pageNumber;
		
        pageNumber = (int) Math.floor(((collectionSize / (size + 0.0)) - 0.1) + 1);

        result = new ModelAndView(path);
		result.addObject("pageNumber", pageNumber);
		result.addObject("page", page);
		
		return result;
	}

	@ModelAttribute
	public void headerConfiguration(final Model model) {
		String result, result2, result3, result4;
		Actor actor;

		result = this.configurationService.findBanner();
		result2 = this.configurationService.findName();
		result3 = this.configurationService.findWelcomeMessage(LocaleContextHolder.getLocale().getLanguage());

		model.addAttribute("banner", result);
		model.addAttribute("nameHeader", result2);
		
		if(LoginService.isAuthenticated()){
			actor = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
			if(actor == null) this.managerService.findByUserAccountId(LoginService.getPrincipal().getId());
			result4 = actor.getName() + " " + actor.getSurname();
			model.addAttribute("nameUser", result4);
		}
		
		model.addAttribute("welcomeMessage", result3);
	}
	
	// Panic handler ----------------------------------------------------------

	@ExceptionHandler(Throwable.class)
	public ModelAndView panic(final Throwable oops) {
		ModelAndView result;

		result = new ModelAndView("misc/panic");
		result.addObject("name", ClassUtils.getShortName(oops.getClass()));
		result.addObject("exception", oops.getMessage());
		result.addObject("stackTrace", ExceptionUtils.getStackTrace(oops));

		return result;
	}

}
