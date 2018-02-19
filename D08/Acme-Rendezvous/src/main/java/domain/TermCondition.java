
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class TermCondition extends DomainEntity {

	//private String	countryCode;

	//private String	messageCode;

	private String	text;


	//	@NotBlank
	//	public String getCountryCode() {
	//		return this.countryCode;
	//	}
	//
	//	public void setCountryCode(final String countryCode) {
	//		this.countryCode = countryCode;
	//	}
	//
	//	@NotBlank
	//	public String getMessageCode() {
	//		return this.messageCode;
	//	}
	//
	//	public void setMessageCode(final String messageCode) {
	//		this.messageCode = messageCode;
	//	}

	@NotBlank
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}

}
