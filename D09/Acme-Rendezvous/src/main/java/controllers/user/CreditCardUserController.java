package controllers.user;

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
import services.CreditCardService;
import services.UserService;

import controllers.AbstractController;
import domain.CreditCard;

@Controller
@RequestMapping("/creditcard/user")
public class CreditCardUserController extends AbstractController {

	// Services
	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private UserService userService;
	
//	@Autowired
//	private RequestService requestService;
	
	// Constructor
	public CreditCardUserController() {
		super();
	}
	
	// List
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false, defaultValue="1") final Integer page) {
		ModelAndView result;
		Page<CreditCard> pageCreditCard;
		
		pageCreditCard = this.creditCardService.findByUserAccountId(LoginService.getPrincipal().getId(), page, 5);
		Assert.notNull(pageCreditCard);
		
		result = new ModelAndView("creditcard/list");
		result.addObject("pageNumber", pageCreditCard.getTotalPages());
		result.addObject("page", page);
		result.addObject("creditCards", pageCreditCard.getContent());
		
		return result;
	}
	
	// Create
	@RequestMapping(value="/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		CreditCard creditCard;		

		creditCard = this.creditCardService.create(this.userService.findByUserAccountId(LoginService.getPrincipal().getId()));
		Assert.notNull(creditCard);

		result = this.createEditModelAndView(creditCard);
		
		return result;
	}

	// Edit
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int creditcardId) {
		ModelAndView result;
		CreditCard creditCard;
//		boolean isAdded;
				
		creditCard = this.creditCardService.findOneToEdit(creditcardId);
		Assert.notNull(creditCard);
		
//		isAdded = this.requestService.countByCreditCardId(creditCardId) == 0;
	
		result = this.createEditModelAndView(creditCard);
//		result.addObject("isAdded", isAdded);
		
		return result;
	}
	
	// Edit
	@RequestMapping(value="/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(CreditCard creditCard, BindingResult binding) {
		ModelAndView result;
		
		creditCard = this.creditCardService.reconstruct(creditCard, binding);
		
		if(binding.hasErrors()){
			result = this.createEditModelAndView(creditCard);
		}else{
			try {
				this.creditCardService.save(creditCard);
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
//				result = super.panic(oops);
				result = this.createEditModelAndView(creditCard, "creditcard.commit.error");
			}
		}
		
		return result;
	}
	
	// Delete
	@RequestMapping(value="/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(CreditCard creditCard, BindingResult binding) {
		ModelAndView result;
		
		creditCard = this.creditCardService.reconstruct(creditCard, binding);
		
		try {
			this.creditCardService.delete(creditCard);
			result = new ModelAndView("redirect:list.do");
		} catch (Throwable oops) {
			result = this.createEditModelAndView(creditCard, "creditcard.commit.error");
		}
		
		return result;
	}
	
	// Ancillary methods
	protected ModelAndView createEditModelAndView(final CreditCard creditCard) {
		ModelAndView result;

		result = this.createEditModelAndView(creditCard, null);

		return result;
	}
	
	protected ModelAndView createEditModelAndView(final CreditCard creditCard, final String messageCode) {
		ModelAndView result;
		
		result = new ModelAndView("creditcard/edit");
		
		result.addObject("creditCard", creditCard);
		result.addObject("message", messageCode);

		return result;
	}
	
}
