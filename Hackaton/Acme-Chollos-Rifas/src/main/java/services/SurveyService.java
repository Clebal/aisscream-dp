
package services;

import java.util.Collection;
import org.springframework.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.SurveyRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Answer;
import domain.Question;
import domain.Surveyer;
import domain.Survey;
import forms.QuestionForm;
import forms.SurveyForm;

@Service
@Transactional
public class SurveyService {

	// Managed repository -----------------------------------------------------
	
	@Autowired
	private SurveyRepository			surveyRepository;

	//Supporting services -----------------------------------------------------------
	
	@Autowired
	private Validator				validator;
	
	@Autowired
	private CompanyService			companyService;
	
	@Autowired
	private SponsorService			sponsorService;
	
	@Autowired
	private ModeratorService		moderatorService;
	

	@Autowired
	private AnswerService		answerService;

	@Autowired
	private QuestionService		questionService;
	

	// Constructors -----------------------------------------------------------
	
	public SurveyService() {
		super();
	}

	// Simple CRUD methods -----------------------------------------------------------
	
	public Survey create() {
		Survey result;
		
		result = new Survey();

		return result;
	}

	public Survey findOne(final int surveyId) {
		Survey result;

		Assert.isTrue(surveyId != 0);
		result = this.surveyRepository.findOne(surveyId);

		return result;
	}

	public Collection<Survey> findAll() {
		Collection<Survey> result;

		result = this.surveyRepository.findAll();

		return result;
	}

	public Survey save(final Survey survey, final SurveyForm surveyForm) {
		Survey result;
		Question question, questionSaved;
		Answer answer;
		Integer number;

		Assert.notNull(survey);

		result = this.surveyRepository.save(survey);
		
		if(surveyForm.getId() == 0) {
			number = 0;
			for(QuestionForm q: surveyForm.getQuestions()) {
				question = this.questionService.reconstructFromSurvey(q, result, number);
				questionSaved = this.questionService.save(question);
				for(Answer a: q.getAnswers()) {
					answer = this.answerService.reconstructFromSurvey(a, questionSaved);
					this.answerService.save(answer);
				}
			}
			number++;
		}

		return result;
	}

	public void delete(final Survey survey) {
		Authority authority;
		UserAccount userAccount;

		Assert.notNull(survey);
		userAccount = LoginService.getPrincipal();

		// Las compañías pueden borrarlas
		authority = new Authority();
		authority.setAuthority("COMAPNY");
		Assert.isTrue(userAccount.getAuthorities().contains(authority));

		this.surveyRepository.delete(survey);
	}

	//Other business methods -------------------------------------------------
	public Page<Survey> findByActorUserAccountId(final int userAccountId, final int page, final int size) {
		Page<Survey> result;
		
		Assert.isTrue(userAccountId != 0);
	
		result = this.surveyRepository.findByActorUserAccountId(userAccountId, this.getPageable(page,size));
		
		return result;
	}
	
	
	// Auxiliary methods
	private Pageable getPageable(final int page, final int size) {
		Pageable result;
		
		if (page == 0 || size <= 0)
			result = new PageRequest(0, 5);
		else
			result = new PageRequest(page - 1, size);
		
		return result;
	}
	
	public Survey reconstruct(final SurveyForm surveyForm, final String model, final BindingResult binding) {
		Survey survey;
		Surveyer surveyer;
		
		survey = this.create();
				
		surveyer = null;
		if(model.equals("moderator")) {
			surveyer = this.moderatorService.findByUserAccountId(LoginService.getPrincipal().getId());
		} else if (model.equals("company")) {
			surveyer = this.companyService.findByUserAccountId(LoginService.getPrincipal().getId());
		} else if (model.equals("sponsor")) {
			surveyer = this.sponsorService.findByUserAccountId(LoginService.getPrincipal().getId());
		}
		Assert.notNull(surveyer);

		survey.setSurveyer(surveyer);
		
		survey.setTitle(surveyForm.getTitle());
		
		this.validator.validate(survey, binding);
				
		return survey;
	}
	
}
