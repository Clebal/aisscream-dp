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

	public String text;

	private Question question;

	private Rsvp rsvp;

	@NotBlank
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Rsvp getRsvp() {
		return rsvp;
	}

	public void setRsvp(Rsvp rsvp) {
		this.rsvp = rsvp;
	}

}
