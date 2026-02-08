package hub.com.api_store.mapper;

import hub.com.api_store.dto.product.ProductDTORequest;
import hub.com.api_store.dto.product.ProductDTOResponse;
import hub.com.api_store.entity.Category;
import hub.com.api_store.entity.Product;
import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.nums.GlobalUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductMapperTest {

    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper();
    }

    @Nested
    @DisplayName("Test mapped response <- entity")
    class ToResponseTests {
        @Test
        @DisplayName("Should map response <- entity")
        void toProductDTOResponse_shouldMapCorrectly() {
            // Arrange
            Category category = new Category(1L,"name","description", GlobalStatus.ACTIVE);
            Product product = new Product(1L,"name",GlobalUnit.KG,GlobalStatus.ACTIVE,category);

            // Act
            ProductDTOResponse productDTOResponse = productMapper.toProductDTOResponse(product);
            // Assert
            assertAll(
                    () -> assertNotNull(productDTOResponse),
                    () -> assertEquals(1L, productDTOResponse.id()),
                    () -> assertEquals("name", productDTOResponse.name()),
                    () -> assertEquals(GlobalUnit.KG, productDTOResponse.unit()),
                    () -> assertEquals(GlobalStatus.ACTIVE, productDTOResponse.status()),
                    () -> assertEquals(1L, productDTOResponse.categoryId()),
                    () -> assertEquals("name", productDTOResponse.categoryName())
            );
        }
    }

    @Test
    @DisplayName("toProduct mapped entity <- request")
    void toProduct(){
        // Arrange
        Category category = new Category(1L,"name","description", GlobalStatus.ACTIVE);

        ProductDTORequest request = new ProductDTORequest("name",GlobalUnit.KG,category.getId());

        // Act
        Product product = productMapper.toProduct(request,category);
        // Assert
        assertAll(
                () -> assertNotNull(product),
                () -> assertNull(product.getId()),
                () -> assertEquals("name", product.getName()),
                () -> assertEquals(GlobalUnit.KG, product.getUnit()),
                () -> assertEquals(GlobalStatus.ACTIVE, product.getStatus()),
                () -> assertEquals(category, product.getCategory())
        );
    }
}
