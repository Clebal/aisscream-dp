package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Announcement;
import domain.Rendezvous;

import repositories.AnnouncementRepository;
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
	
	// Other business methods
	public Collection<Announcement> findByRendezvousId(final int rendezvousId) {
		Collection<Announcement> result;
		
		Assert.isTrue(rendezvousId != 0);
		
		result = this.announcementRepository.findByRendezvousId(rendezvousId);
		
		return result;
	}
	
	public Collection<Announcement> findByCreatorUserId(final int userId) {
		Collection<Announcement> result;
		
		Assert.isTrue(userId != 0);
		
		result = this.announcementRepository.findByCreatorUserId(userId);
		
		return result;
	}
	
	public Collection<Announcement> findByCreatorUserAccountId(final int userAccountId) {
		Collection<Announcement> result;
		
		Assert.isTrue(userAccountId != 0);
		
		result = this.announcementRepository.findByCreatorUserAccountId(userAccountId);
		
		return result;
	}
}
