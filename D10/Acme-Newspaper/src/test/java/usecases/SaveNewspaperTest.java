
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

import services.NewspaperService;
import services.UserService;
import utilities.AbstractTest;
import domain.Newspaper;
import domain.User;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SaveNewspaperTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private UserService			userService;


	/*
	 * Pruebas
	 * 1. Probamos a crear un servicio de forma normal, dándole valores a todos los parámetros
	 * 2. Probamos a crear un servicio dándole valores a todo menos a picture que es nulo
	 * 3. Probamos a crear un servicio sobreescribiendo el status a Accepted
	 * 4. Probamos a crear un servicio sobreescribiendo el manager del servicio por el que está logeado y el status a Accepted
	 * 5. Probamos a crear un servicio sobreescribiendo el manager del servicio por el que está logeado
	 * 
	 * Requisitos
	 * C.5.2: An actor who is registered as a manager must be able to Manage his or her services, which includes creating them
	 */
	@Test()
	public void testCreatePositive() {
		final Object testingData[][] = {
			{
				"user", "user1", "Titulo 1", "12/05/2020", "Descripción 1", "http://www.imagenes.com/imagen1", true, true, null, false, null
			}, {
				"user", "user1", "Titulo 1", "12/05/2020", "Descripción 1", null, true, true, null, false, null
			}, {
				"user", "user1", "Titulo 1", "12/05/2020", "Descripción 1", "http://www.imagenes.com/imagen1", false, true, null, false, null
			}, {
				"user", "user1", "Titulo 1", "12/05/2020", "Descripción 1", null, false, true, null, false, null
			}, {
				"user", "user1", "Titulo 1", "12/05/2020", "Descripción 1", "http://www.imagenes.com/imagen1", true, true, "user1", false, null
			}, {
				"user", "user1", "sexo", "12/05/2020", "Descripción 1", "http://www.imagenes.com/imagen1", true, true, null, true, null
			}, {
				"user", "user1", "Título", "12/05/2020", "sexo", "http://www.imagenes.com/imagen1", true, true, null, true, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				System.out.println(i);
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Boolean) testingData[i][6], (Boolean) testingData[i][7],
					(String) testingData[i][8], (Boolean) testingData[i][9], (Class<?>) testingData[i][10]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test()
	public void testCreateNegative() {
		final Object testingData[][] = {
			{
				"user", "user1", "Titulo 1", null, "Descripción", "http://www.imagenes.com/imagen1", true, true, null, false, IllegalArgumentException.class
			}, {
				"user", "user1", "", "12/05/2020", "Descripción", "http://www.imagenes.com/imagen1", true, true, null, false, ConstraintViolationException.class
			}, {
				"user", "user1", "Título", "12/05/2020", "", "http://www.imagenes.com/imagen1", true, true, null, false, ConstraintViolationException.class
			}, {
				"user", "user1", "Título", "12/05/2020", "Descripción", "yeynyew", true, true, null, false, ConstraintViolationException.class
			}, {
				"user", "user1", "", "12/05/2020", "", null, true, true, null, false, ConstraintViolationException.class
			}, {
				"user", "user1", "Titulo 1", "12/05/2020", "Descripción", "http://www.imagenes.com/imagen1", true, false, null, false, IllegalArgumentException.class
			}, {
				"customer", "customer1", "Titulo 1", "12/05/2020", "Descripción", "http://www.imagenes.com/imagen1", true, true, null, false, IllegalArgumentException.class
			}, {
				"admin", "admin", "Titulo 1", "12/05/2020", "Descripción", "http://www.imagenes.com/imagen1", true, true, null, false, IllegalArgumentException.class
			}, {
				null, null, "Titulo 1", "12/05/2020", "Descripción", "http://www.imagenes.com/imagen1", true, true, null, false, IllegalArgumentException.class
			}, {
				"user", "user1", "Titulo 1", "12/05/2020", "Descripción 1", "http://www.imagenes.com/imagen1", true, true, "user2", false, IllegalArgumentException.class
			}, {
				"user", "user1", "Titulo 1", "12/05/2017", "Descripción 1", "http://www.imagenes.com/imagen1", true, true, null, false, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				System.out.println(i);
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Boolean) testingData[i][6], (Boolean) testingData[i][7],
					(String) testingData[i][8], (Boolean) testingData[i][9], (Class<?>) testingData[i][10]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void template(final String user, final String username, final String title, final String publicationDate, final String description, final String picture, final Boolean isPrivate, final Boolean isPublished, final String userBean,
		final Boolean hasTabooWords, final Class<?> expected) {
		Class<?> caught;
		Newspaper newspaper;
		Newspaper saved;
		final int userId;
		final User userEntity;
		DataBinder binder;
		final Newspaper newspaperReconstruct;
		SimpleDateFormat format;
		Date date;

		date = null;
		caught = null;
		try {
			if (user != null)
				super.authenticate(username); //Nos logeamos si es necesario

			newspaper = this.newspaperService.create(); //Creamos el servicio
			newspaper.setTitle(title);
			if (publicationDate != null) {
				format = new SimpleDateFormat("dd/MM/yyyy");
				date = format.parse(publicationDate); //Si el momento no es nulo creamos el momento
			}
			newspaper.setPublicationDate(date); //Le modificamos el momento
			newspaper.setDescription(description);
			newspaper.setPicture(picture);
			newspaper.setIsPrivate(isPrivate);
			newspaper.setIsPublished(isPublished);
			//Modificamos sus parámetros

			if (userBean != null) {
				userId = super.getEntityId(userBean);
				userEntity = this.userService.findOne(userId);
				newspaper.setPublisher(userEntity); //Le modificamos manualmente el manager para probar hackeos

			}

			binder = new DataBinder(newspaper);
			newspaperReconstruct = this.newspaperService.reconstruct(newspaper, binder.getBindingResult());
			saved = this.newspaperService.save(newspaperReconstruct); //Guardamos el servicio
			super.flushTransaction();

			Assert.isTrue(this.newspaperService.findAll().contains(saved)); //Miramos si están entre todos los servicos de la BD

			if (hasTabooWords == true)
				Assert.isTrue(saved.getHasTaboo() == true);
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println(caught);
		System.out.println(expected);
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

}
