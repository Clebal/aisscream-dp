package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	private boolean isPublicWishList;
	
	private int points;
	
	private String avatar;

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
	
}
