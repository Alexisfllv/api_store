package hub.com.api_store.service.impl;

import hub.com.api_store.dto.category.CategoryDTOResponse;
import hub.com.api_store.entity.Category;
import hub.com.api_store.mapper.CategoryMapper;
import hub.com.api_store.service.CategoryService;
import hub.com.api_store.service.domain.CategoryServiceDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {


    private final CategoryServiceDomain categoryServiceDomain;
    private final CategoryMapper categoryMapper;

    // GET
    @Override
    public CategoryDTOResponse getCategoryId(Long id) {
        Category categoryExist = categoryServiceDomain.findByIdCategory(id);
        CategoryDTOResponse response = categoryMapper.toCategoryDTOResponse(categoryExist);
        return response;
    }

}
