
package controllers.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import javax.validation.Valid;

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
import services.BargainService;
import services.SurveyService;
import services.UserService;
import controllers.AbstractController;
import domain.Answer;
import domain.Question;
import domain.Bargain;
import domain.Survey;
import domain.User;

@Controller
@RequestMapping("/survey/user")
public class SurveyUserController extends AbstractController {

	// Services
	@Autowired
	private SurveyService			surveyService;

	@Autowired
	private AnswerService		answerService;

	@Autowired
	private UserService			userService;

	@Autowired
	private BargainService	bargainService;

	@Autowired
	private QuestionService		questionService;


	// Constructor
	public SurveyUserController() {
		super();
	}

	// List
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) Integer page) {
		ModelAndView result;
		Collection<Survey> surveys;
		Integer size;

		size = 5;
		if (page == null)
			page = 1;

		surveys = this.surveyService.findByAttendantUserAccountId(LoginService.getPrincipal().getId(), page, size);
		Assert.notNull(surveys);

		result = super.paginateModelAndView("survey/list", this.surveyService.countByAttendantUserAccountId(LoginService.getPrincipal().getId()), page, size);
		result.addObject("surveys", surveys);
		result.addObject("requestURI", "survey/user/list.do");

		return result;
	}

	// Disply
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) final int surveyId) {
		ModelAndView result;
		Survey survey;
		LinkedHashMap<Question, Answer> questionAnswer;

		questionAnswer = new LinkedHashMap<Question, Answer>();

		survey = this.surveyService.findOne(surveyId);
		Assert.notNull(survey);

		for (final Question q : this.questionService.findByBargainId(survey.getBargain().getId()))
			questionAnswer.put(q, this.answerService.findByBargainIdAndQuestionId(survey.getId(), q.getId()));

		result = new ModelAndView("survey/display");
		result.addObject("survey", survey);
		result.addObject("questionAnswer", questionAnswer);

		return result;
	}

	// Cancel
	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView editCancel(@RequestParam final int surveyId) {
		ModelAndView result;
		Survey survey;

		survey = this.surveyService.findOne(surveyId);

		this.surveyService.save(survey);
		result = new ModelAndView("redirect:list.do");

		return result;
	}

	// Accept
	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView editAccept(@RequestParam final int surveyId) {
		ModelAndView result;
		Survey survey;

		survey = this.surveyService.findOne(surveyId);

		this.surveyService.save(survey);
		result = new ModelAndView("redirect:list.do");

		return result;
	}

	// Create-------------------------------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int bargainId) {
		ModelAndView result;
		Bargain bargain;
		Collection<Question> questions;
		LinkedHashMap<Integer, String> questionsMap;
		LinkedHashMap<Integer, String> answersMap;
		Survey survey;
		Survey survey;

		bargain = this.bargainService.findOneToDisplay(bargainId);
		Assert.notNull(bargain);

		questions = this.questionService.findByBargainId(bargainId);

		Assert.isTrue(!bargain.getIsDeleted());
		Assert.isTrue(!this.userService.findByUserAccountId(LoginService.getPrincipal().getId()).equals(bargain.getCreator()));

		if (questions.size() == 0)
			//Guardamos automaticamente el survey
			try {
				survey = this.surveyService.create(bargain);
				this.surveyService.save(survey);
				result = new ModelAndView("redirect:list.do");

				//Si se produce algún fallo en este momento, borramos el survey creado anteriormente	
			} catch (final Throwable oops) {
				result = new ModelAndView("redirect:list.do");
			}
		else {
			questionsMap = new LinkedHashMap<Integer, String>();
			answersMap = new LinkedHashMap<Integer, String>();

			survey = new Survey();

			for (final Question question : questions) {
				questionsMap.put(question.getId(), question.getText());
				answersMap.put(question.getId(), "");
			}

			survey.setQuestions(questionsMap);
			survey.setAnswers(answersMap);
			survey.setBargainId(bargainId);

			result = this.createEditModelAndView2(survey);

		}

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Survey survey, final BindingResult binding) {
		ModelAndView result;
		final Survey survey;
		Collection<Answer> answers;
		User user;
		Bargain bargain;
		boolean next;
		boolean deleteSurvey;

		next = true;
		answers = new ArrayList<Answer>();
		deleteSurvey = false;
		result = null;

		if (survey.getQuestions() == null)
			survey.setQuestions(new LinkedHashMap<Integer, String>());

		if (survey.getAnswers() == null)
			survey.setAnswers(new LinkedHashMap<Integer, String>());

		//Reconstruimos las respuestas y creamos el survey
		try {
			answers = this.answerService.reconstruct(survey, binding);

		} catch (final Throwable e) {
			next = false;
			result = this.createEditModelAndView2(survey, "survey.commit.error", new ArrayList<Answer>());
		}

		//Si todo ha ido bien anteriormente vemos si hay errores y si no guardamos las respuestas
		if (next)
			if (binding.hasErrors()) {
				result = this.createEditModelAndView2(survey, null, answers);
				deleteSurvey = true;

			} else
				try {
					//Si hay preguntas las guardamos
					if (answers.size() != 0)
						this.answerService.saveAnswers(answers);
					result = new ModelAndView("redirect:list.do");

					//Si se produce algún fallo en este momento, borramos el survey creado anteriormente	
				} catch (final Throwable oops) {

					deleteSurvey = true;
					result = this.createEditModelAndView2(survey, "survey.commit.error", new ArrayList<Answer>());
				}

		if (deleteSurvey) {
			user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
			bargain = this.bargainService.findOne(survey.getBargainId());

			survey = this.surveyService.findByAttendantUserIdAndBargainId(user.getId(), bargain.getId());

			if (survey != null)
				this.surveyService.delete(survey);
		}

		return result;
	}

	protected ModelAndView createEditModelAndView2(final Survey survey) {
		ModelAndView result;

		result = this.createEditModelAndView2(survey, null, new ArrayList<Answer>());

		return result;
	}

	protected ModelAndView createEditModelAndView2(final Survey survey, final String messageCode, final Collection<Answer> answers) {
		ModelAndView result;

		result = new ModelAndView("survey/edit");

		result.addObject("survey", survey);
		result.addObject("answers", answers);
		result.addObject("message", messageCode);

		return result;
	}
}
