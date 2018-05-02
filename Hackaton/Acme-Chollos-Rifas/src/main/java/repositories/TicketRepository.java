package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

	@Query("select count(t) from Ticket t where t.creditCard != null and t.creditCard.id = ?1")
	Integer countByCreditCardId(int creditCardId);
	
	@Query("select t from Ticket t where t.user.userAccount.id = ?1")
	Page<Ticket> findByUserAccountId(int userAccountId, Pageable pageable);
	
	@Query("select t from Ticket t where t.user.userAccount.id = ?2 and t.raffle.id = ?1")
	Page<Ticket> findByRaffleIdAndUserAccountId(int raffleId, int userAccountId, Pageable pageable);
	
}
