
package usecases;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.DataBinder;

import services.RendezvousService;
import services.UserService;
import utilities.AbstractTest;
import domain.Rendezvous;
import domain.User;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SaveRendezvousTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private UserService			userService;


	/*
	 * Pruebas
	 * 1.Creamos un nuevo rendezvous como el user1 inicializando todos los parámetros (no salta excepción)
	 * 2.Creamos un nuevo rendezvous como el user1 inicializando todos los parámetros menos el picture que está a nulo (no salta excepción)
	 * 3.Creamos un nuevo rendezvous como el user1 inicializando todos los parámetros menos el picture y las coordenadas que están a nulo (no salta excepción)
	 * 4.Creamos un nuevo rendezvous como el user1 iniicializando todos los parámetros y poniéndolo en draft mode (no salta excepción)
	 * 5.Creamos un nuevo rendezvous como el user1 inicializando todos los parámetros y poniendolo solo para adultos (no salta excepción)
	 * 6.Creamos un nuevo rendezvous como el user1 inicializando todos los parámetros y poniéndolo en draft mode y solo para adultos (no salta excepción)
	 * 7.Creamos un nuevo rendezvous como el user4 que es menor de edad inicializando todos los parámetros poniendo el picture a nulo (no salta excepción)
	 * 8.Creamos un nuevo rendezvous como el user4 que es menor de edad inicializando todos los parámetros menos las coordenadas que están a nulo (no salta excepción)
	 * 9.Creamos un nuevo rendezvous como el user4 que es menor de edad inicializando todos los parámetros poniendolo en draft mode (no salta excepción)
	 * 10.Creamos un nuevo rendezvous como el user4 que es menor de edad inicializando todos los parámetros (no salta excepción)
	 * 
	 * Requisitos:
	 * C.2:Users can create rendezvouses. For each rendezvous, the system must store its name, its description, the moment when it is going to be organised, an optional picture, optional GPS
	 * co-ordinates, and the creator and the list of attendants.
	 * C.5.2: An actor who is authenticated as a user must be able to create a rendezvous, which he is implicitly assumed to attend.
	 */
	@Test()
	public void testCreatePositive() {
		final Object testingData[][] = {
			{
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, null, null, null
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", null, false, false, 12.0, 298.7, null, null, null, null
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", null, false, false, null, null, null, null, null, null
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", true, false, 12.0, 298.7, null, null, null, null
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, true, 12.0, 298.7, null, null, null, null
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", true, true, 12.0, 298.7, null, null, null, null
			}, {
				"user", "user4", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", null, false, false, 12.0, 298.7, null, null, null, null
			}, {
				"user", "user4", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", null, false, false, null, null, null, null, null, null
			}, {
				"user", "user4", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", true, false, 12.0, 298.7, null, null, null, null
			}, {
				"user", "user4", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, null, null, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Boolean) testingData[i][6], (Boolean) testingData[i][7],
					(Double) testingData[i][8], (Double) testingData[i][9], (Boolean) testingData[i][10], (String) testingData[i][11], (String) testingData[i][12], (Class<?>) testingData[i][13]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Intentamos crear un rendezvous logeados como user1 poniendo en vacío el nombre, la descripción y el picture y a nulo el momento (salta un IllegalArgumentException)
	 * 2.Intentamos crear un rendezvous logeados como user1 poneindo en vacío el nombre, la descripción y el picture (salta un ConstraintViolationException)
	 * 3.Intentamos crear un rendezvous logeados como user1 poniendo en vacío la descripción y el picture (salta un ConstraintViolationException)
	 * 4.Intentamos crear un rendezvous logeados como user1 poniendo en vacío el nombre y el picture (salta un ConstraintViolationException)
	 * 5.Intentamos crear un rendezvous logeados como user1 poniendo en vacío el nombre y la descripción (salta un ConstraintViolationException)
	 * 6.Intentamos crear un rendezvous logeados como user1 poniendo en vacío la descripción (salta un ConstraintViolationException)
	 * 7.Intentamos crear un rendezvous logeados como user1 poniendo en vacío el nombre (salta un ConstraintViolationException)
	 * 8.Intentamos crear un rendezvous logeados como user1 inicializando todos los parámetros y poniendo el picture con un formato incorrecto (salta un ConstraintViolationException)
	 * 9.Intentamos crear un rendezvous logeados como user1 inicializando todos los parámetros y poniendo el campo de borrado a true (salta un IllegalArgumentException)
	 * 10.Intentamos crear un rendezvous logeados como user1 inicializando todos los parámetros y poniendo manualmente a otro usuario en el campo creator (salta un IllegalArgumentException)
	 * 11.Intentamos crear un rendezvous logeados como user1 inicializando todos los parámetros y metiéndole inicialmente un rendezvous en su lista de linkerRendezvouses (salta un IllegalArgumentException)
	 * 12.Intentamos crear un rendezvous logeados como manager1 (salta un IllegalArgumentException)
	 * 13.Intentamos crear un rendezvous logeados como admin (salta un IllegalArgumentException)
	 * 14.Intentamos crear un rendezvous sin estar logeados (salta un IllegalArgumentException)
	 * 15.Intentamos crear un rendezvous logeados como user4 que es menor de edad inicializando todos los parámetros y poniéndolo solo para adultos (salta un IllegalArgumentException)
	 * 16.Intentamos crear un rendezvous logeados como user4 que es menor de edad inicializando todos los parámetros y sobreescribiendo el campo de creator por otro usuario (salta un IllegalArgumentException)
	 * 17.Intentamos crear un rendezvous logeados como user1 inicializando todos los parámetros y poniendo el momento en el pasado (salta un IllegalArgumentException)
	 * 18.Intentamos crear un rendezvous logeados como user1 inicializando todos los parámetros y poniendo a nulo solo el campo longitude (salta un IllegalArgumentException)
	 * 19.Intentamos crear un rendezvous logeados como user1 inicializando todos los parámetros y poniendo a nulo solo el campo latitude (salta un IllegalArgumentException)
	 */
	@Test()
	public void testCreateNegative() {
		final Object testingData[][] = {
			{
				"user", "user1", "", "", null, "", false, false, 12.0, 298.7, null, null, null, IllegalArgumentException.class
			}, {
				"user", "user1", "", "", "11/09/2018 15:30", "", false, false, 12.0, 298.7, null, null, null, ConstraintViolationException.class
			}, {
				"user", "user1", "Rendezvous nuevo", "", "11/09/2018 15:30", "", false, false, 12.0, 298.7, null, null, null, ConstraintViolationException.class
			}, {
				"user", "user1", "", "Descripción nueva", "11/09/2018 15:30", "", false, false, 12.0, 298.7, null, null, null, ConstraintViolationException.class
			}, {
				"user", "user1", "", "", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, null, null, ConstraintViolationException.class
			}, {
				"user", "user1", "Nuevo rendezvous", "", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, null, null, ConstraintViolationException.class
			}, {
				"user", "user1", "", "Descripción nueva", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, null, null, ConstraintViolationException.class
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "efu0jew9u", false, false, 12.0, 298.7, null, null, null, ConstraintViolationException.class
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, true, null, null, IllegalArgumentException.class
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, false, "user2", null, IllegalArgumentException.class
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, false, null, "rendezvous1", IllegalArgumentException.class
			}, {
				"manager", "manager1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, null, null, IllegalArgumentException.class
			}, {
				"admin", "admin", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, null, null, IllegalArgumentException.class
			}, {
				null, null, "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, null, null, IllegalArgumentException.class
			}, {
				"user", "user4", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, true, 12.0, 298.7, null, null, null, IllegalArgumentException.class
			}, {
				"user", "user4", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, "user2", null, IllegalArgumentException.class
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2016 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, null, null, IllegalArgumentException.class
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, null, 298.7, null, null, null, IllegalArgumentException.class
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 13.4, null, null, null, null, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Boolean) testingData[i][6], (Boolean) testingData[i][7],
					(Double) testingData[i][8], (Double) testingData[i][9], (Boolean) testingData[i][10], (String) testingData[i][11], (String) testingData[i][12], (Class<?>) testingData[i][13]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	protected void template(final String user, final String username, final String name, final String description, final String moment, final String picture, final Boolean draft, final Boolean adultOnly, final Double latitude, final Double longitude,
		final Boolean isDeleted, final String creator, final String linkerRendezvous, final Class<?> expected) {
		Class<?> caught;
		Rendezvous rendezvous;
		int creatorId;
		User creatorAux;
		int rendezvousId;
		Rendezvous rendezvousLinker;
		int rendezvousesBeforeSavedByCreator;
		int rendezvousesAfterSavedByCreator;
		int loggedUserId;
		Rendezvous saved;
		SimpleDateFormat format;
		Date date;
		DataBinder binder;
		Rendezvous rendezvousReconstruct;

		date = null;
		rendezvousesBeforeSavedByCreator = 0;
		loggedUserId = 0;
		caught = null;
		try {
			if (user != null)
				super.authenticate(username); //Nos logeamos como un tipo de usuario si es necesario

			rendezvous = this.rendezvousService.create(); //Creamos el rendezvous
			rendezvous.setName(name); //Le modificamos el name
			rendezvous.setDescription(description);//Le modificamos la descripción
			if (moment != null) {
				format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				date = format.parse(moment); //Si el momento no es nulo creamos el momento
			}
			rendezvous.setMoment(date); //Le modificamos el momento
			rendezvous.setPicture(picture);//Le modificamos el picture

			rendezvous.setDraft(draft); //Le modificamos el draft
			if (adultOnly != null)
				rendezvous.setAdultOnly(adultOnly); //Le modificamos el adultOnly
			rendezvous.setLatitude(latitude); //Le modificamos la latitud
			rendezvous.setLongitude(longitude); //Le modificamos la longitud

			if (isDeleted != null)
				rendezvous.setIsDeleted(isDeleted); //Para probar hackeos dejamos modificar el campo isDeleted
			if (creator != null) {
				creatorId = super.getEntityId(creator);
				creatorAux = this.userService.findOne(creatorId);
				rendezvous.setCreator(creatorAux); //Para comprobar hackeos dejamos modificar el campo creator
			}
			if (linkerRendezvous != null) {
				rendezvousId = super.getEntityId(linkerRendezvous);
				rendezvousLinker = this.rendezvousService.findOne(rendezvousId);
				rendezvous.getLinkerRendezvouses().add(rendezvousLinker); //Para comprobar hackeos dejamos añadir un rendezvous a la lista de linkerRendezvouses
			}

			if (user != null) {
				loggedUserId = super.getEntityId(username);
				rendezvousesBeforeSavedByCreator = this.rendezvousService.countByCreatorId(loggedUserId); //Miramos los rendezvouses que tenía el usuario antes de persistir el rendezvous
			}
			binder = new DataBinder(rendezvous);
			rendezvousReconstruct = this.rendezvousService.reconstruct(rendezvous, binder.getBindingResult());
			saved = this.rendezvousService.save(rendezvousReconstruct); //Guardamos el rendezvous

			rendezvousesAfterSavedByCreator = this.rendezvousService.countByCreatorId(loggedUserId); //Miramos los rendezvouses que tenía el usuario después de persistir el rendezvous

			super.flushTransaction();

			Assert.isTrue(rendezvousesAfterSavedByCreator == rendezvousesBeforeSavedByCreator + 1); //Comprobamos que el usuario tiene un rendezvous más que antes
			Assert.isTrue(this.rendezvousService.findAll().contains(saved)); //Vemos que el nuevo rendezvous está entre la lista de rendezvouses de la BD

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

}
