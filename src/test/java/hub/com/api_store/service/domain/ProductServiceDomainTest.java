package hub.com.api_store.service.domain;

import hub.com.api_store.entity.Category;
import hub.com.api_store.entity.Product;
import hub.com.api_store.exception.ResourceNotFoundException;
import hub.com.api_store.nums.ExceptionMessages;
import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.nums.GlobalUnit;
import hub.com.api_store.repo.ProductRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceDomainTest {

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private ProductServiceDomain productServiceDomain;

    @Nested
    @DisplayName("Test findById")
    class findById{
        @Test
        @DisplayName("Should return Product Id Exists")
        void findByIdSuccess(){
            // Arrange
            Long idExist = 1L;
            Category category = new Category(1L,"name","description", GlobalStatus.ACTIVE);
            Product product = new Product(1L,"name", GlobalUnit.KG,GlobalStatus.ACTIVE,category);
            when(productRepo.findById(idExist)).thenReturn(Optional.of(product));
            // Act
            Product result = productServiceDomain.findById(idExist);
            // Assert
            assertAll(
                    () -> assertEquals(product.getId(),result.getId()),
                    () -> assertEquals(product.getName(),result.getName()),
                    () -> assertEquals(product.getUnit(),result.getUnit()),
                    () -> assertEquals(product.getStatus(),result.getStatus()),
                    () -> assertEquals(product.getCategory().getId(),result.getCategory().getId()),
                    () -> assertEquals(product.getCategory().getName(),result.getCategory().getName()),
                    () -> assertEquals(product.getCategory().getDescription(),result.getCategory().getDescription()),
                    () -> assertEquals(product.getCategory().getStatus(),result.getCategory().getStatus())
            );
            // InOrder - Verify
            InOrder inOrder = Mockito.inOrder(productRepo);
            inOrder.verify(productRepo).findById(idExist);
            inOrder.verifyNoMoreInteractions();
        }

        @Test
        @DisplayName("Should return Product Id Exists Throw")
        void findByIdThrow(){
            // Arrange
            Long idExist = 99L;
            Category category = new Category(1L,"name","description", GlobalStatus.ACTIVE);
            Product product = new Product(1L,"name", GlobalUnit.KG,GlobalStatus.ACTIVE,category);
            when(productRepo.findById(idExist)).thenReturn(Optional.empty());
            // Act
            ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, ()->productServiceDomain.findById(idExist));
            // Assert
            assertAll(
                    () -> assertNotNull(ex),
                    () -> assertEquals(ExceptionMessages.RESOURCE_NOT_FOUND_ERROR.message()+idExist,ex.getMessage())
            );
            // InOrder - Verify
            InOrder inOrder = Mockito.inOrder(productRepo);
            inOrder.verify(productRepo).findById(idExist);
            inOrder.verifyNoMoreInteractions();
        }
    }
}
