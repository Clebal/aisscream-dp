package usecases;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Internationalization;

import services.InternationalizationService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SaveEditInternationalizationTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private InternationalizationService		internationalizationService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * findByCountryCodeAndMessageCode con valores que están en la base de datos
	 */
	@Test
	public void testFindByCountryCodeAndMessageCode1() {
		String countryCode, messageCode, value;
		Internationalization internationalization;
		
		countryCode = "es";
		messageCode = "welcomeMessage";
		value		= "Tu sitio para organizar quedadas de aventura";
		
		internationalization = this.internationalizationService.findByCountryCodeAndMessageCode(countryCode, messageCode);
		
		Assert.isTrue(internationalization.getValue().equals(value));
	}
	
	/*
	 * findByCountryCodeAndMessageCode con valores inventados
	 */
	@Test
	public void testFindByCountryCodeAndMessageCode2() {
		String countryCode, messageCode;
		Internationalization internationalization;
		
		countryCode = "test";
		messageCode = "test";
		
		internationalization = this.internationalizationService.findByCountryCodeAndMessageCode(countryCode, messageCode);
		
		Assert.isNull(internationalization);
	}
	
	@Test
	public void positiveTest() {
		final Object testingData[][] = {
			{
				"admin", null, "es", null, "testMessageCode", "testValue", null // Un administrador puede crear un internationalization
			},{
				"admin", "es", "es", "welcomeMessage", "welcomeMessage", "asdf", null // Un administrador puede editar un internationalization
			}
		};
			
	for (int i = 0; i < testingData.length; i++)
		try {
//			System.out.println(i);
			super.startTransaction();
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		} finally {
			super.rollbackTransaction();
		}
	}
	
	
	@Test()
	public void negativeTest() {
		final Object testingData[][] = {
			{
				"admin", "es", null, "welcomeMessage", "welcome", "Tu sitio para organizar quedadas de aventura", IllegalArgumentException.class // No se puede modificar el messageCode
			}, 	{
				"admin", "es", "en", "welcomeMessage", null, "Tu sitio para organizar quedadas de aventura", IllegalArgumentException.class // No se puede modificar el countryCode
			}, {
				"manager1", "es", null, "welcomeMessage", null, "asdf", IllegalArgumentException.class // No puede ser modificado por un manager
			}, {
				"user1", "es", null, "welcomeMessage", null, "asdf", IllegalArgumentException.class // No puede ser modificado por un user
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
//				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void template(final String user, final String oldCountryCode, final String countryCode, final String oldMessageCode, final String messageCode, final String value, final Class<?> expected) {
		Class<?> caught;
		Internationalization oldInternationalization, newInternationalization;

		caught = null;
		try {
			
			super.authenticate(user);
			
			if(oldCountryCode == null || oldMessageCode == null) {
				newInternationalization = this.internationalizationService.create();
				
				newInternationalization.setCountryCode(countryCode);
				newInternationalization.setMessageCode(messageCode);
			}else{
				oldInternationalization = this.internationalizationService.findByCountryCodeAndMessageCode(oldCountryCode, oldMessageCode);
				newInternationalization = this.copyInternationalization(oldInternationalization);
				
				if(countryCode != null) newInternationalization.setCountryCode(countryCode);
				if(messageCode != null) newInternationalization.setMessageCode(messageCode);
			}
		
			if(value != null) newInternationalization.setValue(value);
			
			this.internationalizationService.save(newInternationalization);
			
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
//		System.out.println("Expected " + expected);
//		System.out.println("Caught " + caught);
		super.checkExceptions(expected, caught);
	}
	
	private Internationalization copyInternationalization(final Internationalization internationalization) {
		Internationalization result;
//		BindingResult binding = null;
		
		Assert.notNull(internationalization);
		
//		result = this.internationalizationService.reconstruct(internationalization, binding);
		result = new Internationalization();
		result.setId(internationalization.getId());
		result.setVersion(internationalization.getVersion());
		result.setCountryCode(internationalization.getCountryCode());
		result.setMessageCode(internationalization.getMessageCode());
		result.setValue(internationalization.getValue());

		return result;
	}

}

