package hub.com.api_store.controller;

import hub.com.api_store.dto.product.ProductDTOResponse;
import hub.com.api_store.service.ProductService;
import hub.com.api_store.util.page.PageResponse;
import hub.com.api_store.util.response.GenericResponse;
import hub.com.api_store.util.response.StatusApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    // GET
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<ProductDTOResponse>> getProductByIdGet(@PathVariable Long id){
        ProductDTOResponse productDTOResponse = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, productDTOResponse)
        );
    }

    @GetMapping("/page")
    public ResponseEntity<GenericResponse<PageResponse<ProductDTOResponse>>> getPageFindAllProductsGet(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String prop
            ){
        PageResponse<ProductDTOResponse> pageResponse = productService.getPageFindAllProducts(page, size, prop);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, pageResponse)
        );

    }
}
