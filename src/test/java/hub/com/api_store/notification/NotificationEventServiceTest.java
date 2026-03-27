package hub.com.api_store.notification;

import hub.com.api_store.entity.Notification;
import hub.com.api_store.notification.event.ExpirationAlertEvent;
import hub.com.api_store.notification.service.NotificationEventService;
import hub.com.api_store.nums.NotificationType;
import hub.com.api_store.repo.NotificationRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationEventServiceTest {

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;
    @Mock
    private NotificationRepo notificationRepo;
    @Mock
    private Clock clock;

    @InjectMocks
    private NotificationEventService notificationEventService;

    private LocalDateTime fixedNow;

    @BeforeEach
    void setUp() {
        fixedNow = LocalDateTime.of(2026, 2, 26, 8, 0, 0);
        lenient().when(clock.instant()).thenReturn(fixedNow.toInstant(ZoneOffset.UTC));
        lenient().when(clock.getZone()).thenReturn(ZoneOffset.UTC);
    }

    @Nested
    class SendExpirationAlert {

        @Test
        @DisplayName("should save and send when EXPIRING and no duplicate")
        void shouldSaveAndSendWhenExpiringAndNoDuplicate() {
            // Arrange
            ExpirationAlertEvent event = new ExpirationAlertEvent(
                    9L, "Arroz Blanco Premium", "LOT-2026-TEST-001",
                    LocalDateTime.of(2026, 2, 28, 23, 59, 59), 2L
            );

            when(notificationRepo.existsByInventoryIdAndTypeAndDaysUntilExpiration(
                    9L, NotificationType.EXPIRING, 2L
            )).thenReturn(false);

            // Act
            notificationEventService.sendExpirationAlert(event);

            // Assert & Verify
            InOrder inOrder = inOrder(notificationRepo, simpMessagingTemplate);
            inOrder.verify(notificationRepo).existsByInventoryIdAndTypeAndDaysUntilExpiration(
                    9L, NotificationType.EXPIRING, 2L);
            inOrder.verify(notificationRepo).save(any(Notification.class));
            inOrder.verify(simpMessagingTemplate).convertAndSend(
                    eq("/topic/expiration-alerts"), eq(event));
            inOrder.verifyNoMoreInteractions();
        }

        @Test
        @DisplayName("should not save nor send when EXPIRING and duplicate exists")
        void shouldNotSaveNorSendWhenExpiringAndDuplicateExists() {
            // Arrange
            ExpirationAlertEvent event = new ExpirationAlertEvent(
                    9L, "Arroz Blanco Premium", "LOT-2026-TEST-001",
                    LocalDateTime.of(2026, 2, 28, 23, 59, 59), 2L
            );

            when(notificationRepo.existsByInventoryIdAndTypeAndDaysUntilExpiration(
                    9L, NotificationType.EXPIRING, 2L
            )).thenReturn(true);

            // Act
            notificationEventService.sendExpirationAlert(event);

            // Verify - no guarda ni envía
            InOrder inOrder = inOrder(notificationRepo, simpMessagingTemplate);
            inOrder.verify(notificationRepo).existsByInventoryIdAndTypeAndDaysUntilExpiration(
                    9L, NotificationType.EXPIRING, 2L);
            inOrder.verifyNoMoreInteractions();
        }

        @Test
        @DisplayName("should save and send when EXPIRED and no duplicate")
        void shouldSaveAndSendWhenExpiredAndNoDuplicate() {
            // Arrange
            ExpirationAlertEvent event = new ExpirationAlertEvent(
                    9L, "Arroz Blanco Premium", "LOT-2026-TEST-001",
                    LocalDateTime.of(2026, 2, 20, 23, 59, 59), -6L
            );

            when(notificationRepo.existsByInventoryIdAndTypeAndDaysUntilExpiration(
                    9L, NotificationType.EXPIRED, -6L
            )).thenReturn(false);

            // Act
            notificationEventService.sendExpirationAlert(event);

            // Verify
            InOrder inOrder = inOrder(notificationRepo, simpMessagingTemplate);
            inOrder.verify(notificationRepo).existsByInventoryIdAndTypeAndDaysUntilExpiration(
                    9L, NotificationType.EXPIRED, -6L);
            inOrder.verify(notificationRepo).save(any(Notification.class));
            inOrder.verify(simpMessagingTemplate).convertAndSend(
                    eq("/topic/expiration-alerts"), eq(event));
            inOrder.verifyNoMoreInteractions();
        }

        @Test
        @DisplayName("should not save nor send when EXPIRED and duplicate exists")
        void shouldNotSaveNorSendWhenExpiredAndDuplicateExists() {
            // Arrange
            ExpirationAlertEvent event = new ExpirationAlertEvent(
                    9L, "Arroz Blanco Premium", "LOT-2026-TEST-001",
                    LocalDateTime.of(2026, 2, 20, 23, 59, 59), -6L
            );

            when(notificationRepo.existsByInventoryIdAndTypeAndDaysUntilExpiration(
                    9L, NotificationType.EXPIRED, -6L
            )).thenReturn(true);

            // Act
            notificationEventService.sendExpirationAlert(event);

            // Verify
            InOrder inOrder = inOrder(notificationRepo, simpMessagingTemplate);
            inOrder.verify(notificationRepo).existsByInventoryIdAndTypeAndDaysUntilExpiration(
                    9L, NotificationType.EXPIRED, -6L);
            inOrder.verifyNoMoreInteractions();
        }
    }
}