
package controllers.user;

import java.util.Collection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import security.LoginService;
import services.CreditCardService;
import services.SubscriptionService;
import services.UserService;
import services.res.PaypalClient;
import controllers.AbstractController;
import converters.CreditCardToStringConverter;
import domain.CreditCard;
import domain.Subscription;

@Controller
@RequestMapping(value = "/subscription/user")
public class SubscriptionUserController extends AbstractController {

	// Supporting services
	@Autowired
	private SubscriptionService			subscriptionService;

	@Autowired
	private UserService					userService;

	@Autowired
	private CreditCardService			creditCardService;

	// Converter
	@Autowired
	private CreditCardToStringConverter	creditCardToStringConverter;


	public SubscriptionUserController() {
		new PaypalClient();
	}

	// Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;
		Subscription subscription;

		subscription = this.subscriptionService.findByUserId(this.userService.findByUserAccountId(LoginService.getPrincipal().getId()).getId());

		result = new ModelAndView("subscription/display");
		result.addObject("subscription", subscription);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView request(@RequestParam final int planId, final HttpServletRequest request) {
		ModelAndView result;
		Subscription subscription;
		Cookie cookie;

		subscription = this.subscriptionService.create(planId);

		result = this.createEditModelAndView(subscription);

		cookie = WebUtils.getCookie(request, "cookiemonster_" + subscription.getUser().getId());
		if (cookie != null)
			result.addObject("lastCreditCard", cookie.getValue());

		return result;

	}

	// Edit
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int subscriptionId) {
		ModelAndView result;
		Subscription subscription;

		subscription = this.subscriptionService.findOneToEdit(subscriptionId);

		result = this.createEditModelAndView(subscription);

		return result;
	}

	// Edit
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Subscription subscription, final BindingResult binding, final HttpServletResponse response) {
		ModelAndView result;
		Cookie lastCreditCard;

		subscription = this.subscriptionService.reconstruct(subscription, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(subscription);
		else
			try {
				this.subscriptionService.save(subscription);
				// --- COOKIE --- //
				lastCreditCard = new Cookie("cookiemonster_" + subscription.getUser().getId(), this.creditCardToStringConverter.convert(subscription.getCreditCard()));
				lastCreditCard.setHttpOnly(true);
				lastCreditCard.setMaxAge(3600000);
				response.addCookie(lastCreditCard);
				// --- COOKIE --- //
				result = new ModelAndView("redirect:display.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(subscription, "subscription.commit.error");
			}

		return result;
	}

	// Delete
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Subscription subscription, final BindingResult binding) {
		ModelAndView result;

		try {
			this.subscriptionService.delete(subscription);
			result = new ModelAndView("redirect:display.do");
		} catch (final Throwable oops) {
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
		Collection<CreditCard> creditCards;

		creditCards = this.creditCardService.findByUserAccountId(LoginService.getPrincipal().getId());

		if (subscription.getId() != 0) {
			result = new ModelAndView("subscription/edit");
			creditCards.remove(subscription.getCreditCard());
		} else
			result = new ModelAndView("subscription/create");

		result.addObject("creditCards", creditCards);
		result.addObject("subscription", subscription);
		result.addObject("message", messageCode);

		return result;
	}

}
