package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Announcement;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {

	@Query("select a from Announcement a where a.rendezvous.id = ?1")
	Page<Announcement> findByRendezvousId(final int rendezvousId, final Pageable pageable);

	@Query("select count(a) from Announcement a where a.rendezvous.id = ?1")
	double countByRendezvousId(final int rendezvousId);

	@Query("select a from Announcement a where a.rendezvous.creator.userAccount.id = ?1")
	Page<Announcement> findByCreatorUserAccountId(final int userAccountId, final Pageable pageable);
	
	@Query("select count(a) from Announcement a where a.rendezvous.creator.userAccount.id = ?1")
	double countByCreatorUserAccountId(final int rendezvousId);
	
}
