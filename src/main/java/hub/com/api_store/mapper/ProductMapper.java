package hub.com.api_store.mapper;

import hub.com.api_store.dto.product.ProductDTOResponse;
import hub.com.api_store.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTOResponse toProductDTOResponse(Product product){
        return new ProductDTOResponse(
                product.getId(),
                product.getName(),
                product.getUnit(),
                product.getStatus(),
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }
}
