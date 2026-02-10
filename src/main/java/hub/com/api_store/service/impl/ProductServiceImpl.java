package hub.com.api_store.service.impl;

import hub.com.api_store.dto.product.ProductDTORequest;
import hub.com.api_store.dto.product.ProductDTOResponse;
import hub.com.api_store.dto.product.ProductDTOUpdate;
import hub.com.api_store.entity.Category;
import hub.com.api_store.entity.Product;
import hub.com.api_store.mapper.ProductMapper;
import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.repo.ProductRepo;
import hub.com.api_store.service.ProductService;
import hub.com.api_store.service.domain.CategoryServiceDomain;
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

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductServiceDomain productServiceDomain;
    private final ProductMapper productMapper;
    private final ProductRepo productRepo;

    // cat
    private final CategoryServiceDomain categoryServiceDomain;

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

    @Override
    public List<ProductDTOResponse> findProductListByCategoryId(Long categoryId,int limit) {
        categoryServiceDomain.findByIdCategory(categoryId);
        List<Product> productList = productRepo.findByCategoryId(categoryId);
        return productList.stream()
                .map(productMapper::toProductDTOResponse)
                .limit(limit)
                .toList();
    }

    @Override
    public List<ProductDTOResponse> findProductListByName(String name, int limit) {
        List<Product> productList = productRepo.findByNameContainingIgnoreCase(name);
        return productList.stream()
                .map(productMapper::toProductDTOResponse)
                .limit(limit)
                .toList();
    }

    @Override
    public List<ProductDTOResponse> findProductListByStatus(GlobalStatus status, int limit) {
        List<Product> productList = productRepo.findByStatus(status);
        return productList.stream()
                .map(productMapper::toProductDTOResponse)
                .limit(limit)
                .toList();
    }

    // POST
    @Transactional
    @Override
    public ProductDTOResponse addProduct(ProductDTORequest productDTORequest) {
        Category category =  categoryServiceDomain.findByIdCategory(productDTORequest.categoryId());

        Product product = productMapper.toProduct(productDTORequest,category);

        Product savedProduct = productRepo.save(product);
        ProductDTOResponse productDTOResponse = productMapper.toProductDTOResponse(savedProduct);
        return productDTOResponse;
    }

    // PUT
    @Transactional
    @Override
    public ProductDTOResponse updateProduct(Long id, ProductDTOUpdate productDTOUpdate) {
        Product productExist = productServiceDomain.findById(id);
        Category category =  categoryServiceDomain.findByIdCategory(productDTOUpdate.categoryId());
        productExist.setName(productDTOUpdate.name());
        productExist.setUnit(productDTOUpdate.unit());
        productExist.setStatus(productDTOUpdate.status());
        productExist.setCategory(category);
        Product productUpdated = productRepo.save(productExist);
        ProductDTOResponse productDTOResponse = productMapper.toProductDTOResponse(productUpdated);
        return productDTOResponse;
    }



    // DELETE
    @Transactional
    @Override
    public void deleteProductById(Long id) {
        Product product = productServiceDomain.findById(id);
        product.setStatus(GlobalStatus.DELETED);
        productRepo.save(product);
    }


}
