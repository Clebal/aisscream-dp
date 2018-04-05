
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

	//Lista de que est�n publicados y est�n p�blicos
	@Query("select n from Newspaper n where n.publicationDate >= CURRENT_DATE and n.isPrivate=false and n.isPublished=true ")
	Page<Newspaper> findPublicsAndPublicated(Pageable pageable); //Sirve para ver los p�blicos y publicados (para los an�nimos)

	//Lista de que est�n publicados y est�n p�blicos
	@Query("select n from Newspaper n")
	Page<Newspaper> findAllPaginated(Pageable pageable); //Sirve para ver todos los newspaper, lo usan para a�adir art�culos o (los no publicados no se ver� su display o tendr� muy poca informaci�n)

	//Lista de newspapers privados y publicados a los que te puedes subscribir
	@Query(value = "select n.id from Newspaper n where n.id NOT IN (select (s.newspaper_id) from Subscription s where s.customer_id=?1) and n.publicationDate <= CURRENT_DATE and n.isPrivate=true", nativeQuery = true)
	Collection<Integer> findForSubscribe(int customerId); //Sirve para suscribirse a un newspaper

}
