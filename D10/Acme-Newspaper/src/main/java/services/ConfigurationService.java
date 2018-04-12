
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ConfigurationRepository;
import security.Authority;
import security.LoginService;
import domain.Article;
import domain.Chirp;
import domain.Configuration;
import domain.Newspaper;

@Service
@Transactional
public class ConfigurationService {

	// Managed repository
	@Autowired
	private ConfigurationRepository	configurationRepository;

	// Other Services
	@Autowired
	private ChirpService			chirpService;

	@Autowired
	private ArticleService			articleService;

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private Validator				validator;


	// Constructor
	public ConfigurationService() {
		super();
	}

	// Simple CRUD methods
	public Configuration save(final Configuration configuration) {
		Configuration result;
		Authority authority;

		//Si no está vacío no te deja persistir otra, por lo que comprueba que la que pasas es la que está persistida y no es otra
		Assert.notNull(configuration, "configuration.not.null");

		if (!this.configurationRepository.findAll().isEmpty())
			Assert.notNull(this.configurationRepository.findOne(configuration.getId()));

		// Solo puede ser modificado por el admin
		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		result = this.configurationRepository.save(configuration);

		return result;
	}

	//Other business methods
	public Collection<String> findTabooWords() {
		Collection<String> result;

		result = this.configurationRepository.findTabooWords();

		return result;
	}

	public void flush() {
		this.configurationRepository.flush();
	}

	//Coger los marcados como taboo, comprobar si ya no lo son y actualizarlo
	public void updateTabooWordsBefore() {
		Collection<Chirp> chirpsTrueBefore;
		Collection<Article> articlesTrueBefore;
		Collection<Newspaper> newspapersTrueBefore;

		chirpsTrueBefore = this.chirpService.findByHasTabooTrue();
		articlesTrueBefore = this.articleService.findByHasTabooTrue();
		newspapersTrueBefore = this.newspaperService.findByHasTabooTrue();

		for (final Article article : articlesTrueBefore)
			if (!this.articleService.checkTabooWords(article)) {
				article.setHasTaboo(false);
				this.articleService.saveFromConfiguration(article);
			}

		for (final Newspaper newspaper : newspapersTrueBefore)
			if (!this.newspaperService.checkTabooWords(newspaper)) {
				newspaper.setHasTaboo(false);
				this.newspaperService.saveFromConfiguration(newspaper);
			}

		//		for (final Chirp chirp : chirpsTrueBefore)
		//			if (!this.chirpService.checkTabooWords(chirp)) {
		//				chirp.setHasTaboo(false);
		//				this.chirpService.saveFromConfiguration(chirp);
		//			}
	}

	//Coger todo aquello que tenga taboo words
	public void updateTabooWords() {
		final Collection<Chirp> chirps;
		final Collection<Newspaper> newspapers;
		final Collection<Article> articles;

		chirps = this.chirpService.findTaboos();
		articles = this.articleService.findTaboos();
		newspapers = this.newspaperService.findTaboos();

		//Actualizamos los encontrados como taboo
		for (final Chirp chirp : chirps) {
			chirp.setHasTaboo(true);
			this.chirpService.saveFromConfiguration(chirp);
		}

		for (final Newspaper newspaper : newspapers) {
			newspaper.setHasTaboo(true);
			this.newspaperService.saveFromConfiguration(newspaper);
		}

		for (final Article article : articles) {
			article.setHasTaboo(true);
			this.articleService.saveFromConfiguration(article);
		}

	}

	public Configuration findUnique() {
		Configuration result;
		Authority authority;

		// Solo puede ser modificado por el admin
		authority = new Authority();
		authority.setAuthority("ADMIN");
		if (LoginService.isAuthenticated())
			Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		result = this.configurationRepository.findUnique();

		return result;
	}

	// Reconstruct pruned object
	public Configuration reconstruct(final Configuration configuration, final BindingResult binding) {
		Configuration aux;

		aux = this.configurationRepository.findOne(configuration.getId());
		Assert.notNull(aux);

		configuration.setVersion(aux.getVersion());

		this.validator.validate(configuration, binding);

		return configuration;
	}

}
