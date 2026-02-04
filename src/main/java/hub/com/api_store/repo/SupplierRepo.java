package hub.com.api_store.repo;

import hub.com.api_store.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepo extends JpaRepository<Supplier,Long> {
}
