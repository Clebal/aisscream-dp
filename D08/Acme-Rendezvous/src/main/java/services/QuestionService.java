package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Question;
import domain.Rendezvous;

import repositories.QuestionRepository;
import security.Authority;
import security.LoginService;

@Service
@Transactional
public class QuestionService {

	// Managed repository
	@Autowired
	private QuestionRepository questionRepository;
	
	// Supporting services
	
	// Constructor
	public QuestionService(){
		super();
	}
	
	// Simple CRUD methods
	public Question create(final Rendezvous rendezvous) {
		Question result;
		
		result = new Question();
		result.setRendezvous(rendezvous);
		
		return result;
	}
	
	public Collection<Question> findAll() {
		Collection<Question> result;
		
		result = this.questionRepository.findAll();
		
		return result;
	}
	
	public Question findOne(final int questionId) {
		Question result;
		
		Assert.isTrue(questionId != 0);
		
		result = this.questionRepository.findOne(questionId);
		
		return result;
	}
	
	public Question save(final Question question) {
		Question result;
		
		Assert.notNull(question);
		// El creador del rendezvous debe ser el que cree las cuestiones
		Assert.isTrue(question.getRendezvous().getCreator().getUserAccount().equals(LoginService.getPrincipal()));
		
		result = this.questionRepository.save(question);
		
		return result;
	}
	
	// Other business methods
	public Collection<Question> findByCreatorUserId(final int userId) {
		Collection<Question> result;
		
		Assert.isTrue(userId != 0);
		
		result = this.questionRepository.findByCreatorUserId(userId);
		
		return result;
	}
	
	public Collection<Question> findByCreatorUserAccountId(final int userAccountId) {
		Collection<Question> result;
		
		Assert.isTrue(userAccountId != 0);
		
		result = this.questionRepository.findByCreatorUserAccountId(userAccountId);
		
		return result;
	}
	
	public Collection<Question> findByRendezvousId(final int rendezvousId) {
		Collection<Question> result;
		
		Assert.isTrue(rendezvousId != 0);
		
		result = this.questionRepository.findByRendezvousId(rendezvousId);
		
		return result;
	}
	
	public Collection<Question> findByRsvpId(final int rsvpId) {
		Collection<Question> result;
		
		Assert.isTrue(rsvpId != 0);
		
		result = this.questionRepository.findByRsvpId(rsvpId);
		
		return result;
	}
	
	public double findAvgQuestionPerRendezvous() {
		double result;
		Authority authority;
		
		authority = new Authority();
		authority.setAuthority("ADMIN");
		
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		
		result = this.questionRepository.findAvgQuestionPerRendezvous();
		
		return result;
	}
	
	public double findStandardDerivationQuestionPerRendezvous() {
		double result;
		Authority authority;
		
		authority = new Authority();
		authority.setAuthority("ADMIN");
		
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		
		result = this.questionRepository.findStandardDerivationQuestionPerRendezvous();
		
		return result;
	}
	
	public double findAvgAnswerPerRendezvous() {
		double result;
		Authority authority;
		
		authority = new Authority();
		authority.setAuthority("ADMIN");
		
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		
		result = this.questionRepository.findAvgAnswerPerRendezvous();
		
		return result;
	}
	
	public double findStandardDerivationAnswerPerRendezvous() {
		double result;
		Authority authority;
		
		authority = new Authority();
		authority.setAuthority("ADMIN");
		
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		
		result = this.questionRepository.findStandardDerivationAnswerPerRendezvous();
		
		return result;
	}
	
	
}














