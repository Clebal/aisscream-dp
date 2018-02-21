
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {

	@Query("select a from Answer a where a.rsvp.id=?1 and a.question.id=?2")
	Answer findByRSVPIdAndQuestionId(int RSVPId, int questionId);

	@Query("select a from Answer a where a.question.rendezvous.id=?1 and a.question.rendezvous.creator.id=?2")
	Integer countRendezvousIdAndUserId(int rendezvousId, int userId);

	@Query("select a from Answer a where a.question.id=?1 and a.question.rendezvous.creator.id=?2")
	Answer findByQuestionIdAndUserId(int questionId, int userId);

}
