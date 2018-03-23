
package usecases;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ConfigurationService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ListConfigurationTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private ConfigurationService	configurationService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * 1. findUnique debe devolver solo un configuration
	 * 2. findName debe devolver el nombre por defecto
	 * 3. findBanner debe devolver la URL por defecto
	 * 4. findWelcomeMessage no debe dar nulo si el countryCode existe
	 * 5. findWelcomeMessage no debe dar nulo si el countryCode existe
	 */
	@Test()
	public void driverPositiveTest() {
		final Object testingData[][] = {
			{
				"admin", "findUnique", null, null
			}, {
				"admin", "findName", null, null
			}, {
				"admin", "findBanner", null, null
			}, {
				"admin", "findFindWelcomeMessage", "es", null
			}, {
				"admin", "findFindWelcomeMessage", "en", null
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
//				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * 1. findWelcomeMessage debe tener como countryCode uno de los idiomas disponibles
	 */
	@Test()
	public void driveNegativeTest() {
		final Object testingData[][] = {
			{
				"admin", "findWelcomeMessage", "crac", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
//				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}

	}

	// Ancillary methods ------------------------------------------------------

	protected void template(final String user, final String method, final String countryCode, final Class<?> expected) {
		Class<?> caught;
		
		caught = null;
		try {
			super.authenticate(user);
			
			if(method.equals("findUnique"))
				Assert.notNull(this.configurationService.findUnique());
			else if(method.equals("findName"))
				Assert.isTrue(this.configurationService.findName().equals("Adventure meetups"));
			else if(method.equals("findBanner"))
				Assert.isTrue(this.configurationService.findBanner().equals("https://tinyurl.com/adventure-meetup"));
			else if(method.equals("findWelcomeMessage")) {
				Assert.notNull(countryCode);
				Assert.notNull(this.configurationService.findWelcomeMessage(countryCode));
			}
			
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
//		System.out.println("Expected " + expected);
//		System.out.println("Caught " + caught);
		super.checkExceptions(expected, caught);
	}
	
}
