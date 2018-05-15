package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Company extends Actor implements Surveyer {

	private String companyName;
	
	private String type;

	@NotBlank
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Pattern(regexp="^(SL||AS||AUTONOMO||COOPERATIVA)$")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
