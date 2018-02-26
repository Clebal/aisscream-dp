package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Rsvp;

@Repository
public interface RsvpRepository extends JpaRepository<Rsvp, Integer>{
	
	@Query("select r from Rsvp r where r.attendant.userAccount.id = ?1")
	Page<Rsvp> findByAttendantUserAccountId(final int userAccountId, final Pageable pageable);
	
	@Query("select count(r) from Rsvp r where r.attendant.userAccount.id = ?1")
	Integer countByAttendantUserAccountId(final int userAccountId);
	
	@Query("select r from Rsvp r where r.rendezvous.id = ?1")
	Page<Rsvp> findByRendezvousId(final int rendezvousId, Pageable pageable);
	
	@Query("select count(r) from Rsvp r where r.rendezvous.id = ?1")
	Integer countByRendezvousId(final int rendezvousId);
	
	@Query("select r from Rsvp r where r.rendezvous.creator.userAccount.id = ?1")
	Collection<Rsvp> findByCreatorUserAccountId(final int userAccountId);
	
	@Query("select r from Rsvp r where r.attendant.id = ?1 and r.rendezvous.id = ?2")
	Rsvp findByAttendantUserIdAndRendezvousId(final int userId, final int rendezvousId);
	
}
