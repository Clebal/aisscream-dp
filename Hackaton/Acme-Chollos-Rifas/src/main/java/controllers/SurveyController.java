package controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Answer;
import domain.Question;
import domain.Survey;

import services.AnswerService;
import services.QuestionService;
import services.SurveyService;

@Controller
@RequestMapping("/survey")
public class SurveyController extends AbstractController{

	// Services
	@Autowired
	private SurveyService surveyService;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private AnswerService answerService;

	// Constructor
	public SurveyController() {
		super();
	}
	
	// List
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public ModelAndView list(@RequestParam int rendezvousId, @RequestParam(required=false) Integer page) {
		ModelAndView result;
		Collection<Survey> surveys;
		Integer size; 
		
		size = 5;
		if (page == null) page = 1;
				
		surveys = this.surveyService.findByRendezvousIdToDisplay(rendezvousId, page, size);
		Assert.notNull(surveys);
		
		result = super.paginateModelAndView("survey/list", this.surveyService.countByRendezvousId(rendezvousId), page, size);
		result.addObject("requestURI", "survey/list.do");	
		result.addObject("surveys", surveys);
		
		return result;
	}
	
	// Display
	@RequestMapping(value="/display", method=RequestMethod.GET)
	public ModelAndView display(@RequestParam int surveyId) {
		ModelAndView result;
		Survey survey;
		Map<Question, Answer> questionAnswer;
		
		questionAnswer = new HashMap<Question, Answer>();
		
		survey = this.surveyService.findOneToDisplay(surveyId);
		Assert.notNull(survey);
		
		for(Question q: this.questionService.findByRendezvousId(survey.getRendezvous().getId())) {
			questionAnswer.put(q, this.answerService.findByRSVPIdAndQuestionId(surveyId, q.getId()));
		}
				
		result = new ModelAndView("survey/display");
		result.addObject("survey", survey);
		result.addObject("questionAnswer", questionAnswer);
		
		return result;
	}
	
}
