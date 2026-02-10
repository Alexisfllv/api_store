package hub.com.api_store.controller;

import hub.com.api_store.dto.product.ProductDTORequest;
import hub.com.api_store.dto.product.ProductDTOResponse;
import hub.com.api_store.dto.product.ProductDTOUpdate;
import hub.com.api_store.service.ProductService;
import hub.com.api_store.util.page.PageResponse;
import hub.com.api_store.util.response.GenericResponse;
import hub.com.api_store.util.response.StatusApi;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<GenericResponse<List<ProductDTOResponse>>> findProductListByCategoryIdGet(
            @PathVariable Long categoryId,
            @Positive (message = "{field.must.be.positive}")
            @RequestParam(defaultValue = "10") int limit){
        List<ProductDTOResponse> productDTOResponseList = productService.findProductListByCategoryId(categoryId, limit);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, productDTOResponseList)
        );
    }

    // POST
    @PostMapping
    public ResponseEntity<GenericResponse<ProductDTOResponse>> addProductPost(@Valid @RequestBody ProductDTORequest productDTORequest){
        ProductDTOResponse productDTOResponse = productService.addProduct(productDTORequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new GenericResponse<>(StatusApi.CREATED, productDTOResponse)
        );
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<ProductDTOResponse>> updateProductPut(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTOUpdate productDTOUpdate){
        ProductDTOResponse productDTOResponse = productService.updateProduct(id, productDTOUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.UPDATED, productDTOResponse)
        );
    }

    // DELETE
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<Void> deleteProductByIdDelete(@PathVariable Long id){
        productService.deleteProductById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
