
package usecases;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

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
				System.out.println(i);
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

		caught = null;
		rendezvous = null;
		try {
			if (user != null)
				super.authenticate(username);

			rendezvousId = 0;
			rendezvousIdAux = super.getEntityId(rendezvousBean);

			if (expected == null) {
				if (user.equals("user")) {
					if (!username.equals("user4")) {
						pagesNumber = this.rendezvousService.countByCreatorId(super.getEntityId(username));
						pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
						for (int i = 1; i <= pagesNumber; i++)
							for (final Rendezvous r : this.rendezvousService.findByCreatorId(super.getEntityId(username), i, 5))
								if (rendezvousIdAux == r.getId()) {
									rendezvousId = r.getId();
									break;
								}
					} else {
						pagesNumber = this.rendezvousService.countByCreatorIdAllPublics(super.getEntityId(username));
						pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
						for (int i = 1; i <= pagesNumber; i++)
							for (final Rendezvous r : this.rendezvousService.findByCreatorIdAllPublics(super.getEntityId(username), i, 5))
								if (rendezvousIdAux == r.getId()) {
									rendezvousId = r.getId();
									break;
								}
					}
				} else if (user.equals("admin")) {
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
				rendezvousId = super.getEntityId(rendezvousBean);

			rendezvous = this.rendezvousService.findOneToEdit(rendezvousId);

			rendezvousToDelete = this.copyRendezvous(rendezvous);
			if (user != null && user.equals("user"))
				rendezvousToDelete.setDraft(draft);

			this.rendezvousService.virtualDelete(rendezvousToDelete);

			super.flushTransaction();

			Assert.isTrue(this.rendezvousService.findOne(rendezvous.getId()).getIsDeleted() == true);

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
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
