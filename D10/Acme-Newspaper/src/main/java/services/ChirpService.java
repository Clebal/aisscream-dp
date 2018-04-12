package services;

import java.util.Collection;
import java.util.Date;

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
		
		// El creador del chirp debe ser el mismo que está logeado
		Assert.isTrue(chirp.getUser().getUserAccount().equals(LoginService.getPrincipal()));
		
		// Actualizamos el moment
		chirp.setMoment(new Date(System.currentTimeMillis() - 1));
		
		// Comprobar si tiene taboo
		for(String t: this.configurationService.findTabooWords()) {
			if(chirp.getTitle().toLowerCase().contains(t)) {
				chirp.setHasTaboo(true);
				break;
			}
			if(chirp.getDescription().toLowerCase().contains(t)) { 
				chirp.setHasTaboo(true);
				break;
			}
		}
		
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
	public Page<Chirp> findByUserId(final int userId, final int page, final int size) {
		Page<Chirp> result;
		
		Assert.isTrue(userId != 0);
		
		result = this.chirpRepository.findByUserId(userId, this.getPageable(page, size));
		
		return result;
	}
	
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
	
	public Double[] avgStandartDeviationNumberChirpsPerUser() {
		Double[] result;
		
		result = this.chirpRepository.avgStandartDeviationNumberChirpsPerUser();
		
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
		chirp.setMoment(new Date(System.currentTimeMillis() - 1));

		this.validator.validate(chirp, binding);

		return chirp;
	}
	
	public void findTaboos() {
        boolean result;
        Collection<String> tabooWords;
        
        tabooWords = this.configurationService.findTabooWords();
        
        for(Chirp c: this.findAll())
            for(String taboo: tabooWords) {
	        	result = c.getTitle().toLowerCase().contains(taboo);
	        	if(result == true) {
	        		this.delete(c);
	        		continue;
	        	}
	        	result = c.getDescription().toLowerCase().contains(taboo);
	        	if(result == true) {
	        		this.delete(c);
	        		continue;
	        	}
	        }
            
	}
	
}
