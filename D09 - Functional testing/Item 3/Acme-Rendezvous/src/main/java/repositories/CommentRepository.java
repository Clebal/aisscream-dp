
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

	@Query("select c from Comment c where c.user.id=?1")
	Collection<Comment> findByUserId(int userId);

	@Query("select c from Comment c where c.repliedComment is null and c.rendezvous.id=?1")
	Page<Comment> findByRendezvousIdAndNoRepliedComment(int rendezvousId, Pageable pageable);

	@Query("select c from Comment c where c.repliedComment.id=?1")
	Page<Comment> findByRepliedCommentId(int commentId, Pageable pageable);

	@Query("select count(c) from Comment c where c.repliedComment.id=?1")
	Integer countByRepliedCommentId(int commentId);

	@Query("select count(c) from Comment c where c.repliedComment is null and c.rendezvous.id=?1")
	Integer countByRendezvousIdAndNoRepliedComment(int rendezvousId);

	@Query("select avg(cast((select count(c2) from Comment c2 where c2.repliedComment.id=c.id) as float )), sqrt(sum((select count(c2) from Comment c2 where c2.repliedComment.id=c.id)*(select count(c2) from Comment c2 where c2.repliedComment.id=c.id))/(select count(c3) from Comment c3)-avg(cast((select count(c2) from Comment c2 where c2.repliedComment.id=c.id) as float ))*avg(cast((select count(c2) from Comment c2 where c2.repliedComment.id=c.id) as float ))) from Comment c")
	Double[] avgStandardRepliesPerComment();
}
