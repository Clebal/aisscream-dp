
package usecases;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.DataBinder;

import services.RendezvousService;
import utilities.AbstractTest;
import domain.Rendezvous;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DeleteRendezvousTest extends AbstractTest {

	@Autowired
	private RendezvousService	rendezvousService;


	/*
	 * Pruebas
	 * 1. Borramos con el user3 el rendezvous3 que es suyo y está en draft mode (no salta excepción)
	 * 2. Borramos con el user4 el rendezvous10 que es suyo y está en draft mode (no salta excepción)
	 * 3. Borramos el rendezvous3 logeados como admin (no salta excepción)
	 * 4. Borramos el rendezvous10 logeados como admin (no salta excepción)
	 * 5. Intentamos borrar el rendezvous10 con un usuario que no es el suyo (salta un IllegalArgumentException)
	 * 6. Intentamos borrar el rendezvous10 logeados como manager (salta un IllegalArgumentException)
	 * 7. Intentamos borrar el rendezvous4 logeados como user3 (salta un IllegalArgumentException)
	 * 8. Intentamos borrar el rendezvous10 con el user4 poniéndolo previamente en final mode (salta un IllegalArgumentException)
	 * 9. Intentamos borrar el rendezvous10 sin estar logeado (salta un IllegalArgumentException)
	 * 10. Intentamos borrar el rendezvous4 que ya está borrado logeados como admin (salta un IllegalArgumentException)
	 * 11.Intentamos borrar el rendezvous1 que está en final mode con el user1 (salta un IllegalArgumentException)
	 * 
	 * Requisitos:
	 * C.5.3:An actor who is authenticated as a user must be able to delete the rendezvouses that he or she is created. Deletion is virtual, that
	 * is: the information is not removed from the database, but the rendezvous cannot be
	 * updated. Deleted rendezvouses are flagged as such when they are displayed
	 * C.6.2:An actor who is authenticated as an administrator must be able to remove a rendezvous that he or she thinks is inappropriate
	 */
	@Test
	public void deleteTest() {
		final Object testingData[][] = {
			{
				"user", "user3", "rendezvous3", true, null
			}, {
				"user", "user4", "rendezvous10", true, null
			}, {
				"admin", "admin", "rendezvous3", true, null
			}, {
				"admin", "admin", "rendezvous10", true, null
			}, {
				"user", "user1", "rendezvous10", true, IllegalArgumentException.class
			}, {
				"manager", "manager1", "rendezvous10", true, IllegalArgumentException.class
			}, {
				"user", "user3", "rendezvous4", true, IllegalArgumentException.class
			}, {
				"user", "user4", "rendezvous10", false, IllegalArgumentException.class
			}, {
				null, null, "rendezvous10", true, IllegalArgumentException.class
			}, {
				"admin", "admin", "rendezvous4", true, IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous1", true, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateDelete((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	protected void templateDelete(final String user, final String username, final String rendezvousBean, final Boolean draft, final Class<?> expected) {
		Class<?> caught;
		int rendezvousId;
		int rendezvousIdAux;
		Rendezvous rendezvous;
		Rendezvous rendezvousToDelete;
		int pagesNumber;
		DataBinder binder;
		Rendezvous rendezvousReconstruct;

		caught = null;
		rendezvous = null;
		try {
			if (user != null)
				super.authenticate(username);	//Nos logeamos si es necesario

			rendezvousId = 0;
			rendezvousIdAux = super.getEntityId(rendezvousBean);

			if (expected == null) { //Si no hay excepción
				if (user.equals("user")) {
					if (!username.equals("user4")) {  //Si no estamos como el usuario menor de edad debemos buscar el rendezvous entre los suyos
						pagesNumber = this.rendezvousService.countByCreatorId(super.getEntityId(username));
						pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
						for (int i = 1; i <= pagesNumber; i++)
							for (final Rendezvous r : this.rendezvousService.findByCreatorId(super.getEntityId(username), i, 5))
								if (rendezvousIdAux == r.getId()) {
									rendezvousId = r.getId();
									break;
								}
					} else { //Si estamos como el usuario menor de edad buscamos el rendezvous entre los suyos teniendo en cuenta de solo poder mirar los públicos
						pagesNumber = this.rendezvousService.countByCreatorIdAllPublics(super.getEntityId(username));
						pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
						for (int i = 1; i <= pagesNumber; i++)
							for (final Rendezvous r : this.rendezvousService.findByCreatorIdAllPublics(super.getEntityId(username), i, 5))
								if (rendezvousIdAux == r.getId()) {
									rendezvousId = r.getId();
									break;
								}
					}
				} else if (user.equals("admin")) { //Si estamos como admin buscamos el rendezvous entre todos los rendezvouses
					pagesNumber = this.rendezvousService.countAllPaginated();
					pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
					for (int i = 1; i <= pagesNumber; i++)
						for (final Rendezvous r : this.rendezvousService.findAllPaginated(i, 5))
							if (rendezvousIdAux == r.getId()) {
								rendezvousId = r.getId();
								break;
							}
				}
			} else
				rendezvousId = super.getEntityId(rendezvousBean); //Si hay una excepción es un intento de hackeo así que cogemos la id directamente simulando que se pone por la URL

			rendezvous = this.rendezvousService.findOneToEdit(rendezvousId);

			rendezvousToDelete = this.copyRendezvous(rendezvous);
			if (user != null && user.equals("user"))	//Si estamos como usuario sobreescribimos el draft para comprobar que este no se puede poner a final
				rendezvousToDelete.setDraft(draft);

			binder = new DataBinder(rendezvousToDelete);
			rendezvousReconstruct = this.rendezvousService.reconstruct(rendezvousToDelete, binder.getBindingResult());

			this.rendezvousService.virtualDelete(rendezvousReconstruct); //Borramos el rendezvous

			super.flushTransaction();

			Assert.isTrue(this.rendezvousService.findOne(rendezvous.getId()).getIsDeleted() == true); //Comprobamos que ese mismo rendezvous persistido en la BD está ahora borrado

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

	private Rendezvous copyRendezvous(final Rendezvous rendezvous) {
		Rendezvous result;

		result = new Rendezvous();
		result.setId(rendezvous.getId());
		result.setVersion(rendezvous.getVersion());
		result.setName(rendezvous.getName());
		result.setDescription(rendezvous.getDescription());
		result.setMoment(rendezvous.getMoment());
		result.setPicture(rendezvous.getPicture());
		result.setDraft(rendezvous.getDraft());
		result.setAdultOnly(rendezvous.getAdultOnly());
		result.setLatitude(rendezvous.getLatitude());
		result.setLongitude(rendezvous.getLongitude());
		result.setIsDeleted(rendezvous.getIsDeleted());
		result.setCreator(rendezvous.getCreator());
		result.setLinkerRendezvouses(rendezvous.getLinkerRendezvouses());

		return result;
	}

}
