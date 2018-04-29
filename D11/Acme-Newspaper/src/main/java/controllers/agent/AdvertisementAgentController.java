
package controllers.agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdvertisementService;
import services.NewspaperService;
import controllers.AbstractController;
import domain.Advertisement;

@Controller
@RequestMapping("/advertisement/agent")
public class AdvertisementAgentController extends AbstractController {

	// Services
	@Autowired
	private AdvertisementService	advertisementService;

	@Autowired
	private NewspaperService		newspaperService;


	// Constructor
	public AdvertisementAgentController() {
		super();
	}

	//List
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false, defaultValue = "1") final Integer page) {
		ModelAndView result;
		Page<Advertisement> advertisements;

		advertisements = this.advertisementService.findByAgentId(page, 5);
		Assert.notNull(advertisements);

		result = new ModelAndView("advertisement/list");
		result.addObject("pageNumber", advertisements.getTotalPages());
		result.addObject("page", page);
		result.addObject("action", "agent-edit");
		result.addObject("advertisements", advertisements.getContent());
		result.addObject("requestURI", "advertisement/agent/list.do");

		return result;
	}

	//Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Advertisement advertisement;

		advertisement = this.advertisementService.create();

		result = this.createEditModelAndView(advertisement);

		return result;

	}

	// Edit
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int advertisementId) {
		ModelAndView result;
		Advertisement advertisement;

		advertisement = this.advertisementService.findOne(advertisementId);
		Assert.notNull(advertisement);

		result = this.createEditModelAndView(advertisement);

		return result;
	}

	//Link/Unlink
	@RequestMapping(value = "/listLink", method = RequestMethod.GET)
	public ModelAndView listLink(@RequestParam final int newspaperId, @RequestParam(required = false, defaultValue = "1") final Integer page) {
		ModelAndView result;
		Page<Advertisement> advertisements;

		Assert.isTrue(newspaperId != 0);

		advertisements = this.advertisementService.findByAgentIdLinkToNewspaper(newspaperId, page, 5);

		result = new ModelAndView("advertisement/list");
		result.addObject("pageNumber", advertisements.getTotalPages());
		result.addObject("page", page);
		result.addObject("action", "agent-link");
		result.addObject("newspaperId", newspaperId);
		result.addObject("advertisements", advertisements.getContent());
		result.addObject("requestURI", "advertisement/agent/listLink.do");

		return result;
	}

	@RequestMapping(value = "/listUnlink", method = RequestMethod.GET)
	public ModelAndView listUnlink(@RequestParam final int newspaperId, @RequestParam(required = false, defaultValue = "1") final Integer page) {
		ModelAndView result;
		Page<Advertisement> advertisements;

		Assert.isTrue(newspaperId != 0);

		advertisements = this.advertisementService.findByAgentIdUnlinkToNewspaper(newspaperId, page, 5);

		result = new ModelAndView("advertisement/list");
		result.addObject("pageNumber", advertisements.getTotalPages());
		result.addObject("page", page);
		result.addObject("action", "agent-unlink");
		result.addObject("newspaperId", newspaperId);
		result.addObject("advertisements", advertisements.getContent());
		result.addObject("requestURI", "advertisement/agent/listUnlink.do");

		return result;
	}

	@RequestMapping(value = "/link", method = RequestMethod.GET)
	public ModelAndView link(@RequestParam final int advertisementId, @RequestParam final int newspaperId) {
		ModelAndView result;

		Assert.isTrue(advertisementId != 0);
		Assert.isTrue(newspaperId != 0);

		this.newspaperService.addAdvertisementToNewspaper(advertisementId, newspaperId);

		result = new ModelAndView("redirect:list.do");

		return result;
	}

	@RequestMapping(value = "/unlink", method = RequestMethod.GET)
	public ModelAndView unlink(@RequestParam final int advertisementId, @RequestParam final int newspaperId) {
		ModelAndView result;

		Assert.isTrue(advertisementId != 0);
		Assert.isTrue(newspaperId != 0);

		this.newspaperService.deleteAdvertisementToNewspaper(advertisementId, newspaperId);

		result = new ModelAndView("redirect:list.do");

		return result;
	}

	//Post Methods
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Advertisement advertisement, final BindingResult binding) {
		ModelAndView result;

		advertisement = this.advertisementService.reconstruct(advertisement, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(advertisement);
		else
			try {
				this.advertisementService.save(advertisement);

				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(advertisement, "advertisement.commit.error");
			}

		return result;
	}

	// Delete
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Advertisement advertisement, final BindingResult binding) {
		ModelAndView result;

		try {
			this.advertisementService.delete(advertisement);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(advertisement, "advertisement.commit.error");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Advertisement advertisement) {
		ModelAndView result;

		result = this.createEditModelAndView(advertisement, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Advertisement advertisement, final String messageCode) {
		ModelAndView result;

		if (advertisement.getId() == 0)
			result = new ModelAndView("advertisement/create");
		else
			result = new ModelAndView("advertisement/edit");

		result.addObject("advertisement", advertisement);
		result.addObject("message", messageCode);

		return result;
	}

}
