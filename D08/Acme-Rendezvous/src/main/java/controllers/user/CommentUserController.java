
package controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import services.RendezvousService;
import controllers.AbstractController;
import domain.Comment;
import domain.Rendezvous;

@Controller
@RequestMapping("/comment/user")
public class CommentUserController extends AbstractController {

	@Autowired
	private CommentService		commentService;

	@Autowired
	private RendezvousService	rendezvousService;


	//Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int rendezvousId, @RequestParam(required = false) final Integer repliedCommentId) {
		ModelAndView result;
		Comment repliedComment;
		final Comment comment;
		Rendezvous rendezvous;

		rendezvous = this.rendezvousService.findOne(rendezvousId);
		Assert.notNull(rendezvous);

		if (repliedCommentId != null) {
			repliedComment = this.commentService.findOne(repliedCommentId);
			Assert.notNull(repliedComment);

		} else
			repliedComment = null;

		comment = this.commentService.create(rendezvous, repliedComment);

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
					result = new ModelAndView("redirect:/rendezvous/display.do?rendezvousId=" + comment.getRendezvous().getId());
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

		result = new ModelAndView("comment/create");

		result.addObject("comment", comment);
		result.addObject("actor", "user");
		result.addObject("message", messageCode);

		return result;
	}

}
