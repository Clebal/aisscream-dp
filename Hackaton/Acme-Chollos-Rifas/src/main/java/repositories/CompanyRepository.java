package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

	@Query("select c from Company c where c.userAccount.id = ?1")
	Company findByUserAccountId(final int userAccountId);
	
	@Query("select c from Company c")
	Page<Company> findAllPaginated(Pageable pageable);
	
	// Dashboard
	@Query("select c from Company c order by cast((select count(t) from Tag t join t.bargains b where b.company.id= c.id) as float) DESC")
	Page<Company> companiesWithMoreTags(Pageable pageable);
	
}
