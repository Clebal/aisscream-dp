
package services;

import java.util.Collection;

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
import domain.Administrator;
import domain.Bargain;
import domain.Tag;

@Service
@Transactional
public class TagService {

	// Managed repository -----------------------------------------------------
	
	@Autowired
	private TagRepository			tagRepository;

	//Supporting services -----------------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;
	
	@Autowired
	private Validator				validator;

	// Constructors -----------------------------------------------------------
	
	public TagService() {
		super();
	}

	// Simple CRUD methods -----------------------------------------------------------
	
	public Tag create() {
		Tag result;

		result = new Tag();

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
		Tag result;
		Authority authority;
		UserAccount userAccount;
		Administrator administrator;

		Assert.notNull(tag);

		userAccount = LoginService.getPrincipal();

		administrator = this.administratorService.findByUserAccountId(userAccount.getId());
		Assert.notNull(administrator);
		
		// Las compañías pueden crearlas y editarlas
		
		authority = new Authority();
		authority.setAuthority("COMPANY");
		Assert.isTrue(userAccount.getAuthorities().contains(authority));

		result = this.tagRepository.save(tag);

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
	

	public Tag reconstruct(final Tag tag, final BindingResult binding) {
		Tag result, aux;

		if (tag.getId() == 0)
			result = tag;
		else {
			result = tag;
			aux = this.tagRepository.findOne(tag.getId());
			result.setVersion(aux.getVersion());
			result.setName(aux.getName());
			result.setBargain(aux.getBargain());
		}

		this.validator.validate(result, binding);

		return result;
	}
	
}
