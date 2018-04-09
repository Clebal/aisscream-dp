package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import utilities.URLCollection;

@Entity
@Access(AccessType.PROPERTY)
public class Article extends DomainEntity {

	private String title;

	private Date moment;

	private String summary;

	private String body;

	private Collection<String> pictures;

	private boolean isFinalMode;
	
	private boolean hasTaboo;

	private Newspaper newspaper;

	private User writer;

	@NotBlank
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@NotNull
	@Past
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMoment() {
		return moment;
	}

	public void setMoment(Date moment) {
		this.moment = moment;
	}

	@NotBlank
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@NotBlank
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@NotNull
	@URLCollection
	@ElementCollection 
	public Collection<String> getPictures() {
		return pictures;
	}

	public void setPictures(Collection<String> pictures) {
		this.pictures = pictures;
	}

	public boolean getIsFinalMode() {
		return isFinalMode;
	}

	public void setIsFinalMode(boolean isFinalMode) {
		this.isFinalMode = isFinalMode;
	}
	
	public boolean getHasTaboo() {
		return hasTaboo;
	}
	
	public void setHasTaboo(boolean hasTaboo) {
		this.hasTaboo = hasTaboo;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Newspaper getNewspaper() {
		return newspaper;
	}

	public void setNewspaper(Newspaper newspaper) {
		this.newspaper = newspaper;
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

}
