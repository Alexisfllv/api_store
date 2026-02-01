package hub.com.api_store.service.domain;

import hub.com.api_store.entity.Category;
import hub.com.api_store.exception.ResourceNotFoundException;
import hub.com.api_store.nums.ExceptionMessages;
import hub.com.api_store.repo.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryServiceDomain {
    private final CategoryRepo categoryRepo;

    // findById
    public Category findByIdCategory(Long id){
        return categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ExceptionMessages.RESOURCE_NOT_FOUND_ERROR.message()+id));
    }

    // findAllPage
    public Page<Category> findAllPage(Pageable pageable){
        return categoryRepo.findAll(pageable);
    }
}
