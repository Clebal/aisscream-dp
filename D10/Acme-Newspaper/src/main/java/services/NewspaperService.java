
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
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

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private Validator				validator;


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

		Assert.isTrue(newspaperId != 0);

		result = this.newspaperRepository.findOne(newspaperId);

		return result;
	}

	public Newspaper findOneToDisplay(final int newspaperId) {
		Newspaper result;
		Authority authority;
		Authority authority2;
		Authority authority3;
		Boolean canPermit;
		Date currentMoment;

		result = this.findOne(newspaperId);
		Assert.notNull(result);
		authority = new Authority();
		authority.setAuthority("USER");
		authority2 = new Authority();
		authority2.setAuthority("CUSTOMER");
		authority3 = new Authority();
		authority3.setAuthority("ADMIN");
		currentMoment = new Date();
		canPermit = false;
		if (LoginService.isAuthenticated()) {
			if (LoginService.getPrincipal().getAuthorities().contains(authority)) {
				if (result.getPublisher().getUserAccount().getId() == LoginService.getPrincipal().getId())
					canPermit = true;
				else if (result.getPublicationDate().compareTo(currentMoment) <= 0 && result.getIsPublished() == true)
					canPermit = true;

			} else if (LoginService.getPrincipal().getAuthorities().contains(authority2)) {
				if (result.getPublicationDate().compareTo(currentMoment) <= 0 && result.getIsPublished() == true)
					canPermit = true;
				else if (this.subscriptionService.findByCustomerAndNewspaperId(this.customerService.findByUserAccountId(LoginService.getPrincipal().getId()).getId(), result.getId()) != null)
					canPermit = true;

			} else if (LoginService.getPrincipal().getAuthorities().contains(authority3))
				canPermit = true;
		} else if (result.getPublicationDate().compareTo(currentMoment) <= 0 && result.getIsPublished() == true && result.getIsPrivate() == false)
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
			if (this.checkTabooWords(newspaper) == true)
				newspaper.setHasTaboo(true);
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
			this.articleService.deleteFromNewspaper(a);

		for (final Subscription s : this.subscriptionService.findByNewspaperId(newspaperToDelete.getId()))
			this.subscriptionService.deleteFromNewspaper(s);

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
		Assert.isTrue(newspaperToPublish.getIsPublished() == true && newspaperToPublish.getPublicationDate().compareTo(currentMoment) > 0);
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

	public Page<Newspaper> findTaboos(final int page, final int size) {
		Page<Newspaper> result;
		Authority authority;

		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.isAuthenticated());
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		result = this.newspaperRepository.findTaboos(this.getPageable(page, size));

		return result;

	}

	public Page<Newspaper> find10PercentageMoreAvg(final int page, final int size) {
		Page<Newspaper> result;
		Authority authority;

		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.isAuthenticated());
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		result = this.newspaperRepository.find10PercentageMoreAvg(this.getPageable(page, size));

		return result;

	}

	public Collection<Newspaper> findForSubscribe(final int customerId, final int page, final int size) {
		List<Integer> listId;
		List<Newspaper> result;
		Newspaper newspaper;
		Integer tamaño, pageAux, fromId, toId;

		Assert.isTrue(customerId != 0);
		result = new ArrayList<Newspaper>();
		Assert.isTrue(LoginService.isAuthenticated());
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

	public Integer countFindForSubscribe(final int customerId) {
		Integer result;

		Assert.isTrue(customerId != 0);

		result = this.newspaperRepository.countFindForSubscribe(customerId);

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

	public Newspaper reconstruct(final Newspaper newspaper, final BindingResult binding) {
		Newspaper result;

		result = newspaper;

		this.validator.validate(result, binding);

		return result;
	}

	public boolean checkTabooWords(final Newspaper newspaper) {
		Collection<String> tabooWords;
		boolean result;

		result = false;
		tabooWords = this.configurationService.findTabooWords();

		for (final String tabooWord : tabooWords) {
			result = newspaper.getTitle() != null && newspaper.getTitle().toLowerCase().contains(tabooWord) || newspaper.getDescription() != null && newspaper.getDescription().toLowerCase().contains(tabooWord);
			if (result == true)
				break;
		}

		return result;
	}

	public boolean canPermit(final int newspaperId) {
		Newspaper newspaper;
		Authority authority;
		Authority authority2;
		Authority authority3;
		Boolean result;
		Date currentMoment;

		newspaper = this.findOne(newspaperId);
		Assert.notNull(newspaper);
		authority = new Authority();
		authority.setAuthority("USER");
		authority2 = new Authority();
		authority2.setAuthority("CUSTOMER");
		authority3 = new Authority();
		authority3.setAuthority("ADMIN");
		currentMoment = new Date();
		result = false;
		if (LoginService.isAuthenticated()) {
			if (LoginService.getPrincipal().getAuthorities().contains(authority)) {
				if (newspaper.getPublisher().getUserAccount().getId() == LoginService.getPrincipal().getId())
					result = true;
				else if (newspaper.getPublicationDate().compareTo(currentMoment) <= 0 && newspaper.getIsPublished() == true)
					result = true;

			} else if (LoginService.getPrincipal().getAuthorities().contains(authority2)) {
				if (newspaper.getPublicationDate().compareTo(currentMoment) <= 0 && newspaper.getIsPublished() == true)
					result = true;
				else if (this.subscriptionService.findByCustomerAndNewspaperId(this.customerService.findByUserAccountId(LoginService.getPrincipal().getId()).getId(), newspaper.getId()) != null)
					result = true;

			} else if (LoginService.getPrincipal().getAuthorities().contains(authority3))
				result = true;
		} else if (newspaper.getPublicationDate().compareTo(currentMoment) <= 0 && newspaper.getIsPublished() == true && newspaper.getIsPrivate() == false)
			result = true;

		return result;

	}
}
