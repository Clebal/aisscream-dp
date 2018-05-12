
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.SurveyRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Bargain;
import domain.Company;
import domain.Survey;

@Service
@Transactional
public class SurveyService {

	// Managed repository -----------------------------------------------------
	
	@Autowired
	private SurveyRepository			surveyRepository;

	//Supporting services -----------------------------------------------------------

	@Autowired
	private CompanyService			companyService;
	
	@Autowired
	private Validator				validator;

	// Constructors -----------------------------------------------------------
	
	public SurveyService() {
		super();
	}

	// Simple CRUD methods -----------------------------------------------------------
	
	public Survey create(final Bargain bargain) {
		Survey result;
		Collection<Bargain> bargains;

		bargains = new HashSet<Bargain>();
		bargains.add(bargain);
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

	public Survey save(final Survey survey) {
		Survey result;
		Authority authority;
		UserAccount userAccount;
		Company company;

		Assert.notNull(survey);

		userAccount = LoginService.getPrincipal();

		company = this.companyService.findByUserAccountId(userAccount.getId());
		Assert.notNull(company);
		
		// Las compañías pueden crearlas y editarlas
		
		authority = new Authority();
		authority.setAuthority("COMPANY");
		Assert.isTrue(userAccount.getAuthorities().contains(authority));
		
		result = this.surveyRepository.save(survey);

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

	public Survey reconstruct(final Survey survey, final BindingResult binding) {
		Survey result, aux;

		if (survey.getId() == 0)
			result = survey;
		else {
			result = survey;
			aux = this.surveyRepository.findOne(survey.getId());
			result.setVersion(aux.getVersion());
		}

		this.validator.validate(result, binding);

		return result;
	}
	
}
