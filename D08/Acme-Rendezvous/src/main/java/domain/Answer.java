
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Answer extends DomainEntity {

	private String		text;

	private Question	question;

	private Rsvp		rsvp;


	@NotBlank
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Question getQuestion() {
		return this.question;
	}

	public void setQuestion(final Question question) {
		this.question = question;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Rsvp getRsvp() {
		return this.rsvp;
	}

	public void setRsvp(final Rsvp rsvp) {
		this.rsvp = rsvp;
	}

}
