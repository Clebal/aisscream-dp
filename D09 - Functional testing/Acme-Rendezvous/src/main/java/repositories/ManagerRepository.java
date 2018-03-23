package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {

	@Query("select m from Manager m where m.userAccount.id = ?1")
	Manager findByUserAccountId(final int userAccountId);
	
	// Queries Dashboard
	@Query("select distinct m from Service s join s.manager m where cast((select count(s2.manager) from Service s2 join s2.manager m2 where m.id=m2.id)as float)>(select avg(cast((select count(s2.manager) from Service s2 join s2.manager m2 where m.id=m2.id)as float)) from Manager m)")
	Collection<Manager> managerMoreServicesAverage();
	
	@Query("select m from Manager m where cast((select count(s2) from Service s2 where s2.status='CANCELLED' and s2.manager.id=m.id)as float)=(select max(cast((select count(s3) from Service s3 where s3.status='CANCELLED' and s3.manager.id=m2.id)as float)) from Manager m2)")
	Collection<Manager> managerMoreServicesCancelled();
	
}
