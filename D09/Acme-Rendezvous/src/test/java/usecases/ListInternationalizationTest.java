package usecases;

import java.util.Collection;

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
public class ListInternationalizationTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private InternationalizationService		internationalizationService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 		1. El administrador llama al m�todo testFindBy
	 * 		2.
	 * 		3.
	 * 		4.
	 * 		5.
	 */
	@Test
	public void driverTestFindByCountryAndMessageCodeTest() {
		final Object testingData[][] = {
			{
				"admin", "findByCountryAndMessageCode", "es", "welcomeMessage", "Tu sitio para organizar quedadas de aventura", null
			}, {
				"admin", "findByCountryAndMessageCode", "en", "welcomeMessage", "Your place to organise your adventure meetups!", null
			}, {
				"admin", "findByCountryAndMessageCode", "test", "welcomeMessage", null, IllegalArgumentException.class
			}, {
				"admin", "findByCountryAndMessageCode", "en", "test", null, IllegalArgumentException.class
			}, {
				"admin", "findByCountryAndMessageCode", null, null, null, IllegalArgumentException.class
			}
		};
			 
	for (int i = 0; i < testingData.length; i++)
		try {
//			System.out.println(i);
			super.startTransaction();
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		} finally {
			super.rollbackTransaction();
		}
	}
	
	/*
	 * Pruebas:
	 * 		1.
	 * 		2.
	 * 		3.
	 */
	@Test
	public void driverTestFindAvailableLanguagesByMessageCodeTest() {
		final Object testingData[][] = {
			{
				"admin", "findAvailableLanguagesByMessageCode", null, "welcomeMessage", null, null
			}, {
				"admin", "findAvailableLanguagesByMessageCode", null, "welcomeMessage", null, null
			}, {
				"admin", "findAvailableLanguagesByMessageCode", null, null, null, IllegalArgumentException.class
			}
		};
			 
	for (int i = 0; i < testingData.length; i++)
		try {
//			System.out.println(i);
			super.startTransaction();
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		} finally {
			super.rollbackTransaction();
		}
	}
	

	// Ancillary methods ------------------------------------------------------

	protected void template(final String user, final String method, final String countryCode, final String messageCode, final String value, final Class<?> expected) {
		Class<?> caught;
		Internationalization internationalization;
		Collection<String> availableLanguage;

		caught = null;
		try {
			
			super.authenticate(user);
			
			if(method.equals("findByCountryAndMessageCode")) {
				internationalization = this.internationalizationService.findByCountryCodeAndMessageCode(countryCode, messageCode);
				if(value != null) Assert.isTrue(internationalization.getValue().equals(value));
			} else {
				availableLanguage = this.internationalizationService.findAvailableLanguagesByMessageCode(messageCode);
				Assert.isTrue(availableLanguage.size() == 2);
			}
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
//		System.out.println("Expected " + expected);
//		System.out.println("Caught " + caught);
		super.checkExceptions(expected, caught);
	}
	
}

