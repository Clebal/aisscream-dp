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

import domain.Chirp;
import domain.User;

import repositories.ChirpRepository;
import security.Authority;
import security.LoginService;
import utilities.DatabaseConfig;

@Service
@Transactional
public class ChirpService {

	// Managed repository
	@Autowired
	private ChirpRepository chirpRepository;
	
	// Supporting services
	@Autowired
	private Validator			validator;
	
	@Autowired
	private ConfigurationService configurationService;
	
	// Constructor
	public ChirpService() {
		super();
	}
	
	// Simple CRUD methods
	public Chirp create(final User user) {
		Chirp result;
		
		result = new Chirp();
		result.setUser(user);
		result.setMoment(new Date(System.currentTimeMillis() - 1));
		
		return result;
	}
	public Collection<Chirp> findAll() {
		Collection<Chirp> result;
		
		result = this.chirpRepository.findAll();
		
		return result;
	}
	
	public Chirp findOne(final int chirpId) {
		Chirp result;
		
		Assert.notNull(chirpId != 0);
		
		result = this.chirpRepository.findOne(chirpId);
		
		return result;
	}
	
	public Chirp save(final Chirp chirp) {
		Chirp result;
		
		Assert.notNull(chirp);
		
		// El creador del chirp debe ser el mismo que est� logeado
		Assert.isTrue(chirp.getUser().getUserAccount().equals(LoginService.getPrincipal()));
		
		// Actualizamos el moment
		chirp.setMoment(new Date(System.currentTimeMillis() - 1));
		
		// Comprobar si tiene taboo
		/*for(String t: this.configurationService.findTabooWords) {
			if(chirp.getTitle().toLowerCase().contains(t)) {
				chirp.setHasTaboo(true);
				break;
			}
			if(chirp.getDescription().toLowerCase().contains(t)) { 
				chirp.setHasTaboo(true);
				break;
			}
		}*/
		
		result = this.chirpRepository.save(chirp);
		
		return result;
	}
	
	public void delete(final Chirp chirp) {
		Chirp saved;
		Authority authority;
		
		saved = this.findOne(chirp.getId());
		Assert.notNull(saved);
				
		// Solo lo puede borrar el administrador
		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		
		this.chirpRepository.delete(saved);	
	}
	
	// Other business methods
	public Page<Chirp> findAllPaginated(final int page, final int size) {
		Page<Chirp> result;
				
		result = this.chirpRepository.findAllPaginated(this.getPageable(page, size));
		
		return result;
	}	
	
	public Chirp findOneToEdit(final int chirpId) {
		Chirp result;
		
		Assert.notNull(chirpId != 0);
		
		result = this.chirpRepository.findOne(chirpId);
		Assert.notNull(result);
		
		return result;
	}
	
	public Page<Chirp> findFollowedsChirpByUserId(final int userAccountId, final int page, final int size) {
		Page<Chirp> result;
		
		Assert.notNull(userAccountId != 0);
		
		result = this.chirpRepository.findFollowedsChirpByUserId(userAccountId, this.getPageable(page, size));
		
		return result;
	}
	
	public Page<Chirp> findHasTaboo(final int page, final int size) {
		Page<Chirp> result;
				
		result = this.chirpRepository.findHasTaboo(this.getPageable(page, size));
		
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
	
	public Chirp reconstruct(final Chirp chirp, final BindingResult binding) {
		chirp.setVersion(0);
		chirp.setHasTaboo(false);
		
		this.validator.validate(chirp, binding);

		return chirp;
	}
	
	// Hibernate Search
	@SuppressWarnings("unchecked")
	public Collection<Chirp> findTaboos() {
        Collection<Chirp> result;
        Collection<String> tabooWords;
        String input;
        int index;
        
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
            
            HibernatePersistenceProvider persistenceProvider = new HibernatePersistenceProvider();
            EntityManagerFactory entityManagerFactory = persistenceProvider.createEntityManagerFactory(DatabaseConfig.PersistenceUnit, null);
 
            EntityManager em = entityManagerFactory.createEntityManager();

            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);

            fullTextEntityManager.createIndexer().startAndWait();

            em.getTransaction().begin();

            QueryBuilder qb = fullTextEntityManager.getSearchFactory()
                                                    .buildQueryBuilder()
                                                    .forEntity(Chirp.class)
                                                    .get();

            org.apache.lucene.search.Query luceneQuery = qb.keyword()
                                                            .onFields("title","description")
                                                            .matching(input)
                                                            .createQuery();

            Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Chirp.class);

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
