
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
	@Query("select n from Newspaper n where n.publicationDate <= CURRENT_DATE and n.isPrivate=false and n.isPublished=true ")
	Page<Newspaper> findPublicsAndPublicated(Pageable pageable); //Sirve para ver los p�blicos y publicados (para los an�nimos)

	//Todos los newspaper (para los logeados)
	@Query("select n from Newspaper n")
	Page<Newspaper> findAllPaginated(Pageable pageable); //Sirve para ver todos los newspaper, lo usan para a�adir art�culos o (los no publicados no se ver� su display o tendr� muy poca informaci�n)

	//Lista de que est�n publicados y est�n p�blicos
	@Query("select n from Newspaper n where n.hasTaboo=true")
	Page<Newspaper> findTaboos(Pageable pageable);

	//Lista de newspapers privados y publicados a los que te puedes subscribir
	@Query(value = "select n.id from Newspaper n where n.id NOT IN (select (s.newspaper_id) from Subscription s where s.customer_id=?1) and n.isPrivate=true", nativeQuery = true)
	Collection<Integer> findForSubscribe(int customerId); //Sirve para suscribirse a un newspaper

	@Query(value = "select count(n.id) from Newspaper n where n.id NOT IN (select (s.newspaper_id) from Subscription s where s.customer_id=?1) and n.isPrivate=true", nativeQuery = true)
	Integer countFindForSubscribe(int customerId);

	@Query("select n from Newspaper n where cast((select count(a) from Article a where a.newspaper.id=n.id)as float)>(select avg(cast((select count(a2) from Article a2 where a2.newspaper.id=n2.id)as float))*1.1 from Newspaper n2)")
	Page<Newspaper> find10PercentageMoreAvg(Pageable pageable);

	@Query("select n from Newspaper n where n.publicationDate <= CURRENT_DATE and n.isPublished=true and n.isPrivate=false and (n.title like CONCAT('%',?1,'%') or n.description like CONCAT('%',?1,'%'))")
	Page<Newspaper> findPublicsPublishedSearch(String keyWord, Pageable pageable);

	@Query("select n from Newspaper n where n.publicationDate <= CURRENT_DATE and n.isPublished=true and (n.title like CONCAT('%',?1,'%') or n.description like CONCAT('%',?1,'%'))")
	Page<Newspaper> findPublishedSearch(String keyWord, Pageable pageable);

	@Query("select  avg(cast((select count(n) from Newspaper n where n.publisher.id=u.id) as float )), sqrt(sum((select count(n) from Newspaper n where n.publisher.id=u.id)*(select count(n) from Newspaper n where n.publisher.id=u.id))/(select count(u2) from User u2)-avg(cast((select count(n) from Newspaper n where n.publisher.id=u.id) as float ))*avg(cast((select count(n) from Newspaper n where n.publisher.id=u.id) as float ))) from User u")
	Double[] avgStandarDevNewspapersCreatedPerUser();

	@Query("select n from Newspaper n where cast((select count(a) from Article a where a.newspaper.id=n.id)as float)>(select avg(cast((select count(a2) from Article a2 where a2.newspaper.id=n2.id)as float))*0.9 from Newspaper n2)")
	Page<Newspaper> find10PercentageLessAvg(Pageable pageable);

	@Query("select (cast((select count(n) from Newspaper n where n.isPrivate=true) as float))/count(n2) from Newspaper n2 where n2.isPrivate=false")
	Double ratioPublicVsPrivateNewspaper();

	@Query("select avg(cast((select count(n) from Newspaper n where n.publisher.id = u.id and n.isPrivate = true) as float) / cast((select count(n2) from Newspaper n2 where n2.publisher.id = u.id and n2.isPrivate=false) as float )) from User u")
	Double ratioPrivateVersusPublicNewspaperPerPublisher();

}
