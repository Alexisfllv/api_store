package hub.com.api_store.service;

import hub.com.api_store.dto.product.ProductDTORequest;
import hub.com.api_store.dto.product.ProductDTOResponse;
import hub.com.api_store.dto.product.ProductDTOUpdate;
import hub.com.api_store.util.page.PageResponse;

import java.util.List;

public interface ProductService {
    // GET
    ProductDTOResponse getProductById(Long id);
    PageResponse<ProductDTOResponse> getPageFindAllProducts(int page, int size,String prop);

    List<ProductDTOResponse> findProductListByCategoryId(Long categoryId,int limit);

    // POST
    ProductDTOResponse addProduct(ProductDTORequest productDTORequest);

    // PUT
    ProductDTOResponse updateProduct(Long id, ProductDTOUpdate productDTOUpdate);

    // DELETE
    void deleteProductById(Long id);

}
