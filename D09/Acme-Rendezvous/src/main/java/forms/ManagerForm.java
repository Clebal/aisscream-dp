
package forms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

public class ManagerForm {

	private String	username;
	private String	password;
	private String	checkPassword;
	private int		id;
	private boolean	check;

	private String	name;
	private String	surname;
	private String	phone;
	private String	email;
	private Date	birthdate;
	private String	address;
	private String	vat;

	public ManagerForm() {
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
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	@Past
	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(final Date birthdate) {
		this.birthdate = birthdate;
	}

	@Pattern(regexp = "^(?![\\W_]+$)(?=.{2,12}$)[-_. 0-9]*(?:[a-zA-Z][-_ 0-9]*){0,3}$")
	@Column(unique=true)
	@NotBlank
	public String getVat() {
		return vat;
	}

	public void setVat(String vat) {
		this.vat = vat;
	}
}
