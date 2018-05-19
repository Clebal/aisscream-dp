
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Bargain;

@Repository
public interface BargainRepository extends JpaRepository<Bargain, Integer> {

	@Query("select b from User u join u.wishList b where u.userAccount.id = ?1")
	Page<Bargain> findBargainByUserAccountId(int userAccountId, Pageable pageable);

	//Todos
	@Query("select b from Bargain b where b.isPublished=true")
	Page<Bargain> findAllPublished(Pageable pageable);

	@Query("select b from Bargain b where b.isPublished=true or b.company.id=?1")
	Page<Bargain> findAllPublishedOrMine(int companyId, Pageable pageable);

	@Query("select b from Bargain b")
	Page<Bargain> findAllPaginated(Pageable pageable);

	//Por categorías
	@Query("select b from Category c join c.bargains b where b.isPublished=true and c.id=?1")
	Page<Bargain> findAllPublishedByCategoryId(int categoryId, Pageable pageable);

	@Query("select b from Category c join c.bargains b where (b.isPublished=true or b.company.id=?1) and c.id=?1")
	Page<Bargain> findAllPublishedOrMineByCategoryId(int companyId, int categoryId, Pageable pageable);

	@Query("select b from Category c join c.bargains b where c.id=?1")
	Page<Bargain> findAllPaginatedByCategoryId(int categoryId, Pageable pageable);

	//Por etiquetas
	@Query("select b from Tag t join t.bargains b where b.isPublished=true and ?1 IN t.id")
	Page<Bargain> findAllPublishedByTagId(int tagId, Pageable pageable);

	@Query("select b from Tag t join t.bargains b where (b.isPublished=true or b.company.id=?1) and ?2 IN t.id")
	Page<Bargain> findAllPublishedOrMineByTagId(int companyId, int tagId, Pageable pageable);

	@Query("select b from Tag t join t.bargains b where ?1 IN t.id")
	Page<Bargain> findAllPaginatedByTagId(int tagId, Pageable pageable);

	//
	@Query("select b from Bargain b where b.company.id=?1")
	Page<Bargain> findByCompanyId(int companyId, Pageable pageable);

	@Query("select b from Bargain b order by cast((select count(s) from Sponsorship s where s.bargain.id=b.id) as int) DESC")
	Page<Bargain> findWithMoreSponsorshipsAllPaginated(Pageable pageable);

	@Query("select b from Bargain b where b.isPublished=true order by cast((select count(s) from Sponsorship s where s.bargain.id=b.id) as int) DESC")
	Page<Bargain> findWithMoreSponsorshipsAllPublished(Pageable pageable);

	@Query("select b from Bargain b where b.isPublished=true or b.company.id=?1 order by cast((select count(s) from Sponsorship s where s.bargain.id=b.id) as int) DESC")
	Page<Bargain> findWithMoreSponsorshipsAllPublishedOrMine(int companyId, Pageable pageable);

	@Query("select b from Bargain b where (select s from Sponsorship s where s.sponsor.id=?1) is null")
	Page<Bargain> findBySponsorIdWithNoSponsorship(int sponsorId, Pageable pageable);
}
