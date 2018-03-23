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

import domain.Comment;
import domain.Rendezvous;
import domain.User;

import services.CommentService;
import services.RendezvousService;
import services.UserService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SaveCommentTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private CommentService		commentService;
	
	@Autowired
	private RendezvousService		rendezvousService;
	
	@Autowired
	private UserService		userService;
	
	// Tests ------------------------------------------------------------------

	/*
	 * Pruebas:
	 * 		1. Creamos un comment con todos los parámetros correctos
	 * 		2. Creamos un comment sin introducir el campo picture
	 */
	@Test
	public void driverPositiveTest() {
		final Object testingData[][] = {
			{
				"user1", "rendezvous1", "01/02/2018 12:30", "Test", "http://example.com/image.jpg", null
			}, {
				"user1", "rendezvous1", "01/02/2018 12:30", "Test", "", null
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
	 * 		1. Creamos una respuesta con todos los parámetros correctos
	 * 		2. Creamos una respuesta sin introducir el campo picture
	 */
	@Test
	public void driverReplyCommentPositiveTest() {
		final Object testingData[][] = {
			{
				"user1", "rendezvous1", "01/02/2018 12:30", "Test", "http://example.com/image.jpg", "comment1", null
			}, {
				"user1", "rendezvous1", "01/02/2018 12:30", "Test", "", "comment1", null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateReplyComment((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	/*
	 * Pruebas:
	 * 		1. Tratamos de crear un comentario pero le ponemos de usuario uno distinto al que está autenticado
	 */
	@Test
	public void driverUrlNegativeTest() {
		final Object testingData[][] = {
			{
				"user1", "rendezvous1", "user3", "01/02/2018 12:30", "Test", "http://example.com/image.jpg", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateUrl((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	
	/*
	 * Pruebas:
	 * 		1. Un usuario trata de crear un comentario dejando el campo moment vacío
	 * 		2. Un usuario trata de crear un comentario eliminando el campo moment
	 * 		3. Un usuario trata de crear un comentario introduciendo en el campo picture un valor que no corresponde con el pattern URL
	 * 		4. Un usuario trata de crear un comentario en un rendezvous para el cual no tiene rsvp.
	 * 		5. Un usuario trata de crear un comentario eliminando el campo text.
	 * 		6. Un usuario trata de crear un comentario para un rendezvous sin introducir nada en el campo text.
	 * 		7. Un manager trata de crear un comentario pero no lo tienen permitido.
	 * 		8. Un administrador trata de crear un comentario pero no lo tiene permitido.
	 */
	@Test
	public void driverNegativeTest() {
		final Object testingData[][] = {
			{
				"user1", "rendezvous1", "", "Test", "", IllegalArgumentException.class
			}, 	{
				"user1", "rendezvous1", null, "Test", "", IllegalArgumentException.class
			}, {
				"user1", "rendezvous1", "01/01/2018 00:00", "Test", "asdf", ConstraintViolationException.class
			}, 	{
				"user6", "rendezvous2", "01/01/2018 00:00", "Test", "", IllegalArgumentException.class
			}, 	{
				"user1", "rendezvous1", "01/01/2018 00:00", null, "", ConstraintViolationException.class
			}, {
				"user1", "rendezvous1", "01/01/2018 00:00", "", "http://example.com/image.jpg", ConstraintViolationException.class
			}, {
				"manager1", "rendezvous1", "01/01/2018 00:00", "Test", "http://example.com/image.jpg", IllegalArgumentException.class
			}, {
				"admin", "rendezvous1", "01/01/2018 00:00", "Test", "http://example.com/image.jpg", IllegalArgumentException.class
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
	 * 		1. Un usuario trata de crear una respuesta con el campo moment vacío.
	 * 		2. Un usuario trata de crear una respuesta eliminando el campo moment.
	 * 		3. Un usuario trata de crea una respuesta introduciendo en el campo picture un valor que no corresponde con el pattern URL.
	 * 		4. Un usuario trata de crea una respuesta en un rendezvous para el cual no tiene rsvp ni es el creator.
	 * 		5. Un usuario trata de crear una respuesta eliminando el campo title.
	 * 		6. Un usuario trata de crear una respuesta para un rendezvous sin introducir nada en el campo text.
	 * 		7. Un manager trata de crear una respuesta pero no lo tienen permitido.
	 * 		8. Un administrador trata de crear una respuesta pero no lo tiene permitido.
	 */
	@Test
	public void driverReplyCommentNegativeTest() {
		final Object testingData[][] = {
			{
				"user1", "rendezvous1", "", "Test", "", "comment1", IllegalArgumentException.class
			}, {
				"user1", "rendezvous1", null, "Test", "", "comment1", IllegalArgumentException.class
			}, {
				"user1", "rendezvous1", "01/01/2018 00:00", "Test", "asdf", "comment1", ConstraintViolationException.class
			}, {
				"user6", "rendezvous2", "01/01/2018 00:00", "Test", "", "comment2", IllegalArgumentException.class
			}, {
				"user1", "rendezvous1", "01/01/2018 00:00", null, "", "comment1", ConstraintViolationException.class
			}, {
				"user1", "rendezvous1", "01/01/2018 00:00", "", "http://example.com/image.jpg", "comment1", ConstraintViolationException.class
			}, {
				"manager1", "rendezvous1", "01/01/2018 00:00", "Test", "http://example.com/image.jpg", "comment1", IllegalArgumentException.class
			}, {
				"admin", "rendezvous1", "01/01/2018 00:00", "Test", "http://example.com/image.jpg", "comment1", IllegalArgumentException.class
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateReplyComment((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	/*
	 * Pruebas:
	 * 		1. Tratamos de crear una respuesta a un comentario pero le ponemos de usuario uno distinto al que está autenticado
	 */
	public void driverUrlReplyCommentNegativeTest() {
		final Object testingData[][] = {
			{
				"user1", "rendezvous1", "user3", "01/02/2018 12:30", "Test", "http://example.com/image.jpg", IllegalArgumentException.class
			}
		};
		
		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateUrlReplyComment((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (Class<?>) testingData[i][7]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	
	// Ancillary methods ------------------------------------------------------

	/*
	 * Crear un comment. Pasos:
	 * 1. Autenticar usuario
	 * 2. Listar los rendezvous
	 * 3. Escoger un rendezvous
	 * 4. Crear un comment asociado a un rendezvous
	 * 5. Guardar el nuevo comment
	 * 6. Dirigir al display de rendezvous
	 */
	protected void template(final String user, final String rendezvousBean, final String moment, final String text, final String picture, final Class<?> expected) {
		Class<?> caught;
		Comment savedComment, newComment;
		Rendezvous rendezvous;
		Collection<Rendezvous> rendezvouses;
		int rendezvousId;
		DateFormat formatter;
		DataBinder binder;

		formatter = new SimpleDateFormat("dd/MM/yy HH:mm");

		caught = null;
		try {
			
			rendezvousId = super.getEntityId(rendezvousBean);
			rendezvous = this.rendezvousService.findOne(rendezvousId);
			
			// 1. Autenticar usuario
			super.authenticate(user);
			
			// 2. Listar los rendezvous
			rendezvouses = this.rendezvousService.findAllPaginated(this.getPage(rendezvous), 5);
			
			// 3. Escoger un rendezvous
			for(Rendezvous r: rendezvouses)
				if(r.getId() == rendezvousId) rendezvous = r;
			
			// 4. Crear un comment asociado a un rendezvous
			newComment = this.commentService.create(rendezvous, null);
			if(moment == null || moment.equals("")){ 
				Assert.notNull(moment);
				Assert.isTrue(!moment.equals("")); 
			} else newComment.setMoment(formatter.parse(moment));
			newComment.setText(text);
			if(picture != null) newComment.setPicture(picture);
			
			// 5. Guardar el nuevo comment
			binder = new DataBinder(newComment);
			newComment = this.commentService.reconstruct(newComment, binder.getBindingResult());
			savedComment = this.commentService.save(newComment);
			this.commentService.flush();
			
			// 6. Dirigir al display de rendezvous
			Assert.isTrue(this.commentService.findByRendezvousIdAndNoRepliedComment(rendezvous.getId(), this.getPageRepliedComment(rendezvous, savedComment), 5).contains(savedComment));
			
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		
		super.checkExceptions(expected, caught);
	}
	
	/*
	 * Crear un comment a través de URL con Postman. Pasos:
	 * 1. Autenticar usuario
	 * 2. Enviar datos de comment por URL
	 */
	protected void templateUrl(final String user, final String rendezvousBean, final String userBean, final String moment, final String text, final String picture, final Class<?> expected) {
		Class<?> caught;
		Comment savedComment, newComment;
		Rendezvous rendezvous;
		int rendezvousId, userId;
		DateFormat formatter;
		User userComment;

		formatter = new SimpleDateFormat("dd/MM/yy HH:mm");

		caught = null;
		try {
			
			rendezvousId = super.getEntityId(rendezvousBean);
			rendezvous = this.rendezvousService.findOne(rendezvousId);
			
			// 1. Autenticar usuario
			super.authenticate(user);
			
			// 2. Enviar datos de comment por URL
			newComment = this.commentService.create(rendezvous, null);
			if(moment == null || moment.equals("")){ 
				Assert.notNull(moment);
				Assert.isTrue(!moment.equals("")); 
			} else newComment.setMoment(formatter.parse(moment));
			newComment.setText(text);
			if(picture != null) newComment.setPicture(picture);
			if(userBean != null) {
				userId = super.getEntityId(userBean);
				userComment = this.userService.findOne(userId);
				newComment.setUser(userComment);
			}
			
			savedComment = this.commentService.save(newComment);
			this.commentService.flush();

			// Comprobación
			Assert.isTrue(this.commentService.findByRendezvousIdAndNoRepliedComment(rendezvous.getId(), this.getPageRepliedComment(rendezvous, savedComment), 5).contains(savedComment));
			
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		
		super.checkExceptions(expected, caught);
	}

	/*
	 * Crear una respuesta a un comment. Pasos:
	 * 1. Autenticar como usuario.
	 * 2. Listar los rendezvouses
	 * 3. Escoger un rendezvous
	 * 4. Escoger un comentario
	 * 5. Crear un respuesta a un comment
	 * 6. Salvar el nuevo comment
	 * 7. Dirigir al display del comentario padre
	 */
	protected void templateReplyComment(final String user, final String rendezvousBean, final String moment, final String text, final String picture, final String repliedCommentBean, final Class<?> expected) {
		Class<?> caught;
		Comment savedComment, newComment, repliedComment;
		Rendezvous rendezvous;
		Collection<Rendezvous> rendezvouses;
		Collection<Comment> comments;
		int rendezvousId, repliedCommentId;
		DateFormat formatter;
		DataBinder binder;

		formatter = new SimpleDateFormat("dd/MM/yy HH:mm");

		caught = null;
		try {
			
			rendezvousId = super.getEntityId(rendezvousBean);
			rendezvous = this.rendezvousService.findOne(rendezvousId);
			
			repliedCommentId = super.getEntityId(repliedCommentBean);
			repliedComment = this.commentService.findOne(repliedCommentId);
			
			// 1. Autenticar como usuario
			super.authenticate(user);
			
			// 2. Listar los rendezvouses
			rendezvouses = this.rendezvousService.findAllPaginated(this.getPage(rendezvous), 5);
			
			// 3. Escoger un rendezvous
			for(Rendezvous r: rendezvouses)
				if(r.getId() == rendezvousId) rendezvous = r;
			
			// 4. Escoger un comentario
			comments = this.commentService.findByRendezvousIdAndNoRepliedComment(rendezvous.getId(), this.getPageRepliedComment(rendezvous, repliedComment), 5);
			for(Comment c: comments)
				if(c.getId() == repliedCommentId) repliedComment = c;
			
			// 5. Crear un respuesta a un comment
			newComment = this.commentService.create(rendezvous, repliedComment);
			if(moment == null || moment.equals("")){ 
				Assert.notNull(moment);
				Assert.isTrue(!moment.equals("")); 
			} else newComment.setMoment(formatter.parse(moment));
			newComment.setText(text);
			if(picture != null) newComment.setPicture(picture);
			
			// 6. Salvar el nuevo comment
			binder = new DataBinder(newComment);
			newComment = this.commentService.reconstruct(newComment, binder.getBindingResult());
			savedComment = this.commentService.save(newComment);
			this.commentService.flush();
			
			// 7. Dirigir al display del comentario padre
			Assert.notNull(this.commentService.findOneToDisplay(savedComment.getRepliedComment().getId()));
			
			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		
		super.checkExceptions(expected, caught);
	}
	
	/*
	 * Crear una respuesta a un comment a través de la URL con Postman. Pasos:
	 * 1. Autenticar usuario
	 * 2. Enviar datos de comment por URL
	 */
	protected void templateUrlReplyComment(final String user, final String rendezvousBean, final String userBean, final String moment, final String text, final String picture, final String repliedCommentBean, final Class<?> expected) {
		Class<?> caught;
		Comment savedComment, newComment, repliedComment;
		Rendezvous rendezvous;
		int rendezvousId, repliedCommentId, userId;
		DateFormat formatter;
		User userComment;
		DataBinder binder;

		formatter = new SimpleDateFormat("dd/MM/yy HH:mm");

		caught = null;
		try {
			
			rendezvousId = super.getEntityId(rendezvousBean);
			rendezvous = this.rendezvousService.findOne(rendezvousId);
			
			repliedCommentId = super.getEntityId(repliedCommentBean);
			repliedComment = this.commentService.findOne(repliedCommentId);
			
			// 1. Autenticar usuario
			super.authenticate(user);
			
			// 2. Enviar datos de comment por URL
			newComment = this.commentService.create(rendezvous, repliedComment);
			if(moment == null || moment.equals("")){ 
				Assert.notNull(moment);
				Assert.isTrue(!moment.equals("")); 
			} else newComment.setMoment(formatter.parse(moment));
			newComment.setText(text);
			if(picture != null) newComment.setPicture(picture);
			if(userBean != null) {
				userId = super.getEntityId(userBean);
				userComment = this.userService.findOne(userId);
				newComment.setUser(userComment);
			}
			
			binder = new DataBinder(newComment);
			newComment = this.commentService.reconstruct(newComment, binder.getBindingResult());
			savedComment = this.commentService.save(newComment);
			this.commentService.flush();
			
			// Comprobación
			Assert.notNull(this.commentService.findOneToDisplay(savedComment.getRepliedComment().getId()));
			
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
	
	private Integer getPageRepliedComment(final Rendezvous rendezvous, final Comment comment) {
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
	
}

