
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Advertisement;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {

	@Query("select n.advertisements from Newspaper n where n.id=?1 order by rand()")
	Page<Advertisement> findRandomAdvertisement(int newspaperId, Pageable pageable);

	@Query("select cast((count(a)) as float)/(select count(a2) from Advertisement a2) from Advertisement a where a.hasTaboo=true")
	Double ratioHaveTabooWords();

	@Query("select a from Advertisement a where a.hasTaboo=true")
	Page<Advertisement> findTaboos(Pageable pageable);

	@Query("select a from Advertisement a where a.agent.id=?1")
	Page<Advertisement> findByAgentId(int agentId, Pageable pageable);

	@Query(value = "select a.* from Advertisement a where a.id IN (select (s.advertisements_id) from Newspaper_advertisement s where newspaper_id=?2) and a.agent_id=?1", nativeQuery = true)
	Collection<Advertisement> findByAgentIdUnLinkToNewspaper(int agentId, int newspaperId);

	@Query(value = "select a.* from Advertisement a where a.id NOT IN (select (s.advertisements_id) from Newspaper_advertisement s where newspaper_id=?2) and a.agent_id=?1", nativeQuery = true)
	Collection<Advertisement> findByAgentIdLinkToNewspaper(int agentId, int newspaperId);

	@Query("select a from Advertisement a order by a.hasTaboo DESC")
	Page<Advertisement> findAllPaginated(Pageable page);

	@Query("select (count(n)*1.0)/(select count(n1)*1.0 from Newspaper n1 where n1.advertisements.size = 0) from Newspaper n where n.advertisements.size > 0")
	Double ratioNewspaperHaveAtLeastOneAdvertisementVSNoAdvertisement();
}
