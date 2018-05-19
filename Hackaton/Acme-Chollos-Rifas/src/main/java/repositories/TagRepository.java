
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

	@Query("select t from Tag t join t.bargains b where ?1 IN b.id order by t.name")
	Collection<Tag> findByBargainId(final int bargainId);

	@Query("select t from Tag t where t.name = ?1")
	Tag findByName(final String name);

	@Query("select t.name from Tag t join t.bargains b where ?1 IN b.id order by t.name")
	Collection<String> findNames(final int bargainId);

	@Query("select t from Tag t")
	Page<Tag> findAllPaginated(Pageable pageable);

}
