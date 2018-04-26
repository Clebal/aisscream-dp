
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


	/*
	 * Test
	 * 1. Publicamos el newspaper6 que no está publicado y puede publicarse (no salta excepción)
	 * 2. Publicamos el newspaper5 que no está publicado y puede publicarse (no salta excepción)
	 * 3. Intentamos publicar el newspaper1 que ya está publicado logeados como user1 (salta un IllegalArgumentException)
	 * 4. Intentamos publicar el newspaper4 que no puede ser publicado logeados como user4 (salta un IllegalArgumentException)
	 * 5. Intentamos publicar el newspaper6 como el user2 que no es su creador (salta un IllegalArgumentException)
	 * 6. Intentamos publicar el newspaper6 logeados como customer (salta un IllegalArgumentException)
	 * 7. Intentamos publicar el newspaper6 logeados como admin (salta un IllegalArgumentException)
	 * 8. Intentamos publicar el newspaper6 sin estar logeado (salta un IllegalArgumentException)
	 * 
	 * Requisitos:
	 * C.6.2: An actor who is authenticated as a user must be able to publish a newspaper that he or she is created. Note that no newspaper can be published
	 * until each of the articles of which it is composed is saved in final mode
	 */

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
				this.templatePublish((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Test
	 * 1.Logeados como user1 ponemos como privado el newspaper1 (no salta excepción)
	 * 2.Logeados como user2 ponemos como público el newspaper2 (no salta excepción)
	 * 3.Logeados como user2 ponemos como privado el newspaper1 (salta IllegalArgumentException)
	 * 4.Logeados como customer1 ponemos como privado el newspaper1 (salta un IllegalArgumentException)
	 * 5.Logeados como admin ponemos como privado el newspaper1 (salta un IllegalArgumentException)
	 * 6.Sin estar logeado intentamos poner como privado el newspaper1 (salta un IllegalArgumentException)
	 * 7.Logeados como user1 ponemos en público el newspaper1 (salta un IllegalArgumentException)
	 * 8.Logeados como user1 ponemos como público el newspaper2 (salta un IllegalArgumentException)
	 * 9.Logeados como customer1 ponemos como público el newspaper2 (salta un IllegalArgumentException)
	 * 10.Logeados como admin ponemos como público el newspaper2 (salta un IllegalArgumentException)
	 * 11.Sin estar logeado intentamos poner como público el newspaper2 (salta un IllegalArgumentException)
	 * 12.Logeados como user2 intentamos poner como privado el newspaper2 (salta un IllegalArgumenteException)
	 * 
	 * Requisitos:
	 * A.23.1: An actor who is authenticated as a user must be able to decide on whether a newspaper that he or she is created is public or private.
	 */
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
				this.templatePutPublicPrivate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (Boolean) testingData[i][4], (Class<?>) testingData[i][5]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Test
	 * 1. Logeados como user1 hacemos el findOneToDisplay con el newspaper1 (no salta excepción)
	 * 2. Logeados como user1 hacemos el findOneToDisplay con el newspaper2 (no salta excepción)
	 * 3. Logeados como user1 hacemos el findOneToDisplay con el newspaper3 (no salta excepción)
	 * 4. Logeados como user4 hacemos el findOneToDisplay con el newspaper4 (no salta excepción)
	 * 5. Logeados como customer1 hacemos el findOneToDisplay con el newspaper1 (no salta excepción)
	 * 6. Logeados como admin hacemos el findOneToDisplay con el newspaper4 (no salta excepción)
	 * 7. Sin estar logeados hacemos el findOneToDisplay con el newspaper1 (no salta excepción)
	 * 8. Logeados como user1 hacemos el findOne con el newspaper2 (no salta excepción)
	 * 9. Logeados como user1 hacemos el findOne de un newspaper con id cero (salta un IllegalArgumentException)
	 * 10.Logeados como user1 hacemos el findOneToDisplay con el newspaper4 (salta un IllegalArgumentException)
	 * 11.Logeados como customer1 hacemos el findOneToDisplay con el newspaper4 (salta un IllegalArgumentException)
	 * 12. Sin estar logeado hacemos el findOneToDisplay con el newspaper4 (salta un IllegalArgumentException)
	 * 13. Sin estar logeado hacemos el findOneToDisplay con el newspaper2 (salta un IllegalArgumentException)
	 * 14. Logeados como user1 hacemos el findOneToDisplay de un newspaper con id cero (salta un IllegalArgumentException)
	 */
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
					newspaperId = super.getEntityId(newspaperBean); //Si no podrías coger el newspaper se pilla directamente para simular hackeos
				else {
					newspaperIdAux = super.getEntityId(newspaperBean);
					for (int i = 1; i <= this.newspaperService.findByUserId(this.userService.findByUserAccountId(LoginService.getPrincipal().getId()).getId(), 1, 5).getTotalPages(); i++)
						for (final Newspaper n : this.newspaperService.findByUserId(this.userService.findByUserAccountId(LoginService.getPrincipal().getId()).getId(), i, 5))
							//Lo cogemos entre nuestros newspapers si eres el creador
							if (newspaperIdAux == n.getId()) {
								newspaperId = n.getId();
								break;
							}
				}

			} else
				newspaperId = super.getEntityId(newspaperBean); //Si no estás logeados o no eres un user entonces pilla la id directamente para simular hackeos.

			this.newspaperService.publish(newspaperId); //Publicamos el newspaper

			Assert.isTrue(this.newspaperService.findOne(newspaperId).getIsPublished() == true && this.newspaperService.findOne(newspaperId).getPublicationDate().compareTo(new Date()) <= 0); //Vemos que esté publicado
			super.flushTransaction();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
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
					newspaperId = super.getEntityId(newspaperBean); // Si los newspapers no son tuyos pillas la id directamente
				else {
					newspaperIdAux = super.getEntityId(newspaperBean);
					for (int i = 1; i <= this.newspaperService.findByUserId(this.userService.findByUserAccountId(LoginService.getPrincipal().getId()).getId(), 1, 5).getTotalPages(); i++)
						for (final Newspaper n : this.newspaperService.findByUserId(this.userService.findByUserAccountId(LoginService.getPrincipal().getId()).getId(), i, 5))
							//Cogemos el newspaper entre los nuestros
							if (newspaperIdAux == n.getId()) {
								newspaperId = n.getId();
								break;
							}
				}

			} else
				newspaperId = super.getEntityId(newspaperBean); //Si no estás logeado o no eres user coges la id directamente

			if (isPrivate == true)
				this.newspaperService.putPublic(newspaperId); //Lo ponemos público
			else
				this.newspaperService.putPrivate(newspaperId); // Lo ponemos privado

			if (isPrivate == true)
				Assert.isTrue(this.newspaperService.findOne(newspaperId).getIsPrivate() == false); //Si lo ponemos público vemos que este sea público después
			else
				Assert.isTrue(this.newspaperService.findOne(newspaperId).getIsPrivate() == true);  //Si lo ponemos privado vemos que este sea privado después
			super.flushTransaction();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
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
						//Si estás logeados coges la id desde el findAll
						if (newspaperIdAux == n.getId())
							newspaperId = n.getId();
			} else if (correctNewspaper == true) {
				newspaperIdAux = super.getEntityId(newspaperBean);
				for (int i = 1; i <= this.newspaperService.findPublicsAndPublicated(1, 5).getTotalPages(); i++)
					//Si no pero es visible lo pillas desde el findAll para los no logeados
					for (final Newspaper n : this.newspaperService.findPublicsAndPublicated(i, 5).getContent())
						if (newspaperIdAux == n.getId())
							newspaperId = n.getId();
			} else
				newspaperId = super.getEntityId(newspaperBean); // Si no cogemos la id directamente para simular hackeos

			if (findOneToDisplay == true)
				if (falseId == false)
					newspaper = this.newspaperService.findOneToDisplay(newspaperId); //Se prueba el findOneToDisplay
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
