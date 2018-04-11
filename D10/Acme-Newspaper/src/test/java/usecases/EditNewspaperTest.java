
package usecases;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.LoginService;
import services.NewspaperService;
import services.UserService;
import utilities.AbstractTest;
import domain.Newspaper;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EditNewspaperTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private UserService			userService;


	@Test
	public void publishTest() {
		final Object testingData[][] = {
			{
				"user", "user1", "newspaper6", true, null
			}, {
				"user", "user5", "newspaper5", true, null
			}, {
				"user", "user1", "newspaper1", true, IllegalArgumentException.class
			}, {
				"user", "user4", "newspaper4", true, IllegalArgumentException.class
			}, {
				"user", "user2", "newspaper6", false, IllegalArgumentException.class
			}, {
				"customer", "customer1", "newspaper6", false, IllegalArgumentException.class
			}, {
				"admin", "admin", "newspaper6", false, IllegalArgumentException.class
			}, {
				null, null, "newspaper6", false, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				System.out.println(i);
				this.templatePublish((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void putPublicPrivateTest() {
		final Object testingData[][] = {
			{
				"user", "user1", "newspaper1", true, false, null
			}, {
				"user", "user2", "newspaper2", true, true, null
			}, {
				"user", "user2", "newspaper1", false, false, IllegalArgumentException.class
			}, {
				"customer", "customer1", "newspaper1", false, false, IllegalArgumentException.class
			}, {
				"admin", "admin", "newspaper1", false, false, IllegalArgumentException.class
			}, {
				null, null, "newspaper1", false, false, IllegalArgumentException.class
			}, {
				"user", "user1", "newspaper1", true, true, IllegalArgumentException.class
			}, {
				"user", "user1", "newspaper2", false, true, IllegalArgumentException.class
			}, {
				"customer", "customer1", "newspaper2", false, true, IllegalArgumentException.class
			}, {
				"admin", "admin", "newspaper2", false, true, IllegalArgumentException.class
			}, {
				null, null, "newspaper2", false, true, IllegalArgumentException.class
			}, {
				"user", "user2", "newspaper2", true, false, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				System.out.println(i);
				this.templatePutPublicPrivate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (Boolean) testingData[i][4], (Class<?>) testingData[i][5]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void findOneFindOneToDisplayTest() {
		final Object testingData[][] = {
			{
				"user", "user1", "newspaper1", true, false, true, null
			}, {
				"user", "user1", "newspaper2", true, false, true, null
			}, {
				"user", "user1", "newspaper3", true, false, true, null
			}, {
				"user", "user4", "newspaper4", true, false, true, null
			}, {
				"customer", "customer1", "newspaper1", true, false, true, null
			}, {
				"admin", "admin", "newspaper4", true, false, true, null
			}, {
				null, null, "newspaper1", true, false, true, null
			}, {
				"user", "user1", "newspaper2", false, false, true, null
			}, {
				"user", "user1", "newspaper2", false, true, true, IllegalArgumentException.class
			}, {
				"user", "user1", "newspaper4", true, false, true, IllegalArgumentException.class
			}, {
				"customer", "customer1", "newspaper4", true, false, true, IllegalArgumentException.class
			}, {
				null, null, "newspaper4", true, false, false, IllegalArgumentException.class
			}, {
				null, null, "newspaper2", true, false, false, IllegalArgumentException.class
			}, {
				"user", "user1", "newspaper1", true, true, true, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				System.out.println(i);
				this.templateFindOneFindOneToDisplay((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (Boolean) testingData[i][4], (Boolean) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void templatePublish(final String user, final String username, final String newspaperBean, final boolean correctBeans, final Class<?> expected) {
		Class<?> caught;
		int newspaperId;
		int newspaperIdAux;

		caught = null;
		try {
			if (user != null)
				super.authenticate(username); //Nos logeamos si es necesario
			newspaperId = 0;
			if (user != null && user.equals("user")) {
				if (correctBeans == false)
					newspaperId = super.getEntityId(newspaperBean);
				else {
					newspaperIdAux = super.getEntityId(newspaperBean);
					for (int i = 1; i <= this.newspaperService.findByUserId(this.userService.findByUserAccountId(LoginService.getPrincipal().getId()).getId(), 1, 5).getTotalPages(); i++)
						//Se coge el servicio entre los servicios del manager logeado
						for (final Newspaper n : this.newspaperService.findByUserId(this.userService.findByUserAccountId(LoginService.getPrincipal().getId()).getId(), i, 5))
							if (newspaperIdAux == n.getId()) {
								newspaperId = n.getId();
								break;
							}
				}

			} else
				newspaperId = super.getEntityId(newspaperBean);

			this.newspaperService.publish(newspaperId);

			Assert.isTrue(this.newspaperService.findOne(newspaperId).getIsPublished() == true && this.newspaperService.findOne(newspaperId).getPublicationDate().compareTo(new Date()) <= 0);
			super.flushTransaction();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println(caught);
		System.out.println(expected);
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

	protected void templatePutPublicPrivate(final String user, final String username, final String newspaperBean, final boolean correctBeans, final boolean isPrivate, final Class<?> expected) {
		Class<?> caught;
		int newspaperId;
		int newspaperIdAux;

		caught = null;
		try {
			if (user != null)
				super.authenticate(username); //Nos logeamos si es necesario
			newspaperId = 0;
			if (user != null && user.equals("user")) {
				if (correctBeans == false)
					newspaperId = super.getEntityId(newspaperBean);
				else {
					newspaperIdAux = super.getEntityId(newspaperBean);
					for (int i = 1; i <= this.newspaperService.findByUserId(this.userService.findByUserAccountId(LoginService.getPrincipal().getId()).getId(), 1, 5).getTotalPages(); i++)
						//Se coge el servicio entre los servicios del manager logeado
						for (final Newspaper n : this.newspaperService.findByUserId(this.userService.findByUserAccountId(LoginService.getPrincipal().getId()).getId(), i, 5))
							if (newspaperIdAux == n.getId()) {
								newspaperId = n.getId();
								break;
							}
				}

			} else
				newspaperId = super.getEntityId(newspaperBean);

			if (isPrivate == true)
				this.newspaperService.putPublic(newspaperId);
			else
				this.newspaperService.putPrivate(newspaperId);

			if (isPrivate == true)
				Assert.isTrue(this.newspaperService.findOne(newspaperId).getIsPrivate() == false);
			else
				Assert.isTrue(this.newspaperService.findOne(newspaperId).getIsPrivate() == true);
			super.flushTransaction();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println(caught);
		System.out.println(expected);
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

	protected void templateFindOneFindOneToDisplay(final String user, final String username, final String newspaperBean, final boolean findOneToDisplay, final boolean falseId, final boolean correctNewspaper, final Class<?> expected) {
		Class<?> caught;
		int newspaperId;
		final Newspaper newspaper;
		final int newspaperIdAux;

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
			} else if (correctNewspaper == true) {
				newspaperIdAux = super.getEntityId(newspaperBean);
				for (int i = 1; i <= this.newspaperService.findPublicsAndPublicated(1, 5).getTotalPages(); i++)
					for (final Newspaper n : this.newspaperService.findPublicsAndPublicated(i, 5).getContent())
						if (newspaperIdAux == n.getId())
							newspaperId = n.getId();
			} else
				newspaperId = super.getEntityId(newspaperBean);

			if (findOneToDisplay == true)
				if (falseId == false)
					newspaper = this.newspaperService.findOneToDisplay(newspaperId); //Se prueba el findOneToEdit
				else
					newspaper = this.newspaperService.findOneToDisplay(0);
			else if (falseId == false)
				newspaper = this.newspaperService.findOne(newspaperId); //Se prueba el findOne
			else
				newspaper = this.newspaperService.findOne(0);

			Assert.notNull(newspaper);
			super.flushTransaction();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

}
