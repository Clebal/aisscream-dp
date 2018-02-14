package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class RSVP extends DomainEntity {

	public String status;

	public User attendant;

	private Rendezvous rendezvous;

	@NotBlank
	@Pattern(regexp = "ACCEPTED|CANCELLED")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getAttendant() {
		return attendant;
	}

	public void setAttendant(User attendant) {
		this.attendant = attendant;
	}

	@ManyToOne(optional = false)
	public Rendezvous getRendezvous() {
		return rendezvous;
	}

	public void setRendezvous(Rendezvous rendezvous) {
		this.rendezvous = rendezvous;
	}

}
