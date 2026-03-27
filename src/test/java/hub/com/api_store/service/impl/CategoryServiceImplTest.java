package hub.com.api_store.service.impl;

import hub.com.api_store.dto.category.CategoryDTORequest;
import hub.com.api_store.dto.category.CategoryDTOResponse;
import hub.com.api_store.dto.category.CategoryDTOUpdate;
import hub.com.api_store.entity.Category;
import hub.com.api_store.mapper.CategoryMapper;
import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.service.domain.CategoryServiceDomain;
import hub.com.api_store.util.page.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        category = new Category(1L,"name","description", GlobalStatus.ACTIVE);
        categoryDTOResponse = new CategoryDTOResponse(1L,"name","description", GlobalStatus.ACTIVE);
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

        Category cat1 = new Category(1L, "Electronics", "Elec", GlobalStatus.ACTIVE);
        Category cat2 = new Category(2L, "Books", "Bo", GlobalStatus.ACTIVE);
        List<Category> cats = List.of(cat1, cat2);

        CategoryDTOResponse dto1 = new CategoryDTOResponse(1L, "Electronics", "Elec", GlobalStatus.ACTIVE);
        CategoryDTOResponse dto2 = new CategoryDTOResponse(2L, "Books", "Bo", GlobalStatus.ACTIVE);


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
        Category savedCategory = new Category(1L, "Electronics", "Electronic devices", GlobalStatus.ACTIVE);
        CategoryDTOResponse expectedResponse = new CategoryDTOResponse(1L, "Electronics", "Electronic devices", GlobalStatus.ACTIVE);

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
                () -> assertEquals(GlobalStatus.ACTIVE,result.status())
        );

        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(categoryMapper, categoryServiceDomain);
        inOrder.verify( categoryMapper).toCategory(request);
        inOrder.verify(categoryServiceDomain).saveCategory(any(Category.class));
        inOrder.verify(categoryMapper).toCategoryDTOResponse(savedCategory);
        inOrder.verifyNoMoreInteractions();
    }

    @Nested
    @DisplayName("PUT updateCategory")
    class updateCategory{
        @Test
        @DisplayName("PUT updateCategory")
        void shouldUpdateCategory(){
            // Arrange
            Long idExist = 1L;
            Category beforeCategory = new Category(1L, "Electronicos", "Elec", GlobalStatus.ACTIVE);
            CategoryDTOUpdate update = new CategoryDTOUpdate("Electronicos New","Elec New", GlobalStatus.ACTIVE);
            Category updateCategory = new Category(idExist,"Electronicos New","Elec New", GlobalStatus.ACTIVE);
            CategoryDTOResponse updateResponse = new CategoryDTOResponse(idExist,"Electronicos New","Elec New", GlobalStatus.ACTIVE);
            when(categoryServiceDomain.findByIdCategory(idExist)).thenReturn(beforeCategory);
            when(categoryServiceDomain.saveCategory(any(Category.class))).thenReturn(updateCategory);
            when(categoryMapper.toCategoryDTOResponse(updateCategory)).thenReturn(updateResponse);

            // Act
            CategoryDTOResponse result = categoryService.updateCategory(idExist, update);

            // Assert
            assertAll(
                    () -> assertEquals(updateResponse.id(),result.id()),
                    () -> assertEquals(updateResponse.name(),result.name()),
                    () -> assertEquals(GlobalStatus.ACTIVE,result.status())
            );

            // InOrder & Verify
            InOrder inOrder = Mockito.inOrder(categoryMapper, categoryServiceDomain);
            inOrder.verify(categoryServiceDomain).findByIdCategory(idExist);
            inOrder.verify(categoryServiceDomain).saveCategory(any(Category.class));
            inOrder.verify(categoryMapper).toCategoryDTOResponse(updateCategory);
        }

        @Test
        @DisplayName("PUT updateCategory name")
        void shouldUpdateCategoryName(){
            // Arrange
            CategoryDTOUpdate update = new CategoryDTOUpdate("Electronicos New","Elec New", GlobalStatus.ACTIVE);
            Category category1 = new Category(1L,"Electronicos New","Elec New", GlobalStatus.ACTIVE);
            CategoryDTOResponse dto1 = new CategoryDTOResponse(1L,"Electronicos New","Elec New", GlobalStatus.ACTIVE);

            when(categoryServiceDomain.findByIdCategory(1L)).thenReturn(category1);
            when(categoryServiceDomain.saveCategory(any(Category.class))).thenReturn(category1);
            when(categoryMapper.toCategoryDTOResponse(category1)).thenReturn(dto1);
            // Act
            CategoryDTOResponse result = categoryService.updateCategory(1L, update);
            // Assert
            assertEquals(dto1,result);

            // Verify
            verify(categoryServiceDomain,never()).validateUniqueName(anyString());

        }
    }

    @Test
    @DisplayName("DELETE deleteCategoryDelete")
    void shouldDeleteCategory(){
        // Arrange
        Long idExist = 1L;
        Category categoryExist =  new Category(1L, "Electronicos", "Electronics devices", GlobalStatus.ACTIVE);
        Category categorySoftDeleted = new Category(1L, "Electronicos", "Electronics devices", GlobalStatus.DELETED);

        when(categoryServiceDomain.findByIdCategory(idExist)).thenReturn(categoryExist);
        when(categoryServiceDomain.saveCategory(any(Category.class))).thenReturn(categorySoftDeleted);
        // Act
        categoryService.deleteSofCategory(idExist);
        // Assert

        assertEquals(GlobalStatus.DELETED,categorySoftDeleted.getStatus());
        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder( categoryServiceDomain);
        inOrder.verify(categoryServiceDomain).findByIdCategory(idExist);
        inOrder.verify(categoryServiceDomain).saveCategory(any(Category.class));
    }

    @Test
    @DisplayName("GET getPageListCategoryByStatus")
    void shouldGetPageListCategoryByStatus(){
        // Arrange
        int page = 0;
        int size = 2;
        GlobalStatus status = GlobalStatus.ACTIVE;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        Category cat1 = new Category(1L, "Electronics", "Elec", GlobalStatus.ACTIVE);
        Category cat2 = new Category(2L, "Books", "Bo", GlobalStatus.ACTIVE);
        List<Category> cats = List.of(cat1, cat2);
        CategoryDTOResponse dto1 = new CategoryDTOResponse(1L, "Electronics", "Elec", GlobalStatus.ACTIVE);
        CategoryDTOResponse dto2 = new CategoryDTOResponse(2L, "Books", "Bo", GlobalStatus.ACTIVE);
        Page<Category> categoryPage = new PageImpl(cats, pageable, 10);
        when(categoryServiceDomain.findAllPageByStatus(status, pageable)).thenReturn(categoryPage);
        when(categoryMapper.toCategoryDTOResponse(cat1)).thenReturn(dto1);
        when(categoryMapper.toCategoryDTOResponse(cat2)).thenReturn(dto2);
        // Act
        PageResponse<CategoryDTOResponse> result = categoryService.getPageListCategoryByStatus(status,page,size);

        // Assert
        assertAll(
                () -> assertEquals(2,result.content().size()),
                () -> assertEquals(0,result.page()),
                () -> assertEquals(10,result.totalElements()),
                () -> assertEquals(5, result.totalPages())
        );
        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(categoryMapper, categoryServiceDomain);
        inOrder.verify(categoryServiceDomain).findAllPageByStatus(status, pageable);
        inOrder.verify(categoryMapper).toCategoryDTOResponse(cat1);
        inOrder.verify(categoryMapper).toCategoryDTOResponse(cat2);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("PATCH updateCategoryStatusPatch")
    void shouldPatchCategoryStatusPatch(){
        // Arrange
        Long idExist = 1L;
        GlobalStatus statusUpdated = GlobalStatus.INACTIVE;
        Category categoryExist = new Category(1L, "Electronics", "Elec", GlobalStatus.ACTIVE);
        Category categoryUpdated = new Category(1L, "Electronics", "Elec", statusUpdated);
        CategoryDTOResponse responseUpdated =  new CategoryDTOResponse(1L, "Electronics", "Elec", statusUpdated);


        when(categoryServiceDomain.findByIdCategory(idExist)).thenReturn(categoryExist);
        when(categoryServiceDomain.saveCategory(any(Category.class))).thenReturn(categoryUpdated);
        when(categoryMapper.toCategoryDTOResponse(categoryUpdated)).thenReturn(responseUpdated);
        // Act
        CategoryDTOResponse response = categoryService.updateCategoryStatus(idExist, statusUpdated);
        // Assert
        assertAll(
                () -> assertEquals(responseUpdated.id(),response.id()),
                () -> assertEquals(responseUpdated.name(),response.name()),
                () -> assertEquals(responseUpdated.status(),response.status())
        );

        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(categoryMapper, categoryServiceDomain);
        inOrder.verify(categoryServiceDomain).findByIdCategory(idExist);
        inOrder.verify(categoryServiceDomain).saveCategory(any(Category.class));
        inOrder.verify(categoryMapper).toCategoryDTOResponse(categoryUpdated);
        inOrder.verifyNoMoreInteractions();
    }

}
