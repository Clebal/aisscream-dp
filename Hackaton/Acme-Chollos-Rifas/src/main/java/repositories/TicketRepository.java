package repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import domain.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

}
