package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Company;
import forms.CompanyForm;

import repositories.CompanyRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class CompanyService {

	// Managed repository
	@Autowired
	private CompanyRepository companyRepository;
	
	// Supporting services
	@Autowired
	private Validator		validator;
	
	// Constructor
	public CompanyService() {
		super();
	}
	
	// Simple CRUD methods
	public Company create() {
		Company result;
		UserAccount userAccount;
		Authority authority;

		result = new Company();
		userAccount = new UserAccount();
		authority = new Authority();

		authority.setAuthority("COMPANY");
		userAccount.addAuthority(authority);

		result.setUserAccount(userAccount);
		
		return result;
	}
	
	public Collection<Company> findAll() {
		Collection<Company> result;
		
		result = this.companyRepository.findAll();
		
		return result;
	}
	
	public Company findOne(final int companyId) {
		Company result;
		
		Assert.isTrue(companyId != 0);
		
		result = this.companyRepository.findOne(companyId);
		
		return result;
	}
	
	public Company save(final Company company) {
		Company result, saved;
		Md5PasswordEncoder encoder;
		
		Assert.notNull(company);

		encoder = new Md5PasswordEncoder();
		
		if(company.getId() == 0) {
			// Para crear un company debes estar sin autenticar
			Assert.isTrue(!LoginService.isAuthenticated());
			company.getUserAccount().setPassword(encoder.encodePassword(company.getUserAccount().getPassword(), null));			
		} else {
			// Solo puede ser editado por él mismo
			Assert.isTrue(company.getUserAccount().equals(LoginService.getPrincipal()));
			
			// No se puede cambiar usuario ni contraseña
			saved = this.companyRepository.findOne(company.getId());
			Assert.isTrue(saved.getUserAccount().getUsername().equals(company.getUserAccount().getUsername()));
			Assert.isTrue(company.getUserAccount().getPassword().equals(saved.getUserAccount().getPassword()));
		}
		
		result = this.companyRepository.save(company);
		
		return result;
	}
	
	// Other business methods
	public Company findByUserAccountId(final int userAccountId) {
		Company result;
		
		Assert.isTrue(userAccountId != 0);
		
		result = this.companyRepository.findByUserAccountId(userAccountId);
		
		return result;
	}
	
	// Reconstruct
	public Company reconstruct(final CompanyForm companyForm, final BindingResult binding) {
		Company result;

		if (companyForm.getId() == 0) {
			result = this.create();

			Assert.notNull(result);
			Assert.isTrue(companyForm.getCheckPassword().equals(companyForm.getPassword()));
			Assert.isTrue(companyForm.isCheck());

			result.getUserAccount().setUsername(companyForm.getUsername());
			result.getUserAccount().setPassword(companyForm.getPassword());
			result.setBirthdate(companyForm.getBirthdate());

		} else {
			result = this.findOne(companyForm.getId());
			Assert.notNull(result);
			Assert.isTrue(result.getUserAccount().getUsername().equals(companyForm.getUsername()));
			companyForm.setBirthdate(result.getBirthdate());
		}

		result.setName(companyForm.getName());
		result.setSurname(companyForm.getSurname());
		result.setAddress(companyForm.getAddress());
		result.setEmail(companyForm.getEmail());
		result.setPhone(companyForm.getPhone());
		result.setIdentifier(companyForm.getIdentifier());
		
		this.validator.validate(companyForm, binding);

		return result;
	}
	
}
