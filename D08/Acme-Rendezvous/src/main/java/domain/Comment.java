package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Comment extends DomainEntity {

	public Date moment;

	public String text;

	public String picture;

	public User writer;

	public Comment repliedComment;

	private Rendezvous rendezvous;

	@Past
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMoment() {
		return moment;
	}

	public void setMoment(Date moment) {
		this.moment = moment;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@URL
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public User getWriter() {
		return writer;
	}

	public void setWriter(User writer) {
		this.writer = writer;
	}
	
	@Valid
	@ManyToOne(optional = true)
	public Comment getRepliedComment() {
		return repliedComment;
	}

	public void setRepliedComment(Comment repliedComment) {
		this.repliedComment = repliedComment;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Rendezvous getRendezvous() {
		return rendezvous;
	}

	public void setRendezvous(Rendezvous rendezvous) {
		this.rendezvous = rendezvous;
	}
	
}
