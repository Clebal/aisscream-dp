package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Configuration;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {

	@Query("select c from Configuration c")
	Configuration findUnique();
	
	@Query("select c.name from Configuration c")
	String findName();

	@Query("select c.banner from Configuration c")
	String findBanner();

	@Query("select c.welcomeMessage from Configuration c")
	String findWelcomeMessage();
	
}
