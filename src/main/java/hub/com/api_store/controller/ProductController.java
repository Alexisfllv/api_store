package hub.com.api_store.controller;

import hub.com.api_store.dto.product.ProductDTOResponse;
import hub.com.api_store.service.ProductService;
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
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<ProductDTOResponse>> getProductByIdGet(@PathVariable Long id){
        ProductDTOResponse productDTOResponse = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, productDTOResponse)
        );
    }
}
