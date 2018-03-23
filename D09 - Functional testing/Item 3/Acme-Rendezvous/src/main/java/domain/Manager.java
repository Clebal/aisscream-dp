
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Manager extends Actor {

	private String	vat;

	@Pattern(regexp = "^(?![\\W]+$)(?=.{2,12}$)[-0-9]*(?:[a-zA-Z-0-9]*){0,3}$")
	@Column(unique = true)
	@NotBlank
	public String getVat() {
		return this.vat;
	}

	public void setVat(final String vat) {
		this.vat = vat;
	}

}
