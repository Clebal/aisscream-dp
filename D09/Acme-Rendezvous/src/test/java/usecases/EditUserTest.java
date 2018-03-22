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

	/*
	 * Pruebas:
	 * 
	 * 1. Probando editar usuario con telefono y direccion a null
	 * 2. Probando editar usuario con telefono pero con direccion a null
	 * 3. Probando editar usuario con telefono a vacio y direccion a null
	 * 4. Probando editar usuario con telefono a null y direccion
	 * 5. Probando editar usuario con telefono a null y direccion a vacio
	 * 6. Probando editar usuario con telefono y direccion
	 * 7. Probando editar usuario con telefono y direccion a vacio
	 * 
	 * Requsitos:
	 * 
	 * 	Se desea probar la correcta edicion de un usuario.
	 */
	@Test
	public void positiveEditUserTest() {
		Calendar calendar;
		Date date;
		
		calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		date = calendar.getTime();
		
		final Object testingData[][] = {
			{
				"user1", "user1", "user1", "user1", "Antonio", "Azana", null, null, date, "ant@mail.com", null 
			}, {
				"user2", "user2", "user2", "user2", "Alejandro", "Perez", "987532146", null, date, "a@hotmail.com", null
			}, {
				"user3", "user3", "user3", "user3", "Carlos", "Sánchez", "", null, date, "carlosuser@mail.com", null
			}, {
				"user4", "user4", "user4", "user4", "Paco", "Millán", null, "Calle Real Nº6", date, "paquito@mail.com", null 
			}, {
				"user5", "user5", "user5", "user5", "Manolo", "Guillen", null, "", date, "manolete@mail.com", null 
			}, {
				"user6", "user6", "user6", "user6", "Pepe", "Escolar", "321456987", "Direccion incorrecta", date, "pepe@mail.com", null 
			}, {
				"user3", "user3", "user3", "user3", "Francisco", "Cerrada", "", "", date, "fran@mail.com", null
			}
		};
			
	for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7], (Date) testingData[i][8], (String) testingData[i][9], (Class<?>) testingData[i][10]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	/*
	 * Pruebas:
	 * 
	 * 1. Solo un usuario registrado puede registrarse a si mismo
	 * 2. Solo un usuario registrado puede registrarse a si mismo
	 * 3. Solo un usuario registrado puede registrarse a si mismo
	 * 4. La fecha debe ser pasada
	 * 5. La fecha no puede ser nula
	 * 6. El email tiene que tener el formato de un email
	 * 7. El nombre no puede ser nulo
	 * 8. El apellido no puede ser nulo
	 * 9. El nombre no puede ser vacio
	 * 10. El apellido no puede ser vacio
	 * 11. El email no puede ser nulo
	 * 12. El email no puede ser vacio
	 * 13. El username no puede cambiar
	 * 14. La password no puede cambiar
	 * 
	 * Requisitos:
	 * 
	 * 	Se desea probar la correcta edicion de un usuario.
	 */
	@Test()
	public void negativeEditUserTest() {
			Calendar calendar;
			Date dateGood, dateBad;
		
			calendar = Calendar.getInstance();
			calendar.set(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dateGood = calendar.getTime();
			
			calendar.set(calendar.get(Calendar.YEAR) + 22, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dateBad = calendar.getTime();
			
		final Object testingData[][] = {
			{
				"user1", "user2", "user2", "user2", "Antonio", "Azana", null, null, dateGood, "ant@mail.com", IllegalArgumentException.class 
			}, {
				"admin", "user2", "user2", "user2", "Antonio", "Azana", "652147893", null, dateGood, "ant@mail.com", IllegalArgumentException.class
			}, {
				"manager1", "user2", "user2", "user2", "Antonio", "Perez", "", "Calle Manager Nº41", dateGood, "ant@mail.com", IllegalArgumentException.class
			}, {
				"user1", "user1", "user1", "user1", "Alejandro", "Azana", null, null, dateBad, "ant@mail.com", ConstraintViolationException.class
			}, {
				"user1", "user1", "user1", "user1", "Manuel", "Azana", null, null, null, "ant@mail.com", ConstraintViolationException.class
			}, {
				"user1", "user1", "user1", "user1", "Marta", "Sanchez", "664857123", "Calle Falsa 23", dateGood, "manuelito", ConstraintViolationException.class 
			}, {
				"user2", "user2", "user2", "user2", null, "Azana", "664857123", "Calle Inventada", dateGood, "m@mail.com", ConstraintViolationException.class
			}, {
				"user1", "user1", "user1", "user1", "Marta", null, "664857123", "Calle sin numero", dateGood, "martita@gmail.es", ConstraintViolationException.class
			}, {
				"user3", "user3", "user3", "user3", "", "Azana", "664857123", "Calle Inventada", dateGood, "m@mail.com", ConstraintViolationException.class
			}, {
				"user1", "user1", "user1", "user1", "Marta", "", "664857123", "Calle sin numero", dateGood, "martita@gmail.es", ConstraintViolationException.class 
			},{
				"user4", "user4", "user4", "user4", "Marta", "Azana", "664857123", "Calle Novena", dateGood, null, ConstraintViolationException.class 
			}, {
				"user1", "user1", "user1", "user1", "Maria", "Villarin", "664254123", "Inserte direccion", dateGood, "", ConstraintViolationException.class 
			}, {
				"user1", "user1", "manager", "user1", "Gostin", "Perez", "", "Calle User Nº41", dateGood, "gostin@mail.com", IllegalArgumentException.class
			}, {
				"user3", "user3", "user3", "admin", "Gostin", "Perez", "", "Calle User Nº41", dateGood, "gostin@mail.com", IllegalArgumentException.class
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7], (Date) testingData[i][8], (String) testingData[i][9], (Class<?>) testingData[i][10]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	/*
	 * 	Pasos:
	 * 
	 * 1. Nos autentificamos como user
	 * 2. Tomamos el id y la entidad de user
	 * 3. Creamos una copia del user y de userAccount para que no se guarde solo con un set
	 * 4. Le asignamos el username, el password, el name, el surname, el phone, la address, el birthdate y el email correspondientes
	 * 5. Guardamos el user copiado con los parámetros
	 * 6. Nos desautentificamos
	 */
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