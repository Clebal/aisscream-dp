
package controllers.user;

import java.util.Collection;

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
import services.CreditCardService;
import services.RendezvousService;
import services.RequestService;
import services.ServicioService;
import services.UserService;
import controllers.AbstractController;
import domain.CreditCard;
import domain.Rendezvous;
import domain.Request;
import domain.Servicio;
import domain.User;

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
	private UserService					userService;
	
	@Autowired
	private ServicioService				servicioService;
	
	// Constructor
	public RequestUserController() {
		super();
	}

	// List
		@RequestMapping(value="/list", method = RequestMethod.GET)
		public ModelAndView list(@RequestParam(required=false) Integer page) {
			ModelAndView result;
			Collection<Request> requests;
			Integer size, pageAux, pageNumber;
			User user;
			
			size = 5;
			pageNumber = 0;
			
			if (page == null)
				pageAux = 1;
			else
				pageAux = page;
			
			user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
			
			requests = this.requestService.findAllPaginated(user.getId(), pageAux, size);
			Assert.notNull(requests);
			
			if (requests.size() != 0)
				pageNumber = this.requestService.countAllPaginated(user.getId());
			
			result = new ModelAndView("request/list");

			pageNumber = (int) Math.floor(((pageNumber / (size + 0.0)) - 0.1) + 1);

			result.addObject("requests", requests);
			result.addObject("pageNumber", pageNumber);
			result.addObject("page", pageAux);
			result.addObject("userId", user.getId());
			result.addObject("requestURI", "request/user/list.do");
			
			return result;
		}
		
		// Display
		@RequestMapping(value = "/display", method = RequestMethod.GET)
		public ModelAndView display(@RequestParam final int requestId) {
			ModelAndView result;
			Request request;

			request = this.requestService.findOne(requestId);
			Assert.notNull(request);

			result = new ModelAndView("request/display");
			result.addObject("request", request);

			return result;
		}
		
		// Delete
		@RequestMapping(value = "/delete", method = RequestMethod.GET)
		public ModelAndView delete(@RequestParam final int requestId) {
			ModelAndView result;
			Request request;

			request = this.requestService.findOne(requestId);
			this.requestService.delete(request);			
			result = new ModelAndView("redirect:list.do");
			
			return result;
		}
		
	// Request -------------------------------------------------------------------------------------------

		@RequestMapping(value = "/request", method = RequestMethod.GET)
		public ModelAndView request(@RequestParam final int rendezvousId, @RequestParam final int servicioId) {
			ModelAndView result;
			Rendezvous rendezvous;
			Servicio servicio;
			Request request;

			rendezvous = this.rendezvousService.findOneToDisplay(rendezvousId);
			Assert.notNull(rendezvous);
			
			servicio = this.servicioService.findOne(servicioId);
			Assert.notNull(servicio);

			request = this.requestService.create(rendezvous, servicio);

			result = this.createEditModelAndView(request);


			return result;

		}
		
	@RequestMapping(value = "/request", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Request request, final BindingResult binding) {
		ModelAndView result = null;

//		request = this.requestService.reconstruct(request, binding);

		if (binding.hasErrors()){
			result = this.createEditModelAndView(request);
		}else
			try {
				this.requestService.save(request);
				result = new ModelAndView("redirect:/rendezvous/display.do?rendezvousId=" + request.getRendezvous().getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(request, "request.commit.error");
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
		Collection<CreditCard> creditCards;
		
		creditCards = this.creditCardService.findByUserAccountId(LoginService.getPrincipal().getId());

		result = new ModelAndView("request/request");
		
		result.addObject("creditcards", creditCards);
		result.addObject("request", request);
		result.addObject("message", messageCode);
		result.addObject("servicio", request.getServicio());
		result.addObject("rendezvous", request.getRendezvous());

		return result;
	}
	
}
