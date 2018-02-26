
package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import controllers.AbstractController;
import domain.Comment;

@Controller
@RequestMapping("/comment/administrator")
public class CommentAdministratorController extends AbstractController {

	// Services
	@Autowired
	private CommentService	commentService;


	// Delete
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int commentId) {
		ModelAndView result;
		Comment comment;

		comment = this.commentService.findOne(commentId);
		Assert.notNull(comment);

		result = this.createEditModelAndView(comment);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Comment comment, final BindingResult binding) {
		ModelAndView result;
		int rendezvousId;
		try {
			Assert.notNull(comment.getRendezvous());
			rendezvousId = comment.getRendezvous().getId();
			this.commentService.delete(comment);
			result = new ModelAndView("redirect:/rendezvous/display.do?rendezvousId=" + rendezvousId);
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

		result = new ModelAndView("comment/edit");

		result.addObject("comment", comment);
		result.addObject("actor", "administrator");
		result.addObject("message", messageCode);

		return result;
	}

}
