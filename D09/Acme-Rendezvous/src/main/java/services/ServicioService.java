
package services;

import java.util.ArrayList;
import java.util.Collection;
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

import repositories.ServicioRepository;
import security.Authority;
import security.LoginService;
import domain.Category;
import domain.Servicio;

@Service
@Transactional
public class ServicioService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private ServicioRepository	servicioRepository;

	@Autowired
	private Validator			validator;

	// Supporting
	// services-----------------------------------------------------------
	@Autowired
	private ManagerService		managerService;

	@Autowired
	private RequestService		requestService;


	// Constructors -----------------------------------------------------------
	public ServicioService() {
		super();
	}

	public Servicio create() {
		Servicio result;
		Collection<Category> categories;

		categories = new ArrayList<Category>();
		result = new Servicio();
		result.setManager(this.managerService.findByUserAccountId(LoginService.getPrincipal().getId()));
		result.setStatus("ACCEPTED");
		result.setCategories(categories);

		return result;
	}

	public Collection<Servicio> findAll() {
		Collection<Servicio> result;

		result = this.servicioRepository.findAll();

		return result;
	}

	public Servicio findOne(final int servicioId) {
		Servicio result;

		Assert.isTrue(servicioId != 0);

		result = this.servicioRepository.findOne(servicioId);

		return result;
	}

	public Servicio findOneToEdit(final int servicioId) {
		Servicio result;

		Assert.isTrue(servicioId != 0);

		result = this.servicioRepository.findOne(servicioId);

		Assert.notNull(result);

		Assert.isTrue(result.getManager().getUserAccount().getId() == LoginService.getPrincipal().getId());

		return result;
	}

	public Servicio save(final Servicio servicio) {
		Servicio result;
		Servicio saved;

		Assert.notNull(servicio);

		Assert.isTrue(servicio.getManager().getUserAccount().getId() == LoginService.getPrincipal().getId());
		if (servicio.getId() == 0)
			Assert.isTrue(servicio.getStatus().equals("ACCEPTED"));
		else {
			saved = this.findOne(servicio.getId());
			Assert.isTrue(servicio.getManager().getId() == saved.getManager().getId()); //No puede cambiar de manager
			Assert.isTrue(servicio.getStatus().equals(saved.getStatus())); //No puede cambiar el status al editar
		}

		result = this.servicioRepository.save(servicio);

		return result;

	}

	public void delete(final Servicio servicio) {

		Assert.notNull(servicio);

		Assert.isTrue(servicio.getManager().getUserAccount().getId() == LoginService.getPrincipal().getId());

		Assert.isTrue(this.requestService.countByServicioId(servicio.getId()) == 0);

		this.servicioRepository.delete(servicio);

	}

	public void addCategory(final Servicio servicio, final Category category) {

		Assert.notNull(servicio);

		Assert.notNull(category);

		Assert.isTrue(!servicio.getCategories().contains(category));

		Assert.isTrue(servicio.getManager().getUserAccount().getId() == LoginService.getPrincipal().getId());

		servicio.getCategories().add(category);

		this.servicioRepository.save(servicio);

	}

	public void removeCategory(final Servicio servicio, final Category category) {

		Assert.notNull(servicio);

		Assert.notNull(category);

		Assert.isTrue(servicio.getCategories().contains(category));

		Assert.isTrue(servicio.getManager().getUserAccount().getId() == LoginService.getPrincipal().getId());

		servicio.getCategories().remove(category);

		this.servicioRepository.save(servicio);

	}

	public void acceptServicio(final Servicio servicio) {

		Authority authority;

		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		Assert.isTrue(servicio.getStatus().equals("CANCELLED"));

		servicio.setStatus("ACCEPTED");

		this.servicioRepository.save(servicio);
	}

	public void cancelServicio(final Servicio servicio) {

		Authority authority;

		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		Assert.isTrue(servicio.getStatus().equals("ACCEPTED"));

		servicio.setStatus("CANCELLED");

		this.servicioRepository.save(servicio);
	}

	public void saveFromCategory(final Servicio servicio) {
		Assert.notNull(servicio);

		this.servicioRepository.save(servicio);
	}

	public Page<Servicio> findAllPaginated(final int page, final int size) {
		Page<Servicio> result;

		result = this.servicioRepository.findAllPaginated(this.getPageable(page, size));

		return result;

	}

	public Page<Servicio> findByManagerUserAccountId(final int managerUserAccountId, final int page, final int size) {
		Page<Servicio> result;

		Assert.isTrue(managerUserAccountId != 0);

		result = this.servicioRepository.findByManagerUserAccountId(managerUserAccountId, this.getPageable(page, size));

		return result;

	}

	public Page<Servicio> findByCategoryId(final int categoryId, final int page, final int size) {
		Page<Servicio> result;

		Assert.isTrue(categoryId != 0);

		result = this.servicioRepository.findByCategoryId(categoryId, this.getPageable(page, size));

		return result;

	}

	public Collection<Servicio> findByCategoryId(final int categoryId) {
		Collection<Servicio> result;

		Assert.isTrue(categoryId != 0);

		result = this.servicioRepository.findByCategoryIdNotPaginated(categoryId);

		return result;

	}

	public Page<Servicio> findByRendezvousId(final int rendezvousId, final int page, final int size) {
		Page<Servicio> result;

		Assert.isTrue(rendezvousId != 0);

		result = this.servicioRepository.findByRendezvousId(rendezvousId, this.getPageable(page, size));

		return result;

	}

	//	public Collection<Servicio> findByRendezvousIdNotPaginated(final int rendezvousId) {
	//		Collection<Servicio> result;
	//
	//		Assert.isTrue(rendezvousId != 0);
	//
	//		result = this.servicioRepository.findByRendezvousIdNotPaginated(rendezvousId);
	//
	//		return result;
	//
	//	}
	//
	//	public Page<Servicio> findServicesForRequetsByRendezvousId(final int rendezvousId, final int page, final int size) {
	//		Page<Servicio> result;
	//		Collection<Servicio> myServices;
	//
	//		Assert.isTrue(rendezvousId != 0);
	//
	//		myServices = this.findByRendezvousIdNotPaginated(rendezvousId);
	//		result = this.servicioRepository.findServicesForRequetsByRendezvousId(myServices, this.getPageable(page, size));
	//
	//		return result;
	//
	//	}

	public Collection<Servicio> findServicesForRequetsByRendezvousId(final int rendezvousId, final int page) {
		List<Integer> listId;
		List<Servicio> result;
		Servicio servicio;
		Integer tamaño, pageAux, fromId, toId;

		result = new ArrayList<Servicio>();

		listId = new ArrayList<Integer>(this.servicioRepository.findServicesForRequetsByRendezvousId(rendezvousId));
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

		for (final Integer servicioId : listId.subList(fromId, toId)) {
			servicio = this.findOne(servicioId);

			result.add(servicio);
		}

		return result;
	}

	public Integer countFindServicesForRequetsByRendezvousId(final int rendezvousId) {
		Integer result;

		Assert.isTrue(rendezvousId != 0);

		result = this.servicioRepository.countFindServicesForRequetsByRendezvousId(rendezvousId);

		return result;
	}
	public Collection<Servicio> topBestSellingServices(final int size) {
		Collection<Integer> listId;
		List<Servicio> result;
		Servicio servicio;
		Assert.isTrue(size >= 0);

		result = new ArrayList<Servicio>();

		listId = this.servicioRepository.topBestSellingServices(size);

		for (final Integer servicioId : listId) {
			servicio = this.findOne(servicioId);

			result.add(servicio);
		}

		return result;
	}

	public Servicio reconstruct(final Servicio servicio, final BindingResult binding) {
		Servicio result;
		Servicio aux;

		if (servicio.getId() == 0)
			result = servicio;
		else {
			result = servicio;
			aux = this.servicioRepository.findOne(servicio.getId());
			result.setVersion(aux.getVersion());
			result.setManager(aux.getManager());
			result.setStatus(aux.getStatus());
			result.setCategories(aux.getCategories());
			result.setName(servicio.getName());
			result.setDescription(servicio.getDescription());
			result.setPicture(servicio.getPicture());

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
