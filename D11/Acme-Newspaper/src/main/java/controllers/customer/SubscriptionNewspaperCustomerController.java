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
import services.SubscriptionNewspaperService;
import services.CustomerService;
import services.NewspaperService;

import controllers.AbstractController;
import domain.SubscriptionNewspaper;

@Controller
@RequestMapping("/subscriptionNewspaper/customer")
public class SubscriptionNewspaperCustomerController extends AbstractController {

	// Services
	@Autowired
	private SubscriptionNewspaperService subscriptionNewspaperService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private NewspaperService newspaperService;
	
	// Constructor
	public SubscriptionNewspaperCustomerController() {
		super();
	}
	
	// List
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false, defaultValue="1") final Integer page) {
		ModelAndView result;
		Page<SubscriptionNewspaper> pageSubscriptionNewspaper;
		
		pageSubscriptionNewspaper = this.subscriptionNewspaperService.findByUserAccountId(LoginService.getPrincipal().getId(), page, 5);
		Assert.notNull(pageSubscriptionNewspaper);
		
		result = new ModelAndView("subscriptionNewspaper/list");
		result.addObject("pageNumber", pageSubscriptionNewspaper.getTotalPages());
		result.addObject("page", page);
		result.addObject("subscriptionNewspapers", pageSubscriptionNewspaper.getContent());
		
		return result;
	}
	
	// Create
	@RequestMapping(value="/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int newspaperId) {
		ModelAndView result;
		SubscriptionNewspaper subscriptionNewspaper;		

		subscriptionNewspaper = this.subscriptionNewspaperService.create(this.customerService.findByUserAccountId(LoginService.getPrincipal().getId()), this.newspaperService.findOne(newspaperId));
		Assert.notNull(subscriptionNewspaper);

		result = this.createEditModelAndView(subscriptionNewspaper);
		
		return result;
	}

	// Edit
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int subscriptionNewspaperId) {
		ModelAndView result;
		SubscriptionNewspaper subscriptionNewspaper;

		subscriptionNewspaper = this.subscriptionNewspaperService.findOneToEdit(subscriptionNewspaperId);
		Assert.notNull(subscriptionNewspaper);

		result = this.createEditModelAndView(subscriptionNewspaper);

		return result;
	}
	
	// Edit
	@RequestMapping(value="/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(SubscriptionNewspaper subscriptionNewspaper, BindingResult binding) {
		ModelAndView result;
		
		subscriptionNewspaper = this.subscriptionNewspaperService.reconstruct(subscriptionNewspaper, binding);
		
		if(binding.hasErrors()){
			result = this.createEditModelAndView(subscriptionNewspaper);
		}else{
			try {
				this.subscriptionNewspaperService.save(subscriptionNewspaper);
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(subscriptionNewspaper, "subscriptionNewspaper.commit.error");
			}
		}
		
		return result;
	}
	
	// Delete
	@RequestMapping(value="/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(SubscriptionNewspaper subscriptionNewspaper, BindingResult binding) {
		ModelAndView result;
		
		try {
			this.subscriptionNewspaperService.delete(subscriptionNewspaper);
			result = new ModelAndView("redirect:list.do");
		} catch (Throwable oops) {
			result = this.createEditModelAndView(subscriptionNewspaper, "subscriptionNewspaper.commit.error");
		}
		
		return result;
	}
	
	// Ancillary methods
	protected ModelAndView createEditModelAndView(final SubscriptionNewspaper subscriptionNewspaper) {
		ModelAndView result;

		result = this.createEditModelAndView(subscriptionNewspaper, null);

		return result;
	}
	
	protected ModelAndView createEditModelAndView(final SubscriptionNewspaper subscriptionNewspaper, final String messageCode) {
		ModelAndView result;
		
		result = new ModelAndView("subscriptionNewspaper/edit");
		
		result.addObject("subscriptionNewspaper", subscriptionNewspaper);
		result.addObject("message", messageCode);

		return result;
	}
	
}
