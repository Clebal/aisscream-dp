
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Announcement;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {

	@Query("select a from Announcement a where a.rendezvous.id = ?1 order by a.rendezvous.moment DESC")
	Page<Announcement> findByRendezvousId(final int rendezvousId, final Pageable pageable);

	@Query("select count(a) from Announcement a where a.rendezvous.id = ?1")
	Integer countByRendezvousId(final int rendezvousId);

	@Query("select a from Announcement a where a.rendezvous.creator.userAccount.id = ?1")
	Page<Announcement> findByCreatorUserAccountId(final int userAccountId, final Pageable pageable);

    @Query("select count(a) from Announcement a")
    Integer countAll();
    
    @Query("select a from Announcement a")
    Page<Announcement> findAll(final Pageable pageable);
	
	@Query("select count(a) from Announcement a where a.rendezvous.creator.userAccount.id = ?1")
	Integer countByCreatorUserAccountId(final int rendezvousId);

	@Query("select avg(cast((select count(a) from Announcement a where a.rendezvous.id=r.id)as int)),sqrt(sum((select count(a) from Announcement a where a.rendezvous.id=r.id)*(select count(a) from Announcement a where a.rendezvous.id=r.id))/(select count(r2) from Rendezvous r2)-avg(cast((select count(a) from Announcement a where a.rendezvous.id=r.id) as float ))*avg(cast((select count(a) from Announcement a where a.rendezvous.id=r.id) as float ))) from Rendezvous r")
	Double[] avgStandartDerivationAnnouncementPerRendezvous();

}
