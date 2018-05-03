
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Bargain;

@Repository
public interface BargainRepository extends JpaRepository<Bargain, Integer> {

	@Query("select b from User u join u.wishList b where u.userAccount.id = ?1")
	Page<Bargain> findBargainByUserAccountId(int userAccountId, Pageable pageable);

}
