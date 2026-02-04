package hub.com.api_store.service.impl;

import hub.com.api_store.dto.supplier.SupplierDTOResponse;
import hub.com.api_store.entity.Supplier;
import hub.com.api_store.mapper.SupplierMapper;
import hub.com.api_store.nums.CategoryStatus;
import hub.com.api_store.service.domain.SupplierServiceDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

    @InjectMocks
    private SupplierServiceImpl supplierServiceImpl;

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
}
