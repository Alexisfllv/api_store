package hub.com.api_store.dto.product;

import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.nums.GlobalUnit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductDTORequest(
        @NotBlank(message = "{field.required}")
        @Size(min = 3, max = 200, message = "{field.size.range}")
        String name,

        @NotNull(message = "{field.required}")
        GlobalUnit unit,

        @NotNull(message = "{field.required}")
        @Positive(message = "{field.must.be.positive}")
        Long categoryId
) {}
