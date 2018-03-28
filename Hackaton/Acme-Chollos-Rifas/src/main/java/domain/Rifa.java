package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class Rifa extends DomainEntity {

	private int price;
	
	public int getPrice() {
		return this.price;
	}
	
	public void setPrice(final int price) {
		this.price = price;
	}
	
}
