
package controllers.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AnswerService;
import services.QuestionService;
import controllers.AbstractController;
import domain.Answer;

@Controller
@RequestMapping("/answer/user")
public class AnswerUserController extends AbstractController {

	// Services

	@Autowired
	private AnswerService		answerService;
	
	@Autowired
	private QuestionService		questionService;


	// Constructor
	public AnswerUserController() {
		super();
	}
	
	// Create
		@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
		public ModelAndView save(Answer answer, final BindingResult binding) {
			ModelAndView result;

			answer = this.answerService.reconstruct(answer, binding);

			if (binding.hasErrors()) {
				result = this.createEditModelAndView(answer);
			} else
				try {
					this.answerService.save(answer);
					if (this.questionService.countBySurveyId(answer.getQuestion().getSurvey().getId()).equals(answer.getQuestion().getNumber())) {
						result = new ModelAndView("redirect:/survey/display.do?surveyId=" + answer.getQuestion().getSurvey().getId());
					} else {
						result = new ModelAndView("redirect:/question/user/list.do?surveyId=" + answer.getQuestion().getSurvey().getId() + "&page=" + (answer.getQuestion().getNumber() + 1));
				}} catch (final Throwable oops) {
					result = this.createEditModelAndView(answer, "answer.commit.error");
				}

			return result;
		}
		
		// Ancillary methods
		protected ModelAndView createEditModelAndView(final Answer answer) {
			ModelAndView result;

			result = this.createEditModelAndView(answer, null);

			return result;
		}
		
		protected ModelAndView createEditModelAndView(final Answer answer, final String messageCode) {
			ModelAndView result;
			
			result = new ModelAndView("redirect:/question/user/list.do?surveyId=" + answer.getQuestion().getSurvey().getId() + "&page=" + (answer.getQuestion().getNumber()));
			
			result.addObject("answer", answer);
			result.addObject("message", messageCode);

			return result;
		}
}
