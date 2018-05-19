package controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import domain.Raffle;

import services.RaffleService;
import services.TicketService;

//import api.PaypalClient;

@Controller
@RequestMapping(value="/raffle")
public class RaffleController extends AbstractController {

	@Autowired
	private RaffleService raffleService;
	
	@Autowired
	private TicketService ticketService;
	
	public RaffleController() {
		super();
	}
	
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required=false, defaultValue="1") final int page) {
		ModelAndView result;
		Page<Raffle> rafflePage;
		
		rafflePage = this.raffleService.findAvailables(page, 5);
		Assert.notNull(rafflePage);
		
		result = new ModelAndView("raffle/list");
		result.addObject("raffles", rafflePage.getContent());
		result.addObject("pageNumber", rafflePage.getTotalPages());
		result.addObject("page", page);
		result.addObject("requestURI", "raffle/list.do");
		
		return result;
	}
	
	@RequestMapping(value="/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int raffleId, HttpServletRequest request) {
		ModelAndView result;
		Raffle raffle;
		Integer numberTickets; 
		
		raffle = this.raffleService.findOne(raffleId);
		Assert.notNull(raffle);
		
		numberTickets = this.ticketService.countByRaffleId(raffle.getId());
		Assert.notNull(numberTickets);
		
		result = new ModelAndView("raffle/display");
		result.addObject("raffle", raffle);
		result.addObject("hasTicketsSelled", numberTickets >= 1);
		result.addObject("url", super.makeUrl(request));
		
		return result;
	}
	
}
