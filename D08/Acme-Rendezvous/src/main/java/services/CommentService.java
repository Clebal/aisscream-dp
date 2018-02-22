
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CommentRepository;
import security.Authority;
import security.LoginService;
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

		rsvp = this.rsvpService.findByAttendantUserIdAndRendezvousId(comment.getUser().getId(), comment.getRendezvous().getId());
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

}
