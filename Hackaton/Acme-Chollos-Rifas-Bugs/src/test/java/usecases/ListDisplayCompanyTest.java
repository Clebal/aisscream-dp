
package usecases;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.CompanyService;
import utilities.AbstractTest;
import domain.Company;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ListDisplayCompanyTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private CompanyService	companyService;

	// Tests ------------------------------------------------------------------

	/*
	 * 1. Listar y desplegar un usuario sin autenticar
	 * 2. Listar y desplegar un usuario autenticado como usuario
	 * 3. Listar y desplegar un usuario autenticado como compañía
	 * 4. Listar y desplegar un usuario autenticado como patrocinador
	 * 5. Listar y desplegar un usuario autenticado como moderador
	 */
	@Test
	public void driverPositiveTests() {
		final Object testingData[][] = {
			{
				null, "company1", null
			}, {
				"company1", "company1", null
			}, {
				"company1", "company2", null
			}, {
				"sponsor1", "company3", null
			}, {
				"moderator2", "company4", null
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListDisplay((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/*
	 * 1. Autenticar (o no).
	 * 2. Listar compañías
	 * 3. Desplegar usuario
	 */

	protected void templateListDisplay(final String actorBean, final String companyBean, final Class<?> expected) {
		Class<?> caught;
		Page<Company> companys;
		Integer countCompanys;
		Company companyChoosen, company;

		caught = null;
		try {
			
			super.startTransaction();
			
			this.authenticate(actorBean);

			//Inicializamos
			companyChoosen = null;

			company = this.companyService.findOne(super.getEntityId(companyBean));
			Assert.notNull(company);

			//Obtenemos las compañías
			companys = this.companyService.findAllPaginated(1, 1);
			Assert.notNull(companys);
			
			countCompanys = companys.getTotalPages();
			Assert.notNull(countCompanys);
	
			//Buscamos el que queremos desplegar
			for (int i = 0; i < countCompanys; i++) {
				companys = this.companyService.findAllPaginated(1+i, 5);

				//Si estamos pidiendo una página mayor
				if (companys.getContent().size() == 0)
					break;

				// Navegar hasta la compañía que queremos.
				for (final Company newCompany : companys.getContent())
					if (newCompany.equals(company)) {
						companyChoosen = newCompany;
						break;
					}

				if (companyChoosen != null) break;
			}

			Assert.notNull(companyChoosen);

			this.companyService.findOne(companyChoosen.getId());

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		this.checkExceptions(expected, caught);

	}

}
