package hub.com.api_store.service;

import hub.com.api_store.dto.category.CategoryDTORequest;
import hub.com.api_store.dto.category.CategoryDTOResponse;
import hub.com.api_store.dto.category.CategoryDTOUpdate;
import hub.com.api_store.nums.CategoryStatus;
import hub.com.api_store.util.page.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface CategoryService {
    // GET
    CategoryDTOResponse getCategoryId (Long id);
    PageResponse<CategoryDTOResponse> getPageListCategory (int page, int size);
    PageResponse<CategoryDTOResponse> getPageListCategoryByStatus (CategoryStatus status, int page, int size);

    // POST
    CategoryDTOResponse addCategory(CategoryDTORequest categoryDTORequest);

    // PUT
    CategoryDTOResponse updateCategory(Long id, CategoryDTOUpdate categoryDTOUpdate);

    // DELETE
    void deleteSofCategory(Long id);

    // PATCH
    CategoryDTOResponse updateCategoryStatus(Long id,CategoryStatus newStatus);

}
