package hub.com.api_store.mapper;

import hub.com.api_store.dto.supplier.SupplierDTORequest;
import hub.com.api_store.dto.supplier.SupplierDTOResponse;
import hub.com.api_store.entity.Supplier;
import hub.com.api_store.nums.GlobalStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

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
                    (1L,"Fring","+51920287650","Fring@email.com","Lima-Lima", GlobalStatus.ACTIVE);
            SupplierDTOResponse supplierDTOResponse = new SupplierDTOResponse
                    (1L,"Fring","+51920287650","Fring@email.com","Lima-Lima", GlobalStatus.ACTIVE);

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

    @Nested
    @DisplayName("Test toEntity entity <- request")
    class toEntity{

        @Test
        @DisplayName("toEntity")
        public void toEntity(){
            // Arrange

            SupplierDTORequest supplierDTORequest = new SupplierDTORequest(
                    "Fring","987654321","fring@email.com","Lima-Lima");

            Supplier supplier = new Supplier
                    (null,"Fring","+51920287650","Fring@email.com","Lima-Lima", null);
            // Act
            Supplier result = supplierMapper.toSupplier(supplierDTORequest);

            // Assert
            assertAll(
                    () -> assertEquals(supplierDTORequest.name(),result.getName()),
                    () -> assertEquals(supplierDTORequest.phone(),result.getPhone()),
                    () -> assertEquals(supplierDTORequest.email(),result.getEmail()),
                    () -> assertEquals(supplierDTORequest.address() ,result.getAddress())
            );
        }

        @Test
        @DisplayName("toEntity null")
        public void toEntityNull(){
            // Act
            Supplier result = supplierMapper.toSupplier(null);
            // Assert
            assertNull(result);
        }

    }
}
