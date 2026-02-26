package hub.com.api_store.repo;

import hub.com.api_store.entity.Inventory;
import hub.com.api_store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InventoryRepo extends JpaRepository<Inventory, Long> {

    // POST
    Optional<Inventory> findByProductAndLotAndWarehouse(
            Product product,
            String lot,
            String warehouse
    );

    List<Inventory> findByProductId(Long productId);

    List<Inventory> findByLot(String lot);

    List<Inventory> findByWarehouse(String warehouse);

    // avialable
    List<Inventory> findByProductIdAndQuantityGreaterThanAndExpirationDateAfterOrderByExpirationDateAsc(
            Long productId,
            BigDecimal quantity,
            LocalDateTime now
    );

    List<Inventory> findByQuantityGreaterThanAndExpirationDateAfterOrderByExpirationDateAsc(
            BigDecimal quantity,
            LocalDateTime now
    );

    // expiration
    List<Inventory> findByExpirationDateBetweenOrderByExpirationDateAsc(
            LocalDateTime start,
            LocalDateTime end
    );


}
