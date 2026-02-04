package hub.com.api_store.mapper;

import hub.com.api_store.dto.supplier.SupplierDTOResponse;
import hub.com.api_store.entity.Supplier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    // toResponse
    SupplierDTOResponse toSupplierDTOResponse(Supplier supplier);
}
