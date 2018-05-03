package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	private boolean isPublicWishList;
	
	private int points;
	
	private String avatar;
	
	private Collection<Bargain> wishList;

	public boolean getIsPublicWishList() {
		return isPublicWishList;
	}

	public void setIsPublicWishList(boolean isPublicWishList) {
		this.isPublicWishList = isPublicWishList;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
	@URL
	@NotBlank
	public String getAvatar() {
		return this.avatar;
	}
	
	public void setAvatar(final String avatar) {
		this.avatar = avatar;
	}

	@Valid
	@NotNull
	@ManyToMany
	public Collection<Bargain> getWishList() {
		return wishList;
	}

	public void setWishList(Collection<Bargain> wishList) {
		this.wishList = wishList;
	}
	
}
