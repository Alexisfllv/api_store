package hub.com.api_store.service.impl;

import hub.com.api_store.dto.notification.NotificationDTOResponse;
import hub.com.api_store.entity.Notification;
import hub.com.api_store.mapper.NotificationMapper;
import hub.com.api_store.nums.NotificationType;
import hub.com.api_store.repo.NotificationRepo;
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


import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepo notificationRepo;
    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationServiceImpl notificationServiceImpl;

    @Nested
    class FindAllPageResponseNotification {

        @Test
        @DisplayName("should return page of notifications")
        void findAllPageResponseNotificationSuccess() {
            // Arrange
            int page = 0;
            int size = 10;
            String prop = "id";
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, prop));

            Notification notification = new Notification(
                    1L,
                    NotificationType.EXPIRED,
                    "message",
                    1L,
                    "productName",
                    "lot",
                    LocalDateTime.of(2026, 2, 28, 23, 59, 59),
                    3L,
                    false, LocalDateTime.of(2026, 2, 26, 8, 0, 0)
            );

            NotificationDTOResponse notificationDTOResponse = new NotificationDTOResponse(
                    1L,
                    NotificationType.EXPIRED,
                    "message",
                    1L,
                    "productName",
                    "lot",
                    LocalDateTime.of(2026, 2, 28, 23, 59, 59),
                    3L,
                    false, LocalDateTime.of(2026, 2, 26, 8, 0, 0)
            );

            List<Notification> notificationList = List.of(notification);
            Page<Notification> notificationPage = new PageImpl<>(notificationList, pageable, notificationList.size());

            when(notificationRepo.findAll(pageable)).thenReturn(notificationPage);
            when(notificationMapper.toNotificationDTOResponse(notification)).thenReturn(notificationDTOResponse);

            // Act
            PageResponse<NotificationDTOResponse> result = notificationServiceImpl
                    .findAllPageResponseNotification(page, size, prop);

            // Assert
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(1, result.content().size()),
                    () -> assertEquals(0, result.page()),
                    () -> assertEquals(10, result.size()),
                    () -> assertEquals(1, result.totalElements()),
                    () -> assertEquals(1, result.totalPages()),
                    () -> assertEquals(notificationDTOResponse, result.content().get(0))
            );

            // Verify & InOrder
            InOrder inOrder = inOrder(notificationRepo, notificationMapper);
            inOrder.verify(notificationRepo, times(1)).findAll(pageable);
            inOrder.verify(notificationMapper, times(1)).toNotificationDTOResponse(notification);
            inOrder.verifyNoMoreInteractions();
        }

        @Test
        @DisplayName("should return empty page when no notifications")
        void findAllPageResponseNotificationEmpty() {
            // Arrange
            int page = 0;
            int size = 10;
            String prop = "id";
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, prop));
            Page<Notification> emptyPage = new PageImpl<>(List.of(), pageable, 0);

            when(notificationRepo.findAll(pageable)).thenReturn(emptyPage);

            // Act
            PageResponse<NotificationDTOResponse> result = notificationServiceImpl
                    .findAllPageResponseNotification(page, size, prop);

            // Assert
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertTrue(result.content().isEmpty()),
                    () -> assertEquals(0, result.page()),
                    () -> assertEquals(10, result.size()),
                    () -> assertEquals(0, result.totalElements()),
                    () -> assertEquals(0, result.totalPages())
            );

            // Verify & InOrder
            InOrder inOrder = inOrder(notificationRepo, notificationMapper);
            inOrder.verify(notificationRepo, times(1)).findAll(pageable);
            inOrder.verifyNoMoreInteractions();
        }
    }
}