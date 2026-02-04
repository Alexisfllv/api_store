package hub.com.api_store.mapper;

import hub.com.api_store.dto.supplier.SupplierDTOResponse;
import hub.com.api_store.entity.Supplier;
import hub.com.api_store.nums.CategoryStatus;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SupplierMapperTest {
    private SupplierMapper supplierMapper;

    @BeforeEach
    public void setup() {
        supplierMapper = Mappers.getMapper(SupplierMapper.class);
    }

    @Nested
    @DisplayName("Test toResponse response <- entity")
    class toResponse{

        @Test
        @DisplayName("Test toResponse response <- entity")
        public void toResponse(){
            // Arrange
            Supplier supplier = new Supplier
                    (1L,"Fring","+51920287650","Fring@email.com","Lima-Lima", CategoryStatus.ACTIVE);
            SupplierDTOResponse supplierDTOResponse = new SupplierDTOResponse
                    (1L,"Fring","+51920287650","Fring@email.com","Lima-Lima", CategoryStatus.ACTIVE);

            // Act
            SupplierDTOResponse result = supplierMapper.toSupplierDTOResponse(supplier);

            // Assert
            assertAll(
                    () -> assertEquals(supplierDTOResponse.id(),result.id()),
                    () -> assertEquals(supplierDTOResponse.name(),result.name()),
                    () -> assertEquals(supplierDTOResponse.phone(),result.phone()),
                    () -> assertEquals(supplierDTOResponse.email(),result.email()),
                    () -> assertEquals(supplierDTOResponse.address() ,result.address()),
                    () -> assertEquals(supplierDTOResponse.status(),result.status())
            );
        }

        @Test
        @DisplayName("Test toResponse response <- entity null")
        public void toResponseNull(){
            // Act
            SupplierDTOResponse result = supplierMapper.toSupplierDTOResponse(null);
            // Assert
            assertNull(result);
        }
    }
}
