package hub.com.api_store.service.impl;

import hub.com.api_store.dto.purchase.PurchaseDTORequest;
import hub.com.api_store.dto.purchase.PurchaseDTOResponse;
import hub.com.api_store.dto.purchase.PurchaseDTOUpdate;
import hub.com.api_store.entity.*;
import hub.com.api_store.mapper.PurchaseMapper;
import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.nums.GlobalUnit;
import hub.com.api_store.nums.PurchaseStatus;
import hub.com.api_store.repo.InventoryRepo;
import hub.com.api_store.repo.ProductRepo;
import hub.com.api_store.repo.PurchaseRepo;
import hub.com.api_store.service.domain.ProductServiceDomain;
import hub.com.api_store.service.domain.PurchaseServiceDomain;
import hub.com.api_store.service.domain.SupplierServiceDomain;
import hub.com.api_store.util.page.PageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceImplTest {

    @Mock
    private PurchaseServiceDomain purchaseServiceDomain;

    @Mock
    private PurchaseRepo purchaseRepo;

    @Mock
    private PurchaseMapper purchaseMapper;

    @Mock
    private ProductServiceDomain productServiceDomain;

    @Mock
    private SupplierServiceDomain supplierServiceDomain;

    @Mock
    private InventoryServiceImpl inventoryServiceImpl;

    @Mock
    private InventoryRepo inventoryRepo;

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

        Inventory inventory = new Inventory(
                1L,new BigDecimal("10.500"),GlobalUnit.KG,"A-01-B","LOT-2026-022",
                LocalDateTime.of(2027, 12, 10, 14, 0, 0),product
        );

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
                supplier.getName(),
                inventory.getId(),
                inventory.getWarehouse()
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
                () -> assertEquals(purchaseDTOResponse.supplierName(), result.supplierName()),
                () -> assertEquals(purchaseDTOResponse.inventoryId(), result.inventoryId()),
                () -> assertEquals(purchaseDTOResponse.inventoryWarehouse(), result.inventoryWarehouse())
        );

        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(purchaseServiceDomain, purchaseMapper);
        inOrder.verify(purchaseServiceDomain).findPurchaseById(purchaseId);
        inOrder.verify(purchaseMapper).toPurchaseDTOResponse(purchase);
        inOrder.verifyNoMoreInteractions();

    }

    @Nested
    @DisplayName("GET findAllListPagePurchaseGet")
    class FindAllListPagePurchaseGetTest {
        @Test
        @DisplayName("Should return a page of PurchaseDTOResponse")
        void shouldReturnPageOfPurchaseDTOResponse() {
            // Arrange
            int page = 0;
            int size = 10;
            String prop = "id";

            Long purchaseId = 1L;
            Category category = new Category(1L, "Alimentos", "Productos alimenticios", GlobalStatus.ACTIVE);
            Product product = new Product(1L, "Arroz Premium", GlobalUnit.KG, GlobalStatus.ACTIVE, category);

            Supplier supplier = new Supplier
                    (1L, "Fring", "+51920287650", "Fring@email.com", "Lima-Lima", GlobalStatus.ACTIVE);

            Inventory inventory = new Inventory(
                    1L,new BigDecimal("10.500"),GlobalUnit.KG,"A-01-B","LOT-2026-022",
                    LocalDateTime.of(2027, 12, 10, 14, 0, 0),product
            );

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
                    supplier.getName(),
                    inventory.getId(),
                    inventory.getWarehouse()
            );
            List<Purchase> purchaseList = List.of(purchase);

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, prop));
            Page<Purchase> purchasePage =  new PageImpl<>(purchaseList,pageable, purchaseList.size());

            when(purchaseRepo.findAll(pageable)).thenReturn(purchasePage);
            when(purchaseMapper.toPurchaseDTOResponse(purchase)).thenReturn(purchaseDTOResponse);

            // Act
            PageResponse<PurchaseDTOResponse> result = purchaseServiceImpl.findAllListPagePurchase(page, size, prop);

            // Assert
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(1, result.content().size()),
                    () -> assertEquals(purchaseDTOResponse, result.content().get(0)),
                    () -> assertEquals(page, result.page()),
                    () -> assertEquals(size, result.size()),
                    () -> assertEquals(purchaseList.size(), result.totalElements()),
                    () -> assertEquals(1, result.totalPages())
            );

            // InOrder & Verify
            InOrder inOrder = Mockito.inOrder(purchaseRepo, purchaseMapper);
            inOrder.verify(purchaseRepo).findAll(pageable);
            inOrder.verify(purchaseMapper).toPurchaseDTOResponse(purchase);
            inOrder.verifyNoMoreInteractions();
        }
    }

    @Test
    @DisplayName("POST createPurchase")
    void createPurchase(){
        // Arrange
        Category category = new Category(1L, "Alimentos", "Productos alimenticios", GlobalStatus.ACTIVE);
        Product product = new Product(1L, "Arroz Premium", GlobalUnit.KG, GlobalStatus.ACTIVE, category);

        Supplier supplier = new Supplier
                (1L,"Fring","+51920287650","Fring@email.com","Lima-Lima", GlobalStatus.ACTIVE);

        Inventory inventory = new Inventory(
                1L,new BigDecimal("10.500"),GlobalUnit.KG,"A-01-B","LOT-2026-022",
                LocalDateTime.of(2027, 12, 10, 14, 0, 0),product
        );

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
                supplier.getName(),
                inventory.getId(),
                inventory.getWarehouse()
        );

        PurchaseDTORequest request = new PurchaseDTORequest(
                new BigDecimal("100.000"),
                GlobalUnit.KG,
                new BigDecimal("3.5000"),
                "LOT-2024-001",
                LocalDateTime.of(2025, 6, 30, 23, 59, 59),
                "A-01-B",
                LocalDateTime.of(2024, 1, 16, 14, 0, 0),
                "INV-2024-0001",
                "Arroz de primera calidad",
                1L,
                1L
        );

        when(productServiceDomain.findById(request.productId())).thenReturn(product);
        when(supplierServiceDomain.findByIdSupplier(request.supplierId())).thenReturn(supplier);
        when(purchaseMapper.toPurchase(request,product,supplier)).thenReturn(purchase);
        when(purchaseRepo.save(purchase)).thenReturn(purchase);
        when(inventoryServiceImpl.addStockFromPurchase(purchase)).thenReturn(inventory);
        when(purchaseRepo.save(purchase)).thenReturn(purchase);
        when(purchaseMapper.toPurchaseDTOResponse(purchase)).thenReturn(purchaseDTOResponse);
        // Act
        PurchaseDTOResponse result = purchaseServiceImpl.createPurchase(request);

        // Assert
        assertAll(
                () -> assertEquals(purchaseDTOResponse.id(),result.id()),
                () -> assertEquals(purchaseDTOResponse.quantity(),result.quantity()),
                () -> assertEquals(purchaseDTOResponse.unit(),result.unit()),
                () -> assertEquals(purchaseDTOResponse.costUnit(),result.costUnit()),
                () -> assertEquals(purchaseDTOResponse.totalCost(),result.totalCost()),
                () -> assertEquals(purchaseDTOResponse.lot(),result.lot()),
                () -> assertEquals(purchaseDTOResponse.expirationDate(),result.expirationDate()),
                () -> assertEquals(purchaseDTOResponse.warehouseLocation(),result.warehouseLocation()),
                () -> assertEquals(purchaseDTOResponse.arrivalDate(),result.arrivalDate()),
                () -> assertEquals(purchaseDTOResponse.purchaseDate(),result.purchaseDate()),
                () -> assertEquals(purchaseDTOResponse.invoiceNumber(),result.invoiceNumber()),
                () -> assertEquals(purchaseDTOResponse.notes(),result.notes()),
                () -> assertEquals(purchaseDTOResponse.purchaseStatus(),result.purchaseStatus()),
                () -> assertEquals(purchaseDTOResponse.productId(),result.productId()),
                () -> assertEquals(purchaseDTOResponse.productName(),result.productName()),
                () -> assertEquals(purchaseDTOResponse.supplierId(),result.supplierId()),
                () -> assertEquals(purchaseDTOResponse.supplierName(),result.supplierName()),
                () -> assertEquals(purchaseDTOResponse.inventoryId(),result.inventoryId()),
                () -> assertEquals(purchaseDTOResponse.inventoryWarehouse(),result.inventoryWarehouse())
        );

        // Verify
        InOrder inOrder = Mockito.inOrder(productServiceDomain, supplierServiceDomain, purchaseMapper, purchaseRepo,inventoryServiceImpl);
        inOrder.verify(productServiceDomain).findById(request.productId());
        inOrder.verify(supplierServiceDomain).findByIdSupplier(request.supplierId());
        inOrder.verify(purchaseMapper).toPurchase(request, product, supplier);
        inOrder.verify(purchaseRepo).save(purchase);
        inOrder.verify(inventoryServiceImpl).addStockFromPurchase(purchase);
        inOrder.verify(purchaseRepo).save(purchase);
        inOrder.verify(purchaseMapper).toPurchaseDTOResponse(purchase);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("PUT updatePurchase")
    void updatePurchase() {
        // Arrange
        Long purchaseId = 1L;

        Category category = new Category(1L, "Alimentos", "Productos alimenticios", GlobalStatus.ACTIVE);
        Product product = new Product(1L, "Arroz Premium", GlobalUnit.KG, GlobalStatus.ACTIVE, category);
        Supplier supplier = new Supplier(1L, "Fring", "+51920287650", "Fring@email.com", "Lima-Lima", GlobalStatus.ACTIVE);

        // Inventory existente asociado al purchase
        Inventory existingInventory = new Inventory(
                1L, new BigDecimal("100.000"), GlobalUnit.KG, "A-01-B", "LOT-2026-001",
                LocalDateTime.of(2027, 12, 10, 14, 0, 0), product
        );

        // Purchase existente que se va a actualizar
        Purchase existingPurchase = new Purchase();
        existingPurchase.setId(purchaseId);
        existingPurchase.setQuantity(new BigDecimal("100.000"));
        existingPurchase.setUnit(GlobalUnit.KG);
        existingPurchase.setCostUnit(new BigDecimal("3.5000"));
        existingPurchase.setTotalCost(new BigDecimal("350.0000"));
        existingPurchase.setLot("LOT-2026-001");
        existingPurchase.setExpirationDate(LocalDateTime.of(2027, 6, 30, 23, 59, 59));
        existingPurchase.setWarehouseLocation("A-01-B");
        existingPurchase.setArrivalDate(LocalDateTime.of(2026, 1, 16, 14, 0, 0));
        existingPurchase.setPurchaseDate(LocalDateTime.of(2026, 1, 15, 9, 30, 0));
        existingPurchase.setStatus(PurchaseStatus.RECEIVED);
        existingPurchase.setInvoiceNumber("INV-2026-0001");
        existingPurchase.setNotes("Notas originales");
        existingPurchase.setProduct(product);
        existingPurchase.setSupplier(supplier);
        existingPurchase.setInventory(existingInventory);

        // Request con los nuevos datos
        PurchaseDTOUpdate updateRequest = new PurchaseDTOUpdate(
                new BigDecimal("150.000"),  // quantity
                GlobalUnit.KG,              // unit
                new BigDecimal("4.0000"),   // costUnit
                "023",                      // lot
                LocalDateTime.of(2028, 6, 30, 23, 59, 59), // expirationDate
                "B-02-C",                   // warehouseLocation
                LocalDateTime.of(2026, 2, 20, 14, 0, 0),   // arrivalDate
                LocalDateTime.of(2026, 2, 15, 10, 0, 0),   // purchaseDate
                "0023",                     // invoiceNumber
                PurchaseStatus.RECEIVED,    // purchaseStatus
                "Notas actualizadas",       // notes
                1L,                         // productId
                1L                          // supplierId
        );

        // Purchase actualizado (después de los setters)
        Purchase updatedPurchase = new Purchase();
        updatedPurchase.setId(purchaseId);
        updatedPurchase.setQuantity(new BigDecimal("150.000"));
        updatedPurchase.setUnit(GlobalUnit.KG);
        updatedPurchase.setCostUnit(new BigDecimal("4.0000"));
        updatedPurchase.setTotalCost(new BigDecimal("600.0000"));
        updatedPurchase.setLot("LOT-2026-023");
        updatedPurchase.setExpirationDate(LocalDateTime.of(2028, 6, 30, 23, 59, 59));
        updatedPurchase.setWarehouseLocation("B-02-C");
        updatedPurchase.setArrivalDate(LocalDateTime.of(2026, 2, 20, 14, 0, 0));
        updatedPurchase.setPurchaseDate(LocalDateTime.of(2026, 2, 15, 10, 0, 0));
        updatedPurchase.setStatus(PurchaseStatus.RECEIVED);
        updatedPurchase.setInvoiceNumber("INV-2026-0023");
        updatedPurchase.setNotes("Notas actualizadas");
        updatedPurchase.setProduct(product);
        updatedPurchase.setSupplier(supplier);
        updatedPurchase.setInventory(existingInventory);

        // Response esperado
        PurchaseDTOResponse expectedResponse = new PurchaseDTOResponse(
                purchaseId,
                new BigDecimal("150.000"),
                GlobalUnit.KG,
                new BigDecimal("4.0000"),
                new BigDecimal("600.0000"),
                "LOT-2026-023",
                LocalDateTime.of(2028, 6, 30, 23, 59, 59),
                "B-02-C",
                LocalDateTime.of(2026, 2, 20, 14, 0, 0),
                LocalDateTime.of(2026, 2, 15, 10, 0, 0),
                "INV-2026-0023",
                "Notas actualizadas",
                PurchaseStatus.RECEIVED,
                product.getId(),
                product.getName(),
                supplier.getId(),
                supplier.getName(),
                existingInventory.getId(),
                "B-02-C" // warehouse actualizado
        );

        // Mocks
        when(purchaseServiceDomain.findPurchaseById(purchaseId)).thenReturn(existingPurchase);
        when(productServiceDomain.findById(updateRequest.productId())).thenReturn(product);
        when(supplierServiceDomain.findByIdSupplier(updateRequest.supplierId())).thenReturn(supplier);
        when(inventoryRepo.save(existingInventory)).thenReturn(existingInventory);
        when(purchaseRepo.save(existingPurchase)).thenReturn(updatedPurchase);
        when(purchaseMapper.toPurchaseDTOResponse(updatedPurchase)).thenReturn(expectedResponse);

        // Act
        PurchaseDTOResponse result = purchaseServiceImpl.updatePurchase(purchaseId, updateRequest);

        // Assert
        assertAll(
                () -> assertEquals(expectedResponse.id(), result.id()),
                () -> assertEquals(expectedResponse.quantity(), result.quantity()),
                () -> assertEquals(expectedResponse.unit(), result.unit()),
                () -> assertEquals(expectedResponse.costUnit(), result.costUnit()),
                () -> assertEquals(expectedResponse.totalCost(), result.totalCost()),
                () -> assertEquals(expectedResponse.lot(), result.lot()),
                () -> assertEquals(expectedResponse.expirationDate(), result.expirationDate()),
                () -> assertEquals(expectedResponse.warehouseLocation(), result.warehouseLocation()),
                () -> assertEquals(expectedResponse.arrivalDate(), result.arrivalDate()),
                () -> assertEquals(expectedResponse.purchaseDate(), result.purchaseDate()),
                () -> assertEquals(expectedResponse.invoiceNumber(), result.invoiceNumber()),
                () -> assertEquals(expectedResponse.notes(), result.notes()),
                () -> assertEquals(expectedResponse.purchaseStatus(), result.purchaseStatus()),
                () -> assertEquals(expectedResponse.productId(), result.productId()),
                () -> assertEquals(expectedResponse.productName(), result.productName()),
                () -> assertEquals(expectedResponse.supplierId(), result.supplierId()),
                () -> assertEquals(expectedResponse.supplierName(), result.supplierName()),
                () -> assertEquals(expectedResponse.inventoryId(), result.inventoryId()),
                () -> assertEquals(expectedResponse.inventoryWarehouse(), result.inventoryWarehouse())
        );

        // Verify
        InOrder inOrder = Mockito.inOrder(
                purchaseServiceDomain,
                productServiceDomain,
                supplierServiceDomain,
                inventoryRepo,
                purchaseRepo,
                purchaseMapper
        );
        inOrder.verify(purchaseServiceDomain).findPurchaseById(purchaseId);
        inOrder.verify(productServiceDomain).findById(updateRequest.productId());
        inOrder.verify(supplierServiceDomain).findByIdSupplier(updateRequest.supplierId());
        inOrder.verify(inventoryRepo).save(existingInventory);
        inOrder.verify(purchaseRepo).save(existingPurchase);
        inOrder.verify(purchaseMapper).toPurchaseDTOResponse(updatedPurchase);
        inOrder.verifyNoMoreInteractions();
    }

}
