package hub.com.api_store.repo;

import hub.com.api_store.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {

    // validation unique name
    boolean existsByName(String name);
}
