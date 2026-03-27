package hub.com.api_store.repo;

import hub.com.api_store.entity.Supplier;
import hub.com.api_store.nums.GlobalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepo extends JpaRepository<Supplier,Long> {
    // existsPhone
    boolean existsByPhone(String name);

    // existsEmail
    boolean existsByEmail(String email);

    // ListSupplierByName
    List<Supplier> findByNameContainingIgnoreCase(String nombre);

    // ListSupplierByStatus
    List<Supplier> findByStatus(GlobalStatus status);
}
