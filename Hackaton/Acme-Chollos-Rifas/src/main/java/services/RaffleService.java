package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Raffle;

import repositories.RaffleRepository;

@Service
@Transactional
public class RaffleService {

	// Managed repository
	@Autowired
	private RaffleRepository raffleRepository;
	
	// Supporting services
	
	// Constructor
	public RaffleService() {
		super();
	}
	
	// Simple CRUD methods
	public Collection<Raffle> findAll() {
		Collection<Raffle> result;
		
		result = this.raffleRepository.findAll();
		
		return result;
	}
	
	public Raffle findOne(final int rifaId) {
		Raffle result;
		
		Assert.isTrue(rifaId != 0);
		
		result = this.raffleRepository.findOne(rifaId);
		
		return result;
	}
	
	public Raffle save(final Raffle rifa) {
		Raffle result;
		
		Assert.notNull(rifa);
		
		// El usuario está logeado
		
		// Guardar
		result = this.raffleRepository.save(rifa);
		
		return result;
	}
	
	// Delete
	
	// Other business methods 
	public Page<Raffle> findAvailables(final int page, final int size) {
		Page<Raffle> result;
		
		result = this.raffleRepository.findAvailables(this.getPageable(page, size));
		
		return result;
	}
	
	public Page<Raffle> findByUserAccountId(final int userAccountId, final int page, final int size) {
		Page<Raffle> result;
		
		Assert.isTrue(userAccountId != 0);
		
		result = this.raffleRepository.findByUserAccountId(userAccountId, this.getPageable(page, size));
		
		return result;
	}
	
	public Page<Raffle> findOrderedByMaxDate(final int page, final int size) {
		Page<Raffle> result;
		
		result = this.raffleRepository.findOrderedByMaxDate(this.getPageable(page, size));
		
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
	
}
