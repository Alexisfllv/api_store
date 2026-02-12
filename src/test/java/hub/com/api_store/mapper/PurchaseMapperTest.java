package hub.com.api_store.mapper;

import hub.com.api_store.dto.purchase.PurchaseDTOResponse;
import hub.com.api_store.entity.Category;
import hub.com.api_store.entity.Product;
import hub.com.api_store.entity.Purchase;
import hub.com.api_store.entity.Supplier;
import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.nums.GlobalUnit;
import hub.com.api_store.nums.PurchaseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PurchaseMapperTest {

    private PurchaseMapper purchaseMapper;

    @BeforeEach
    void setUp(){
        purchaseMapper = new PurchaseMapper();
    }

    @Test
    @DisplayName("Test Mapped response <- entity")
    void toPurchaseDTOResponse_shouldMapCorrectly(){
        // Arrange
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

        // Act
        PurchaseDTOResponse result = purchaseMapper.toPurchaseDTOResponse(purchase);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1L, result.id()),
                () -> assertEquals(new BigDecimal("100.000"), result.quantity()),
                () -> assertEquals(GlobalUnit.KG, result.unit()),
                () -> assertEquals(new BigDecimal("3.5000"), result.costUnit()),
                () -> assertEquals(new BigDecimal("350.0000"), result.totalCost()),
                () -> assertEquals("LOT-2024-001", result.lot()),
                () -> assertEquals(LocalDateTime.of(2025, 6, 30, 23, 59, 59), result.expirationDate()),
                () -> assertEquals("A-01-B", result.warehouseLocation()),
                () -> assertEquals(LocalDateTime.of(2024, 1, 16, 14, 0, 0), result.arrivalDate()),
                () -> assertEquals(LocalDateTime.of(2024, 1, 15, 9, 30, 0), result.purchaseDate()),
                () -> assertEquals(PurchaseStatus.RECEIVED, result.purchaseStatus()),
                () -> assertEquals("INV-2024-0001", result.invoiceNumber()),
                () -> assertEquals("Arroz de primera calidad", result.notes()),
                () -> assertEquals(1L, result.productId()),
                () -> assertEquals("Arroz Premium", result.productName()),
                () -> assertEquals(1L, result.supplierId()),
                () -> assertEquals("Fring", result.supplierName())
        );

    }
}
