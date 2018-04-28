package controllers.user;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import domain.Raffle;
import domain.Ticket;
import forms.TicketForm;

import security.LoginService;
import services.RaffleService;
import services.TicketService;

import api.PaypalClient;

@Controller
@RequestMapping(value="/raffle/user")
public class RaffleUserController extends AbstractController {

    private final PaypalClient paypalClient;
    
    // Supporting services
	@Autowired
	private RaffleService raffleService;
	
	@Autowired
	private TicketService ticketService;
	
    public RaffleUserController(){
        this.paypalClient = new PaypalClient();
    }
	
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required=false, defaultValue="1") final int page) {
		ModelAndView result;
		Page<Raffle> rafflePage;
		
		rafflePage = this.raffleService.findByUserAccountId(LoginService.getPrincipal().getId(), page, 5);
		Assert.notNull(rafflePage);
		
		result = new ModelAndView("raffle/list");
		result.addObject("raffles", rafflePage.getContent());
		result.addObject("pageNumber", rafflePage.getTotalPages());
		result.addObject("page", page);
		
		return result;
	}
	
	@RequestMapping(value="/buy", method = RequestMethod.GET)
	public ModelAndView buy(@RequestParam(defaultValue="CREDITCARD") final String method, @RequestParam final Integer amount, @RequestParam int raffleId) {
		ModelAndView result;
		Raffle raffle;
		Map<String, Object> attributes;

		raffle = this.raffleService.findOne(raffleId);
		Assert.notNull(raffle);
		
		result = null;
		if(method.equals("CREDITCARD")) {
			
		} else if(method.equals("PAYPAL")) {
			attributes = paypalClient.createPayment(String.valueOf(raffle.getPrice()), raffleId, amount);
			
			result = new ModelAndView("redirect:"+(String) attributes.get("redirect_url"));
			
			for(String key: attributes.keySet())
				result.addObject(key, attributes.get(key));
		}
		
		return result;
	}
	
	@RequestMapping(value="/completepayment", method = RequestMethod.GET)
	public ModelAndView completePayment(@RequestParam final String paymentId, @RequestParam final String PayerID, @RequestParam final Integer amount, @RequestParam int raffleId) {
		ModelAndView result;
		Collection<Ticket> tickets;
		TicketForm ticketForm;
		Raffle raffle;
		
		System.out.println(amount);

		raffle = this.raffleService.findOne(raffleId);
		Assert.notNull(raffle);
		
		if(paymentId != null) {
			paypalClient.completePayment(paymentId, PayerID);
		}
		
		ticketForm = new TicketForm();
		ticketForm.setAmount(amount);
		ticketForm.setRaffle(raffle);
		
		tickets = this.ticketService.reconstruct(ticketForm, null);
		Assert.notNull(tickets);

		try {
			this.ticketService.save(tickets);
			result = new ModelAndView("redirect:/raffle/display.do?raffleId="+raffleId);
		} catch (Throwable oops) {
			result = super.panic(oops);
		}
		
		return result;
	}
	
	@RequestMapping(value="/buy", method = RequestMethod.POST, params = "save")
	public ModelAndView buy(final TicketForm ticketForm, @RequestParam final int raffleId, final BindingResult binding) {
		ModelAndView result;
		Collection<Ticket> tickets;
		
		tickets = this.ticketService.reconstruct(ticketForm, binding);
		Assert.notNull(tickets);

		if(binding.hasErrors()) {
			result = this.createModelAndView(ticketForm, raffleId);
		} else {
			try {
				this.ticketService.save(tickets);
				result = new ModelAndView("redirect:/raffle/display.do?raffleId="+raffleId);
			} catch (Throwable oops) {
				result = super.panic(oops);
			}
		}
		
		return result;
	}
	
	protected ModelAndView createModelAndView(final TicketForm ticketForm, final int raffleId) {
		ModelAndView result;

		result = this.createModelAndView(ticketForm, raffleId, null);

		return result;
	}

	protected ModelAndView createModelAndView(final TicketForm ticketForm, final int raffleId, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("raffle/buy");

		result.addObject("ticketForm", ticketForm);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "raffle/user/buy.do?method=CREDITCARD&raffleId="+raffleId);

		return result;
	}
	
}
