package controllers.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.SubscriptionService;
import services.CustomerService;
import services.NewspaperService;

import controllers.AbstractController;
import domain.Subscription;

@Controller
@RequestMapping("/subscription/customer")
public class SubscriptionCustomerController extends AbstractController {

	// Services
	@Autowired
	private SubscriptionService subscriptionService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private NewspaperService newspaperrService;
	
	// Constructor
	public SubscriptionCustomerController() {
		super();
	}
	
	// List
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false, defaultValue="1") final Integer page) {
		ModelAndView result;
		Page<Subscription> pageSubscription;
		
		pageSubscription = this.subscriptionService.findByUserAccountId(LoginService.getPrincipal().getId(), page, 5);
		Assert.notNull(pageSubscription);
		
		result = new ModelAndView("subscription/list");
		result.addObject("pageNumber", pageSubscription.getTotalPages());
		result.addObject("page", page);
		result.addObject("subscriptions", pageSubscription.getContent());
		
		return result;
	}
	
	// Create
	@RequestMapping(value="/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int newspaperId) {
		ModelAndView result;
		Subscription subscription;		

		subscription = this.subscriptionService.create(this.customerService.findByUserAccountId(LoginService.getPrincipal().getId()), this.newspaperService.findOne(newspaperId));
		Assert.notNull(subscription);

		result = this.createEditModelAndView(subscription);
		
		return result;
	}

	// Edit
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int subscriptionId) {
		ModelAndView result;
		Subscription subscription;

		subscription = this.subscriptionService.findOneToEdit(subscriptionId);
		Assert.notNull(subscription);

		result = this.createEditModelAndView(subscription);

		return result;
	}
	
	// Edit
	@RequestMapping(value="/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Subscription subscription, BindingResult binding) {
		ModelAndView result;
		
		subscription = this.subscriptionService.reconstruct(subscription, binding);
		
		if(binding.hasErrors()){
			result = this.createEditModelAndView(subscription);
		}else{
			try {
				this.subscriptionService.save(subscription);
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(subscription, "subscription.commit.error");
			}
		}
		
		return result;
	}
	
	// Delete
	@RequestMapping(value="/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Subscription subscription, BindingResult binding) {
		ModelAndView result;
		
		try {
			this.subscriptionService.delete(subscription);
			result = new ModelAndView("redirect:list.do");
		} catch (Throwable oops) {
			result = this.createEditModelAndView(subscription, "subscription.commit.error");
		}
		
		return result;
	}
	
	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Subscription subscription) {
		ModelAndView result;

		result = this.createEditModelAndView(subscription, null);

		return result;
	}
	
	protected ModelAndView createEditModelAndView(final Subscription subscription, final String messageCode) {
		ModelAndView result;
		
		result = new ModelAndView("subscription/edit");
		
		result.addObject("subscription", subscription);
		result.addObject("message", messageCode);

		return result;
	}
	
}
