
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select u from User u where u.userAccount.id = ?1")
	User findByUserAccountId(int id);

	@Query("select u from User u")
	Page<User> findAllPageable(Pageable pageable);

	@Query("select count(u) from User u")
	Integer findAllCount();

	//	@Query("select distinct u from User u, Rendezvous r, Rsvp rs where rs.rendezvous.id=?1 and rs.attendant=u")
	//	Page<User> findAttendantsPageables(int rendezvousId, Pageable pageable);

	@Query("select rs.attendant from Rsvp rs where rs.rendezvous.id=?1")
	Page<User> findAttendantsPageable(int rendezvousId, Pageable pageable);

	//	@Query("select count(distinct u) from User u, Rendezvous r, Rsvp rs where rs.rendezvous.id=?1 and rs.attendant=u")
	//	Integer findAttendantsCount(int rendezvousId);

	@Query("select count(rs.attendant)from Rsvp rs where rs.rendezvous.id=?1")
	Integer findAttendantsCount(int rendezvousId);

	@Query("select avg(cast((select count(r.rendezvous) from Rsvp r where r.attendant.id=u.id) as float )),sqrt(sum((select count(r.rendezvous) from Rsvp r where r.attendant.id=u.id)*(select count(r.rendezvous) from Rsvp r where r.attendant.id=u.id))/(select count(u2) from User u2)-avg(cast((select count(r.rendezvous) from Rsvp r where r.attendant.id=u.id) as float ))*avg(cast((select count(r.rendezvous) from Rsvp r where r.attendant.id=u.id) as float ))) from User u")
	Double[] avgStandardDUsersPerRendezvous();

}
