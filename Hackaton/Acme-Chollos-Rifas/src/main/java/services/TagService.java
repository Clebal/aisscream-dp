
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.TagRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Bargain;
import domain.Company;
import domain.Tag;

@Service
@Transactional
public class TagService {

	// Managed repository -----------------------------------------------------
	
	@Autowired
	private TagRepository			tagRepository;

	//Supporting services -----------------------------------------------------------

	@Autowired
	private CompanyService			companyService;
	
	@Autowired
	private Validator				validator;

	// Constructors -----------------------------------------------------------
	
	public TagService() {
		super();
	}

	// Simple CRUD methods -----------------------------------------------------------
	
	public Tag create(final Bargain bargain) {
		Tag result;
		Collection<Bargain> bargains;

		bargains = new HashSet<Bargain>();
		bargains.add(bargain);
		result = new Tag();
		result.setBargains(bargains);

		return result;
	}

	public Tag findOne(final int tagId) {
		Tag result;

		Assert.isTrue(tagId != 0);
		result = this.tagRepository.findOne(tagId);

		return result;
	}

	public Collection<Tag> findAll() {
		Collection<Tag> result;

		result = this.tagRepository.findAll();

		return result;
	}

	public Tag save(final Tag tag) {
		Tag result, aux;
		Authority authority;
		UserAccount userAccount;
		Company company;

		Assert.notNull(tag);

		userAccount = LoginService.getPrincipal();

		company = this.companyService.findByUserAccountId(userAccount.getId());
		Assert.notNull(company);
		
		// Las compañías pueden crearlas y editarlas
		
		authority = new Authority();
		authority.setAuthority("COMPANY");
		Assert.isTrue(userAccount.getAuthorities().contains(authority));

		aux = this.findByName(tag.getName());
		if (aux == null) {
			aux = tag;
		} else { 
			aux.getBargains().addAll(tag.getBargains());
		}
		
		result = this.tagRepository.save(aux);

		return result;
	}

	public void delete(final Tag tag) {
		Authority authority;
		UserAccount userAccount;

		Assert.notNull(tag);
		userAccount = LoginService.getPrincipal();

		// Las compañías pueden borrarlas
		authority = new Authority();
		authority.setAuthority("COMAPNY");
		Assert.isTrue(userAccount.getAuthorities().contains(authority));

		this.tagRepository.delete(tag);
	}

	//Other business methods -------------------------------------------------

	public Collection<Tag> findByBargain(Bargain bargain) {
		Collection<Tag> tags;
		
		Assert.notNull(bargain);
		
		tags = this.tagRepository.findByBargain(bargain);
		
		return tags;
	}
	
	public Tag findByName(String name) {
		Tag tag;
		
		Assert.notNull(name);
		
		tag = this.tagRepository.findByName(name);
		
		return tag;
	}

	public Tag reconstruct(final Tag tag, final BindingResult binding) {
		Tag result, aux;

		if (tag.getId() == 0)
			result = tag;
		else {
			result = tag;
			aux = this.tagRepository.findOne(tag.getId());
			result.setVersion(aux.getVersion());
			result.setName(aux.getName());
			result.setBargains(aux.getBargains());
		}

		this.validator.validate(result, binding);

		return result;
	}
	
}
