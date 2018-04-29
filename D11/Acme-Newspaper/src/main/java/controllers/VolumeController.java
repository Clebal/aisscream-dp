
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
import services.CustomerService;
import services.SubscriptionVolumeService;
import services.VolumeService;
import domain.Volume;

@Controller
@RequestMapping("/volume")
public class VolumeController extends AbstractController {

	// Services

	@Autowired
	private VolumeService				volumeService;

	@Autowired
	private CustomerService				customerService;

	@Autowired
	private SubscriptionVolumeService	subscriptionVolumeService;


	// Constructor
	public VolumeController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int volumeId) {
		ModelAndView result;
		Volume volume;

		volume = this.volumeService.findOne(volumeId);

		Assert.notNull(volume);
		result = this.createDisplayModelAndView(volume);

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false, defaultValue = "1") final Integer page) {
		ModelAndView result;
		Page<Volume> volumes;

		volumes = this.volumeService.findAllPaginated(page, 5);
		Assert.notNull(volumes);

		result = new ModelAndView("volume/list");
		result.addObject("pageNumber", volumes.getTotalPages());
		result.addObject("page", page);
		result.addObject("volumes", volumes.getContent());
		result.addObject("requestURI", "volume/list.do");

		return result;
	}

	//Ancillary methods -----------------------
	protected ModelAndView createDisplayModelAndView(final Volume volume) {
		ModelAndView result;
		Boolean canCreateVolumeSubscription;
		Authority authority;

		canCreateVolumeSubscription = false;
		authority = new Authority();
		authority.setAuthority("CUSTOMER");

		if (LoginService.isAuthenticated() && LoginService.getPrincipal().getAuthorities().contains(authority))
			if (this.subscriptionVolumeService.findByCustomerIdAndVolumeId(this.customerService.findByUserAccountId(LoginService.getPrincipal().getId()).getId(), volume.getId()) == null)
				canCreateVolumeSubscription = true;

		result = new ModelAndView("volume/display");

		result.addObject("volume", volume);
		result.addObject("canCreateVolumeSubscription", canCreateVolumeSubscription);

		return result;

	}

}
