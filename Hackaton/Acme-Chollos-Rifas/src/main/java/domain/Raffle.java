package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import utilities.URLCollection;

@Entity
@Access(AccessType.PROPERTY)
public class Raffle extends DomainEntity {

	private String title;
	
	private String description;
	
	private String productName;
	
	private String productUrl;
	
	private Collection<String> productImages;
	
	private Date maxDate;
		
	private double price;
	
	private Company company;
	
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

	@NotBlank
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@URL
	public String getProductUrl() {
		return productUrl;
	}

	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}

	@ElementCollection
	@URLCollection
	public Collection<String> getProductImages() {
		return productImages;
	}

	public void setProductImages(Collection<String> productImages) {
		this.productImages = productImages;
	}

	@NotNull
	@Future
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	@Min(0)
	public double getPrice() {
		return this.price;
	}
	
	public void setPrice(final double price) {
		this.price = price;
	}

	@NotNull
	@Valid
	@ManyToOne(optional=false)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
}
