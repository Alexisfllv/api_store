package hub.com.api_store.service.impl;

import hub.com.api_store.dto.product.ProductDTOResponse;
import hub.com.api_store.entity.Product;
import hub.com.api_store.mapper.ProductMapper;
import hub.com.api_store.repo.ProductRepo;
import hub.com.api_store.service.ProductService;
import hub.com.api_store.service.domain.ProductServiceDomain;
import hub.com.api_store.util.page.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductServiceDomain productServiceDomain;
    private final ProductMapper productMapper;
    private final ProductRepo productRepo;

    // GET
    @Override
    public ProductDTOResponse getProductById(Long id) {
        Product product = productServiceDomain.findById(id);
        ProductDTOResponse productDTOResponse = productMapper.toProductDTOResponse(product);
        return productDTOResponse;
    }

    @Override
    public PageResponse<ProductDTOResponse> getPageFindAllProducts(int page, int size,String prop) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC,prop));
        Page<Product> productPageResponse = productRepo.findAll(pageable);
        return new PageResponse<>(
                productPageResponse.getContent()
                        .stream()
                        .map(productMapper::toProductDTOResponse)
                        .toList(),
                productPageResponse.getNumber(),
                productPageResponse.getSize(),
                productPageResponse.getTotalElements(),
                productPageResponse.getTotalPages()
        );
    }
}
