
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import domain.Comment;

@Controller
@RequestMapping("/comment")
public class CommentController extends AbstractController {

	// Services
	@Autowired
	private CommentService	commentService;
	
	// List
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int bargainId, @RequestParam(required = false, defaultValue="1") Integer page) {
		ModelAndView result;
		Page<Comment> comments;
				
		comments = this.commentService.findByBargainId(bargainId, page, 5);
		Assert.notNull(comments);
		
		result = new ModelAndView("comment/list");
		result.addObject("requestURI", "comment/list.do?bargainId=" + bargainId);
		result.addObject("comments", comments.getContent());
		result.addObject("page", page);
		result.addObject("pageNumber", comments.getTotalPages());
				
		return result;
	}
	
	//Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int commentId, @RequestParam(required = false) Integer page) {
		ModelAndView result;
		Comment comment;

		comment = this.commentService.findOne(commentId);

		Assert.notNull(comment);

		if (page == null)
			page = 1;

		result = this.displayModelAndView(comment, page);

		return result;
	}
	
	//Ancillary methods -----------------------
	protected ModelAndView displayModelAndView(final Comment comment, final int page) {
		ModelAndView result;
		Page<Comment> comments;
		Boolean canComment;

		canComment = true;
		
		comments = this.commentService.findByRepliedCommentId(comment.getId(), page, 5);

		result = new ModelAndView("comment/display");

		result.addObject("pageNumber", comments.getTotalPages());
		result.addObject("page", page);
		result.addObject("comment", comment);
		result.addObject("comments", comments.getContent());
		result.addObject("canComment", canComment);
		result.addObject("mapLinkBoolean", super.checkLinkImages(comment.getImages()));

		return result;

	}
}
