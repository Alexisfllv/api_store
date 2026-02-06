package hub.com.api_store.controller;

import hub.com.api_store.dto.supplier.SupplierDTORequest;
import hub.com.api_store.dto.supplier.SupplierDTOResponse;
import hub.com.api_store.dto.supplier.SupplierDTOUpdate;
import hub.com.api_store.service.SupplierService;
import hub.com.api_store.util.page.PageResponse;
import hub.com.api_store.util.response.GenericResponse;
import hub.com.api_store.util.response.StatusApi;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    // GET
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<SupplierDTOResponse>> getSupplierIdGet(@PathVariable Long id){
        SupplierDTOResponse response = supplierService.getSupplierId(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, response)
        );
    }

    @GetMapping("/page")
    public ResponseEntity<GenericResponse<PageResponse<SupplierDTOResponse>>> getPageListSupplierGet(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "3")int size){
        PageResponse<SupplierDTOResponse> response = supplierService.getPageListSupplier(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, response)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<GenericResponse<List<SupplierDTOResponse>>> getListSupplierByNameGet(
            @RequestParam(defaultValue = "abcd")
            @NotBlank(message = "{field.required}")
            @Size(min = 2, max = 100, message = "{field.size.range}")
            String name,

            @RequestParam(defaultValue = "50")
            @Positive(message = "{field.must.be.positive}")
            int limit){
        List<SupplierDTOResponse> response = supplierService.getListSupplierByName(name, limit);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS,response)
        );
    }

    // POST
    @PostMapping
    public ResponseEntity<GenericResponse<SupplierDTOResponse>> addSupplierPost(@Valid @RequestBody SupplierDTORequest supplierDTORequest){
        SupplierDTOResponse response = supplierService.addSupplier(supplierDTORequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new GenericResponse<>(StatusApi.CREATED, response)
        );
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<SupplierDTOResponse>> updateSupplierPut(
            @PathVariable Long id, @Valid @RequestBody SupplierDTOUpdate supplierDTOUpdate){
        SupplierDTOResponse response = supplierService.updateSupplier(id, supplierDTOUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.UPDATED, response)
        );
    }

    // DELETE
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<Void> deleteSupplierDelete(@PathVariable Long id){
        supplierService.deleteSupplier(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
