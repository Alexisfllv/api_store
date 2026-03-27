package hub.com.api_store.service.domain;

import hub.com.api_store.dto.product.ProductDTOResponse;
import hub.com.api_store.entity.Product;
import hub.com.api_store.exception.ResourceNotFoundException;
import hub.com.api_store.nums.ExceptionMessages;
import hub.com.api_store.repo.ProductRepo;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductServiceDomain {
    private final ProductRepo productRepo;

    public Product findById(Long id){
        return productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.RESOURCE_NOT_FOUND_ERROR.message()+id));
    }
}
