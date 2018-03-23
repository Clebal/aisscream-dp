
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

	@Query("select q from Question q where q.rendezvous.creator.userAccount.id = ?1 order by q.number")
	Page<Question> findByCreatorUserAccountId(int userAccountId, Pageable pageable);

	@Query("select count(q) from Question q where q.rendezvous.creator.userAccount.id = ?1")
	Integer countByCreatorUserAccountId(int userAccountId);
	
	@Query("select q from Question q where q.rendezvous.id = ?1 order by q.number ASC")
	Collection<Question> findByRendezvousId(int rendezvousId);

	@Query("select count(q) from Question q where q.rendezvous.id=?1")
	Integer countByRendezvousId(int rendezvousId);
	
	@Query("select avg(cast((select count(q) from Question q where q.rendezvous.id=r.id) as float )), sqrt(sum((select count(q) from Question q where q.rendezvous.id=r.id)*(select count(q) from Question q where q.rendezvous.id=r.id))/(select count(r2) from Rendezvous r2)-avg(cast((select count(q) from Question q where q.rendezvous.id=r.id) as float ))*avg(cast((select count(q) from Question q where q.rendezvous.id=r.id) as float ))) from Rendezvous r")
	Double[] avgStandartDerivationQuestionsPerRendezvous();
	
	@Query("select q from Question q where q.number > ?1 and rendezvous.id=?2")
	Collection<Question> findByHigherNumber(int number, int rendezvousId);
	
}
