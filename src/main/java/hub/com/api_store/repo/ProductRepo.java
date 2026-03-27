package hub.com.api_store.repo;

import hub.com.api_store.entity.Product;
import hub.com.api_store.nums.GlobalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {

    // products x category Id
    List<Product> findByCategoryId(Long categoryId);

    // products x name
    List<Product> findByNameContainingIgnoreCase(String name);

    // products x status
    List<Product> findByStatus(GlobalStatus status);
}
