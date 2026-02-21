package hub.com.api_store.repo;

import hub.com.api_store.entity.Inventory;
import hub.com.api_store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface InventoryRepo extends JpaRepository<Inventory, Long> {

    // POST
    Optional<Inventory> findByProductAndLotAndWarehouse(
            Product product,
            String lot,
            String warehouse
    );

}
