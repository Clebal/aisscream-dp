
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

import repositories.CategoryRepository;
import security.Authority;
import security.LoginService;
import domain.Actor;
import domain.Category;
import domain.Servicio;

@Service
@Transactional
public class CategoryService {

	// Managed repository
	@Autowired
	private CategoryRepository	categoryRepository;

	// Services
	@Autowired
	private ActorService		actorService;

	@Autowired
	private ServicioService		servicioService;

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

		return result;
	}

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

		//Vemos que sea un admin el que modifica las categorias
		authority = new Authority();
		authority.setAuthority("ADMIN");
		actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());

		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

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
		Collection<Category> childrenCategoriesOfChildren;
		Collection<Category> servicioCategories;
		Servicio servicio;

		Assert.notNull(category);

		//Solo borra categorias un admin
		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));

		childrenCategories = this.findAllByFatherCategoryId(category.getId());
		Assert.notNull(childrenCategories);

		for (final Category c : childrenCategories) {

			childrenCategoriesOfChildren = this.findAllByFatherCategoryId(c.getId());
			//Si borras una categoria borras sus hijas empezando desde el fondo del arbol
			if (childrenCategoriesOfChildren && this.servicioService.findByCategoryId(c.getId()).isEmpty())
				this.categoryRepository.delete(c);
			else
				this.delete(c);
		}

		for (final Servicio s : this.servicioService.findByCategoryId(category.getId())) {
			servicio = s;
			servicioCategories = servicio.getCategories();
			servicioCategories.remove(category);
			servicio.setCategories(servicioCategories);
			this.servicioService.saveFromCategory(servicio);

		}

		saved = this.findOne(category.getId());
		Assert.notNull(saved);
		this.categoryRepository.delete(saved);

	}

	//Other business methods
	public void reorganising(final Category category, final Category newFather) {
		Authority authority;
		//Si quisieramos que solo se moviera una categorķa sin sus hijas
		//		Collection<Category> childrenCategory;
		//		
		//		childrenCategory= findByFatherCategoryId(category.getId());
		//		
		//		//A los hijos le ponemos el abuelo como padre
		//		for(Category childCategory: childrenCategory){
		//			childCategory.setFatherCategory(category.getFatherCategory());
		//			categoryRepository.save(childCategory);
		//		}

		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		//Movemos una categorķa
		category.setFatherCategory(newFather);
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

	//Usados para borrar categorķas
	public Collection<Category> findAllByFatherCategoryId(final int fatherCategoryId) {
		Collection<Category> result;

		Assert.isTrue(fatherCategoryId != 0);

		result = this.categoryRepository.findAllByFatherCategoryId(fatherCategoryId);

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
		} else {
			aux = this.create(category.getFatherCategory());
			category.setVersion(aux.getVersion());
		}

		this.validator.validate(category, binding);

		return category;
	}
}
