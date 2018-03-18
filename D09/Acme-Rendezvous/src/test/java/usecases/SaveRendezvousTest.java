
package usecases;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.RendezvousService;
import services.UserService;
import utilities.AbstractTest;
import domain.Rendezvous;
import domain.User;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SaveRendezvousTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private UserService			userService;


	@Test()
	public void testCreatePositive() {
		final Object testingData[][] = {
			{
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, null, null, null
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", null, false, false, 12.0, 298.7, null, null, null, null
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", null, false, false, null, null, null, null, null, null
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", true, false, 12.0, 298.7, null, null, null, null
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, true, 12.0, 298.7, null, null, null, null
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", true, true, 12.0, 298.7, null, null, null, null
			}, {
				"user", "user4", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", null, false, false, 12.0, 298.7, null, null, null, null
			}, {
				"user", "user4", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", null, false, false, null, null, null, null, null, null
			}, {
				"user", "user4", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", true, false, 12.0, 298.7, null, null, null, null
			}, {
				"user", "user4", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, null, null, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Boolean) testingData[i][6], (Boolean) testingData[i][7],
					(Double) testingData[i][8], (Double) testingData[i][9], (Boolean) testingData[i][10], (String) testingData[i][11], (String) testingData[i][12], (Class<?>) testingData[i][13]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test()
	public void testCreateNegative() {
		final Object testingData[][] = {
			{
				"user", "user1", "", "", null, "", false, false, 12.0, 298.7, null, null, null, IllegalArgumentException.class
			}, {
				"user", "user1", "", "", "11/09/2018 15:30", "", false, false, 12.0, 298.7, null, null, null, ConstraintViolationException.class
			}, {
				"user", "user1", "Rendezvous nuevo", "", "11/09/2018 15:30", "", false, false, 12.0, 298.7, null, null, null, ConstraintViolationException.class
			}, {
				"user", "user1", "", "Descripción nueva", "11/09/2018 15:30", "", false, false, 12.0, 298.7, null, null, null, ConstraintViolationException.class
			}, {
				"user", "user1", "", "", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, null, null, ConstraintViolationException.class
			}, {
				"user", "user1", "Nuevo rendezvous", "", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, null, null, ConstraintViolationException.class
			}, {
				"user", "user1", "", "Descripción nueva", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, null, null, ConstraintViolationException.class
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "efu0jew9u", false, false, 12.0, 298.7, null, null, null, ConstraintViolationException.class
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, true, null, null, IllegalArgumentException.class
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, false, "user2", null, IllegalArgumentException.class
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, false, null, "rendezvous1", IllegalArgumentException.class
			}, {
				"manager", "manager1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, null, null, IllegalArgumentException.class
			}, {
				"admin", "admin", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, null, null, IllegalArgumentException.class
			}, {
				null, null, "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, null, null, IllegalArgumentException.class
			}, {
				"user", "user4", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, true, 12.0, 298.7, null, null, null, IllegalArgumentException.class
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, "user2", null, IllegalArgumentException.class
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2016 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, null, null, IllegalArgumentException.class
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, null, 298.7, null, null, null, IllegalArgumentException.class
			}, {
				"user", "user1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 13.4, null, null, null, null, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Boolean) testingData[i][6], (Boolean) testingData[i][7],
					(Double) testingData[i][8], (Double) testingData[i][9], (Boolean) testingData[i][10], (String) testingData[i][11], (String) testingData[i][12], (Class<?>) testingData[i][13]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	protected void template(final String user, final String username, final String name, final String description, final String moment, final String picture, final Boolean draft, final Boolean adultOnly, final Double latitude, final Double longitude,
		final Boolean isDeleted, final String creator, final String linkerRendezvous, final Class<?> expected) {
		Class<?> caught;
		Rendezvous rendezvous;
		int creatorId;
		User creatorAux;
		int rendezvousId;
		Rendezvous rendezvousLinker;
		int rendezvousesBeforeSavedByCreator;
		int rendezvousesAfterSavedByCreator;
		int loggedUserId;
		Rendezvous saved;
		SimpleDateFormat format;
		Date date;

		date = null;
		rendezvousesBeforeSavedByCreator = 0;
		loggedUserId = 0;
		caught = null;
		try {
			if (user != null)
				super.authenticate(username);

			rendezvous = this.rendezvousService.create();
			rendezvous.setName(name);
			rendezvous.setDescription(description);
			if (moment != null) {
				format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				date = format.parse(moment);
			}
			rendezvous.setMoment(date);
			rendezvous.setPicture(picture);

			rendezvous.setDraft(draft);
			if (adultOnly != null)
				rendezvous.setAdultOnly(adultOnly);
			rendezvous.setLatitude(latitude);
			rendezvous.setLongitude(longitude);

			if (isDeleted != null)
				rendezvous.setIsDeleted(isDeleted);
			if (creator != null) {
				creatorId = super.getEntityId(creator);
				creatorAux = this.userService.findOne(creatorId);
				rendezvous.setCreator(creatorAux);
			}
			if (linkerRendezvous != null) {
				rendezvousId = super.getEntityId(linkerRendezvous);
				rendezvousLinker = this.rendezvousService.findOne(rendezvousId);
				rendezvous.getLinkerRendezvouses().add(rendezvousLinker);
			}

			if (user != null) {
				loggedUserId = super.getEntityId(username);
				rendezvousesBeforeSavedByCreator = this.rendezvousService.countByCreatorId(loggedUserId);
			}
			saved = this.rendezvousService.save(rendezvous);

			rendezvousesAfterSavedByCreator = this.rendezvousService.countByCreatorId(loggedUserId);

			super.flushTransaction();

			Assert.isTrue(rendezvousesAfterSavedByCreator == rendezvousesBeforeSavedByCreator + 1);
			Assert.isTrue(this.rendezvousService.findAll().contains(saved));

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
