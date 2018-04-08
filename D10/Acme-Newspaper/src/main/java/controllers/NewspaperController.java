
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.LoginService;
import services.ArticleService;
import services.CustomerService;
import services.NewspaperService;
import services.SubscriptionService;
import domain.Article;
import domain.Newspaper;

@Controller
@RequestMapping("/newspaper")
public class NewspaperController extends AbstractController {

	// Services
	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private ArticleService		articleService;

	@Autowired
	private SubscriptionService	subscriptionService;

	@Autowired
	private CustomerService		customerService;


	// Constructor
	public NewspaperController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int newspaperId, @RequestParam(required = false, defaultValue = "1") final Integer page) {
		ModelAndView result;
		Newspaper newspaper;

		newspaper = this.newspaperService.findOneToDisplay(newspaperId);

		Assert.notNull(newspaper);
		result = this.createDisplayModelAndView(newspaper, page);

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false, defaultValue = "1") final Integer page) {
		ModelAndView result;
		Page<Newspaper> newspapers;

		if (LoginService.isAuthenticated())
			newspapers = this.newspaperService.findAllPaginated(page, 5);
		else
			newspapers = this.newspaperService.findPublicsAndPublicated(page, 5);
		Assert.notNull(newspapers);

		result = new ModelAndView("newspaper/list");
		result.addObject("pageNumber", newspapers.getTotalPages());
		result.addObject("page", page);
		result.addObject("newspapers", newspapers.getContent());
		result.addObject("requestURI", "newspaper/list.do");

		return result;
	}

	//Ancillary methods -----------------------
	protected ModelAndView createDisplayModelAndView(final Newspaper newspaper, final int page) {
		ModelAndView result;
		Page<Article> articles;
		Boolean canSeeArticles;
		Authority authority;

		articles = this.articleService.findByNewspaperIdPaginated(newspaper.getId(), page, 5);

		canSeeArticles = false;
		authority = new Authority();
		authority.setAuthority("CUSTOMER");
		if (newspaper.getIsPrivate() == false)
			canSeeArticles = true;
		else if (LoginService.isAuthenticated() && LoginService.getPrincipal().getAuthorities().contains(authority)
			&& this.subscriptionService.findByCustomerAndNewspaperId(this.customerService.findByUserAccountId(LoginService.getPrincipal().getId()).getId(), newspaper.getId()) != null)
			canSeeArticles = true;
		else if (LoginService.isAuthenticated() && newspaper.getPublisher().getUserAccount().getId() == LoginService.getPrincipal().getId())
			canSeeArticles = true;

		result = new ModelAndView("newspaper/display");

		result.addObject("pageNumber", articles.getTotalPages());
		result.addObject("page", page);
		result.addObject("newspaper", newspaper);
		result.addObject("articles", articles.getContent());
		result.addObject("canSeeArticles", canSeeArticles);

		return result;

	}
}
