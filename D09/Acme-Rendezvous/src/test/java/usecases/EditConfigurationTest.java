package usecases;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import domain.Configuration;

import services.ConfigurationService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EditConfigurationTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private ConfigurationService	configurationService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 		1. El admin guarda la configuración tal y como está
	 * 		2. El admin guarda la configuración cambiando la propiedad name
	 * 		3. El admin guarda la configuración cambiando la propiedad banner
	 * 		4. El admin guarda la configuración cambiando la propiedad welcomeMessage
	 * 		5. El admin guarda la configuración cambiando todas las propiedades
	 * 
	 * Las pruebas comprenden los requisitos relacionados con la configuración del sistema
	 * para ser utilizado por distintas franquicias.
	 */
	@Test
	public void driverConfigurationPositiveTest() {
		final Object testingData[][] = {
			{
				"admin", null, null, null, null
			}, {
				"admin", "This is a test bro", null, null, null
			}, {
				"admin", null, "http://example.com/image.jpg", null, null
			}, {
				"admin", null, null, "testCode", null
			}, {
				"admin", "This is a test bro", "http://example.com/image.jpg", "testCode", null
			}
		};
			
	for (int i = 0; i < testingData.length; i++)
		try {
			super.startTransaction();
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		} finally {
			super.rollbackTransaction();
		}
	}
	
	/*
	 * Pruebas:
	 * 		1. Un actor de tipo usuario no puede modificar configuration
	 *		2. Un actor de tipo manager no puede modificar configuration
	 * 		3. La propiedad banner debe cumplir el pattern URL
	 * 
	 * Las pruebas comprenden los requisitos relacionados con la configuración del sistema
	 * para ser utilizado por distintas franquicias.
	 */
	@Test
	public void driverConfigurationNegativeTest() {
		final Object testingData[][] = {
			{
				"user1", null, null, null, IllegalArgumentException.class
			}, {
				"manager1", null, null, null, IllegalArgumentException.class
			}, {
				"admin", null, "asdf", null, ConstraintViolationException.class
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	/*
	 * Editar configuration. Pasos:
	 * 1. Autenticar administrador.
	 * 2. Obtener el único configuration que existe
	 * 3. Editamos el configuration
	 * 4. Guardamos el configuration
	 * 5. Volvemos a la vista de configuration
	 */
	protected void template(final String user, final String name, final String banner, final String welcomeMessage, final Class<?> expected) {
		Class<?> caught;
		Configuration oldConfiguration, newConfiguration, savedConfiguration;

		caught = null;
		try {
			
			// 1. Autenticar administrador.
			super.authenticate(user);
			
			// 2. Obtener el único configuration que existe
			oldConfiguration = this.configurationService.findUnique();
			
			// 3. Editamos el configuration
			newConfiguration = this.copyConfiguration(oldConfiguration);
			if(name != null) newConfiguration.setName(name);
			if(banner != null) newConfiguration.setBanner(banner);
			if(welcomeMessage != null) newConfiguration.setWelcomeMessage(welcomeMessage);
				
			// 4. Guardamos el configuration
			this.configurationService.save(newConfiguration);
			this.configurationService.flush();
			
			// 5. Volvemos a la vista de configuration
			savedConfiguration = this.configurationService.findUnique();
			if(name != null) Assert.isTrue(savedConfiguration.getName().equals(name));
			if(banner != null) Assert.isTrue(savedConfiguration.getBanner().equals(banner));
			if(welcomeMessage != null) Assert.isTrue(savedConfiguration.getWelcomeMessage().equals(welcomeMessage));
			
			super.unauthenticate();
			super.flushTransaction();
			
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		
		super.checkExceptions(expected, caught);
	}
	
	private Configuration copyConfiguration(final Configuration configuration) {
		Configuration result;
		
		Assert.notNull(configuration);
		
		result = new Configuration();
		result.setId(configuration.getId());
		result.setVersion(configuration.getVersion());
		result.setName(configuration.getName());
		result.setBanner(configuration.getBanner());
		result.setWelcomeMessage(configuration.getWelcomeMessage());

		return result;
	}

}

