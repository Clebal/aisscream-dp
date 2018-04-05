
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Newspaper;

@Repository
public interface NewspaperRepository extends JpaRepository<Newspaper, Integer> {

	@Query("select n from Newspaper n where n.publisher.id = ?1")
	Page<Newspaper> findByUserId(int userId, Pageable pageable); //Sirve para ver tus newspaper

	@Query("select s.newspaper from Subscription s where s.customer.id = ?1")
	Page<Newspaper> findByCustomerId(int customerId, Pageable pageable); //Sirve para ver todas tus subscripciones

	//Lista de que estén publicados y estén públicos
	@Query("select n from Newspaper n where n.publicationDate >= CURRENT_DATE and n.isPrivate=false and n.isPublished=true ")
	Page<Newspaper> findPublicsAndPublicated(Pageable pageable); //Sirve para ver los públicos y publicados (para los anónimos)

	//Lista de que estén publicados y estén públicos
	@Query("select n from Newspaper n")
	Page<Newspaper> findAllPaginated(Pageable pageable); //Sirve para ver todos los newspaper, lo usan para añadir artículos o (los no publicados no se verá su display o tendrá muy poca información)

	//Lista de newspapers privados y publicados a los que te puedes subscribir
	@Query(value = "select n.id from Newspaper n where n.id NOT IN (select (s.newspaper_id) from Subscription s where s.customer_id=?1) and n.publicationDate <= CURRENT_DATE and n.isPrivate=true", nativeQuery = true)
	Collection<Integer> findForSubscribe(int customerId); //Sirve para suscribirse a un newspaper

}
