
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import security.UserAccount;
import services.TagService;
import services.BargainService;
import services.CompanyService;
import domain.Bargain;
import domain.Tag;

@Controller
@RequestMapping("/tagValue")
public class TagController extends AbstractController {

	// Services
	@Autowired
	TagService				tagService;

	@Autowired
	CompanyService	companyService;

	@Autowired
	BargainService				bargainService;

	// Constructor
	public TagController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int bargainId) {
		ModelAndView result;
		Collection<Tag> tags;
		String requestURI;
		UserAccount userAccount;
		Bargain bargain;
		boolean canEditOrCreate;
		int userAccountId;

		userAccountId = 0;
		canEditOrCreate = false;

		if (LoginService.isAuthenticated()) {
			userAccount = LoginService.getPrincipal();
			userAccountId = userAccount.getId();
		}
		
		bargain = this.bargainService.findOne(bargainId);
		Assert.notNull(bargain);
		if (bargain.getCompany().getUserAccount().getId() == userAccountId)
			canEditOrCreate = true;

		requestURI = "tag/list.do?bargainId=" + bargainId;
		tags = this.tagService.findByBargain(bargain);
		Assert.notNull(tags);

		result = new ModelAndView("tag/list");
		result.addObject("tags", tags);
		result.addObject("requestURI", requestURI);
		result.addObject("bargainId", bargainId);
		result.addObject("canEditOrCreate", canEditOrCreate);

		return result;
	}

}
