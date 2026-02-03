package hub.com.api_store.service.impl;

import hub.com.api_store.dto.category.CategoryDTORequest;
import hub.com.api_store.dto.category.CategoryDTOResponse;
import hub.com.api_store.dto.category.CategoryDTOUpdate;
import hub.com.api_store.entity.Category;
import hub.com.api_store.mapper.CategoryMapper;
import hub.com.api_store.nums.CategoryStatus;
import hub.com.api_store.service.CategoryService;
import hub.com.api_store.service.domain.CategoryServiceDomain;
import hub.com.api_store.util.page.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
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

    @Override
    public PageResponse<CategoryDTOResponse> getPageListCategory(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Category> categoryPage = categoryServiceDomain.findAllPage(pageRequest);

        return new PageResponse<>(
                categoryPage.getContent()
                        .stream()
                        .map(categoryMapper::toCategoryDTOResponse)
                        .toList(),
                categoryPage.getNumber(),
                categoryPage.getSize(),
                categoryPage.getTotalElements(),
                categoryPage.getTotalPages()
        );
    }

    // POST
    @Override
    public CategoryDTOResponse addCategory(CategoryDTORequest categoryDTORequest) {
        Category category = categoryMapper.toCategory(categoryDTORequest);
        categoryServiceDomain.validateUniqueName(category.getName());
        category.setStatus(CategoryStatus.ACTIVE);
        Category categorySaved =  categoryServiceDomain.saveCategory(category);
        CategoryDTOResponse response = categoryMapper.toCategoryDTOResponse(categorySaved);
        return response;
    }

    @Override
    public CategoryDTOResponse updateCategory(Long id, CategoryDTOUpdate categoryDTOUpdate) {
        Category categoryExist = categoryServiceDomain.findByIdCategory(id);
        // set
        categoryExist.setName(categoryDTOUpdate.name());
        categoryExist.setDescription(categoryDTOUpdate.description());
        categoryExist.setStatus(categoryDTOUpdate.status());

        categoryServiceDomain.validateUniqueName(categoryExist.getName());

        Category categorySaved =  categoryServiceDomain.saveCategory(categoryExist);
        CategoryDTOResponse response = categoryMapper.toCategoryDTOResponse(categorySaved);
        return response;
    }


}
