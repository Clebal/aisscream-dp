package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "year")
})
public class Volume extends DomainEntity {

	private String title;
	
	private String description;
	
	private int year;
		
	private User user;
	
	private Collection<CreditCard> subscriptions;
	
	private Collection<Newspaper> newspapers;

	@NotBlank
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@NotBlank
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Min(0)
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@NotNull
	@Valid
	@ManyToOne(optional=false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@NotNull
	@Valid
	@OneToMany
	public Collection<CreditCard> getSubscriptions() {
		return this.subscriptions;
	}
	
	public void setSubscriptions(final Collection<CreditCard> subscriptions) {
		this.subscriptions = subscriptions;
	}
	
	@NotNull
	@Valid
	@ManyToMany
	public Collection<Newspaper> getNewspapers() {
		return this.newspapers;
	}
	
	public void setNewspapers(final Collection<Newspaper> newspapers) {
		this.newspapers = newspapers;
	}
	
}
