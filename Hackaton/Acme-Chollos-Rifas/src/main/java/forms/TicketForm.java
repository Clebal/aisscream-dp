package forms;

import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import domain.CreditCard;
import domain.Raffle;
import domain.User;

public class TicketForm {

	private String code;

//	private String payMethod;
	
	private User user;

	private Raffle raffle;

	private CreditCard creditCard;

	private int amount;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
//	@Pattern(regexp="^(CREDITCARD||PAYPAL)$")
//	public String getPayMethod() {
//		return this.payMethod;
//	}
//	
//	public void setPayMethod(final String payMethod) {
//		this.payMethod = payMethod;
//	}


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Raffle getRaffle() {
		return raffle;
	}

	public void setRaffle(Raffle raffle) {
		this.raffle = raffle;
	}

	@Valid
	@ManyToOne(optional = true)
	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@Min(1)
	public int getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

}
