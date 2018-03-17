package usecases;

import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.User;

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
	
	/*
	 * 1. Probando registrar usuario con telefono y dirección a null
	 * 2. Probando registrar usuario con telefono pero con dirección a null
	 * 3. Probando registrar usuario con telefono a vacío y dirección a null
	 * 4. Probando registrar usuario con telefono a null y dirección
	 * 5. Probando registrar usuario con telefono a null y dirección a vacío
	 * 6. Probando registrar usuario con telefono y dirección
	 * 7. Probando registrar usuario con telefono y dirección a vacío
	 */
	@Test
	public void positiveRegisterUserTest() {
		Calendar calendar;
		Date date;
		
		calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		date = calendar.getTime();
		
		final Object testingData[][] = {
				{
					null, "antonio1", "antonio1", "Antonio", "Azaña", null, null, date, "ant@mail.com", null 
				}, {
					null, "alexito", "alexito", "Alejandro", "Perez", "987532146", null, date, "a@hotmail.com", null 
				}, {
					null, "carlos", "carlos", "Carlos", "Sánchez", "", null, date, "carlosuser@mail.com", null 
				}, {
					null, "paquito", "paquito", "Paco", "Millán", null, "Calle Real Nº6", date, "paquito@mail.com", null 
				}, {
					null, "manolo", "manolo", "Manolo", "Guillen", null, "", date, "manolete@mail.com", null 
				}, {
					null, "pepito", "pepito", "Pepe", "Escolar", "321456987", "Dirección incorrecta", date, "pepe@mail.com", null
				}, {
					null, "francisco", "francisco", "Francisco", "Cerrada", "", "", date, "fran@mail.com", null 
				}
		};
			
	for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (Date) testingData[i][7], (String) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	/*
	 * 1. Un usuario logueado no puede registrar a otro
	 * 2. Un usuario logueado no puede registrar a otro
	 * 3. Un usuario logueado no puede registrar a otro
	 * 4. La fecha debe ser pasada
	 * 5. La fecha no puede ser nula
	 * 6. El email tiene que tener el formato de un email
	 * 7. El nombre no puede ser nulo
	 * 8. El apellido no puede ser nulo
	 * 9. El nombre no puede ser vacío
	 * 10. El apellido no puede ser vacío
	 * 11. El email no puede ser nulo
	 * 12. El email no puede ser vacío
	 * 13. El username debe estar entre 5 y 32
	 * 14. La password debe estar entre 5 y 32
	 */
	@Test()
	public void negativeRegisterUserTest() {
		Calendar calendar;
		Date dateGood, dateBad;
	
		calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		dateGood = calendar.getTime();
		
		calendar.set(calendar.get(Calendar.YEAR) + 22, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		dateBad = calendar.getTime();
		
		final Object testingData[][] = {
				{
					"user2", "user13", "user13", "Antonio", "Azaña", null, null, dateGood, "ant@mail.com", IllegalArgumentException.class 
				}, {
					"admin", "user23", "user23", "Antonio", "Azaña", "652147893", null, dateGood, "ant@mail.com", IllegalArgumentException.class 
				}, {
					"manager1", "user23", "user23", "Antonio", "Perez", "", "Calle Manager Nº41", dateGood, "ant@mail.com", IllegalArgumentException.class 
				}, {
					null, "alexito", "alexito","Alejandro", "Azaña", null, null, dateBad, "ant@mail.com", ConstraintViolationException.class 
				}, {
					null, "manuel", "manuel", "Manuel", "Azaña", null, null, null, "ant@mail.com", ConstraintViolationException.class 
				}, {
					null, "marta", "marta", "Marta", "Sanchez", "664857123", "Calle Falsa 23", dateGood, "manuelito", ConstraintViolationException.class 
				}, {
					null, "azaña", "azaña", null, "Azaña", "664857123", "Calle Inventada", dateGood, "m@mail.com", ConstraintViolationException.class 
				}, {
					null, "marta", "marta", "Marta", null, "664857123", "Calle sin numero", dateGood, "martita@gmail.es", ConstraintViolationException.class 
				}, {
					null, "azaña2", "azaña2", "", "Azaña", "664857123", "Calle Inventada", dateGood, "m@mail.com", ConstraintViolationException.class 
				}, {
					null, "marta2", "marta2", "Marta", "", "664857123", "Calle sin numero", dateGood, "martita@gmail.es", ConstraintViolationException.class 
				},{
					null, "marta3", "marta3", "Marta", "Azaña", "664857123", "Calle Novena", dateGood, null, ConstraintViolationException.class 
				}, {
					null, "maria", "maria", "María", "Villarín", "664254123", "Inserte dirección", dateGood, "", ConstraintViolationException.class 
				}, {
					null, "gost", "gostino", "Gostin", "Perez", "", "Calle User Nº41", dateGood, "gostin@mail.com", ConstraintViolationException.class 
				}, {
					null, "administratoradministratoradministrator", "admin", "Gostin", "Perez", "", "Calle User Nº41", dateGood, "gostin@mail.com", ConstraintViolationException.class 
				}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (Date) testingData[i][7], (String) testingData[i][8], (Class<?>) testingData[i][9]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	/*
	 * An actor who is not authenticated must be able to register to the system as a user.
	 */
	protected void template(final String user, final String username, final String password, final String name, final String surname, final String phone, final String address, final Date birthdate, final String email, final Class<?> expected) {
		Class<?> caught;
		User userEntity;

		caught = null;
		try {
			super.authenticate(user);

			userEntity = this.userService.create();
			userEntity.getUserAccount().setUsername(username);
			userEntity.getUserAccount().setPassword(password);
			userEntity.setName(name);
			userEntity.setSurname(surname);
			userEntity.setPhone(phone);
			userEntity.setAddress(address);
			userEntity.setBirthdate(birthdate);
			userEntity.setEmail(email);
			
			this.userService.save(userEntity);
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}

