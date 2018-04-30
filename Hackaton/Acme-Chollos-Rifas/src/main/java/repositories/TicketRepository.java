package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

	@Query("select count(t) from Ticket t where t.creditCard != null and t.creditCard.id = ?1")
	Integer countByCreditCardId(int creditCardId);
	
}
