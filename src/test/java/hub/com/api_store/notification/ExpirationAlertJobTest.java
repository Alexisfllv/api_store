package hub.com.api_store.notification;

import hub.com.api_store.entity.Category;
import hub.com.api_store.entity.Inventory;
import hub.com.api_store.entity.Product;
import hub.com.api_store.job.inventory.ExpirationAlertJob;
import hub.com.api_store.notification.event.ExpirationAlertEvent;
import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.nums.GlobalUnit;
import hub.com.api_store.repo.InventoryRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpirationAlertJobTest {

    @Mock
    private InventoryRepo inventoryRepo;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @Mock
    private Clock clock;

    @InjectMocks private ExpirationAlertJob expirationAlertJob;

    private LocalDateTime fixedNow;

    @BeforeEach
    void setUp() {
        fixedNow = LocalDateTime.of(2026, 2, 26, 8, 0, 0);
        lenient().when(clock.instant()).thenReturn(fixedNow.toInstant(ZoneOffset.UTC));
        lenient().when(clock.getZone()).thenReturn(ZoneOffset.UTC);
    }

    @Nested
    class CheckExpiringInventories {

        @Test
        @DisplayName("should publish event for inventory expiring in 3 days")
        void shouldPublishEventForInventoryExpiringIn3Days() {
            // Arrange
            Category category = new Category(1L, "name", "description", GlobalStatus.ACTIVE);
            Product product = new Product(1L, "Arroz Blanco Premium", GlobalUnit.KG, GlobalStatus.ACTIVE, category);
            Inventory inventory = new Inventory(9L, new BigDecimal("10.000"), GlobalUnit.KG,
                    "A-01-A", "LOT-2026-TEST-001",
                    LocalDateTime.of(2026, 2, 28, 23, 59, 59), product);

            when(inventoryRepo.findByExpirationDateBetweenOrderByExpirationDateAsc(
                    eq(fixedNow), eq(fixedNow.plusDays(3))
            )).thenReturn(List.of(inventory));

            when(inventoryRepo.findByExpirationDateBetweenOrderByExpirationDateAsc(
                    eq(fixedNow), eq(fixedNow.plusDays(2))
            )).thenReturn(List.of());

            when(inventoryRepo.findByExpirationDateBetweenOrderByExpirationDateAsc(
                    eq(fixedNow), eq(fixedNow.plusDays(1))
            )).thenReturn(List.of());

            when(inventoryRepo.findByExpirationDateBeforeOrderByExpirationDateAsc(
                    eq(fixedNow)
            )).thenReturn(List.of());

            // Act
            expirationAlertJob.checkExpiringInventories();

            // Verify
            verify(applicationEventPublisher, times(1)).publishEvent(
                    new ExpirationAlertEvent(
                            9L,
                            "Arroz Blanco Premium",
                            "LOT-2026-TEST-001",
                            LocalDateTime.of(2026, 2, 28, 23, 59, 59),
                            3L
                    )
            );
        }

        @Test
        @DisplayName("should publish event for expired inventory")
        void shouldPublishEventForExpiredInventory() {
            // Arrange
            Category category = new Category(1L, "name", "description", GlobalStatus.ACTIVE);
            Product product = new Product(1L, "Manzanas Red Delicious", GlobalUnit.KG, GlobalStatus.ACTIVE, category);
            Inventory inventory = new Inventory(6L, new BigDecimal("60.000"), GlobalUnit.KG,
                    "C-01-A", "LOT-2026-020",
                    LocalDateTime.of(2026, 2, 20, 23, 59, 59), product);

            when(inventoryRepo.findByExpirationDateBetweenOrderByExpirationDateAsc(
                    any(), any())).thenReturn(List.of());

            when(inventoryRepo.findByExpirationDateBeforeOrderByExpirationDateAsc(
                    eq(fixedNow)
            )).thenReturn(List.of(inventory));

            // Act
            expirationAlertJob.checkExpiringInventories();

            // Verify
            verify(applicationEventPublisher, times(1)).publishEvent(
                    new ExpirationAlertEvent(
                            6L,
                            "Manzanas Red Delicious",
                            "LOT-2026-020",
                            LocalDateTime.of(2026, 2, 20, 23, 59, 59),
                            -5L
                    )
            );
        }

        @Test
        @DisplayName("should not publish event when no inventories found")
        void shouldNotPublishEventWhenNoInventoriesFound() {
            // Arrange
            when(inventoryRepo.findByExpirationDateBetweenOrderByExpirationDateAsc(
                    any(), any())).thenReturn(List.of());
            when(inventoryRepo.findByExpirationDateBeforeOrderByExpirationDateAsc(
                    any())).thenReturn(List.of());

            // Act
            expirationAlertJob.checkExpiringInventories();

            // Verify
            verify(applicationEventPublisher, never()).publishEvent(any());
        }
    }
}
