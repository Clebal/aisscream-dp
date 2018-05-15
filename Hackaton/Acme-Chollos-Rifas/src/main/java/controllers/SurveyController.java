
package controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.AnswerService;
import services.CompanyService;
import services.ModeratorService;
import services.QuestionService;
import services.SponsorService;
import services.SurveyService;
import domain.Answer;
import domain.Question;
import domain.Surveyer;
import domain.Survey;
import forms.QuestionForm;
import forms.SurveyForm;

@Controller
@RequestMapping("/survey/{actor}")
public class SurveyController extends AbstractController {

	// Services
	@Autowired
	private SurveyService			surveyService;

	@Autowired
	private AnswerService		answerService;

	@Autowired
	private QuestionService		questionService;
	
	@Autowired
	private ModeratorService	moderatorService;
	
	@Autowired
	private CompanyService	companyService;

	@Autowired
	private SponsorService	sponsorService;

	// Constructor
	public SurveyController() {
		super();
	}

	// List
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false, defaultValue="1") Integer page, @PathVariable(value="actor") final String model) {
		ModelAndView result;
		Page<Survey> surveyPage;
		    
		surveyPage = this.surveyService.findByActorUserAccountId(LoginService.getPrincipal().getId(), page, 5);
		Assert.notNull(surveyPage);
		
		result = new ModelAndView("survey/list");
		result.addObject("surveys", surveyPage.getContent());
		result.addObject("page", page);
		result.addObject("pageNumber", surveyPage.getTotalPages());
		result.addObject("requestURI", "survey/"+model+"/list.do");
		result.addObject("model", model);
		    
		return result;
	}
	
	// Display
		@RequestMapping(value="/display", method = RequestMethod.GET)
		public ModelAndView display(@RequestParam int surveyId, @PathVariable(value="actor") final String model) {
			ModelAndView result;
			Survey survey;
			Collection<Question> questions;
			Collection<Answer> answers;
			Map<Answer, Double> answersRatio;
			Map<String, Map<Answer, Double>> questionsAnswerRatio;
			
			survey = this.surveyService.findOne(surveyId);
			Assert.notNull(survey);
			
			questions = this.questionService.findBySurveyId(surveyId, 1, this.questionService.countBySurveyId(surveyId));
			
			questionsAnswerRatio = new HashMap<String, Map<Answer, Double>>();
			
			for(Question q : questions) {
				answersRatio = new HashMap<Answer, Double>();
				answers = this.answerService.findByQuestionId(q.getId());
				for (Answer a : answers)
					answersRatio.put(a, this.answerService.ratioAnswerPerQuestion(q.getId(), a.getId()));
				questionsAnswerRatio.put(q.getText(), answersRatio);
			}
			
			result = new ModelAndView("survey/display");
			result.addObject("requestURI", "survey/"+model+"/list.do");
			result.addObject("survey", survey);
			result.addObject("questions", questions);
			result.addObject("questionsAnswerRatio", questionsAnswerRatio);
					
			return result;
		}

	// Create-------------------------------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView edit(@PathVariable(value="actor") final String model) {
		ModelAndView result;
		SurveyForm surveyForm;
		Surveyer surveyer;
		
		surveyForm = new SurveyForm();
		
		surveyer = null;
		if(model.equals("moderator")) {
			surveyer = this.moderatorService.findByUserAccountId(LoginService.getPrincipal().getId());
		} else if (model.equals("company")) {
			surveyer = this.companyService.findByUserAccountId(LoginService.getPrincipal().getId());
		} else if (model.equals("sponsor")) {
			surveyer = this.sponsorService.findByUserAccountId(LoginService.getPrincipal().getId());
		}
		Assert.notNull(surveyer);
		
		surveyForm.setSurveyer(surveyer);
		
		result = this.createEditModelAndView(surveyForm, model);
		
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final SurveyForm surveyForm, final BindingResult binding, @PathVariable(value="actor") final String model) {
		ModelAndView result;
		Survey survey, surveySaved;
		Question question, questionSaved;
		Answer answer;
		Integer number;
		
		survey = this.surveyService.reconstruct(surveyForm, model, binding);
		Assert.notNull(survey);
			
		if(binding.hasErrors()) {
			result = createEditModelAndView(surveyForm, model);
		} else {
			try {
				surveySaved = this.surveyService.save(survey);
				if(surveyForm.getId() == 0) {
					number = 0;
					for(QuestionForm q: surveyForm.getQuestions()) {
						question = this.questionService.reconstructFromSurvey(q, surveySaved, number);
						questionSaved = this.questionService.save(question);
						for(Answer a: q.getAnswers()) {
							answer = this.answerService.reconstructFromSurvey(a, questionSaved);
							this.answerService.save(answer);
						}
					}
					number++;
				}
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
//				result = super.panic(oops);
				result = this.createEditModelAndView(surveyForm, "survey.commit.error");
			}
		}
			
		return result;
	}

	protected ModelAndView createEditModelAndView(final SurveyForm surveyForm, final String model) {
		ModelAndView result;

		result = this.createEditModelAndView(surveyForm, model, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final SurveyForm surveyForm, final String model, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("survey/edit");

		result.addObject("surveyForm", surveyForm);
		result.addObject("message", messageCode);
		result.addObject("model", model);

		return result;
	}
}
