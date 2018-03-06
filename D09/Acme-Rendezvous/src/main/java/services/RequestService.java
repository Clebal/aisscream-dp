
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import repositories.UserRepository;
import security.Authority;
import security.LoginService;
import domain.Rendezvous;
import domain.Request;
import domain.User;

@Service
@Transactional
public class RequestService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private UserRepository	userRepository;
	
	@Autowired
	private RequestRepository requestRepository;

//	@Autowired
//	private Validator		validator;

	// Supporting
	// services-----------------------------------------------------------

	// Constructors -----------------------------------------------------------
	public RequestService() {
		super();
	}

	// Simple CRUD
	// methods-----------------------------------------------------------
	public Request create(final Rendezvous rendezvous) {
		Request result;
		
		result = new Request();
		result.setRendezvous(rendezvous);

		return result;
	}

	public Collection<Request> findAll() {
		Collection<Request> result;

		result = this.requestRepository.findAll();

		return result;
	}

	public Request findOne(final int requestId) {
		Request result;

		Assert.isTrue(requestId != 0);

		result = this.requestRepository.findOne(requestId);

		return result;
	}

	public Request save(final Request request) {
		Request result;
		Authority authority;
		User user;

		authority = new Authority();
		authority.setAuthority("USER");
		
		/* Solo puede crearlo o editarlo un user y debe ser el del rendezvous*/
		Assert.isTrue(LoginService.isAuthenticated());
		user = this.userRepository.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.isTrue(user.getUserAccount().getAuthorities().contains(authority));
		Assert.isTrue(request.getRendezvous().getCreator().equals(user));
		
		/* Si ya fue creado, no puede cambiar el rendezvous ni el service */ 

		result = this.requestRepository.save(request);

		return result;
	}

	// Other business methods
	
	public Integer countByCreditCardId(final int creditCardId) {
		Integer result;

		Assert.isTrue(creditCardId != 0);

		result = this.requestRepository.countByCreditCardId(creditCardId);

		return result;
	}

	/*public User reconstruct(final UserForm userForm, final BindingResult binding) {
		User result;

		this.validator.validate(userForm, binding);

		if (userForm.getId() == 0) {
			result = this.create();

			Assert.notNull(result);
			Assert.isTrue(userForm.getCheckPassword().equals(userForm.getPassword()));
			Assert.isTrue(userForm.isCheck());

			result.getUserAccount().setUsername(userForm.getUsername());
			result.getUserAccount().setPassword(userForm.getPassword());

		} else {
			result = this.findOne(userForm.getId());
			Assert.notNull(result);
			Assert.isTrue(result.getUserAccount().getUsername().equals(userForm.getUsername()));
		}

		result.setName(userForm.getName());
		result.setSurname(userForm.getSurname());
		result.setAddress(userForm.getAddress());
		result.setBirthdate(userForm.getBirthdate());
		result.setEmail(userForm.getEmail());
		result.setPhone(userForm.getPhone());

		return result;
	}*/

}
