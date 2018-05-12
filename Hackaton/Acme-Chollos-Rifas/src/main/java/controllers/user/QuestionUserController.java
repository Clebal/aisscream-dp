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
import services.AnswerService;
import services.QuestionService;
import services.SurveyService;

import controllers.AbstractController;
import domain.Answer;
import domain.Question;
import domain.Survey;

@Controller
@RequestMapping("/question/user")
public class QuestionUserController extends AbstractController {

	// Services
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private SurveyService surveyService;
	
	@Autowired
	private AnswerService answerService;
	
	// Constructor
	public QuestionUserController() {
		super();
	}
	
	// List
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) int surveyId, @RequestParam(required = false) Integer page) {
		ModelAndView result;
		Collection<Question> questions;
		Integer size;
		Answer answer;
		Question question;

		question = null;
		size = 1;
		if (page == null) page = 1;
				
		questions = this.questionService.findBySurveyId(surveyId, page, size);
		Assert.notNull(questions);
		
		for (Question q : questions){
			if (q.getNumber().equals(page)){
				question = q;
			}
		}
		
		answer = this.answerService.create(question);
	
		result = super.paginateModelAndView("question/list", this.questionService.countBySurveyId(LoginService.getPrincipal().getId()), page, size);
		result.addObject("requestURI", "question/user/list.do");
		result.addObject("questions", questions);
		result.addObject("answer", answer);
		
		return result;
	}
	
	// Create
	@RequestMapping(value="/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int surveyId) {
		ModelAndView result;
		Question question;
		Survey survey;
		
		survey = this.surveyService.findOne(surveyId);
		Assert.notNull(survey);
		
		question = this.questionService.create(survey);
		Assert.notNull(question);

		result = this.createEditModelAndView(question);
		
		return result;
	}
	
	// Edit
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int questionId) {
		ModelAndView result;
		Question question;
				
		question = this.questionService.findOneToEdit(questionId);
		Assert.notNull(question);
		
		result = this.createEditModelAndView(question);
		
		return result;
	}
	
	// Edit
	@RequestMapping(value="/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Question question, BindingResult binding) {
		ModelAndView result;
		
		question = this.questionService.reconstruct(question, binding);
		
		if(binding.hasErrors()){
			result = this.createEditModelAndView(question);
		}else{
			try {
				this.questionService.save(question);
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(question, "question.commit.error");
			}
		}
		
		return result;
	}
	
	// Delete
	@RequestMapping(value="/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Question question, BindingResult binding) {
		ModelAndView result;
		
		try {
			this.questionService.delete(question);
			result = new ModelAndView("redirect:list.do");
		} catch (Throwable oops) {
			result = this.createEditModelAndView(question, "question.commit.error");
		}
		
		return result;
	}
	
	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Question question) {
		ModelAndView result;

		result = this.createEditModelAndView(question, null);

		return result;
	}
	
	protected ModelAndView createEditModelAndView(final Question question, final String messageCode) {
		ModelAndView result;
		
		result = new ModelAndView("question/edit");
		
		result.addObject("question", question);
		result.addObject("message", messageCode);

		return result;
	}
	
}
