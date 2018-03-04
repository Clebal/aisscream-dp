package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Category extends DomainEntity{

	private String name;

	private String description;

	private Category fatherCategory;

	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotBlank
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(optional = true)
	public Category getFatherCategory() {
		return fatherCategory;
	}

	public void setFatherCategory(Category fatherCategory) {
		this.fatherCategory = fatherCategory;
	}

	
}
