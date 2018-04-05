
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Newspaper extends DomainEntity {

	private Date	publicationDate;

	private String	title;

	private String	description;

	private String	picture;

	private boolean	isPrivate;

	private boolean	hasTaboo;

	private boolean	isPublished;

	private User	publisher;


	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getPublicationDate() {
		return this.publicationDate;
	}

	public void setPublicationDate(final Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@URL
	public String getPicture() {
		return this.picture;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	public boolean getIsPrivate() {
		return this.isPrivate;
	}

	public void setIsPrivate(final boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public boolean getHasTaboo() {
		return this.hasTaboo;
	}

	public void setHasTaboo(final boolean hasTaboo) {
		this.hasTaboo = hasTaboo;
	}

	public boolean getIsPublished() {
		return this.isPublished;
	}

	public void setIsPublished(final boolean isPublished) {
		this.isPublished = isPublished;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public User getPublisher() {
		return this.publisher;
	}

	public void setPublisher(final User publisher) {
		this.publisher = publisher;
	}

}
