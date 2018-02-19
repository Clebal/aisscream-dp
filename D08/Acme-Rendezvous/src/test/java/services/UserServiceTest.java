
package services;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.User;

import security.LoginService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UserServiceTest extends AbstractTest {

	// Service under test ---------------------------------

	@Autowired
	private UserService		userService;

	// Managed services


	// Tests ----------------------------------------------

	/*
	 * Creamos un nuevo User y comprobamos que sus atributos tengan el valor
	 * esperado
	 */
	@Test
	public void testCreate() {
		User user;

		user = this.userService.create();
		Assert.isNull(user.getAddress());
		Assert.isNull(user.getEmail());
		Assert.isNull(user.getName());
		Assert.isNull(user.getSurname());
		Assert.isNull(user.getPhone());
	}

	/*
	 * Creamos un User logeados como un usuario y lo guardamos en la
	 * base de datos
	 */
	@Test
	public void testSave1() {

		User user;
		int oldCountUser;

		super.authenticate("user1");

		oldCountUser = this.userService.findAll().size();

		user = this.userService.create();
		user.setName("Antonio");
		user.setSurname("Sanchez");
		user.setAddress("Calle Monte");
		user.setEmail("an@hotmail.com");
		user.setPhone("632145879");
		user.setBirthdate(new Date(System.currentTimeMillis()-1));

		user.getUserAccount().setUsername("antonio");
		user.getUserAccount().setPassword("antonio123");

		this.userService.save(user);

		Assert.isTrue(this.userService.findAll().size() == oldCountUser + 1);

		super.authenticate(null);
	}

	/*
	 * Creamos un User sin loguearse y lo guardamos en la base de datos
	 */
	@Test
	public void testSave2() {

		User user;
		int oldCountUser;

		oldCountUser = this.userService.findAll().size();

		user = this.userService.create();
		user.setName("Antonio");
		user.setSurname("Sanchez");
		user.setAddress("Calle Monte");
		user.setEmail("an@hotmail.com");
		user.setPhone("632145879");

		user.getUserAccount().setUsername("antonio");
		user.getUserAccount().setPassword("antonio123");
		user.setBirthdate(new Date(System.currentTimeMillis()-1));

		super.authenticate(null);

		this.userService.save(user);

		Assert.isTrue(this.userService.findAll().size() == oldCountUser + 1);

	}

	/* Se cogen todos los users y el tamaño es el esperado. */
	@Test
	public void testFindAll() {
		Integer allUsers;

		allUsers = this.userService.findAll().size();

		Assert.isTrue(allUsers == 4);

		super.authenticate(null);

	}

	/* Un user accede a él mismo a traves de findOne */
	@Test
	public void testFindOne() {
		User saved;
		User user;

		super.authenticate("user1");
		saved = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		user = this.userService.findOne(saved.getId());

		Assert.isTrue(saved.getUserAccount().equals(user.getUserAccount()));

		super.authenticate(null);

	}

	
	/*
	 * Probamos el metodo findByUserAccountId que debe devolver el User que
	 * se corresponga con la cuenta de usuario
	 */
	@Test
	public void testFindByUserAccountId() {
		User user;

		super.authenticate("user1");

		user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());

		Assert.isTrue(this.userService.findAll().contains(user));

		super.authenticate(null);

	}

}
