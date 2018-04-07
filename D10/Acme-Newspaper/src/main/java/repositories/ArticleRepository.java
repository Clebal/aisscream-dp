
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Article;


@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
	
	@Query("select a from Article a where a.writer.id=?1 and a.newspaper.isPublished=true and a.isFinalMode=true")
	Page<Article> findAllUserPaginated(final int userId, final Pageable pageable);

	@Query("select a from Article a where a.writer.id=?1 and a.newspaper.id=?2 and a.newspaper.isPublished=true and a.isFinalMode=true")
	Page<Article> findAllNewspaperPaginated(final int userId, final int newspaperId, final Pageable pageable);
	
	@Query("select a from Article a where a.hasTaboo = true")
	Page<Article> findAllTabooPaginated(final Pageable pageable);

	@Query("select a from Article a where a.writer.id = ?1")
	Page<Article> findByWritterId(final int userId, final Pageable pageable);
	
	@Query("select a from Article a where a.isFinalMode=true")
	Page<Article> findAllPaginated(final Pageable pageable);

	@Query("select a from Article a where a.writer.id=?1 and a.newspaper.id=?2 and a.newspaper.isPublished=true and a.isFinalMode=true")
	Collection<Article> findByUserIdAndNewspaperId(final int userId, final int newspaperId);
	
	@Query("select a from Article a where a.newspaper.id=?1 and a.newspaper.isPublished=true and a.isFinalMode=true")
	Collection<Article> findByNewspaperId(final int newspaperId);
	
	@Query("select a from Article a where a.newspaper.id=?1 and a.newspaper.isPublished=true and a.isFinalMode=true")
	Page<Article> findByNewspaperIdPaginated(final int newspaperId, final Pageable pageable);
	
}
