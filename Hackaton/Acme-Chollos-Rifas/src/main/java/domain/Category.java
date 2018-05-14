
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Category extends DomainEntity {

	private String				name;

	private String				image;

	private boolean				defaultCategory;

	private Category			fatherCategory;

	private Collection<Bargain>	bargain;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	@URL
	public String getImage() {
		return this.image;
	}

	public void setImage(final String image) {
		this.image = image;
	}

	public boolean getDefaultCategory() {
		return this.defaultCategory;
	}

	public void setDefaultCategory(final boolean defaultCategory) {
		this.defaultCategory = defaultCategory;
	}

	@ManyToOne(optional = true)
	@Valid
	public Category getFatherCategory() {
		return this.fatherCategory;
	}

	public void setFatherCategory(final Category fatherCategory) {
		this.fatherCategory = fatherCategory;
	}

	@ManyToMany
	@Valid
	@NotNull
	public Collection<Bargain> getBargain() {
		return this.bargain;
	}

	public void setBargain(final Collection<Bargain> bargain) {
		this.bargain = bargain;
	}

}
