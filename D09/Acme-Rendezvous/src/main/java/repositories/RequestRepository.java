
package repositories;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

	@Query("select r from Request r where r.rendezvous.creator.id=?1")
	Page<Request> findAllPageable(final int userId, Pageable pageable);

	@Query("select count(r) from Request r where r.rendezvous.creator.id=?1")
	Integer findAllCount(final int userId);
	
	@Query("select count(r) from Request r where r.creditCard.id = ?1")
	Integer countByCreditCardId(final int creditCardId);
	
	@Query("select count(r) from Request r where r.service.id = ?1")
	Integer countByServiceId(final int serviceId);
	
	@Query("select r from Request r where r.rendezvous.id=?1 and r.service.id = ?2")
	Request findRequestEqualRendezvousService(final int rendezvousId, final int serviceId);
	
}
