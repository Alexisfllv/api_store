package hub.com.api_store.mapper;

import hub.com.api_store.dto.inventory.InventoryDTOResponse;
import hub.com.api_store.entity.Inventory;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    public InventoryDTOResponse toInventoryDTOResponse(Inventory inventory){
        return new InventoryDTOResponse(
                inventory.getId(),
                inventory.getQuantity(),
                inventory.getUnit(),
                inventory.getWarehouse(),
                inventory.getLot(),
                inventory.getExpirationDate(),
                inventory.getProduct().getId(),
                inventory.getProduct().getName()
        );
    }
}
