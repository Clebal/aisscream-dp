package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Question;
import domain.Rendezvous;

import repositories.QuestionRepository;
import security.LoginService;

@Service
@Transactional
public class QuestionService {

	// Managed repository
	@Autowired
	private QuestionRepository questionRepository;
	
	// Supporting services
	@Autowired
	private AnswerService answerService;
	
	// Constructor
	public QuestionService(){
		super();
	}
	
	// Simple CRUD methods
	public Question create(final Rendezvous rendezvous) {
		Question result;
		Collection<Question> questions;
		
		result = new Question();
		
		questions = this.questionRepository.findByRendezvousId(rendezvous.getId());
		result.setNumber(questions.size() + 1);
		
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
		Question result, saved;
		Collection<Question> questions;
		
		if(question.getId() == 0){
			questions = this.questionRepository.findByRendezvousId(question.getRendezvous().getId());
			question.setNumber(questions.size() + 1);
		}else{
			saved = this.questionRepository.findOne(question.getId());
			Assert.isTrue(saved.getNumber() == question.getNumber());
		}
		
		Assert.notNull(question);
		// El creador del rendezvous debe ser el que cree las cuestiones
		Assert.isTrue(question.getRendezvous().getCreator().getUserAccount().equals(LoginService.getPrincipal()));
		
		result = this.questionRepository.save(question);
		
		return result;
	}
	
	public void delete(final Question question) {
		
		Assert.notNull(question);
		
		for(final Answer a: this.answerService.findByQuestionId(question.getId()){
			this.answerService.delete(a);
		}
		
		this.questionRepository.delete(question);
		
	}
	
	// Other business methods
	
	public Collection<Question> findByCreatorUserAccountId(final int userAccountId, final int page, final int size) {
		Collection<Question> result;
		
		Assert.isTrue(userAccountId != 0);
		
		result = this.questionRepository.findByCreatorUserAccountId(userAccountId, this.getPageable(page, size)).getContent();
		
		return result;
	}
	
	public Integer countByCreatorUserAccountId(final int userAccountId) {
		Integer result;
		
		Assert.isTrue(userAccountId != 0);
		
		result = this.questionRepository.countByCreatorUserAccountId(userAccountId);
		
		return result;
	}
	
	public Collection<Question> findByRendezvousId(final int rendezvousId) {
		Collection<Question> result;
		
		Assert.isTrue(rendezvousId != 0);
		
		result = this.questionRepository.findByRendezvousId(rendezvousId);
		
		return result;
	}
	
	public Integer countByRendezvousId(final int rendezvousId) {
		Integer result;

		Assert.isTrue(rendezvousId != 0);

		result = this.questionRepository.countByRendezvousId(rendezvousId);

		return result;
	}
	
	public Double[] avgStandartDerivationQuestionsPerRendezvous() {
		Double[] result;
		
		result = this.questionRepository.avgStandartDerivationQuestionsPerRendezvous();
		
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














