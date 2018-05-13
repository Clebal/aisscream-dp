package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Collection;

@Entity
@Access(AccessType.PROPERTY)
public class Tag extends DomainEntity {

	private String name;

	private Collection<Bargain> bargains;

	@NotBlank
	@Column(unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	@Valid
	@ManyToMany
	public Collection<Bargain> getBargains() {
		return bargains;
	}

	public void setBargains(Collection<Bargain> bargains) {
		this.bargains = bargains;
	}

}
