package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

	@Query("select s from Subscription s where s.customer.userAccount.id = ?1")
	Page<Subscription> findByUserAccountId(int userAccountId, Pageable pageable);
	
	@Query("select s from Subscription s where s.customer.userAccount.id = ?1")
	Collection<Subscription> findByUserAccountId(int userAccountId);
	
	@Query("select s from Subscription s where s.customer.userAccount.id = ?1 and s.newspaper.id = ?2")
	Integer findByUserAccountIdNewspaper(int userAccountId, int newspaperId);
	
}
