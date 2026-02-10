package hub.com.api_store.service.impl;

import hub.com.api_store.dto.product.ProductDTORequest;
import hub.com.api_store.dto.product.ProductDTOResponse;
import hub.com.api_store.dto.product.ProductDTOUpdate;
import hub.com.api_store.entity.Category;
import hub.com.api_store.entity.Product;
import hub.com.api_store.mapper.ProductMapper;
import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.nums.GlobalUnit;
import hub.com.api_store.repo.CategoryRepo;
import hub.com.api_store.repo.ProductRepo;
import hub.com.api_store.service.domain.CategoryServiceDomain;
import hub.com.api_store.service.domain.ProductServiceDomain;
import hub.com.api_store.util.page.PageResponse;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductServiceDomain productServiceDomain;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductRepo productRepo;
    @Mock
    private CategoryServiceDomain categoryServiceDomain;

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

    @Test
    @DisplayName("getPageFindAllProducts Test")
    void getPageFindAllProductsGet(){
        // Arrange
        int page = 0;
        int size = 10;
        String prop = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, prop));
        Category category = new Category(1L,"name","description", GlobalStatus.ACTIVE);
        Product product = new Product(1L,"name", GlobalUnit.KG,GlobalStatus.ACTIVE,category);
        List<Product> productList = List.of(product);
        ProductDTOResponse productDTOResponse = new ProductDTOResponse(1L,"name", GlobalUnit.KG,GlobalStatus.ACTIVE,product.getCategory().getId(),product.getCategory().getName());
        Page<Product> pageProducts = new PageImpl<>(productList,pageable,productList.size());

        when(productRepo.findAll(pageable)).thenReturn(pageProducts);
        when(productMapper.toProductDTOResponse(product)).thenReturn(productDTOResponse);
        // Act
        PageResponse<ProductDTOResponse> resultList = productServiceImpl.getPageFindAllProducts(page,size,prop);
        // Assert
        assertAll(
                () -> assertEquals(1,resultList.content().size()),
                () -> assertEquals(productDTOResponse.id(),resultList.content().get(0).id()),
                () -> assertEquals(productDTOResponse.name(),resultList.content().get(0).name()),
                () -> assertEquals(productDTOResponse.unit(),resultList.content().get(0).unit()),
                () -> assertEquals(productDTOResponse.status(),resultList.content().get(0).status()),
                () -> assertEquals(productDTOResponse.categoryId(),resultList.content().get(0).categoryId()),
                () -> assertEquals(productDTOResponse.categoryName(),resultList.content().get(0).categoryName()),
                () -> assertEquals(page,resultList.page()),
                () -> assertEquals(size,resultList.size()),
                () -> assertEquals(productList.size(),resultList.totalElements()),
                () -> assertEquals(1,resultList.totalPages())
        );
        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(productRepo, productMapper);
        inOrder.verify(productRepo).findAll(pageable);
        inOrder.verify(productMapper).toProductDTOResponse(product);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("addProduct Test")
    void addProduct(){
        // Arrange
        Category category = new Category(1L,"name","description", GlobalStatus.ACTIVE);

        ProductDTORequest productDTORequest = new ProductDTORequest("name", GlobalUnit.KG,category.getId());
        Product product = new Product(1L,"name", GlobalUnit.KG,GlobalStatus.ACTIVE,category);
        ProductDTOResponse productDTOResponse = new ProductDTOResponse
                (1L,"name", GlobalUnit.KG,GlobalStatus.ACTIVE,product.getCategory().getId(),product.getCategory().getName());
        when(categoryServiceDomain.findByIdCategory(category.getId())).thenReturn(category);
        when(productMapper.toProduct(productDTORequest,category)).thenReturn(product);
        when(productRepo.save(product)).thenReturn(product);
        when(productMapper.toProductDTOResponse(product)).thenReturn(productDTOResponse);

        // Act
        ProductDTOResponse result = productServiceImpl.addProduct(productDTORequest);
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
        InOrder inOrder = Mockito.inOrder(categoryServiceDomain, productMapper, productRepo);
        inOrder.verify(categoryServiceDomain).findByIdCategory(category.getId());
        inOrder.verify(productMapper).toProduct(productDTORequest,category);
        inOrder.verify(productRepo).save(product);
        inOrder.verify(productMapper).toProductDTOResponse(product);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("updateProduct Test")
    void updateProduct(){
        // Arrange
        Long productId = 1L;
        Category originalCategory = new Category(1L,"Lacteos","desc", GlobalStatus.ACTIVE);
        Product product = new Product(productId,"Arroz", GlobalUnit.KG, GlobalStatus.ACTIVE, originalCategory);


        Category newCategory = new Category(2L,"Panaderia","desc update", GlobalStatus.ACTIVE);

        ProductDTOUpdate productDTOUpdate = new ProductDTOUpdate(
                "Trigo trigal",
                GlobalUnit.LITER,
                GlobalStatus.INACTIVE,
                newCategory.getId()
        );

        ProductDTOResponse expectedResponse = new ProductDTOResponse(
                productId,
                "Trigo trigal",
                GlobalUnit.LITER,
                GlobalStatus.INACTIVE,
                newCategory.getId(),
                newCategory.getName()
        );


        when(productServiceDomain.findById(productId)).thenReturn(product);
        when(categoryServiceDomain.findByIdCategory(newCategory.getId())).thenReturn(newCategory);
        when(productRepo.save(product)).thenReturn(product);
        when(productMapper.toProductDTOResponse(product)).thenReturn(expectedResponse);

        // Act
        ProductDTOResponse result = productServiceImpl.updateProduct(productId,productDTOUpdate);
        // Assert
        assertAll(
                () -> assertEquals(expectedResponse.id(),result.id()),
                () -> assertEquals(expectedResponse.name(),result.name()),
                () -> assertEquals(expectedResponse.unit(),result.unit()),
                () -> assertEquals(expectedResponse.status(),result.status()),
                () -> assertEquals(expectedResponse.categoryId(),result.categoryId()),
                () -> assertEquals(expectedResponse.categoryName(),result.categoryName())
        );

        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(productRepo, productMapper,productServiceDomain,categoryServiceDomain);
        inOrder.verify(productServiceDomain).findById(productId);
        inOrder.verify(categoryServiceDomain).findByIdCategory(newCategory.getId());
        inOrder.verify(productRepo).save(product);
        inOrder.verify(productMapper).toProductDTOResponse(product);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("deleteProductById Test")
    void deleteProductById(){
        // Arrange
        Long productId = 1L;
        Category category = new Category(1L,"Lacteos","desc", GlobalStatus.ACTIVE);
        Product product = new Product(productId,"Arroz", GlobalUnit.KG, GlobalStatus.ACTIVE, category);

        when(productServiceDomain.findById(productId)).thenReturn(product);
        when(productRepo.save(product)).thenReturn(product);

        // Act
        productServiceImpl.deleteProductById(productId);
        // Assert
        assertEquals(GlobalStatus.DELETED,product.getStatus());

        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(productServiceDomain, productRepo);
        inOrder.verify(productServiceDomain).findById(productId);
        inOrder.verify(productRepo).save(product);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("findProductListByCategoryId Test")
    void  findProductListByCategoryId(){
        // Arrange
        Long categoryid = 1L;
        int limit = 5;
        Category category = new Category(1L,"Lacteos","desc", GlobalStatus.ACTIVE);
        Product product1 = new Product(1L,"Arroz", GlobalUnit.KG, GlobalStatus.ACTIVE, category);
        Product product2 = new Product(2L,"Leche", GlobalUnit.LITER, GlobalStatus.ACTIVE, category);
        List<Product> productList = List.of(product1,product2);
        ProductDTOResponse productDTOResponse1 = new ProductDTOResponse(1L,"Arroz", GlobalUnit.KG, GlobalStatus.ACTIVE,category.getId(),category.getName());
        ProductDTOResponse productDTOResponse2 = new ProductDTOResponse(2L,"Leche", GlobalUnit.LITER, GlobalStatus.ACTIVE,category.getId(),category.getName());
        List<ProductDTOResponse> productDTOResponseList = List.of(productDTOResponse1,productDTOResponse2);

        when(categoryServiceDomain.findByIdCategory(categoryid)).thenReturn(category);
        when(productRepo.findByCategoryId(categoryid)).thenReturn(productList);
        when(productMapper.toProductDTOResponse(product1)).thenReturn(productDTOResponse1);
        when(productMapper.toProductDTOResponse(product2)).thenReturn(productDTOResponse2);
        // Act
        List<ProductDTOResponse> resultList = productServiceImpl.findProductListByCategoryId(categoryid,limit);

        // Assert
        assertAll(
                () -> assertEquals(productDTOResponseList.size(),resultList.size()),
                () -> assertEquals(productDTOResponseList.get(0).id(),resultList.get(0).id()),
                () -> assertEquals(productDTOResponseList.get(0).name(),resultList.get(0).name()),
                () -> assertEquals(productDTOResponseList.get(0).unit(),resultList.get(0).unit()),
                () -> assertEquals(productDTOResponseList.get(0).status(),resultList.get(0).status()),
                () -> assertEquals(productDTOResponseList.get(0).categoryId(),resultList.get(0).categoryId()),
                () -> assertEquals(productDTOResponseList.get(0).categoryName(),resultList.get(0).categoryName()),
                () -> assertEquals(productDTOResponseList.get(1).id(),resultList.get(1).id()),
                () -> assertEquals(productDTOResponseList.get(1).name(),resultList.get(1).name()),
                () -> assertEquals(productDTOResponseList.get(1).unit(),resultList.get(1).unit()),
                () -> assertEquals(productDTOResponseList.get(1).status(),resultList.get(1).status()),
                () -> assertEquals(productDTOResponseList.get(1).categoryId(),resultList.get(1).categoryId()),
                () -> assertEquals(productDTOResponseList.get(1).categoryName(),resultList.get(1).categoryName())
        );

        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(categoryServiceDomain,productMapper,productRepo);
        inOrder.verify(categoryServiceDomain).findByIdCategory(categoryid);
        inOrder.verify(productRepo).findByCategoryId(categoryid);
        inOrder.verify(productMapper).toProductDTOResponse(product1);
        inOrder.verify(productMapper).toProductDTOResponse(product2);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("findProductListByName Test")
    void findProductListByName() {
        // Arrange
        String name = "Arroz";
        int limit = 5;
        Category category = new Category(1L,"Trigo","desc", GlobalStatus.ACTIVE);
        Product product1 = new Product(1L,"Arroz", GlobalUnit.KG, GlobalStatus.ACTIVE, category);
        Product product2 = new Product(2L,"Arroz integral", GlobalUnit.KG, GlobalStatus.ACTIVE, category);
        List<Product> productList = List.of(product1,product2);
        ProductDTOResponse productDTOResponse1 = new ProductDTOResponse(1L,"Arroz", GlobalUnit.KG, GlobalStatus.ACTIVE,category.getId(),category.getName());
        ProductDTOResponse productDTOResponse2 = new ProductDTOResponse(2L,"Arroz integral", GlobalUnit.KG, GlobalStatus.ACTIVE,category.getId(),category.getName());
        List<ProductDTOResponse> productDTOResponseList = List.of(productDTOResponse1,productDTOResponse2);

        when(productRepo.findByNameContainingIgnoreCase(name)).thenReturn(productList);
        when(productMapper.toProductDTOResponse(product1)).thenReturn(productDTOResponse1);
        when(productMapper.toProductDTOResponse(product2)).thenReturn(productDTOResponse2);
        // Act
        List<ProductDTOResponse> resultList = productServiceImpl.findProductListByName(name,limit);
        // Assert
        assertAll(
                () -> assertEquals(productDTOResponseList.size(),resultList.size()),
                () -> assertEquals(productDTOResponseList.get(0).id(),resultList.get(0).id()),
                () -> assertEquals(productDTOResponseList.get(0).name(),resultList.get(0).name()),
                () -> assertEquals(productDTOResponseList.get(0).unit(),resultList.get(0).unit()),
                () -> assertEquals(productDTOResponseList.get(0).status(),resultList.get(0).status()),
                () -> assertEquals(productDTOResponseList.get(0).categoryId(),resultList.get(0).categoryId()),
                () -> assertEquals(productDTOResponseList.get(0).categoryName(),resultList.get(0).categoryName()),
                () -> assertEquals(productDTOResponseList.get(1).id(),resultList.get(1).id()),
                () -> assertEquals(productDTOResponseList.get(1).name(),resultList.get(1).name()),
                () -> assertEquals(productDTOResponseList.get(1).unit(),resultList.get(1).unit()),
                () -> assertEquals(productDTOResponseList.get(1).status(),resultList.get(1).status()),
                () -> assertEquals(productDTOResponseList.get(1).categoryId(),resultList.get(1).categoryId()),
                () -> assertEquals(productDTOResponseList.get(1).categoryName(),resultList.get(1).categoryName())
        );
        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(productRepo, productMapper);
        inOrder.verify(productRepo).findByNameContainingIgnoreCase(name);
        inOrder.verify(productMapper).toProductDTOResponse(product1);
        inOrder.verify(productMapper).toProductDTOResponse(product2);
        inOrder.verifyNoMoreInteractions();


    }

    @Test
    @DisplayName("findProductListByStatusGet Test")
    void findProductListByStatusGet() {
        // Arrange
        GlobalStatus status = GlobalStatus.ACTIVE;
        int limit = 5;
        Category category = new Category(1L, "Trigo", "desc", GlobalStatus.ACTIVE);
        Product product1 = new Product(1L, "Arroz", GlobalUnit.KG, GlobalStatus.ACTIVE, category);
        Product product2 = new Product(2L, "Arroz integral", GlobalUnit.KG, GlobalStatus.ACTIVE, category);
        List<Product> productList = List.of(product1, product2);
        ProductDTOResponse productDTOResponse1 = new ProductDTOResponse(1L, "Arroz", GlobalUnit.KG, GlobalStatus.ACTIVE, category.getId(), category.getName());
        ProductDTOResponse productDTOResponse2 = new ProductDTOResponse(2L, "Arroz integral", GlobalUnit.KG, GlobalStatus.ACTIVE, category.getId(), category.getName());
        List<ProductDTOResponse> productDTOResponseList = List.of(productDTOResponse1, productDTOResponse2);

        when(productRepo.findByStatus(GlobalStatus.ACTIVE)).thenReturn(productList);
        when(productMapper.toProductDTOResponse(product1)).thenReturn(productDTOResponse1);
        when(productMapper.toProductDTOResponse(product2)).thenReturn(productDTOResponse2);
        // Act
        List<ProductDTOResponse> resultList = productServiceImpl.findProductListByStatus(status, limit);
        // Assert
        assertAll(
                () -> assertEquals(productDTOResponseList.size(), resultList.size()),
                () -> assertEquals(productDTOResponseList.get(0).id(), resultList.get(0).id()),
                () -> assertEquals(productDTOResponseList.get(0).name(), resultList.get(0).name()),
                () -> assertEquals(productDTOResponseList.get(0).unit(), resultList.get(0).unit()),
                () -> assertEquals(productDTOResponseList.get(0).status(), resultList.get(0).status()),
                () -> assertEquals(productDTOResponseList.get(0).categoryId(), resultList.get(0).categoryId()),
                () -> assertEquals(productDTOResponseList.get(0).categoryName(), resultList.get(0).categoryName()),
                () -> assertEquals(productDTOResponseList.get(1).id(), resultList.get(1).id()),
                () -> assertEquals(productDTOResponseList.get(1).name(), resultList.get(1).name()),
                () -> assertEquals(productDTOResponseList.get(1).unit(), resultList.get(1).unit()),
                () -> assertEquals(productDTOResponseList.get(1).status(), resultList.get(1).status()),
                () -> assertEquals(productDTOResponseList.get(1).categoryId(), resultList.get(1).categoryId()),
                () -> assertEquals(productDTOResponseList.get(1).categoryName(), resultList.get(1).categoryName())
        );
        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(productRepo, productMapper);
        inOrder.verify(productRepo).findByStatus(GlobalStatus.ACTIVE);
        inOrder.verify(productMapper).toProductDTOResponse(product1);
        inOrder.verify(productMapper).toProductDTOResponse(product2);
        inOrder.verifyNoMoreInteractions();
    }
}
