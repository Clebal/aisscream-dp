package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.User;

import repositories.UserRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class UserService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private UserRepository userRepository;

	// Supporting
	// services-----------------------------------------------------------

	// Constructors -----------------------------------------------------------
	public UserService() {
		super();
	}

	// Simple CRUD
	// methods-----------------------------------------------------------
	public User create() {
		User result;
		UserAccount userAccount;
		Authority authority;

		result = new User();
		userAccount = new UserAccount();
		authority = new Authority();

		authority.setAuthority("USER");
		userAccount.addAuthority(authority);

		result.setUserAccount(userAccount);

		return result;
	}

	public Collection<User> findAll() {
		Collection<User> result;

		result = this.userRepository.findAll();

		return result;
	}

	public User findOne(final int explorerId) {
		User result;

		Assert.isTrue(explorerId != 0);

		result = this.userRepository.findOne(explorerId);

		return result;
	}

	public User save(final User user) {
		User result, saved;
		UserAccount userAccount;
		Authority authority;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();
		authority = new Authority();
		Assert.notNull(user);
		authority.setAuthority("USER");

		/* Si el user ya existe, debe ser el que este logueado */
		if (user.getId() != 0) {
			userAccount = LoginService.getPrincipal();
			Assert.notNull(userAccount);
			Assert.isTrue(userAccount.equals(user.getUserAccount()));
			saved = this.userRepository.findOne(user.getId());
			Assert.notNull(saved);
			Assert.isTrue(saved.getUserAccount().getUsername().equals(user.getUserAccount().getUsername()));
			Assert.isTrue(user.getUserAccount().getPassword().equals(saved.getUserAccount().getPassword()));
		} else {
			/* Si no existe, debe tratarse de un user o anonimo */
			if(LoginService.isAuthenticated()){
				userAccount = LoginService.getPrincipal();
				Assert.isTrue(userAccount.getAuthorities().contains(authority));
			}
			
			user.getUserAccount().setPassword(encoder.encodePassword(user.getUserAccount().getPassword(), null));
		}

		result = this.userRepository.save(user);

		return result;
	}

	// Other business methods

	public User findByUserAccountId(final int id) {
		User result;

		Assert.isTrue(id != 0);

		result = this.userRepository.findByUserAccountId(id);

		return result;
	}

}
