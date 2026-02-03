package hub.com.api_store.service;

import hub.com.api_store.dto.category.CategoryDTORequest;
import hub.com.api_store.dto.category.CategoryDTOResponse;
import hub.com.api_store.dto.category.CategoryDTOUpdate;
import hub.com.api_store.util.page.PageResponse;
import org.springframework.data.domain.PageRequest;

public interface CategoryService {
    // GET
    CategoryDTOResponse getCategoryId (Long id);
    PageResponse<CategoryDTOResponse> getPageListCategory (int page, int size);

    // POST
    CategoryDTOResponse addCategory(CategoryDTORequest categoryDTORequest);

    // PUT
    CategoryDTOResponse updateCategory(Long id, CategoryDTOUpdate categoryDTOUpdate);
}
