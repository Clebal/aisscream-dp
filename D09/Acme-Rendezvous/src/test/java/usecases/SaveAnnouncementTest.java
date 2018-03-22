package usecases;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.DataBinder;

import domain.Announcement;
import domain.Rendezvous;

import services.AnnouncementService;
import services.RendezvousService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SaveAnnouncementTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private AnnouncementService		announcementService;
	
	@Autowired
	private RendezvousService		rendezvousService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 		1. Creamos un announcement con todos los par�metros correctos
	 */
	@Test
	public void driverPositiveTest() {
		final Object testingData[][] = {
			{
				"user1", "rendezvous1", "01/02/2018 12:30", "Test", "test", null
			}
		};
			
	for (int i = 0; i < testingData.length; i++)
		try {
			super.startTransaction();
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		} finally {
			super.rollbackTransaction();
		}
	}
	
	/*
	 * Pruebas:
	 * 		1. Un usuario trata de crear un announcement para un rendezvous que no es suyo.
	 * 		2. Un manager trata de crear un announcement cuando no lo tiene permitido
	 * 		3. Un administrator trata de crear un announcement cuando no lo tiene permitido
	 * 		4. Un usuario trata de crear un announcement con el campo title vac�o
	 * 		5. Un usuario trata de crear un announcement eliminando el campo title
	 * 		6. Un usuario trata de crear un announcement con el campo description vac�o
	 * 		7. Un usuario trata de crear un announcement eliminando el campo description
	 * 		8. Un usuario trata de crear un announcement para un rendezvous borrado
	 */
	@Test
	public void driverNegativeTest() {
		final Object testingData[][] = {
			{
				"user1", "rendezvous3", "01/01/2018 00:00", "Test", "test", IllegalArgumentException.class
			}, 	{
				"manager1", "rendezvous1", "01/01/2018 00:00", "Test", "test", IllegalArgumentException.class
			}, {
				"administrator1", "rendezvous1", "01/01/2018 00:00", "Test", "test", IllegalArgumentException.class
			}, {
				"user1", "rendezvous1", "01/01/2018 00:00", "", "test", ConstraintViolationException.class
			}, {
				"user1", "rendezvous1", "01/01/2018 00:00", null, "test", ConstraintViolationException.class
			}, {
				"user1", "rendezvous1", "01/01/2018 00:00", "Test", "", ConstraintViolationException.class
			}, {
				"user1", "rendezvous1", "01/01/2018 00:00", "Test", null, ConstraintViolationException.class
			}, {
				"user3", "rendezvous4", "01/01/2018 00:00", "Test", "test", IllegalArgumentException.class
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	/*
	 * Crear un announcement. Pasos:
	 * 1. Autenticarnos como usuario.
	 * 2. Listar los rendezvous
	 * 3. Escoger un rendezvous
	 * 4. Crear un announcement asociado a un rendezvous
	 * 5. Salvar el nuevo announcement
	 * 6. Dirigir al listado de las cuestiones
	 */
	protected void template(final String user, final String rendezvousName, final String moment, final String title, final String description, final Class<?> expected) {
		Class<?> caught;
		Announcement savedAnnouncement, newAnnouncement;
		Rendezvous rendezvous;
		Collection<Rendezvous> rendezvouses;
		int rendezvousId, pageRendezvous;
		DateFormat formatter;
		DataBinder binder;

		formatter = new SimpleDateFormat("dd/MM/yy HH:mm");

		caught = null;
		try {
			
			rendezvousId = super.getEntityId(rendezvousName);
			rendezvous = this.rendezvousService.findOne(rendezvousId);
			
			// 1. Autenticarnos como usuario
			super.authenticate(user);
			
			// 2. Listar los rendezvous
			pageRendezvous = this.getPage(rendezvous);
			Assert.notNull(pageRendezvous);
			rendezvouses = this.rendezvousService.findAllPaginated(pageRendezvous, 5);
			
			// 3. Escoger un rendezvous
			for(Rendezvous r: rendezvouses)
				if(r.getId() == rendezvousId) rendezvous = r;
			
			// 4. Crear un announcement asociado a un rendezvous
			newAnnouncement = this.announcementService.create(rendezvous);
			newAnnouncement.setMoment(formatter.parse(moment));
			newAnnouncement.setTitle(title);
			newAnnouncement.setDescription(description);
			newAnnouncement.setRendezvous(rendezvous);
			
			// 5. Salvar el nuevo announcement
			binder = new DataBinder(newAnnouncement);
			newAnnouncement = this.announcementService.reconstruct(newAnnouncement, binder.getBindingResult());
			savedAnnouncement = this.announcementService.save(newAnnouncement);
			
			// 6. Dirigir al listado de las cuestiones
			Assert.isTrue(this.announcementService.findAll().contains(savedAnnouncement));
			
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
	
	private Integer getPage(final Rendezvous rendezvous) {
		Integer result, collectionSize, pageNumber;
		Collection<Rendezvous> rendezvouses;

		collectionSize = this.rendezvousService.countAllPaginated();
        pageNumber = (int) Math.floor(((collectionSize / 5.0) - 0.1) + 1);

		result = null;
		for (int i = 1; i <= pageNumber; i++) {
			rendezvouses = this.rendezvousService.findAllPaginated(i, 5);
			if (rendezvouses.contains(rendezvous)) {
				result = i;
				break;
			}
		}

		return result;
	}

}

