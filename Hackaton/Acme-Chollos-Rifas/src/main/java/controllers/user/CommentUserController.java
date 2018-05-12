
package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.BargainService;
import services.CommentService;
import services.UserService;
import controllers.AbstractController;
import domain.Bargain;
import domain.Comment;
import domain.User;

@Controller
@RequestMapping("/comment/user")
public class CommentUserController extends AbstractController {

	@Autowired
	private CommentService		commentService;

	@Autowired
	private BargainService		bargainService;

	@Autowired
	private UserService			userService;

	// List
		@RequestMapping(value="/list", method = RequestMethod.GET)
		public ModelAndView list(@RequestParam(required = false) Integer page) {
			ModelAndView result;
			Collection<Comment> comments;
			Integer size;

			size = 5;
			if (page == null) page = 1;
					
			comments = this.commentService.findByUserAccountId(LoginService.getPrincipal().getId(), page, size);
			Assert.notNull(comments);
			
			result = super.paginateModelAndView("comment/list", this.commentService.countByUserAccountnId(LoginService.getPrincipal().getId()), page, size);
			result.addObject("requestURI", "comment/user/list.do");
			result.addObject("comments", comments);
					
			return result;
	}
		
	//Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int bargainId, @RequestParam(required = false) final Integer repliedCommentId) {
		ModelAndView result;
		Comment repliedComment;
		final Comment comment;
		Bargain bargain;

		bargain = this.bargainService.findOne(bargainId);
		Assert.notNull(bargain);

		if (repliedCommentId != null) {
			repliedComment = this.commentService.findOne(repliedCommentId);
			Assert.notNull(repliedComment);

		} else
			repliedComment = null;

		comment = this.commentService.create(bargain, repliedComment);

		result = this.createEditModelAndView(comment);

		return result;

	}

	//Edit
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Comment comment, final BindingResult binding) {
		ModelAndView result;

		comment = this.commentService.reconstruct(comment, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(comment);
		else
			try {
				this.commentService.save(comment);

				if (comment.getRepliedComment() == null)
					result = new ModelAndView("redirect:/bargain/display.do?bargainId=" + comment.getBargain().getId());
				else
					result = new ModelAndView("redirect:/comment/display.do?commentId=" + comment.getRepliedComment().getId());

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(comment, "comment.commit.error");
			}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Comment comment) {
		ModelAndView result;

		result = this.createEditModelAndView(comment, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Comment comment, final String messageCode) {
		ModelAndView result;
		Boolean canEdit;
		User user;

		result = new ModelAndView("comment/create");

		user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(user);

		canEdit = false;

		Assert.isTrue(canEdit);
		result.addObject("canEdit", canEdit);
		result.addObject("comment", comment);
		result.addObject("actor", "user");
		result.addObject("message", messageCode);

		return result;
	}

}
