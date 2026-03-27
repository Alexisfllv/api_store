package hub.com.api_store.service.impl;

import hub.com.api_store.dto.notification.NotificationDTOResponse;
import hub.com.api_store.entity.Notification;
import hub.com.api_store.mapper.NotificationMapper;
import hub.com.api_store.repo.NotificationRepo;
import hub.com.api_store.service.NotificationService;
import hub.com.api_store.util.page.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {


    private final NotificationMapper notificationMapper;

    private final NotificationRepo notificationRepo;

    @Override
    public PageResponse<NotificationDTOResponse> findAllPageResponseNotification(int page, int size,String prop) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC,prop));
        Page<Notification> notificationPage = notificationRepo.findAll(pageable);
        return new PageResponse<>(
                notificationPage.getContent()
                        .stream()
                        .map(notificationMapper::toNotificationDTOResponse)
                        .toList(),
                notificationPage.getNumber(),
                notificationPage.getSize(),
                notificationPage.getTotalElements(),
                notificationPage.getTotalPages()
        );
    }

}
