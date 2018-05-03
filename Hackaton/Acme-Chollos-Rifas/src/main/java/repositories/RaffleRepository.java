package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.Raffle;

public interface RaffleRepository extends JpaRepository<Raffle, Integer> {
	
	@Query("select r from Raffle r where r.maxDate > CURRENT_DATE")
	Page<Raffle> findAvailables(Pageable pageable);
	
	@Query("select r from Raffle r where r IN (select t.raffle from Ticket t where t.user.userAccount.id = ?1)")
	Page<Raffle> findByUserAccountId(int userAccountId, Pageable pageable);

	@Query("select r from Raffle r where r.company.userAccount.id = ?1")
	Page<Raffle> findByCompanyAccountId(int userAccountId, Pageable pageable);
	
	@Query("select r from Raffle r where r.maxDate > CURRENT_DATE order by r.maxDate")
	Page<Raffle> findOrderedByMaxDate(Pageable pageable);
	
	@Query("select r from Raffle r")
	Page<Raffle> findAllPaginated(Pageable pageable);
	
}
