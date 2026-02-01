package hub.com.api_store.controller;


import hub.com.api_store.dto.category.CategoryDTOResponse;
import hub.com.api_store.service.CategoryService;
import hub.com.api_store.util.response.GenericResponse;
import hub.com.api_store.util.response.StatusApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{id}")
    ResponseEntity<GenericResponse<CategoryDTOResponse>> getCategoryIdGet(@PathVariable Long id ){
        CategoryDTOResponse response = categoryService.getCategoryId(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, response)
        );
    }
}
