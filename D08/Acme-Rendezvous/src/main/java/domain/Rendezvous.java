package domain;

import java.util.Date;
import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Future;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Rendezvous extends DomainEntity {

	public String name;

	public String description;

	public Date moment;

	public String[] picture;

	public Boolean draft;

	public Boolean adultOnly;

	public Double[] latitude;

	public Double[] longitude;

	public Boolean isDeleted;

	public User creator;

	public Collection<Rendezvous> linkerRendezvouses;

	private TermCondition termCondition;

	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotBlank
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Future
	public Date getMoment() {
		return moment;
	}

	public void setMoment(Date moment) {
		this.moment = moment;
	}

	@URL
	public String[] getPicture() {
		return picture;
	}

	public void setPicture(String[] picture) {
		this.picture = picture;
	}

	public Boolean getDraft() {
		return draft;
	}

	public void setDraft(Boolean draft) {
		this.draft = draft;
	}

	public Boolean getAdultOnly() {
		return adultOnly;
	}

	public void setAdultOnly(Boolean adultOnly) {
		this.adultOnly = adultOnly;
	}

	public Double[] getLatitude() {
		return latitude;
	}

	public void setLatitude(Double[] latitude) {
		this.latitude = latitude;
	}

	public Double[] getLongitude() {
		return longitude;
	}

	public void setLongitude(Double[] longitude) {
		this.longitude = longitude;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@ManyToMany
	public Collection<Rendezvous> getLinkerRendezvouses() {
		return linkerRendezvouses;
	}

	public void setLinkerRendezvouses(Collection<Rendezvous> linkerRendezvouses) {
		this.linkerRendezvouses = linkerRendezvouses;
	}

	@ManyToOne(optional = false)
	public TermCondition getTermCondition() {
		return termCondition;
	}

	public void setTermCondition(TermCondition termCondition) {
		this.termCondition = termCondition;
	}

}
