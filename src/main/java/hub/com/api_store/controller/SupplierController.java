package hub.com.api_store.controller;

import hub.com.api_store.dto.supplier.SupplierDTOResponse;
import hub.com.api_store.service.SupplierService;
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
@RequestMapping("/api/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<SupplierDTOResponse>> getSupplierIdGet(@PathVariable Long id){
        SupplierDTOResponse response = supplierService.getSupplierId(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, response)
        );
    }
}
