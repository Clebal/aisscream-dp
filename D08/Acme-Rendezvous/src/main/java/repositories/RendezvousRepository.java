
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Rendezvous;

@Repository
public interface RendezvousRepository extends JpaRepository<Rendezvous, Integer> {

	@Query("select r from Rendezvous r where r.creator.id=?1")
	Collection<Rendezvous> findByCreatorId(int cretorId);

	@Query("select r from Rendezvous r join r.linkerRendezvouses l where l.id=?1")
	Collection<Rendezvous> findByLinkerRendezvousId(int linkerRendezvousId);

	@Query("select r from Rendezvous r where r.termCondition.id=?1")
	Collection<Rendezvous> findByTermConditionId(int termConditionId);

	@Query("select avg(cast((select count(r.rendezvous) from Rsvp r where r.attendant.id=u.id) as float )),sqrt(sum((select count(r.rendezvous) from Rsvp r where r.attendant.id=u.id)*(select count(r.rendezvous) from Rsvp r where r.attendant.id=u.id))/(select count(u2) from User u2)-avg(cast((select count(r.rendezvous) from Rsvp r where r.attendant.id=u.id) as float ))*avg(cast((select count(r.rendezvous) from Rsvp r where r.attendant.id=u.id) as float ))) from User u")
	Double[] avgStandardDRsvpdRendezvouses();

	@Query("select r from Rendezvous r where cast((select count(r2) from Rendezvous r2 join r2.linkerRendezvouses l where l.id=r.id)as float)>(select avg(cast((select count(r2) from Rendezvous r2 join r2.linkerRendezvouses l where l.id=r3.id)as float))*1.1 from Rendezvous r3)")
	Collection<Rendezvous> rendezvousesLinkedMoreAvgPlus10Percentage();

}
