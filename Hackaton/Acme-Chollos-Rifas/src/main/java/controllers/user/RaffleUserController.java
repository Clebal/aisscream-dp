package controllers.user;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import controllers.AbstractController;
import converters.StringToCreditCardConverter;

import domain.CreditCard;
import domain.Raffle;
import domain.Ticket;
import domain.User;
import forms.TicketForm;

import security.LoginService;
import services.CreditCardService;
import services.RaffleService;
import services.TicketService;
import services.UserService;
import services.res.PaypalClient;


@Controller
@RequestMapping(value="/raffle/user")
public class RaffleUserController extends AbstractController {

    private final PaypalClient paypalClient;
    
    // Supporting services
	@Autowired
	private RaffleService raffleService;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private UserService	userService;
	
	// Converters
	@Autowired
	private StringToCreditCardConverter stringToCreditCardConverter;
	
	@Autowired
	private CreditCardService creditCardService;
	
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
	public ModelAndView buy(@RequestParam(defaultValue="CREDITCARD") final String method, @RequestParam(defaultValue="1", required=false) final Integer amount, @RequestParam int raffleId, HttpServletRequest request) {
		ModelAndView result;
		Raffle raffle;
		TicketForm ticketForm;
		Map<String, Object> attributes;
		Collection<CreditCard> creditCards;
		Cookie cookie;

		raffle = this.raffleService.findOne(raffleId);
		Assert.notNull(raffle);
		
		result = null;
		if(method.equals("CREDITCARD")) {
			
			ticketForm = new TicketForm();
			ticketForm.setRaffle(raffle);
			
			creditCards = this.creditCardService.findByUserAccountId(LoginService.getPrincipal().getId());
			Assert.notNull(creditCards);
			
			result = new ModelAndView("raffle/buy");
			
			cookie = WebUtils.getCookie(request, "cookiemonster_"+LoginService.getPrincipal().getId());
			if(cookie != null) {
				result.addObject("primaryCreditCard", cookie.getValue());
				ticketForm.setCreditCard(this.stringToCreditCardConverter.convert(cookie.getValue()));
			}
			
			result.addObject("ticketForm", ticketForm);
			result.addObject("creditCards", creditCards);
			result.addObject("raffleId", raffleId);
			
		} else if(method.equals("PAYPAL")) {
			attributes = paypalClient.createPayment(String.valueOf(raffle.getPrice()*amount), raffleId, amount);
			
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
		User user;
		
		raffle = this.raffleService.findOne(raffleId);
		Assert.notNull(raffle);
		
		user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(user);
		
		if(paymentId != null) {
			paypalClient.completePayment(paymentId, PayerID);
		}
		
		ticketForm = new TicketForm();
		ticketForm.setAmount(amount);
		ticketForm.setRaffle(raffle);
		ticketForm.setUser(user);
		
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
	public ModelAndView buy(final TicketForm ticketForm, final BindingResult binding) {
		ModelAndView result;
		Collection<Ticket> tickets;
		
		Assert.notNull(ticketForm.getCreditCard());
		tickets = this.ticketService.reconstruct(ticketForm, binding);
		Assert.notNull(tickets);

		if(binding.hasErrors()) {
			for(ObjectError e: binding.getAllErrors())
				System.out.println(e);
			result = this.createModelAndView(ticketForm);
		} else {
			try {
				this.ticketService.save(tickets);
				result = new ModelAndView("redirect:/raffle/display.do?raffleId="+ticketForm.getRaffle().getId());
			} catch (Throwable oops) {
				result = super.panic(oops);
			}
		}
		
		return result;
	}
	
	protected ModelAndView createModelAndView(final TicketForm ticketForm) {
		ModelAndView result;

		result = this.createModelAndView(ticketForm, null);

		return result;
	}

	protected ModelAndView createModelAndView(final TicketForm ticketForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("raffle/buy");

		result.addObject("ticketForm", ticketForm);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "raffle/user/buy.do?method=CREDITCARD&raffleId="+ticketForm.getRaffle().getId());

		return result;
	}
	
}
