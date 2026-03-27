package hub.com.api_store.dto.purchase;

import hub.com.api_store.nums.GlobalUnit;
import hub.com.api_store.nums.PurchaseStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PurchaseDTORequest(


        @NotNull(message = "{field.required}")
        @DecimalMin(value = "0.001", message = "{field.must.be.positive}")
        @Digits(integer = 10, fraction = 3, message = "{field.amount.format}")
        BigDecimal quantity,

        @NotNull(message = "{field.required}")
        GlobalUnit unit,

        @NotNull(message = "{field.required}")
        @DecimalMin(value = "0.0001", message = "{field.price.min}")
        @Digits(integer = 15, fraction = 4, message = "{field.price.format}")
        BigDecimal costUnit,

        @Size(max = 50, message = "{field.max.length}")
        @NotBlank(message = "{field.required}")
        String lot,

        @Future(message = "{field.date.future}")
        LocalDateTime expirationDate,

        @Size(max = 100, message = "{field.max.length}")
        @NotBlank(message = "{field.required}")
        String warehouseLocation,

        @PastOrPresent(message = "{field.date.past.or.present}")
        LocalDateTime arrivalDate,

        @Size(max = 100, message = "{field.max.length}")
        @NotBlank(message = "{field.required}")
        String invoiceNumber,

        @Size(max = 500, message = "{field.max.length}")
        String notes,

        @NotNull(message = "{field.required}")
        @Positive(message = "{field.must.be.positive}")
        Long productId,

        @NotNull(message = "{field.required}")
        @Positive(message = "{field.must.be.positive}")
        Long supplierId
) {}