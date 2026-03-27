package hub.com.api_store.mapper;

import hub.com.api_store.dto.purchase.PurchaseDTORequest;
import hub.com.api_store.dto.purchase.PurchaseDTOResponse;
import hub.com.api_store.entity.Product;
import hub.com.api_store.entity.Purchase;
import hub.com.api_store.entity.Supplier;
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
                purchase.getSupplier().getName(),
                purchase.getInventory().getId(),
                purchase.getInventory().getWarehouse()
        );
    }

    public Purchase toPurchase(PurchaseDTORequest purchaseDTORequest, Product product, Supplier supplier){
        return new Purchase(
                null,
                purchaseDTORequest.quantity(),
                purchaseDTORequest.unit(),
                purchaseDTORequest.costUnit(),
                null, // totalCost will be calculated in the service layer
                null,
                purchaseDTORequest.lot(),
                purchaseDTORequest.expirationDate(),
                purchaseDTORequest.warehouseLocation(),
                purchaseDTORequest.arrivalDate(),
                null,
                purchaseDTORequest.invoiceNumber(),
                purchaseDTORequest.notes(),
                product,
                supplier,
                null
        );
    }
}
