package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	private boolean isPublicWishList;

	public boolean isPublicWishList() {
		return isPublicWishList;
	}

	public void setPublicWishList(boolean isPublicWishList) {
		this.isPublicWishList = isPublicWishList;
	}
	
}
