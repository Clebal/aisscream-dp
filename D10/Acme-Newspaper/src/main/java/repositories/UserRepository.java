
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
	
	@Query("select u.followers from User u where u.id = ?1")
	Page<User> findFollowersByUserId(int userId, Pageable page);
	
	@Query("select u from User u where (select u2 from User u2 where u2.id = ?1) member of u.followers")
	Page<User> findFollowedsByUserId(int userId, Pageable page);

	@Query("select u.followers.size from User u where u.id = ?1")
	Integer countFollowersByUserId(int userId);
	
	@Query("select count(u) from User u where (select u2 from User u2 where u2.id = ?1) member of u.followers")
	Integer countFollowedsByUserId(int userId);
	
	@Query("select u from User u")
	Page<User> findAllPaginated(Pageable page);
	
}
