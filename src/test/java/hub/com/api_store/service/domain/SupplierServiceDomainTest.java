package hub.com.api_store.service.domain;

import hub.com.api_store.entity.Supplier;
import hub.com.api_store.exception.ResourceNotFoundException;
import hub.com.api_store.exception.UniqueValidateException;
import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.nums.ExceptionMessages;
import hub.com.api_store.repo.SupplierRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SupplierServiceDomainTest {

    @Mock
    private SupplierRepo supplierRepo;

    @InjectMocks
    private SupplierServiceDomain supplierServiceDomain;

    @Nested
    @DisplayName("Test findByIdSupplier")
    class FindByIdSupplierTest{
        @Test
        @DisplayName("Should return Supplier Id Exists")
        public void testFindByIdSupplier(){
            // Arrange
            Long idExist = 1L;
            Supplier supplier = new Supplier
                    (1L,"Fring","+51920287650","Fring@email.com","Lima-Lima", GlobalStatus.ACTIVE);

            when(supplierRepo.findById(idExist)).thenReturn(Optional.of(supplier));

            // Act
            Supplier result = supplierServiceDomain.findByIdSupplier(idExist);

            // Assert
            assertAll(
                    () -> assertEquals(supplier.getId(),result.getId()),
                    () -> assertEquals(supplier.getName(),result.getName()),
                    () -> assertEquals(supplier.getPhone(),result.getPhone()),
                    () -> assertEquals(supplier.getEmail(),result.getEmail()),
                    () -> assertEquals(supplier.getAddress(),result.getAddress()),
                    () -> assertEquals(supplier.getStatus(),result.getStatus())
            );

            // InOrder - Verify
            InOrder inOrder = Mockito.inOrder(supplierRepo);
            inOrder.verify(supplierRepo).findById(idExist);
            inOrder.verifyNoMoreInteractions();
        }
        @Test
        @DisplayName("Should return Supplier Id not Exists")
        public void testFindByIdSupplierNotExist(){
            // Arrange
            Long idNotExist = 99L;
            when(supplierRepo.findById(idNotExist)).thenReturn(Optional.empty());
            // Act
            ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                    () -> supplierServiceDomain.findByIdSupplier(idNotExist));

            // Assert
            assertAll(
                    () -> assertNotNull(ex),
                    () -> assertNotNull(ex.getMessage().contains(ExceptionMessages.RESOURCE_NOT_FOUND_ERROR.message())),
                    () -> assertNotNull(ex.getMessage().contains(idNotExist.toString()))
            );
            // InOrder - Verify
            InOrder inOrder = Mockito.inOrder(supplierRepo);
            inOrder.verify(supplierRepo).findById(idNotExist);
            inOrder.verifyNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("Test validateExistsByPhone")
    class ValidateExistsByPhone{
        @Test
        @DisplayName("Test validateExistsByPhone Success")
        void testValidateExistsByPhoneSuccess(){
            // Arrange
            String phone = "912837465";
            when(supplierRepo.existsByPhone(phone)).thenReturn(false);
            // Act & Assert
            assertDoesNotThrow(
                    () -> supplierServiceDomain.validateExistsByPhone(phone)
            );

            // Verify
            verify(supplierRepo).existsByPhone(phone);
            verifyNoMoreInteractions(supplierRepo);
        }

        @Test
        @DisplayName("Test validateExistsByPhone Throw")
        void testValidateExistsByPhoneThrow(){
            // Arrange
            String phone = "912837465";
            when( supplierRepo.existsByPhone(phone)).thenReturn(true);
            // Act
            UniqueValidateException ex = assertThrows(UniqueValidateException.class,
                    () -> supplierServiceDomain.validateExistsByPhone(phone));
            // Assert
            assertAll(
                    () -> assertNotNull(ex),
                    () -> assertTrue(ex.getMessage().contains(ExceptionMessages.UNIQUE_EXC.message())),
                    () -> assertTrue(ex.getMessage().contains(phone))
            );
            verify(supplierRepo).existsByPhone(phone);
            verifyNoMoreInteractions(supplierRepo);
        }
    }

    @Nested
    @DisplayName("Test validateExistsByEmail")
    class ValidateExistsByEmail{
        @Test
        @DisplayName("Test validateExistsByEmail Success")
        void testValidateExistsByEmailSuccess(){
            // Arrange
            String email = "West@email.com";
            when(supplierRepo.existsByEmail(email)).thenReturn(false);
            // Act & Assert
            assertDoesNotThrow( ()-> supplierServiceDomain.validateExistsByEmail(email));
            // Verify
            verify(supplierRepo).existsByEmail(email);
            verifyNoMoreInteractions(supplierRepo);
        }

        @Test
        @DisplayName("Test validateExistsByEmail Throw")
        void testValidateExistsByEmailThrow(){
            // Arrange
            String email = "west@email.com";
            when( supplierRepo.existsByEmail(email)).thenReturn(true);
            // Act
            UniqueValidateException ex = assertThrows(UniqueValidateException.class,
                    () -> supplierServiceDomain.validateExistsByEmail(email));

            // Assert
            assertAll(
                    () -> assertNotNull(ex),
                    () -> assertTrue(ex.getMessage().contains(ExceptionMessages.UNIQUE_EXC.message())),
                    () -> assertTrue(ex.getMessage().contains(email))
            );

            // Verify
            verify(supplierRepo).existsByEmail(email);
            verifyNoMoreInteractions(supplierRepo);
        }
    }
}
