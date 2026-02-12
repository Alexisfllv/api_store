package hub.com.api_store.controller;

import hub.com.api_store.dto.purchase.PurchaseDTOResponse;
import hub.com.api_store.service.PurchaseService;
import hub.com.api_store.util.response.GenericResponse;
import hub.com.api_store.util.response.StatusApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<PurchaseDTOResponse>> findByPurchaseIdGet(@PathVariable Long id){
        PurchaseDTOResponse purchaseDTOResponse = purchaseService.findByPurchaseId(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, purchaseDTOResponse)
        );
    }

}
