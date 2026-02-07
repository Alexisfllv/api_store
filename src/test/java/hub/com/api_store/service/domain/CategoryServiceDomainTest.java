package hub.com.api_store.service.domain;

import hub.com.api_store.entity.Category;
import hub.com.api_store.exception.ResourceNotFoundException;
import hub.com.api_store.exception.UniqueValidateException;
import hub.com.api_store.nums.GlobalStatus;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
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
        category = new Category(1L,"name","description", GlobalStatus.ACTIVE);
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

    @Nested
    @DisplayName("findAllPage")
    class findAllPage{

        @Test
        @DisplayName("findAllPageTest")
        void findAllPageTest(){
            // Arrange
            int page = 0;
            int size = 2;
            Pageable pageable = PageRequest.of(page, size);

            Category cat1 = new Category(1L, "Electronics", "Elec", GlobalStatus.ACTIVE);
            Category cat2 = new Category(2L, "Books", "Bo", GlobalStatus.ACTIVE);
            List<Category> cats = List.of(cat1, cat2);

            Page<Category> expected = new PageImpl<>(cats, pageable, 10); // total=10 en BD

            when(categoryRepo.findAll(pageable)).thenReturn(expected);

            // Act
            Page<Category> result = categoryServiceDomain.findAllPage(pageable);

            // Assert
            assertAll(
                    () -> assertEquals(2, result.getContent().size()),
                    () -> assertEquals(10, result.getTotalElements())
            );

            // Verify
            verify(categoryRepo).findAll(pageable);
        }

        @Test
        @DisplayName("findAllPageTestNoCategories")
        void shouldReturnEmptyPageWhenNoCategoriesExist(){
            // Arrange
            int page = 0;
            int size = 2;
            Pageable pageable = PageRequest.of(page, size);

            List<Category> cats = List.of(); // Lista vacía

            Page<Category> expected = new PageImpl<>(cats, pageable, 0); // total=0

            when(categoryRepo.findAll(pageable)).thenReturn(expected);

            // Act
            Page<Category> result = categoryServiceDomain.findAllPage(pageable);

            // Assert
            assertAll(
                    () -> assertEquals(0, result.getContent().size()),
                    () -> assertEquals(0, result.getTotalElements()),
                    () -> assertTrue(result.isEmpty())
            );

            // Verify
            verify(categoryRepo).findAll(pageable);
        }
    }

    @Nested
    @DisplayName("validateUniqueName")
    class saveCategory{

        @Test
        @DisplayName("validateUniqueName Not exist name")
        void validateUniqueName_notExistName(){
            // Arrange
            String name = "name";
            when(categoryRepo.existsByName(name)).thenReturn(Boolean.FALSE);
            // Act & Assert
            assertDoesNotThrow(
                    () -> categoryServiceDomain.validateUniqueName(name)
            );
            // Verify
            verify(categoryRepo).existsByName(name);
            verifyNoMoreInteractions(categoryRepo);
        }

        @Test
        @DisplayName("validateUniqueName exist name")
        void validateUniqueName_existName(){
            // Arrange
            String nameExist = "name";
            when(categoryRepo.existsByName(nameExist)).thenReturn(Boolean.TRUE);
            // Act
            UniqueValidateException ex = assertThrows(UniqueValidateException.class,
                    () -> categoryServiceDomain.validateUniqueName(nameExist));
            // Assert
            assertAll(
                    () -> assertNotNull(ex),
                    () -> assertTrue(ex.getMessage().contains(ExceptionMessages.UNIQUE_EXC.message())),
                    () -> assertTrue(ex.getMessage().contains(nameExist))
            );
            // Verify
            verify(categoryRepo).existsByName(nameExist);
            verifyNoMoreInteractions(categoryRepo);
        }
    }

    @Test
    @DisplayName("saveCategory")
    void saveCategory(){
        // Arrange
        Category emptyCategory = new Category(null,"name","description",null);
        when(categoryRepo.save(emptyCategory)).thenReturn(category);
        // Act
        Category result = categoryServiceDomain.saveCategory(emptyCategory);

        // Assert
        assertAll(
                () -> assertEquals(1L,result.getId()),
                () -> assertEquals(emptyCategory.getName(), result.getName()),
                () -> assertEquals(emptyCategory.getDescription(), result.getDescription()),
                () -> assertEquals(GlobalStatus.ACTIVE, result.getStatus())
        );

        // Verify
        verify(categoryRepo).save(emptyCategory);
        verifyNoMoreInteractions(categoryRepo);
    }

    @Nested
    @DisplayName("findAllPageByStatus")
    class findAllPageByStatus{

        @Test
        @DisplayName("findAllPageByStatus")
        void findAllPageByStatus(){
            // Arrange
            int page = 0;
            int size = 2;
            GlobalStatus status = GlobalStatus.ACTIVE;
            Pageable pageable = PageRequest.of(page, size);
            Category cat1 = new Category(1L, "Electronics", "Elec", GlobalStatus.ACTIVE);
            Category cat2 = new Category(2L, "Books", "Bo", GlobalStatus.ACTIVE);
            List<Category> cats = List.of(cat1, cat2);
            Page<Category> expected = new PageImpl<>(cats, pageable, 10);
            when(categoryRepo.findByStatus(status, pageable)).thenReturn(expected);
            // Act
            Page<Category> result = categoryServiceDomain.findAllPageByStatus(status, pageable);
            assertAll(
                    () -> assertEquals(2,result.getContent().size()),
                    () -> assertEquals(10, result.getTotalElements())
            );
            // Verify
            verify(categoryRepo).findByStatus(status, pageable);
        }

        @Test
        @DisplayName("findAllPageByStatusNoCategories")
        void findAllPageByStatusNoCategories(){
            // Arrange
            int page = 0;
            int size = 2;
            GlobalStatus status = GlobalStatus.ACTIVE;
            Pageable pageable = PageRequest.of(page, size);
            List<Category> cats = List.of();
            Page<Category> expected = new PageImpl<>(cats, pageable, 0);
            when( categoryRepo.findByStatus(status, pageable)).thenReturn(expected);
            // Act
            Page<Category> result = categoryServiceDomain.findAllPageByStatus(status, pageable);

            // Assert
            assertAll(
                    () -> assertEquals(0,result.getContent().size()),
                    () -> assertEquals(0, result.getTotalElements()),
                    () -> assertTrue(result.isEmpty())
            );

            // Verify
            verify( categoryRepo).findByStatus(status, pageable);
        }
    }
}
