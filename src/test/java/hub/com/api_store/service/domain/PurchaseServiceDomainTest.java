package hub.com.api_store.service.domain;

import hub.com.api_store.entity.Category;
import hub.com.api_store.entity.Product;
import hub.com.api_store.entity.Purchase;
import hub.com.api_store.entity.Supplier;
import hub.com.api_store.exception.ResourceNotFoundException;
import hub.com.api_store.nums.ExceptionMessages;
import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.nums.GlobalUnit;
import hub.com.api_store.nums.PurchaseStatus;
import hub.com.api_store.repo.PurchaseRepo;
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

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceDomainTest {

    @Mock
    private PurchaseRepo purchaseRepo;

    @InjectMocks
    private PurchaseServiceDomain purchaseServiceDomain;

    @Nested
    @DisplayName("findPurchaseById")
    class FindPurchaseById {
        @Test
        @DisplayName("findPurchaseById success")
        void findPurchaseByIdSuccess() {
            // Arrange
            Long idExist = 1L;

            Category category = new Category(1L, "Alimentos", "Productos alimenticios", GlobalStatus.ACTIVE);
            Product product = new Product(1L, "Arroz Premium", GlobalUnit.KG, GlobalStatus.ACTIVE, category);

            Supplier supplier = new Supplier
                    (1L,"Fring","+51920287650","Fring@email.com","Lima-Lima", GlobalStatus.ACTIVE);

            Purchase purchase = new Purchase();
            purchase.setId(1L);
            purchase.setQuantity(new BigDecimal("100.000"));
            purchase.setUnit(GlobalUnit.KG);
            purchase.setCostUnit(new BigDecimal("3.5000"));
            purchase.setTotalCost(new BigDecimal("350.0000"));
            purchase.setLot("LOT-2024-001");
            purchase.setExpirationDate(LocalDateTime.of(2025, 6, 30, 23, 59, 59));
            purchase.setWarehouseLocation("A-01-B");
            purchase.setArrivalDate(LocalDateTime.of(2024, 1, 16, 14, 0, 0));
            purchase.setPurchaseDate(LocalDateTime.of(2024, 1, 15, 9, 30, 0));
            purchase.setStatus(PurchaseStatus.RECEIVED);
            purchase.setInvoiceNumber("INV-2024-0001");
            purchase.setNotes("Arroz de primera calidad");
            purchase.setProduct(product);
            purchase.setSupplier(supplier);

            when(purchaseRepo.findById(idExist)).thenReturn(Optional.of(purchase));

            // Act
            Purchase result = purchaseServiceDomain.findPurchaseById(idExist);

            // Assert
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(purchase.getId(), result.getId()),
                    () -> assertEquals(purchase.getQuantity(), result.getQuantity()),
                    () -> assertEquals(purchase.getUnit(), result.getUnit()),
                    () -> assertEquals(purchase.getCostUnit(), result.getCostUnit()),
                    () -> assertEquals(purchase.getTotalCost(), result.getTotalCost()),
                    () -> assertEquals(purchase.getLot(), result.getLot()),
                    () -> assertEquals(purchase.getExpirationDate(), result.getExpirationDate()),
                    () -> assertEquals(purchase.getWarehouseLocation(), result.getWarehouseLocation()),
                    () -> assertEquals(purchase.getArrivalDate(), result.getArrivalDate()),
                    () -> assertEquals(purchase.getPurchaseDate(), result.getPurchaseDate()),
                    () -> assertEquals(purchase.getStatus(), result.getStatus()),
                    () -> assertEquals(purchase.getInvoiceNumber(), result.getInvoiceNumber()),
                    () -> assertEquals(purchase.getNotes(), result.getNotes()),
                    () -> assertEquals(purchase.getProduct().getId(), result.getProduct().getId()),
                    () -> assertEquals(purchase.getSupplier().getId(), result.getSupplier().getId())
            );

            // InOrder & Verfy
            InOrder inOrder = Mockito.inOrder(purchaseRepo);
            inOrder.verify(purchaseRepo).findById(idExist);
            inOrder.verifyNoMoreInteractions();
        }

        @Test
        @DisplayName("findPurchaseById not found throw exception")
        void findPurchaseByIdNotFoundThrowException() {
            // Arrange
            Long idNotExist = 999L;
            when(purchaseRepo.findById(idNotExist)).thenReturn(Optional.empty());

            // Act & Assert
            ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
                purchaseServiceDomain.findPurchaseById(idNotExist);
            });

            assertEquals(ExceptionMessages.RESOURCE_NOT_FOUND_ERROR.message()+idNotExist, ex.getMessage());

             // InOrder & Verfy
             InOrder inOrder = Mockito.inOrder(purchaseRepo);
             inOrder.verify(purchaseRepo).findById(idNotExist);
             inOrder.verifyNoMoreInteractions();
        }
    }
}
