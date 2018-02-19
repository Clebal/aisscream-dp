package controllers.user;

import java.util.Collection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.RsvpService;

import controllers.AbstractController;
import domain.Rsvp;

@Controller
@RequestMapping("/rsvp/user")
public class RsvpUserController extends AbstractController {
	
	// Services
	@Autowired
	private RsvpService rsvpService;
	
	// Constructor
	public RsvpUserController() {
		super();
	}
	
	// List
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Rsvp> rsvps;
		
		rsvps = this.rsvpService.findByCreatorUserAccountId(LoginService.getPrincipal().getId());
		
		result = new ModelAndView("rsvp/list");
		result.addObject("rsvps", rsvps);
		
		return result;
	}
	
	// Edit
	@RequestMapping(value="/cancel", method=RequestMethod.GET)
	public ModelAndView edit(@RequestParam int rsvpId) {
		ModelAndView result;
		Rsvp rsvp;
		
		rsvp = this.rsvpService.findOne(rsvpId);
		Assert.notNull(rsvp);
		
		result = this.createEditModelAndView(rsvp);
		
		return result;
	}
	
	// Cancel
	@RequestMapping(value="/cancel", method=RequestMethod.GET)
	public ModelAndView editCancel(@RequestParam int rsvpId, BindingResult binding) {
		ModelAndView result;
		Rsvp rsvp;
		
		rsvp = this.rsvpService.findOne(rsvpId);
	
		try{
			rsvp.setStatus("CANCELLED");
			this.rsvpService.save(rsvp);
			result = new ModelAndView("redirect:list.do");
		}catch(Throwable oops){
			// result = super.panic(oops);
			result = this.createEditModelAndView(rsvp, "rsvp.commit.error");
		}

		
		return result;
	}
	
	// Accept
	@RequestMapping(value="/accept", method=RequestMethod.GET)
	public ModelAndView editAccept(@RequestParam int rsvpId, BindingResult binding) {
		ModelAndView result;
		Rsvp rsvp;
	
		rsvp = this.rsvpService.findOne(rsvpId);
		
		try{
			rsvp.setStatus("ACCEPTED");
			this.rsvpService.save(rsvp);
			result = new ModelAndView("redirect:list.do");
		}catch(Throwable oops){
			// result = super.panic(oops);
			result = this.createEditModelAndView(rsvp, "rsvp.commit.error");
		}

		
		return result;
	}
	
	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Rsvp rsvp) {
		ModelAndView result;

		result = this.createEditModelAndView(rsvp, null);

		return result;
	}
	
	protected ModelAndView createEditModelAndView(final Rsvp rsvp, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("question/edit");
		
		result.addObject("rsvp", rsvp);
		result.addObject("message", messageCode);

		return result;
	}
	

}
