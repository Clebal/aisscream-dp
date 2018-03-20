
package usecases;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.LoginService;
import services.ServiceService;
import utilities.AbstractTest;
import domain.Service;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DeleteServiceTest extends AbstractTest {

	@Autowired
	private ServiceService	serviceService;


	/*
	 * Pruebas
	 * 1. Probamos a borrar el servicio6 con el manager 2
	 * 2. Probamos a borrar el servicio6 sin estar logeado (salta una excepción)
	 * 3. Probamos a borrar el servicio6 logeados como el manager que no es el del servicio (salta una excepción)
	 * 4. Probamos a borrar el servicio1 que tiene request asociadas (salta una excepción)
	 * 
	 * Requisito
	 * C.5.2: An actor who is registered as a manager must be able to manage his or her services, which includes deleting them as long as they are not required by any rendezvouses
	 */
	@Test
	public void deleteTest() {
		final Object testingData[][] = {
			{
				"manager", "manager2", "service6", null
			}, {
				null, null, "service6", IllegalArgumentException.class
			}, {
				"manager", "manager1", "service6", IllegalArgumentException.class
			}, {
				"manager", "manager1", "service1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.templateDelete((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void templateDelete(final String user, final String username, final String servicioBean, final Class<?> expected) {
		Class<?> caught;
		int serviceId;
		Service service;

		caught = null;
		service = null;
		try {
			if (user != null)
				super.authenticate(username); //Nos logeamos si es necesario
			serviceId = super.getEntityId(servicioBean);

			if (expected == null) { //Si no va a saltar excepción
				for (int i = 1; i <= this.serviceService.findByManagerUserAccountId(LoginService.getPrincipal().getId(), 1, 5).getTotalPages(); i++)
					//Cogemos el servicio entre todos los del manager logeado
					for (final Service s : this.serviceService.findByManagerUserAccountId(LoginService.getPrincipal().getId(), i, 5).getContent())
						if (serviceId == s.getId()) {
							service = s;
							break;
						}
			} else
				service = this.serviceService.findOne(serviceId); //Si va a saltar una excepción lo cogemos directamente para probar hackeos

			this.serviceService.delete(service); //Borramos el servicio

			super.flushTransaction();

			Assert.isTrue(!this.serviceService.findAll().contains(service)); //Miramos que ya no esté entre los servicios del sistema

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

}
