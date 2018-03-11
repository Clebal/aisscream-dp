
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import security.Authority;
import security.LoginService;
import domain.CreditCard;
import domain.Rendezvous;
import domain.Request;
import domain.Servicio;
import domain.User;

@Service
@Transactional
public class RequestService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private UserService userService;
	
	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private CreditCardService		creditCardService;

	// Supporting
	// services-----------------------------------------------------------

	// Constructors -----------------------------------------------------------
	public RequestService() {
		super();
	}

	// Simple CRUD
	// methods-----------------------------------------------------------
	public Request create(final Rendezvous rendezvous, final Servicio servicio) {
		Request result;
		
		result = new Request();
		result.setRendezvous(rendezvous);
		result.setServicio(servicio);

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
		Collection<CreditCard> creditCards;

		authority = new Authority();
		authority.setAuthority("USER");
		
		/* Solo puede crearlo o editarlo un user y debe ser el del rendezvous*/
		Assert.isTrue(LoginService.isAuthenticated());
		user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.isTrue(user.getUserAccount().getAuthorities().contains(authority));
		Assert.isTrue(request.getRendezvous().getCreator().equals(user));
		
		/*  El rendezvous no pueda estar borrado y además debes mirar que no exista un request ya para ese servicio y ese rendezvous */ 

		Assert.isTrue(!request.getRendezvous().getIsDeleted());
		Assert.isNull(this.findRequestEqualRendezvousServicio(request.getRendezvous().getId(), request.getServicio().getId()));
		
		/* La creditCard debe pertenecer al usuario */
		
		creditCards = this.creditCardService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.isTrue(creditCards.contains(request.getCreditCard()));
		
		result = this.requestRepository.save(request);

		return result;
	}
	
	public void delete(final Request request) {
		User user;

		Assert.notNull(request);

		//Lo borra el creador de la question
		user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(user);
		Assert.isTrue(request.getRendezvous().getCreator().equals(user));

		this.requestRepository.delete(request);

	}

	// Other business methods
	
	public Request findRequestEqualRendezvousServicio(final int rendezvousId, final int servicioId) {
		Request result;

		Assert.isTrue(rendezvousId != 0 && servicioId != 0);

		result = this.requestRepository.findRequestEqualRendezvousServicio(rendezvousId, servicioId);

		return result;
	}
	
	public Collection<Request> findAllPaginated(final int userId, final int page, final int size) {
		Collection<Request> result;
		Pageable pageable;

		Assert.isTrue(userId != 0);
		
		if (page == 0 || size <= 0)
			pageable = new PageRequest(0, 5);
		else
			pageable = new PageRequest(page - 1, size);

		result = this.requestRepository.findAllPageable(userId, pageable).getContent();

		return result;
	}

	public Integer countAllPaginated(final int userId) {
		Integer result;
		
		Assert.isTrue(userId != 0);

		result = this.requestRepository.findAllCount(userId);

		return result;
	}
	
	public Integer countByCreditCardId(final int creditCardId) {
		Integer result;

		Assert.isTrue(creditCardId != 0);

		result = this.requestRepository.countByCreditCardId(creditCardId);

		return result;
	}
	
	public Integer countByServicioId(final int servicioId) {
		Integer result;

		Assert.isTrue(servicioId != 0);

		result = this.requestRepository.countByServicioId(servicioId);

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
