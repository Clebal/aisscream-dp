
package controllers.company;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BargainService;
import services.CompanyService;
import services.TagService;
import controllers.AbstractController;
import domain.Bargain;
import domain.Tag;

@Controller
@RequestMapping("/tag/company")
public class TagCompanyController extends AbstractController {

	// Services
	@Autowired
	TagService				tagService;

	@Autowired
	CompanyService			companyService;

	@Autowired
	BargainService			bargainService;
	
	// Constructor
	public TagCompanyController() {
		super();
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int bargainId) {
		ModelAndView result;
		Tag tag;
		Bargain bargain;

		bargain = this.bargainService.findOne(bargainId);
		tag = this.tagService.create(bargain);

		result = this.createEditModelAndView(tag);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int tagId) {
		ModelAndView result;
		Tag tag;

		tag = this.tagService.findOne(tagId);
		Assert.notNull(tag);

		result = this.createEditModelAndView(tag);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Tag tag, final BindingResult binding) {
		ModelAndView result;
		
		tag = this.tagService.reconstruct(tag, binding);
		
		if (binding.hasErrors())
			result = this.createEditModelAndView(tag);
		else
			try {
				this.tagService.save(tag);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(tag, "tag.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Tag tag, final BindingResult binding) {
		ModelAndView result;

		try {
			this.tagService.delete(tag);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(tag, "tag.commit.error");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Tag tag) {
		ModelAndView result;

		result = this.createEditModelAndView(tag, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Tag tag, final String messageCode) {
		ModelAndView result;

		if (tag.getId() > 0)
			result = new ModelAndView("tag/edit");
		else
			result = new ModelAndView("tag/create");

		result.addObject("tag", tag);
		result.addObject("message", messageCode);
		result.addObject("canEdit", true);

		return result;
	}

}
