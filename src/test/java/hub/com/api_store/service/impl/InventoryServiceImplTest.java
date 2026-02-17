package hub.com.api_store.service.impl;

import hub.com.api_store.entity.*;
import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.nums.GlobalUnit;
import hub.com.api_store.nums.PurchaseStatus;
import hub.com.api_store.repo.InventoryRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceImplTest {

    @Mock
    private InventoryRepo inventoryRepo;

    @InjectMocks
    private InventoryServiceImpl inventoryService;


    @Nested
    class addStockFromPurchase {
        @Test
        void shouldCreateNewInventoryWhenNoDuplicateExists() {
            // Arrange
            Category category = new Category(1L, "Alimentos", "Productos alimenticios", GlobalStatus.ACTIVE);
            Product product = new Product(1L, "Arroz Premium", GlobalUnit.KG, GlobalStatus.ACTIVE, category);

            Supplier supplier = new Supplier
                    (1L, "Fring", "+51920287650", "Fring@email.com", "Lima-Lima", GlobalStatus.ACTIVE);

            Purchase purchase = new Purchase(
                    1L,
                    new BigDecimal("100.000"),
                    GlobalUnit.KG,
                    new BigDecimal("3.5000"),
                    new BigDecimal("350.0000"),
                    LocalDateTime.of(2026, 6, 15, 8, 20, 59),
                    "LOT-2026-001",
                    LocalDateTime.of(2027, 12, 10, 14, 0, 0),
                    "A-01-B",
                    LocalDateTime.of(2026, 6, 10, 8, 0, 0),
                    PurchaseStatus.RECEIVED,
                    "INV-2026-0001",
                    "Arroz de primera calidad",
                    product,
                    supplier
            );

            when(inventoryRepo.findByProductAndLotAndWarehouse(
                    purchase.getProduct(),
                    purchase.getLot(),
                    purchase.getWarehouseLocation()
            )).thenReturn(Optional.empty());

            // Act
            inventoryService.addStockFromPurchase(purchase);
            // Assert
            // Verify
            verify(inventoryRepo, times(1)).save(argThat(inventory ->
                    inventory.getProduct().equals(purchase.getProduct()) &&
                            inventory.getQuantity().equals(purchase.getQuantity()) &&
                            inventory.getUnit().equals(purchase.getUnit()) &&
                            inventory.getLot().equals(purchase.getLot()) &&
                            inventory.getWarehouse().equals(purchase.getWarehouseLocation()) &&
                            inventory.getExpirationDate().equals(purchase.getExpirationDate())
            ));
        }

        @Test
        @DisplayName("Debe sumar la cantidad cuando ya existe un inventario con el mismo producto, lote y ubicación")
        void shouldAddQuantityWhenInventoryAlreadyExists() {
            // Arrange

            Category category = new Category(1L, "Alimentos", "Productos alimenticios", GlobalStatus.ACTIVE);
            Product product = new Product(1L, "Arroz Premium", GlobalUnit.KG, GlobalStatus.ACTIVE, category);

            Supplier supplier = new Supplier
                    (1L, "Fring", "+51920287650", "Fring@email.com", "Lima-Lima", GlobalStatus.ACTIVE);

            Purchase purchase = new Purchase(
                    1L,
                    new BigDecimal("100.000"),
                    GlobalUnit.KG,
                    new BigDecimal("3.5000"),
                    new BigDecimal("350.0000"),
                    LocalDateTime.of(2026, 6, 15, 8, 20, 59),
                    "LOT-2026-001",
                    LocalDateTime.of(2027, 12, 10, 14, 0, 0),
                    "A-01-B",
                    LocalDateTime.of(2026, 6, 10, 8, 0, 0),
                    PurchaseStatus.RECEIVED,
                    "INV-2026-0001",
                    "Arroz de primera calidad",
                    product,
                    supplier
            );

            Inventory existingInventory = new Inventory();
            existingInventory.setProduct(product);
            existingInventory.setQuantity(new BigDecimal("50.000"));
            existingInventory.setUnit(GlobalUnit.KG);
            existingInventory.setLot("LOT-2026-001");
            existingInventory.setWarehouse("A-01-B");

            when(inventoryRepo.findByProductAndLotAndWarehouse(
                    purchase.getProduct(),
                    purchase.getLot(),
                    purchase.getWarehouseLocation()
            )).thenReturn(Optional.of(existingInventory));

            // Act
            inventoryService.addStockFromPurchase(purchase);

            // Verify
            verify(inventoryRepo, times(1)).save(argThat(inventory ->
                    inventory.getQuantity().equals(new BigDecimal("150.000")) // 50 + 100
            ));
        }
    }
}
