package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Bargain;

import repositories.BargainRepository;

@Service
@Transactional
public class BargainService {

	// Managed repository
	@Autowired
	private BargainRepository bargainRepository;
	
	// Supporting services
	
	// Simple CRUD methods
	
	public Bargain findOne(final int bargainId) {
		Bargain result;

		Assert.isTrue(bargainId != 0);

		result = this.bargainRepository.findOne(bargainId);

		return result;
	}
	
	// Other business methods
	public Page<Bargain> findBargainByUserAccountId(final int userAccountId, final int page, final int size) {
		Page<Bargain> result;
		
		Assert.isTrue(userAccountId != 0);
		
		result = this.bargainRepository.findBargainByUserAccountId(userAccountId, this.getPageable(page, size));
		
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
