package hub.com.api_store.mapper;

import hub.com.api_store.dto.category.CategoryDTORequest;
import hub.com.api_store.dto.category.CategoryDTOResponse;
import hub.com.api_store.entity.Category;
import hub.com.api_store.nums.CategoryStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CategoryMapperTest {

    // mapper pure
    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        categoryMapper = Mappers.getMapper(CategoryMapper.class);
    }

    @Nested
    @DisplayName("Tests mapped response <- entity")
    class ToResponseTests {
        @Test
        @DisplayName("Should map response <- entity")
        void toCategoryDTOResponse_shouldMapCorrectly() {
            // Arrange
            Category category = new Category(1L,"name","description", CategoryStatus.ACTIVE);
            // Act
            CategoryDTOResponse result = categoryMapper.toCategoryDTOResponse(category);

            // Assert
            assertAll(
                    () -> assertEquals(1L,result.id()),
                    () -> assertEquals("name",result.name()),
                    () -> assertEquals("description",result.description()),
                    () -> assertEquals(CategoryStatus.ACTIVE,result.status())
            );
        }

        @Test
        @DisplayName("Should map response <- entity null")
        void toCategoryDTOResponse_shouldHandleNull() {
            // Act
            CategoryDTOResponse result = categoryMapper.toCategoryDTOResponse(null);

            // Assert
            assertNull(result);
        }
    }


    @Nested
    @DisplayName("Test mapped entity <- request")
    class ToEntityTests {
        @Test
        @DisplayName("Should map entity <- request")
        void toCategoryDTORequest_shouldMapCorrectly() {
            // Arrange
            CategoryDTORequest request = new CategoryDTORequest("name","description");
            // Act
            Category result = categoryMapper.toCategory(request);

            // Assert
            assertAll(
                    () -> assertEquals("name",result.getName()),
                    () -> assertEquals("description",result.getDescription())
            );
        }

        @Test
        @DisplayName("Should map entity <- request null")
        void toCategoryDTORequest_shouldHandleNull() {
            // Act
            Category result = categoryMapper.toCategory(null);
            // Assert
            assertNull(result);
        }
    }




}
