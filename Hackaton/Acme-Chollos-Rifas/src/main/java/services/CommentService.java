
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import domain.Bargain;
import domain.Comment;
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
	private BargainService		bargainService;
	
	@Autowired
	private Validator			validator;


	// Constructor
	public CommentService() {
		super();
	}

	// Simple CRUD methods-----------------------------------
	
	public Comment create(final Bargain bargain, final Comment repliedComment) {
		Comment result;
		User user;

		Assert.notNull(bargain);

		result = new Comment();

		user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(user);
		result.setUser(user);

		if (repliedComment != null)
			result.setRepliedComment(repliedComment);

		result.setBargain(bargain);

		result.setMoment(new Date(System.currentTimeMillis() - 1));

		return result;
	}

	public Collection<Comment> findAll() {
		Collection<Comment> result;
	
		result = this.commentRepository.findAll();
	
		return result;
	}

	public Comment findOne(final int commentId) {
		Comment result;

		Assert.isTrue(commentId != 0);

		result = this.commentRepository.findOne(commentId);

		return result;
	}
	
	public Comment findOneToDisplay(final int commentId) {
		Comment result;

		Assert.isTrue(commentId != 0);

		result = this.commentRepository.findOne(commentId);

		return result;
	}

	public Comment save(final Comment comment) {
		Comment result;
		User user;
		final Authority authority;

		Assert.notNull(comment);
		Assert.notNull(comment.getUser());
		Assert.notNull(comment.getBargain());
		
		authority = new Authority();
		authority.setAuthority("USER");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		Assert.isTrue(comment.getId() == 0); // No se permitirán editar en principio

		user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(user);

		Assert.isTrue(comment.getUser().equals(user));

		if (comment.getRepliedComment() != null)
			Assert.isTrue(comment.getRepliedComment().getBargain().equals(comment.getBargain()));

		comment.setMoment(new Date(System.currentTimeMillis() - 1));

		result = this.commentRepository.save(comment);

		return result;

	}

	public void delete(final Comment comment) {
		final Authority authority;
		Comment commentForDelete;
		Integer size;

		authority = new Authority();
		authority.setAuthority("USER");

		Assert.notNull(comment);

		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		size = this.countByRepliedCommentId(comment.getId());

		//Delete repliedComments
		for (final Comment repliedComment : this.findByRepliedCommentId(comment.getId(), 1, size))
			this.delete(repliedComment);

		commentForDelete = this.findOne(comment.getId());
		this.commentRepository.delete(commentForDelete);

	}

	public void deleteAdmin(final Comment comment) {
		final Authority authority;
		Comment commentForDelete;
		Integer size;

		authority = new Authority();
		authority.setAuthority("ADMIN");

		Assert.notNull(comment);

		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		size = this.countByRepliedCommentId(comment.getId());

		//Delete repliedComments
		for (final Comment repliedComment : this.findByRepliedCommentId(comment.getId(), 1, size))
			this.delete(repliedComment);

		commentForDelete = this.findOne(comment.getId());
		this.commentRepository.delete(commentForDelete);

	}
	
	public void deleteFromBargain(final Bargain bargain) {
		Comment commentForDelete;
		Integer size;
		Collection<Comment> comments;

		Assert.isTrue(bargain.getId() != 0);
		
		comments = this.findByBargainId(bargain.getId(), 1, this.countByBargainId(bargain.getId())).getContent();

		for (Comment c : comments) { 
			size = this.countByRepliedCommentId(c.getId());
	
			//Delete repliedComments
			for (final Comment repliedComment : this.findByRepliedCommentId(c.getId(), 1, size))
				this.delete(repliedComment);
	
			commentForDelete = this.findOne(c.getId());
			this.commentRepository.delete(commentForDelete);
		}
	}
	
	public void flush() {
		this.commentRepository.flush();
	}

	public Page<Comment> findByUserId(final int userId, final int page, final int size) {
		Page<Comment> result;

		Assert.isTrue(userId != 0);
		result = this.commentRepository.findByUserId(userId, this.getPageable(page, size));

		return result;
	}
	
	public Integer countByUserId(final int userId) {
		Integer result;

		Assert.isTrue(userId != 0);
		result = this.commentRepository.countByUserId(userId);

		return result;
	}

	public Page<Comment> findByNoRepliedComment(final int page, final int size) {
		Page<Comment> result;

		result = this.commentRepository.findByNoRepliedComment(this.getPageable(page, size));

		return result;
	}

	public Page<Comment> findByRepliedCommentId(final int repliedCommentId, final int page, final int size) {
		Page<Comment> result;

		Assert.isTrue(repliedCommentId != 0);
		result = this.commentRepository.findByRepliedCommentId(repliedCommentId, this.getPageable(page, size));

		return result;
	}
	
	public Page<Comment> findByBargainIdAndNoRepliedComment(final int bargainId, final int page, final int size) {
		Page<Comment> result;

		Assert.isTrue(bargainId != 0);
		result = this.commentRepository.findByRepliedCommentId(bargainId, this.getPageable(page, size));

		return result;
	}
	
	public Integer countByBargainIdAndNoRepliedComment(final int bargainId) {
		Integer result;

		Assert.isTrue(bargainId != 0);
		result = this.commentRepository.countByBargainIdAndNoRepliedComment(bargainId);

		return result;
	}
	
	public Integer countByRepliedCommentId(final int commentId) {
		Integer result;

		Assert.isTrue(commentId != 0);
		result = this.commentRepository.countByRepliedCommentId(commentId);

		return result;
	}
	
	private Pageable getPageable(final int page, final int size) {
		Pageable result;
		
		if (page == 0 || size <= 0)
			result = new PageRequest(0, 5);
		else
			result = new PageRequest(page - 1, size);
		
		return result;
	}

	public Page<Comment> findByBargainId(final int bargainId, final int page, final int size) {
		Page<Comment> result;

		Assert.isTrue(bargainId != 0);
		
		result = this.commentRepository.findByBargainId(bargainId, this.getPageable(page, size));
		
		return result;
	}
	
	public Integer countByBargainId(final int bargainId) {
		Integer result;

		Assert.isTrue(bargainId != 0);
		result = this.commentRepository.countByBargainId(bargainId);

		return result;
	}
	
	public Page<Comment> findAllComments(final int page, final int size) {
		Page<Comment> result;
		
		result = this.commentRepository.findAllComments(this.getPageable(page, size));
		
		return result;
	}
	
	public Page<Comment> findByUserAccountId(final int userAccountId, final int page, final int size) {
		Page<Comment> result;

		Assert.isTrue(userAccountId != 0);
		
		result = this.commentRepository.findByUserAccountId(userAccountId, this.getPageable(page, size));
		
		return result;
	}
	
	public Integer countByUserAccountnId(final int userAccountId) {
		Integer result;

		Assert.isTrue(userAccountId != 0);
		result = this.commentRepository.countByUserAccountId(userAccountId);

		return result;
	}
	
	public Comment reconstruct(final Comment comment, final BindingResult binding) {
		Comment saved;
		Bargain bargain;
		Comment repliedComment;
		User user;

		if (comment.getId() == 0) {
			user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
			Assert.notNull(user);
			bargain = this.bargainService.findOne(comment.getBargain().getId());
			Assert.notNull(bargain);
			if(comment.getRepliedComment() != null) {
				repliedComment = this.findOne(comment.getRepliedComment().getId());
				comment.setRepliedComment(repliedComment);
			}
			comment.setUser(user);
			comment.setBargain(bargain);
			
		} else {
			saved = this.commentRepository.findOne(comment.getId());
			Assert.notNull(saved);
			comment.setVersion(saved.getVersion());
			comment.setUser(saved.getUser());
			comment.setBargain(saved.getBargain());
			comment.setRepliedComment(saved.getRepliedComment());
		}

		this.validator.validate(comment, binding);

		return comment;
	}

}
