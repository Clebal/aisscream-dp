
package usecases;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.CommentService;
import services.RendezvousService;
import utilities.AbstractTest;
import domain.Comment;
import domain.Rendezvous;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ListCommentTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private CommentService		commentService;
	
	@Autowired
	private RendezvousService	rendezvousService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 		1. Un usuario autenticado como USER entra en la vista de detalles de comment
	 * 		2. Un usuario sin autenticar entra en la vista de detalles de comment
	 * 		3. Un usuario autenticado como USER entra en la vista de detalles de la respuesta a un comment
	 * 		4. Un usuario autenticado como ADMIN entra en la vista de detalles de la respuesta a un comment
	 * 		5. Un usuario autenticado como manager1 entra en la vista de detalles de la respuesta a un commen
	 * 	
	 * Caso de uso en el que se comprueba los casos en los que el usuario trata de mostrar
	 * tanto los comentarios de un rendezvous como sus respuestas, navegando a través de la web.
	 */
	@Test
	public void driverPositiveTest() {
		final Object testingData[][] = {
			{
				"user1", "rendezvous1", "comment1", 1, null, null, null
			}, {
				null, "rendezvous1", "comment1", 1, null, null, null
			}, {
				"user1", "rendezvous2", "comment2", 1, "comment3", 1, null
			}, {
				"admin", "rendezvous2", "comment2", 1, "comment3", 1, null
			}, {
				"manager1", "rendezvous2", "comment2", 1, "comment3", 1, null
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	/*
	 * Pruebas:
	 * 		1. Un usuario autenticado como USER trata de mostrar un comentario entrando desde un rendezvous distinto
	 * 		2. Un usuario autenticado como USER trata de mostrar una respuesta que no existe como tal, pues es un comentario.
	 * 		3. Un usuario autenticado como MANAGER trata de mostrar una respuesta que no existe como tal, pues es un comentario.
	 * 		4. Un usuario autenticado como ADMIN trata de mostrar una respuesta que no existe como tal, pues es un comentario.
	 * 
	 * Caso de uso en el que se comprueban las distintas excepciones que pueden saltar si se trata
	 * de navegar de una forma incorrecta a la hora de mostrar los comentarios y sus respuestas.
	 */
	@Test
	public void driverNegativeTest() {
		final Object testingData[][] = {
			{
				"user1", "rendezvous2", "comment5", 1, null, null, IllegalArgumentException.class
			}, {
				"user1", "rendezvous2", "comment2", 1, "comment1", null, IllegalArgumentException.class
			}, {
				"manager1", "rendezvous2", "comment2", 1, "comment1", null, IllegalArgumentException.class
			}, {
				"admin", "rendezvous2", "comment2", 1, "comment1", null, IllegalArgumentException.class
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	/*
	 * Pruebas:
	 * 		1. Un usuario sin autenticar trata de mostrar un comentario de un rendezvous no público (+18)
	 * 		2. Un usuario autenticado como USER trata de mostrar un comentario de un rendezvous no público (+18)
	 * 		3. Un usuario autenticado como MANAGER trata de mostrar un comentario de un rendezvous no público (+18)
	 * 
	 * Caso de uso en el que se simula como el usuario podría tratar de acceder a una sin usar la navegación
	 * sino utilizando la URL directamente.
	 */
	@Test
	public void driverUrlNegativeTest() {
		final Object testingData[][] = {
			{
				null, "comment2", IllegalArgumentException.class
			}, {
				"user4", "comment2", IllegalArgumentException.class
			}, {
				"manager3", "comment2", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateUrl((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
		
	// Ancillary methods ------------------------------------------------------

	/*
	 * Listar comment y su respuesta si la hubiera. Pasos:
	 * 1. Autenticar usuario
	 * 2. Listar rendezvous
	 * 3. Escoger rendezvous
	 * 4. Listar comentarios
	 * 5. Entrar en el display del comentario
	 * ---- SI TIENE RESPUESTA ----
	 * 6. Listar respuestas
	 * 7. Entrar en el display de la respuesta
	 */
	protected void template(final String user, final String rendezvousBean, final String commentBean, final Integer tamano, final String commentReplyBean, final Integer tamanoReplies, final Class<?> expected) {
		Class<?> caught;
		int rendezvousId, commentReplyId, commentId;
		Integer pageComment, pageCommentReply;
		Comment commentReply, comment;
		Collection<Rendezvous> rendezvouses;
		Collection<Comment> comments, commentReplies;
		Rendezvous rendezvous;

		caught = null;
		try {
			
			// 1. Autenticar usuario
			super.authenticate(user);
			
			/*******/
			rendezvousId = super.getEntityId(rendezvousBean);
			rendezvous = this.rendezvousService.findOne(rendezvousId);
			
			commentId = super.getEntityId(commentBean);
			comment = this.commentService.findOneToDisplay(commentId);
			/*******/

			// 2. Listar rendezvous
			if(user != null) rendezvouses = this.rendezvousService.findAllPaginated(this.getPageFindAllPaginated(rendezvous), 5); 
			else rendezvouses = this.rendezvousService.findAllPublics(this.getPageFindAllPublics(rendezvous), 5);
			Assert.notNull(rendezvouses);
			
			// 3. Escoger rendezvous
			for(Rendezvous r: rendezvouses) if(r.getId() == rendezvousId) rendezvous = r;
			Assert.notNull(rendezvous);
			
			// 4. Listar comentarios
			pageComment = this.getPageComment(rendezvous, comment);
			Assert.notNull(pageComment);
			comments = this.commentService.findByRendezvousIdAndNoRepliedComment(rendezvous.getId(), pageComment, 5);
			Assert.notNull(comments);
			
			// Comprobación
			Assert.isTrue(comments.size() == tamano);
			
			// 5. Entrar en el display del comentario
			for(Comment c: comments) if(c.getId() == commentId) comment = c;
			
			if(commentReplyBean != null) {
				
				/*******/
				commentReplyId = super.getEntityId(commentReplyBean);
				commentReply = this.commentService.findOneToDisplay(commentReplyId);
				/******/
				
				// 6. Listar respuestas
				pageCommentReply = this.getPageReplyComment(comment, commentReply);
				Assert.notNull(pageCommentReply);
				commentReplies = this.commentService.findByRepliedCommentId(commentId, pageCommentReply, 5);
				
				// 7. Entrar en el display de la respuesta
				for(Comment c: commentReplies) if(c.getId() == commentReplyId) commentReply = c;
				
				// Comprobación
				Assert.isTrue(commentReplies.size() == tamanoReplies);
				Assert.isTrue(commentReply.getRepliedComment().equals(comment));
			}
			
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
	
	protected void templateUrl(final String user, final String commentBean, final Class<?> expected) {
		Class<?> caught;
		int commentId;

		caught = null;
		try {
			
			// 1. Autenticar usuario
			super.authenticate(user);

			// 2. Entrar en el display de comment	
			commentId = super.getEntityId(commentBean);
			this.commentService.findOneToDisplay(commentId);	
			
			super.unauthenticate();
			
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	private Integer getPageFindAllPaginated(final Rendezvous rendezvous) {
		Integer result, collectionSize, pageNumber;
		Collection<Rendezvous> rendezvouses;

		collectionSize = this.rendezvousService.countAllPaginated();
		pageNumber = (int) Math.floor(((collectionSize / (5.0)) - 0.1) + 1);

		result = null;

		for (int i = 1; i <= pageNumber; i++) {
			rendezvouses = this.rendezvousService.findAllPaginated(i, 5);
			if (rendezvouses.contains(rendezvous)){
				result = i;
				break;
			}
		}

		return result;
	}
	
	private Integer getPageFindAllPublics(final Rendezvous rendezvous) {
		Integer result, collectionSize, pageNumber;
		Collection<Rendezvous> rendezvouses;

		collectionSize = this.rendezvousService.countAllPublics();
		pageNumber = (int) Math.floor(((collectionSize / (5.0)) - 0.1) + 1);

		result = null;

		for (int i = 1; i <= pageNumber; i++) {
			rendezvouses = this.rendezvousService.findAllPublics(i, 5);
			if (rendezvouses.contains(rendezvous)){
				result = i;
				break;
			}
		}

		return result;
	}
	
	private Integer getPageComment(final Rendezvous rendezvous, final Comment comment) {
		Integer result, collectionSize, pageNumber;
		Collection<Comment> comments;

		collectionSize = this.commentService.countByRendezvousIdAndNoRepliedComment(rendezvous.getId());
		pageNumber = (int) Math.floor(((collectionSize / (5.0)) - 0.1) + 1);
				
		result = null;

		for (int i = 1; i <= pageNumber; i++) {
			comments = this.commentService.findByRendezvousIdAndNoRepliedComment(rendezvous.getId(), i, 5);
			if (comments.contains(comment)){
				result = i;
				break;
			}
		}

		return result;
	}
	
	private Integer getPageReplyComment(final Comment comment, final Comment commentReply) {
		Integer result, collectionSize, pageNumber;
		Collection<Comment> comments;

		collectionSize = this.commentService.countByRepliedCommentId(comment.getId());
		pageNumber = (int) Math.floor(((collectionSize / (5.0)) - 0.1) + 1);
				
		result = null;

		for (int i = 1; i <= pageNumber; i++) {
			comments = this.commentService.findByRepliedCommentId(comment.getId(), i, 5);
			if (comments.contains(commentReply)){
				result = i;
				break;
			}
		}

		return result;
	}
	
}
