
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

	@Query("select rs.attendant from Rsvp rs where rs.rendezvous.id=?1")
	Page<User> findAttendantsPageable(int rendezvousId, Pageable pageable);

	@Query("select count(rs.attendant)from Rsvp rs where rs.rendezvous.id=?1")
	Integer findAttendantsCount(int rendezvousId);

	@Query("select avg(cast((select count(rs.attendant) from Rsvp rs where rs.rendezvous.id=r.id) as float )+1), sqrt(sum((select count(rs.attendant)+1 from Rsvp rs where rs.rendezvous.id=r.id)*(select count(rs.attendant)+1 from Rsvp rs where rs.rendezvous.id=r.id))/(select count(r2) from Rendezvous r2)-avg(cast((select count(rs.attendant) from Rsvp rs where rs.rendezvous.id=r.id) as float )+1)*avg(cast((select count(rs.attendant) from Rsvp rs where rs.rendezvous.id=r.id) as float )+1)) from Rendezvous r")
	Double[] avgStandardDUsersPerRendezvous();

}
