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

import domain.Administrator;

import security.UserAccount;
import services.AdministratorService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EditAdministratorTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private AdministratorService		administratorService;

	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 
	 * 1. Probando editar administrador con telefono y direcci�n a null
	 * 2. Probando editar administrador con telefono pero con direcci�n a null
	 * 3. Probando editar administrador con telefono a vac�o y direcci�n a null
	 * 4. Probando editar administrador con telefono a null y direcci�n
	 * 5. Probando editar administrador con telefono a null y direcci�n a vac�o
	 * 6. Probando editar administrador con telefono y direcci�n
	 * 7. Probando editar administrador con telefono y direcci�n a vac�o
	 * 
	 * Requisitos:
	 * 
	 * 	Se desea probar la correcta edici�n de un administrador.
	 */
	@Test
	public void positiveEditAdministratorTest() {
		Calendar calendar;
		Date date;
		
		calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR) - 30, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		date = calendar.getTime();
		
		final Object testingData[][] = {
			{ 
				"admin", "administrator", "admin", "admin", "Cristina", "Sgourakes", null, null, date, "administrator@mail.com", null
			}, {
				"admin", "administrator", "admin", "admin", "Manuel", "Kudera", "987532146", null, date, "administrator@hotmail.com", null
			}, {
				"admin", "administrator", "admin", "admin", "Carlos", "S�nchez", "", null, date, "carlosadministrator@mail.com", null
			}, {
				"admin", "administrator", "admin", "admin", "Paco", "Jespersen", null, "Calle No tan Real", date, "paquito@mail.com", null
			}, {
				"admin", "administrator", "admin", "admin", "Jos�", "Rumer", null, "", date, "joselito@mail.com", null
			}, {
				"admin", "administrator", "admin", "admin", "Pepe", "Escolar", "258753159", "Direcci�n Correcta", date, "pepe@mail.com", null
			}, {
				"admin", "administrator", "admin", "admin", "Fran", "Vinson", "", "", date, "fran@mail.com", null
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
	 * 1. Solo el administratoristrador puede editarse a si mismo
	 * 2. Solo el administratoristrador puede editarse a si mismo
	 * 3. Solo el administratoristrador puede editarse a si mismo
	 * 4. La fecha debe ser pasada
	 * 5. La fecha no puede ser nula
	 * 6. El email tiene que tener el formato de un email
	 * 7. El nombre no puede ser nulo
	 * 8. El apellido no puede ser nulo
	 * 9. El nombre no puede ser vac�o
	 * 10. El apellido no puede ser vac�o
	 * 11. El email no puede ser nulo
	 * 12. El email no puede ser vac�o
	 * 13. El username no puede cambiar
	 * 14. La password no puede cambiar
	 * 
	 * Requisitos:
	 * 
	 * 	Se desea probar la correcta edici�n de un administrador.
	 */
	@Test()
	public void negativeEditAdministratorTest() {
			Calendar calendar;
			Date dateGood, dateBad;
		
			calendar = Calendar.getInstance();
			calendar.set(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dateGood = calendar.getTime();
			
			calendar.set(calendar.get(Calendar.YEAR) + 22, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dateBad = calendar.getTime();
			
		final Object testingData[][] = {
			{
				"user1", "administrator", "admin", "admin", "Antonio", "Aza�a", null, null, dateGood, "ant@mail.com", IllegalArgumentException.class 
			}, {
				"manager2", "administrator", "admin", "admin", "Jes�s", "Harvey", "652147893", null, dateGood, "harvey@mail.com", IllegalArgumentException.class 
			}, {
				"user1", "administrator", "admin", "admin", "Gostin", "Perez", "", "Calle User Nº41", dateGood, "gostin@mail.com", IllegalArgumentException.class 
			}, {
				"admin", "administrator", "admin", "admin", "Alejandro", "Aza�a", null, null, dateBad, "alexito@mail.com", ConstraintViolationException.class 
			}, {
				"admin", "administrator", "admin", "admin", "Manuel", "Sterne", null, null, null, "sterne@mail.com", ConstraintViolationException.class 
			}, {
				"admin", "administrator", "admin", "admin", "Diana", "Mart�n", "664857123", "Calle Falsa 23", dateGood, "diana", ConstraintViolationException.class 
			}, {
				"admin", "administrator", "admin", "admin", null, "Ahmed", "664857123", "Calle Inventada", dateGood, "m@mail.com", ConstraintViolationException.class 
			}, {
				"admin", "administrator", "admin", "admin", "Paco", null, "664857123", "Calle sin numero", dateGood, "paco@gmail.es", ConstraintViolationException.class 
			}, {
				"admin", "administrator", "admin", "admin", "", "Ahmed", "664857123", "Calle Inventada", dateGood, "m@mail.com", ConstraintViolationException.class 
			}, {
				"admin", "administrator", "admin", "admin", "Luc�a", "", "664857123", "Calle sin numero", dateGood, "lucia@gmail.es", ConstraintViolationException.class 
			},{
				"admin", "administrator", "admin", "admin", "Marta", "Merdoz", "664857123", "Calle Novena", dateGood, null, ConstraintViolationException.class 
			}, {
				"admin", "administrator", "admin", "admin", "Mar�a", "Villar�n", "664254123", "Inserte direcci�n", dateGood, "", ConstraintViolationException.class 
			}, {
				"admin", "administrator", "user50", "admin", "Gostin", "Perez", "", "Calle User Nº41", dateGood, "gostin@mail.com", IllegalArgumentException.class 
			}, {
				"admin", "administrator", "admin", "manager", "Gostin", "Perez", "", "Calle User Nº41", dateGood, "gostin@mail.com", IllegalArgumentException.class 
			},
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
	 * 1. Nos autentificamos como admin
	 * 2. Tomamos el id y la entidad de admin
	 * 3. Creamos una copia del admin y de userAccount para que no se guarde solo con un set
	 * 4. Le asignamos el username, el password, el name, el surname, el phone, la address, el birthdate y el email correspondientes
	 * 5. Guardamos el admin copiado con los par�metros
	 * 6. Nos desautentificamos
	 */
	protected void template(final String administratorAuthenticate, final String administratorEdit, final String username, final String password, final String name, final String surname, final String phone, final String address, final Date birthdate, final String email, final Class<?> expected) {
		Class<?> caught;
		int administratorId;
		Administrator administratorEntity, administratorCopy;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();
		caught = null;
		try {
			super.authenticate(administratorAuthenticate);

			administratorId = super.getEntityId(administratorEdit);
			administratorEntity = this.administratorService.findOne(administratorId);
			administratorCopy = this.copyAdministrator(administratorEntity);
			administratorCopy.getUserAccount().setUsername(username);
			administratorCopy.getUserAccount().setPassword(encoder.encodePassword(password, null));
			administratorCopy.setName(name);
			administratorCopy.setSurname(surname);
			administratorCopy.setPhone(phone);
			administratorCopy.setAddress(address);
			administratorCopy.setBirthdate(birthdate);
			administratorCopy.setEmail(email);
			this.administratorService.save(administratorCopy);
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	private Administrator copyAdministrator(final Administrator administrator) {
		Administrator result;

		result = new Administrator();
		result.setId(administrator.getId());
		result.setVersion(administrator.getVersion());
		result.setUserAccount(copyUserAccount(administrator.getUserAccount()));
		result.setName(administrator.getName());
		result.setSurname(administrator.getSurname());
		result.setAddress(administrator.getAddress());
		result.setBirthdate(administrator.getBirthdate());
		result.setEmail(administrator.getEmail());
		result.setPhone(administrator.getPhone());

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

