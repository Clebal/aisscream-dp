
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
import security.Authority;
import security.LoginService;
import domain.Answer;
import domain.Question;
import domain.Rendezvous;
import domain.Rsvp;
import domain.User;
import forms.RsvpForm;

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
	private RsvpService			rsvpService;

	@Autowired
	private QuestionService		questionService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private Validator			validator;


	// Constructor
	public AnswerService() {
		super();
	}

	// Simple CRUD methods----------
	public Answer create(final Question question, final Rsvp rsvp) {
		Answer result;

		Assert.notNull(question);
		Assert.notNull(rsvp);

		result = new Answer();

		result.setQuestion(question);
		result.setRsvp(rsvp);

		return result;
	}

	//	public Collection<Comment> findAll() {
	//		Collection<Comment> result;
	//
	//		result = this.commentRepository.findAll();
	//
	//		return result;
	//	}

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
		Assert.notNull(answer.getRsvp());
		Assert.notNull(answer.getQuestion());

		//Responde el principal
		user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(user);
		Assert.isTrue(answer.getRsvp().getAttendant().equals(user));

		//La respuesta es para una pregunta de un rsvp de un rendezvous
		Assert.isTrue(answer.getRsvp().getRendezvous().equals(answer.getQuestion().getRendezvous()));

		//Solo hay una respuesta por pregunta y rsvp
		saved = this.answerRepository.findByRSVPIdAndQuestionId(answer.getRsvp().getId(), answer.getQuestion().getId());
		Assert.isTrue(saved == null || saved.equals(answer));

		result = this.answerRepository.save(answer);

		return result;

	}

	public void delete(final Answer answer) {
		User user;

		Assert.notNull(answer);

		//Lo borra el creador de la question
		user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(user);
		Assert.isTrue(answer.getQuestion().getRendezvous().getCreator().equals(user));

		this.answerRepository.delete(answer);

	}

	public Answer findByRSVPIdAndQuestionId(final int RSVPId, final int questionId) {
		Answer result;

		Assert.isTrue(RSVPId != 0 && questionId != 0);
		result = this.answerRepository.findByRSVPIdAndQuestionId(RSVPId, questionId);

		return result;
	}

	public Integer countRendezvousIdAndUserId(final int rendezvousId, final int userId) {
		Integer result;

		Assert.isTrue(rendezvousId != 0 && userId != 0);
		result = this.answerRepository.countRendezvousIdAndUserId(rendezvousId, userId);

		return result;
	}

	public Answer findByQuestionIdAndUserId(final int questionId, final int userId) {
		Answer result;

		Assert.isTrue(questionId != 0 && userId != 0);
		result = this.answerRepository.findByQuestionIdAndUserId(questionId, userId);

		return result;
	}

	public Collection<Answer> reconstruct(final RsvpForm rsvpForm, final BindingResult binding) {
		Collection<Answer> result;
		Answer auxAnswer;
		Question auxQuestion;
		Rsvp rsvp;
		Rendezvous rendezvous;

		rendezvous = this.rendezvousService.findOne(rsvpForm.getRendezvousId());
		Assert.notNull(rendezvous);

		result = new ArrayList<Answer>();

		//Comprobamos que vienen todas las preguntas
		Assert.isTrue(this.questionService.countByRendezvousId(rendezvous.getId()).equals(rsvpForm.getQuestions().keySet().size()) && this.questionService.countByRendezvousId(rendezvous.getId()).equals(rsvpForm.getAnswers().keySet().size()));

		//Creamos y guardamos el rsvp para poder a�adirlo a cada respuesta
		rsvp = this.rsvpService.create(rendezvous);

		rsvp = this.rsvpService.save(rsvp);

		for (final Integer questionId : rsvpForm.getQuestions().keySet()) {
			auxQuestion = this.questionService.findOne(questionId);
			Assert.notNull(auxQuestion);

			auxAnswer = this.create(auxQuestion, rsvp);
			auxAnswer.setText(rsvpForm.getAnswers().get(questionId));

			this.validator.validate(auxAnswer, binding);
			result.add(auxAnswer);
		}

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

	public Double[] avgStandardAnswerPerRendezvous() {
		Double[] result;
		Authority authority;

		//Solo puede acceder admin
		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		result = this.answerRepository.avgStandardAnswerPerRendezvous();

		return result;
	}

	public Collection<Answer> findByQuestionId(final int questionId) {
		Collection<Answer> result;

		Assert.isTrue(questionId != 0);
		result = this.answerRepository.findByQuestionId(questionId);

		return result;
	}

}
