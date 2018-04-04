package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chirp;

@Repository
public interface ChirpRepository extends JpaRepository<Chirp, Integer> {

	@Query("select c from Chirp c where c.user in (select u from User u where (select u2 from User u2 where u2.id = ?1) member of u.followers)")
	Page<Chirp> findFollowedsChirpByUserId(int userId, Pageable page);
	
	@Query("select c from Chirp c where c.hasTaboo = true")
	Page<Chirp> findHasTaboo(Pageable page);
	
	@Query("select c from Chirp c order by c.hasTaboo DESC")
	Page<Chirp> findAllPaginated(Pageable page);
	
}
