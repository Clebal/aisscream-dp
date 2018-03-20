
package usecases;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

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
public class EditRendezvousTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private UserService			userService;


	/*
	 * Pruebas
	 * 1.Modificamos el rendezvous3 logeados como user3 el cual posee ese rendzvous, modificamos todos los parámetros (no salta excepción)
	 * 2.Modificamos el rendezvous3 logeados como user3 el cual posee ese rendzvous, modificamos todos los parámetros poniéndolo en draft mode y solo adultos (no salta excepción)
	 * 3.Modificamos el rendezvous3 logeados como user3 el cual posee ese rendzvous, modificamos todos los parámetros poniendo el picture a nulo (no salta excepción)
	 * 4.Modificamos el rendezvous3 logeados como user3 el cual posee ese rendzvous, modificamos todos los parámetros poniéndolo en draft mode y las coordenadas a nulo (no salta excepción)
	 * 5.Modificamos el rendezvous3 logeados como user3 el cual posee ese rendzvous, modificamos todos los parámetros poniéndolo para solo adultos (no salta excepción)
	 * 6.Modificamos el rendezvous3 logeados como user3 el cual posee ese rendzvous, modificamos todos los parámetros poniéndolo en draft mode (no salta excepción)
	 * 7.Modificamos el rendezvous3 logeados como user3 el cual posee ese rendzvous, modificamos todos los parámetros sin alterar el valor del momento (no salta excepción)
	 * 8.Modificamos el rendezvous10 logeados como user4 el cual posee ese rendzvous y es menor de edad, modificamos todos los parámetros poniéndolo en draft mode (no salta excepción)
	 * 9.Modificamos el rendezvous10 logeados como user4 el cual posee ese rendzvous y es menor de edad, modificamos todos los parámetros poniéndolo en draft mode y con el picture a nulo (no salta excepción)
	 * 10.Modificamos el rendezvous10 logeados como user4 el cual posee ese rendzvous y es menor de edad, modificamos todos los parámetros poniéndolo en final mode y con el picture a nulo (no salta excepción)
	 * 11.Modificamos el rendezvous10 logeados como user4 el cual posee ese rendzvous y es menor de edad, modificamos todos los parámetros poniéndolo en final mode y con el picture y las coordenadas a nulo (no salta excepción)
	 * 
	 * Requisitos
	 * C.5.2: An actor who is authenticated as a user must be able to Update or delete the rendezvouses that he or she is created. Deletion is virtual, that
	 * is: the information is not removed from the database, but the rendezvous cannot be updated.
	 */
	@Test()
	public void positiveEditTest() {
		final Object testingData[][] = {
			{
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, false, null, null, null
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", true, true, 12.0, 298.7, null, false, null, null, null
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", null, false, false, 12.0, 298.7, null, false, null, null, null
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", true, false, null, null, null, false, null, null, null
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, true, 12.0, 298.7, null, false, null, null, null
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", true, false, 12.0, 298.7, null, false, null, null, null
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "15/05/2018 20:05", "http://www.imagenes.com/imagen1", true, false, 12.0, 298.7, null, false, null, null, null
			}, {
				"user", "user4", "rendezvous10", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", true, false, 12.0, 298.7, null, false, null, null, null
			}, {
				"user", "user4", "rendezvous10", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", null, true, false, 12.0, 298.7, null, false, null, null, null
			}, {
				"user", "user4", "rendezvous10", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", null, false, false, 12.0, 298.7, null, false, null, null, null
			}, {
				"user", "user4", "rendezvous10", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", null, false, false, null, null, null, false, null, null, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
					(Boolean) testingData[i][7], (Boolean) testingData[i][8], (Double) testingData[i][9], (Double) testingData[i][10], (Boolean) testingData[i][11], (Boolean) testingData[i][12], (String) testingData[i][13], (String) testingData[i][14],
					(Class<?>) testingData[i][15]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Intentamos modificar el rendezvous3 logeados como user3 el cual tiene ese rendezvous, dejamos vacío el nombre, la descripción y el picture y dejamos a nulo el momento (salta un IllegalArgumentException)
	 * 2.Intentamos modificar el rendezvous3 logeados como user3 el cual tiene ese rendezvous, dejamos vacío el nombre, la descripción y el picture (salta un ConstraintViolationException)
	 * 3.Intentamos modificar el rendezvous3 logeados como user3 el cual tiene ese rendezvous, dejamos vacío la descripción y el picture (salta un ConstraintViolationException)
	 * 4.Intentamos modificar el rendezvous3 logeados como user3 el cual tiene ese rendezvous, dejamos vacío el nombre y el picture (salta un ConstraintViolationException)
	 * 5.Intentamos modificar el rendezvous3 logeados como user3 el cual tiene ese rendezvous, dejamos vacío el nombre y la descripción (salta un ConstraintViolationException)
	 * 6.Intentamos modificar el rendezvous3 logeados como user3 el cual tiene ese rendezvous, dejamos vacío la descripción (salta un ConstraintViolationException)
	 * 7.Intentamos modificar el rendezvous3 logeados como user3 el cual tiene ese rendezvous, dejamos vacío el nombre (salta un ConstraintViolationException)
	 * 8.Intentamos modificar el rendezvous3 logeados como user3 el cual tiene ese rendezvous, pones el picture con un formato incorrecto (salta un ConstraintViolationException)
	 * 9.Intentamos modificar el rendezvous3 logeados como user3 el cual tiene ese rendezvous, sobreescribimos el creador del rendezvous por otro usuario (salta un IllegalArgumentException)
	 * 10.Intentamos modificar el rendezvous3 logeados como user3 el cual tiene ese rendezvous, intentamos añadirle un nuevo rendezvous a su lista de linkerRendezvouses (salta un IllegalArgumentException)
	 * 11.Intentamos modificar el rendezvous1 logeados como user1 el cual tiene ese rendezvous, el rendezvous1 está en final mode (salta un IllegalArgumentException)
	 * 12.Intentamos modificar el rendezvous4 logeados como user3 el cual tiene ese rendezvous, el rendezvous4 está borrado (salta un IllegalArgumentException)
	 * 13.Intentamos modificar el rendezvous10 logeados como user1 el cual no tiene ese rendezvous (salta un IllegalArgumentException)
	 * 14.Intentamos modificar el rendezvous3 logeados como manager1 (salta un IllegalArgumentException)
	 * 15.Intentamos modificar el rendezvous3 logeados como admin (salta un IllegalArgumentException)
	 * 16.Intentamos modificar el rendezvous3 sin estar logeados (salta un IllegalArgumentException)
	 * 17.Intentamos modificar el rendezvous10 logeados como user4 que es menor de edad el cual tiene ese rendezvous, le ponemos el rendezvous solo para adultos (salta un IllegalArgumentException)
	 * 18.Intentamos modificar el rendezvous10 logeados como user4 que es menor de edad el cual tiene ese rendezvous, le cambiamos el creador del rendezvous por otro usuario (salta un IllegalArgumentException)
	 * 19.Intentamos modificar el rendezvous3 logeados como user3 el cual tiene ese rendezvous, le ponemos la fecha en el pasado (salta un IllegalArgumentException)
	 * 20.Intentamos modificar el rendezvous3 logeados como user3 el cual tiene ese rendezvous, el campo latitude se queda a nulo solo (salta un IllegalArgumentException)
	 * 21.Intentamos modificar el rendezvous3 logeados como user3 el cual tiene ese rendezvous, el campo longitude se queda a nulo solo (salta un IllegalArgumentException)
	 */
	@Test()
	public void negativeEditTest() {
		final Object testingData[][] = {
			{
				"user", "user3", "rendezvous3", "", "", null, "", false, false, 12.0, 298.7, null, false, null, null, IllegalArgumentException.class
			}, {
				"user", "user3", "rendezvous3", "", "", "11/09/2018 15:30", "", false, false, 12.0, 298.7, null, false, null, null, ConstraintViolationException.class
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "", "11/09/2018 15:30", "", false, false, 12.0, 298.7, null, false, null, null, ConstraintViolationException.class
			}, {
				"user", "user3", "rendezvous3", "", "Descripción nueva", "11/09/2018 15:30", "", false, false, 12.0, 298.7, null, false, null, null, ConstraintViolationException.class
			}, {
				"user", "user3", "rendezvous3", "", "", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, false, null, null, ConstraintViolationException.class
			}, {
				"user", "user3", "rendezvous3", "Nuevo rendezvous", "", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, false, null, null, ConstraintViolationException.class
			}, {
				"user", "user3", "rendezvous3", "", "Descripción nueva", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, false, null, null, ConstraintViolationException.class
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "efu0jew9u", false, false, 12.0, 298.7, null, false, null, null, ConstraintViolationException.class
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, false, false, "user2", null, IllegalArgumentException.class
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, false, false, null, "rendezvous1", IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", true, true, 12.0, 298.7, null, false, null, null, IllegalArgumentException.class
			}, {
				"user", "user3", "rendezvous4", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", true, true, 12.0, 298.7, null, false, null, null, IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous10", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", true, true, 12.0, 298.7, null, true, null, null, IllegalArgumentException.class
			}, {
				"manager", "manager1", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, true, null, null, IllegalArgumentException.class
			}, {
				"admin", "admin", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, true, null, null, IllegalArgumentException.class
			}, {
				null, null, "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, true, null, null, IllegalArgumentException.class
			}, {
				"user", "user4", "rendezvous10", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, true, 12.0, 298.7, null, false, null, null, IllegalArgumentException.class
			}, {
				"user", "user4", "rendezvous10", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, false, "user2", null, IllegalArgumentException.class
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2016 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, false, null, null, IllegalArgumentException.class
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, null, 298.7, null, false, null, null, IllegalArgumentException.class
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 13.4, null, null, false, null, null, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
					(Boolean) testingData[i][7], (Boolean) testingData[i][8], (Double) testingData[i][9], (Double) testingData[i][10], (Boolean) testingData[i][11], (Boolean) testingData[i][12], (String) testingData[i][13], (String) testingData[i][14],
					(Class<?>) testingData[i][15]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Nos logeamos como user1 y al rendezvous1 el cual es del user1 le linkeamos el rendezvous2 (no salta excepción)
	 * 2.Nos logeamos como user1 y al rendezvous2 el cual es del user1 le deslinkeamos el rendezvous1 (no salta excepción)
	 * 3.Nos logeamos como user4 y al rendezvous10 el cual es del user4 le linkeamos el rendezvous9 (no salta excepción)
	 * 4.Nos logeamos como user4 y al rendezvous10 el cual es del user4 le deslinkeamos el rendezvous8 (no salta excepción)
	 * 5.Nos logeamos como user1 e intentamos add un rendezvous con una id incorrecta a otro con id incorrecta (salta un IllegalArgumentException)
	 * 6.Nos logeamos como user1 y al rendezvous2 el cual es nuestro le intentamos linkear el rendezvous1 el cual ya tiene linkeado (salta un IllegalArgumentException)
	 * 7.Nos logeamos como user1 y al rendezvous2 el cual es nuestro le intentamos linkear un rendezvous con una id incorrecta (salta un IllegalArgumentException)
	 * 8.Nos logeamos como user1 y al rendezvous1 el cual es nuestro le intentamos linkear el rendezvous1 (salta un IllegalArgumentException)
	 * 9.Nos logeamos como user1 y a un rendezvous con id incorrecta le intentamos linkear el rendezvous2 (salta un IllegalArgumentException)
	 * 10.Nos logeamos como user3 y al rendezvous4 le intentamos linkear el rendezvous1, el rendezvous4 está borrado (salta un IllegalArgumentException)
	 * 11.Intentamos linkear el rendezvous1 con el rendezvous2 sin estar logeado (salta un IllegalArgumentException)
	 * 12.Intentamos linkear el rendezvous1 con el rendezvous2 estando logeados como manager1 (salta un IllegalArgumentException)
	 * 13.Intentamos linkear el rendezvous1 con el rendezvous2 estando logeados como admin (salta un IllegalArgumentException)
	 * 14.Nos logeamos como user1 e intentamos deslinkear un rendezvous con un id falso, otro rendezvous con id falso (salta un IllegalArgumentException)
	 * 15.Nos logeamos como user1 y al rendezvous1 que es del user1 le intentamos deslinkear el rendezvous2, el rendezvous1 no tiene al rendezvous2 linkeado (salta un IllegalArgumentException)
	 * 16.Nos logeamos como user1 y al rendezvous1 que es del user1 le intentamos deslinkear un rendezvous con una id incorrecta (salta un IllegalArgumentException)
	 * 17.Nos logeamos como user1 y al rendezvous1 que es del user1 le intentamos deslinkear el rendezvous1 (salta un IllegalArgumentException)
	 * 18.Nos logeamos como user1 y a un rendezvous de id incorrecta le intentamos deslinkear el rendezvous1 (salta un IllegalArgumentException)
	 * 19.Al rendezvous2 le intentamos deslinkear el rendezvous1 sin estar logeado (salta un IllegalArgumentException)
	 * 20.Al rendezvous2 le intentamos deslinkear el rendezvous1 logeados como manager1 (salta un IllegalArgumentException)
	 * 21.Al rendezvous2 le intentamos deslinkear el rendezvous1 logeados como admin (salta un IllegalArgumentException)
	 * 
	 * Requisitos
	 * B.13:A rendezvous may be explicitly linked to similar ones by its creator. Note that such links may be added or removed even if the rendezvous is saved in final mode. They must be listed whenever a rendezvous is shown
	 * B.16.4: An actor who is authenticated as a user must be able to link one of the rendezvouses that he or she is created to other similar rendezvouses
	 */
	@Test
	public void addRemoveLinkTest() {
		final Object testingData[][] = {
			{
				"user", "user1", "rendezvous1", "rendezvous2", true, true, null
			}, {
				"user", "user1", "rendezvous2", "rendezvous1", true, false, null
			}, {
				"user", "user4", "rendezvous10", "rendezvous9", true, true, null
			}, {
				"user", "user4", "rendezvous10", "rendezvous8", true, false, null
			}, {
				"user", "user1", "service1", "category1", false, true, IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous2", "rendezvous1", false, true, IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous1", "category1", false, true, IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous1", "rendezvous1", false, true, IllegalArgumentException.class
			}, {
				"user", "user1", "service1", "rendezvous2", false, true, IllegalArgumentException.class
			}, {
				"user", "user3", "rendezvous4", "rendezvous1", false, true, IllegalArgumentException.class
			}, {
				null, null, "rendezvous1", "rendezvous2", false, true, IllegalArgumentException.class
			}, {
				"manager", "manager1", "rendezvous1", "rendezvous2", false, true, IllegalArgumentException.class
			}, {
				"admin", "admin", "rendezvous1", "rendezvous2", false, true, IllegalArgumentException.class
			}, {
				"user", "user1", "service1", "category1", false, false, IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous1", "rendezvous2", false, false, IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous2", "category1", false, false, IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous1", "rendezvous1", false, false, IllegalArgumentException.class
			}, {
				"user", "user1", "service1", "rendezvous1", false, false, IllegalArgumentException.class
			}, {
				null, null, "rendezvous2", "rendezvous1", false, false, IllegalArgumentException.class
			}, {
				"manager", "manager1", "rendezvous2", "rendezvous1", false, false, IllegalArgumentException.class
			}, {
				"admin", "admin", "rendezvous2", "rendezvous1", false, false, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.templateAddDeleteLink((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Boolean) testingData[i][4], (Boolean) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Logeados como user1 hacemos el findOne del rendezvous1 (no salta una excepción)
	 * 2.Logeados como manager1 hacemos el findOne del rendezvous1 (no salta una excepción)
	 * 3.Logeados como admin hacemos el findOne del rendezvous1 (no salta una excepción)
	 * 4.Sin logear hacemos el findOne del rendezvous1 (no salta una excepción)
	 * 5.Logeados como admin haemos el findOneToEdit para el rendezvous1 (no salta una excepción)
	 * 6.Logeados como user3 hacemos el findOneToEdit para el rendezvous3 (no salta una excepción)
	 * 7.Logeados como user1 hacemos el findOne de un rendezvous de id 0 (salta un IllegalArgumentException)
	 * 8.Logeados como user1 hacemos el findOneToEdit de un rendezvous de id 0 (salta un IllegalArgumentException)
	 * 9.Sin estar logeado hacemos el findOneToEdit del rendezvous1 (salta un IllegalArgumentException)
	 * 10.Logeados como manager hacemos el findOneToEdit del rendezvous1 (salta un IllegalArgumentException)
	 * 11.Logeados como admin hacemos el findOneToEdit del rendezvous4 el cual está borrado (salta un IllegalArgumentException)
	 * 12.Logeados como user3 hacemos el findOneToEdit del rendezvous4 el cual está borrado (salta un IllegalArgumentException)
	 * 13.Logeados como user1 hacemos el findOneToEdit del rendezvous1 el cual está en final mode (salta un IllegalArgumentException)
	 */
	@Test
	public void findOneFindOneToEditTest() {
		final Object testingData[][] = {
			{
				"user", "user1", "rendezvous1", false, false, true, null
			}, {
				"manager", "manager1", "rendezvous1", false, false, false, null
			}, {
				"admin", "admin", "rendezvous1", false, false, false, null
			}, {
				null, null, "rendezvous1", false, false, false, null
			}, {
				"admin", "admin", "rendezvous1", true, false, false, null
			}, {
				"user", "user3", "rendezvous3", true, false, true, null
			}, {
				"user", "user1", "rendezvous1", false, true, true, IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous1", true, true, true, IllegalArgumentException.class
			}, {
				null, null, "rendezvous1", true, false, false, IllegalArgumentException.class
			}, {
				"manager", "manager1", "rendezvous1", true, false, false, IllegalArgumentException.class
			}, {
				"admin", "admin", "rendezvous4", true, false, false, IllegalArgumentException.class
			}, {
				"user", "user3", "rendezvous4", true, false, true, IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous1", true, false, true, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.templateFindOneFindOneToEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (Boolean) testingData[i][4], (Boolean) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas
	 * 1.Hacemos el findOneToDisplay del rendezvous1 logeados como user1 (no salta excepción)
	 * 2.Hacemos un findOneToDisplay del rendezvous2 logeados como user1 (no salta excepción)
	 * 3.Hacemos el findOneToDisplay del rendezvous2 logeados como manager1 (no salta excepción)
	 * 4.Hacemos el findOneToDisplay del rendezvous2 logeados como admin (no salta excepción)
	 * 5.Hacemos el findOneToDisplay del rendezvous1 sin estar logeado (no salta excepción)
	 * 6.Hacemos el findOneToDisplay del rendezvous2 que es para solo adultos logeados como user4 que es menor de edad (salta un IllegalArgumentException)
	 * 7.Hacemos el findOneToDisplay del rendezvous2 que es para solo adultos sin estar logeados (salta un IllegalArgumentException)
	 * 8.Hacemos el findOneToDisplay de un rendezvous con id 0 (salta un IllegalArgumentException)
	 */
	@Test
	public void findOneToDisplayTest() {
		final Object testingData[][] = {
			{
				"user", "user1", "rendezvous1", false, true, null
			}, {
				"user", "user1", "rendezvous2", false, true, null
			}, {
				"manager", "manager1", "rendezvous2", false, true, null
			}, {
				"admin", "admin", "rendezvous2", false, true, null
			}, {
				null, null, "rendezvous1", false, true, null
			}, {
				"user", "user4", "rendezvous2", false, false, IllegalArgumentException.class
			}, {
				null, null, "rendezvous2", false, false, IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous1", true, true, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.templateFindOneToDisplay((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (Boolean) testingData[i][4], (Class<?>) testingData[i][5]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void templateEdit(final String user, final String username, final String rendezvousBean, final String name, final String description, final String moment, final String picture, final Boolean draft, final Boolean adultOnly,
		final Double latitude, final Double longitude, final Boolean isDeleted, final Boolean isNotMyRendezvous, final String creator, final String linkerRendezvous, final Class<?> expected) {
		Class<?> caught;
		int rendezvousId;
		int rendezvousIdAux;
		int creatorId;
		Rendezvous rendezvousAux;
		Rendezvous rendezvous;
		SimpleDateFormat format;
		Date date;
		Rendezvous saved;
		User creatorAux;
		Rendezvous rendezvousLinker;

		date = null;
		caught = null;
		try {
			if (user != null)
				super.authenticate(username); //Nos logeamos si es necesario

			rendezvousId = 0;
			rendezvousIdAux = super.getEntityId(rendezvousBean);

			if (user != null && user.equals("user")) {
				if (isNotMyRendezvous == false) { //Si eres un user y el rendezvous que buscamos es nuestro
					creatorId = super.getEntityId(username);
					if (!username.equals("user4")) {
						for (int i = 1; i <= this.rendezvousService.countByCreatorId(creatorId); i++)
							//Si eres un usuario mayor de edad se busca entre las del creador sin tener en cuenta las restricción de adultez
							for (final Rendezvous r : this.rendezvousService.findByCreatorId(creatorId, i, 5))
								if (rendezvousIdAux == r.getId())
									rendezvousId = r.getId();
					} else
						for (int i = 1; i <= this.rendezvousService.countByCreatorIdAllPublics(creatorId); i++)
							//Si eres menor de edad se busca solo entre los tuyos públicos
							for (final Rendezvous r : this.rendezvousService.findByCreatorIdAllPublics(creatorId, i, 5))
								if (rendezvousIdAux == r.getId())
									rendezvousId = r.getId();
				} else
					rendezvousId = super.getEntityId(rendezvousBean);
			} else
				rendezvousId = super.getEntityId(rendezvousBean); //Si eres otro usuario o en el else anterior si no forma parte de tus rendezvouses se pilla directamente la id ya que estamos simulando un hackeo

			rendezvousAux = this.rendezvousService.findOneToEdit(rendezvousIdAux);
			rendezvous = this.copyRendezvous(rendezvousAux); //Se hace una copia para evitar que Spring lo persista directamente al hacer el set
			rendezvous.setName(name);
			rendezvous.setDescription(description);
			if (moment != null) {
				format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				date = format.parse(moment);
			}
			rendezvous.setMoment(date);
			rendezvous.setPicture(picture);

			rendezvous.setDraft(draft);
			if (adultOnly != null)
				rendezvous.setAdultOnly(adultOnly);
			rendezvous.setLatitude(latitude);
			rendezvous.setLongitude(longitude);
			//Hasta aquí se modifican los valores establecidos al rendezvous
			if (isDeleted != null)
				rendezvous.setIsDeleted(isDeleted); //Se pone paa hacer hackeos
			if (creator != null) {
				creatorId = super.getEntityId(creator);
				creatorAux = this.userService.findOne(creatorId);
				rendezvous.setCreator(creatorAux); //Se pone paa hacer hackeos
			}
			if (linkerRendezvous != null) {
				rendezvousId = super.getEntityId(linkerRendezvous);
				rendezvousLinker = this.rendezvousService.findOne(rendezvousId);
				rendezvous.getLinkerRendezvouses().add(rendezvousLinker);//Se pone paa hacer hackeos
			}

			saved = this.rendezvousService.save(rendezvous); //Se guarda el rendezvous
			super.flushTransaction();

			Assert.isTrue(this.rendezvousService.findAll().contains(saved)); //Se comprueba que el rendezvous guardado esté entre todos los de la bd

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

	protected void templateAddDeleteLink(final String user, final String username, final String myRendezvousBean, final String theAnotherRendezvousBean, final boolean correctBeans, final boolean add, final Class<?> expected) {
		Class<?> caught;
		int myRendezvousId;
		int theAnotherRendezvousId;
		int myRendezvousIdAux;
		final int theAnotherRendezvousIdAux;
		int creatorId;
		int pagesNumber;
		Rendezvous myRendezvousAux;
		Rendezvous theAnotherRendezvousAux;

		caught = null;
		try {
			if (user != null)
				super.authenticate(username); //Nos logeamos
			myRendezvousId = 0;
			theAnotherRendezvousId = 0;
			if (user != null && user.equals("user")) {
				if (correctBeans == false) { //Entramos aquí si pones beans erróneos a propósito, de esta forma se simulan hackeos
					myRendezvousId = super.getEntityId(myRendezvousBean);
					theAnotherRendezvousId = super.getEntityId(theAnotherRendezvousBean);
				} else {
					creatorId = super.getEntityId(username);
					myRendezvousIdAux = super.getEntityId(myRendezvousBean);
					if (username.equals("user4")) {
						pagesNumber = this.rendezvousService.countByCreatorIdAllPublics(creatorId); //Buscamos el primer rendezvous entre la lista de nuestros rendezvouses públicos si eres menor de edad
						pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
						for (int i = 1; i <= pagesNumber; i++)
							for (final Rendezvous r : this.rendezvousService.findByCreatorIdAllPublics(creatorId, i, 5))
								if (myRendezvousIdAux == r.getId()) {
									myRendezvousId = r.getId();
									break;
								}
					} else {
						pagesNumber = this.rendezvousService.countByCreatorId(creatorId); //Hacemos lo anterior pero entre todos los nuestros si eres mayor de edad
						pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
						for (int i = 1; i <= pagesNumber; i++)
							for (final Rendezvous r : this.rendezvousService.findByCreatorId(creatorId, i, 5))
								if (myRendezvousIdAux == r.getId()) {
									myRendezvousId = r.getId();
									break;
								}
					}
					if (add == true) { //Linkeamos el rendezvous
						theAnotherRendezvousIdAux = super.getEntityId(theAnotherRendezvousBean);
						if (username.equals("user4")) {
							pagesNumber = this.rendezvousService.countNotLinkedByRendezvousAllPublics(this.rendezvousService.findOne(myRendezvousId));
							pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
							for (int i = 1; i <= pagesNumber; i++)
								for (final Rendezvous r : this.rendezvousService.findNotLinkedByRendezvousAllPublics(this.rendezvousService.findOne(myRendezvousId), i, 5))
									//Buscamos el otro rendezvous entre nuestra lista de rendezvouses no linkeados pública si eres menor
									if (theAnotherRendezvousIdAux == r.getId()) {
										theAnotherRendezvousId = r.getId();
										break;
									}
						} else {
							pagesNumber = this.rendezvousService.countNotLinkedByRendezvous(this.rendezvousService.findOne(myRendezvousId));
							pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
							for (int i = 1; i <= pagesNumber; i++)
								for (final Rendezvous r : this.rendezvousService.findNotLinkedByRendezvous(this.rendezvousService.findOne(myRendezvousId), i, 5))
									//Buscamos en nuestra lista de rendezvouses no linkeados si eres mayor de edad
									if (theAnotherRendezvousIdAux == r.getId()) {
										theAnotherRendezvousId = r.getId();
										break;
									}
						}
					} else { //Deslinkeamos el rendezvous
						theAnotherRendezvousIdAux = super.getEntityId(theAnotherRendezvousBean);
						if (username.equals("user4")) {
							pagesNumber = this.rendezvousService.countByLinkerRendezvousIdAndAllpublics(myRendezvousId);
							pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
							for (int i = 1; i <= pagesNumber; i++)
								for (final Rendezvous r : this.rendezvousService.findByLinkerRendezvousIdAndAllpublics(myRendezvousId, i, 5))
									//Buscamos en nuestra lista de rendezvouses linkeados pública el otro rendezvous si eres menor de edad
									if (theAnotherRendezvousIdAux == r.getId()) {
										theAnotherRendezvousId = r.getId();
										break;
									}
						} else {
							pagesNumber = this.rendezvousService.countByLinkerRendezvousId(myRendezvousId);
							pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
							for (int i = 1; i <= pagesNumber; i++)
								for (final Rendezvous r : this.rendezvousService.findByLinkerRendezvousId(myRendezvousId, i, 5))
									//Buscamos en nuestra lista de rendezvouses linkeados el otro rendezvous
									if (theAnotherRendezvousIdAux == r.getId()) {
										theAnotherRendezvousId = r.getId();
										break;
									}
						}
					}
				}

			} else { //Si eres cualquier otro tipo de usuario se pillan las id directamente para así simular hackeos
				myRendezvousId = super.getEntityId(myRendezvousBean);
				theAnotherRendezvousId = super.getEntityId(theAnotherRendezvousBean);
			}

			myRendezvousAux = this.rendezvousService.findOne(myRendezvousId);
			theAnotherRendezvousAux = this.rendezvousService.findOne(theAnotherRendezvousId);
			if (add == true)
				this.rendezvousService.addLink(myRendezvousAux, theAnotherRendezvousAux); //Linkeamos los rendezvouses
			else
				this.rendezvousService.removeLink(myRendezvousAux, theAnotherRendezvousAux); //Deslinkeamos los rendezvouses

			super.flushTransaction();

			if (add == true)
				Assert.isTrue(this.rendezvousService.findOne(theAnotherRendezvousAux.getId()).getLinkerRendezvouses().contains(myRendezvousAux)); //Si se ha linkeado se comprueba que el rendezvous linkeado tiene a nuestro rendezvous cono linkeador
			else
				Assert.isTrue(!this.rendezvousService.findOne(theAnotherRendezvousAux.getId()).getLinkerRendezvouses().contains(myRendezvousAux));//Si se ha deslinkeado se comprueba que el rendezvous linkeado no tiene a nuestro rendezvous cono linkeador

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

	protected void templateFindOneFindOneToEdit(final String user, final String username, final String rendezvousBean, final boolean findOneToEdit, final boolean falseId, final boolean isMyRendezvous, final Class<?> expected) {
		Class<?> caught;
		int rendezvousId;
		final Rendezvous rendezvous;
		int rendezvousIdAux;
		int pagesNumber;

		caught = null;
		try {
			if (user != null)
				super.authenticate(username); //Nos autenticamos si es necesario
			rendezvousId = 0;

			if (user != null && user.equals("user")) {
				if (findOneToEdit == true && isMyRendezvous == true) { //Si vamos a hacer el findoneToEdit de uno nuestro
					rendezvousIdAux = super.getEntityId(rendezvousBean);
					if (!username.equals("user4")) {
						pagesNumber = this.rendezvousService.countByCreatorId(super.getEntityId(username));
						pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
						for (int i = 1; i <= pagesNumber; i++)
							for (final Rendezvous r : this.rendezvousService.findByCreatorId(super.getEntityId(username), i, 5))
								//Si eres un usuario mayor de edad buscas el rendezvous entre los tuyos
								if (rendezvousIdAux == r.getId())
									rendezvousId = r.getId();
					} else {
						pagesNumber = this.rendezvousService.countByCreatorIdAllPublics(super.getEntityId(username));
						pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
						for (int i = 1; i <= pagesNumber; i++)
							for (final Rendezvous r : this.rendezvousService.findByCreatorIdAllPublics(super.getEntityId(username), i, 5))
								//Si eres un usuario menor de edad lo buscas entre tus rendezvouses para todos los públicos
								if (rendezvousIdAux == r.getId())
									rendezvousId = r.getId();
					}
				} else { //Si no haces el findOneToEdit de un rendezvous tuyo
					rendezvousIdAux = super.getEntityId(rendezvousBean);
					if (!username.equals("user4")) {
						pagesNumber = this.rendezvousService.countAllPaginated(); //Si eres un usuario mayor de edad buscas entre todos los rendezvouses
						pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
						for (int i = 1; i <= pagesNumber; i++)
							for (final Rendezvous r : this.rendezvousService.findAllPaginated(i, 5))
								if (rendezvousIdAux == r.getId())
									rendezvousId = r.getId();
					} else {
						pagesNumber = this.rendezvousService.countAllPublics();
						pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
						for (int i = 1; i <= pagesNumber; i++)
							for (final Rendezvous r : this.rendezvousService.findAllPublics(i, 5))
								//Si eres menor de edad buscas entre todos los públicos 
								if (rendezvousIdAux == r.getId())
									rendezvousId = r.getId();
					}
				}

			} else { //Si no estás logeado como user
				rendezvousIdAux = super.getEntityId(rendezvousBean);
				if (user == null) {
					pagesNumber = this.rendezvousService.countAllPublics();
					pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
					for (int i = 1; i <= pagesNumber; i++)
						for (final Rendezvous r : this.rendezvousService.findAllPublics(i, 5))
							//Si no estas logeado buscas el rendezvous entre todos los públicos
							if (rendezvousIdAux == r.getId())
								rendezvousId = r.getId();
				} else {
					pagesNumber = this.rendezvousService.countAllPaginated();
					pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
					for (int i = 1; i <= pagesNumber; i++)
						for (final Rendezvous r : this.rendezvousService.findAllPaginated(i, 5))
							//Si estas logeado lo biscas entre todos los rendezvouses
							if (rendezvousIdAux == r.getId())
								rendezvousId = r.getId();
				}
			}
			if (findOneToEdit == true)
				if (falseId == false)
					rendezvous = this.rendezvousService.findOneToEdit(rendezvousId);
				else
					rendezvous = this.rendezvousService.findOneToEdit(0);
			else if (falseId == false)
				rendezvous = this.rendezvousService.findOne(rendezvousId);
			else
				rendezvous = this.rendezvousService.findOne(0);

			//Hacemos findOne o findOneToEdit dependiendo  de la condición
			Assert.notNull(rendezvous);//Comprobamos que ha devuelto el rendezvous
			super.flushTransaction();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

	protected void templateFindOneToDisplay(final String user, final String username, final String rendezvousBean, final boolean falseId, final boolean isVisibleForMe, final Class<?> expected) {
		Class<?> caught;
		int rendezvousId;
		final Rendezvous rendezvous;
		int rendezvousIdAux;
		int pagesNumber;

		caught = null;
		try {
			if (user != null)
				super.authenticate(username); //Nos logeamos si es necesario
			rendezvousId = 0;

			rendezvousIdAux = super.getEntityId(rendezvousBean);

			if (isVisibleForMe == true) { //Si va a ser visible el rendezvous para nosotros
				if (user == null || username.equals("user4")) {
					pagesNumber = this.rendezvousService.countAllPublics(); //Si eres menor de edad se busca entre todos los públicos
					pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
					for (int i = 1; i <= pagesNumber; i++)
						for (final Rendezvous r : this.rendezvousService.findAllPublics(i, 5))
							if (rendezvousIdAux == r.getId())
								rendezvousId = r.getId();
				} else {
					pagesNumber = this.rendezvousService.countAllPaginated(); //Si eres mayor de edad se busca para todos
					pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
					for (int i = 1; i <= pagesNumber; i++)
						for (final Rendezvous r : this.rendezvousService.findAllPaginated(i, 5))
							if (rendezvousIdAux == r.getId())
								rendezvousId = r.getId();
				}
			} else
				rendezvousId = super.getEntityId(rendezvousBean);
			if (falseId == false)
				rendezvous = this.rendezvousService.findOneToDisplay(rendezvousId);
			else
				rendezvous = this.rendezvousService.findOneToDisplay(0);

			//Hacemos el findOneToDisplay
			Assert.notNull(rendezvous); //Vemos si nos devuelve el rendezvous
			super.flushTransaction();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

	private Rendezvous copyRendezvous(final Rendezvous rendezvous) {
		Rendezvous result;
		Collection<Rendezvous> rendezvouses;
		Rendezvous rendezvousCopy;

		rendezvouses = new ArrayList<Rendezvous>();
		result = new Rendezvous();
		result.setId(rendezvous.getId());
		result.setVersion(rendezvous.getVersion());
		result.setName(rendezvous.getName());
		result.setDescription(rendezvous.getDescription());
		result.setMoment(rendezvous.getMoment());
		result.setPicture(rendezvous.getPicture());
		result.setDraft(rendezvous.getDraft());
		result.setAdultOnly(rendezvous.getAdultOnly());
		result.setLatitude(rendezvous.getLatitude());
		result.setLongitude(rendezvous.getLongitude());
		result.setIsDeleted(rendezvous.getIsDeleted());
		result.setCreator(rendezvous.getCreator());

		for (final Rendezvous r : rendezvous.getLinkerRendezvouses()) {
			rendezvousCopy = this.copyRendezvous2(r);
			rendezvouses.add(rendezvousCopy);
		}
		result.setLinkerRendezvouses(rendezvouses);

		return result;
	}

	private Rendezvous copyRendezvous2(final Rendezvous rendezvous) {
		Rendezvous result;

		result = new Rendezvous();
		result.setId(rendezvous.getId());
		result.setVersion(rendezvous.getVersion());
		result.setName(rendezvous.getName());
		result.setDescription(rendezvous.getDescription());
		result.setMoment(rendezvous.getMoment());
		result.setPicture(rendezvous.getPicture());
		result.setDraft(rendezvous.getDraft());
		result.setAdultOnly(rendezvous.getAdultOnly());
		result.setLatitude(rendezvous.getLatitude());
		result.setLongitude(rendezvous.getLongitude());
		result.setIsDeleted(rendezvous.getIsDeleted());
		result.setCreator(rendezvous.getCreator());
		result.setLinkerRendezvouses(rendezvous.getLinkerRendezvouses());

		return result;
	}

}
