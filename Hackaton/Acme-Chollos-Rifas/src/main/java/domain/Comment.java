package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Access(AccessType.PROPERTY)
public class Comment extends DomainEntity {

	private Date moment;

	private String text;

	private String image;

	private Bargain bargain;

	private Comment repliedComment;

	private User user;

	@Past
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMoment() {
		return moment;
	}

	public void setMoment(Date moment) {
		this.moment = moment;
	}

	@NotBlank
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@URL
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Bargain getBargain() {
		return bargain;
	}

	public void setBargain(Bargain bargain) {
		this.bargain = bargain;
	}

	@Valid
	//@NotNull
	@ManyToOne(optional = true)
	public Comment getRepliedComment() {
		return repliedComment;
	}

	public void setRepliedComment(Comment repliedComment) {
		this.repliedComment = repliedComment;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
