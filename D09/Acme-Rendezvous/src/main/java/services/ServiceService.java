
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ServiceRepository;
import security.Authority;
import security.LoginService;
import domain.Category;
import domain.Rendezvous;
import domain.Service;

@org.springframework.stereotype.Service
@Transactional
public class ServiceService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private ServiceRepository	serviceRepository;

	@Autowired
	private Validator			validator;

	// Supporting
	// services-----------------------------------------------------------
	@Autowired
	private ManagerService		managerService;

	@Autowired
	private RequestService		requestService;

	@Autowired
	private RendezvousService	rendezvousService;


	// Constructors -----------------------------------------------------------
	public ServiceService() {
		super();
	}

	public Service create() {
		Service result;
		Collection<Category> categories;
		Authority authority;

		authority = new Authority();
		authority.setAuthority("MANAGER");
		Assert.isTrue(LoginService.isAuthenticated());
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		categories = new ArrayList<Category>();
		result = new Service();
		result.setManager(this.managerService.findByUserAccountId(LoginService.getPrincipal().getId()));
		result.setStatus("ACCEPTED");
		result.setCategories(categories);

		return result;
	}

	public Collection<Service> findAll() {
		Collection<Service> result;

		result = this.serviceRepository.findAll();

		return result;
	}

	public Service findOne(final int serviceId) {
		Service result;

		Assert.isTrue(serviceId != 0);

		result = this.serviceRepository.findOne(serviceId);

		return result;
	}

	public Service findOneToEdit(final int serviceId) {
		Service result;

		Assert.isTrue(serviceId != 0);

		result = this.serviceRepository.findOne(serviceId);

		Assert.notNull(result);

		Assert.isTrue(LoginService.isAuthenticated());

		Assert.isTrue(result.getManager().getUserAccount().getId() == LoginService.getPrincipal().getId());

		return result;
	}

	public Service save(final Service service) {
		Service result;
		Service saved;

		Assert.notNull(service);

		Assert.isTrue(LoginService.isAuthenticated());
		Assert.isTrue(service.getManager().getUserAccount().getId() == LoginService.getPrincipal().getId());
		if (service.getId() == 0) {
			Assert.isTrue(service.getStatus().equals("ACCEPTED"));
			Assert.isTrue(service.getCategories().isEmpty());
		} else {
			saved = this.findOne(service.getId());
			Assert.isTrue(service.getManager().getId() == saved.getManager().getId()); //No puede cambiar de manager
			Assert.isTrue(service.getStatus().equals(saved.getStatus())); //No puede cambiar el status al editar
			Assert.isTrue(saved.getCategories().containsAll(service.getCategories()));//No pueden cambiar las categorias
		}

		result = this.serviceRepository.save(service);

		return result;

	}

	public void delete(final Service service) {

		Assert.notNull(service);

		Assert.isTrue(LoginService.isAuthenticated());

		Assert.isTrue(service.getManager().getUserAccount().getId() == LoginService.getPrincipal().getId());

		Assert.isTrue(this.requestService.countByServiceId(service.getId()) == 0);

		this.serviceRepository.delete(service);

	}

	public void addCategory(final Service service, final Category category) {

		Assert.notNull(service);

		Assert.notNull(category);

		Assert.isTrue(!service.getCategories().contains(category));

		Assert.isTrue(LoginService.isAuthenticated());

		Assert.isTrue(service.getManager().getUserAccount().getId() == LoginService.getPrincipal().getId());

		service.getCategories().add(category);

		this.serviceRepository.save(service);

	}

	public void removeCategory(final Service service, final Category category) {

		Assert.notNull(service);

		Assert.notNull(category);

		Assert.isTrue(service.getCategories().contains(category));

		Assert.isTrue(LoginService.isAuthenticated());

		Assert.isTrue(service.getManager().getUserAccount().getId() == LoginService.getPrincipal().getId());

		service.getCategories().remove(category);

		this.serviceRepository.save(service);

	}

	public void acceptService(final Service service) {

		Authority authority;

		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.isAuthenticated());
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		Assert.isTrue(service.getStatus().equals("CANCELLED"));

		service.setStatus("ACCEPTED");

		this.serviceRepository.save(service);
	}

	public void cancelService(final Service service) {

		Authority authority;

		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.isAuthenticated());
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		Assert.isTrue(service.getStatus().equals("ACCEPTED"));

		service.setStatus("CANCELLED");

		this.serviceRepository.save(service);
	}

	public void saveFromCategory(final Service service) {
		Assert.notNull(service);

		this.serviceRepository.save(service);
	}

	public Page<Service> findAllPaginated(final int page, final int size) {
		Page<Service> result;
		Assert.isTrue(LoginService.isAuthenticated());

		result = this.serviceRepository.findAllPaginated(this.getPageable(page, size));

		return result;

	}

	public Page<Service> findByManagerUserAccountId(final int managerUserAccountId, final int page, final int size) {
		Page<Service> result;

		Assert.isTrue(managerUserAccountId != 0);
		Assert.isTrue(LoginService.isAuthenticated());

		result = this.serviceRepository.findByManagerUserAccountId(managerUserAccountId, this.getPageable(page, size));

		return result;

	}

	public Page<Service> findByCategoryId(final int categoryId, final int page, final int size) {
		Page<Service> result;

		Assert.isTrue(categoryId != 0);
		Assert.isTrue(LoginService.isAuthenticated());

		result = this.serviceRepository.findByCategoryId(categoryId, this.getPageable(page, size));

		return result;

	}

	public Collection<Service> findByCategoryId(final int categoryId) {
		Collection<Service> result;

		Assert.isTrue(categoryId != 0);
		Assert.isTrue(LoginService.isAuthenticated());

		result = this.serviceRepository.findByCategoryIdNotPaginated(categoryId);

		return result;

	}

	public Page<Service> findByRendezvousId(final int rendezvousId, final int page, final int size) {
		Page<Service> result;

		Assert.isTrue(rendezvousId != 0);
		Assert.isTrue(LoginService.isAuthenticated());

		result = this.serviceRepository.findByRendezvousId(rendezvousId, this.getPageable(page, size));

		return result;

	}

	public Collection<Service> findServicesForRequetsByRendezvousId(final int rendezvousId, final int page) {
		List<Integer> listId;
		List<Service> result;
		Service service;
		Integer tamaño, pageAux, fromId, toId;
		Rendezvous rendezvous;

		result = new ArrayList<Service>();
		rendezvous = this.rendezvousService.findOne(rendezvousId);
		Assert.notNull(rendezvous);
		Assert.isTrue(rendezvous.getCreator().getUserAccount().getId() == LoginService.getPrincipal().getId() && !rendezvous.getIsDeleted());
		listId = new ArrayList<Integer>(this.serviceRepository.findServicesForRequetsByRendezvousId(rendezvousId));
		tamaño = listId.size();

		pageAux = page;
		if (page <= 0)
			pageAux = 1;

		fromId = (pageAux - 1) * 5;
		if (fromId > tamaño)
			fromId = 0;
		toId = (pageAux * 5);
		if (tamaño > 5) {
			if (toId > tamaño && fromId == 0)
				toId = 5;
			else if (toId > tamaño && fromId != 0)
				toId = tamaño;
		} else
			toId = tamaño;

		for (final Integer serviceId : listId.subList(fromId, toId)) {
			service = this.findOne(serviceId);

			result.add(service);
		}

		return result;
	}
	public Integer countFindServicesForRequetsByRendezvousId(final int rendezvousId) {
		Integer result;

		Assert.isTrue(rendezvousId != 0);

		result = this.serviceRepository.countFindServicesForRequetsByRendezvousId(rendezvousId);

		return result;
	}

	public Page<Service> bestSellingServices(final int page, final int size) {
		Page<Service> result;
		Authority authority;

		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		result = this.serviceRepository.bestSellingServices(this.getPageable(page, size));

		return result;

	}

	public Double ratioServicesEachCategory() {
		Double result;
		Authority authority;

		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		result = this.serviceRepository.ratioServicesEachCategory();

		return result;

	}

	public Double[] avgMinMaxStandartDerivationServicesPerRendezvous() {
		Double[] result;
		Authority authority;

		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		result = this.serviceRepository.avgMinMaxStandartDerivationServicesPerRendezvous();

		return result;
	}

	public Collection<Service> topBestSellingServices(final int size) {
		Collection<Integer> listId;
		List<Service> result;
		Service service;
		Assert.isTrue(size == 5 || size == 10 || size == 20);
		Authority authority;

		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		result = new ArrayList<Service>();

		listId = this.serviceRepository.topBestSellingServices(size);

		for (final Integer serviceId : listId) {
			service = this.findOne(serviceId);

			result.add(service);
		}

		return result;
	}

	public Service reconstruct(final Service service, final BindingResult binding) {
		Service result;
		Service aux;

		if (service.getId() == 0)
			result = service;
		else {
			result = service;
			aux = this.serviceRepository.findOne(service.getId());
			result.setVersion(aux.getVersion());
			result.setManager(aux.getManager());
			result.setStatus(aux.getStatus());
			result.setCategories(aux.getCategories());
			result.setName(service.getName());
			result.setDescription(service.getDescription());
			result.setPicture(service.getPicture());

		}

		this.validator.validate(result, binding);

		return result;
	}

	//Auxiliare methods
	private Pageable getPageable(final int page, final int size) {
		Pageable result;

		if (page == 0 || size <= 0)
			result = new PageRequest(0, 5);
		else
			result = new PageRequest(page - 1, size);

		return result;

	}
}
