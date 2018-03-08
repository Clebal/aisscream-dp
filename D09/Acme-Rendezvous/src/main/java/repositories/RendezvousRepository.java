
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Rendezvous;

@Repository
public interface RendezvousRepository extends JpaRepository<Rendezvous, Integer> {

	@Query("select r from Rendezvous r where r.creator.id=?1")
	Page<Rendezvous> findByCreatorId(int cretorId, Pageable pageable);

	@Query("select count(r) from Rendezvous r where r.creator.id=?1")
	Integer countByCreatorId(int creatorId);

	@Query("select r from Rendezvous r where r.creator.id=?1 and r.adultOnly=false")
	Page<Rendezvous> findByCreatorIdAllPublics(int cretorId, Pageable pageable);

	@Query("select count(r) from Rendezvous r where r.creator.id=?1 and r.adultOnly=false")
	Integer countByCreatorIdAllPublics(int creatorId);

	@Query("select r.rendezvous from Rsvp r where r.attendant.id=?1")
	Page<Rendezvous> findByAttendantId(int attendantId, Pageable pageable);

	@Query("select count(r.rendezvous) from Rsvp r where r.attendant.id=?1")
	Integer countByAttendantId(int attendantId);

	@Query("select r.rendezvous from Rsvp r where r.attendant.id=?1 and r.rendezvous.adultOnly=false")
	Page<Rendezvous> findByAttendantIdAllPublics(int attendantId, Pageable pageable);

	@Query("select count(r.rendezvous) from Rsvp r where r.attendant.id=?1 and (r.rendezvous.adultOnly=false or r.rendezvous.creator.id=?1)")
	Integer countByAttendantIdAllPublics(int attendantId);

	@Query("select r from Rendezvous r join r.linkerRendezvouses l where l.id=?1")
	Page<Rendezvous> findByLinkerRendezvousId(int linkerRendezvousId, Pageable pageable);

	@Query("select count(r) from Rendezvous r join r.linkerRendezvouses l where l.id=?1")
	Integer countByLinkerRendezvousId(int linkerRendezvousId);

	@Query("select r from Rendezvous r join r.linkerRendezvouses l where l.id=?1 and r.adultOnly=false")
	Page<Rendezvous> findByLinkerRendezvousIdAndAllPublics(int linkerRendezvousId, Pageable pageable);

	@Query("select count(r) from Rendezvous r join r.linkerRendezvouses l where l.id=?1 and r.adultOnly=false")
	Integer countByLinkerRendezvousIdAndAllPublics(int linkerRendezvousId);

	@Query("select l from Rendezvous r join r.linkerRendezvouses l where r.id=?1 and l.adultOnly=false")
	Page<Rendezvous> findLinkerRendezvousesAllPublicsByRendezvousId(int rendezvousId, Pageable pageable);

	@Query("select count(l) from Rendezvous r join r.linkerRendezvouses l where r.id=?1 and l.adultOnly=false")
	Integer countLinkerRendezvousesAllPublicsByRendezvousId(int rendezvousId);

	@Query("select l from Rendezvous r join r.linkerRendezvouses l where r.id=?1")
	Page<Rendezvous> findLinkerRendezvousesByRendezvousId(int rendezvousId, Pageable pageable);

	@Query("select count(l) from Rendezvous r join r.linkerRendezvouses l where r.id=?1")
	Integer countLinkerRendezvousesByRendezvousId(int rendezvousId);

	@Query("select r from Rendezvous r where r.adultOnly=false")
	Page<Rendezvous> findAllPublics(Pageable pageable);

	@Query("select count(r) from Rendezvous r where r.adultOnly=false")
	Integer countAllPublics();

	@Query("select r from Rendezvous r")
	Page<Rendezvous> findAllPaginated(Pageable pageable);

	@Query("select count(r) from Rendezvous r")
	Integer countAllPaginated();

	@Query("select r from Rendezvous r where ?1 not member of r.linkerRendezvouses and r!=?1 and r.isDeleted=false")
	Page<Rendezvous> findNotLinkedByRendezvous(Rendezvous rendezvous, Pageable pageable);

	@Query("select count(r) from Rendezvous r where ?1 not member of r.linkerRendezvouses and r!=?1 and r.isDeleted=false")
	Integer countNotLinkedByRendezvous(Rendezvous rendezvous);

	@Query("select r from Rendezvous r where ?1 not member of r.linkerRendezvouses and r!=?1 and r.isDeleted=false and r.adultOnly=false")
	Page<Rendezvous> findNotLinkedByRendezvousAllPublics(Rendezvous rendezvous, Pageable pageable);

	@Query("select count(r) from Rendezvous r where ?1 not member of r.linkerRendezvouses and r!=?1 and r.isDeleted=false and r.adultOnly=false")
	Integer countNotLinkedByRendezvousAllPublics(Rendezvous rendezvous);

	@Query("select avg(cast((select count(r) from Rendezvous r where r.creator.id=u.id) as float )), sqrt(sum((select count(r) from Rendezvous r where r.creator.id=u.id)*(select count(r) from Rendezvous r where r.creator.id=u.id))/(select count(u2) from User u2)-avg(cast((select count(r) from Rendezvous r where r.creator.id=u.id) as float ))*avg(cast((select count(r) from Rendezvous r where r.creator.id=u.id) as float ))) from User u")
	Double[] avgStandardDRsvpdCreatedPerUser();

	@Query("select cast((count(DISTINCT r.creator)) as float)/(-count(DISTINCT r.creator)+(select count(u) from User u)) from Rendezvous r")
	Double ratioCreatorsVsTotal();

	@Query("select avg(cast((select count(r.rendezvous) from Rsvp r where r.attendant.id=u.id) as float )),sqrt(sum((select count(r.rendezvous) from Rsvp r where r.attendant.id=u.id)*(select count(r.rendezvous) from Rsvp r where r.attendant.id=u.id))/(select count(u2) from User u2)-avg(cast((select count(r.rendezvous) from Rsvp r where r.attendant.id=u.id) as float ))*avg(cast((select count(r.rendezvous) from Rsvp r where r.attendant.id=u.id) as float ))) from User u")
	Double[] avgStandardDRendezvousesRsvpdPerUser();

	@Query(value = "select r.* from Rendezvous r ORDER BY (select count(rs.id) from Rsvp rs where rs.rendezvous_id=r.id) DESC LIMIT 10", nativeQuery = true)
	Collection<Object[]> top10Rendezvouses();

	@Query("select r from Rendezvous r where cast((select count(a) from Announcement a where a.rendezvous.id=r.id)as float)>(select avg(cast((select count(an) from Announcement an where an.rendezvous.id=r2.id)as float))*0.75 from Rendezvous r2)")
	Page<Rendezvous> rendezvousesNumberAnnouncementsPlus75Percentage(Pageable pageable);

	@Query("select count(r) from Rendezvous r where cast((select count(a) from Announcement a where a.rendezvous.id=r.id)as float)>(select avg(cast((select count(an) from Announcement an where an.rendezvous.id=r2.id)as float))*0.75 from Rendezvous r2)")
	Integer countRendezvousesNumberAnnouncementsPlus75Percentage();

	@Query("select r from Rendezvous r where cast((select count(r2) from Rendezvous r2 join r2.linkerRendezvouses l where l.id=r.id)as float)>(select avg(cast((select count(r2) from Rendezvous r2 join r2.linkerRendezvouses l where l.id=r3.id)as float))*1.1 from Rendezvous r3)")
	Page<Rendezvous> rendezvousesLinkedMoreAvgPlus10Percentage(Pageable pageable);

	@Query("select count(r) from Rendezvous r where cast((select count(r2) from Rendezvous r2 join r2.linkerRendezvouses l where l.id=r.id)as float)>(select avg(cast((select count(r2) from Rendezvous r2 join r2.linkerRendezvouses l where l.id=r3.id)as float))*1.1 from Rendezvous r3)")
	Integer countRendezvousesLinkedMoreAvgPlus10Percentage();

	@Query("select r.rendezvous from Request r join r.servicio.categories c where c.id=?1")
	Page<Rendezvous> findByCategoryId(int categoryId, Pageable pageable);

	@Query("select r.rendezvous from Request r join r.servicio.categories c where c.id=?1 and r.rendezvous.adultOnly=false")
	Page<Rendezvous> findByCategoryIdAllPublics(int categoryId, Pageable pageable);

}
