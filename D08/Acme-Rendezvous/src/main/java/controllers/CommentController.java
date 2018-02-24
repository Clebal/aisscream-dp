
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.CommentService;
import services.RsvpService;
import services.UserService;
import domain.Comment;
import domain.Rsvp;
import domain.User;

@Controller
@RequestMapping("/comment")
public class CommentController extends AbstractController {

	// Services
	@Autowired
	private CommentService	commentService;

	@Autowired
	private UserService		userService;

	@Autowired
	private RsvpService		rsvpService;


	//Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int commentId, @RequestParam(required = false) Integer page) {
		ModelAndView result;
		Comment comment;

		comment = this.commentService.findOneToDisplay(commentId);

		Assert.notNull(comment);

		if (page == null)
			page = 1;

		result = this.createEditModelAndView(comment, page);

		return result;
	}

	//Ancillary methods -----------------------
	protected ModelAndView createEditModelAndView(final Comment comment, final int page) {
		ModelAndView result;
		Collection<Comment> comments;
		Integer pageNumber;
		Integer size;
		Boolean canComment;
		User user;
		Rsvp rsvp;

		canComment = false;

		if (LoginService.isAuthenticated()) {
			user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());

			if (user != null) {
				rsvp = this.rsvpService.findByAttendantUserIdAndRendezvousId(user.getId(), comment.getRendezvous().getId());

				if ((rsvp != null && rsvp.getStatus().equals("ACCEPTED")) || comment.getRendezvous().getCreator().equals(user))
					canComment = true;
			}
		}

		comments = new ArrayList<Comment>();
		size = 5;
		pageNumber = 0;

		comments = this.commentService.findByRepliedCommentId(comment.getId(), page, size);

		if (comments.size() != 0)
			pageNumber = this.commentService.countByRepliedCommentId(comment.getId());

		result = new ModelAndView("comment/display");

		pageNumber = (int) Math.floor(((pageNumber / (size + 0.0)) - 0.1) + 1);

		result.addObject("pageNumber", pageNumber);
		result.addObject("page", page);
		result.addObject("comment", comment);
		result.addObject("comments", comments);
		result.addObject("canComment", canComment);

		return result;

	}
}
