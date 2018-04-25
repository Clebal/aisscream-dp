package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Advertisement extends DomainEntity {

	private String title;
	
	private String urlBanner;
	
	private String urlTarget;
	
	private CreditCard creditCard;
	
	private Collection<Newspaper> newspapers;
	
	@NotBlank
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@NotBlank
	@URL
	public String getUrlBanner() {
		return urlBanner;
	}

	public void setUrlBanner(String urlBanner) {
		this.urlBanner = urlBanner;
	}

	@NotBlank
	@URL
	public String getUrlTarget() {
		return urlTarget;
	}

	public void setUrlTarget(String urlTarget) {
		this.urlTarget = urlTarget;
	}

	@NotNull
	@Valid
	@ManyToOne(optional=false)
	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@NotNull
	@Valid
	@ManyToMany
	public Collection<Newspaper> getNewspapers() {
		return newspapers;
	}

	public void setNewspapers(Collection<Newspaper> newspapers) {
		this.newspapers = newspapers;
	}
	
}
