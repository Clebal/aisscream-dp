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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;

@Controller
public class AbstractController {
	
	@Autowired
	private ConfigurationService configurationService;
	
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
		String banner, nameHeader, slogan;

		banner = this.configurationService.findBanner();
		slogan = this.configurationService.findSlogan();
		nameHeader = this.configurationService.findName();

		model.addAttribute("banner", banner);
		model.addAttribute("slogan", slogan);
		model.addAttribute("nameHeader", nameHeader);
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
