package usecases;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.User;
import services.UserService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BanUnbanUserTest extends AbstractTest {

	// System under test ------------------------------------------------------
	
	@Autowired
	private UserService 		userService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 		1.
	 * 		2.
	 *
	 * Requisitos:
	 * 		
	 * 
	 */
	@Test
	public void driverPositiveTest() {		
		final Object testingData[][] = {
			{
				"moderator1", "user1", null
			}, {
				"moderator2", "user3", null
			}
		};
			
	for (int i = 0; i < testingData.length; i++)
		try {
			super.startTransaction();
			this.template((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		} finally {
			super.rollbackTransaction();
		}
	}
	
	/*
	 * Pruebas:
	 * 		1. 
	 * 		2. 
	 * 		3. 
	 * 		4.
	 * 		5.
	 * 		6.
	 * 		7.
	 * 		8.
	 * 		9.
	 * 
	 * Requisitos:
	 * 		
	 * 
	 */
	@Test
	public void driverNegativeTest() {		
		final Object testingData[][] = {
				{
					"admin", "user1", IllegalArgumentException.class
				}, {
					"sponsor1", "user3", IllegalArgumentException.class
				}, {
					"company1", "user1", IllegalArgumentException.class
				}, {
					"user1", "user2", IllegalArgumentException.class
				}, {
					"moderator1", "sponsor1", IllegalArgumentException.class
				}, {
					"moderator1", "company1", IllegalArgumentException.class
				}
			};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	/*
	 * Banear/Desbanear a un usuario
	 * Pasos:
	 * 		1. Autenticar como moderador
	 * 		2. Listar usuarios
	 * 		3. Banear/desbanear usuario
	 * 		4. Volver al listado de usuarios
	 */
	protected void template(final String moderatorBean, final String userBean, final Class<?> expected) {
		Class<?> caught;
		Integer userId, countUsers;
		User user, userChoosen, oldUser;
		Page<User> users;
		boolean oldIsEnabled;
		
		caught = null;
		userChoosen = null;
		try {
			
			// 1. Autenticar como moderador
			super.authenticate(moderatorBean);
			
			userId = super.getEntityId(userBean);
			Assert.notNull(userId);
			
			oldUser = this.userService.findOne(userId);
			Assert.notNull(oldUser);
			
			oldIsEnabled = oldUser.getUserAccount().isEnabled();
			
			// 2. Listar usuarios
			users = this.userService.findAllPaginated(1, 5);
			Assert.notNull(users);
			countUsers = users.getTotalPages();

			//Buscamos el que queremos modificar
			for (int i = 0; i < countUsers; i++) {

				users = this.userService.findAllPaginated(i + 1, 5);

				//Si estamos pidiendo una página mayor
				if (users.getContent().size() == 0) break;

				// Navegar hasta el usuario que queremos.
				for (final User newUser : users.getContent())
					if (newUser.equals(oldUser)) {
						userChoosen = newUser;
						break;
					}

				if (userChoosen != null) break;
			}

			//Ya tenemos el usuario
			Assert.notNull(userChoosen);
			
			// 3. Banear/desbanear usuario
			this.userService.ban(userChoosen);
			this.userService.flush();

			// 4. Volver al listado de usuarios - COMPROBACIÓN
			user = this.userService.findOne(userChoosen.getId());
			Assert.notNull(user);
			Assert.isTrue(oldIsEnabled != user.getUserAccount().isEnabled());
				
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		
		super.checkExceptions(expected, caught);
	}

}

