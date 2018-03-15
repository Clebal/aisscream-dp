package usecases;

import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.Manager;

import services.ManagerService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RegisterManagerTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private ManagerService		managerService;
	
	// Tests ------------------------------------------------------------------
	
	/*
	 * 1. Probando registrar manager con telefono y direccion a null
	 * 2. Probando registrar manager con telefono pero con direccion a null
	 * 3. Probando registrar manager con telefono a vacio y direccion a null
	 * 4. Probando registrar manager con telefono a null y direccion
	 * 5. Probando registrar manager con telefono a null y direccion a vacio
	 * 6. Probando registrar manager con telefono y direccion
	 * 7. Probando registrar manager con telefono y direccion a vacio
	 */
	@Test
	public void positiveRegisterManagerTest() {
		Calendar calendar;
		Date date;
		
		calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR) - 18, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		date = calendar.getTime();
		
		final Object testingData[][] = {
				{
					null, "antonio1", "antonio1", "Antonio", "Azan", null, null, date, "ant@mail.com", "20063918-Y", null 
				}, {
					null, "alexito", "alexito", "Alejandro", "Perez", "987532146", null, date, "a@hotmail.com", "20063918-Y", null 
				}, {
					null, "carlos", "carlos", "Carlos", "S√°nchez", "", null, date, "carlosuser@mail.com", "20483918-Y", null 
				}, {
					null, "paquito", "paquito", "Paco", "Mill√°n", null, "Calle Real N∫6", date, "paquito@mail.com", "20063918-Y", null 
				}, {
					null, "manolo", "manolo", "Manolo", "Guillen", null, "", date, "manolete@mail.com", "20893918-Y", null 
				}, {
					null, "pepito", "pepito", "Pepe", "Escolar", "321456987", "Direccion incorrecta", date, "pepe@mail.com", "20063918-Y", null
				}, {
					null, "francisco", "francisco", "Francisco", "Cerrada", "", "", date, "fran@mail.com", "21473918-Y", null 
				}
		};
			
	for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (Date) testingData[i][7], (String) testingData[i][8], (String) testingData[i][9], (Class<?>) testingData[i][10]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	/*
	 * 1. Un manager logueado no puede registrar a otro
	 * 2. Un manager logueado no puede registrar a otro
	 * 3. Un manager logueado no puede registrar a otro
	 * 4. La fecha debe ser pasada
	 * 5. La fecha no puede ser nula
	 * 6. El email tiene que tener el formato de un email
	 * 7. El nombre no puede ser nulo
	 * 8. El apellido no puede ser nulo
	 * 9. El nombre no puede ser vacio
	 * 10. El apellido no puede ser vacio
	 * 11. El email no puede ser nulo
	 * 12. El email no puede ser vacio
	 * 13. El username debe estar entre 5 y 32
	 * 14. La password debe estar entre 5 y 32
	 * 	15. El vat no puede ser vacio
	 * 16. El vat no puede ser nulo
	 */
	@Test()
	public void negativeRegisterManagerTest() {
		Calendar calendar;
		Date dateGood, dateBad;
	
		calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR) - 15, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		dateGood = calendar.getTime();
		
		calendar.set(calendar.get(Calendar.YEAR) + 25, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		dateBad = calendar.getTime();
		
		final Object testingData[][] = {
				{
					"user2", "manager13", "manager13", "Antonio", "Azan", null, null, dateGood, "ant@mail.com", "21473918-Y", IllegalArgumentException.class 
				}, {
					"admin", "manager23", "manager23", "Antonio", "Azan", "652147893", null, dateGood, "ant@mail.com", "21473918-Y", IllegalArgumentException.class 
				}, {
					"manager1", "manager23", "manager23", "Antonio", "Perez", "", "Calle Manager N∫41", dateGood, "ant@mail.com", "21473918-Y", IllegalArgumentException.class 
				}, {
					null, "alexito", "alexito","Alejandro", "Azan", null, null, dateBad, "ant@mail.com", "21473918-Y", ConstraintViolationException.class 
				}, {
					null, "manuel", "manuel", "Manuel", "Azan", null, null, null, "ant@mail.com", "21473918-Y", ConstraintViolationException.class 
				}, {
					null, "marta", "marta", "Marta", "Sanchez", "664857123", "Calle Falsa 23", dateGood, "manuelito", "21473918-Y", ConstraintViolationException.class 
				}, {
					null, "azan", "azan", null, "Azan", "664857123", "Calle Inventada", dateGood, "m@mail.com", "21473918-Y", ConstraintViolationException.class 
				}, {
					null, "marta", "marta", "Marta", null, "664857123", "Calle sin numero", dateGood, "martita@gmail.es", "21473918-Y", ConstraintViolationException.class 
				}, {
					null, "azan2", "azan2", "", "Azan", "664857123", "Calle Inventada", dateGood, "m@mail.com", "21473918-Y", ConstraintViolationException.class 
				}, {
					null, "marta2", "marta2", "Marta", "", "664857123", "Calle sin numero", dateGood, "martita@gmail.es", "21473918-Y", ConstraintViolationException.class 
				},{
					null, "marta3", "marta3", "Marta", "Azan", "664857123", "Calle Novena", dateGood, null, "21473918-Y", ConstraintViolationException.class 
				}, {
					null, "maria", "maria", "Maria", "Villarin", "664254123", "Inserte direccion", dateGood, "", "21473918-Y", ConstraintViolationException.class 
				}, {
					null, "gost", "gostino", "Gostin", "Perez", "", "Calle User N∫41", dateGood, "gostin@mail.com", "21473918-Y", ConstraintViolationException.class 
				}, {
					null, "administratoradministratoradministrator", "admin", "Gostin", "Perez", "", "Calle User N∫41", dateGood, "gostin@mail.com", "21473918-Y", ConstraintViolationException.class 
				}, {
					null, "manager2", "manager2", "Zema", "Perez", "", "Calle manager N∫41", dateGood, "gostin@mail.com", "", DataIntegrityViolationException.class 
				}, {
					null, "manager1", "manager1", "Javier", "Perez", "", "Calle manager N∫41", dateGood, "gostin@mail.com", null, DataIntegrityViolationException.class 
				}		
			};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (Date) testingData[i][7], (String) testingData[i][8], (String) testingData[i][9], (Class<?>) testingData[i][10]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	/*
	 * An actor who is not authenticated must be able to register to the system as a manager.
	 */
	protected void template(final String manager, final String username, final String password, final String name, final String surname, final String phone, final String address, final Date birthdate, final String email, final String vat, final Class<?> expected) {
		Class<?> caught;
		Manager managerEntity;

		caught = null;
		try {
			super.authenticate(manager);

			managerEntity = this.managerService.create();
			managerEntity.getUserAccount().setUsername(username);
			managerEntity.getUserAccount().setPassword(password);
			managerEntity.setName(name);
			managerEntity.setSurname(surname);
			managerEntity.setPhone(phone);
			managerEntity.setAddress(address);
			managerEntity.setBirthdate(birthdate);
			managerEntity.setEmail(email);
			managerEntity.setVat(vat);
			
			this.managerService.save(managerEntity);
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
		super.checkExceptions(expected, caught);
	}

}

