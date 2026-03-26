package hub.com.api_store.service.impl;

import hub.com.api_store.dto.waste.WasteDTORequest;
import hub.com.api_store.dto.waste.WasteDTOResponse;
import hub.com.api_store.entity.Category;
import hub.com.api_store.entity.Inventory;
import hub.com.api_store.entity.Product;
import hub.com.api_store.entity.Waste;
import hub.com.api_store.mapper.WasteMapper;
import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.nums.GlobalUnit;
import hub.com.api_store.nums.WasteReason;
import hub.com.api_store.repo.InventoryRepo;
import hub.com.api_store.repo.WasteRepo;
import hub.com.api_store.service.domain.InventoryServiceDomain;
import hub.com.api_store.service.domain.WasteServiceDomain;
import hub.com.api_store.util.page.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WasteServiceImplTest {

    @Mock
    private WasteMapper wasteMapper;

    @Mock
    private WasteRepo wasteRepo;

    @Mock
    private InventoryRepo inventoryRepo;

    @Mock
    private WasteServiceDomain wasteServiceDomain;

    @Mock
    private InventoryServiceDomain inventoryServiceDomain;

    @Mock
    private Clock clock;

    @InjectMocks
    private WasteServiceImpl wasteServiceImpl;

    private LocalDateTime fixedNow;

    @BeforeEach
    void setUp() {
        fixedNow = LocalDateTime.of(2026, Month.MARCH, 24, 0, 0);
        lenient().when(clock.instant()).thenReturn(fixedNow.atZone(Clock.systemDefaultZone().getZone()).toInstant());
        lenient().when(clock.getZone()).thenReturn(Clock.systemDefaultZone().getZone());
    }

    // GET
    @Test
    @DisplayName("findAllPage")
    void findAllPage() {
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

        Waste waste = new Waste(1L, new BigDecimal(5.00), GlobalUnit.KG, WasteReason.EXPIRED,
                "notes", fixedNow, inventory);
        List<Waste> wasteList = List.of(waste);

        WasteDTOResponse wasteDTOResponse = new WasteDTOResponse(
                1L,
                new BigDecimal(5.00),
                GlobalUnit.KG,
                WasteReason.EXPIRED,
                "notes",
                fixedNow,
                1L,
                "LOT-2026-022",
                "A-01-B",
                1L,
                "name"
        );
        Page<Waste> wastePage = new PageImpl<>(wasteList,pageable,wasteList.size());

        when(wasteRepo.findAll(pageable)).thenReturn(wastePage);
        when(wasteMapper.toWasteDTOResponse(waste)).thenReturn(wasteDTOResponse);

        // Act
        PageResponse<WasteDTOResponse> resultList = wasteServiceImpl.findAllPage(page, size, prop);

        // Assert
        assertAll(
                () -> assertNotNull(resultList),
                () -> assertEquals(1, resultList.content().size()),
                () -> assertEquals(wasteDTOResponse, resultList.content().get(0)),
                () -> assertEquals(0, resultList.page()),
                () -> assertEquals(10, resultList.size()),
                () -> assertEquals(1, resultList.totalElements()),
                () -> assertEquals(1, resultList.totalPages())
        );

        // InOrder & Verify
        InOrder inOrder = inOrder(wasteRepo, wasteMapper);
        inOrder.verify(wasteRepo, times(1)).findAll(pageable);
        inOrder.verify(wasteMapper, times(1)).toWasteDTOResponse(waste);
        inOrder.verifyNoMoreInteractions();

    }

    @Test
    @DisplayName("findByIdWaste")
    void findByIdWaste() {
        // Arrange
        Long id = 1L;
        Category category = new Category(1L,"name","description", GlobalStatus.ACTIVE);
        Product product = new Product(1L,"name", GlobalUnit.KG,GlobalStatus.ACTIVE,category);

        Inventory inventory = new Inventory(1L,new BigDecimal(10.00),GlobalUnit.KG,
                "A-01-B","LOT-2026-022", LocalDateTime.of(2026, 6, 30, 23, 59, 59),
                product);

        Waste waste = new Waste(1L, new BigDecimal(5.00), GlobalUnit.KG, WasteReason.EXPIRED,
                "notes", fixedNow, inventory);
        WasteDTOResponse wasteDTOResponse = new WasteDTOResponse(
                1L,
                new BigDecimal(5.00),
                GlobalUnit.KG,
                WasteReason.EXPIRED,
                "notes",
                LocalDateTime.of(2026, 6, 30, 23, 59, 59),
                1L,
                "LOT-2026-022",
                "A-01-B",
                1L,
                "name"
        );

        when(wasteServiceDomain.findById(id)).thenReturn(waste);
        when(wasteMapper.toWasteDTOResponse(waste)).thenReturn(wasteDTOResponse);

        // Act
        WasteDTOResponse result = wasteServiceImpl.findByIdWaste(id);
        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(wasteDTOResponse, result),
                () -> assertEquals(wasteDTOResponse.id(), result.id()),
                () -> assertEquals(wasteDTOResponse.quantity(), result.quantity()),
                () -> assertEquals(wasteDTOResponse.unit(), result.unit()),
                () -> assertEquals(wasteDTOResponse.reason(), result.reason()),
                () -> assertEquals(wasteDTOResponse.notes(), result.notes()),
                () -> assertEquals(wasteDTOResponse.wasteDate(), result.wasteDate()),
                () -> assertEquals(wasteDTOResponse.inventoryId(), result.inventoryId()),
                () -> assertEquals(wasteDTOResponse.inventoryLot(), result.inventoryLot()),
                () -> assertEquals(wasteDTOResponse.inventoryWarehouse(), result.inventoryWarehouse()),
                () -> assertEquals(wasteDTOResponse.productId(), result.productId()),
                () -> assertEquals(wasteDTOResponse.productName(), result.productName())
        );

        // InOrder & Verify
        InOrder inOrder = inOrder(wasteServiceDomain, wasteMapper);
        inOrder.verify(wasteServiceDomain, times(1)).findById(id);
        inOrder.verify(wasteMapper, times(1)).toWasteDTOResponse(waste);
        inOrder.verifyNoMoreInteractions();
    }


    @Test
    @DisplayName("findAllWasteByReason")
    void findAllWasteByReason() {
        // Arrange
        WasteReason reason = WasteReason.EXPIRED;

        Category category = new Category(1L, "name", "description", GlobalStatus.ACTIVE);
        Product product = new Product(1L, "name", GlobalUnit.KG, GlobalStatus.ACTIVE, category);
        Inventory inventory = new Inventory(1L, new BigDecimal("10.000"), GlobalUnit.KG,
                "A-01-B", "LOT-2026-022", LocalDateTime.of(2026, 6, 30, 23, 59, 59), product);

        Waste waste1 = new Waste(1L, new BigDecimal("5.000"), GlobalUnit.KG, WasteReason.EXPIRED,
                "notes1", fixedNow, inventory);
        Waste waste2 = new Waste(2L, new BigDecimal("10.000"), GlobalUnit.KG, WasteReason.EXPIRED,
                "notes2", fixedNow, inventory);

        WasteDTOResponse response1 = new WasteDTOResponse(1L, new BigDecimal("5.000"), GlobalUnit.KG,
                WasteReason.EXPIRED, "notes1", fixedNow, 1L, "LOT-2026-022", "A-01-B", 1L, "name");
        WasteDTOResponse response2 = new WasteDTOResponse(2L, new BigDecimal("10.000"), GlobalUnit.KG,
                WasteReason.EXPIRED, "notes2", fixedNow, 1L, "LOT-2026-022", "A-01-B", 1L, "name");

        List<Waste> wasteList = List.of(waste1, waste2);
        List<WasteDTOResponse> expectedList = List.of(response1, response2);

        when(wasteRepo.findByReason(reason)).thenReturn(wasteList);
        when(wasteMapper.toWasteDTOResponse(waste1)).thenReturn(response1);
        when(wasteMapper.toWasteDTOResponse(waste2)).thenReturn(response2);

        // Act
        List<WasteDTOResponse> result = wasteServiceImpl.findAllWasteByReason(reason);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertEquals(expectedList, result),
                () -> assertEquals(WasteReason.EXPIRED, result.get(0).reason()),
                () -> assertEquals(WasteReason.EXPIRED, result.get(1).reason())
        );

        // InOrder & Verify
        InOrder inOrder = inOrder(wasteRepo, wasteMapper);
        inOrder.verify(wasteRepo, times(1)).findByReason(reason);
        inOrder.verify(wasteMapper, times(1)).toWasteDTOResponse(waste1);
        inOrder.verify(wasteMapper, times(1)).toWasteDTOResponse(waste2);
        inOrder.verifyNoMoreInteractions();
    }

    // POST
    @Test
    @DisplayName("createWaste")
    void createWaste() {
        // Arrange
        BigDecimal originalQuantity = new BigDecimal("80.000");

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

        Waste waste = new Waste();
        waste.setQuantity(request.quantity());
        waste.setUnit(request.unit());
        waste.setReason(request.reason());
        waste.setNotes(request.notes());
        waste.setInventory(inventory);
        waste.setWasteDate(fixedNow);

        Waste wasteSaved = new Waste();
        wasteSaved.setId(1L);
        wasteSaved.setQuantity(request.quantity());
        wasteSaved.setUnit(request.unit());
        wasteSaved.setReason(request.reason());
        wasteSaved.setNotes(request.notes());
        wasteSaved.setInventory(inventory);
        wasteSaved.setWasteDate(fixedNow);

        WasteDTOResponse wasteDTOResponse = new WasteDTOResponse(
                1L,
                new BigDecimal("10.000"),
                GlobalUnit.KG,
                WasteReason.EXPIRED,
                "notes",
                fixedNow,
                1L,
                "LOT-2026-001",
                "A-01-A",
                1L,
                "Arroz Blanco Premium"
        );

        when(inventoryServiceDomain.findById(1L)).thenReturn(inventory);
        when(wasteMapper.toWaste(request, inventory)).thenReturn(waste);
        when(inventoryRepo.save(inventory)).thenReturn(inventory);
        when(wasteRepo.save(waste)).thenReturn(wasteSaved);
        when(wasteMapper.toWasteDTOResponse(wasteSaved)).thenReturn(wasteDTOResponse);

        // Act
        WasteDTOResponse result = wasteServiceImpl.createWaste(request);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(wasteDTOResponse, result),
                () -> assertEquals(wasteDTOResponse.id(), result.id()),
                () -> assertEquals(wasteDTOResponse.quantity(), result.quantity()),
                () -> assertEquals(wasteDTOResponse.unit(), result.unit()),
                () -> assertEquals(wasteDTOResponse.reason(), result.reason()),
                () -> assertEquals(wasteDTOResponse.notes(), result.notes()),
                () -> assertEquals(wasteDTOResponse.wasteDate(), result.wasteDate()),
                () -> assertEquals(wasteDTOResponse.inventoryId(), result.inventoryId()),
                () -> assertEquals(wasteDTOResponse.inventoryLot(), result.inventoryLot()),
                () -> assertEquals(wasteDTOResponse.inventoryWarehouse(), result.inventoryWarehouse()),
                () -> assertEquals(wasteDTOResponse.productId(), result.productId()),
                () -> assertEquals(wasteDTOResponse.productName(), result.productName())
        );

        // Verify & InOrder
        InOrder inOrder = inOrder(inventoryServiceDomain, wasteServiceDomain, wasteMapper, inventoryRepo, wasteRepo);
        inOrder.verify(inventoryServiceDomain, times(1)).findById(1L);
        inOrder.verify(wasteServiceDomain, times(1)).validateStock(originalQuantity, request.quantity());
        inOrder.verify(wasteMapper, times(1)).toWaste(request, inventory);
        inOrder.verify(inventoryRepo, times(1)).save(inventory);
        inOrder.verify(wasteRepo, times(1)).save(waste);
        inOrder.verify(wasteMapper, times(1)).toWasteDTOResponse(wasteSaved);
        inOrder.verifyNoMoreInteractions();
    }


    // DELETE
    @Test
    @DisplayName("deleteWaste")
    void deleteWaste() {

        // Arrange
        Long id = 1L;
        Category category = new Category(1L, "name", "description", GlobalStatus.ACTIVE);
        Product product = new Product(1L, "name", GlobalUnit.KG, GlobalStatus.ACTIVE, category);

        Inventory inventory = new Inventory(1L, new BigDecimal("10.00"), GlobalUnit.KG,
                "A-01-B", "LOT-2026-022", LocalDateTime.of(2026, 6, 30, 23, 59, 59),
                product);

        Waste waste = new Waste(1L, new BigDecimal("5.00"), GlobalUnit.KG, WasteReason.EXPIRED,
                "notes", fixedNow, inventory);

        when(wasteServiceDomain.findById(id)).thenReturn(waste);

        // Act
        wasteServiceImpl.deleteWaste(id);

        // Assert
        InOrder inOrder = inOrder(inventoryRepo, wasteRepo);
        inOrder.verify(inventoryRepo).save(inventory);
        inOrder.verify(wasteRepo).delete(waste);
        assertEquals(0, new BigDecimal("15.00").compareTo(inventory.getQuantity()));
    }
}
