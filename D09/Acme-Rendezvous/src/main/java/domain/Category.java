
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Category extends DomainEntity {

	private String		name;

	private String		description;

	private Category	fatherCategory;

	private boolean		defaultCategory;


	public boolean isDefaultCategory() {
		return this.defaultCategory;
	}

	public void setDefaultCategory(final boolean defaultCategory) {
		this.defaultCategory = defaultCategory;
	}

	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Valid
	@ManyToOne(optional = true)
	public Category getFatherCategory() {
		return this.fatherCategory;
	}

	public void setFatherCategory(final Category fatherCategory) {
		this.fatherCategory = fatherCategory;
	}

}
