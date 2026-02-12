package hub.com.api_store.repo;

import hub.com.api_store.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepo extends JpaRepository<Purchase, Long> {
}
