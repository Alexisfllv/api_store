package hub.com.api_store.mapper;

import hub.com.api_store.dto.waste.WasteDTORequest;
import hub.com.api_store.dto.waste.WasteDTOResponse;
import hub.com.api_store.entity.Category;
import hub.com.api_store.entity.Inventory;
import hub.com.api_store.entity.Product;
import hub.com.api_store.entity.Waste;
import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.nums.GlobalUnit;
import hub.com.api_store.nums.WasteReason;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class WasteMapperTest {

    private WasteMapper wasteMapper;

    @BeforeEach
    void setUp(){
        wasteMapper = new WasteMapper();
    }

    @Test
    @DisplayName("toWasteDTOResponse")
    void toWasteDTOResponse(){
        // Arrange
        Category category = new Category(1L, "name", "description", GlobalStatus.ACTIVE);
        Product product = new Product(1L, "Arroz Blanco Premium", GlobalUnit.KG, GlobalStatus.ACTIVE, category);
        Inventory inventory = new Inventory(1L, new BigDecimal("80.000"), GlobalUnit.KG,
                "A-01-A", "LOT-2026-001",
                LocalDateTime.of(2027, 6, 30, 23, 59, 59), product);

        Waste waste = new Waste();
        waste.setId(1L);
        waste.setQuantity(new BigDecimal("10.000"));
        waste.setUnit(GlobalUnit.KG);
        waste.setReason(WasteReason.EXPIRED);
        waste.setNotes("notes");
        waste.setWasteDate(LocalDateTime.of(2026, 3, 2, 8, 0, 0));
        waste.setInventory(inventory);

        // Act
        WasteDTOResponse result = wasteMapper.toWasteDTOResponse(waste);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(waste.getId(), result.id()),
                () -> assertEquals(waste.getQuantity(), result.quantity()),
                () -> assertEquals(waste.getUnit(), result.unit()),
                () -> assertEquals(waste.getReason(), result.reason()),
                () -> assertEquals(waste.getNotes(), result.notes()),
                () -> assertEquals(waste.getWasteDate(), result.wasteDate()),
                () -> assertEquals(inventory.getId(), result.inventoryId()),
                () -> assertEquals(inventory.getLot(), result.inventoryLot()),
                () -> assertEquals(inventory.getWarehouse(), result.inventoryWarehouse()),
                () -> assertEquals(product.getId(), result.productId()),
                () -> assertEquals(product.getName(), result.productName())
        );
    }

    @Test
    @DisplayName("toWaste")
    void toWaste(){
        // Arrange
        Category category = new Category(1L, "name", "description", GlobalStatus.ACTIVE);
        Product product = new Product(1L, "Arroz Blanco Premium", GlobalUnit.KG, GlobalStatus.ACTIVE, category);
        Inventory inventory = new Inventory(1L, new BigDecimal("80.000"), GlobalUnit.KG,
                "A-01-A", "LOT-2026-001",
                LocalDateTime.of(2027, 6, 30, 23, 59, 59), product);

        WasteDTORequest request = new WasteDTORequest(
                new BigDecimal("10.000"),
                GlobalUnit.KG,
                WasteReason.EXPIRED,
                "notes",
                1L
        );

        // Act
        Waste result = wasteMapper.toWaste(request, inventory);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(request.quantity(), result.getQuantity()),
                () -> assertEquals(request.unit(), result.getUnit()),
                () -> assertEquals(request.reason(), result.getReason()),
                () -> assertEquals(request.notes(), result.getNotes()),
                () -> assertEquals(inventory, result.getInventory()),
                () -> assertNull(result.getWasteDate()) // wasteDate se asigna en el service
        );
    }
}
