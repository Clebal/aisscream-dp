package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Level;

@Repository
public interface LevelRepository extends JpaRepository<Level, Integer> {

	@Query("select l from Level l where ?1 >= l.minPoints and ?1 <= l.maxPoints")
	Level findByPoints(int points);
	
}
