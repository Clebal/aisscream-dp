
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Participation extends DomainEntity {

	private int		amountProduct;

	private User	user;

	private Groupon	groupon;


	@Min(0)
	public int getAmountProduct() {
		return this.amountProduct;
	}

	public void setAmountProduct(final int amountProduct) {
		this.amountProduct = amountProduct;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Groupon getGroupon() {
		return this.groupon;
	}

	public void setGroupon(final Groupon groupon) {
		this.groupon = groupon;
	}

}
