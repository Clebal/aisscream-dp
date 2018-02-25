
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

import repositories.CommentRepository;
import security.Authority;
import security.LoginService;
import domain.Actor;
import domain.Comment;
import domain.Rendezvous;
import domain.Rsvp;
import domain.User;

@Service
@Transactional
public class CommentService {

	// Managed repository
	@Autowired
	private CommentRepository	commentRepository;

	// Services
	@Autowired
	private UserService			userService;

	@Autowired
	private RsvpService			rsvpService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private Validator			validator;


	// Constructor
	public CommentService() {
		super();
	}

	// Simple CRUD methods----------
	public Comment create(final Rendezvous rendezvous, final Comment repliedComment) {
		Comment result;
		User user;

		Assert.notNull(rendezvous);

		result = new Comment();

		//Guardamos usuario
		user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(user);
		result.setUser(user);

		//Guardamos comentario padre si lo hay
		if (repliedComment != null)
			result.setRepliedComment(repliedComment);

		result.setRendezvous(rendezvous);

		result.setMoment(new Date(System.currentTimeMillis() - 1));

		return result;
	}

	//	public Collection<Comment> findAll() {
	//		Collection<Comment> result;
	//
	//		result = this.commentRepository.findAll();
	//
	//		return result;
	//	}

	public Comment findOne(final int commentId) {
		Comment result;

		Assert.isTrue(commentId != 0);

		result = this.commentRepository.findOne(commentId);

		return result;
	}

	public Comment save(final Comment comment) {
		Comment result;
		User user;
		Rsvp rsvp;

		Assert.notNull(comment);
		Assert.notNull(comment.getUser());
		Assert.notNull(comment.getRendezvous());

		Assert.isTrue(comment.getId() == 0);

		user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(user);

		rsvp = this.rsvpService.findByAttendantUserIdAndRendezvousId(comment.getUser().getId(), comment.getRendezvous().getId());
		Assert.isTrue(rsvp != null || comment.getRendezvous().getCreator().equals(user));

		Assert.isTrue(comment.getUser().equals(user));

		if (comment.getRepliedComment() != null)
			Assert.isTrue(comment.getRepliedComment().getRendezvous().equals(comment.getRendezvous()));

		comment.setMoment(new Date(System.currentTimeMillis() - 1));

		result = this.commentRepository.save(comment);

		return result;

	}

	public void delete(final Comment comment) {
		final Authority authority;
		Comment commentForDelete;
		Integer size;

		authority = new Authority();
		authority.setAuthority("ADMIN");

		Assert.notNull(comment);

		//only can deleted it an admin
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		size = this.countByRepliedCommentId(comment.getId());

		//Delete repliedComments
		for (final Comment repliedComment : this.findByRepliedCommentId(comment.getId(), 1, size))
			this.delete(repliedComment);

		commentForDelete = this.findOne(comment.getId());
		this.commentRepository.delete(commentForDelete);

	}

	//Evitamos que se puedan ver los comentarios de un rendezvous que no se puede ver
	public Comment findOneToDisplay(final int commentId) {
		Comment result;
		Actor actor;
		Calendar birthDatePlus18Years;
		Boolean canPermit;

		Assert.isTrue(commentId != 0);

		result = this.commentRepository.findOne(commentId);

		if (LoginService.isAuthenticated()) {
			actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());
			birthDatePlus18Years = Calendar.getInstance();
			birthDatePlus18Years.setTime(actor.getBirthdate());
			birthDatePlus18Years.add(Calendar.YEAR, 18);
			if (birthDatePlus18Years.getTime().compareTo(new Date()) <= 0 || actor.getId() == result.getRendezvous().getCreator().getId())
				canPermit = true;
			else
				canPermit = false;
		} else
			canPermit = false;

		Assert.isTrue(result.getRendezvous().getAdultOnly() == false || result.getRendezvous().getAdultOnly() == true && canPermit);

		return result;
	}

	public Collection<Comment> findByUserId(final int userId) {
		Collection<Comment> result;

		Assert.isTrue(userId != 0);
		result = this.commentRepository.findByUserId(userId);

		return result;
	}

	public Collection<Comment> findByRendezvousIdAndNoRepliedComment(final int userId, final int page, final int size) {
		Collection<Comment> result;
		Pageable pageable;

		if (page == 0 || size <= 0)
			pageable = new PageRequest(0, 5);
		else
			pageable = new PageRequest(page - 1, size);

		Assert.isTrue(userId != 0);
		result = this.commentRepository.findByRendezvousIdAndNoRepliedComment(userId, pageable).getContent();

		return result;
	}

	public Collection<Comment> findByRepliedCommentId(final int repliedCommentId, final int page, final int size) {
		Collection<Comment> result;
		Pageable pageable;

		if (page == 0 || size <= 0)
			pageable = new PageRequest(0, 5);
		else
			pageable = new PageRequest(page - 1, size);

		Assert.isTrue(repliedCommentId != 0);
		result = this.commentRepository.findByRepliedCommentId(repliedCommentId, pageable).getContent();

		return result;
	}

	public Integer countByRepliedCommentId(final int commentId) {
		Integer result;

		Assert.isTrue(commentId != 0);
		result = this.commentRepository.countByRepliedCommentId(commentId);

		return result;
	}

	public Integer countByRendezvousIdAndNoRepliedComment(final int rendezvousId) {
		Integer result;

		Assert.isTrue(rendezvousId != 0);
		result = this.commentRepository.countByRendezvousIdAndNoRepliedComment(rendezvousId);

		return result;
	}

	public Comment reconstruct(final Comment comment, final BindingResult binding) {
		Comment result;
		Comment aux;

		aux = this.create(comment.getRendezvous(), comment.getRepliedComment());

		result = comment;

		result.setRendezvous(comment.getRendezvous());
		result.setUser(aux.getUser());
		result.setRepliedComment(comment.getRepliedComment());
		result.setMoment(comment.getMoment());
		result.setPicture(comment.getPicture());
		result.setText(comment.getText());

		this.validator.validate(result, binding);

		return result;
	}

	public Double[] avgStandardRepliesPerComment() {
		Double[] result;
		Authority authority;

		//Solo puede acceder admin
		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		result = this.commentRepository.avgStandardRepliesPerComment();

		return result;
	}
}
