package hub.com.api_store.service.domain;

import hub.com.api_store.exception.InsufficientStockException;
import hub.com.api_store.nums.ExceptionMessages;
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

import static org.junit.jupiter.api.Assertions.*;

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
}
