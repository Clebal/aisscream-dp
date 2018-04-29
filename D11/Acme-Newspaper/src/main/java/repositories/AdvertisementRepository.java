
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Advertisement;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {

	@Query("select n.advertisement from Newspaper n where n.id=?1 order by rand()")
	public Page<Advertisement> findRandomAdvertisement(Pageable pageable, int newspaperId);

	@Query("select cast((count(a)) as float)/(select count(a2) from Advertisement a2) from Advertisement a where a.hasTaboo=true")
	public Double ratioHaveTabooWords();

	@Query("select a from Advertisement a where a.hasTaboo=true")
	public Page<Advertisement> findTaboos(Pageable pageable);

	@Query("select a from Advertisement a where a.agent.id=?1")
	public Page<Advertisement> findByAgentId(int agentId, Pageable pageable);

	@Query(value = "select a from Advertisement a where a.agent_id=?1 and NOT IN (select (a1) from Newspaper n join n.advertisements a1 where n.newspaper_id=?2)", nativeQuery = true)
	public Page<Advertisement> findByAgentIdUnLinkToNewspaper(int agentId, int newspaperId, Pageable pageable);

	@Query(value = "select a from Advertisement a where a.agent_id=?1 and IN (select (a1) from Newspaper n join n.advertisements a1 where n.newspaper_id=?2)", nativeQuery = true)
	public Page<Advertisement> findByAgentIdLinkToNewspaper(int agentId, int newspaperId, Pageable pageable);

	@Query("select a from Advertisement a order by a.hasTaboo DESC")
	Page<Advertisement> findAllPaginated(Pageable page);

	@Query("select (count(n)*1.0)/(select count(n1)*1.0 from Newspaper n1 where n1.advertisement.size = 0) from Newspaper n where n.advertisement.size > 0")
	Double ratioNewspaperHaveAtLeastOneAdvertisementVSNoAdvertisement();
}
