package usecases;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.User;

import security.LoginService;
import services.UserService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RegisterUserTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private UserService		userService;

	// Tests ------------------------------------------------------------------

		@Test
		public void testSave() {
			User created;
			User saved;

			created = this.userService.create();
			created.setName("Antonio");
			created.setSurname("Ruíz García");
			created.setEmail("antoniorgarci@gamil.com");
			created.getUserAccount().setUsername("antonio");
			created.getUserAccount().setPassword("antonio");
			
			super.unauthenticate();
			saved = this.userService.save(created);

			Assert.isTrue(this.userService.findAll().contains(saved));

			super.authenticate(null);

		}
		
	//@Test
	public void positiveTest() {
		/*Calendar calendar;
		SimpleDateFormat format;
		Date date = null;
		
		calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		System.out.println(calendar.getTime().);
		
		format = new SimpleDateFormat("dd/MM/yyyy");

		SimpleDateFormat parseador = new SimpleDateFormat("dd-MM-yy");

		try {
			date = parseador.parse("31-03-2016");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		System.out.println(date);*/
		
		
		// el que parsea
		SimpleDateFormat parseador = new SimpleDateFormat("dd-MM-yyyy");
		// el que formatea
		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");

		Date date = null;
		try {
			date = parseador.parse("31-03-2016");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(formateador.format(date));
		
		
		final Object testingData[][] = {
			{
				"non-existent", "antonio1", "antonio1", "Antonio", "Azaña", null, null, date, "ant@mail.com", null
			}
		};
			
	for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (Date) testingData[i][7], (String) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	
	//@Test()
	public void negativeTest() {
		final Object testingData[][] = {
			{
				"admin", "rendezvous1", "service1", "creditCard1", "", NullPointerException.class // Solo puede crearlo un user
			}, {
				"manager1", "rendezvous2", "service5", "creditCard4", "Otro comentario más", NullPointerException.class // Solo puede crearlo un user
			}, {
				"user1", "rendezvous5", "service3", "creditCard2", "", IllegalArgumentException.class // La creditCard debe pertenecer al user logueado
			}, {
				"user3", "rendezvous4", "service6", "creditCard4", "", IllegalArgumentException.class // El rendezvous no puede estar borrado
			}, {
				"user3", "rendezvous9", "service7", "creditCard4", "Comentario poco útil", IllegalArgumentException.class // No puede existir un request con mismo rendezvous y service
			}, {
				"user1", "rendezvous6", "service4", "creditCard1", "", IllegalArgumentException.class // No puede existir un request con mismo rendezvous y service
			}, {
				"user1", "rendezvous3", "service3", "creditCard3", null, IllegalArgumentException.class // El user logueado debe ser el creador del rendezvous
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (Date) testingData[i][7], (String) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void template(final String user, final String username, final String password, final String name, final String surname, final String phone, final String address, final Date birthdate, final String email, final Class<?> expected) {
		Class<?> caught;
		User userEntity, saved;

		caught = null;
		try {
			super.authenticate(user);

			System.out.println(LoginService.isAuthenticated());
			userEntity = this.userService.create();
			userEntity.getUserAccount().setUsername(username);
			userEntity.getUserAccount().setPassword(password);
			userEntity.setName(name);
			userEntity.setSurname(surname);
			userEntity.setPhone(phone);
			userEntity.setAddress(address);
			userEntity.setBirthdate(birthdate);
			userEntity.setEmail(email);
			saved = this.userService.save(userEntity);
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			System.out.println(oops.getLocalizedMessage());
			System.out.println(oops.getMessage());
			System.out.println(oops.getSuppressed());
			caught = oops.getClass();
		}
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
		super.checkExceptions(expected, caught);
	}

}

