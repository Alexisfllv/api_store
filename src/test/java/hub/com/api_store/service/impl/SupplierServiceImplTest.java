package hub.com.api_store.service.impl;

import hub.com.api_store.dto.supplier.SupplierDTOResponse;
import hub.com.api_store.entity.Supplier;
import hub.com.api_store.mapper.SupplierMapper;
import hub.com.api_store.nums.CategoryStatus;
import hub.com.api_store.repo.SupplierRepo;
import hub.com.api_store.service.domain.SupplierServiceDomain;
import hub.com.api_store.util.page.PageResponse;
import org.junit.jupiter.api.DisplayName;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
}
