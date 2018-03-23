
package controllers.administrator;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.RendezvousService;
import controllers.AbstractController;
import domain.Rendezvous;

@Controller
@RequestMapping("/rendezvous/administrator")
public class RendezvousAdministratorController extends AbstractController {

	// Services
	@Autowired
	private RendezvousService	rendezvousService;


	// Constructor
	public RendezvousAdministratorController() {
		super();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Rendezvous rendezvous;

		rendezvous = this.rendezvousService.findOne(rendezvousId);
		Assert.notNull(rendezvous);

		this.rendezvousService.virtualDelete(rendezvous);

		result = new ModelAndView("redirect:/rendezvous/display.do?rendezvousId=" + rendezvousId);

		return result;
	}

	@RequestMapping(value = "/listByNumberAnnouncements", method = RequestMethod.GET)
	public ModelAndView listByNumberAnnouncements(@RequestParam(required = false) final Integer page) {
		ModelAndView result;
		Collection<Rendezvous> rendezvouses;
		Boolean canPermit;
		Boolean canLink;
		Boolean canUnLink;
		Integer pageNumber;
		Integer size;
		Boolean haveRendezvousId;
		Integer pageAux;

		if (page == null)
			pageAux = 1;
		else
			pageAux = page;

		haveRendezvousId = false;
		canPermit = false;
		canLink = false;
		canUnLink = false;
		rendezvouses = new ArrayList<Rendezvous>();
		size = 5;
		pageNumber = 0;

		rendezvouses = this.rendezvousService.rendezvousesNumberAnnouncementsPlus75Percentage(pageAux, size);

		pageNumber = this.rendezvousService.countRendezvousesNumberAnnouncementsPlus75Percentage();

		result = new ModelAndView("rendezvous/list");
		pageNumber = (int) Math.floor(((pageNumber / (size + 0.0)) - 0.1) + 1);

		result.addObject("pageNumber", pageNumber);
		result.addObject("page", pageAux);
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestURI", "rendezvous/administrator/listByNumberAnnouncements.do");
		result.addObject("canPermit", canPermit);
		result.addObject("canLink", canLink);
		result.addObject("canUnLink", canUnLink);
		result.addObject("haveRendezvousId", haveRendezvousId);

		return result;
	}

	@RequestMapping(value = "/listByLinkedNumber", method = RequestMethod.GET)
	public ModelAndView listByLinkedNumber(@RequestParam(required = false) final Integer page) {
		ModelAndView result;
		Collection<Rendezvous> rendezvouses;
		Boolean canPermit;
		Boolean canLink;
		Boolean canUnLink;
		Integer pageNumber;
		Integer size;
		Boolean haveRendezvousId;
		Integer pageAux;

		if (page == null)
			pageAux = 1;
		else
			pageAux = page;

		haveRendezvousId = false;
		canPermit = false;
		canLink = false;
		canUnLink = false;
		rendezvouses = new ArrayList<Rendezvous>();
		size = 5;
		pageNumber = 0;

		rendezvouses = this.rendezvousService.rendezvousesLinkedMoreAvgPlus10Percentage(pageAux, size);

		pageNumber = this.rendezvousService.countRendezvousesLinkedMoreAvgPlus10Percentage();

		result = new ModelAndView("rendezvous/list");
		pageNumber = (int) Math.floor(((pageNumber / (size + 0.0)) - 0.1) + 1);

		result.addObject("pageNumber", pageNumber);
		result.addObject("page", pageAux);
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestURI", "rendezvous/administrator/listByLinkedNumber.do");
		result.addObject("canPermit", canPermit);
		result.addObject("canLink", canLink);
		result.addObject("canUnLink", canUnLink);
		result.addObject("haveRendezvousId", haveRendezvousId);

		return result;
	}
}
