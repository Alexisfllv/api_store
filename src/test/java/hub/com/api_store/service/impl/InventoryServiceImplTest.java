package hub.com.api_store.service.impl;

import hub.com.api_store.dto.inventory.InventoryDTOResponse;
import hub.com.api_store.entity.*;
import hub.com.api_store.mapper.InventoryMapper;
import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.nums.GlobalUnit;
import hub.com.api_store.nums.PurchaseStatus;
import hub.com.api_store.repo.InventoryRepo;
import hub.com.api_store.util.page.PageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceImplTest {

    @Mock
    private InventoryRepo inventoryRepo;

    @Mock
    private InventoryMapper inventoryMapper;

    @InjectMocks
    private InventoryServiceImpl inventoryService;


    @Nested
    class addStockFromPurchase {
        @Test
        @DisplayName("Debe crear un nuevo inventario cuando no existe duplicado")
        void shouldCreateNewInventoryWhenNoDuplicateExists() {
            // Arrange
            Category category = new Category(1L, "Alimentos", "Productos alimenticios", GlobalStatus.ACTIVE);
            Product product = new Product(1L, "Arroz Premium", GlobalUnit.KG, GlobalStatus.ACTIVE, category);

            Supplier supplier = new Supplier(
                    1L, "Fring", "+51920287650", "Fring@email.com", "Lima-Lima", GlobalStatus.ACTIVE
            );

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
                    supplier,
                    null // inventory aún no existe
            );

            // Mock: No existe inventory previo
            when(inventoryRepo.findByProductAndLotAndWarehouse(
                    purchase.getProduct(),
                    purchase.getLot(),
                    purchase.getWarehouseLocation()
            )).thenReturn(Optional.empty());

            // Mock: Simular que el save retorna un inventory con ID asignado
            when(inventoryRepo.save(any(Inventory.class))).thenAnswer(invocation -> {
                Inventory saved = invocation.getArgument(0);
                saved.setId(1L); // Simula que la BD asigna el ID
                return saved;
            });

            // Act
            Inventory result = inventoryService.addStockFromPurchase(purchase);

            // Assert
            assertAll(
                    () -> assertEquals(1L, result.getId()),
                    () -> assertEquals(purchase.getQuantity(), result.getQuantity()),
                    () -> assertEquals(purchase.getUnit(), result.getUnit()),
                    () -> assertEquals(purchase.getLot(), result.getLot()),
                    () -> assertEquals(purchase.getWarehouseLocation(), result.getWarehouse()),
                    () -> assertEquals(purchase.getExpirationDate(), result.getExpirationDate()),
                    () -> assertEquals(purchase.getProduct(), result.getProduct())
            );

            // Verify
            verify(inventoryRepo, times(1)).findByProductAndLotAndWarehouse(
                    purchase.getProduct(),
                    purchase.getLot(),
                    purchase.getWarehouseLocation()
            );

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

            Supplier supplier = new Supplier(
                    1L, "Fring", "+51920287650", "Fring@email.com", "Lima-Lima", GlobalStatus.ACTIVE
            );

            // Inventory que YA EXISTE en la BD con 50 KG
            Inventory existingInventory = new Inventory();
            existingInventory.setId(1L);
            existingInventory.setProduct(product);
            existingInventory.setQuantity(new BigDecimal("50.000"));
            existingInventory.setUnit(GlobalUnit.KG);
            existingInventory.setLot("LOT-2026-001");
            existingInventory.setWarehouse("A-01-B");
            existingInventory.setExpirationDate(LocalDateTime.of(2027, 12, 10, 14, 0, 0));

            // Nuevo purchase que aporta 100 KG al mismo lote y ubicación
            Purchase purchase = new Purchase(
                    2L,
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
                    "Segundo lote del mismo producto",
                    product,
                    supplier,
                    existingInventory
            );

            // Mock: SÍ existe inventory previo
            when(inventoryRepo.findByProductAndLotAndWarehouse(
                    purchase.getProduct(),
                    purchase.getLot(),
                    purchase.getWarehouseLocation()
            )).thenReturn(Optional.of(existingInventory));

            // Mock: El save retorna el mismo inventory actualizado
            when(inventoryRepo.save(existingInventory)).thenReturn(existingInventory);

            // Act
            Inventory result = inventoryService.addStockFromPurchase(purchase);

            // Assert - Debe tener 150 KG (50 existentes + 100 nuevos)
            assertAll(
                    () -> assertEquals(1L, result.getId()),
                    () -> assertEquals(new BigDecimal("150.000"), result.getQuantity()),
                    () -> assertEquals(GlobalUnit.KG, result.getUnit()),
                    () -> assertEquals("LOT-2026-001", result.getLot()),
                    () -> assertEquals("A-01-B", result.getWarehouse()),
                    () -> assertEquals(product, result.getProduct())
            );

            // Verify
            verify(inventoryRepo, times(1)).findByProductAndLotAndWarehouse(
                    purchase.getProduct(),
                    purchase.getLot(),
                    purchase.getWarehouseLocation()
            );

            verify(inventoryRepo, times(1)).save(argThat(inventory ->
                    inventory.getId().equals(1L) &&
                            inventory.getQuantity().equals(new BigDecimal("150.000"))
            ));
        }
    }

    @Nested
    class findAllListPageInventory {
        @Test
        @DisplayName("retonar page find all inventory")
        void findAllListPageInventorySuccess() {
            // Arrange
            int page = 0;
            int size = 10;
            String prop = "id";
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, prop));

            Category category = new Category(1L,"name","description", GlobalStatus.ACTIVE);
            Product product = new Product(1L,"name", GlobalUnit.KG,GlobalStatus.ACTIVE,category);

            Inventory inventory = new Inventory(1L,new BigDecimal(10.00),GlobalUnit.KG,
                    "A-01-B","LOT-2026-022", LocalDateTime.of(2026, 6, 30, 23, 59, 59),
                    product);

            List<Inventory> inventoryList = List.of(inventory);
            InventoryDTOResponse inventoryDTOResponse = new InventoryDTOResponse(1L,new BigDecimal(10.00),GlobalUnit.KG,
                    "A-01-B","LOT-2026-022", LocalDateTime.of(2026, 6, 30, 23, 59, 59),
                    1L,"name");
            Page<Inventory> inventoryPage = new PageImpl(inventoryList,pageable,inventoryList.size());

            when(inventoryRepo.findAll(pageable)).thenReturn(inventoryPage);
            when(inventoryMapper.toInventoryDTOResponse(inventory)).thenReturn(inventoryDTOResponse);

            // Act
            PageResponse<InventoryDTOResponse> resultList = inventoryService.findAllListPageInventory(page, size, prop);

            // Assert
            assertAll(
                    () -> assertEquals(1, resultList.content().size()),
                    () -> assertEquals(0, resultList.page()),
                    () -> assertEquals(10, resultList.size()),
                    () -> assertEquals(1, resultList.totalElements()),
                    () -> assertEquals(1, resultList.totalPages()),
                    () -> assertEquals(inventoryDTOResponse, resultList.content().get(0))
            );

            // InOrder & Verify
            InOrder inOrder = inOrder(inventoryRepo, inventoryMapper);
            inOrder.verify(inventoryRepo, times(1)).findAll(pageable);
            inOrder.verify(inventoryMapper, times(1)).toInventoryDTOResponse(inventory);
            inOrder.verifyNoMoreInteractions();
        }

        @Test
        @DisplayName("retornar page vacía cuando no hay inventarios")
        void findAllListPageInventoryEmpty() {
            // Arrange
            int page = 0;
            int size = 10;
            String prop = "id";
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, prop));

            Page<Inventory> emptyPage = new PageImpl<>(List.of(), pageable, 0);

            when(inventoryRepo.findAll(pageable)).thenReturn(emptyPage);

            // Act
            PageResponse<InventoryDTOResponse> resultList = inventoryService.findAllListPageInventory(page, size, prop);

            // Assert
            assertAll(
                    () -> assertNotNull(resultList),
                    () -> assertTrue(resultList.content().isEmpty()),
                    () -> assertEquals(0, resultList.page()),
                    () -> assertEquals(10, resultList.size()),
                    () -> assertEquals(0, resultList.totalElements()),
                    () -> assertEquals(0, resultList.totalPages())
            );

            // InOrder & Verify
            InOrder inOrder = inOrder(inventoryRepo, inventoryMapper);
            inOrder.verify(inventoryRepo, times(1)).findAll(pageable);
            inOrder.verifyNoMoreInteractions(); // el mapper nunca se llama si no hay elementos
        }
    }
}
