
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CommentRepository;
import security.Authority;
import security.LoginService;
import domain.Comment;
import domain.RSVP;
import domain.Rendezvous;
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
		RSVP rsvp;

		Assert.notNull(comment);
		Assert.notNull(comment.getUser());
		Assert.notNull(comment.getRendezvous());

		//TODO Revisar orden
		rsvp = RSVPService.findByUserIdAndRendezvousId(comment.getUser().getId(), comment.getRendezvous().getId);
		Assert.notNull(rsvp);

		user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(user);

		Assert.isTrue(comment.getUser().equals(user));

		if (comment.getRepliedComment() != null)
			Assert.isTrue(comment.getRepliedComment().getRendezvous().equals(comment.getRendezvous()));

		comment.setMoment(new Date(System.currentTimeMillis() - 1));

		result = this.commentRepository.save(comment);

		return result;

	}

	public void delete(final Comment comment) {
		final Authority authority;
		final Rendezvous rendezvousForDelete;
		Comment commentForDelete;

		authority.setAuthority("ADMIN");

		Assert.notNull(comment);

		//only can deleted it an admin
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		//Delete repliedComments
		for (final Comment repliedComment : this.findByRepliedCommentId(comment.getId()))
			this.delete(repliedComment);

		commentForDelete = this.findOne(comment.getId());
		this.commentRepository.delete(commentForDelete);

	}

	public Collection<Comment> findByUserId(final int userId) {
		Collection<Comment> result;

		Assert.isTrue(userId != 0);
		result = this.commentRepository.findByUserId(userId);

		return result;
	}

	public Collection<Comment> findByRendezvousId(final int userId) {
		Collection<Comment> result;

		Assert.isTrue(userId != 0);
		result = this.commentRepository.findByRendezvousId(userId);

		return result;
	}

	public Collection<Comment> findByRepliedCommentId(final int userId) {
		Collection<Comment> result;

		Assert.isTrue(userId != 0);
		result = this.commentRepository.findByRepliedCommentId(userId);

		return result;
	}

}
