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

import domain.Manager;

import security.UserAccount;
import services.ManagerService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EditManagerTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private ManagerService		managerService;

	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 
	 * 1. Probando editar manager con telefono y dirección a null
	 * 2. Probando editar manager con telefono pero con dirección a null
	 * 3. Probando editar manager con telefono a vacío y dirección a null
	 * 4. Probando editar manager con telefono a null y dirección
	 * 5. Probando editar manager con telefono a null y dirección a vacío
	 * 6. Probando editar manager con telefono y dirección
	 * 7. Probando editar manager con telefono y dirección a vacío
	 * 
	 * Requisitos:
	 * 
	 * 	Se desea probar la correcta edición de un manager.
	 */
	@Test
	public void positiveEditManagerTest() {
		Calendar calendar;
		Date date;
		
		calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR) - 15, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		date = calendar.getTime();
		
		final Object testingData[][] = {
			{
				"manager1", "manager1", "manager1", "manager1", "Antonio", "Azaña", null, null, date, "ant@mail.com", "20063918-Y", null
			}, {
				"manager2", "manager2", "manager2", "manager2", "Alejandro", "Perez", "987532146", null, date, "a@hotmail.com", "30063918-Y", null
			}, {
				"manager1", "manager1", "manager1", "manager1", "Carlos", "Sánchez", "", null, date, "carlosmanager@mail.com", "45063918-Y", null
			}, {
				"manager2", "manager2", "manager2", "manager2", "Paco", "Millán", null, "Calle Real Nº6", date, "paquito@mail.com", "20063918-A", null
			}, {
				"manager1", "manager1", "manager1", "manager1", "Manolo", "Guillen", null, "", date, "manolete@mail.com", "20063918-Y", null
			}, {
				"manager2", "manager2", "manager2", "manager2", "Pepe", "Escolar", "321456987", "Dirección incorrecta", date, "pepe@mail.com", "20547918-G", null
			}, {
				"manager1", "manager1", "manager1", "manager1", "Francisco", "Cerrada", "", "", date, "fran@mail.com", "29863918-H", null
			}
		};
			
	for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7], (Date) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (Class<?>) testingData[i][11]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	/*
	 * Pruebas:
	 * 
	 * 1. Solo un manager registrado puede registrarse a si mismo
	 * 2. Solo un manager registrado puede registrarse a si mismo
	 * 3. Solo un manager registrado puede registrarse a si mismo
	 * 4. La fecha debe ser pasada
	 * 5. La fecha no puede ser nula
	 * 6. El email tiene que tener el formato de un email
	 * 7. El nombre no puede ser nulo
	 * 8. El apellido no puede ser nulo
	 * 9. El nombre no puede ser vacío
	 * 10. El apellido no puede ser vacío
	 * 11. El email no puede ser nulo
	 * 12. El email no puede ser vacío
	 * 13. El username no puede cambiar
	 * 14. La password no puede cambiar
	 * 15. El vat no puede ser vacío
	 * 16. El vat no puede ser nulo
	 * 
	 * Requisitos:
	 * 
	 * 	Se desea probar la correcta edición de un manager.
	 */
	@Test()
	public void negativeEditManagerTest() {
			Calendar calendar;
			Date dateGood, dateBad;
		
			calendar = Calendar.getInstance();
			calendar.set(calendar.get(Calendar.YEAR) - 12, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dateGood = calendar.getTime();
			
			calendar.set(calendar.get(Calendar.YEAR) + 25, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dateBad = calendar.getTime();
			
		final Object testingData[][] = {
			{
				"manager1", "manager2", "manager2", "manager2", "Antonio", "Azaña", null, null, dateGood, "ant@mail.com", "20063918-Y", IllegalArgumentException.class
			}, {
				"admin", "manager2", "manager2", "manager2", "Antonio", "Azaña", "652147893", null, dateGood, "ant@mail.com", "20063918-Y", IllegalArgumentException.class 
			}, {
				"user1", "manager2", "manager2", "manager2", "Antonio", "Perez", "", "Calle Manager Nº41", dateGood, "ant@mail.com", "20063918-Y", IllegalArgumentException.class
			}, {
				"manager1", "manager1", "manager1", "manager1", "Alejandro", "Azaña", null, null, dateBad, "ant@mail.com", "20063918-Y", ConstraintViolationException.class
			}, {
				"manager1", "manager1", "manager1", "manager1", "Manuel", "Azaña", null, null, null, "ant@mail.com", "20063918-Y", ConstraintViolationException.class
			}, {
				"manager1", "manager1", "manager1", "manager1", "Marta", "Sanchez", "664857123", "Calle Falsa 23", dateGood, "manuelito", "20063918-Y", ConstraintViolationException.class
			}, {
				"manager2", "manager2", "manager2", "manager2", null, "Azaña", "664857123", "Calle Inventada", dateGood, "m@mail.com", "20063918-Y", ConstraintViolationException.class 
			}, {
				"manager1", "manager1", "manager1", "manager1", "Marta", null, "664857123", "Calle sin numero", dateGood, "martita@gmail.es", "20063918-Y", ConstraintViolationException.class 
			}, {
				"manager2", "manager2", "manager2", "manager2", "", "Azaña", "664857123", "Calle Inventada", dateGood, "m@mail.com", "20063918-Y", ConstraintViolationException.class 
			}, {
				"manager1", "manager1", "manager1", "manager1", "Marta", "", "664857123", "Calle sin numero", dateGood, "martita@gmail.es", "20063918-Y", ConstraintViolationException.class
			},{
				"manager2", "manager2", "manager2", "manager2", "Marta", "Azaña", "664857123", "Calle Novena", dateGood, null, "20063918-Y", ConstraintViolationException.class 
			}, {
				"manager1", "manager1", "manager1", "manager1", "María", "Villarín", "664254123", "Inserte dirección", dateGood, "", "20063918-Y", ConstraintViolationException.class 
			}, {
				"manager1", "manager1", "user", "manager1", "Gostin", "Perez", "", "Calle manager Nº41", dateGood, "gostin@mail.com", "20063918-Y", IllegalArgumentException.class
			}, {
				"manager2", "manager2", "manager2", "admin", "Olga", "Martinez", "", "Calle manager Nº41", dateGood, "gostin@mail.com", "20063918-Y", IllegalArgumentException.class
			}, {
				"manager2", "manager2", "manager2", "manager2", "Zema", "Perez", "", "Calle manager Nº41", dateGood, "gostin@mail.com", "", ConstraintViolationException.class 
			}, {
				"manager1", "manager1", "manager1", "manager1", "Javier", "Perez", "", "Calle manager Nº41", dateGood, "gostin@mail.com", null, ConstraintViolationException.class 
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7], (Date) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (Class<?>) testingData[i][11]);
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
	 * 1. Nos autentificamos como manager
	 * 2. Tomamos el id y la entidad de manager
	 * 3. Creamos una copia del manager y de userAccount para que no se guarde solo con un set
	 * 4. Le asignamos el username, el password, el name, el surname, el phone, la address, el birthdate, el email y el vat correspondientes
	 * 5. Guardamos el manager copiado con los parámetros
	 * 6. Nos desautentificamos
	 */
	protected void template(final String managerAuthenticate, final String managerEdit, final String username, final String password, final String name, final String surname, final String phone, final String address, final Date birthdate, final String email, final String vat, final Class<?> expected) {
		Class<?> caught;
		int userId;
		Manager managerEntity, managerCopy;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();
		caught = null;
		try {
			super.authenticate(managerAuthenticate);

			userId = super.getEntityId(managerEdit);
			managerEntity = this.managerService.findOne(userId);
			managerCopy = this.copyManager(managerEntity);
			managerCopy.getUserAccount().setUsername(username);
			managerCopy.getUserAccount().setPassword(encoder.encodePassword(password, null));
			managerCopy.setName(name);
			managerCopy.setSurname(surname);
			managerCopy.setPhone(phone);
			managerCopy.setAddress(address);
			managerCopy.setBirthdate(birthdate);
			managerCopy.setEmail(email);
			managerCopy.setVat(vat);
			this.managerService.save(managerCopy);
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	private Manager copyManager(final Manager manager) {
		Manager result;

		result = new Manager();
		result.setId(manager.getId());
		result.setVersion(manager.getVersion());
		result.setUserAccount(copyUserAccount(manager.getUserAccount()));
		result.setName(manager.getName());
		result.setSurname(manager.getSurname());
		result.setAddress(manager.getAddress());
		result.setBirthdate(manager.getBirthdate());
		result.setEmail(manager.getEmail());
		result.setPhone(manager.getPhone());
		result.setVat(manager.getVat());

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