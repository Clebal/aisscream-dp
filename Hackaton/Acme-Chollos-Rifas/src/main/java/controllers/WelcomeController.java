/*
 * WelcomeController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import domain.Raffle;

import services.RaffleService;

@Controller
@RequestMapping("/welcome")
public class WelcomeController extends AbstractController {

	@Autowired
	private RaffleService raffleService;
	
	// Constructors -----------------------------------------------------------

	public WelcomeController() {
		super();
	}

	// Index ------------------------------------------------------------------		

	@RequestMapping(value = "/index")
	public ModelAndView index() {
		ModelAndView result;
		Page<Raffle> raffles;
		
		raffles = this.raffleService.findOrderedByMaxDate(1, 3);
		Assert.notNull(raffles);
		
		result = new ModelAndView("welcome/index");
		result.addObject("raffles", raffles.getContent());

		return result;
	}
}
