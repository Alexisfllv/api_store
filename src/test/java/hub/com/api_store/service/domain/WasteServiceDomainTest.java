package hub.com.api_store.service.domain;

import hub.com.api_store.dto.waste.WasteDTOResponse;
import hub.com.api_store.entity.Category;
import hub.com.api_store.entity.Inventory;
import hub.com.api_store.entity.Product;
import hub.com.api_store.entity.Waste;
import hub.com.api_store.exception.InsufficientStockException;
import hub.com.api_store.exception.ResourceNotFoundException;
import hub.com.api_store.nums.ExceptionMessages;
import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.nums.GlobalUnit;
import hub.com.api_store.nums.WasteReason;
import hub.com.api_store.repo.WasteRepo;
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
public class WasteServiceDomainTest {
    @Mock
    private WasteRepo wasteRepo;

    @InjectMocks
    private WasteServiceDomain wasteServiceDomain;

    @Nested
    @DisplayName("validateStock")
    class validateStock {
        @Test
        @DisplayName("Validate stock with sufficient quantity")
        void validateStockSufficient() {
            // Arrange
            BigDecimal quantityActual = new BigDecimal("10.000");
            BigDecimal quantityRequest = new BigDecimal("5.000");

            // Act & Assert
            assertDoesNotThrow(() -> wasteServiceDomain.validateStock(quantityActual, quantityRequest));

            // InOrder & Verify
            InOrder inOrder = Mockito.inOrder(wasteRepo);
            inOrder.verifyNoMoreInteractions();
        }

        @Test
        @DisplayName("Validate stock with insufficient quantity throws InsufficientStockException")
        void validateStockInsufficient() {
            // Arrange
            BigDecimal quantityActual = new BigDecimal("5.000");
            BigDecimal quantityRequest = new BigDecimal("10.000");

            // Act & Assert
            InsufficientStockException ex =  assertThrows(InsufficientStockException.class, () -> wasteServiceDomain.validateStock(quantityActual, quantityRequest));

            // Assert exception message
            assertEquals(ExceptionMessages.INSUFFICIENT_STOCK.message() +
                    quantityActual +
                    " requested: " +
                    quantityRequest, ex.getMessage());

            // InOrder & Verify
            InOrder inOrder = Mockito.inOrder(wasteRepo);
            inOrder.verifyNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("findById")
    class findById {
        @Test
        @DisplayName("Find by ID with existing ID returns Waste")
        void findByIdExisting() {
            // Arrange
            Long id = 1L;
            Category category = new Category(1L, "name", "description", GlobalStatus.ACTIVE);
            Product product = new Product(1L, "name", GlobalUnit.KG, GlobalStatus.ACTIVE, category);

            Inventory inventory = new Inventory(1L, new BigDecimal(10.00), GlobalUnit.KG,
                    "A-01-B", "LOT-2026-022", LocalDateTime.of(2026, 6, 30, 23, 59, 59),
                    product);

            Waste waste = new Waste(1L, new BigDecimal(5.00), GlobalUnit.KG, WasteReason.EXPIRED,
                    "notes",
                    LocalDateTime.of(2026, 6, 30, 23, 59, 59), inventory);


            when(wasteRepo.findById(id)).thenReturn(Optional.of(waste));
            // Act
            Waste result = wasteServiceDomain.findById(id);

            // Assert
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(waste, result)
            );

            // InOrder & Verify
            InOrder inOrder = Mockito.inOrder(wasteRepo);
            inOrder.verify(wasteRepo, Mockito.times(1)).findById(id);
            inOrder.verifyNoMoreInteractions();

        }

        @Test
        @DisplayName("Find by ID with non-existing ID throws ResourceNotFoundException")
        void findByIdNonExisting() {
            // Arrange
            Long id = 1L;
            when(wasteRepo.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> wasteServiceDomain.findById(id));

            // Assert exception message
            assertEquals(ExceptionMessages.RESOURCE_NOT_FOUND_ERROR.message() + id, ex.getMessage());

            // InOrder & Verify
            InOrder inOrder = Mockito.inOrder(wasteRepo);
            inOrder.verify(wasteRepo, Mockito.times(1)).findById(id);
            inOrder.verifyNoMoreInteractions();
        }
    }
}
