
package usecases;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
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
public class EditRendezvousTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private UserService			userService;


	@Test()
	public void positiveEditTest() {
		final Object testingData[][] = {
			{
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, false, null, null, null
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", true, true, 12.0, 298.7, null, false, null, null, null
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", null, false, false, 12.0, 298.7, null, false, null, null, null
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", true, false, null, null, null, false, null, null, null
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, true, 12.0, 298.7, null, false, null, null, null
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", true, false, 12.0, 298.7, null, false, null, null, null
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "15/05/2018 20:05", "http://www.imagenes.com/imagen1", true, false, 12.0, 298.7, null, false, null, null, null
			}, {
				"user", "user4", "rendezvous10", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", true, false, 12.0, 298.7, null, false, null, null, null
			}, {
				"user", "user4", "rendezvous10", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", null, true, false, 12.0, 298.7, null, false, null, null, null
			}, {
				"user", "user4", "rendezvous10", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", null, false, false, 12.0, 298.7, null, false, null, null, null
			}, {
				"user", "user4", "rendezvous10", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", null, false, false, null, null, null, false, null, null, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
					(Boolean) testingData[i][7], (Boolean) testingData[i][8], (Double) testingData[i][9], (Double) testingData[i][10], (Boolean) testingData[i][11], (Boolean) testingData[i][12], (String) testingData[i][13], (String) testingData[i][14],
					(Class<?>) testingData[i][15]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test()
	public void negativeEditTest() {
		final Object testingData[][] = {
			{
				"user", "user3", "rendezvous3", "", "", null, "", false, false, 12.0, 298.7, null, false, null, null, IllegalArgumentException.class
			}, {
				"user", "user3", "rendezvous3", "", "", "11/09/2018 15:30", "", false, false, 12.0, 298.7, null, false, null, null, ConstraintViolationException.class
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "", "11/09/2018 15:30", "", false, false, 12.0, 298.7, null, false, null, null, ConstraintViolationException.class
			}, {
				"user", "user3", "rendezvous3", "", "Descripción nueva", "11/09/2018 15:30", "", false, false, 12.0, 298.7, null, false, null, null, ConstraintViolationException.class
			}, {
				"user", "user3", "rendezvous3", "", "", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, false, null, null, ConstraintViolationException.class
			}, {
				"user", "user3", "rendezvous3", "Nuevo rendezvous", "", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, false, null, null, ConstraintViolationException.class
			}, {
				"user", "user3", "rendezvous3", "", "Descripción nueva", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, false, null, null, ConstraintViolationException.class
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "efu0jew9u", false, false, 12.0, 298.7, null, false, null, null, ConstraintViolationException.class
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, false, false, "user2", null, IllegalArgumentException.class
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, false, false, null, "rendezvous1", IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous1", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", true, true, 12.0, 298.7, null, false, null, null, IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous4", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", true, true, 12.0, 298.7, null, false, null, null, IllegalArgumentException.class
			}, {
				"manager", "manager1", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, true, null, null, IllegalArgumentException.class
			}, {
				"admin", "admin", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, true, null, null, IllegalArgumentException.class
			}, {
				null, null, "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, true, null, null, IllegalArgumentException.class
			}, {
				"user", "user4", "rendezvous10", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, true, 12.0, 298.7, null, false, null, null, IllegalArgumentException.class
			}, {
				"user", "user4", "rendezvous10", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, false, "user2", null, IllegalArgumentException.class
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2016 15:30", "http://www.imagenes.com/imagen1", false, false, 12.0, 298.7, null, false, null, null, IllegalArgumentException.class
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, null, 298.7, null, false, null, null, IllegalArgumentException.class
			}, {
				"user", "user3", "rendezvous3", "Rendezvous nuevo", "Descripción del rendezvous", "11/09/2018 15:30", "http://www.imagenes.com/imagen1", false, false, 13.4, null, null, false, null, null, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
					(Boolean) testingData[i][7], (Boolean) testingData[i][8], (Double) testingData[i][9], (Double) testingData[i][10], (Boolean) testingData[i][11], (Boolean) testingData[i][12], (String) testingData[i][13], (String) testingData[i][14],
					(Class<?>) testingData[i][15]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void addRemoveLinkTest() {
		final Object testingData[][] = {
			{
				"user", "user1", "rendezvous1", "rendezvous2", true, true, null
			}, {
				"user", "user1", "rendezvous2", "rendezvous1", true, false, null
			}, {
				"user", "user4", "rendezvous10", "rendezvous9", true, true, null
			}, {
				"user", "user4", "rendezvous10", "rendezvous8", true, false, null
			}, {
				"user", "user1", "service1", "category1", false, true, IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous2", "rendezvous1", false, true, IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous1", "category1", false, true, IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous1", "rendezvous1", false, true, IllegalArgumentException.class
			}, {
				"user", "user1", "service1", "rendezvous2", false, true, IllegalArgumentException.class
			}, {
				"user", "user3", "rendezvous4", "rendezvous1", false, true, IllegalArgumentException.class
			}, {
				null, null, "rendezvous1", "rendezvous2", false, true, IllegalArgumentException.class
			}, {
				"manager", "manager1", "rendezvous1", "rendezvous2", false, true, IllegalArgumentException.class
			}, {
				"admin", "admin", "rendezvous1", "rendezvous2", false, true, IllegalArgumentException.class
			}, {
				"user", "user1", "service1", "category1", false, false, IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous1", "rendezvous2", false, false, IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous2", "category1", false, false, IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous1", "rendezvous1", false, false, IllegalArgumentException.class
			}, {
				"user", "user1", "service1", "rendezvous1", false, false, IllegalArgumentException.class
			}, {
				null, null, "rendezvous2", "rendezvous1", false, false, IllegalArgumentException.class
			}, {
				"manager", "manager1", "rendezvous2", "rendezvous1", false, false, IllegalArgumentException.class
			}, {
				"admin", "admin", "rendezvous2", "rendezvous1", false, false, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.templateAddDeleteLink((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Boolean) testingData[i][4], (Boolean) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void findOneFindOneToEditTest() {
		final Object testingData[][] = {
			{
				"user", "user1", "rendezvous1", false, false, true, null
			}, {
				"manager", "manager1", "rendezvous1", false, false, false, null
			}, {
				"admin", "admin", "rendezvous1", false, false, false, null
			}, {
				null, null, "rendezvous1", false, false, false, null
			}, {
				"admin", "admin", "rendezvous1", true, false, false, null
			}, {
				"user", "user3", "rendezvous3", true, false, true, null
			}, {
				"user", "user1", "rendezvous1", false, true, true, IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous1", true, true, true, IllegalArgumentException.class
			}, {
				null, null, "rendezvous1", true, false, false, IllegalArgumentException.class
			}, {
				"manager", "manager1", "rendezvous1", true, false, false, IllegalArgumentException.class
			}, {
				"admin", "admin", "rendezvous4", true, false, false, IllegalArgumentException.class
			}, {
				"user", "user3", "rendezvous4", true, false, true, IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous1", true, false, true, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.templateFindOneFindOneToEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (Boolean) testingData[i][4], (Boolean) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void findOneToDisplayTest() {
		final Object testingData[][] = {
			{
				"user", "user1", "rendezvous1", false, true, null
			}, {
				"user", "user1", "rendezvous2", false, true, null
			}, {
				"manager", "manager1", "rendezvous2", false, true, null
			}, {
				"admin", "admin", "rendezvous2", false, true, null
			}, {
				null, null, "rendezvous1", false, true, null
			}, {
				"user", "user4", "rendezvous2", false, false, IllegalArgumentException.class
			}, {
				null, null, "rendezvous2", false, false, IllegalArgumentException.class
			}, {
				"user", "user1", "rendezvous1", true, true, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.templateFindOneToDisplay((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (Boolean) testingData[i][4], (Class<?>) testingData[i][5]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void templateEdit(final String user, final String username, final String rendezvousBean, final String name, final String description, final String moment, final String picture, final Boolean draft, final Boolean adultOnly,
		final Double latitude, final Double longitude, final Boolean isDeleted, final Boolean isNotMyRendezvous, final String creator, final String linkerRendezvous, final Class<?> expected) {
		Class<?> caught;
		int rendezvousId;
		int rendezvousIdAux;
		int creatorId;
		Rendezvous rendezvousAux;
		Rendezvous rendezvous;
		SimpleDateFormat format;
		Date date;
		Rendezvous saved;
		User creatorAux;
		Rendezvous rendezvousLinker;

		date = null;
		caught = null;
		try {
			if (user != null)
				super.authenticate(username);

			rendezvousId = 0;
			rendezvousIdAux = super.getEntityId(rendezvousBean);

			if (user != null && user.equals("user")) {
				if (isNotMyRendezvous == false) {
					creatorId = super.getEntityId(username);
					if (!username.equals("user4")) {
						for (int i = 1; i <= this.rendezvousService.countByCreatorId(creatorId); i++)
							for (final Rendezvous r : this.rendezvousService.findByCreatorId(creatorId, i, 5))
								if (rendezvousIdAux == r.getId())
									rendezvousId = r.getId();
					} else
						for (int i = 1; i <= this.rendezvousService.countByCreatorIdAllPublics(creatorId); i++)
							for (final Rendezvous r : this.rendezvousService.findByCreatorIdAllPublics(creatorId, i, 5))
								if (rendezvousIdAux == r.getId())
									rendezvousId = r.getId();
				} else
					rendezvousId = super.getEntityId(rendezvousBean);
			} else
				rendezvousId = super.getEntityId(rendezvousBean);

			rendezvousAux = this.rendezvousService.findOneToEdit(rendezvousIdAux);
			rendezvous = this.copyRendezvous(rendezvousAux);
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

			saved = this.rendezvousService.save(rendezvous);
			super.flushTransaction();

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

	protected void templateAddDeleteLink(final String user, final String username, final String myRendezvousBean, final String theAnotherRendezvousBean, final boolean correctBeans, final boolean add, final Class<?> expected) {
		Class<?> caught;
		int myRendezvousId;
		int theAnotherRendezvousId;
		int myRendezvousIdAux;
		final int theAnotherRendezvousIdAux;
		int creatorId;
		int pagesNumber;
		Rendezvous myRendezvousAux;
		Rendezvous theAnotherRendezvousAux;

		caught = null;
		try {
			if (user != null)
				super.authenticate(username);
			myRendezvousId = 0;
			theAnotherRendezvousId = 0;
			if (user != null && user.equals("user")) {
				if (correctBeans == false) {
					myRendezvousId = super.getEntityId(myRendezvousBean);
					theAnotherRendezvousId = super.getEntityId(theAnotherRendezvousBean);
				} else {
					creatorId = super.getEntityId(username);
					myRendezvousIdAux = super.getEntityId(myRendezvousBean);
					if (username.equals("user4")) {
						pagesNumber = this.rendezvousService.countByCreatorIdAllPublics(creatorId);
						pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
						for (int i = 1; i <= pagesNumber; i++)
							for (final Rendezvous r : this.rendezvousService.findByCreatorIdAllPublics(creatorId, i, 5))
								if (myRendezvousIdAux == r.getId()) {
									myRendezvousId = r.getId();
									break;
								}
					} else {
						pagesNumber = this.rendezvousService.countByCreatorId(creatorId);
						pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
						for (int i = 1; i <= pagesNumber; i++)
							for (final Rendezvous r : this.rendezvousService.findByCreatorId(creatorId, i, 5))
								if (myRendezvousIdAux == r.getId()) {
									myRendezvousId = r.getId();
									break;
								}
					}
					if (add == true) {
						theAnotherRendezvousIdAux = super.getEntityId(theAnotherRendezvousBean);
						if (username.equals("user4")) {
							pagesNumber = this.rendezvousService.countNotLinkedByRendezvousAllPublics(this.rendezvousService.findOne(myRendezvousId));
							pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
							for (int i = 1; i <= pagesNumber; i++)
								for (final Rendezvous r : this.rendezvousService.findNotLinkedByRendezvousAllPublics(this.rendezvousService.findOne(myRendezvousId), i, 5))
									if (theAnotherRendezvousIdAux == r.getId()) {
										theAnotherRendezvousId = r.getId();
										break;
									}
						} else {
							pagesNumber = this.rendezvousService.countNotLinkedByRendezvous(this.rendezvousService.findOne(myRendezvousId));
							pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
							for (int i = 1; i <= pagesNumber; i++)
								for (final Rendezvous r : this.rendezvousService.findNotLinkedByRendezvous(this.rendezvousService.findOne(myRendezvousId), i, 5))
									if (theAnotherRendezvousIdAux == r.getId()) {
										theAnotherRendezvousId = r.getId();
										break;
									}
						}
					} else {
						theAnotherRendezvousIdAux = super.getEntityId(theAnotherRendezvousBean);
						if (username.equals("user4")) {
							pagesNumber = this.rendezvousService.countByLinkerRendezvousIdAndAllpublics(myRendezvousId);
							pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
							for (int i = 1; i <= pagesNumber; i++)
								for (final Rendezvous r : this.rendezvousService.findByLinkerRendezvousIdAndAllpublics(myRendezvousId, i, 5))
									if (theAnotherRendezvousIdAux == r.getId()) {
										theAnotherRendezvousId = r.getId();
										break;
									}
						} else {
							pagesNumber = this.rendezvousService.countByLinkerRendezvousId(myRendezvousId);
							pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
							for (int i = 1; i <= pagesNumber; i++)
								for (final Rendezvous r : this.rendezvousService.findByLinkerRendezvousId(myRendezvousId, i, 5))
									if (theAnotherRendezvousIdAux == r.getId()) {
										theAnotherRendezvousId = r.getId();
										break;
									}
						}
					}
				}

			} else {
				myRendezvousId = super.getEntityId(myRendezvousBean);
				theAnotherRendezvousId = super.getEntityId(theAnotherRendezvousBean);
			}

			myRendezvousAux = this.rendezvousService.findOne(myRendezvousId);
			theAnotherRendezvousAux = this.rendezvousService.findOne(theAnotherRendezvousId);
			if (add == true)
				this.rendezvousService.addLink(myRendezvousAux, theAnotherRendezvousAux);
			else
				this.rendezvousService.removeLink(myRendezvousAux, theAnotherRendezvousAux);

			super.flushTransaction();

			if (add == true)
				Assert.isTrue(this.rendezvousService.findOne(theAnotherRendezvousAux.getId()).getLinkerRendezvouses().contains(myRendezvousAux));
			else
				Assert.isTrue(!this.rendezvousService.findOne(theAnotherRendezvousAux.getId()).getLinkerRendezvouses().contains(myRendezvousAux));

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

	protected void templateFindOneFindOneToEdit(final String user, final String username, final String rendezvousBean, final boolean findOneToEdit, final boolean falseId, final boolean isMyRendezvous, final Class<?> expected) {
		Class<?> caught;
		int rendezvousId;
		final Rendezvous rendezvous;
		int rendezvousIdAux;
		int pagesNumber;

		caught = null;
		try {
			if (user != null)
				super.authenticate(username);
			rendezvousId = 0;

			if (user != null && user.equals("user")) {
				if (findOneToEdit == true && isMyRendezvous == true) {
					rendezvousIdAux = super.getEntityId(rendezvousBean);
					if (!username.equals("user4")) {
						pagesNumber = this.rendezvousService.countByCreatorId(super.getEntityId(username));
						pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
						for (int i = 1; i <= pagesNumber; i++)
							for (final Rendezvous r : this.rendezvousService.findByCreatorId(super.getEntityId(username), i, 5))
								if (rendezvousIdAux == r.getId())
									rendezvousId = r.getId();
					} else {
						pagesNumber = this.rendezvousService.countByCreatorIdAllPublics(super.getEntityId(username));
						pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
						for (int i = 1; i <= pagesNumber; i++)
							for (final Rendezvous r : this.rendezvousService.findByCreatorIdAllPublics(super.getEntityId(username), i, 5))
								if (rendezvousIdAux == r.getId())
									rendezvousId = r.getId();
					}
				} else {
					rendezvousIdAux = super.getEntityId(rendezvousBean);
					if (!username.equals("user4")) {
						pagesNumber = this.rendezvousService.countAllPaginated();
						pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
						for (int i = 1; i <= pagesNumber; i++)
							for (final Rendezvous r : this.rendezvousService.findAllPaginated(i, 5))
								if (rendezvousIdAux == r.getId())
									rendezvousId = r.getId();
					} else {
						pagesNumber = this.rendezvousService.countAllPublics();
						pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
						for (int i = 1; i <= pagesNumber; i++)
							for (final Rendezvous r : this.rendezvousService.findAllPublics(i, 5))
								if (rendezvousIdAux == r.getId())
									rendezvousId = r.getId();
					}
				}

			} else {
				rendezvousIdAux = super.getEntityId(rendezvousBean);
				if (user == null) {
					pagesNumber = this.rendezvousService.countAllPublics();
					pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
					for (int i = 1; i <= pagesNumber; i++)
						for (final Rendezvous r : this.rendezvousService.findAllPublics(i, 5))
							if (rendezvousIdAux == r.getId())
								rendezvousId = r.getId();
				} else {
					pagesNumber = this.rendezvousService.countAllPaginated();
					pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
					for (int i = 1; i <= pagesNumber; i++)
						for (final Rendezvous r : this.rendezvousService.findAllPaginated(i, 5))
							if (rendezvousIdAux == r.getId())
								rendezvousId = r.getId();
				}
			}
			if (findOneToEdit == true)
				if (falseId == false)
					rendezvous = this.rendezvousService.findOneToEdit(rendezvousId);
				else
					rendezvous = this.rendezvousService.findOneToEdit(0);
			else if (falseId == false)
				rendezvous = this.rendezvousService.findOne(rendezvousId);
			else
				rendezvous = this.rendezvousService.findOne(0);

			Assert.notNull(rendezvous);
			super.flushTransaction();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		System.out.println("Expected " + expected);
		System.out.println("Caught " + caught);
		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

	protected void templateFindOneToDisplay(final String user, final String username, final String rendezvousBean, final boolean falseId, final boolean isVisibleForMe, final Class<?> expected) {
		Class<?> caught;
		int rendezvousId;
		final Rendezvous rendezvous;
		int rendezvousIdAux;
		int pagesNumber;

		caught = null;
		try {
			if (user != null)
				super.authenticate(username);
			rendezvousId = 0;

			rendezvousIdAux = super.getEntityId(rendezvousBean);

			if (isVisibleForMe == true) {
				if (user == null || username.equals("user4")) {
					pagesNumber = this.rendezvousService.countAllPublics();
					pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
					for (int i = 1; i <= pagesNumber; i++)
						for (final Rendezvous r : this.rendezvousService.findAllPublics(i, 5))
							if (rendezvousIdAux == r.getId())
								rendezvousId = r.getId();
				} else {
					pagesNumber = this.rendezvousService.countAllPaginated();
					pagesNumber = (int) Math.floor(((pagesNumber / (5 + 0.0)) - 0.1) + 1);
					for (int i = 1; i <= pagesNumber; i++)
						for (final Rendezvous r : this.rendezvousService.findAllPaginated(i, 5))
							if (rendezvousIdAux == r.getId())
								rendezvousId = r.getId();
				}
			} else
				rendezvousId = super.getEntityId(rendezvousBean);
			if (falseId == false)
				rendezvous = this.rendezvousService.findOneToDisplay(rendezvousId);
			else
				rendezvous = this.rendezvousService.findOneToDisplay(0);

			Assert.notNull(rendezvous);
			super.flushTransaction();

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
		Collection<Rendezvous> rendezvouses;
		Rendezvous rendezvousCopy;

		rendezvouses = new ArrayList<Rendezvous>();
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

		for (final Rendezvous r : rendezvous.getLinkerRendezvouses()) {
			rendezvousCopy = this.copyRendezvous2(r);
			rendezvouses.add(rendezvousCopy);
		}
		result.setLinkerRendezvouses(rendezvouses);

		return result;
	}

	private Rendezvous copyRendezvous2(final Rendezvous rendezvous) {
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
