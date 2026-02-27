package hub.com.api_store.controller;

import hub.com.api_store.dto.inventory.InventoryDTOResponse;
import hub.com.api_store.dto.inventory.InventoryTotalStockDTOResponse;
import hub.com.api_store.service.InventoryService;
import hub.com.api_store.util.page.PageResponse;
import hub.com.api_store.util.response.GenericResponse;
import hub.com.api_store.util.response.StatusApi;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventories")
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<InventoryDTOResponse>> findInventoryByIdGet(@PathVariable Long id){
        InventoryDTOResponse response = inventoryService.findInventoryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, response)
        );
    }

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

    @GetMapping("/product/{productId}")
    public ResponseEntity<GenericResponse<java.util.List<InventoryDTOResponse>>> findAllListInventoryByProductGet(
            @PathVariable Long productId,
            @RequestParam (defaultValue = "10") int limit
    ){
        List<InventoryDTOResponse> response= inventoryService.findAllListInventoryByProduct(productId, limit);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, response)
        );
    }

    @GetMapping("/lot/{lot}")
    public ResponseEntity<GenericResponse<List<InventoryDTOResponse>>> findAllListInventoryByLotGet(
            @PathVariable String lot,
            @RequestParam (defaultValue = "10") int limit
    ) {
        List<InventoryDTOResponse> response = inventoryService.findAllListInventoryByLot(lot, limit);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, response)
        );
    }

    @GetMapping("/warehouse/{warehouse}")
    public ResponseEntity<GenericResponse<List<InventoryDTOResponse>>> findAllListInventoryByWarehouseGet(
            @PathVariable String warehouse,
            @RequestParam (defaultValue = "10") int limit
    ) {
        List<InventoryDTOResponse> response = inventoryService.findAllListInventoryByWarehouse(warehouse, limit);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, response)
        );
    }

    @GetMapping("/available/product/{productId}")
    public ResponseEntity<GenericResponse<List<InventoryDTOResponse>>> findAvailableInventoryByProductGet(
            @PathVariable Long productId){
        List<InventoryDTOResponse> response = inventoryService.findAvailableInventoryByProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, response)
        );
    }

    @GetMapping("/available")
    public ResponseEntity<GenericResponse<List<InventoryDTOResponse>>> findAvailableInventoryGet() {
        List<InventoryDTOResponse> response = inventoryService.findAvailableInventory();
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, response)
        );
    }

    @GetMapping("/expiration/range")
    public ResponseEntity<GenericResponse<List<InventoryDTOResponse>>> findInventoryExpiringBetweenGet(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        List<InventoryDTOResponse> response = inventoryService.findInventoryExpiringBetween(start, end);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, response)
        );
    }

    @GetMapping("/expiration")
    public ResponseEntity<GenericResponse<List<InventoryDTOResponse>>> findInventoryExpirationGet() {
        List<InventoryDTOResponse> response = inventoryService.findInventoryExpiration();
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, response)
        );
    }

    @GetMapping("/stock/product/{productId}")
    public ResponseEntity<GenericResponse<InventoryTotalStockDTOResponse>> findTotalStockByProductIdGet(@PathVariable Long productId) {
        InventoryTotalStockDTOResponse response = inventoryService.findTotalStockByProductId(productId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, response)
        );
    }
}
