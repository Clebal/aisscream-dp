
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.FollowUp;

@Repository
public interface FollowUpRepository extends JpaRepository<FollowUp, Integer> {

	@Query("select f from FollowUp f where f.user.id=?1")
	Page<FollowUp> findByUserIdPaginated(int userId, Pageable pageable);

	@Query("select f from FollowUp f where f.article.id=?1")
	Page<FollowUp> findByArticleIdPaginated(int articleId, Pageable pageable);

	@Query("select f from FollowUp f where f.article.id=?1")
	Collection<FollowUp> findByArticleId(int articleId);
}
