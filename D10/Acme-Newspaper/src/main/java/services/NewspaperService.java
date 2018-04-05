
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.engine.config.spi.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.Validator;

import repositories.NewspaperRepository;
import security.Authority;
import security.LoginService;
import domain.Article;
import domain.Newspaper;
import domain.Subscription;

@Service
@Transactional
public class NewspaperService {

	// Managed repository
	@Autowired
	private NewspaperRepository		newspaperRepository;

	// Supporting services
	@Autowired
	private UserService				userService;

	@Autowired
	private ArticleService			articleService;

	@Autowired
	private SubscriptionService		subscriptionService;

	@Autowired
	private CustomerService			customerService;

	// Constructor
	public NewspaperService() {
		super();
	}

	public Newspaper create() {
		Newspaper result;
		Authority authority;

		authority = new Authority();
		authority.setAuthority("USER");
		Assert.isTrue(LoginService.isAuthenticated());
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		result = new Newspaper();
		result.setPublisher(this.userService.findByUserAccountId(LoginService.getPrincipal().getId()));
		result.setIsPrivate(false);
		result.setHasTaboo(false);
		result.setIsPublished(true);

		return result;
	}

	public Collection<Newspaper> findAll() {
		Collection<Newspaper> result;

		result = this.newspaperRepository.findAll();

		return result;
	}

	public Newspaper findOne(final int newspaperId) {
		Newspaper result;

		Assert.notNull(newspaperId != 0);

		result = this.newspaperRepository.findOne(newspaperId);

		Assert.isTrue(LoginService.isAuthenticated());

		return result;
	}

	//	public Newspaper findOneToEdit(final int newspaperId) {
	//		Newspaper result;
	//		Authority authority;
	//
	//		result = this.findOne(newspaperId);
	//		Assert.notNull(result);
	//		authority = new Authority();
	//		authority.setAuthority("USER");
	//		Assert.isTrue(LoginService.isAuthenticated());
	//
	//		Assert.isTrue(result.getPublisher().getUserAccount().getId() == LoginService.getPrincipal().getId());
	//
	//		return result;
	//
	//	}

	public Newspaper findOneToDisplay(final int newspaperId) {
		Newspaper result;
		Authority authority;
		final Authority authority2;
		Boolean canPermit;
		Date currentMoment;

		result = this.findOne(newspaperId);
		Assert.notNull(result);
		authority = new Authority();
		authority.setAuthority("USER");
		authority = new Authority();
		authority.setAuthority("CUSTOMER");
		currentMoment = new Date();
		canPermit = false;
		if (LoginService.isAuthenticated()) {
			if (LoginService.getPrincipal().getAuthorities().contains(authority)) {
				if (result.getPublisher().getUserAccount().getId() == LoginService.getPrincipal().getId())
					canPermit = true;
				else if (result.getIsPrivate() == false)
					canPermit = true;
				else if (result.getIsPrivate() == true)
					if (!this.articleService.findByUserIdAndNewspaperId(this.userService.findByUserAccountId(LoginService.getPrincipal().getId()).getId(), result.getId()).isEmpty())
						canPermit = true;
			} 
		} else if (result.getIsPrivate() == false && result.getPublicationDate().compareTo(currentMoment) <= 0 && result.getIsPublished() == true)
			canPermit = true;

		Assert.isTrue(canPermit == true);
		return result;

	}

	public Newspaper save(final Newspaper newspaper) {
		Newspaper result;
		Date currentMoment;

		Assert.notNull(newspaper);

		currentMoment = new Date();
		Assert.isTrue(LoginService.isAuthenticated());
		Assert.isTrue(newspaper.getPublisher().getUserAccount().getId() == LoginService.getPrincipal().getId());
		if (newspaper.getId() == 0) {
			Assert.isTrue(newspaper.getPublicationDate().compareTo(currentMoment) >= 0);
			Assert.isTrue(newspaper.getIsPublished() == true);
		}
		result = this.newspaperRepository.save(newspaper);

		return result;
	}

	public void delete(final Newspaper newspaper) {
		Newspaper newspaperToDelete;
		Authority authority;

		authority = new Authority();
		authority.setAuthority("ADMIN");

		newspaperToDelete = this.findOne(newspaper.getId());
		Assert.notNull(newspaperToDelete);

		Assert.isTrue(LoginService.isAuthenticated());
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		for (final Article a : this.articleService.findByNewspaperId(newspaperToDelete.getId()))
			this.articleService.delete(a);

		for (final Subscription s : this.subscriptionService.findByNewspaperId(newspaperToDelete.getId()))
			this.subscriptionService.delete(s);

		this.newspaperRepository.delete(newspaperToDelete);

	}

	public void publish(final int newspaperId) {
		Newspaper newspaperToPublish;
		Date currentMoment;

		newspaperToPublish = this.findOne(newspaperId);
		currentMoment = new Date();
		Assert.notNull(newspaperToPublish);
		Assert.isTrue(LoginService.isAuthenticated());
		Assert.isTrue(newspaperToPublish.getPublisher().getUserAccount().getId() == LoginService.getPrincipal().getId());
		Assert.isTrue(newspaperToPublish.getIsPublished() == true && newspaperToPublish.getPublicationDate().compareTo(currentMoment) < 0);
		newspaperToPublish.setPublicationDate(currentMoment);
		this.newspaperRepository.save(newspaperToPublish);

	}

	public void putPublic(final int newspaperId) {
		Newspaper newspaperToPublic;

		newspaperToPublic = this.findOne(newspaperId);
		Assert.notNull(newspaperToPublic);
		Assert.isTrue(LoginService.isAuthenticated());
		Assert.isTrue(newspaperToPublic.getPublisher().getUserAccount().getId() == LoginService.getPrincipal().getId());
		Assert.isTrue(newspaperToPublic.getIsPrivate() == true);
		newspaperToPublic.setIsPrivate(false);
		this.newspaperRepository.save(newspaperToPublic);

	}

	public void putPrivate(final int newspaperId) {
		Newspaper newspaperToPublic;

		newspaperToPublic = this.findOne(newspaperId);
		Assert.notNull(newspaperToPublic);
		Assert.isTrue(LoginService.isAuthenticated());
		Assert.isTrue(newspaperToPublic.getPublisher().getUserAccount().getId() == LoginService.getPrincipal().getId());
		Assert.isTrue(newspaperToPublic.getIsPrivate() == false);
		newspaperToPublic.setIsPrivate(true);
		this.newspaperRepository.save(newspaperToPublic);

	}

	public Page<Newspaper> findByUserId(final int userId, final int page, final int size) {
		Page<Newspaper> result;

		Assert.isTrue(userId != 0);

		result = this.newspaperRepository.findByUserId(userId, this.getPageable(page, size));

		return result;

	}

	public Page<Newspaper> findByCustomerId(final int customerId, final int page, final int size) {
		Page<Newspaper> result;

		Assert.isTrue(customerId != 0);

		result = this.newspaperRepository.findByCustomerId(customerId, this.getPageable(page, size));

		return result;

	}

	public Page<Newspaper> findPublicsAndPublicated(final int page, final int size) {
		Page<Newspaper> result;

		result = this.newspaperRepository.findPublicsAndPublicated(this.getPageable(page, size));

		return result;

	}

	public Page<Newspaper> findAllPaginated(final int page, final int size) {
		Page<Newspaper> result;

		Assert.isTrue(LoginService.isAuthenticated());
		result = this.newspaperRepository.findAllPaginated(this.getPageable(page, size));

		return result;

	}

	public Collection<Newspaper> findForSubscribe(final int customerId, final int page, final int size) {
		List<Integer> listId;
		List<Newspaper> result;
		Newspaper newspaper;
		Integer tamaño, pageAux, fromId, toId;

		Assert.isTrue(customerId != 0);
		result = new ArrayList<Newspaper>();
		Assert.isTrue(this.customerService.findByUserAccountId(LoginService.getPrincipal().getId()).getId() == customerId);
		listId = new ArrayList<Integer>(this.newspaperRepository.findForSubscribe(customerId));
		tamaño = listId.size();

		pageAux = page;
		if (page <= 0)
			pageAux = 1;

		fromId = (pageAux - 1) * size;
		if (fromId > tamaño)
			fromId = 0;
		toId = (pageAux * size);
		if (tamaño > size) {
			if (toId > tamaño && fromId == 0)
				toId = size;
			else if (toId > tamaño && fromId != 0)
				toId = tamaño;
		} else
			toId = tamaño;

		for (final Integer newspaperId : listId.subList(fromId, toId)) {
			newspaper = this.findOne(newspaperId);

			result.add(newspaper);
		}

		return result;
	}

	private Pageable getPageable(final int page, final int size) {
		Pageable result;

		if (page == 0 || size <= 0)
			result = new PageRequest(0, 5);
		else
			result = new PageRequest(page - 1, size);

		return result;

	}

}
