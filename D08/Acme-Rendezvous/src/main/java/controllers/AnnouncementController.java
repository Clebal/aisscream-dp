package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AnnouncementService;
import controllers.AbstractController;
import domain.Announcement;

@Controller
@RequestMapping("/announcement")
public class AnnouncementController extends AbstractController {

	// Services
	@Autowired
	private AnnouncementService announcementService;
	
	// Constructor
	public AnnouncementController() {
		super();
	}
	
	// List
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int rendezvousId, @RequestParam(required = false) Integer page) {
		ModelAndView result;
		Collection<Announcement> announcements;
		Integer size;
		
		size = 5;
		if (page == null) page = 1;
		
		announcements = this.announcementService.findByRendezvousId(rendezvousId, page, size);
		Assert.notNull(announcements);
		
		result = super.paginateModelAndView("announcement/list", this.announcementService.countByRendezvousId(rendezvousId), page, size);
		result.addObject("requestURI", "announcement/list.do");
		result.addObject("announcements", announcements);
		result.addObject("rendezvousId", rendezvousId);
		
		return result;
	}
	
}
