package services;

import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.LoginService;
import utilities.AbstractTest;
import domain.Actor;
import domain.Administrator;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml" })
@Transactional
public class ActorServiceTest extends AbstractTest {

	// Service under test----------------
	@Autowired
	private ActorService actorService;

	@Autowired
	private AdministratorService administratorService;

	@Autowired
	private UserService userService;

	// Test -------------------------------
	/* Se cogen todos los administradores y vemos si uno cogido al azar está dentro. */
	@Test
	public void testFindAll() {
		final Collection<Actor> allActors;
		Administrator saved;

		super.authenticate("admin");

		saved = this.administratorService.findByUserAccountId(LoginService
				.getPrincipal().getId());

		allActors = this.actorService.findAll();

		Assert.isTrue(allActors.contains(saved));

		super.authenticate(null);

	}

	@Test
	public void testfindOne() {
		Administrator saved;
		Actor actor;

		super.authenticate("admin");
		saved = this.administratorService.findByUserAccountId(LoginService
				.getPrincipal().getId());
		actor = this.actorService.findOne(saved.getId());

		Assert.isTrue(saved.getUserAccount().equals(actor.getUserAccount()));

		super.authenticate(null);

	}

	@Test
	public void testSave() {
		User created;
		Actor saved;

		super.authenticate("admin");

		created = this.userService.create();
		created.setName("Antonio");
		created.setSurname("Ruíz García");
		created.setEmail("antoniorgarci@gamil.com");
		created.setBirthdate(new Date(System.currentTimeMillis()-1));
		created.getUserAccount().setUsername("userAntonio");
		created.getUserAccount().setPassword("userAntonio");

		saved = this.actorService.save(created);

		Assert.isTrue(this.actorService.findAll().contains(saved));

		super.authenticate(null);

	}
}
