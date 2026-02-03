package hub.com.api_store.controller;


import hub.com.api_store.dto.category.CategoryDTORequest;
import hub.com.api_store.dto.category.CategoryDTOResponse;
import hub.com.api_store.dto.category.CategoryDTOUpdate;
import hub.com.api_store.service.CategoryService;
import hub.com.api_store.util.page.PageResponse;
import hub.com.api_store.util.response.GenericResponse;
import hub.com.api_store.util.response.StatusApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    // GET
    @GetMapping("/{id}")
    ResponseEntity<GenericResponse<CategoryDTOResponse>> getCategoryIdGet(@PathVariable Long id ){
        CategoryDTOResponse response = categoryService.getCategoryId(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, response)
        );
    }

    @GetMapping("/page")
    ResponseEntity<GenericResponse<PageResponse<CategoryDTOResponse>>> getCategoryPageGet(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "3") int size){
        PageResponse<CategoryDTOResponse> paged = categoryService.getPageListCategory(page, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponse<>(StatusApi.SUCCESS, paged));
    }

    // POST
    @PostMapping
    ResponseEntity<GenericResponse<CategoryDTOResponse>> addCategoryPost(@Valid @RequestBody CategoryDTORequest categoryDTORequest){
        CategoryDTOResponse response = categoryService.addCategory(categoryDTORequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new GenericResponse<>(StatusApi.CREATED, response)
        );
    }

    // PUT
    @PutMapping("/{id}")
    ResponseEntity<GenericResponse<CategoryDTOResponse>> updateCategoryPut(@PathVariable Long id, @Valid @RequestBody CategoryDTOUpdate categoryDTOUpdate){
        CategoryDTOResponse response = categoryService.updateCategory(id, categoryDTOUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, response)
        );
    }

    // Delete
    @DeleteMapping("/soft-delete/{id}")
    ResponseEntity<Void> deleteCategoryDelete(@PathVariable Long id){
        categoryService.deleteSofCategory(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
