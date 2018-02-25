package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
	
	// Constructor
	public AnnouncementService() {
		super();
	}
	
	// Simple CRUD methods
	public Announcement create(final Rendezvous rendezvous) {
		Announcement result;
		
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
	
	public Announcement save(final Announcement announcement) {
		Announcement result;
		
		Assert.notNull(announcement);
		Assert.isTrue(announcement.getRendezvous().getCreator().getUserAccount().equals(LoginService.getPrincipal()));
		
		result = this.announcementRepository.save(announcement);
		
		return result;
	}
	
	public void delete(final Announcement announcement) {
		
		Assert.notNull(announcement);
		Assert.isTrue(announcement.getRendezvous().getCreator().getUserAccount().equals(LoginService.getPrincipal()));
		
		this.announcementRepository.delete(announcement);
		
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
		
		Assert.isTrue(userAccountId != 0);
		
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
	
	// Auxiliar methods
	private Pageable getPageable(final int page, final int size) {
		Pageable result;
		
		if (page == 0 || size <= 0)
			result = new PageRequest(0, 5);
		else
			result = new PageRequest(page - 1, size);
		
		return result;
	}
	
}
