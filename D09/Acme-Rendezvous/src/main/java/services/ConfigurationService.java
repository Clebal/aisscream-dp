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
import domain.Internationalization;

@Service
@Transactional
public class ConfigurationService {

	// Managed repository
	@Autowired
	private ConfigurationRepository		configurationRepository;

	// Other Services
	@Autowired
	private InternationalizationService	internationalizationService;

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

		//Si no está vacío no te deja persistir otra, por lo que comprueba que la que pasas es la que está persistida y no es otra
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

	public String findBanner() {
		String result;		
		
		result = this.configurationRepository.findBanner();

		return result;
	}

	public String findWelcomeMessage(final String countryCode) {
		String result;
		Internationalization internationalizationWelcomeMessage;
		String messageCode;
		
		Assert.notNull(countryCode);
		
		messageCode = this.configurationRepository.findWelcomeMessage();
		
		// El countryCode debe estar entre los idiomas disponibles
		Assert.isTrue(this.internationalizationService.findAvailableLanguagesByMessageCode(messageCode).contains(countryCode));
	
		internationalizationWelcomeMessage = this.internationalizationService.findByCountryCodeAndMessageCode(countryCode, messageCode);
		result = internationalizationWelcomeMessage.getValue();

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
		
		if(configuration.getWelcomeMessage() == null){
			configuration.setWelcomeMessage(aux.getWelcomeMessage());
		}else{
			configuration.setName(aux.getName());
			configuration.setBanner(aux.getBanner());
		}
		
		this.validator.validate(configuration, binding);

		return configuration;
	}
	
}
