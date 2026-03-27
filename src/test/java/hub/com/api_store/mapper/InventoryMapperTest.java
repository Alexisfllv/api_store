package hub.com.api_store.mapper;

import hub.com.api_store.dto.inventory.InventoryDTOResponse;
import hub.com.api_store.entity.Category;
import hub.com.api_store.entity.Inventory;
import hub.com.api_store.entity.Product;
import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.nums.GlobalUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryMapperTest {

    private InventoryMapper inventoryMapper;

     @BeforeEach
     void setUp() {
         inventoryMapper = new InventoryMapper();
     }

     @Test
     @DisplayName("Test mapped response <- entity")
    void toResponse(){
         // Arrange
         Category category = new Category(1L,"name","description", GlobalStatus.ACTIVE);
         Product product = new Product(1L,"name", GlobalUnit.KG,GlobalStatus.ACTIVE,category);

         Inventory inventory = new Inventory(1L,new BigDecimal(10.00),GlobalUnit.KG,
                 "A-01-B","LOT-2026-022", LocalDateTime.of(2026, 6, 30, 23, 59, 59),
                 product);
         // Act
         InventoryDTOResponse inventoryDTOResponse = inventoryMapper.toInventoryDTOResponse(inventory);

         // Assert
         assertAll(
                 () -> assertNotNull(inventoryDTOResponse),
                 () -> assertEquals(1L, inventoryDTOResponse.id()),
                 () -> assertEquals(new BigDecimal(10.00), inventoryDTOResponse.quantity()),
                 () -> assertEquals(GlobalUnit.KG, inventoryDTOResponse.unit()),
                 () -> assertEquals("A-01-B", inventoryDTOResponse.warehouse()),
                 () -> assertEquals("LOT-2026-022", inventoryDTOResponse.lot()),
                 () -> assertEquals(LocalDateTime.of(2026, 6, 30, 23, 59, 59), inventoryDTOResponse.expirationDate()),
                 () -> assertEquals(1L, inventoryDTOResponse.productId()),
                 () -> assertEquals("name", inventoryDTOResponse.productName())
         );
     }
}
