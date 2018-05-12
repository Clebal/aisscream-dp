
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {

	@Query("select count(a) from Answer a where a.question.survey.id=?1 and a.user.userAccount.id=?2")
	Integer countSurveyIdAndUserId(int surveyId, int userId);

	@Query("select a from Answer a where a.question.id=?1 and a.user.userAccount.id=?2")
	Answer findByQuestionIdAndUserAccountId(int questionId, int userAccountId);

	@Query("select a from Answer a where a.question.id=?1")
	Collection<Answer> findByQuestionId(int questionId);
	
}
