package hub.com.api_store.controller;

import hub.com.api_store.dto.inventory.InventoryDTOResponse;
import hub.com.api_store.service.InventoryService;
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
@RequestMapping("/api/inventories")
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/page")
    public ResponseEntity<GenericResponse<PageResponse<InventoryDTOResponse>>> findAllListPageInventoryGet(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size,
            @RequestParam (defaultValue = "id") String prop
    ){
        PageResponse<InventoryDTOResponse> response= inventoryService.findAllListPageInventory(page, size, prop);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, response)
        );
    }
}
