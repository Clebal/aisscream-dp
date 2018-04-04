package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Customer;
import domain.Newspaper;
import domain.Subscription;

import repositories.SubscriptionRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class SubscriptionService {

	// Managed repository
	@Autowired
	private SubscriptionRepository subscriptionRepository;
	
	// Supporting service
	@Autowired
	private Validator			validator;
	
	// Constructor
	public SubscriptionService() {
		super();
	}
	
	// Simple CRUD methods
	public Subscription create(final Customer customer, final Newspaper newspaper) {
		Subscription result;
		
		result = new Subscription();
		result.setCustomer(customer);
		result.setNewspaper(newspaper);
		
		return result;
	}
	
	public Collection<Subscription> findAll() {
		Collection<Subscription> result;
		
		result = this.subscriptionRepository.findAll();
		
		return result;
	}
	
	public Subscription findOne(final int subscriptionId) {
		Subscription result;
		
		Assert.isTrue(subscriptionId != 0);
		
		result = this.subscriptionRepository.findOne(subscriptionId);
		
		return result;
	}
	
	public Subscription findOneToEdit(final int subscriptionId) {
		Subscription result;
		
		Assert.isTrue(subscriptionId != 0);
		
		result = this.subscriptionRepository.findOne(subscriptionId);
		
		// Solo puede ser editado por el usuario adjunto
		Assert.isTrue(result.getCustomer().getUserAccount().equals(LoginService.getPrincipal()));
		
		return result;
	}
	
	public Subscription save(final Subscription subscription) {
		Subscription result;
		Authority authority;
		
		Assert.notNull(subscription);
		
		// Solo puede ser añadido por un usuario
		authority = new Authority();
		authority.setAuthority("CUSTOMER");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		
		// Solo puede ser añadido por el user de Subscription
		Assert.isTrue(subscription.getCustomer().getUserAccount().equals(LoginService.getPrincipal()));
		
		result = this.subscriptionRepository.save(subscription);
		
		return result;
	}
	
	public void delete(final Subscription subscription) {
		Subscription savedSubscription;
		
		Assert.notNull(subscription);
		
		savedSubscription = this.subscriptionRepository.findOne(subscription.getId());
		
		// Solo puede borrarlo el creador del mismo
		Assert.isTrue(savedSubscription.getCustomer().getUserAccount().equals(LoginService.getPrincipal()));
		
		this.subscriptionRepository.delete(savedSubscription);
		
	}
	
	// Other business methods
	public Page<Subscription> findByUserAccountId(final int userAccountId, final int page, final int size) {
		Page<Subscription> result;
		Authority authority;
		UserAccount userAccount;

		authority = new Authority();
		authority.setAuthority("CUSTOMER");

		if(LoginService.isAuthenticated()) {
			userAccount = LoginService.getPrincipal();
			Assert.notNull(userAccount);
			Assert.isTrue(userAccount.getAuthorities().contains(authority));
		}
		
		Assert.isTrue(userAccountId != 0);
		
		result = this.subscriptionRepository.findByUserAccountId(userAccountId, this.getPageable(page, size));
		
		return result;
	}
	
	public Collection<Subscription> findByUserAccountId(final int userAccountId) {
		Collection<Subscription> result;
		
		Assert.isTrue(userAccountId != 0);
		
		result = this.subscriptionRepository.findByUserAccountId(userAccountId);
		
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
	
	// Pruned object domain
	public Subscription reconstruct(final Subscription subscription, final BindingResult binding) {
		Subscription aux;

		if(subscription.getId() != 0) {
			aux = this.subscriptionRepository.findOne(subscription.getId());
			
			subscription.setVersion(aux.getVersion());
			subscription.setCustomer(aux.getCustomer());
		}
		
		this.validator.validate(subscription, binding);

		return subscription;
	}
	
}
