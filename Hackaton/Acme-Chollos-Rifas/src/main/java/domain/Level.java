package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Level extends DomainEntity {

	private String name;
	
	private String image;
	
	private int minPoints;
	
	private int maxPoints;

	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@URL
	@NotBlank
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getMinPoints() {
		return minPoints;
	}

	public void setMinPoints(final int minPoints) {
		this.minPoints = minPoints;
	}

	public int getMaxPoints() {
		return maxPoints;
	}

	public void setMaxPoints(final int maxPoints) {
		this.maxPoints = maxPoints;
	}
	
}
