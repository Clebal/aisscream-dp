package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Sponsor;
import forms.SponsorForm;

import repositories.SponsorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class SponsorService {

	// Managed repository
	@Autowired
	private SponsorRepository sponsorRepository;
	
	// Supporting services
	@Autowired
	private Validator		validator;
	
	// Constructor
	public SponsorService() {
		super();
	}
	
	// Simple CRUD methods
	public Sponsor create() {
		Sponsor result;
		UserAccount userAccount;
		Authority authority;

		result = new Sponsor();
		userAccount = new UserAccount();
		authority = new Authority();

		authority.setAuthority("SPONSOR");
		userAccount.addAuthority(authority);

		result.setUserAccount(userAccount);
		
		return result;
	}
	
	public Collection<Sponsor> findAll() {
		Collection<Sponsor> result;
		
		result = this.sponsorRepository.findAll();
		
		return result;
	}
	
	public Sponsor findOne(final int sponsorId) {
		Sponsor result;
		
		Assert.isTrue(sponsorId != 0);
		
		result = this.sponsorRepository.findOne(sponsorId);
		
		return result;
	}
	
	public Sponsor save(final Sponsor sponsor) {
		Sponsor result, saved;
		Md5PasswordEncoder encoder;
		
		Assert.notNull(sponsor);

		encoder = new Md5PasswordEncoder();
		
		if(sponsor.getId() == 0) {
			// Para crear un sponsor debes estar sin autenticar
			Assert.isTrue(!LoginService.isAuthenticated());
			sponsor.getUserAccount().setPassword(encoder.encodePassword(sponsor.getUserAccount().getPassword(), null));			
		} else {
			// Solo puede ser editado por �l mismo
			Assert.isTrue(sponsor.getUserAccount().equals(LoginService.getPrincipal()));
			
			// No se puede cambiar usuario ni contrase�a
			saved = this.sponsorRepository.findOne(sponsor.getId());
			Assert.isTrue(saved.getUserAccount().getUsername().equals(sponsor.getUserAccount().getUsername()));
			Assert.isTrue(sponsor.getUserAccount().getPassword().equals(saved.getUserAccount().getPassword()));
		}
		
		result = this.sponsorRepository.save(sponsor);
		
		return result;
	}
	
	// Other business methods
	public Sponsor findByUserAccountId(final int userAccountId) {
		Sponsor result;
		
		Assert.isTrue(userAccountId != 0);
		
		result = this.sponsorRepository.findByUserAccountId(userAccountId);
		
		return result;
	}
	
	// Reconstruct
	public Sponsor reconstruct(final SponsorForm sponsorForm, final BindingResult binding) {
		Sponsor result;

		if (sponsorForm.getId() == 0) {
			result = this.create();

			Assert.notNull(result);
			Assert.isTrue(sponsorForm.getCheckPassword().equals(sponsorForm.getPassword()));
			Assert.isTrue(sponsorForm.isCheck());

			result.getUserAccount().setUsername(sponsorForm.getUsername());
			result.getUserAccount().setPassword(sponsorForm.getPassword());
			result.setBirthdate(sponsorForm.getBirthdate());

		} else {
			result = this.findOne(sponsorForm.getId());
			Assert.notNull(result);
			Assert.isTrue(result.getUserAccount().getUsername().equals(sponsorForm.getUsername()));
			sponsorForm.setBirthdate(result.getBirthdate());
		}

		result.setName(sponsorForm.getName());
		result.setSurname(sponsorForm.getSurname());
		result.setAddress(sponsorForm.getAddress());
		result.setEmail(sponsorForm.getEmail());
		result.setPhone(sponsorForm.getPhone());
		result.setIdentifier(sponsorForm.getIdentifier());
		
		this.validator.validate(sponsorForm, binding);

		return result;
	}
	
}
