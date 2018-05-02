package controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import domain.Ticket;

import security.LoginService;
import services.TicketService;

@Controller
@RequestMapping(value="/ticket/user")
public class TicketUserController extends AbstractController {

	// Supporint services
	@Autowired
	private TicketService ticketService;
	
	// Constructor
	public TicketUserController() {
		super();
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public ModelAndView list(@RequestParam(required=false) final Integer raffleId, @RequestParam(required=false, defaultValue="1")  final int page) {
		ModelAndView result;
		Page<Ticket> ticketPage;
		
		if(raffleId == null)
			ticketPage = this.ticketService.findByUserAccountId(LoginService.getPrincipal().getId(), page, 5);
		else
			ticketPage = this.ticketService.findByRaffleIdAndUserAccountId(raffleId, LoginService.getPrincipal().getId(), page, 5);
		
		result = new ModelAndView("ticket/list");
		result.addObject("tickets", ticketPage.getContent());
		result.addObject("pageNumber", ticketPage.getTotalPages());
		result.addObject("page", page);
		
		result.addObject("requestURI", "ticket/user/list.do");

		if(raffleId == null)
			result.addObject("showRaffle", true);
		else
			result.addObject("showRaffle", false);
		
		return result;
	}
	
}
