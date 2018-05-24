
package usecases;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ConfigurationService;
import utilities.AbstractTest;
import domain.Configuration;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ConfigurationTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private ConfigurationService	configurationService;


	// Tests ------------------------------------------------------------------

	//Pruebas
	/*
	 * 1. Actualizar configuracion por admin.
	 */
	@Test
	public void driverPositive() {

		//rol, banner, defaultAvatar, defaultImage, email, name, slogan, expected
		final Object testingData[][] = {
			{
				"admin", "http://web.com/banner.jpg", "http://web.com/avatar.jpg", "http://web.com/image.jpg", "hola@aisscream.com", "Acme Chollos y Rifas", "La mejor web", null
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateEditConfiguration((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);

	}

	//Pruebas
	/*
	 * 1. Actualizar configuracion por moderador. IllegalArgumentException
	 * 2. Actualizar configuracion por user. IllegalArgumentException
	 * 3. Actualizar configuracion por company. IllegalArgumentException
	 * 4. Actualizar configuracion por no autenticado. IllegalArgumentException
	 * 5. Actualizar configuracion por sponsor. IllegalArgumentException
	 */
	@Test
	public void driverStatementConstraint() {

		//rol, banner, defaultAvatar, defaultImage, email, name, slogan, expected
		final Object testingData[][] = {
			{
				"moderator1", "http://web.com/banner.jpg", "http://web.com/avatar.jpg", "http://web.com/image.jpg", "hola@aisscream.com", "Acme Chollos y Rifas", "La mejor web", IllegalArgumentException.class
			}, {
				"user1", "http://web.com/banner.jpg", "http://web.com/avatar.jpg", "http://web.com/image.jpg", "hola@aisscream.com", "Acme Chollos y Rifas", "La mejor web", IllegalArgumentException.class
			}, {
				"company1", "http://web.com/banner.jpg", "http://web.com/avatar.jpg", "http://web.com/image.jpg", "hola@aisscream.com", "Acme Chollos y Rifas", "La mejor web", IllegalArgumentException.class
			}, {
				null, "http://web.com/banner.jpg", "http://web.com/avatar.jpg", "http://web.com/image.jpg", "hola@aisscream.com", "Acme Chollos y Rifas", "La mejor web", IllegalArgumentException.class
			}, {
				"sponsor1", "http://web.com/banner.jpg", "http://web.com/avatar.jpg", "http://web.com/image.jpg", "hola@aisscream.com", "Acme Chollos y Rifas", "La mejor web", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateEditConfiguration((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);

	}

	/*
	 * 1. Actualizar configuracion banner no url. ConstraintViolationException
	 * 2. Actualizar configuracion avatar no url. ConstraintViolationException
	 * 3. Actualizar configuracion image no url. ConstraintViolationException
	 * 4. Actualizar configuracion email no válido. ConstraintViolationException
	 * 5. Actualizar configuracion name blanco. ConstraintViolationException
	 * 6. Actualizar configuracion slogan null. ConstraintViolationException
	 */
	@Test
	public void driverDataConstraint() {

		//rol, banner, defaultAvatar, defaultImage, email, name, slogan, expected
		final Object testingData[][] = {
			{
				"admin", "web.com/banner.jpg", "http://web.com/avatar.jpg", "http://web.com/image.jpg", "hola@aisscream.com", "Acme Chollos y Rifas", "La mejor web", ConstraintViolationException.class
			}, {
				"admin", "web.com/banner.jpg", "web.com/avatar.jpg", "http://web.com/image.jpg", "hola@aisscream.com", "Acme Chollos y Rifas", "La mejor web", ConstraintViolationException.class
			}, {
				"admin", "web.com/banner.jpg", "http://web.com/avatar.jpg", "web.com/image.jpg", "hola@aisscream.com", "Acme Chollos y Rifas", "La mejor web", ConstraintViolationException.class
			}, {
				"admin", "web.com/banner.jpg", "http://web.com/avatar.jpg", "web.com/image.jpg", "holaaisscream.com", "Acme Chollos y Rifas", "La mejor web", ConstraintViolationException.class
			}, {
				"admin", "web.com/banner.jpg", "http://web.com/avatar.jpg", "web.com/image.jpg", "holaa@isscream.com", " ", "La mejor web", ConstraintViolationException.class
			}, {
				"admin", "web.com/banner.jpg", "http://web.com/avatar.jpg", "web.com/image.jpg", "holaaisscream.com", "Acme Chollos y Rifas", null, ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)

			this.templateEditConfiguration((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);

	}

	/*
	 * 1. Autenticarnos.
	 * 2. Acceder a la configuración.
	 * 3. Editar configuración
	 */
	protected void templateEditConfiguration(final String username, final String banner, final String defaultAvatar, final String defaultImage, final String email, final String name, final String slogan, final Class<?> expected) {
		Class<?> caught;

		Configuration configuration;
		Configuration saved;

		caught = null;

		try {
			super.startTransaction();
			this.authenticate(username);

			//Sacamos la configuration
			configuration = this.configurationService.findUnique();

			configuration.setBanner(banner);
			configuration.setDefaultAvatar(defaultAvatar);
			configuration.setDefaultImage(defaultImage);
			configuration.setEmail(email);
			configuration.setName(name);
			configuration.setSlogan(slogan);

			saved = this.configurationService.save(configuration);
			this.configurationService.flush();

			//Vemos que se haya actualizado
			Assert.isTrue(saved.getBanner().equals(banner));
			Assert.isTrue(configuration.getDefaultAvatar().equals(defaultAvatar));
			Assert.isTrue(saved.getDefaultImage().equals(defaultImage));
			Assert.isTrue(saved.getEmail().equals(email));
			Assert.isTrue(saved.getName().equals(name));
			Assert.isTrue(saved.getSlogan().equals(slogan));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		this.checkExceptions(expected, caught);

	}
}
