
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.Groupon;

public interface GrouponRepository extends JpaRepository<Groupon, Integer> {

	@Query("select g from Groupon g where g.maxDate > CURRENT_TIMESTAMP")
	Page<Groupon> findWithMaxDateFuture(Pageable pageable);

	@Query("select g from Groupon g")
	Page<Groupon> findAllPaginated(Pageable pageable);

	@Query("select g from Groupon g where g.creator.id = ?1")
	Page<Groupon> findByCreatorId(int creatorId, Pageable pageable);

	@Query("select p.groupon from Participation p where p.user.id = ?1")
	Page<Groupon> findByParticipantId(int participantId, Pageable pageable);

}
