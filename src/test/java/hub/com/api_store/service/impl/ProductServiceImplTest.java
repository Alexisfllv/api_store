package hub.com.api_store.service.impl;

import hub.com.api_store.dto.product.ProductDTOResponse;
import hub.com.api_store.entity.Category;
import hub.com.api_store.entity.Product;
import hub.com.api_store.mapper.ProductMapper;
import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.nums.GlobalUnit;
import hub.com.api_store.service.domain.ProductServiceDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductServiceDomain productServiceDomain;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Test
    @DisplayName("should return productDTOResponse when product exists")
    void getProductById() {
        // Arrange
        Long idExist = 1L;
        Category category = new Category(1L,"name","description", GlobalStatus.ACTIVE);
        Product product = new Product(1L,"name", GlobalUnit.KG,GlobalStatus.ACTIVE,category);
        ProductDTOResponse productDTOResponse = new ProductDTOResponse(1L,"name", GlobalUnit.KG,GlobalStatus.ACTIVE,product.getCategory().getId(),product.getCategory().getName());
        when(productServiceDomain.findById(idExist)).thenReturn(product);
        when(productMapper.toProductDTOResponse(product)).thenReturn(productDTOResponse);
        // Act
        ProductDTOResponse result = productServiceImpl.getProductById(idExist);
        // Assert
        assertAll(
                () -> assertEquals(productDTOResponse.id(),result.id()),
                () -> assertEquals(productDTOResponse.name(),result.name()),
                () -> assertEquals(productDTOResponse.unit(),result.unit()),
                () -> assertEquals(productDTOResponse.status(),result.status()),
                () -> assertEquals(productDTOResponse.categoryId(),result.categoryId()),
                () -> assertEquals(productDTOResponse.categoryName(),result.categoryName())
        );

        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(productServiceDomain, productMapper);
        inOrder.verify(productServiceDomain).findById(idExist);
        inOrder.verify(productMapper).toProductDTOResponse(product);
        inOrder.verifyNoMoreInteractions();

    }
}
