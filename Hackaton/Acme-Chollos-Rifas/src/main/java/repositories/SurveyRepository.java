package repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.Survey;

public interface SurveyRepository extends JpaRepository<Survey, Integer> {

	@Query("select s from Survey s where s.surveyer.userAccount.id = ?1")
	Page<Survey> findByActorUserAccountId(int userAccountId, Pageable pageable);
	
}
