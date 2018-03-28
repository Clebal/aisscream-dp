package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Rifa;

import repositories.RifaRepository;

@Service
@Transactional
public class RifaService {

	// Managed repository
	@Autowired
	private RifaRepository rifaRepository;
	
	// Supporting services
	
	// Constructor
	public RifaService() {
		super();
	}
	
	// Simple CRUD methods
	public Collection<Rifa> findAll() {
		Collection<Rifa> result;
		
		result = this.rifaRepository.findAll();
		
		return result;
	}
	
	public Rifa findOne(final int rifaId) {
		Rifa result;
		
		Assert.isTrue(rifaId != 0);
		
		result = this.rifaRepository.findOne(rifaId);
		
		return result;
	}
	
	public Rifa save(final Rifa rifa) {
		Rifa result;
		
		Assert.notNull(rifa);
		
		// El usuario está logeado
		
		// Guardar
		result = this.rifaRepository.save(rifa);
		
		return result;
	}
	
	// Delete
	
	// Other business methods 
	
	
}
