package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Moderator;
import forms.ModeratorForm;

import repositories.ModeratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class ModeratorService {

	// Managed repository
	@Autowired
	private ModeratorRepository moderatorRepository;
	
	// Supporting services
	@Autowired
	private Validator		validator;
	
	// Constructor
	public ModeratorService() {
		super();
	}
	
	// Simple CRUD methods
	public Moderator create() {
		Moderator result;
		UserAccount userAccount;
		Authority authority;

		result = new Moderator();
		userAccount = new UserAccount();
		authority = new Authority();

		authority.setAuthority("MODERATOR");
		userAccount.addAuthority(authority);

		result.setUserAccount(userAccount);
		
		return result;
	}
	
	public Collection<Moderator> findAll() {
		Collection<Moderator> result;
		
		result = this.moderatorRepository.findAll();
		
		return result;
	}
	
	public Moderator findOne(final int moderatorId) {
		Moderator result;
		
		Assert.isTrue(moderatorId != 0);
		
		result = this.moderatorRepository.findOne(moderatorId);
		
		return result;
	}
	
	public Moderator save(final Moderator moderator) {
		Moderator result, saved;
		Md5PasswordEncoder encoder;
		
		Assert.notNull(moderator);

		encoder = new Md5PasswordEncoder();
		
		if(moderator.getId() == 0){
			// Para crear un moderator debes estar sin autenticar
			Assert.isTrue(!LoginService.isAuthenticated());
			moderator.getUserAccount().setPassword(encoder.encodePassword(moderator.getUserAccount().getPassword(), null));			
		}else{
			// Solo puede ser editado por �l mismo
			Assert.isTrue(moderator.getUserAccount().equals(LoginService.getPrincipal()));
			
			// No se puede cambiar usuario ni contrase�a
			saved = this.moderatorRepository.findOne(moderator.getId());
			Assert.isTrue(saved.getUserAccount().getUsername().equals(moderator.getUserAccount().getUsername()));
			Assert.isTrue(moderator.getUserAccount().getPassword().equals(saved.getUserAccount().getPassword()));
		}
		
		result = this.moderatorRepository.save(moderator);
		
		return result;
	}
	
	// Other business methods
	public Moderator findByUserAccountId(final int userAccountId) {
		Moderator result;
		
		Assert.isTrue(userAccountId != 0);
		
		result = this.moderatorRepository.findByUserAccountId(userAccountId);
		
		return result;
	}
	
	// Reconstruct
	public Moderator reconstruct(final ModeratorForm moderatorForm, final BindingResult binding) {
		Moderator result;

		if (moderatorForm.getId() == 0) {
			result = this.create();

			Assert.notNull(result);
			Assert.isTrue(moderatorForm.getCheckPassword().equals(moderatorForm.getPassword()));
			Assert.isTrue(moderatorForm.isCheck());

			result.getUserAccount().setUsername(moderatorForm.getUsername());
			result.getUserAccount().setPassword(moderatorForm.getPassword());
			result.setBirthdate(moderatorForm.getBirthdate());


		} else {
			result = this.findOne(moderatorForm.getId());
			Assert.notNull(result);
			Assert.isTrue(result.getUserAccount().getUsername().equals(moderatorForm.getUsername()));
			moderatorForm.setBirthdate(result.getBirthdate());
		}

		result.setName(moderatorForm.getName());
		result.setSurname(moderatorForm.getSurname());
		result.setAddress(moderatorForm.getAddress());
		result.setEmail(moderatorForm.getEmail());
		result.setPhone(moderatorForm.getPhone());
		result.setIdentifier(moderatorForm.getIdentifier());

		this.validator.validate(moderatorForm, binding);

		return result;
	}
	
}
