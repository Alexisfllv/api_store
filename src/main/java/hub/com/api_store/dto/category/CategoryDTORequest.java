package hub.com.api_store.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryDTORequest(
        @Schema(description = "Category name.", example = "Frutas", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "{field.required}")
        @Size(min = 2, max = 100, message = "{field.size.range}")
        String name,

        @Schema(description = "Category description.", example = "Frutas frescas y de temporada",  requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Size(max = 500, message = "{field.max.length}")
        String description
) {}
