package controllers.user;

import java.util.Collection;

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
	public ModelAndView list(@RequestParam(required=false) Integer page) {
		ModelAndView result;
		Collection<Announcement> announcements;
		Integer size;
		
		size = 5;
		if(page == null) page = 1;
		
		announcements = this.announcementService.findByCreatorUserAccountId(LoginService.getPrincipal().getId(), page, size);
		Assert.notNull(announcements);
		
		result = super.paginateModelAndView("announcement/list", this.announcementService.countByCreatorUserAccountId(LoginService.getPrincipal().getId()), page, size);
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
		
        // Solo puede crearlo el creator
        Assert.isTrue(rendezvous.getCreator().getUserAccount().equals(LoginService.getPrincipal()));
		
        // No puede crearse si está borrado el rendezvous
        Assert.isTrue(!rendezvous.getIsDeleted());
        
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
		
        // Solo puede editarlo el creator
        Assert.isTrue(announcement.getRendezvous().getCreator().getUserAccount().equals(LoginService.getPrincipal()));

		result = this.createEditModelAndView(announcement);
		
		return result;
	}
	
	// Edit
	@RequestMapping(value="/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Announcement announcement, BindingResult binding) {
		ModelAndView result;
		
		announcement = this.announcementService.reconstruct(announcement, binding);
		if(binding.hasErrors()){
			result = this.createEditModelAndView(announcement);
		}else{
			try {
				this.announcementService.save(announcement);
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(announcement, "announcement.commit.error");
			}
		}
		
		return result;
	}
	
	// Delete
	@RequestMapping(value="/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Announcement announcement, BindingResult binding) {
		ModelAndView result;
		
		announcement = this.announcementService.reconstruct(announcement, binding);
		
		try {
			this.announcementService.delete(announcement);
			result = new ModelAndView("redirect:list.do");
		} catch (Throwable oops) {
			result = this.createEditModelAndView(announcement, "announcement.commit.error");
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
