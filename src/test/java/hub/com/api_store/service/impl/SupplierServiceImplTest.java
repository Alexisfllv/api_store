package hub.com.api_store.service.impl;

import hub.com.api_store.dto.supplier.SupplierDTORequest;
import hub.com.api_store.dto.supplier.SupplierDTOResponse;
import hub.com.api_store.dto.supplier.SupplierDTOUpdate;
import hub.com.api_store.entity.Supplier;
import hub.com.api_store.mapper.SupplierMapper;
import hub.com.api_store.nums.CategoryStatus;
import hub.com.api_store.repo.SupplierRepo;
import hub.com.api_store.service.domain.SupplierServiceDomain;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SupplierServiceImplTest {

    @Mock
    private SupplierMapper supplierMapper;

    @Mock
    private SupplierServiceDomain supplierServiceDomain;

    @Mock
    private SupplierRepo supplierRepo;

    @InjectMocks
    private SupplierServiceImpl supplierServiceImpl;


    // helper entity and dto
    private Supplier createSupplier(Long id, String name, String phone, String email, String address, CategoryStatus status) {
        return new Supplier(id, name, phone, email, address, status);
    }

    private SupplierDTOResponse createSupplierDTO(Long id, String name, String phone, String email, String address, CategoryStatus status) {
        return new SupplierDTOResponse(id, name, phone, email, address, status);
    }

    private SupplierDTORequest createSupplierDTORequest(String name , String phone, String email, String address) {
        return new SupplierDTORequest(name, phone, email, address);
    }
    private SupplierDTOUpdate createSupplierDTOUpdate(String name , String phone, String email, String address , CategoryStatus status) {
        return new SupplierDTOUpdate(name, phone, email, address, status);
    }



    @Test
    @DisplayName("GET getSupplierId")
    void getSupplierId(){
        // Arrange
        Long supplierId = 1L;
        Supplier supplier = new Supplier
                (1L,"Fring","+51920287650","Fring@email.com","Lima-Lima", CategoryStatus.ACTIVE);
        SupplierDTOResponse supplierResponse = new SupplierDTOResponse
                (1L,"Fring","+51920287650","Fring@email.com","Lima-Lima", CategoryStatus.ACTIVE);

        when(supplierServiceDomain.findByIdSupplier(supplierId)).thenReturn(supplier);
        when(supplierMapper.toSupplierDTOResponse(supplier)).thenReturn(supplierResponse);
        // Act
        SupplierDTOResponse result = supplierServiceImpl.getSupplierId(supplierId);

        // Assert
        assertAll(
                () -> assertEquals(supplierResponse.id(),result.id()),
                () -> assertEquals(supplierResponse.name(),result.name()),
                () -> assertEquals(supplierResponse.phone(),result.phone()),
                () -> assertEquals(supplierResponse.email(),result.email()),
                () -> assertEquals(supplierResponse.address(),result.address()),
                () -> assertEquals(supplierResponse.status(),result.status())
        );
        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(supplierServiceDomain, supplierMapper);
        inOrder.verify(supplierServiceDomain).findByIdSupplier(supplierId);
        inOrder.verify(supplierMapper).toSupplierDTOResponse(supplier);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("GET getPageListSupplier")
    void getPageListSupplier(){
        // Arrange
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC,"id"));
        Supplier supplier1 = createSupplier(1L, "Fring", "+51920287650", "Fring@email.com", "Lima-Lima", CategoryStatus.ACTIVE);
        Supplier supplier2 = createSupplier(2L, "West", "+51923431112", "West@email.com", "AQP-", CategoryStatus.INACTIVE);
        List<Supplier> supplierList = List.of(supplier1, supplier2);
        Page<Supplier> pageSupplier = new PageImpl(supplierList, pageRequest, supplierList.size());

        SupplierDTOResponse dto1 = createSupplierDTO(1L, "Fring", "+51920287650", "Fring@email.com", "Lima-Lima", CategoryStatus.ACTIVE);
        SupplierDTOResponse dto2 = createSupplierDTO(2L, "West", "+51923431112", "West@email.com", "AQP-", CategoryStatus.INACTIVE);

        when(supplierRepo.findAll(pageRequest)).thenReturn(pageSupplier);
        when(supplierMapper.toSupplierDTOResponse(supplier1)).thenReturn(dto1);
        when(supplierMapper.toSupplierDTOResponse(supplier2)).thenReturn(dto2);
        // Act
        PageResponse<SupplierDTOResponse> result = supplierServiceImpl.getPageListSupplier(page,size);

        // Assert
        assertAll(
                () -> assertEquals(2,result.content().size()),
                () -> assertEquals(0,result.page()),
                () -> assertEquals(2,result.totalElements()),
                () -> assertEquals(10,result.size()),
                () -> assertEquals(1,result.totalPages())
        );

        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(supplierRepo, supplierMapper);
        inOrder.verify(supplierRepo).findAll(pageRequest);
        inOrder.verify(supplierMapper).toSupplierDTOResponse(supplier1);
        inOrder.verify(supplierMapper).toSupplierDTOResponse(supplier2);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("POST addSupplier")
    void addSupplier(){
        // Arrange
        SupplierDTORequest supplierDTORequest = createSupplierDTORequest("Fring","+51920287650","Fring@email.com","Lima-Lima");
        Supplier supplierempty = createSupplier(null,"Fring","+51920287650","Fring@email.com","Lima-Lima",null);
        Supplier supplier1 = createSupplier(1L, "Fring", "+51920287650", "Fring@email.com", "Lima-Lima", CategoryStatus.ACTIVE);
        SupplierDTOResponse dto1 = createSupplierDTO(1L, "Fring", "+51920287650", "Fring@email.com", "Lima-Lima", CategoryStatus.ACTIVE);

        when(supplierMapper.toSupplier(supplierDTORequest)).thenReturn(supplierempty);
        when(supplierRepo.save(supplierempty)).thenReturn(supplier1);
        when(supplierMapper.toSupplierDTOResponse(supplier1)).thenReturn(dto1);
        // Act
        SupplierDTOResponse result = supplierServiceImpl.addSupplier(supplierDTORequest);
        // Assert
        assertAll(
                () -> assertEquals(dto1.id(),result.id()),
                () -> assertEquals(dto1.name(),result.name()),
                () -> assertEquals(dto1.phone(),result.phone()),
                () -> assertEquals(dto1.email(),result.email()),
                () -> assertEquals(dto1.address(),result.address()),
                () -> assertEquals(dto1.status(),result.status())
        );

        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(supplierRepo, supplierMapper);
        inOrder.verify(supplierMapper).toSupplier(supplierDTORequest);
        inOrder.verify(supplierRepo).save(supplierempty);
        inOrder.verify(supplierMapper).toSupplierDTOResponse(supplier1);
        inOrder.verifyNoMoreInteractions();
    }

    @Nested
    @DisplayName("PUT updateSupplier")
    class UpdateSupplier{
        @Test
        @DisplayName("updateSupplier")
        void updateSupplier(){
            // Arrange
            SupplierDTOUpdate update = createSupplierDTOUpdate("Demo","+51000000000","Demo@email.com","Lima-Lima",CategoryStatus.ACTIVE);
            Supplier updateEntity = createSupplier(null,"Demo","+51000000000","Demo@email.com","Lima-Lima",CategoryStatus.ACTIVE);

            Supplier supplier1 = createSupplier(1L, "Fring", "+51920287650", "Fring@email.com", "Lima-Lima", CategoryStatus.ACTIVE);
            SupplierDTOResponse dto1 = createSupplierDTO(1L,"Demo","+51000000000","Demo@email.com","Lima-Lima",CategoryStatus.ACTIVE);
            when(supplierServiceDomain.findByIdSupplier(1L)).thenReturn(supplier1);
            when(supplierRepo.save(any(Supplier.class))).thenReturn(updateEntity);
            when(supplierMapper.toSupplierDTOResponse(updateEntity)).thenReturn(dto1);

            // Act
            SupplierDTOResponse result = supplierServiceImpl.updateSupplier(1L,update);

            // Assert
            assertAll(
                    () -> assertEquals(dto1.id(),result.id()),
                    () -> assertEquals(dto1.name(),result.name()),
                    () -> assertEquals(dto1.phone(),result.phone()),
                    () -> assertEquals(dto1.email(),result.email()),
                    () -> assertEquals(dto1.address(),result.address()),
                    () -> assertEquals(dto1.status(),result.status())
            );

            // InOrder & Verify
            InOrder inOrder = Mockito.inOrder(supplierServiceDomain, supplierRepo,supplierMapper);
            inOrder.verify(supplierServiceDomain).findByIdSupplier(1L);
            inOrder.verify(supplierRepo).save(any(Supplier.class));
            inOrder.verify(supplierMapper).toSupplierDTOResponse(updateEntity);
            inOrder.verifyNoMoreInteractions();
        }

        @Test
        @DisplayName("PUT updateSupplier - solo cambia status (mismo phone y email)")
        void updateSupplierOnlyStatus(){
            // Arrange - phone y email IGUALES
            SupplierDTOUpdate update = createSupplierDTOUpdate("Demo","+51920287650","Fring@email.com","Lima-Lima",CategoryStatus.INACTIVE);
            Supplier supplier1 = createSupplier(1L, "Fring", "+51920287650", "Fring@email.com", "Lima-Lima", CategoryStatus.ACTIVE);
            SupplierDTOResponse dto1 = createSupplierDTO(1L,"Demo","+51920287650","Fring@email.com","Lima-Lima",CategoryStatus.INACTIVE);

            when(supplierServiceDomain.findByIdSupplier(1L)).thenReturn(supplier1);
            when(supplierRepo.save(any(Supplier.class))).thenReturn(supplier1);
            when(supplierMapper.toSupplierDTOResponse(any())).thenReturn(dto1);

            // Act
            SupplierDTOResponse result = supplierServiceImpl.updateSupplier(1L,update);

            // Assert
            assertEquals(dto1, result);

            // Verify - NO debe llamar validaciones
            verify(supplierServiceDomain, never()).validateExistsByPhone(anyString());
            verify(supplierServiceDomain, never()).validateExistsByEmail(anyString());
        }
    }

    @Test
    @DisplayName("DELETE deleteSupplierDelete")
    void deleteSupplierDelete(){
        // Arrange
        Long idSupplier = 1L;
        Supplier supplierExist = createSupplier(1L,"West","+51920287650","west@email.com","Lima...",CategoryStatus.ACTIVE);
        Supplier supplierSofDelete = createSupplier(1L,"West","+51920287650","west@email.com","Lima...",CategoryStatus.DELETED);
        when(supplierServiceDomain.findByIdSupplier(idSupplier)).thenReturn(supplierExist);
        when(supplierRepo.save(any(Supplier.class))).thenReturn(supplierSofDelete);
        // Act
        supplierServiceImpl.deleteSupplier(idSupplier);
        // Assert
        assertEquals(CategoryStatus.DELETED,supplierSofDelete.getStatus());
        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(supplierServiceDomain, supplierRepo);
        inOrder.verify(supplierServiceDomain).findByIdSupplier(idSupplier);
        inOrder.verify(supplierRepo).save(any(Supplier.class));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("GET getListSupplierByName")
    void getListSupplierByName(){
        // Arrange
        String name = "We";
        Integer limit = 10;
        Supplier sup1 = createSupplier(2L,"Well","51987654321","Well@email.com","Lima-Lima",CategoryStatus.ACTIVE);
        Supplier sup2 = createSupplier(8L,"West","51983737322","West@email.com","Lima-Ate",CategoryStatus.INACTIVE);
        SupplierDTOResponse supd1 = createSupplierDTO(2L,"Well","51987654321","Well@email.com","Lima-Lima",CategoryStatus.ACTIVE);
        SupplierDTOResponse supd2 = createSupplierDTO(8L,"West","51983737322","West@email.com","Lima-Ate",CategoryStatus.INACTIVE);

        List<Supplier> suppliers = List.of(sup1,sup2);
        when(supplierRepo.findByNameContainingIgnoreCase(name)).thenReturn(suppliers);
        when(supplierMapper.toSupplierDTOResponse(sup1)).thenReturn(supd1);
        when(supplierMapper.toSupplierDTOResponse(sup2)).thenReturn(supd2);
        // Act
        List<SupplierDTOResponse> resultList = supplierServiceImpl.getListSupplierByName(name,limit);
        // Assert
        assertAll(
                () -> assertNotNull(resultList),
                () -> assertEquals(2,resultList.size()),
                () -> assertEquals("Well", resultList.get(0).name()),
                () -> assertEquals("West", resultList.get(1).name())
        );

        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(supplierRepo, supplierMapper);
        inOrder.verify(supplierRepo).findByNameContainingIgnoreCase(name);
        inOrder.verify(supplierMapper).toSupplierDTOResponse(sup1);
        inOrder.verify(supplierMapper).toSupplierDTOResponse(sup2);
        inOrder.verifyNoMoreInteractions();

    }

    @Test
    @DisplayName("GET getListSupplierStatus")
    void getListSupplierStatus(){
        // Arrange
        String status = "ACTIVE";
        Integer limit = 10;
        CategoryStatus enumStatus =  CategoryStatus.valueOf(status.toUpperCase());
        Supplier sup1 = createSupplier(2L,"Well","51987654321","Well@email.com","Lima-Lima",CategoryStatus.ACTIVE);
        Supplier sup2 = createSupplier(8L,"West","51983737322","West@email.com","Lima-Ate",CategoryStatus.ACTIVE);
        SupplierDTOResponse supd1 = createSupplierDTO(2L,"Well","51987654321","Well@email.com","Lima-Lima",CategoryStatus.ACTIVE);
        SupplierDTOResponse supd2 = createSupplierDTO(8L,"West","51983737322","West@email.com","Lima-Ate",CategoryStatus.ACTIVE);

        List<Supplier> suppliers = List.of(sup1,sup2);
        when(supplierRepo.findByStatus(enumStatus)).thenReturn(suppliers);
        when(supplierMapper.toSupplierDTOResponse(sup1)).thenReturn(supd1);
        when(supplierMapper.toSupplierDTOResponse(sup2)).thenReturn(supd2);
        // Act
        List<SupplierDTOResponse> resultList = supplierServiceImpl.getListSupplierByStatus(status,limit);
        // Assert
        assertAll(
                () -> assertNotNull(resultList),
                () -> assertEquals(2,resultList.size()),
                () -> assertEquals(CategoryStatus.ACTIVE,resultList.get(0).status()),
                () -> assertEquals(CategoryStatus.ACTIVE,resultList.get(1).status())
        );

        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(supplierRepo, supplierMapper);
        inOrder.verify(supplierRepo).findByStatus(enumStatus);
        inOrder.verify(supplierMapper).toSupplierDTOResponse(sup1);
        inOrder.verify(supplierMapper).toSupplierDTOResponse(sup2);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("PATCH changeStatusSupplier")
    void changeStatusSupplier(){
        // Arrange
        Long id = 1L;
        String status = "INACTIVE";
        CategoryStatus enumStatus = CategoryStatus.valueOf(status.toUpperCase());
        Supplier supplierExist = createSupplier(1L,"Fring","+51920287650","fring@email.com","Lima-Lima",CategoryStatus.ACTIVE);
        Supplier supplierUpdated = createSupplier(1L,"Fring","+51920287650","fring@email.com","Lima-Lima",CategoryStatus.INACTIVE);
        SupplierDTOResponse supplierDTOResponse = createSupplierDTO(1L,"Fring","+51920287650","fring@email.com","Lima-Lima",CategoryStatus.INACTIVE);
        when(supplierServiceDomain.findByIdSupplier(id)).thenReturn(supplierExist);
        when(supplierRepo.save(supplierExist)).thenReturn(supplierUpdated);
        when(supplierMapper.toSupplierDTOResponse(supplierUpdated)).thenReturn(supplierDTOResponse);

        // Act
        SupplierDTOResponse result = supplierServiceImpl.changeStatusSupplier(id, enumStatus);

        // Assert
        assertAll(
                () -> assertEquals(supplierUpdated.getId(), result.id()),
                () -> assertEquals(supplierUpdated.getName(), result.name()),
                () -> assertEquals(supplierUpdated.getPhone(), result.phone()),
                () -> assertEquals(supplierUpdated.getEmail(), result.email()),
                () -> assertEquals(supplierUpdated.getAddress(), result.address()),
                () -> assertEquals(supplierUpdated.getStatus(), result.status())
        );

        // InOrder & Verify
        InOrder inOrder = Mockito.inOrder(supplierServiceDomain, supplierRepo, supplierMapper);
        inOrder.verify(supplierServiceDomain).findByIdSupplier(id);
        inOrder.verify(supplierRepo).save(any(Supplier.class));
        inOrder.verify(supplierMapper).toSupplierDTOResponse(supplierUpdated);
        inOrder.verifyNoMoreInteractions();
    }
}
