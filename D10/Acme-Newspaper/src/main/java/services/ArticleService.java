
package services;

import java.util.Collection;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Article;
import domain.FollowUp;
import domain.Newspaper;
import domain.User;

import repositories.ArticleRepository;
import security.Authority;
import security.LoginService;
import utilities.DatabaseConfig;

@Service
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
	
	@Autowired
	private FollowUpService followUpService;
	
	@Autowired
	private SubscriptionService subscriptionService;
	
	@Autowired
	private CustomerService	customerService;

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
	
	public Article findOneToDisplay(final int articleId) {
		Article result;

		Assert.isTrue(articleId != 0);

		result = this.articleRepository.findOne(articleId);

		Assert.notNull(result);

		Assert.isTrue(this.checkVisibleArticle(result));

		return result;
	}

	public Article save(final Article article) {
		Article result;
		Article saved;
		boolean isTaboo;
		Collection<Article> articles;
		boolean isFinal;
		
		Assert.notNull(article);

		Assert.isTrue(LoginService.isAuthenticated());
		Assert.isTrue(article.getWriter().getUserAccount().getId() == LoginService.getPrincipal().getId());
		if (article.getId() == 0) {
			if (!article.getIsFinalMode()) {
				article.getNewspaper().setIsPublished(false);
			}
		} else {
			saved = this.findOne(article.getId());
			Assert.isTrue(!saved.getIsFinalMode());
			Assert.isTrue(article.getWriter().getId() == saved.getWriter().getId()); //No puede cambiar de user
			Assert.isTrue(article.getNewspaper().equals(saved.getNewspaper())); //No puede cambiar el newspaper
			
			isFinal = true;
			
			if (article.getIsFinalMode()) {
				articles = this.findByNewspaperId(article.getNewspaper().getId());
				for (Article a : articles) {
					isFinal = true;
					if (!a.getIsFinalMode()) {
						isFinal = false;
						break;
					}
				}
				if (!isFinal) {
					article.getNewspaper().setIsPublished(true);
				}	
			}
		}

		isTaboo = this.checkTabooWords(article);
		
		article.setHasTaboo(isTaboo);
		
		if (article.getNewspaper().getIsPublished()) {
			
		}
		
		result = this.articleRepository.save(article);

		return result;

	}

	public void delete(final Article article) {
		Article articleToDelete;
		Collection<FollowUp> followUps;
		Collection<Article> articles;
		boolean isFinal;

		articleToDelete = this.findOne(article.getId());

		Assert.notNull(articleToDelete);

		Assert.isTrue(LoginService.isAuthenticated());

		Assert.isTrue(articleToDelete.getWriter().getUserAccount().getId() == LoginService.getPrincipal().getId());
		
		followUps = this.followUpService.findByArticleId(articleToDelete.getId());
				
		for (FollowUp f : followUps) {
			this.followUpService.deleteFromArticle(f.getId());
		}
		
		isFinal = true;
		
		if (!article.getIsFinalMode()) {
			articles = this.findByNewspaperId(articleToDelete.getNewspaper().getId());
			for (Article a : articles) {
				isFinal = true;
				if (!a.getIsFinalMode()) {
					isFinal = false;
					break;
				}
			}
			if (!isFinal) {
				articleToDelete.getNewspaper().setIsPublished(true);
			}	
		}

		this.articleRepository.delete(articleToDelete);

	}
	
	public void deleteFromNewspaper(final Article article) {
		Authority authority;
		Collection<FollowUp> followUps;
		Collection<Article> articles;
		boolean isFinal;
		
		authority = new Authority();
		authority.setAuthority("ADMIN");
		
		Assert.notNull(article);
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		
		followUps = this.followUpService.findByArticleId(article.getId());

		for (FollowUp f : followUps) {
			this.followUpService.deleteFromArticle(f.getId());
		}
		
		isFinal = true;

		if (!article.getIsFinalMode()) {
			articles = this.findByNewspaperId(article.getNewspaper().getId());
			for (Article a : articles) {
				isFinal = true;
				if (!a.getIsFinalMode()) {
					isFinal = false;
					break;
				}
			}
			if (!isFinal) {
				article.getNewspaper().setIsPublished(true);
			}	
		}

		this.articleRepository.delete(article);

	}
	
	public void flush() {
		
		this.articleRepository.flush();
		
	}
	
	//Auxiliare methods

	public Page<Article> findAllUserPaginated(final int userId, final int page, final int size) {
		Page<Article> result;

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

		result = this.articleRepository.findByWritterId(userId, this.getPageable(page, size));

		return result;

	}
	
	public Page<Article> findAllPaginated (final int page, final int size) {
		Page<Article> result;
		Authority authority;

		authority = new Authority();
		authority.setAuthority("ADMIN");

		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		result = this.articleRepository.findAllPaginated(this.getPageable(page, size));

		return result;

	}
	
	public Page<Article> findAllTabooPaginated (final int page, final int size) {
		Page<Article> result;
		Authority authority;

		authority = new Authority();
		authority.setAuthority("ADMIN");
		
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		
		result = this.articleRepository.findAllTabooPaginated(this.getPageable(page, size));

		return result;

	}
	
	public Collection<Article> findByUserIdAndNewspaperId(final int userId, final int newspaperId) {
		Collection<Article> result;
		
		Assert.isTrue(userId != 0);
		Assert.isTrue(newspaperId != 0);
		
		result = this.articleRepository.findByUserIdAndNewspaperId(userId, newspaperId);
		
		return result;
	}
	
	public Collection<Article> findByNewspaperId(final int newspaperId) {
		Collection<Article> result;
		
		Assert.isTrue(newspaperId != 0);
		
		result = this.articleRepository.findByNewspaperId(newspaperId);
		
		return result;
	}

	public Page<Article> findByNewspaperIdPaginated(final int newspaperId, final int page, final int size) {
		Page<Article> result;
		
		Assert.isTrue(newspaperId != 0);
		
		result = this.articleRepository.findByNewspaperIdPaginated(newspaperId, this.getPageable(page, size));
		
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
		tabooWords = this.configurationService.findTabooWords();

		for (final String tabooWord : tabooWords) {
			result = article.getTitle() != null && article.getTitle().toLowerCase().contains(tabooWord) || 
					article.getBody() != null && article.getBody().toLowerCase().contains(tabooWord);
			if (result == true)
				break;
		}

		return result;
	}
	
	public Boolean checkVisibleArticle(final Article article) {
		Boolean result;
		Authority authority;
		Authority authority2;
		Authority authority3;
		Date currentMoment;

		result = false;

		Assert.notNull(article);

		Assert.notNull(article.getNewspaper());
		authority = new Authority();
		authority.setAuthority("USER");
		authority2 = new Authority();
		authority2.setAuthority("CUSTOMER");
		authority3 = new Authority();
		authority3.setAuthority("ADMIN");
		currentMoment = new Date();
		result = false;

		//Si el usuario esta autenticado
		if (LoginService.isAuthenticated()) {

			//Si es un USER
			if (LoginService.getPrincipal().getAuthorities().contains(authority)) {

				//Si es el creador del artículo
				if (article.getWriter().getUserAccount().getId() == LoginService.getPrincipal().getId())
					result = true;
				//Si el newspaper no es privado y está publicado. El articulo es finalMode
				else if (article.getNewspaper().getPublicationDate().compareTo(currentMoment) <= 0 && article.getNewspaper().getIsPublished() == true && !article.getNewspaper().getIsPrivate() && article.getIsFinalMode())
					result = true;

				//Si es un CUSTOMER y el articulo es final mode
			} else if (LoginService.getPrincipal().getAuthorities().contains(authority2) && article.getIsFinalMode()) {
				//Si está publicado y no es privado
				if (article.getNewspaper().getPublicationDate().compareTo(currentMoment) <= 0 && article.getNewspaper().getIsPublished() == true && !article.getNewspaper().getIsPrivate())
					result = true;

				//Si esta publicado y tiene una suscripción
				else if (article.getNewspaper().getPublicationDate().compareTo(currentMoment) <= 0 && article.getNewspaper().getIsPublished() == true
					&& this.subscriptionService.findByCustomerAndNewspaperId(this.customerService.findByUserAccountId(LoginService.getPrincipal().getId()).getId(), article.getNewspaper().getId()) != null)
					result = true;

				//Si es un ADMIN
			} else if (LoginService.getPrincipal().getAuthorities().contains(authority3) && article.getIsFinalMode())
				result = true;

			//Si no está autenticado
			//Si esta públicado y no es privado
		} else if (article.getNewspaper().getPublicationDate().compareTo(currentMoment) <= 0 && article.getNewspaper().getIsPublished() == true && article.getNewspaper().getIsPrivate() == false && article.getIsFinalMode())
			result = true;

		return result;
	}
	
	// Hibernate Search
	@SuppressWarnings("unchecked")
	public Collection<Article> findTaboos() {
        Collection<Article> result;
        Collection<String> tabooWords;
        String input;
        int index;
        HibernatePersistenceProvider persistenceProvider;
        EntityManagerFactory entityManagerFactory;
        EntityManager em;
        FullTextEntityManager fullTextEntityManager;
        QueryBuilder qb;
        org.apache.lucene.search.Query luceneQuery;
        Query jpaQuery;
        result = null;

        try {

            tabooWords = this.configurationService.findTabooWords();
            index = 0;
            input = "";
            for(String s: tabooWords) {
                if(index == 0) {
                    input = s;
                    index++;
                } else
                    input += ", " + s;
            }
            
            persistenceProvider = new HibernatePersistenceProvider();
            entityManagerFactory = persistenceProvider.createEntityManagerFactory(DatabaseConfig.PersistenceUnit, null);
 
            em = entityManagerFactory.createEntityManager();

            fullTextEntityManager = Search.getFullTextEntityManager(em);

            fullTextEntityManager.createIndexer().startAndWait();

            em.getTransaction().begin();

            qb = fullTextEntityManager.getSearchFactory()
                                      .buildQueryBuilder()
                                      .forEntity(Article.class)
                                      .get();

            luceneQuery = qb.keyword()
                            .onFields("title","summary","body")
                            .matching(input)
                            .createQuery();

            jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Article.class);

            result = jpaQuery.getResultList();

            em.getTransaction().commit();

            em.close();

        }catch (IllegalArgumentException e) {
        	e.printStackTrace();
        } catch (Throwable a) {
        	a.printStackTrace();
        }
        
        return result;

    }
}
