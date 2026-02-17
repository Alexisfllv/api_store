package hub.com.api_store.dto.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import hub.com.api_store.nums.GlobalUnit;
import hub.com.api_store.nums.PurchaseStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PurchaseDTOResponse(
        Long id,
        BigDecimal quantity,
        GlobalUnit unit,
        BigDecimal costUnit,
        BigDecimal totalCost,
        String lot,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime expirationDate,

        String warehouseLocation,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime arrivalDate,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime purchaseDate,

        String invoiceNumber,
        String notes,
        PurchaseStatus purchaseStatus,

        Long productId,
        String productName,
        Long supplierId,
        String supplierName,

        Long inventoryId,
        String inventoryWarehouse
) {}
