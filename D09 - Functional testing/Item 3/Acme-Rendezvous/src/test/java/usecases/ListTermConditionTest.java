
package usecases;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Administrator;
import domain.Internationalization;

import security.Authority;
import services.AdministratorService;
import services.InternationalizationService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ListTermConditionTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private InternationalizationService internationalizationService;
	
	@Autowired
	private AdministratorService			administratorService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 
	 * 1. Probamos listar los términos y condiciones en español
	 * 2. Probamos listar los términos y condiciones en inglés
	 * 
	 * Requisitos:
	 * 
	 * 	Se desea probar el listado correcto de los términos y condiciones.
	 */
	@Test
	public void positiveFindByCountryCodeAndMessageCodeTest() {
		
		final Object testingData[][] = {
			{
				"admin", "administrator", "es", "term.condition", null
			} , {
				"admin", "administrator", "en", "term.condition", null
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
	 * 
	 * 1. Sólo un administrador puede listar los términos y condiciones
	 * 2. Sólo un administrador puede listar los términos y condiciones
	 * 3. Debe estar logueado como un administrador
	 * 
	 * Requisitos:
	 * 
	 * 	Se desea probar el listado correcto de los términos y condiciones.
	 */
	@Test
	public void negativeFindByCountryCodeAndMessageCodeTest() {
		
		final Object testingData[][] = {
			{
				"user1", "user1", "es", "term.condition", IllegalArgumentException.class
			} , {
				"manager1", "manager1", "en", "term.condition", IllegalArgumentException.class
			}, {
				null, "manager1", "en", "term.condition", IllegalArgumentException.class
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
	 * 	Pasos:
	 * 
	 * 1. Nos autentificamos como admin
	 * 2. Tomamos el id y la entidad de admin
	 * 3. Obtenemos el termCondition correspondiente al countryCode y messageCode dado
	 * 4. Comprobamos que su value tiene el valor esperado
	 * 5. Nos desautentificamos
	 */
	protected void template(final String user, final String username, final String countryCode, final String messageCode, final Class<?> expected) {
		Class<?> caught;
		Internationalization termCondition;
		int adminId;
		Administrator adminEntity;
		Authority authority;
		
		authority = new Authority();
		authority.setAuthority("ADMIN");

		caught = null;
		try {
			super.authenticate(user);
			
			adminId = super.getEntityId(username);
			adminEntity = this.administratorService.findOne(adminId);
			Assert.notNull(adminEntity);
			Assert.isTrue(adminEntity.getUserAccount().getAuthorities().contains(authority));
			
			termCondition = this.internationalizationService.findByCountryCodeAndMessageCode(countryCode, messageCode);
			
			if (termCondition.getCountryCode().equals("es")) {
				Assert.isTrue(termCondition.getValue().substring(0, 50).equals("INFORMACIÓN RELEVANTE  Es requisito necesario para"));
			} else { 
				Assert.isTrue(termCondition.getValue().substring(0, 50).equals("RELEVANT INFORMATION    It is a necessary requirem"));
			}
			
			super.flushTransaction();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}
	
}
