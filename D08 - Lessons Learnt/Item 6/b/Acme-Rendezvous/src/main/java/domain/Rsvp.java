
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
@Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = {
		"attendant_id", "rendezvous_id"
	})
})
public class Rsvp extends DomainEntity {

	private String		status;

	private User		attendant;

	private Rendezvous	rendezvous;


	@NotBlank
	@Pattern(regexp = "^(ACCEPTED|CANCELLED)$")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public User getAttendant() {
		return this.attendant;
	}

	public void setAttendant(final User attendant) {
		this.attendant = attendant;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Rendezvous getRendezvous() {
		return this.rendezvous;
	}

	public void setRendezvous(final Rendezvous rendezvous) {
		this.rendezvous = rendezvous;
	}

}
