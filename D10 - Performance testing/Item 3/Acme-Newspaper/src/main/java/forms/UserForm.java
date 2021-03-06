
package forms;

import java.util.Collection;

import javax.persistence.ManyToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import domain.User;

public class UserForm {

	private String	username;
	private String	password;
	private String	checkPassword;
	private int		id;
	private boolean	check;

	private String	name;
	private String	surname;
	private String	phoneNumber;
	private String	emailAddress;
	private String	postalAddress;
	
	private Collection<User> followers;

	public UserForm() {
		super();
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	@Size(min = 5, max = 32)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	@Size(min = 5, max = 32)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	@Size(min = 5, max = 32)
	public String getCheckPassword() {
		return this.checkPassword;
	}

	public void setCheckPassword(final String checkPassword) {
		this.checkPassword = checkPassword;
	}

	public boolean isCheck() {
		return this.check;
	}
	public void setCheck(final boolean check) {
		this.check = check;
	}

	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	@Email
	@NotBlank
	public String getEmailAddress() {
		return this.emailAddress;
	}

	public void setEmailAddress(final String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPostalAddress() {
		return this.postalAddress;
	}

	public void setPostalAddress(final String postalAddress) {
		this.postalAddress = postalAddress;
	}

	@NotNull
	@Valid
	@ManyToMany
	public Collection<User> getFollowers() {
		return followers;
	}

	public void setFollowers(Collection<User> followers) {
		this.followers = followers;
	}

}
