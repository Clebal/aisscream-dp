
package usecases;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.NewspaperService;
import utilities.AbstractTest;
import domain.Newspaper;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DeleteNewspaperTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private NewspaperService	newspaperService;


	@Test
	public void findOneFindOneToDisplayTest() {
		final Object testingData[][] = {
			{
				"admin", "admin", "newspaper1", null
			}, {
				"admin", "admin", "newspaper2", null
			}, {
				"admin", "admin", "newspaper3", null
			}, {
				"admin", "admin", "newspaper4", null
			}, {
				"admin", "admin", "newspaper5", null
			}, {
				"admin", "admin", "newspaper6", null
			}, {
				"user", "user1", "newspaper1", IllegalArgumentException.class
			}, {
				"customer", "customer1", "newspaper1", IllegalArgumentException.class
			}, {
				null, null, "newspaper1", IllegalArgumentException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				System.out.println(i);
				this.templateDelete((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void templateDelete(final String user, final String username, final String newspaperBean, final Class<?> expected) {
		Class<?> caught;
		int newspaperId;
		Newspaper newspaper;
		int newspaperIdAux;

		caught = null;
		try {
			if (user != null)
				super.authenticate(username);//Nos logeamos si es necesario

			newspaperId = 0;
			if (user != null) {
				newspaperIdAux = super.getEntityId(newspaperBean);
				for (int i = 1; i <= this.newspaperService.findAllPaginated(1, 5).getTotalPages(); i++)
					for (final Newspaper n : this.newspaperService.findAllPaginated(i, 5).getContent())
						if (newspaperIdAux == n.getId())
							newspaperId = n.getId();
			} else
				newspaperId = super.getEntityId(newspaperBean);

			newspaper = this.newspaperService.findOne(newspaperId);
			this.newspaperService.delete(newspaper);

			Assert.isTrue(!this.newspaperService.findAll().contains(newspaper));

			super.flushTransaction();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}
}
