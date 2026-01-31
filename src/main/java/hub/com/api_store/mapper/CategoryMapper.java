package hub.com.api_store.mapper;

import hub.com.api_store.dto.category.CategoryDTOResponse;
import hub.com.api_store.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    // toResponse
    CategoryDTOResponse toCategoryDTOResponse(Category category);
}
