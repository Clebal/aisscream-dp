package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Collection;

@Entity
@Access(AccessType.PROPERTY)
public class Tag extends DomainEntity {

	public String name;

	private Collection<Bargain> bargain;

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
	@ManyToOne(optional = false)
	public Collection<Bargain> getBargain() {
		return bargain;
	}

	public void setBargain(Collection<Bargain> bargain) {
		this.bargain = bargain;
	}

}
