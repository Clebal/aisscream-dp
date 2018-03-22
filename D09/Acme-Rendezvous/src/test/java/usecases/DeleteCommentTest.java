
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
public class DeleteCommentTest extends AbstractTest {

	@Autowired
	private CommentService	commentService;

	@Autowired
	private RendezvousService rendezvousService;
	
	/*
	 * Pruebas:
	 * 		1. Un administrador trata de eliminar un comentario
	 * 		2. Un administrador trata de eliminar un comentario que tiene respuestas
	 * 		3. Un administrador trata de eliminar la respuesta de un comentario
	 * 
	 * 
	 */
	@Test
	public void driverPostiveTest() {
		final Object testingData[][] = {
			{
				"admin", "rendezvous1", "comment1", null, null
			}, {
				"admin", "rendezvous2", "comment2", null, null
			}, {
				"admin", "rendezvous2", "comment2", "comment3", null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
//				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	/*
	 * Pruebas:
	 * 		1. Un usuario trata de eliminar un comentario cuando no lo tiene permitido.
	 * 		2. Un manager trata de eliminar un comentario cuando no lo tiene permitido.
	 * 
	 * 
	 */
	@Test
	public void driverNegativeTest() {
		final Object testingData[][] = {
			{
				"user1", "rendezvous1", "comment1", null, IllegalArgumentException.class
			}, {
				"manager1", "rendezvous2", "comment2", null, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
//				System.out.println(i);
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	// Ancillary methods ------------------------------------------------------

	/*
	 * Eliminar un comment. Pasos:
	 * 1. Autenticar usuario
	 * 1. Listar rendezvous
	 * 2. Escoger un rendezvous 
	 * 3. Listar comentarios
	 * 4. Escoger comentario
	 * 5. Eliminar comentario
	 * ---- SI TIENE RESPUESTA ----
	 * 6. Listas respuestas
	 * 7. Escoger respuesta
	 * 8. Eliminar respuesta
	 */
	protected void template(final String user, final String rendezvousBean, final String commentBean, final String commentReplyBean, final Class<?> expected) {
		Class<?> caught;
		int rendezvousId, commentId, commentReplyId, pageComment, pageCommentReply;
		Comment comment, commentReply;
		Collection<Comment> comments, commentReplies;
		Collection<Rendezvous> rendezvouses;
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
			
			// 5. Entrar en el display del comentario
			for(Comment c: comments) if(c.getId() == commentId) comment = c;
			
			if(commentReplyBean == null) {
				this.commentService.delete(comment);
				//Comprobación
				Assert.isNull(this.commentService.findOne(commentId));
				Assert.isTrue(this.commentService.findByRepliedCommentId(commentId, 0, 99).size() == 0);
			}
			
			if(commentReplyBean != null) {
				
				/*******/
				commentReplyId = super.getEntityId(commentReplyBean);
				commentReply = this.commentService.findOneToDisplay(commentReplyId);
				/******/
				
				// 6. Listar respuestas
				pageCommentReply = this.getPageReplyComment(comment, commentReply);
				Assert.notNull(pageCommentReply);
				commentReplies = this.commentService.findByRepliedCommentId(commentId, pageCommentReply, 5);
				
				// 7. Escoger respuesta
				for(Comment c: commentReplies) if(c.getId() == commentReplyId) commentReply = c;
				
				// 8. Eliminar respuesta
				this.commentService.delete(commentReply);
				
				// Comprobación
				Assert.isNull(this.commentService.findOne(commentReplyId));
				Assert.isTrue(this.commentService.findByRepliedCommentId(commentReplyId, 0, 99).size() == 0);
				Assert.notNull(this.commentService.findOne(commentId));
			}

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
//		System.out.println("Expected " + expected);
//		System.out.println("Caught " + caught);
		super.unauthenticate();
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
