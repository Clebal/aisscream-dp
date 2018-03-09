
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Servicio;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {

	@Query("select s from Servicio s")
	Page<Servicio> findAllPaginated(final Pageable pageable);

	@Query("select s from Servicio s where s.manager.userAccount.id = ?1")
	Page<Servicio> findByManagerUserAccountId(final int userAccountId, final Pageable pageable);

	@Query("select s from Servicio s join s.categories c where c.id = ?1")
	Page<Servicio> findByCategoryId(final int categoryId, final Pageable pageable);

	@Query("select s from Servicio s join s.categories c where c.id = ?1")
	Collection<Servicio> findByCategoryIdNotPaginated(final int categoryId);

	@Query("select r.servicio from Request r where r.rendezvous.id = ?1")
	Page<Servicio> findByRendezvousId(final int rendezvousId, final Pageable pageable);

	@Query(value = "select s.id from Servicio s where s.id NOT IN (select (r.servicio_id) from Request r where r.rendezvous_id=?1) and s.status='ACCEPTED'", nativeQuery = true)
	Collection<Integer> findServicesForRequetsByRendezvousId(final int rendezvousId);

	@Query(value = "select count(s.id) from Servicio s where s.id NOT IN (select (r.servicio_id) from Request r where r.rendezvous_id=?1)and s.status='ACCEPTED'", nativeQuery = true)
	Integer countFindServicesForRequetsByRendezvousId(final int rendezvousId);

	@Query("select s from Servicio s where cast((select count(r) from Request r where r.servicio.id=s.id) as float)=(select max(cast((select count(r2) from Request r2 where r2.servicio.id=s2.id) as float)) from Servicio s2)")
	Page<Servicio> bestSellingServices(final Pageable pageable);

	@Query("select avg(cast((select count(s) from Servicio s where c member of s.categories) as float) / cast((select count(s2) from Servicio s2) as float )) from Category c")
	Double ratioServicesEachCategory();

	@Query("select  avg(cast((select count(s) from Servicio s, Request rq where rq.servicio.id=s.id and rq.rendezvous.id=r.id) as float )), min(cast((select count(s) from Servicio s, Request rq where rq.servicio.id=s.id and rq.rendezvous.id=r.id) as int )), max(cast((select count(s) from Servicio s, Request rq where rq.servicio.id=s.id and rq.rendezvous.id=r.id) as int )), sqrt(sum((select count(s) from Servicio s, Request rq where rq.servicio.id=s.id and rq.rendezvous.id=r.id)*(select count(s) from Servicio s, Request rq where rq.servicio.id=s.id and rq.rendezvous.id=r.id))/(select count(r2) from Rendezvous r2)-avg(cast((select count(s) from Servicio s, Request rq where rq.servicio.id=s.id and rq.rendezvous.id=r.id) as float ))*avg(cast((select count(s) from Servicio s, Request rq where rq.servicio.id=s.id and rq.rendezvous.id=r.id) as float ))) from Rendezvous r")
	Double[] avgMinMaxStandartDerivationServicesPerRendezvous();

	@Query(value = "select s.id from Servicio s ORDER BY (select count(r.id) from Request r where r.servicio_id=s.id) DESC LIMIT ?1", nativeQuery = true)
	Collection<Integer> topBestSellingServices(final int size);

}
