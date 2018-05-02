package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Level;

import repositories.LevelRepository;

@Service
@Transactional
public class LevelService {

	// Managed repository
	@Autowired
	private LevelRepository levelRepository;
	
	// Supporting service
	
	// Constructor
	public LevelService() {
		super();
	}
	
	// Basic CRUD methods
	public Level findOne(final int levelId) {
		Level result;
		
		Assert.isTrue(levelId != 0);
		
		result = this.levelRepository.findOne(levelId);
		
		return result;
	}
	
	// Other business methods
	public Level findByPoints(final int points) {
		Level result;
				
		result = this.levelRepository.findByPoints(points);
		
		return result;
	}
	
}
