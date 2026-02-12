package hub.com.api_store.service.impl;

import hub.com.api_store.dto.purchase.PurchaseDTOResponse;
import hub.com.api_store.entity.Category;
import hub.com.api_store.entity.Product;
import hub.com.api_store.entity.Purchase;
import hub.com.api_store.entity.Supplier;
import hub.com.api_store.mapper.PurchaseMapper;
import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.nums.GlobalUnit;
import hub.com.api_store.nums.PurchaseStatus;
import hub.com.api_store.service.domain.PurchaseServiceDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceImplTest {

    @Mock
    private PurchaseServiceDomain purchaseServiceDomain;

    @Mock
    private PurchaseMapper purchaseMapper;

    @InjectMocks
    private PurchaseServiceImpl purchaseServiceImpl;

    @Test
    @DisplayName("GET findByPurchaseId ")
    void findByPurchaseId(){
        // Arrange
        Long purchaseId = 1L;
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

        PurchaseDTOResponse purchaseDTOResponse = new PurchaseDTOResponse(
                1L,
                new BigDecimal("100.000"),
                GlobalUnit.KG,
                new BigDecimal("3.5000"),
                new BigDecimal("350.0000"),
                "LOT-2024-001",
                LocalDateTime.of(2025, 6, 30, 23, 59, 59),
                "A-01-B",
                LocalDateTime.of(2024, 1, 16, 14, 0, 0),
                LocalDateTime.of(2024, 1, 15, 9, 30, 0),
                "INV-2024-0001",
                "Arroz de primera calidad",
                PurchaseStatus.RECEIVED,
                product.getId(),
                product.getName(),
                supplier.getId(),
                supplier.getName()
        );

        when(purchaseServiceDomain.findPurchaseById(purchaseId)).thenReturn(purchase);
        when(purchaseMapper.toPurchaseDTOResponse(purchase)).thenReturn(purchaseDTOResponse);

        // Act
        PurchaseDTOResponse result = purchaseServiceImpl.findByPurchaseId(purchaseId);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(purchaseDTOResponse.id(), result.id()),
                () -> assertEquals(purchaseDTOResponse.quantity(), result.quantity()),
                () -> assertEquals(purchaseDTOResponse.unit(), result.unit()),
                () -> assertEquals(purchaseDTOResponse.costUnit(), result.costUnit()),
                () -> assertEquals(purchaseDTOResponse.totalCost(), result.totalCost()),
                () -> assertEquals(purchaseDTOResponse.lot(), result.lot()),
                () -> assertEquals(purchaseDTOResponse.expirationDate(), result.expirationDate()),
                () -> assertEquals(purchaseDTOResponse.warehouseLocation(), result.warehouseLocation()),
                () -> assertEquals(purchaseDTOResponse.arrivalDate(), result.arrivalDate()),
                () -> assertEquals(purchaseDTOResponse.purchaseDate(), result.purchaseDate()),
                () -> assertEquals(purchaseDTOResponse.invoiceNumber(), result.invoiceNumber()),
                () -> assertEquals(purchaseDTOResponse.notes(), result.notes()),
                () -> assertEquals(purchaseDTOResponse.purchaseStatus(), result.purchaseStatus()),
                () -> assertEquals(purchaseDTOResponse.productId(), result.productId()),
                () -> assertEquals(purchaseDTOResponse.productName(), result.productName()),
                () -> assertEquals(purchaseDTOResponse.supplierId(), result.supplierId()),
                () -> assertEquals(purchaseDTOResponse.supplierName(), result.supplierName())
        );

        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(purchaseServiceDomain, purchaseMapper);
        inOrder.verify(purchaseServiceDomain).findPurchaseById(purchaseId);
        inOrder.verify(purchaseMapper).toPurchaseDTOResponse(purchase);
        inOrder.verifyNoMoreInteractions();

    }

}
