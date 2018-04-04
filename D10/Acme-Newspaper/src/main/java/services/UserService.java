
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.UserRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.User;
import forms.UserForm;

@Service
@Transactional
public class UserService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private UserRepository	userRepository;

	@Autowired
	private Validator		validator;


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
		List<User> followers;
		
		result = new User();
		userAccount = new UserAccount();
		authority = new Authority();
		followers = new ArrayList<User>();

		authority.setAuthority("USER");
		userAccount.addAuthority(authority);

		result.setUserAccount(userAccount);
		result.setFollowers(followers);

		return result;
	}

	public Collection<User> findAll() {
		Collection<User> result;

		result = this.userRepository.findAll();

		return result;
	}

	public User findOne(final int userId) {
		User result;

		Assert.isTrue(userId != 0);

		result = this.userRepository.findOne(userId);

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
			Assert.isTrue(userAccount.getAuthorities().contains(authority));
			Assert.isTrue(userAccount.equals(user.getUserAccount()));
			saved = this.userRepository.findOne(user.getId());
			Assert.notNull(saved);
			Assert.isTrue(saved.getUserAccount().getUsername().equals(user.getUserAccount().getUsername()));
			Assert.isTrue(user.getUserAccount().getPassword().equals(saved.getUserAccount().getPassword()));
		} else {
			/* Si no existe, debe tratarse de anonimo */

			Assert.isTrue(!LoginService.isAuthenticated());
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

	public User reconstruct(final UserForm userForm, final BindingResult binding) {
		User result;

		if (userForm.getId() == 0) {
			result = this.create();

			Assert.notNull(result);
			Assert.isTrue(userForm.getCheckPassword().equals(userForm.getPassword()));
			Assert.isTrue(userForm.isCheck());

			result.getUserAccount().setUsername(userForm.getUsername());
			result.getUserAccount().setPassword(userForm.getPassword());
			
			userForm.setFollowers(result.getFollowers());
		} else {
			result = this.findOne(userForm.getId());
			Assert.notNull(result);
			Assert.isTrue(result.getUserAccount().getUsername().equals(userForm.getUsername()));
			
			result.setFollowers(userForm.getFollowers());
		}

		result.setName(userForm.getName());
		result.setSurname(userForm.getSurname());
		result.setPostalAddress(userForm.getPostalAddress());
		result.setEmailAddress(userForm.getEmailAddress());
		result.setPhoneNumber(userForm.getPhoneNumber());

		this.validator.validate(userForm, binding);

		return result;
	}

}
