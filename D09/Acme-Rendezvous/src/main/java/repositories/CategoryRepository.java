
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

}
