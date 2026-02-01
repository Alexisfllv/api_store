package hub.com.api_store.service.impl;

import hub.com.api_store.dto.category.CategoryDTOResponse;
import hub.com.api_store.entity.Category;
import hub.com.api_store.mapper.CategoryMapper;
import hub.com.api_store.nums.CategoryStatus;
import hub.com.api_store.service.domain.CategoryServiceDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
public class CategoryServiceImplTest {

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CategoryServiceDomain categoryServiceDomain;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Long idExist;
    private Category category;
    private CategoryDTOResponse categoryDTOResponse;

    @BeforeEach
    public void setup(){
        idExist = 1L;
        category = new Category(1L,"name","description", CategoryStatus.ACTIVE);
        categoryDTOResponse = new CategoryDTOResponse(1L,"name","description", CategoryStatus.ACTIVE);
    }

    @Test
    @DisplayName("GET getCategoryId")
    void shoudlReturnCategory(){
        // Arrange
        Category categoryExist = category;
        when(categoryServiceDomain.findByIdCategory(idExist)).thenReturn(categoryExist);
        when(categoryMapper.toCategoryDTOResponse(categoryExist)).thenReturn(categoryDTOResponse);
        // Act
        CategoryDTOResponse result = categoryService.getCategoryId(idExist);
        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(categoryDTOResponse.id(), result.id()),
                () -> assertEquals(categoryDTOResponse.name(),result.name()),
                () -> assertEquals(categoryDTOResponse.description(),result.description()),
                () -> assertEquals(categoryDTOResponse.status(),result.status())
        );
        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(categoryMapper, categoryServiceDomain);
        inOrder.verify(categoryServiceDomain).findByIdCategory(idExist);
        inOrder.verify(categoryMapper).toCategoryDTOResponse(categoryExist);
        inOrder.verifyNoMoreInteractions();
    }

}
