package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Announcement;
import domain.Rendezvous;

import repositories.AnnouncementRepository;
import security.Authority;
import security.LoginService;

@Service
@Transactional
public class AnnouncementService {

	// Managed repository
	@Autowired
	private AnnouncementRepository announcementRepository;
	
	// Supporting services
	@Autowired
	private Validator			validator;
	
	// Constructor
	public AnnouncementService() {
		super();
	}
	
	// Simple CRUD methods
	public Announcement create(final Rendezvous rendezvous) {
		Announcement result;
		
        // Solo puede crearlo el creator
        Assert.isTrue(rendezvous.getCreator().getUserAccount().equals(LoginService.getPrincipal()));
		
        // No puede crearse si est� borrado el rendezvous
        Assert.isTrue(!rendezvous.getIsDeleted());
		
		result = new Announcement();
		result.setRendezvous(rendezvous);
		result.setMoment(new Date(System.currentTimeMillis() - 1));
		
		return result;
	}
	
	public Collection<Announcement> findAll() {
		Collection<Announcement> result;
		
		result = this.announcementRepository.findAll();
		
		return result;
	}
	
	public Announcement findOne(final int announcementId) {
		Announcement result;
		
		Assert.isTrue(announcementId != 0);
				
		result = this.announcementRepository.findOne(announcementId);
		
		return result;
	}
	
	public Announcement findOneToEdit(final int announcementId) {
		Announcement result;
		
		Assert.isTrue(announcementId != 0);
				
		result = this.announcementRepository.findOne(announcementId);
		
		// Solo puede editarlo el creator
        Assert.isTrue(result.getRendezvous().getCreator().getUserAccount().equals(LoginService.getPrincipal()));
		
		return result;
		
	}
	 
	public Announcement save(final Announcement announcement) {
		Announcement result, saved;
		Calendar calendar;
		Authority authority;
		
		Assert.notNull(announcement);
		Assert.isTrue(announcement.getRendezvous().getCreator().getUserAccount().equals(LoginService.getPrincipal()));
		
		authority = new Authority();
		authority.setAuthority("USER");
		
		if(announcement.getId() == 0){
			calendar = Calendar.getInstance();
			calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), 0);
			calendar.getTime().setTime(calendar.getTimeInMillis() - 1);
			announcement.setMoment(calendar.getTime());
			
			// No puedo crear un announcement si el rendezvous est� borrado
			Assert.isTrue(!announcement.getRendezvous().getIsDeleted());
			
			// Solo puede ser creado por un USER
			Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		}
		
		if(announcement.getId() != 0) {
			saved = this.announcementRepository.findOne(announcement.getId());
			Assert.isTrue(saved.getMoment().compareTo(announcement.getMoment()) == 0);
		}

		result = this.announcementRepository.save(announcement);
		
		return result;
	}
	
	public void delete(final Announcement announcement) {
		Authority authority;
		Announcement savedAnnouncement;

		authority = new Authority();
		authority.setAuthority("ADMIN");
		
		Assert.notNull(announcement);
		
		savedAnnouncement = this.announcementRepository.findOne(announcement.getId());
		
		// Solo puede ser borrado por el administrador o por el creador del announcement
		Assert.isTrue(savedAnnouncement.getRendezvous().getCreator().getUserAccount().equals(LoginService.getPrincipal()) || LoginService.getPrincipal().getAuthorities().contains(authority));
		
		this.announcementRepository.delete(savedAnnouncement);
		
	}
	
	public void deleteFromRendezvous(final Announcement announcement) {
		Authority authority;
		
		authority = new Authority();
		authority.setAuthority("ADMIN");
		
		Assert.notNull(announcement);
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		
		this.announcementRepository.delete(announcement);
	}
	
	// Other business methods
	public Collection<Announcement> findByRendezvousId(final int rendezvousId, final int page, final int size) {
		Collection<Announcement> result;
		
		Assert.isTrue(rendezvousId != 0);
		
		result = this.announcementRepository.findByRendezvousId(rendezvousId, this.getPageable(page, size)).getContent();
		
		return result;
	}
	
	public Integer countByRendezvousId(final int rendezvousId) {
		Integer result;
		
		Assert.isTrue(rendezvousId != 0);
		
		result = this.announcementRepository.countByRendezvousId(rendezvousId);
		
		return result;
	}
	
	public Collection<Announcement> findByCreatorUserAccountId(final int userAccountId, final int page, final int size) {
		Collection<Announcement> result;
		Authority authority;
		
		Assert.isTrue(userAccountId != 0);
		
		// Solo puede ser accedido por un actor de tipo USER
		authority = new Authority();
		authority.setAuthority("USER");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		
		result = this.announcementRepository.findByCreatorUserAccountId(userAccountId, this.getPageable(page, size)).getContent();
		
		return result;
	}
	
	public Integer countByCreatorUserAccountId(final int userAccountId) {
		Integer result;
		
		Assert.isTrue(userAccountId != 0);
		
		result = this.announcementRepository.countByCreatorUserAccountId(userAccountId);
		
		return result;
	}
		
	public Double[] avgStandartDerivationAnnouncementPerRendezvous() {
		Double[] result;
		
		result = this.announcementRepository.avgStandartDerivationAnnouncementPerRendezvous();
		
		return result;
	}
	
	public Collection<Announcement> findAll(final Integer page, final Integer size) {
		Collection<Announcement> result;
		Authority authority;
		
		// Solo puede ser accedido por un administrador
		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		
		result = this.announcementRepository.findAll(this.getPageable(page, size)).getContent();
		
		return result;
	}
	
	public Integer countAll() {
		Integer result;
		
		result = this.announcementRepository.countAll();
		
		return result;
	}
	
	// Auxiliary methods
	private Pageable getPageable(final int page, final int size) {
		Pageable result;
		
		if (page == 0 || size <= 0)
			result = new PageRequest(0, 5);
		else
			result = new PageRequest(page - 1, size);
		
		return result;
	}
	
	public void flush(){
		this.announcementRepository.flush();
	}
	
	// Pruned object domain
	public Announcement reconstruct(final Announcement announcement, final BindingResult binding) {
		Announcement aux;

		if(announcement.getId() != 0) {
			aux = this.announcementRepository.findOne(announcement.getId());
			announcement.setVersion(aux.getVersion());
			announcement.setRendezvous(aux.getRendezvous());
		}
		
		this.validator.validate(announcement, binding);

		return announcement;
	}
	
}
