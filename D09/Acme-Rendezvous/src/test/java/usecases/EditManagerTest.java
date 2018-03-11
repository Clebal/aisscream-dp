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

	@Test
	public void positiveTest() {
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
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7], (Date) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (Class<?>) testingData[i][11]);
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
			calendar.set(calendar.get(Calendar.YEAR) - 12, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dateGood = calendar.getTime();
			
			calendar.set(calendar.get(Calendar.YEAR) + 25, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dateBad = calendar.getTime();
			
		final Object testingData[][] = {
			{
				"manager1", "manager2", "manager2", "manager2", "Antonio", "Azaña", null, null, dateGood, "ant@mail.com", "20063918-Y", IllegalArgumentException.class // Solo un usuario registrado puede editarse a si mismo
			}, {
				"admin", "manager2", "manager2", "manager2", "Antonio", "Azaña", "652147893", null, dateGood, "ant@mail.com", "20063918-Y", IllegalArgumentException.class // Solo un usuario registrado puede editarse a si mismo
			}, {
				"user1", "manager2", "manager2", "manager2", "Antonio", "Perez", "", "Calle Manager Nº41", dateGood, "ant@mail.com", "20063918-Y", IllegalArgumentException.class // Solo un usuario registrado puede editarse a si mismo
			}, {
				"manager1", "manager1", "manager1", "manager1", "Alejandro", "Azaña", null, null, dateBad, "ant@mail.com", "20063918-Y", ConstraintViolationException.class // La fecha debe ser pasada
			}, {
				"manager1", "manager1", "manager1", "manager1", "Manuel", "Azaña", null, null, null, "ant@mail.com", "20063918-Y", ConstraintViolationException.class // La fecha no puede ser nula
			}, {
				"manager1", "manager1", "manager1", "manager1", "Marta", "Sanchez", "664857123", "Calle Falsa 23", dateGood, "manuelito", "20063918-Y", ConstraintViolationException.class // El email tiene que tener el formato de un email
			}, {
				"manager2", "manager2", "manager2", "manager2", null, "Azaña", "664857123", "Calle Inventada", dateGood, "m@mail.com", "20063918-Y", ConstraintViolationException.class // El nombre no puede ser nulo
			}, {
				"manager1", "manager1", "manager1", "manager1", "Marta", null, "664857123", "Calle sin numero", dateGood, "martita@gmail.es", "20063918-Y", ConstraintViolationException.class // El apellido no puede ser nulo
			}, {
				"manager2", "manager2", "manager2", "manager2", "", "Azaña", "664857123", "Calle Inventada", dateGood, "m@mail.com", "20063918-Y", ConstraintViolationException.class // El nombre no puede ser vacío
			}, {
				"manager1", "manager1", "manager1", "manager1", "Marta", "", "664857123", "Calle sin numero", dateGood, "martita@gmail.es", "20063918-Y", ConstraintViolationException.class // El apellido no puede ser vacío
			},{
				"manager2", "manager2", "manager2", "manager2", "Marta", "Azaña", "664857123", "Calle Novena", dateGood, null, "20063918-Y", ConstraintViolationException.class // El email no puede ser nulo
			}, {
				"manager1", "manager1", "manager1", "manager1", "María", "Villarín", "664254123", "Inserte dirección", dateGood, "", "20063918-Y", ConstraintViolationException.class // El email no puede ser vacío
			}, {
				"manager1", "manager1", "user", "manager1", "Gostin", "Perez", "", "Calle manager Nº41", dateGood, "gostin@mail.com", "20063918-Y", IllegalArgumentException.class // El managername no puede cambiar
			}, {
				"manager2", "manager2", "manager2", "admin", "Olga", "Martinez", "", "Calle manager Nº41", dateGood, "gostin@mail.com", "20063918-Y", IllegalArgumentException.class // La password no puede cambiar
			}, {
				"manager2", "manager2", "manager2", "manager2", "Zema", "Perez", "", "Calle manager Nº41", dateGood, "gostin@mail.com", "", ConstraintViolationException.class // El vat no puede ser vacío
			}, {
				"manager1", "manager1", "manager1", "manager1", "Javier", "Perez", "", "Calle manager Nº41", dateGood, "gostin@mail.com", null, ConstraintViolationException.class // El vat no puede ser nulo
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7], (Date) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (Class<?>) testingData[i][11]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

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
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
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