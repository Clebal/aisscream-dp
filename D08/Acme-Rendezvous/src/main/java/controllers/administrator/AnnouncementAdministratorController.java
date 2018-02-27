package controllers.administrator;

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
@RequestMapping("/announcement/administrator")
public class AnnouncementAdministratorController extends AbstractController {

	// Services
	@Autowired
	private AnnouncementService announcementService;
	
	// Constructor
	public AnnouncementAdministratorController() {
		super();
	}
	
	// List
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required=false) Integer page) {
		ModelAndView result;
		Collection<Announcement> announcements;
		Integer size;
		
		size = 5;
		if(page == null) page = 1;
		
		announcements = this.announcementService.findAll(page, size);
		Assert.notNull(announcements);
		
		result = super.paginateModelAndView("announcement/list", this.announcementService.countAll(), page, size);
		result.addObject("requestURI", "announcement/administrator/list.do");
		result.addObject("announcements", announcements);
		
		return result;
	}
	
	// Delete
	@RequestMapping(value="/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int announcementId) {
		ModelAndView result;
        Announcement announcement;
		
        announcement = this.announcementService.findOne(announcementId);
        Assert.notNull(announcement);
        
    	this.announcementService.delete(announcement);
    	
		result = new ModelAndView("redirect:list.do");
		
		return result;
	}
	
}
