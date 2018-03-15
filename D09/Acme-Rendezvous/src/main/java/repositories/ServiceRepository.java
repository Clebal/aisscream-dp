
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {

	@Query("select s from Service s")
	Page<Service> findAllPaginated(final Pageable pageable);

	@Query("select s from Service s where s.manager.userAccount.id = ?1")
	Page<Service> findByManagerUserAccountId(final int userAccountId, final Pageable pageable);

	@Query("select s from Service s join s.categories c where c.id = ?1")
	Page<Service> findByCategoryId(final int categoryId, final Pageable pageable);

	@Query("select s from Service s join s.categories c where c.id = ?1")
	Collection<Service> findByCategoryIdNotPaginated(final int categoryId);

	@Query("select r.service from Request r where r.rendezvous.id = ?1")
	Page<Service> findByRendezvousId(final int rendezvousId, final Pageable pageable);

	@Query(value = "select s.id from Service s where s.id NOT IN (select (r.service_id) from Request r where r.rendezvous_id=?1) and s.status='ACCEPTED'", nativeQuery = true)
	Collection<Integer> findServicesForRequetsByRendezvousId(final int rendezvousId);

	@Query(value = "select count(s.id) from Service s where s.id NOT IN (select (r.service_id) from Request r where r.rendezvous_id=?1)and s.status='ACCEPTED'", nativeQuery = true)
	Integer countFindServicesForRequetsByRendezvousId(final int rendezvousId);

	@Query("select s from Service s where cast((select count(r) from Request r where r.service.id=s.id) as float)=(select max(cast((select count(r2) from Request r2 where r2.service.id=s2.id) as float)) from Service s2)")
	Page<Service> bestSellingServices(final Pageable pageable);

	@Query("select avg(cast((select count(s) from Service s where c member of s.categories) as float) / cast((select count(s2) from Service s2) as float )) from Category c")
	Double ratioServicesEachCategory();

	@Query("select  avg(cast((select count(s) from Service s, Request rq where rq.service.id=s.id and rq.rendezvous.id=r.id) as float )), min(cast((select count(s) from Service s, Request rq where rq.service.id=s.id and rq.rendezvous.id=r.id) as int )), max(cast((select count(s) from Service s, Request rq where rq.service.id=s.id and rq.rendezvous.id=r.id) as int )), sqrt(sum((select count(s) from Service s, Request rq where rq.service.id=s.id and rq.rendezvous.id=r.id)*(select count(s) from Service s, Request rq where rq.service.id=s.id and rq.rendezvous.id=r.id))/(select count(r2) from Rendezvous r2)-avg(cast((select count(s) from Service s, Request rq where rq.service.id=s.id and rq.rendezvous.id=r.id) as float ))*avg(cast((select count(s) from Service s, Request rq where rq.service.id=s.id and rq.rendezvous.id=r.id) as float ))) from Rendezvous r")
	Double[] avgMinMaxStandartDerivationServicesPerRendezvous();

	@Query(value = "select s.id from Service s ORDER BY (select count(r.id) from Request r where r.service_id=s.id) DESC LIMIT ?1", nativeQuery = true)
	Collection<Integer> topBestSellingServices(final int size);

}
