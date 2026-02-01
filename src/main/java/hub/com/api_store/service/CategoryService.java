package hub.com.api_store.service;

import hub.com.api_store.dto.category.CategoryDTOResponse;

public interface CategoryService {
    // GET
    CategoryDTOResponse getCategoryId (Long id);
}
