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
import org.springframework.util.Assert;

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
	 * Pruebas:
	 * 
	 * 1. Probando registrar usuario con telefono y direcci�n a null
	 * 2. Probando registrar usuario con telefono pero con direcci�n a null
	 * 3. Probando registrar usuario con telefono a vac�o y direcci�n a null
	 * 4. Probando registrar usuario con telefono a null y direcci�n
	 * 5. Probando registrar usuario con telefono a null y direcci�n a vac�o
	 * 6. Probando registrar usuario con telefono y direcci�n
	 * 7. Probando registrar usuario con telefono y direcci�n a vac�o
	 * 
	 * Requisitos:
	 * 
	 * 	An actor who is not authenticated must be able to register to the system as a user.
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
					null, "antonio1", "antonio1", "Antonio", "Aza�a", null, null, date, "ant@mail.com", null 
				}, {
					null, "alexito", "alexito", "Alejandro", "Perez", "987532146", null, date, "a@hotmail.com", null 
				}, {
					null, "carlos", "carlos", "Carlos", "S�nchez", "", null, date, "carlosuser@mail.com", null 
				}, {
					null, "paquito", "paquito", "Paco", "Mill�n", null, "Calle Real N�6", date, "paquito@mail.com", null 
				}, {
					null, "manolo", "manolo", "Manolo", "Guillen", null, "", date, "manolete@mail.com", null 
				}, {
					null, "pepito", "pepito", "Pepe", "Escolar", "321456987", "Direcci�n incorrecta", date, "pepe@mail.com", null
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
	 * Pruebas:
	 * 
	 * 1. Un usuario logueado no puede registrar a otro
	 * 2. Un usuario logueado no puede registrar a otro
	 * 3. Un usuario logueado no puede registrar a otro
	 * 4. La fecha debe ser pasada
	 * 5. La fecha no puede ser nula
	 * 6. El email tiene que tener el formato de un email
	 * 7. El nombre no puede ser nulo
	 * 8. El apellido no puede ser nulo
	 * 9. El nombre no puede ser vac�o
	 * 10. El apellido no puede ser vac�o
	 * 11. El email no puede ser nulo
	 * 12. El email no puede ser vac�o
	 * 13. El username debe estar entre 5 y 32
	 * 14. La password debe estar entre 5 y 32
	 * 
	 * Requisitos:
	 * 
	 * 	An actor who is not authenticated must be able to register to the system as a user.
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
					"user2", "user13", "user13", "Antonio", "Aza�a", null, null, dateGood, "ant@mail.com", IllegalArgumentException.class 
				}, {
					"admin", "user23", "user23", "Antonio", "Aza�a", "652147893", null, dateGood, "ant@mail.com", IllegalArgumentException.class 
				}, {
					"manager1", "user23", "user23", "Antonio", "Perez", "", "Calle Manager N�41", dateGood, "ant@mail.com", IllegalArgumentException.class 
				}, {
					null, "alexito", "alexito","Alejandro", "Aza�a", null, null, dateBad, "ant@mail.com", ConstraintViolationException.class 
				}, {
					null, "manuel", "manuel", "Manuel", "Aza�a", null, null, null, "ant@mail.com", ConstraintViolationException.class 
				}, {
					null, "marta", "marta", "Marta", "Sanchez", "664857123", "Calle Falsa 23", dateGood, "manuelito", ConstraintViolationException.class 
				}, {
					null, "aza�a", "aza�a", null, "Aza�a", "664857123", "Calle Inventada", dateGood, "m@mail.com", ConstraintViolationException.class 
				}, {
					null, "marta", "marta", "Marta", null, "664857123", "Calle sin numero", dateGood, "martita@gmail.es", ConstraintViolationException.class 
				}, {
					null, "aza�a2", "aza�a2", "", "Aza�a", "664857123", "Calle Inventada", dateGood, "m@mail.com", ConstraintViolationException.class 
				}, {
					null, "marta2", "marta2", "Marta", "", "664857123", "Calle sin numero", dateGood, "martita@gmail.es", ConstraintViolationException.class 
				},{
					null, "marta3", "marta3", "Marta", "Aza�a", "664857123", "Calle Novena", dateGood, null, ConstraintViolationException.class 
				}, {
					null, "maria", "maria", "Mar�a", "Villar�n", "664254123", "Inserte direcci�n", dateGood, "", ConstraintViolationException.class 
				}, {
					null, "gost", "gostino", "Gostin", "Perez", "", "Calle User N�41", dateGood, "gostin@mail.com", ConstraintViolationException.class 
				}, {
					null, "administratoradministratoradministrator", "admin", "Gostin", "Perez", "", "Calle User N�41", dateGood, "gostin@mail.com", ConstraintViolationException.class 
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
	 * 	Pasos:
	 * 
	 * 1. Nos autentificamos como el usuario user
	 * 2. Creamos un nuevo user
	 * 3. Le asignamos el username, el password, el name, el surname, el phone, la address, el birthdate y el email correspondientes
	 * 4. Guardamos el nuevo user
	 * 5. Nos desautentificamos
	 * 6. Comprobamos se ha creado y existe
	 */
	protected void template(final String user, final String username, final String password, final String name, final String surname, final String phone, final String address, final Date birthdate, final String email, final Class<?> expected) {
		Class<?> caught;
		User userEntity, userSave;

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
			
			userSave = this.userService.save(userEntity);
			super.unauthenticate();
			super.flushTransaction();
			
			Assert.isTrue(this.userService.findAll().contains(userSave));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}

