
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {

	@Query("select a from Answer a where a.rsvp.id=?1 and a.question.id=?2")
	Answer findByRSVPIdAndQuestionId(int RSVPId, int questionId);

	@Query("select count(a) from Answer a where a.question.rendezvous.id=?1 and a.question.rendezvous.creator.id=?2")
	Integer countRendezvousIdAndUserId(int rendezvousId, int userId);

	@Query("select a from Answer a where a.question.id=?1 and a.rsvp.attendant.id=?2")
	Answer findByQuestionIdAndUserId(int questionId, int userId);

	@Query("select avg(cast((select count(a) from Answer a where a.question.rendezvous.id=r.id)as int)),sqrt(sum((select count(a) from Answer a where a.question.rendezvous.id=r.id)*(select count(a) from Answer a where a.question.rendezvous.id=r.id))/(select count(r2) from Rendezvous r2)-avg(cast((select count(a) from Answer a where a.question.rendezvous.id=r.id) as float ))*avg(cast((select count(a) from Answer a where a.question.rendezvous.id=r.id) as float ))) from Rendezvous r")
	Double[] avgStandardAnswerPerRendezvous();

	@Query("select a from Answer a where a.question.id=?1")
	Collection<Answer> findByQuestionId(int questionId);
}
