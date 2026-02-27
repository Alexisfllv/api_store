package hub.com.api_store.service;


import hub.com.api_store.dto.notification.NotificationDTOResponse;
import hub.com.api_store.util.page.PageResponse;

import java.util.List;

public interface NotificationService {

    PageResponse<NotificationDTOResponse> findAllPageResponseNotification(int page, int size,String prop);
}
