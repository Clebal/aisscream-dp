
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
	
	@Query("select a from Article a where a.writer.id=?1")
	Page<Article> findAllUserPaginated(final int userId, final Pageable pageable);

	@Query("select a from Article a where a.writer.id=?1 and a.newspaper.id=?2")
	Page<Article> findAllNewspaperPaginated(final int userId, final int newspaperId, final Pageable pageable);
	
	@Query("select a from Article a where a.id = ?1")
	Page<Article> findByWritterId(final int userId, final Pageable pageable);

}
