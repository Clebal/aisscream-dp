package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Rsvp;
import domain.Rendezvous;
import domain.User;

import repositories.RsvpRepository;
import security.Authority;
import security.LoginService;

@Service
@Transactional
public class RsvpService {

	// Managed repository
	@Autowired
	private RsvpRepository rspvRepository;
	
	// Supporting services
	@Autowired
	private UserService userService;
	
	// Constructor
	public RsvpService() {
		super();
	}
	
	// Simple CRUD methods
	public Rsvp create(final Rendezvous rendezvous) {
		Rsvp result;
		User attendant;
		
		attendant = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(attendant);
		
		result = new Rsvp();
		result.setAttendant(attendant);
		result.setRendezvous(rendezvous);
		result.setStatus("ACCEPTED");
		
		return result;
	}
	
	public Collection<Rsvp> findAll() {
		Collection<Rsvp> result;
		
		result = this.rspvRepository.findAll();
		
		return result;
	}
	
	public Rsvp findOne(final int rsvpId) {
		Rsvp result;
		
		Assert.isTrue(rsvpId != 0);
		
		result = this.rspvRepository.findOne(rsvpId);
		
		return result;
	}
	
	public Rsvp save(final Rsvp rsvp) {
		Rsvp result;
		
		Assert.notNull(rsvp);
		Assert.isTrue(rsvp.getAttendant().getUserAccount().equals(LoginService.getPrincipal()));
		
		if(rsvp.getId() == 0) {
			Assert.isTrue(this.rspvRepository.findByAttendantUserIdAndRendezvousId(rsvp.getAttendant().getId(), rsvp.getRendezvous().getId()) == null);
		}
		
		result = this.rspvRepository.save(rsvp);
		
		return result;
	}
	
	public Rsvp saveFromCreator(final Rsvp rsvp) {
		Rsvp result;
		
		Assert.notNull(rsvp);
		Assert.isTrue(rsvp.getAttendant().getUserAccount().equals(LoginService.getPrincipal()));
		
		result = this.rspvRepository.save(rsvp);
		
		return result;
	}
	
	public void deleteFromRendezvous(final Rsvp rsvp) {
		Authority authority;
		
		authority = new Authority();
		authority.setAuthority("ADMIN");
		
		Assert.notNull(rsvp);
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		
		this.rspvRepository.delete(rsvp);
		
	}
	
	// Other business methods
	public Collection<Rsvp> findByAttendantUserId(final int userId) {
		Collection<Rsvp> result;
		
		Assert.isTrue(userId != 0);
		Assert.isTrue(this.userService.findOne(userId).getUserAccount().equals(LoginService.getPrincipal()));

		result = this.rspvRepository.findByAttendantUserId(userId);
		
		return result;
	}
	
	public Collection<Rsvp> findByRendezvousId(final int rendezvousId){
		Collection<Rsvp> result;
		
		Assert.isTrue(rendezvousId != 0);
		
		result = this.rspvRepository.findByRendezvousId(rendezvousId);
		
		return result;
	}
	
	public Collection<Rsvp> findByCreatorId(final int userId){
		Collection<Rsvp> result;
		
		Assert.isTrue(userId != 0);
		Assert.isTrue(this.userService.findOne(userId).getUserAccount().equals(LoginService.getPrincipal()));
		
		result = this.rspvRepository.findByCreatorId(userId);
		
		return result;
	}
	
	public Collection<Rsvp> findByCreatorUserAccountId(final int userAccountId){
		Collection<Rsvp> result;
		
		Assert.isTrue(userAccountId != 0);
		Assert.isTrue(this.userService.findByUserAccountId(userAccountId).getUserAccount().equals(LoginService.getPrincipal()));
		
		result = this.rspvRepository.findByCreatorUserAccountId(userAccountId);
		
		return result;
	}
	
	public Rsvp findByAttendantUserIdAndRendezvousId(final int userId, final int rendezvousId){
		Rsvp result;
		
		Assert.isTrue(userId != 0 && rendezvousId != 0);
		
		result = this.rspvRepository.findByAttendantUserIdAndRendezvousId(userId, rendezvousId);
		
		return result;
	}
	
}
