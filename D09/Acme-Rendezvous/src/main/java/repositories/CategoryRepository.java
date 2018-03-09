
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

	@Query("select c from Category c, Servicio s where s.id=?1 and c not member of s.categories;")
	Page<Category> findByServicioId(int servicioId, Pageable pageable);

	@Query("select avg(cast((select count(distinct c ) from Request req join req.servicio.categories c where req.rendezvous.id=r.id)as float)) from Rendezvous r;")
	double avgNumberCategoriesPerRendezvous();
	//Contando duplicados
	//select avg(cast((select sum(req.servicio.categories.size) from Request req where req.rendezvous.id=r.id)as float)) from Rendezvous r;

}
