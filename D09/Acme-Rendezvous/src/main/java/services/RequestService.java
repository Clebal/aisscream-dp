
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.RequestRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.CreditCard;
import domain.Rendezvous;
import domain.Request;
import domain.Service;
import domain.User;

@org.springframework.stereotype.Service
@Transactional
public class RequestService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private UserService			userService;

	@Autowired
	private RequestRepository	requestRepository;

	@Autowired
	private CreditCardService	creditCardService;


	// Supporting
	// services-----------------------------------------------------------

	@Autowired
	private Validator			validator;
	
	// Constructors -----------------------------------------------------------
	public RequestService() {
		super();
	}

	// Simple CRUD
	// methods-----------------------------------------------------------
	public Request create(final Rendezvous rendezvous, final Service service) {
		Request result;

		result = new Request();
		result.setRendezvous(rendezvous);
		result.setService(service);

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

		/* Solo puede crearlo o editarlo un user y debe ser el del rendezvous */
		Assert.isTrue(LoginService.isAuthenticated());
		user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(user);
		Assert.isTrue(user.getUserAccount().getAuthorities().contains(authority));
		Assert.isTrue(request.getRendezvous().getCreator().equals(user));

		/* El rendezvous no pueda estar borrado y además debes mirar que no exista un request ya para ese service y ese rendezvous */

		Assert.isTrue(!request.getRendezvous().getIsDeleted());
		Assert.isNull(this.findRequestEqualRendezvousService(request.getRendezvous().getId(), request.getService().getId()));

		/* La creditCard debe pertenecer al usuario */

		creditCards = this.creditCardService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.isTrue(creditCards.contains(request.getCreditCard()));

		result = this.requestRepository.save(request);

		return result;
	}

	public void delete(final Request request) {
		User user;
		Request requestToDelete;

		requestToDelete = this.findOne(request.getId());
		Assert.notNull(requestToDelete);

		//Lo borra el creador de la request
		user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(user);
		Assert.isTrue(requestToDelete.getRendezvous().getCreator().equals(user));

		this.requestRepository.delete(requestToDelete);

	}

	// Other business methods

	public Request findRequestEqualRendezvousService(final int rendezvousId, final int serviceId) {
		Request result;

		Assert.isTrue(rendezvousId != 0 && serviceId != 0);

		result = this.requestRepository.findRequestEqualRendezvousService(rendezvousId, serviceId);

		return result;
	}

	public Collection<Request> findAllPaginated(final int userId, final int page, final int size) {
		Collection<Request> result;
		Pageable pageable;
		Authority authority;
		UserAccount userAccount;

		authority = new Authority();
		authority.setAuthority("USER");

		if (LoginService.isAuthenticated()) {
			userAccount = LoginService.getPrincipal();
			Assert.notNull(userAccount);
			Assert.isTrue(userAccount.getAuthorities().contains(authority));
		}

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

	public Integer countByServiceId(final int serviceId) {
		Integer result;

		Assert.isTrue(serviceId != 0);

		result = this.requestRepository.countByServiceId(serviceId);

		return result;
	}

	// Pruned object domain
	public Request reconstruct(final Request request, final BindingResult binding) {
			Request result;

			result = request;

			result.setVersion(request.getVersion());
			result.setRendezvous(request.getRendezvous());
			result.setService(request.getService());
			result.setCreditCard(request.getCreditCard());
			result.setComments(request.getComments());

			this.validator.validate(result, binding);

			return result;
		}

}
