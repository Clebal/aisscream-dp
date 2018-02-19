package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

	@Query("select q from Question q where q.rendezvous.creator.id = ?1")
	Collection<Question> findByCreatorUserId(int userId);
	
	@Query("select q from Question q where q.rendezvous.creator.userAccount.id = ?1")
	Collection<Question> findByCreatorUserAccountId(int userAccountId);
	
	@Query("select q from Question q where q.rendezvous.id = ?1")
	Collection<Question> findByRendezvousId(int rendezvousId);

	@Query("")
	double findAvgQuestionPerRendezvous();
	
	@Query("")
	double findStandardDerivationQuestionPerRendezvous();
	
	@Query("")
	double findAvgAnswerPerRendezvous();
	
	@Query("")
	double findStandardDerivationAnswerPerRendezvous();
	
}
