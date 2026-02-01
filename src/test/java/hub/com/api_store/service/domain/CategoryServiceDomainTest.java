package hub.com.api_store.service.domain;

import hub.com.api_store.dto.category.CategoryDTOResponse;
import hub.com.api_store.entity.Category;
import hub.com.api_store.exception.ResourceNotFoundException;
import hub.com.api_store.nums.CategoryStatus;
import hub.com.api_store.nums.ExceptionMessages;
import hub.com.api_store.repo.CategoryRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceDomainTest {

    @Mock
    private CategoryRepo categoryRepo;

    @InjectMocks
    private CategoryServiceDomain categoryServiceDomain;

    // def
    private Long idExist;
    private Long idNotExist;
    private Category category ;

    @BeforeEach
    public void setup(){
        idExist = 1L;
        idNotExist = 99L;
        category = new Category(1L,"name","description", CategoryStatus.ACTIVE);
    }

    @Nested
    @DisplayName("findByIdCategory")
    class FindByIdCategory{

        @Test
        @DisplayName("Should return category when id exists")
        void shouldReturnCategoryWhenIdExist(){
            // Arrange
            when(categoryRepo.findById(idExist)).thenReturn(Optional.of(category));
            // Act
            Category result  = categoryServiceDomain.findByIdCategory(idExist);

            // Assert
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(idExist,result.getId()),
                    () -> assertEquals(category.getName(),result.getName()),
                    () -> assertEquals(category.getDescription(),result.getDescription()),
                    () -> assertEquals(category.getStatus(),result.getStatus())
            );
            // Verify
            verify(categoryRepo).findById(idExist);
            verifyNoMoreInteractions(categoryRepo);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when id not exists")
        void shouldReturnThrowExceptionWhenIdNotExist(){
            // Arrange
            when(categoryRepo.findById(idNotExist)).thenReturn(Optional.empty());
            // Act
            ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                    () -> categoryServiceDomain.findByIdCategory(idNotExist));
            // Assert
            assertAll(
                    () -> assertNotNull(ex),
                    () -> assertTrue(ex.getMessage().contains(ExceptionMessages.RESOURCE_NOT_FOUND_ERROR.message())),
                    () -> assertTrue(ex.getMessage().contains(idNotExist.toString()))
            );
            // Verify
            verify(categoryRepo).findById(idNotExist);
            verifyNoMoreInteractions(categoryRepo);
        }
    }
}
