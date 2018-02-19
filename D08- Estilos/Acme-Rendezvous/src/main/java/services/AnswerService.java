
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AnswerRepository;
import security.LoginService;
import domain.Answer;
import domain.Question;
import domain.RSVP;
import domain.User;

@Service
@Transactional
public class AnswerService {

	// Managed repository
	@Autowired
	private AnswerRepository	answerRepository;
	select cast((count(final DISTINTC r.creator)) as float)/((select count(u) from User u)-(count(DISTINTC r.creator)) from Rendezvous r
	
	select ((select count(u) from User u)-(count(final DISTINTC r.creator)) from Rendezvous r;
	// Services
	@Autowired
	private UserService			userService;


	// Constructor
	public AnswerService() {
		super();
	}

	// Simple CRUD methods----------
	public Answer create(final Question question, final RSVP rsvp) {
		Answer result;
		final User user;

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

	public Answer findByRSVPIdAndQuestionId(final int RSVPId, final int questionId) {
		Answer result;

		Assert.isTrue(RSVPId != 0 && questionId != 0);
		result = this.answerRepository.findByRSVPIdAndQuestionId(RSVPId, questionId);

		return result;
	}

}
