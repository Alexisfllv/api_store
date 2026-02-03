package hub.com.api_store.repo;

import hub.com.api_store.entity.Category;
import hub.com.api_store.nums.CategoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {

    // validation unique name
    boolean existsByName(String name);

    // pageAllCategoryByStatus
    Page<Category> findByStatus(CategoryStatus status, Pageable pageable);

}
