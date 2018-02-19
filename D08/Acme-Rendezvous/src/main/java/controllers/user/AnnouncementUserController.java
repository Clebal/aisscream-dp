package controllers.user;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.AnnouncementService;
import services.RendezvousService;

import controllers.AbstractController;
import domain.Announcement;
import domain.Rendezvous;

@Controller
@RequestMapping("/announcement/user")
public class AnnouncementUserController extends AbstractController {

	// Services
	@Autowired
	private AnnouncementService announcementService;
	
	@Autowired
	private RendezvousService rendezvousService;
	
	// Constructor
	public AnnouncementUserController() {
		super();
	}
	
	// List
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Announcement> announcements;
		
		announcements = this.announcementService.findByCreatorUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(announcements);
		
		result = new ModelAndView("announcement/list");
		result.addObject("requestURI", "announcement/user/list.do");
		result.addObject("announcements", announcements);
		
		return result;
	}
	
	// Create
	@RequestMapping(value="/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int rendezvousId) {
		ModelAndView result;
		Announcement announcement;
		Rendezvous rendezvous;
		
		rendezvous = this.rendezvousService.findOne(rendezvousId);
		Assert.notNull(rendezvous);
		
		announcement = this.announcementService.create(rendezvous);
		Assert.notNull(announcement);

		result = this.createEditModelAndView(announcement);
		
		return result;
	}
	
	// Edit
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int announcementId) {
		ModelAndView result;
		Announcement announcement;
				
		announcement = this.announcementService.findOne(announcementId);
		Assert.notNull(announcement);

		result = this.createEditModelAndView(announcement);
		
		return result;
	}
	
	// Edit
	@RequestMapping(value="/edit", method = RequestMethod.POST)
	public ModelAndView edit(@Valid Announcement announcement, BindingResult binding) {
		ModelAndView result;
		
		if(binding.hasErrors()){
			result = this.createEditModelAndView(announcement);
		}else{
			try {
				this.announcementService.save(announcement);
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				// result = super.panic(oops);
				result = this.createEditModelAndView(announcement, "announcement.commit.error");
			}
		}
		
		return result;
	}
	
	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Announcement announcement) {
		ModelAndView result;

		result = this.createEditModelAndView(announcement, null);

		return result;
	}
	
	protected ModelAndView createEditModelAndView(final Announcement announcement, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("announcement/edit");
		
		result.addObject("announcement", announcement);
		result.addObject("message", messageCode);

		return result;
	}
	
}
