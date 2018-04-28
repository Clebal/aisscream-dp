
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class Moderator extends Actor {
	
//	private string	vat;
//
//	@pattern(regexp = "^(?![\\w]+$)(?=.{2,12}$)[-0-9]*(?:[a-za-z-0-9]*){0,3}$")
//	@column(unique = true)
//	@notblank
//	public string getvat() {
//		return this.vat;
//	}
//
//	public void setvat(final string vat) {
//		this.vat = vat;
//	}

}
