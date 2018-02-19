package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.RSVP;
import domain.Rendezvous;
import domain.User;

import repositories.RSVPRepository;
import security.LoginService;

@Service
@Transactional
public class RSVPService {

	// Managed repository
	@Autowired
	private RSVPRepository rspvRepository;
	
	// Supporting services
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AnswerService answerService;
	
	// Constructor
	public RSVPService() {
		super();
	}
	
	// Simple CRUD methods
	public RSVP create(final User attendant, final Rendezvous rendezvous) {
		RSVP result;
		
		result = new RSVP();
		result.setAttendant(attendant);
		result.setRendezvous(rendezvous);
		
		return result;
	}
	
	public Collection<RSVP> findAll() {
		Collection<RSVP> result;
		
		result = this.rspvRepository.findAll();
		
		return result;
	}
	
	public RSVP findOne(final int rsvpId) {
		RSVP result;
		
		Assert.isTrue(rsvpId != 0);
		
		result = this.rspvRepository.findOne(rsvpId);
		
		return result;
	}
	
	public RSVP save(final RSVP rsvp) {
		RSVP result;
		
		Assert.notNull(rsvp);
		Assert.isTrue(rsvp.getAttendant().getUserAccount().equals(LoginService.getPrincipal()));
		
		if(rsvp.getId() == 0) {
			Assert.isTrue(this.questionService.findByRendezvousId(rsvp.getRendezvous().getId()).size() == this.answerService.findByRendezvousIdAndUserId().size());
		}
		
		result = this.rspvRepository.save(rsvp);
		
		return result;
	}
	
	public RSVP saveFromCreator(final RSVP rsvp) {
		RSVP result;
		
		Assert.notNull(rsvp);
		Assert.isTrue(rsvp.getAttendant().getUserAccount().equals(LoginService.getPrincipal()));
		
		result = this.rspvRepository.save(rsvp);
		
		return result;
	}
	
	// Other business methods
	public Collection<RSVP> findByAttendandUserId(final int userId) {
		Collection<RSVP> result;
		
		Assert.isTrue(userId != 0);
		Assert.isTrue(this.userService.findOne(userId).getUserAccount().equals(LoginService.getPrincipal()));

		result = this.rspvRepository.findByAttendandUserId(userId);
		
		return result;
	}
	
	public Collection<RSVP> findByRendezvousId(final int rendezvousId){
		Collection<RSVP> result;
		
		Assert.isTrue(rendezvousId != 0);
		
		result = this.rspvRepository.findByRendezvousId(rendezvousId);
		
		return result;
	}
	
	public Collection<RSVP> findByRendezvousCreatorId(final int userId){
		Collection<RSVP> result;
		
		Assert.isTrue(userId != 0);
		Assert.isTrue(this.userService.findOne(userId).getUserAccount().equals(LoginService.getPrincipal()));
		
		result = this.rspvRepository.findByRendezvousCreatorId(userId);
		
		return result;
	}
	
}
