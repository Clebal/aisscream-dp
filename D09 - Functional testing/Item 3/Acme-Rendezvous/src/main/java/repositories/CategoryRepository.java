
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	@Query("select c from Category c where c.fatherCategory.id=?1")
	Page<Category> findByFatherCategoryId(int fatherCategoryId, Pageable pageable);

	@Query("select c from Category c where c.fatherCategory.id=?1")
	Collection<Category> findAllByFatherCategoryId(int fatherCategoryId);

	@Query("select c from Category c where c.fatherCategory is null")
	Page<Category> findWithoutFather(Pageable pageable);

	@Query("select c from Category c where c.fatherCategory is null")
	Collection<Category> findAllWithoutFather();

	@Query("select c from Category c, Service s where s.id=?1 and c not member of s.categories")
	Page<Category> findByServiceId(int serviceId, Pageable pageable);

	@Query("select avg(cast((select count(distinct c ) from Request req join req.service.categories c where req.rendezvous.id=r.id)as float)) from Rendezvous r")
	Double avgNumberCategoriesPerRendezvous();

	@Query("select c from Category c where c.defaultCategory=true")
	Category findByDefaultCategory();

	@Query("select c from Category c")
	Page<Category> findAllPaginated(Pageable pageable);
	//Contando duplicados
	//select avg(cast((select sum(req.service.categories.size) from Request req where req.rendezvous.id=r.id)as float)) from Rendezvous r;

}
