
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AnswerRepository;
import security.LoginService;
import domain.Answer;
import domain.Question;
import domain.User;

@Service
@Transactional
public class AnswerService {

	// Managed repository
	@Autowired
	private AnswerRepository	answerRepository;

	// Services
	@Autowired
	private UserService			userService;

	@Autowired
	private Validator			validator;


	// Constructor
	public AnswerService() {
		super();
	}

	// Simple CRUD methods----------
	
	public Answer create(final Question question) {
		Answer result;

		Assert.notNull(question);

		result = new Answer();
		
		result.setCounter(0);
		result.setQuestion(question);

		return result;
	}

	public Collection<Answer> findAll() {
		Collection<Answer> result;

		result = this.answerRepository.findAll();

		return result;
	}

	public Answer findOne(final int answerId) {
		Answer result;

		Assert.isTrue(answerId != 0);

		result = this.answerRepository.findOne(answerId);

		return result;
	}

	public Answer save(final Answer answer) {
		Answer result;
		final Answer saved;
		final User user;

		Assert.notNull(answer);
		Assert.notNull(answer.getQuestion());

		// Solo puede realizarla su user
//		user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
//		Assert.notNull(user);

		// Solo hay una respuesta por pregunta y usuario
//		saved = this.answerRepository.findByQuestionIdAndUserAccountId(answer.getQuestion().getSurvey().getId(), user.getUserAccount().getId());
//		Assert.isTrue(saved == null || saved.equals(answer));

		result = this.answerRepository.save(answer);

		return result;

	}

	public void delete(final Answer answer) {
		User user;

		Assert.notNull(answer);

		// Lo borra el creador de la pregunta
		user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(user);

		this.answerRepository.delete(answer);

	}

	public void flush() {
		this.answerRepository.flush();
	}
	
	// Pruned object domain
	
	public Answer reconstruct(final Answer answer, final BindingResult binding) {
		Answer saved;

		if (answer.getId() != 0) {
			saved = this.answerRepository.findOne(answer.getId());
			Assert.notNull(saved);
			answer.setVersion(saved.getVersion());
			answer.setQuestion(saved.getQuestion());
		}

		this.validator.validate(answer, binding);

		return answer;
	}
	
	public Answer reconstructFromSurvey(final Answer answer, final Question question) {
		Answer result;
		
		result = this.create(question);
		result.setId(0);
		result.setVersion(0);
		result.setText(answer.getText());
		
		return result;
	}

	public Collection<Answer> saveAnswers(final Collection<Answer> answers) {
		final Collection<Answer> result;
		Answer saved;

		result = new ArrayList<Answer>();

		for (final Answer answer : answers) {
			saved = this.save(answer);
			result.add(saved);
		}

		return result;

	}
	
	public Collection<Answer> findByQuestionId(final int questionId) {
		Collection<Answer> result;

		Assert.isTrue(questionId != 0);
		result = this.answerRepository.findByQuestionId(questionId);

		return result;
	}

}
