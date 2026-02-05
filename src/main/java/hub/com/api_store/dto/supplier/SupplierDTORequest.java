package hub.com.api_store.dto.supplier;

import hub.com.api_store.nums.CategoryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SupplierDTORequest(
        @Schema(description = "Supplier name.", example = "Evans")
        @NotBlank(message = "{field.required}")
        @Size(min = 2, max = 100, message = "{field.size.range}")
        String name,

        @Schema(description = "Supplier phone.", example = "987654321", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "{field.required}")
        @Size(min = 2, max = 12, message = "{field.size.range}")
        String phone,

        @Schema(description = "Supplier email.", example = "evans@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "{field.required}")
        @Size(min = 2, max = 100, message = "{field.size.range}")
        String email,

        @Schema(description = "Supplier address.", example = "Lima...",requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "{field.required}")
        @Size(min = 2, max = 200, message = "{field.size.range}")
        String address
) {}
