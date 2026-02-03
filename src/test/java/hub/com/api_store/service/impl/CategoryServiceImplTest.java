package hub.com.api_store.service.impl;

import hub.com.api_store.dto.category.CategoryDTORequest;
import hub.com.api_store.dto.category.CategoryDTOResponse;
import hub.com.api_store.dto.category.CategoryDTOUpdate;
import hub.com.api_store.entity.Category;
import hub.com.api_store.mapper.CategoryMapper;
import hub.com.api_store.nums.CategoryStatus;
import hub.com.api_store.service.domain.CategoryServiceDomain;
import hub.com.api_store.util.page.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
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

    @Test
    @DisplayName("GET getPageListCategory")
    void shoudlReturnPageListCategory(){
        // Arrange
        int page = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        Category cat1 = new Category(1L, "Electronics", "Elec", CategoryStatus.ACTIVE);
        Category cat2 = new Category(2L, "Books", "Bo", CategoryStatus.ACTIVE);
        List<Category> cats = List.of(cat1, cat2);

        CategoryDTOResponse dto1 = new CategoryDTOResponse(1L, "Electronics", "Elec", CategoryStatus.ACTIVE);
        CategoryDTOResponse dto2 = new CategoryDTOResponse(2L, "Books", "Bo", CategoryStatus.ACTIVE);


        Page<Category> categoryPage = new PageImpl<>(cats,pageable,10);

        when(categoryServiceDomain.findAllPage(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toCategoryDTOResponse(cat1)).thenReturn(dto1);
        when(categoryMapper.toCategoryDTOResponse(cat2)).thenReturn(dto2);

        // Act
        PageResponse<CategoryDTOResponse> result = categoryService.getPageListCategory(page, size);

        // Assert
        assertAll(
                () -> assertEquals(2,result.content().size()),
                () -> assertEquals(0,result.page()),
                () -> assertEquals(10,result.totalElements())
        );
        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(categoryMapper, categoryServiceDomain);
        inOrder.verify(categoryServiceDomain).findAllPage(pageable);
        inOrder.verify(categoryMapper, times(2)).toCategoryDTOResponse(any(Category.class));
        inOrder.verifyNoMoreInteractions();

    }

    @Test
    @DisplayName("POST addCategory")
    void shouldSaveCategory(){
        // Arrange
        CategoryDTORequest request = new CategoryDTORequest("Electronics", "Electronic devices");
        Category unmappedCategory = new Category(null, "Electronics", "Electronic devices", null);
        Category savedCategory = new Category(1L, "Electronics", "Electronic devices", CategoryStatus.ACTIVE);
        CategoryDTOResponse expectedResponse = new CategoryDTOResponse(1L, "Electronics", "Electronic devices", CategoryStatus.ACTIVE);

        when(categoryMapper.toCategory(request)).thenReturn(unmappedCategory);
        when(categoryServiceDomain.saveCategory(any(Category.class))).thenReturn(savedCategory);
        when(categoryMapper.toCategoryDTOResponse(savedCategory)).thenReturn(expectedResponse);
        // Act
        CategoryDTOResponse result = categoryService.addCategory(request);

        // Assert
        assertAll(
                () -> assertEquals(1L,result.id()),
                () -> assertEquals("Electronics", result.name()),
                () -> assertEquals("Electronic devices",result.description()),
                () -> assertEquals(CategoryStatus.ACTIVE,result.status())
        );

        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(categoryMapper, categoryServiceDomain);
        inOrder.verify( categoryMapper).toCategory(request);
        inOrder.verify(categoryServiceDomain).saveCategory(any(Category.class));
        inOrder.verify(categoryMapper).toCategoryDTOResponse(savedCategory);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("PUT updateCategory")
    void shouldUpdateCategory(){
        // Arrange
        Long idExist = 1L;
        Category beforeCategory = new Category(1L, "Electronicos", "Elec", CategoryStatus.ACTIVE);
        CategoryDTOUpdate update = new CategoryDTOUpdate("Electronicos New","Elec New",CategoryStatus.ACTIVE);
        Category updateCategory = new Category(idExist,"Electronicos New","Elec New",CategoryStatus.ACTIVE);
        CategoryDTOResponse updateResponse = new CategoryDTOResponse(idExist,"Electronicos New","Elec New",CategoryStatus.ACTIVE);
        when(categoryServiceDomain.findByIdCategory(idExist)).thenReturn(beforeCategory);
        when(categoryServiceDomain.saveCategory(any(Category.class))).thenReturn(updateCategory);
        when(categoryMapper.toCategoryDTOResponse(updateCategory)).thenReturn(updateResponse);

        // Act
        CategoryDTOResponse result = categoryService.updateCategory(idExist, update);

        // Assert
        assertAll(
                () -> assertEquals(updateResponse.id(),result.id()),
                () -> assertEquals(updateResponse.name(),result.name()),
                () -> assertEquals(CategoryStatus.ACTIVE,result.status())
        );

        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(categoryMapper, categoryServiceDomain);
        inOrder.verify(categoryServiceDomain).findByIdCategory(idExist);
        inOrder.verify(categoryServiceDomain).saveCategory(any(Category.class));
        inOrder.verify(categoryMapper).toCategoryDTOResponse(updateCategory);
    }

    @Test
    @DisplayName("DELETE deleteCategoryDelete")
    void shouldDeleteCategory(){
        // Arrange
        Long idExist = 1L;
        Category categoryExist =  new Category(1L, "Electronicos", "Electronics devices", CategoryStatus.ACTIVE);
        Category categorySoftDeleted = new Category(1L, "Electronicos", "Electronics devices", CategoryStatus.DELETED);

        when(categoryServiceDomain.findByIdCategory(idExist)).thenReturn(categoryExist);
        when(categoryServiceDomain.saveCategory(any(Category.class))).thenReturn(categorySoftDeleted);
        // Act
        categoryService.deleteSofCategory(idExist);
        // Assert

        assertEquals(CategoryStatus.DELETED,categorySoftDeleted.getStatus());
        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder( categoryServiceDomain);
        inOrder.verify(categoryServiceDomain).findByIdCategory(idExist);
        inOrder.verify(categoryServiceDomain).saveCategory(any(Category.class));
    }

}
