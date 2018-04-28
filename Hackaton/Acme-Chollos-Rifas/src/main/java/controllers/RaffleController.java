package controllers;

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

import api.PaypalClient;

@Controller
@RequestMapping(value="/raffle")
public class RaffleController extends AbstractController {

    private final PaypalClient paypalClient;
    
    public RaffleController(){
        this.paypalClient = new PaypalClient();
    }
	
	@Autowired
	private RaffleService raffleService;
	
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
		
		return result;
	}
	
	@RequestMapping(value="/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int raffleId) {
		ModelAndView result;
		Raffle raffle;
		
		raffle = this.raffleService.findOne(raffleId);
		Assert.notNull(raffle);
		
		result = new ModelAndView("raffle/display");
		result.addObject("raffle", raffle);
		
		return result;
	}
	
}
