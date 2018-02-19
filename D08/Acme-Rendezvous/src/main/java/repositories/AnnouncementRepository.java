package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Announcement;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {

	@Query("select a from Announcement a where a.rendezvous.id = ?1")
	Collection<Announcement> findByRendezvousId(final int rendezvousId);
	
	@Query("select a from Announcement a where a.rendezvous.creator.id = ?1")
	Collection<Announcement> findByCreatorUserId(final int userId);
	
	@Query("select a from Announcement a where a.rendezvous.creator.userAccount.id = ?1")
	Collection<Announcement> findByCreatorUserAccountId(final int userAccountId);
	
}
