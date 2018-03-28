package controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Rifa;

import services.RifaService;

import api.PaypalClient;

@Controller
@RequestMapping(value="/rifa")
public class RifaController extends AbstractController {

    private final PaypalClient paypalClient;
    
    public RifaController(){
        this.paypalClient = new PaypalClient();
    }
	
	@Autowired
	private RifaService rifaService;
	
	@RequestMapping(value="/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int rifaId, @RequestParam(required=false) final String paymentId, @RequestParam(required=false) final String PayerID) {
		ModelAndView result;
		Rifa rifa;
		
		rifa = this.rifaService.findOne(rifaId);
		Assert.notNull(rifa);
		
		result = new ModelAndView("rifa/display");
		result.addObject("rifa", rifa);
		
		if(paymentId != null) paypalClient.completePayment(paymentId, PayerID);
		
		return result;
	}
	
	@RequestMapping(value="/buy", method = RequestMethod.GET)
	public ModelAndView buy(@RequestParam int rifaId) {
		ModelAndView result;
		Rifa rifa;
		Map<String, Object> attributes;

		rifa = this.rifaService.findOne(rifaId);
		Assert.notNull(rifa);
		
		attributes = paypalClient.createPayment(String.valueOf(rifa.getPrice()), rifaId);
		
		result = new ModelAndView("redirect:"+(String) attributes.get("redirect_url"));
		
		for(String key: attributes.keySet())
			result.addObject(key, attributes.get(key));
		
		return result;
	}
	
}
