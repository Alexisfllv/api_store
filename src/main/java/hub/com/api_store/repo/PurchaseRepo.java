package hub.com.api_store.repo;

import hub.com.api_store.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseRepo extends JpaRepository<Purchase, Long> {
    List<Purchase> findByProductId(Long productId);
    List<Purchase> findBySupplierId(Long supplierId);

    List<Purchase> findByPurchaseDateBetween(LocalDateTime start, LocalDateTime end);
}
