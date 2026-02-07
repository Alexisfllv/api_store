package hub.com.api_store.service.impl;

import hub.com.api_store.dto.product.ProductDTOResponse;
import hub.com.api_store.entity.Product;
import hub.com.api_store.mapper.ProductMapper;
import hub.com.api_store.service.ProductService;
import hub.com.api_store.service.domain.ProductServiceDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductServiceDomain productServiceDomain;
    private final ProductMapper productMapper;

    @Override
    public ProductDTOResponse getProductById(Long id) {
        Product product = productServiceDomain.findById(id);
        ProductDTOResponse productDTOResponse = productMapper.toProductDTOResponse(product);
        return productDTOResponse;
    }
}
