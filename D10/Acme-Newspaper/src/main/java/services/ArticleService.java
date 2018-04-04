
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ArticleRepository;
import services.ConfigurationService;
import security.Authority;
import security.LoginService;
import domain.Article;
import domain.Newspaper;
import domain.User;

@org.springframework.stereotype.Service
@Transactional
public class ArticleService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private ArticleRepository	articleRepository;

	@Autowired
	private Validator			validator;
	
	// Supporting Service
	@Autowired
	private ConfigurationService configurationService;

	// Constructors -----------------------------------------------------------
	public ArticleService() {
		super();
	}

	public Article create(final User writer, final Newspaper newspaper) {
		Article result;
		Authority authority;

		authority = new Authority();
		authority.setAuthority("USER");
		Assert.isTrue(LoginService.isAuthenticated());
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		result = new Article();
		result.setWriter(writer);
		result.setNewspaper(newspaper);

		return result;
	}
	
	public Collection<Article> findAll() {
		Collection<Article> result;

		result = this.articleRepository.findAll();

		return result;
	}

	public Article findOne(final int articleId) {
		Article result;

		Assert.isTrue(articleId != 0);

		result = this.articleRepository.findOne(articleId);

		return result;
	}

	public Article findOneToEdit(final int articleId) {
		Article result;

		Assert.isTrue(articleId != 0);

		result = this.articleRepository.findOne(articleId);

		Assert.notNull(result);

		Assert.isTrue(LoginService.isAuthenticated());

		Assert.isTrue(result.getWriter().getUserAccount().getId() == LoginService.getPrincipal().getId());

		return result;
	}

	public Article save(final Article article) {
		Article result;
		Article saved;
		boolean isTaboo;
		
		Assert.notNull(article);

		Assert.isTrue(LoginService.isAuthenticated());
		Assert.isTrue(article.getWriter().getUserAccount().getId() == LoginService.getPrincipal().getId());
		if (article.getId() != 0) {
			saved = this.findOne(article.getId());
			Assert.isTrue(article.getWriter().getId() == saved.getWriter().getId()); //No puede cambiar de user
			Assert.isTrue(article.getNewspaper().equals(saved.getNewspaper())); //No puede cambiar el newspaper
		}

		isTaboo = this.checkTabooWords(article);
		
		article.setHasTaboo(isTaboo);
		
		result = this.articleRepository.save(article);

		return result;

	}

	public void delete(final Article article) {
		Article articleToDelete;

		articleToDelete = this.findOne(article.getId());

		Assert.notNull(articleToDelete);

		Assert.isTrue(LoginService.isAuthenticated());

		Assert.isTrue(articleToDelete.getWriter().getUserAccount().getId() == LoginService.getPrincipal().getId());

		this.articleRepository.delete(articleToDelete);

	}
	
	//Auxiliare methods

	public Page<Article> findAllUserPaginated(final int userId, final int page, final int size) {
		Page<Article> result;
		Assert.isTrue(LoginService.isAuthenticated());

		result = this.articleRepository.findAllUserPaginated(userId, this.getPageable(page, size));

		return result;

	}
	
	public Page<Article> findAllNewspaperPaginated(final int userId, final int newspaperId, final int page, final int size) {
		Page<Article> result;
		Assert.isTrue(LoginService.isAuthenticated());

		result = this.articleRepository.findAllNewspaperPaginated(userId, newspaperId, this.getPageable(page, size));

		return result;

	}

	public Page<Article> findByWritterId (final int userId, final int page, final int size) {
		Page<Article> result;

		Assert.isTrue(userId != 0);
		Assert.isTrue(LoginService.isAuthenticated());

		result = this.articleRepository.findByWritterId (userId, this.getPageable(page, size));

		return result;

	}

	public Article reconstruct(final Article article, final BindingResult binding) {
		Article result, aux;

		if (article.getId() == 0)
			result = article;
		else {
			result = article;
			aux = this.articleRepository.findOne(article.getId());
			result.setVersion(aux.getVersion());
			result.setWriter(aux.getWriter());
			result.setTitle(aux.getTitle());
			result.setMoment(aux.getMoment());
			result.setSummary(article.getSummary());
			result.setBody(article.getBody());
			result.setPictures(article.getPictures());
			result.setIsFinalMode(article.getIsFinalMode());
			result.setNewspaper(article.getNewspaper());

		}

		this.validator.validate(result, binding);

		return result;
	}

	private Pageable getPageable(final int page, final int size) {
		Pageable result;

		if (page == 0 || size <= 0)
			result = new PageRequest(0, 5);
		else
			result = new PageRequest(page - 1, size);

		return result;

	}
	
	public boolean checkTabooWords(final Article article) {
		Collection<String> tabooWords;
		boolean result;

		result = false;
		tabooWords = this.configurationService.findSpamWords();

		for (final String tabooWord : tabooWords) {
			result = article.getTitle() != null && article.getTitle().toLowerCase().contains(tabooWord) || 
					article.getBody() != null && article.getBody().toLowerCase().contains(tabooWord);
			if (result == true)
				break;
		}

		return result;
	}
}
