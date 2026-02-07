package hub.com.api_store.service;

import hub.com.api_store.dto.product.ProductDTOResponse;

public interface ProductService {
    // GET
    ProductDTOResponse getProductById(Long id);
}
