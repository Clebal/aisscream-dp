
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import domain.Actor;
import domain.Bargain;
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
		authority.setAuthority("MODERATOR");

		/* Si el user ya existe, debe ser el que este logueado */
		if (user.getId() != 0) {
			userAccount = LoginService.getPrincipal();
			Assert.notNull(userAccount);
			Assert.isTrue(userAccount.getAuthorities().contains(authority) || user.getUserAccount().equals(LoginService.getPrincipal()));
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

	public Page<User> findAllPaginated(final int page, final int size) {
		Page<User> result;

		result = this.userRepository.findAllPageable(this.getPageable(page, size));

		return result;
	}
	
	public Page<User> findOrderByPoints(final int page, final int size) {
		Page<User> result;

		result = this.userRepository.findOrderByPoints(this.getPageable(page, size));

		return result;
	}
	
	public void addPoints(final int points) {
		User user;
		
		Assert.isTrue(LoginService.isAuthenticated());
		
		user = this.userRepository.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(user);
		
		user.setPoints(user.getPoints()+points);
		
		this.save(user);
	}
	
	public void ban(final User user) {
		Authority authority;
		
		Assert.notNull(user);
		
		authority = new Authority();
		authority.setAuthority("MODERATOR");
		
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		
		if(user.getUserAccount().isEnabled()) {
			user.getUserAccount().setEnabled(false);
		} else {
			user.getUserAccount().setEnabled(true);
		}
		
		this.save(user);

	}
	
	public void changeWishList(final User user) {
		
		Assert.notNull(user);
		Assert.isTrue(user.getUserAccount().equals(LoginService.getPrincipal()));

		if(user.getIsPublicWishList()) 
			user.setIsPublicWishList(false);
		else
			user.setIsPublicWishList(true);
		
		this.save(user);
		
	}

	public void addBargainToWishList(final Bargain bargain) {
		User user;
		
		Assert.notNull(bargain);
		
		Assert.isTrue(LoginService.isAuthenticated());
		user = this.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(user);
		
		user.getWishList().add(bargain);
		
		this.save(user);
	}
	
	public void removeBargainFromWishList(final Bargain bargain) {
		User user;
		
		Assert.notNull(bargain);
		
		Assert.isTrue(LoginService.isAuthenticated());
		user = this.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(user);
		
		user.getWishList().remove(bargain);
		
		this.save(user);
	}
	
	public Collection<Actor> findWithGoldPremium() {
		Collection<Actor> result;

		result = this.userRepository.findWithGoldPremium();

		return result;
	}
	
	public Collection<Actor> findWithBasicPremium() {
		Collection<Actor> result;

		result = this.userRepository.findWithBasicPremium();

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
	
	public User reconstruct(final UserForm userForm, final BindingResult binding) {
		User result;
		Collection<Bargain> wishList;
		
		wishList = new ArrayList<Bargain>();
		
		if (userForm.getId() == 0) {
			result = this.create();

			Assert.notNull(result);
			Assert.isTrue(userForm.getCheckPassword().equals(userForm.getPassword()));
			Assert.isTrue(userForm.isCheck());

			result.getUserAccount().setUsername(userForm.getUsername());
			result.getUserAccount().setPassword(userForm.getPassword());
			
			// Por defecto, un usuario tiene 50 puntos
			result.setPoints(50);
			
			result.setWishList(wishList);
		} else {
			result = this.findOne(userForm.getId());
			Assert.notNull(result);
			Assert.isTrue(result.getUserAccount().getUsername().equals(userForm.getUsername()));			
		}

		result.setName(userForm.getName());
		result.setSurname(userForm.getSurname());
		result.setAddress(userForm.getAddress());
		result.setEmail(userForm.getEmail());
		result.setPhone(userForm.getPhone());
		result.setIdentifier(userForm.getIdentifier());
		result.setAvatar(userForm.getAvatar());

		this.validator.validate(userForm, binding);

		return result;
	}

}
