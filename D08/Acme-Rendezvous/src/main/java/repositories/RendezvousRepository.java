
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Rendezvous;

@Repository
public interface RendezvousRepository extends JpaRepository<Rendezvous, Integer> {

	@Query("select r from Rendezvous r where r.creator.id=?1")
	Collection<Rendezvous> findByUserId(int userId);

	@Query("select r from Rendezvous r join r.linkerRendezvouses l where l.id=?1")
	Collection<Rendezvous> findByLinkerRendezvousId(int linkerRendezvousId);

	@Query("select r from Rendezvous r where r.termCondition.id=?1")
	Collection<Rendezvous> findByTermConditionId(int termConditionId);

}
