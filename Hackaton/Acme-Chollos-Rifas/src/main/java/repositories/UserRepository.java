
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select u from User u where u.userAccount.id = ?1")
	User findByUserAccountId(int id);

	@Query("select u from User u")
	Page<User> findAllPageable(Pageable pageable);

	@Query("select u from User u order by u.points desc")
	Page<User> findOrderByPoints(Pageable pageable);
	
	@Query("select s.user from Subscription s where s.plan.name = 'Gold Premium'")
	Page<Actor> findWithGoldPremium(Pageable pageable);

	@Query("select s.user from Subscription s where s.plan.name = 'Basic Premium'")
	Page<Actor> findWithBasicPremium(Pageable pageable);
	
}
