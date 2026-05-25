package org.example.ecommerceprojectmysql.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerceprojectmysql.dto.CategoryResponse;
import org.example.ecommerceprojectmysql.dto.CreateCategoryRequest;
import org.example.ecommerceprojectmysql.dto.UpdateCategoryRequest;
import org.example.ecommerceprojectmysql.model.Category;
import org.example.ecommerceprojectmysql.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Validated
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        log.info("Creating category with name: {}", request.getName());
        CategoryResponse response = categoryService.createCategory(request);
        log.info("Category created successfully with id: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long id) {
        log.info("Getting category with id: {}", id);
        CategoryResponse response = categoryService.getCategory(id);
        log.info("Category received successfully with id: {}", response.getId());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> listCategories(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                                 @RequestParam(defaultValue = "10") @Min(1) int size,
                                                                 @RequestParam(required = false) String sort) {
        log.info("Fetching categories with pagination - page: {}, size: {}, sort:{}", page, size, sort);
        // Creating Sort Order Object
        Sort sortOrder = Sort.unsorted();
        if(sort != null && !sort.isEmpty()) {
            String[] sortParts = sort.split(",");
            String field = sortParts[0];
            Sort.Direction direction = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            sortOrder = Sort.by(direction, field);
        }
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<CategoryResponse> responses = categoryService.listCategories(pageable);
        log.info("Fetching categories with sort order: {}", sortOrder);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id, @Valid @RequestBody UpdateCategoryRequest request) {
        log.info("Updating category with id: {}", id);
        CategoryResponse response = categoryService.updateCategory(id, request);
        log.info("Category updated successfully with id: {}", response.getId());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<void> deleteCategory(@PathVariable Long id) {
        log.info("Deleting category with id: {}", id);
        categoryService.deleteCategory(id);
        log.info("Category deleted successfully with id: {}", id);
        return ResponseEntity.noContent().build();
    }


}
