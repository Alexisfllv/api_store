package hub.com.api_store.repo;

import hub.com.api_store.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepo extends JpaRepository<Purchase, Long> {
    List<Purchase> findByProductId(Long productId);
}
