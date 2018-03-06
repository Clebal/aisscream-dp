package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Manager;

import repositories.ManagerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class ManagerService {

	// Managed repository
	@Autowired
	private ManagerRepository managerRepository;
	
	// Supporting services
	
	// Constructor
	public ManagerService() {
		super();
	}
	
	// Simple CRUD methods
	public Manager create() {
		Manager result;
		UserAccount userAccount;
		Authority authority;

		result = new Manager();
		userAccount = new UserAccount();
		authority = new Authority();

		authority.setAuthority("MANAGER");
		userAccount.addAuthority(authority);

		result.setUserAccount(userAccount);

		return result;
	}
	
	public Collection<Manager> findAll() {
		Collection<Manager> result;
		
		result = this.managerRepository.findAll();
		
		return result;
	}
	
	public Manager findOne(final int managerId) {
		Manager result;
		
		Assert.isTrue(managerId != 0);
		
		result = this.managerRepository.findOne(managerId);
		
		return result;
	}
	
	public Manager save(final Manager manager) {
		Manager result, saved;
		Md5PasswordEncoder encoder;
		
		Assert.notNull(manager);

		encoder = new Md5PasswordEncoder();
		
		if(manager.getId() == 0){
			// Para crear un manager debes estar sin autenticar
			Assert.isTrue(!LoginService.isAuthenticated());

			manager.getUserAccount().setPassword(encoder.encodePassword(manager.getUserAccount().getPassword(), null));
		}else{
			// Solo puede ser editado por él mismo
			Assert.isTrue(manager.getUserAccount().equals(LoginService.getPrincipal()));
			
			// No se puede cambiar usuario ni contraseña
			saved = this.managerRepository.findOne(manager.getId());
			Assert.isTrue(saved.getUserAccount().getUsername().equals(manager.getUserAccount().getUsername()));
			Assert.isTrue(manager.getUserAccount().getPassword().equals(saved.getUserAccount().getPassword()));
		}
		
		result = this.managerRepository.save(manager);
		
		return result;
	}
	
	// Other business methods
	public Manager findByUserAccountId(final int userAccountId) {
		Manager result;
		
		Assert.isTrue(userAccountId != 0);
		
		result = this.managerRepository.findByUserAccountId(userAccountId);
		
		return result;
	}
	
}
