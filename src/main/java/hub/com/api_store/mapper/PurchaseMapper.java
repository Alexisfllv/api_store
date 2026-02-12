package hub.com.api_store.mapper;

import hub.com.api_store.dto.purchase.PurchaseDTOResponse;
import hub.com.api_store.entity.Purchase;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper {

    public PurchaseDTOResponse toPurchaseDTOResponse(Purchase purchase){
        return new PurchaseDTOResponse(
                purchase.getId(),
                purchase.getQuantity(),
                purchase.getUnit(),
                purchase.getCostUnit(),
                purchase.getTotalCost(),
                purchase.getLot(),
                purchase.getExpirationDate(),
                purchase.getWarehouseLocation(),
                purchase.getArrivalDate(),
                purchase.getPurchaseDate(),
                purchase.getInvoiceNumber(),
                purchase.getNotes(),
                purchase.getStatus(),
                purchase.getProduct().getId(),
                purchase.getProduct().getName(),
                purchase.getSupplier().getId(),
                purchase.getSupplier().getName()
        );
    }
}
