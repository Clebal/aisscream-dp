package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ConfigurationRepository;
import security.Authority;
import security.LoginService;
import domain.Configuration;

@Service
@Transactional
public class ConfigurationService {

	// Managed repository
	@Autowired
	private ConfigurationRepository		configurationRepository;

	// Other Services
	@Autowired
	private Validator					validator;

	// Constructor
	public ConfigurationService() {
		super();
	}

	// Simple CRUD methods
	public Configuration save(final Configuration configuration) {
		Configuration result;
		Authority authority;

		//Si no est� vac�o no te deja persistir otra, por lo que comprueba que la que pasas es la que est� persistida y no es otra
		if (!this.configurationRepository.findAll().isEmpty())
			Assert.notNull(this.configurationRepository.findOne(configuration.getId()));

		Assert.notNull(configuration, "configuration.not.null");

		// Solo puede ser modificado por el admin
		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		result = this.configurationRepository.save(configuration);

		return result;
	}

	//Other business methods
	public Configuration findUnique() {
		Configuration result;
		Authority authority;
		
		// Solo puede ser modificado por el admin
		authority = new Authority();
		authority.setAuthority("ADMIN");
		if(LoginService.isAuthenticated()) Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		
		result = this.configurationRepository.findUnique();
		
		return result;
	}
	
	public String findName() {
		String result;
		
		result = this.configurationRepository.findName();

		return result;
	}
	
	public String findSlogan() {
		String result;
		
		result = this.configurationRepository.findSlogan();

		return result;
	}
	
	public String findEmail() {
		String result;
		
		result = this.configurationRepository.findEmail();

		return result;
	}

	public String findBanner() {
		String result;		
		
		result = this.configurationRepository.findBanner();

		return result;
	}
	
	public void flush(){
		this.configurationRepository.flush();
	}

	// Reconstruct pruned object
	public Configuration reconstruct(final Configuration configuration, final BindingResult binding) {
		Configuration aux;

		aux = this.configurationRepository.findOne(configuration.getId());
		Assert.notNull(aux);
		
		configuration.setVersion(aux.getVersion());
		
		configuration.setName(aux.getName());
		configuration.setSlogan(aux.getSlogan());
		configuration.setEmail(aux.getEmail());
		configuration.setBanner(aux.getBanner());
		
		this.validator.validate(configuration, binding);

		return configuration;
	}
	
}
