
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Bargain;
import domain.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

	@Query("select t from Tag t where ?1 MEMBER OF t.bargains")
	Collection<Tag> findByBargain(final Bargain bargain);

	@Query("select t from Tag t where ?1 = t.name")
	Tag findByName(final String name);

}
