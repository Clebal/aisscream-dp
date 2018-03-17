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
	 * findUnique debe devolver solo un configuration
	 */
	@Test
	public void testFindUnique() {
		Configuration configuration;
		
		configuration = this.configurationService.findUnique();
		
		Assert.notNull(configuration);
	}
	
	/*
	 * findName debe devolver el nombre por defecto
	 */
	@Test
	public void testFindName() {
		Assert.isTrue(this.configurationService.findName().equals("Adventure meetups"));
	}
	
	/*
	 * findBanner debe devolver la URL por defecto
	 */
	@Test
	public void testFindBanner() {
		Assert.isTrue(this.configurationService.findBanner().equals("https://tinyurl.com/adventure-meetup"));
	}
	
	/*
	 * findWelcomeMessage debe devolver un welcomeMessage para el inglés y otro para español
	 */
	@Test
	public void testFindWelcomeMessage() {
		Assert.isTrue(this.configurationService.findWelcomeMessage("es").equals("Tu sitio para organizar quedadas de aventura"));
		Assert.isTrue(this.configurationService.findWelcomeMessage("en").equals("Your place to organise your adventure meetups!"));
	}
	
	@Test
	public void testFindWelcomeMessage2() {
		Assert.isNull(this.configurationService.findWelcomeMessage("crac"));
	}
	
	/*
	 * El admin guarda la configuración tal y como está
	 * El admin guarda la configuración cambiando la propiedad name
	 * El admin guarda la configuración cambiando la propiedad banner
	 * El admin guarda la configuración cambiando la propiedad welcomeMessage
	 * El admin guarda la configuración cambiando todas las propiedades
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
			System.out.println(i);
			super.startTransaction();
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		} finally {
			super.rollbackTransaction();
		}
	}
	
	/*
	 * Un actor de tipo usuario no puede modificar configuration
	 * Un actor de tipo manager no puede modificar configuration
	 * La propiedad banner debe cumplir el pattern URL
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
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void template(final String user, final String name, final String banner, final String welcomeMessage, final Class<?> expected) {
		Class<?> caught;
		Configuration oldConfiguration, newConfiguration;

		caught = null;
		try {
			
			super.authenticate(user);
			
			oldConfiguration = this.configurationService.findUnique();
			
			newConfiguration = this.copyConfiguration(oldConfiguration);
			
			if(name != null) newConfiguration.setName(name);
			if(banner != null) newConfiguration.setBanner(banner);
			if(welcomeMessage != null) newConfiguration.setWelcomeMessage(welcomeMessage);
				
			this.configurationService.save(newConfiguration);
			
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
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

