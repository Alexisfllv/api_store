package hub.com.api_store.service.domain;

import hub.com.api_store.entity.Category;
import hub.com.api_store.entity.Inventory;
import hub.com.api_store.entity.Product;
import hub.com.api_store.exception.ResourceNotFoundException;
import hub.com.api_store.nums.ExceptionMessages;
import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.nums.GlobalUnit;
import hub.com.api_store.repo.InventoryRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceDomainTest {


    @Mock
    private InventoryRepo inventoryRepo;

    @InjectMocks
    private InventoryServiceDomain inventoryServiceDomain;

    @Nested
    @DisplayName("findById")
    class findById {
        @Test
        @DisplayName("Success")
        void findByIdSuccess() {
            // Arrange
            Long id = 1L;
            Category category = new Category(1L,"name","description", GlobalStatus.ACTIVE);
            Product product = new Product(1L,"name", GlobalUnit.KG,GlobalStatus.ACTIVE,category);

            Inventory inventory = new Inventory(1L,new BigDecimal(10.00),GlobalUnit.KG,
                    "A-01-B","LOT-2026-022", LocalDateTime.of(2026, 6, 30, 23, 59, 59),
                    product);

            when(inventoryRepo.findById(id)).thenReturn(Optional.of(inventory));
            // Act
            Inventory result = inventoryServiceDomain.findById(id);
            // Assert
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(inventory.getId(), result.getId()),
                    () -> assertEquals(inventory.getQuantity(), result.getQuantity()),
                    () -> assertEquals(inventory.getUnit(), result.getUnit()),
                    () -> assertEquals(inventory.getWarehouse(), result.getWarehouse()),
                    () -> assertEquals(inventory.getLot(), result.getLot()),
                    () -> assertEquals(inventory.getExpirationDate(), result.getExpirationDate()),
                    () -> assertEquals(inventory.getProduct().getId(), result.getProduct().getId())
            );

            // InOrder & Verify
            InOrder inOrder = Mockito.inOrder(inventoryRepo);
            inOrder.verify(inventoryRepo).findById(id);
            inOrder.verifyNoMoreInteractions();
        }

        @Test
        @DisplayName("Not Found")
        void findByIdNotFound() {
            // Arrange
            Long id = 1L;
            when(inventoryRepo.findById(id)).thenReturn(Optional.empty());
            // Act
            Exception exception = assertThrows(ResourceNotFoundException.class,
                    () -> inventoryServiceDomain.findById(id));

            // Assert
            assertEquals(ExceptionMessages.RESOURCE_NOT_FOUND_ERROR.message()+ id, exception.getMessage());

            // InOrder & Verify
            InOrder inOrder = Mockito.inOrder(inventoryRepo);
            inOrder.verify(inventoryRepo).findById(id);
            inOrder.verifyNoMoreInteractions();
        }
    }
}
