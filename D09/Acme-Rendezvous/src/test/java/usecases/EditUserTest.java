package usecases;


import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.User;

import security.UserAccount;
import services.UserService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EditUserTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private UserService		userService;

	// Tests ------------------------------------------------------------------

	@Test
	public void positiveTest() {
		Calendar calendar;
		Date date;
		
		calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		date = calendar.getTime();
		
		final Object testingData[][] = {
			{
				"user1", "user1", "user1", "user1", "Antonio", "Azaña", null, null, date, "ant@mail.com", null
			}, {
				"user2", "user2", "user2", "user2", "Alejandro", "Perez", "987532146", null, date, "a@hotmail.com", null
			}, {
				"user3", "user3", "user3", "user3", "Carlos", "Sánchez", "", null, date, "carlosuser@mail.com", null
			}, {
				"user4", "user4", "user4", "user4", "Paco", "Millán", null, "Calle Real Nº6", date, "paquito@mail.com", null
			}, {
				"user5", "user5", "user5", "user5", "Manolo", "Guillen", null, "", date, "manolete@mail.com", null
			}, {
				"user6", "user6", "user6", "user6", "Pepe", "Escolar", "321456987", "Dirección incorrecta", date, "pepe@mail.com", null
			}, {
				"user3", "user3", "user3", "user3", "Francisco", "Cerrada", "", "", date, "fran@mail.com", null
			}
		};
			
	for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7], (Date) testingData[i][8], (String) testingData[i][9], (Class<?>) testingData[i][10]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	
	@Test()
	public void negativeTest() {
			Calendar calendar;
			Date dateGood, dateBad;
		
			calendar = Calendar.getInstance();
			calendar.set(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dateGood = calendar.getTime();
			
			calendar.set(calendar.get(Calendar.YEAR) + 22, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dateBad = calendar.getTime();
			
		final Object testingData[][] = {
			{
				"user1", "user2", "user2", "user2", "Antonio", "Azaña", null, null, dateGood, "ant@mail.com", IllegalArgumentException.class // Solo un usuario registrado puede editarse a si mismo
			}, {
				"admin", "user2", "user2", "user2", "Antonio", "Azaña", "652147893", null, dateGood, "ant@mail.com", IllegalArgumentException.class // Solo un usuario registrado puede editarse a si mismo
			}, {
				"manager1", "user2", "user2", "user2", "Antonio", "Perez", "", "Calle Manager Nº41", dateGood, "ant@mail.com", IllegalArgumentException.class // Solo un usuario registrado puede editarse a si mismo
			}, {
				"user1", "user1", "user1", "user1", "Alejandro", "Azaña", null, null, dateBad, "ant@mail.com", ConstraintViolationException.class // La fecha debe ser pasada
			}, {
				"user1", "user1", "user1", "user1", "Manuel", "Azaña", null, null, null, "ant@mail.com", ConstraintViolationException.class // La fecha no puede ser nula
			}, {
				"user1", "user1", "user1", "user1", "Marta", "Sanchez", "664857123", "Calle Falsa 23", dateGood, "manuelito", ConstraintViolationException.class // El email tiene que tener el formato de un email
			}, {
				"user2", "user2", "user2", "user2", null, "Azaña", "664857123", "Calle Inventada", dateGood, "m@mail.com", ConstraintViolationException.class // El nombre no puede ser nulo
			}, {
				"user1", "user1", "user1", "user1", "Marta", null, "664857123", "Calle sin numero", dateGood, "martita@gmail.es", ConstraintViolationException.class // El apellido no puede ser nulo
			}, {
				"user3", "user3", "user3", "user3", "", "Azaña", "664857123", "Calle Inventada", dateGood, "m@mail.com", ConstraintViolationException.class // El nombre no puede ser vacío
			}, {
				"user1", "user1", "user1", "user1", "Marta", "", "664857123", "Calle sin numero", dateGood, "martita@gmail.es", ConstraintViolationException.class // El apellido no puede ser vacío
			},{
				"user4", "user4", "user4", "user4", "Marta", "Azaña", "664857123", "Calle Novena", dateGood, null, ConstraintViolationException.class // El email no puede ser nulo
			}, {
				"user1", "user1", "user1", "user1", "María", "Villarín", "664254123", "Inserte dirección", dateGood, "", ConstraintViolationException.class // El email no puede ser vacío
			}, {
				"user1", "user1", "manager", "user1", "Gostin", "Perez", "", "Calle User Nº41", dateGood, "gostin@mail.com", IllegalArgumentException.class // El username no puede cambiar
			}, {
				"user3", "user3", "user3", "admin", "Gostin", "Perez", "", "Calle User Nº41", dateGood, "gostin@mail.com", IllegalArgumentException.class // La password no puede cambiar
			},
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7], (Date) testingData[i][8], (String) testingData[i][9], (Class<?>) testingData[i][10]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void template(final String userAuthenticate, final String userEdit, final String username, final String password, final String name, final String surname, final String phone, final String address, final Date birthdate, final String email, final Class<?> expected) {
		Class<?> caught;
		int userId;
		User userEntity, userCopy;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();
		caught = null;
		try {
			super.authenticate(userAuthenticate);

			userId = super.getEntityId(userEdit);
			userEntity = this.userService.findOne(userId);
			userCopy = this.copyUser(userEntity);
			userCopy.getUserAccount().setUsername(username);
			userCopy.getUserAccount().setPassword(encoder.encodePassword(password, null));
			userCopy.setName(name);
			userCopy.setSurname(surname);
			userCopy.setPhone(phone);
			userCopy.setAddress(address);
			userCopy.setBirthdate(birthdate);
			userCopy.setEmail(email);
			this.userService.save(userCopy);
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
		super.checkExceptions(expected, caught);
	}
	
	private User copyUser(final User user) {
		User result;

		result = new User();
		result.setId(user.getId());
		result.setVersion(user.getVersion());
		result.setUserAccount(copyUserAccount(user.getUserAccount()));
		result.setName(user.getName());
		result.setSurname(user.getSurname());
		result.setAddress(user.getAddress());
		result.setBirthdate(user.getBirthdate());
		result.setEmail(user.getEmail());
		result.setPhone(user.getPhone());

		return result;
	}
	
	private UserAccount copyUserAccount(final UserAccount userAccount) {
		UserAccount result;

		result = new UserAccount();
		result.setId(userAccount.getId());
		result.setVersion(userAccount.getVersion());
		result.setUsername(userAccount.getUsername());
		result.setPassword(userAccount.getPassword());
		result.setAuthorities(userAccount.getAuthorities());

		return result;
	}

}