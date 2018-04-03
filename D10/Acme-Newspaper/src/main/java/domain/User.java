package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor{

	private Collection<User> followers;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Collection<User> getFollowers() {
		return followers;
	}

	public void setFollowers(Collection<User> followers) {
		this.followers = followers;
	}
	
}
