package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Bargain extends DomainEntity {

	public String productName;

	public String productUrl;

	public String[] productImages;

	public String description;

	public Integer price;

	public Integer benefitPercent;

	public Integer estimatedSells;

	public String[] discountCode;

	public Boolean isPublished;

	public Company company;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductUrl() {
		return productUrl;
	}

	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}

	public String[] getProductImages() {
		return productImages;
	}

	public void setProductImages(String[] productImages) {
		this.productImages = productImages;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getBenefitPercent() {
		return benefitPercent;
	}

	public void setBenefitPercent(Integer benefitPercent) {
		this.benefitPercent = benefitPercent;
	}

	public Integer getEstimatedSells() {
		return estimatedSells;
	}

	public void setEstimatedSells(Integer estimatedSells) {
		this.estimatedSells = estimatedSells;
	}

	public String[] getDiscountCode() {
		return discountCode;
	}

	public void setDiscountCode(String[] discountCode) {
		this.discountCode = discountCode;
	}

	public Boolean getIsPublished() {
		return isPublished;
	}

	public void setIsPublished(Boolean isPublished) {
		this.isPublished = isPublished;
	}

	@NotNull
	@Valid
	@OneToOne
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	
}
