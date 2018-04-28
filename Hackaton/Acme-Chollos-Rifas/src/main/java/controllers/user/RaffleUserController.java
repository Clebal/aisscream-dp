package controllers.user;

import java.util.Map;

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

import security.LoginService;
import services.RaffleService;

import api.PaypalClient;

@Controller
@RequestMapping(value="/raffle/user")
public class RaffleUserController extends AbstractController {

    private final PaypalClient paypalClient;
    
    public RaffleUserController(){
        this.paypalClient = new PaypalClient();
    }
	
	@Autowired
	private RaffleService raffleService;
	
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
	
	@RequestMapping(value="/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int raffleId, @RequestParam(required=false) final String paymentId, @RequestParam(required=false) final String PayerID) {
		ModelAndView result;
		Raffle raffle;
		
		raffle = this.raffleService.findOne(raffleId);
		Assert.notNull(raffle);
		
		result = new ModelAndView("raffle/display");
		result.addObject("raffle", raffle);
		
		if(paymentId != null) paypalClient.completePayment(paymentId, PayerID);
		
		return result;
	}
	
	@RequestMapping(value="/buy", method = RequestMethod.GET)
	public ModelAndView buy(@RequestParam(defaultValue="CREDITCARD") final String payMethod, @RequestParam int raffleId) {
		ModelAndView result;
		Raffle raffle;
		Map<String, Object> attributes;

		raffle = this.raffleService.findOne(raffleId);
		Assert.notNull(raffle);
		
		result = null;
		if(payMethod.equals("CREDITCARD")) {
			
		} else if(payMethod.equals("PAYPAL")) {
			attributes = paypalClient.createPayment(String.valueOf(raffle.getPrice()), raffleId);
			
			result = new ModelAndView("redirect:"+(String) attributes.get("redirect_url"));
			
			for(String key: attributes.keySet())
				result.addObject(key, attributes.get(key));
		}
		
		return result;
	}
	
}
