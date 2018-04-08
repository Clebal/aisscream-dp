package usecases;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Article;
import domain.User;
import services.ArticleService;
import services.UserService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DeleteArticleTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private ArticleService		articleService;
	
	@Autowired
	private UserService				userService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 
	 * 	Primero se realizarán las pruebas desde un listado y luego
	 * como si accedemos a la entidad desde getEntityId:
	 * 1. Probando que el user2 borra la article2
	 * 	2. Probando que el user3 borra la article3
	 * 	3. Probando que el user1 borra la article3
	 * 
	 * Requisitos:
	 * 2. A user may create a newspaper, for which the system must store a title, a publication date
		(year, month, and day), a description, an optional picture, and the list of articles of which it is
		composed.
		3. For each article, the system must store a title, the moment when it is published, a summary,
		a piece of text (the body), and some optional pictures. An article is published when the corresponding
		newspaper is published.
	 *
	 */
	@Test
	public void positiveDeleteArticleTest() {
		final Object testingData[][] = {
			{
				"user2", "article2", null
			}, {
				"user3", "article3", null
			} , {
				"user1", "article1", null
			}
		};
			
	for (int i = 0; i < testingData.length; i++)
			try {
				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				System.out.println(oops.getMessage());
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	
	for (int i = 0; i < testingData.length; i++)
		try {
			super.startTransaction();
			this.templateNoList((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		} finally {
			super.rollbackTransaction();
		}
	}
	
	/*
	 * Pruebas:
	 * 
	 * Primero se realizarán las pruebas desde un listado y luego
	 * como si accedemos a la entidad desde getEntityId:
	 * 
	 * 1. No puede borrarlo un usuario no logueado
	 * 2. Solo puede borrarlo un user
	 * 3. Solo puede borrarlo un user
	 * 
	 * Requisitos:
	 * 2. A user may create a newspaper, for which the system must store a title, a publication date
		(year, month, and day), a description, an optional picture, and the list of articles of which it is
		composed.
		3. For each article, the system must store a title, the moment when it is published, a summary,
		a piece of text (the body), and some optional pictures. An article is published when the corresponding
		newspaper is published.
	 *
	 */
	 @Test()
	public void negativeDeleteArticleTest() {
		final Object testingData[][] = {
			{
				null, "article1", IllegalArgumentException.class 
			}, 	{
				"administrator", "article1", IllegalArgumentException.class
			}, {
				"customer2", "article2", IllegalArgumentException.class 
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
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateNoList((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

		/*
		 * 	Pasos:
		 * 
		 * 1. Nos autentificamos como user
		 * 2. Tomamos el id y la entidad de user
		 * 3. Accedemos a la lista de articles y tomamos la que nos interesa
		 * 4. Borramos el article
		 * 5. Nos desautentificamos
		 * 6. Comprobamos que no existe el articulo borrado
		 */
	protected void template(final String user, final String article, final Class<?> expected) {
		Class<?> caught;
		int userId, articleId;
		User userEntity;
		Article articleEntity;
		Collection<Article> articles;

		articleEntity = null;
		caught = null;
		try {
			super.authenticate(user);
			Assert.notNull(user);
			userId = super.getEntityId(user);
			userEntity = this.userService.findOne(userId);
			Assert.notNull(userEntity);
			articleId = super.getEntityId(article);
			Assert.notNull(articleId);
			articles = this.articleService.findByWritterId(userEntity.getId(), 1, 5).getContent();
			for (Article a : articles) {
				if(a.getId() == articleId){
					articleEntity = a;
					break;
				}
			}
			this.articleService.delete(articleEntity);
			super.unauthenticate();
			
			Assert.isTrue(!this.articleService.findAll().contains(articleEntity));

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	/*
	 * 	Pasos:
	 * 
	 * 1. Nos autentificamos como user
	 * 2. Tomamos el id y la entidad de article
	 * 3. Borramos el articulo
	 * 4. Nos desautentificamos
	 * 5. Comprobamos que no existe el articulo borrado
	 */
	protected void templateNoList(final String user, final String article, final Class<?> expected) {
		Class<?> caught;
		int userId;
		Article articleEntity = null;

		caught = null;
		try {
			super.authenticate(user);
			userId = super.getEntityId(article);
			Assert.notNull(userId);

			articleEntity = this.articleService.findOneToEdit(userId);
			this.articleService.delete(articleEntity);
			super.unauthenticate();
			
			Assert.isTrue(!this.articleService.findAll().contains(articleEntity));

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}

