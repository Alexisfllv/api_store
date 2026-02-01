package hub.com.api_store.mapper;

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




}
