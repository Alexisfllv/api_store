package hub.com.api_store.controller;

import hub.com.api_store.dto.notification.NotificationDTOResponse;
import hub.com.api_store.service.NotificationService;
import hub.com.api_store.util.page.PageResponse;
import hub.com.api_store.util.response.GenericResponse;
import hub.com.api_store.util.response.StatusApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/page")
    public ResponseEntity<GenericResponse<PageResponse<NotificationDTOResponse>>> findAllPageResponseNotificationGet(
            @RequestParam (defaultValue = "0") Integer page,
            @RequestParam (defaultValue = "10") Integer limit,
            @RequestParam (defaultValue = "id") String prop){
        PageResponse<NotificationDTOResponse> pageResponse = notificationService.findAllPageResponseNotification(page, limit, prop);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponse<>(StatusApi.SUCCESS, pageResponse));

    }
}
