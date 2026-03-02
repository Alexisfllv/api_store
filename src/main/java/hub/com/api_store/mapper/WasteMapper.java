package hub.com.api_store.mapper;


import hub.com.api_store.dto.waste.WasteDTORequest;
import hub.com.api_store.dto.waste.WasteDTOResponse;
import hub.com.api_store.entity.Inventory;
import hub.com.api_store.entity.Waste;
import org.springframework.stereotype.Component;

@Component
public class WasteMapper {
    public WasteDTOResponse toWasteDTOResponse(Waste waste){
        return new WasteDTOResponse(
                waste.getId(),
                waste.getQuantity(),
                waste.getUnit(),
                waste.getReason(),
                waste.getNotes(),
                waste.getWasteDate(),
                waste.getInventory().getId(),
                waste.getInventory().getLot(),
                waste.getInventory().getWarehouse(),
                waste.getInventory().getProduct().getId(),
                waste.getInventory().getProduct().getName()
        );
    }

    public Waste toWaste(WasteDTORequest wasteDTORequest , Inventory inventory){
        Waste waste = new Waste();
        waste.setQuantity(wasteDTORequest.quantity());
        waste.setUnit(wasteDTORequest.unit());
        waste.setReason(wasteDTORequest.reason());
        waste.setNotes(wasteDTORequest.notes());
        waste.setInventory(inventory);
        return waste;
    }
}
