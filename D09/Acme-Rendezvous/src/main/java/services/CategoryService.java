
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CategoryRepository;
import security.Authority;
import security.LoginService;
import domain.Actor;
import domain.Category;
import domain.Service;

@org.springframework.stereotype.Service
public class CategoryService {

	// Managed repository
	@Autowired
	private CategoryRepository	categoryRepository;

	// Services
	@Autowired
	private ActorService		actorService;

	@Autowired
	private ServiceService		serviceService;

	@Autowired
	private Validator			validator;


	// Constructor
	public CategoryService() {
		super();
	}

	// Simple CRUD methods----------
	public Category create(final Category category) {
		Category result;

		result = new Category();

		//Guardamos categoria padre si la hay
		if (category != null)
			result.setFatherCategory(category);

		result.setDefaultCategory(false);

		return result;
	}

	//Lo usamos para escoger la primera categoría
	//	public Collection<Category> findAll() {
	//		Collection<Category> result;
	//
	//		result = this.categoryRepository.findAll();
	//
	//		return result;
	//	}

	public Category findOne(final int categoryId) {
		Category result;

		Assert.isTrue(categoryId != 0);

		result = this.categoryRepository.findOne(categoryId);

		return result;
	}

	public Category save(final Category category) {
		Authority authority;
		Actor actor;
		Category result;
		Category defaultCategory;

		//Vemos que sea un admin el que modifica las categorias
		authority = new Authority();
		authority.setAuthority("ADMIN");
		actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());

		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		//Cogemos la categoría por defecto. Solo se puede actualizar, no crear ni cambiar una a default.
		defaultCategory = this.categoryRepository.findByDefaultCategory();
		Assert.notNull(defaultCategory);
		if (category.isDefaultCategory())
			Assert.isTrue(defaultCategory.getId() == category.getId());
		if (defaultCategory.getId() != category.getId())
			Assert.isTrue(!category.isDefaultCategory());

		//Vemos que su padre no sea ella misma
		if (category.getFatherCategory() != null)
			Assert.isTrue(category.getFatherCategory().getId() != category.getId());

		result = this.categoryRepository.save(category);

		return result;
	}

	public void delete(final Category category) {
		Authority authority;
		Category saved;
		Collection<Category> childrenCategories;
		Collection<Category> serviceCategories;
		Collection<Service> services;
		Service service;
		Category defaultCategory;

		Assert.notNull(category);

		//Solo borra categorias un admin
		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		//Cogemos la categoría por defecto
		defaultCategory = this.categoryRepository.findByDefaultCategory();
		Assert.notNull(defaultCategory);
		//Vemos que el administrador no borre la categoría por defecto
		Assert.isTrue(category.getId() != defaultCategory.getId());

		childrenCategories = this.findAllByFatherCategoryId(category.getId());
		Assert.notNull(childrenCategories);

		//Si tiene hijos usamos recursión
		for (final Category c : childrenCategories)
			this.delete(c);

		//Cuando ya no tiene hijos, actualizamos el servicio
		services = this.serviceService.findByCategoryId(category.getId());
		for (final Service s : services) {
			service = s;
			serviceCategories = service.getCategories();
			serviceCategories.remove(category);
			service.setCategories(serviceCategories);

			//Vemos que no se quede sin categoría, si es así metemos la categoria por defecto
			if (service.getCategories().size() == 0) {
				serviceCategories.add(defaultCategory);
				service.setCategories(serviceCategories);
			}

			this.serviceService.saveFromCategory(service);

		}

		saved = this.findOne(category.getId());
		Assert.notNull(saved);
		this.categoryRepository.delete(saved);

	}

	public void flush() {
		this.categoryRepository.flush();
	}

	//Other business methods
	public void reorganising(final Category category, final Category newFather) {
		Authority authority;
		Collection<Category> childrenCategory;
		Category defaultCategory;

		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		//La categoría a mover no puede ser null
		Assert.notNull(category);

		//No se puede mover la categoría por defecto
		//Cogemos la categoría por defecto
		defaultCategory = this.categoryRepository.findByDefaultCategory();
		Assert.notNull(defaultCategory);
		Assert.isTrue(!category.equals(defaultCategory));

		//Si quisieramos que solo se moviera una categoría sin sus hijas

		childrenCategory = this.findAllByFatherCategoryId(category.getId());

		//A los hijos le ponemos el abuelo como padre
		for (final Category childCategory : childrenCategory) {
			childCategory.setFatherCategory(category.getFatherCategory());
			this.categoryRepository.save(childCategory);
		}

		//Si son distintas de null, no sean iguales
		if (newFather != null)
			Assert.isTrue(!(category.equals(newFather)));

		category.setFatherCategory(newFather);
		//Movemos una categoría
		this.save(category);

	}

	//Usados para las consultas del usuario para paginar
	public Page<Category> findByFatherCategoryId(final int fatherCategoryId, final int page, final int size) {
		Page<Category> result;

		Assert.isTrue(fatherCategoryId != 0);

		result = this.categoryRepository.findByFatherCategoryId(fatherCategoryId, this.getPageable(page, size));

		return result;
	}

	public Page<Category> findWithoutFather(final int page, final int size) {
		Page<Category> result;

		result = this.categoryRepository.findWithoutFather(this.getPageable(page, size));

		return result;
	}

	//Usados para borrar categorías
	public Collection<Category> findAllByFatherCategoryId(final int fatherCategoryId) {
		Collection<Category> result;

		Assert.isTrue(fatherCategoryId != 0);

		result = this.categoryRepository.findAllByFatherCategoryId(fatherCategoryId);

		return result;
	}

	//Usado en los test
	public Collection<Category> findAllWithoutFather() {
		Collection<Category> result;

		result = this.categoryRepository.findAllWithoutFather();

		return result;
	}

	public Page<Category> findByServiceId(final int serviceId, final int page, final int size) {
		Page<Category> result;

		Assert.isTrue(serviceId != 0);
		result = this.categoryRepository.findByServiceId(serviceId, this.getPageable(page, size));

		return result;
	}

	public double avgNumberCategoriesPerRendezvous() {
		double result;

		result = this.categoryRepository.avgNumberCategoriesPerRendezvous();

		return result;
	}

	public Page<Category> findAllPaginated(final int page, final int size) {
		Page<Category> result;

		result = this.categoryRepository.findAllPaginated(this.getPageable(page, size));

		return result;
	}

	public Category findByDefaultCategory() {
		Category result;

		result = this.categoryRepository.findByDefaultCategory();

		return result;
	}

	//Auxilary methods
	private Pageable getPageable(final int page, final int size) {
		Pageable result;

		if (page == 0 || size <= 0)
			result = new PageRequest(0, 5);
		else
			result = new PageRequest(page - 1, size);

		return result;
	}

	// Pruned object domain
	public Category reconstruct(final Category category, final BindingResult binding) {
		Category aux;

		if (category.getId() != 0) {
			aux = this.categoryRepository.findOne(category.getId());
			category.setVersion(aux.getVersion());
			category.setFatherCategory(aux.getFatherCategory());
			category.setDefaultCategory(aux.isDefaultCategory());
		} else {
			aux = this.create(category.getFatherCategory());
			category.setVersion(aux.getVersion());
			category.setDefaultCategory(false);
		}

		this.validator.validate(category, binding);

		return category;
	}
}
