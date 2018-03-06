
package controllers.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.AnswerService;
import services.CreditCardService;
import services.QuestionService;
import services.RendezvousService;
import services.RequestService;
import services.RsvpService;
import services.UserService;
import controllers.AbstractController;
import domain.Answer;
import domain.Comment;
import domain.CreditCard;
import domain.Question;
import domain.Rendezvous;
import domain.Request;
import domain.Rsvp;
import domain.User;
import forms.RsvpForm;

@Controller
@RequestMapping("/request/user")
public class RequestUserController extends AbstractController {

	// Services
	@Autowired
	private RequestService			requestService;

	@Autowired
	private RendezvousService			rendezvousService;
	
	@Autowired
	private CreditCardService			creditCardService;
	
	@Autowired
	private ServicioService			servicioService;
	// Constructor
	public RequestUserController() {
		super();
	}

	// Request -------------------------------------------------------------------------------------------

	//Create
		@RequestMapping(value = "/request", method = RequestMethod.GET)
		public ModelAndView create(@RequestParam final int rendezvousId) {
			ModelAndView result;
			Rendezvous rendezvous;
			Request request;

			rendezvous = this.rendezvousService.findOneToDisplay(rendezvousId);
			Assert.notNull(rendezvous);

			request = this.requestService.create(rendezvous);

			result = this.createEditModelAndView(request);

			return result;

		}
		
	@RequestMapping(value = "/request", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Request request, final BindingResult binding) {
		ModelAndView result = null;

//		request = this.requestService.reconstruct(request, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(request);
		else
			try {
				this.requestService.save(request);
				result = new ModelAndView("redirect:/rendezvous/display.do?rendezvousId=" + request.getRendezvous().getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(request, "comment.commit.error");
			}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Request request) {
		ModelAndView result;

		result = this.createEditModelAndView(request, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Request request, final String messageCode) {
		ModelAndView result;
		Collection<Service> services;
		Collection<CreditCard> creditCards;

		services = this.servicioService.findByRendezvousId(request.getRendezvous().getId());
		
		creditCards = this.creditCardService.findByUserAccountId(LoginService.getPrincipal().getId());

		result = new ModelAndView("request/request");
		result.addObject("services", services);
		result.addObject("creditCards", creditCards);

		return result;
	}
	
}
