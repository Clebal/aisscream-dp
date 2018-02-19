package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.RSVP;

@Repository
public interface RSVPRepository extends JpaRepository<RSVP, Integer>{

	@Query("select r from RSVP r where r.attendant.id = ?1")
	Collection<RSVP> findByAttendandUserId(final int userId);
	
	@Query("select r from RSVP r where r.rendezvous.id = ?1")
	Collection<RSVP> findByRendezvousId(final int rendezvousId);
	
	@Query("select r from RSVP r where r.rendezvous.creator.id = ?1")
	Collection<RSVP> findByRendezvousCreatorId(final int userId);
	
}
