
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import utilities.URLCollection;

@Entity
@Access(AccessType.PROPERTY)
public class Bargain extends DomainEntity {

	private String				productName;

	private String				productUrl;

	private Collection<String>	productImages;

	private String				description;

	private int					price;

	private int					minimumPrice;

	private int					originalPrice;

	private int					estimatedSells;

	private String				discountCode;

	private boolean				isPublished;

	private Company				company;


	@NotBlank
	public String getProductName() {
		return this.productName;
	}

	public void setProductName(final String productName) {
		this.productName = productName;
	}

	@NotBlank
	@URL
	public String getProductUrl() {
		return this.productUrl;
	}

	public void setProductUrl(final String productUrl) {
		this.productUrl = productUrl;
	}

	@URLCollection
	@ElementCollection
	@NotNull
	public Collection<String> getProductImages() {
		return this.productImages;
	}

	public void setProductImages(final Collection<String> productImages) {
		this.productImages = productImages;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Min(0)
	public int getPrice() {
		return this.price;
	}

	public void setPrice(final int price) {
		this.price = price;
	}

	@Min(0)
	public int getMinimumPrice() {
		return this.minimumPrice;
	}

	public void setMinimumPrice(final int minimumPrice) {
		this.minimumPrice = minimumPrice;
	}

	@Min(0)
	public int getOriginalPrice() {
		return this.originalPrice;
	}

	public void setOriginalPrice(final int originalPrice) {
		this.originalPrice = originalPrice;
	}

	@Min(0)
	public int getEstimatedSells() {
		return this.estimatedSells;
	}

	public void setEstimatedSells(final int estimatedSells) {
		this.estimatedSells = estimatedSells;
	}

	public String getDiscountCode() {
		return this.discountCode;
	}

	public void setDiscountCode(final String discountCode) {
		this.discountCode = discountCode;
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
	public Company getCompany() {
		return this.company;
	}

	public void setCompany(final Company company) {
		this.company = company;
	}

}
