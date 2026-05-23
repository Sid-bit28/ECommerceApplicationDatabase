package org.example.ecommerceprojectmysql.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerceprojectmysql.dto.CategoryResponse;
import org.example.ecommerceprojectmysql.dto.CreateCategoryRequest;
import org.example.ecommerceprojectmysql.dto.UpdateCategoryRequest;
import org.example.ecommerceprojectmysql.exception.DuplicateResourceException;
import org.example.ecommerceprojectmysql.exception.InvalidOperationException;
import org.example.ecommerceprojectmysql.exception.ResourceNotFoundException;
import org.example.ecommerceprojectmysql.model.Category;
import org.example.ecommerceprojectmysql.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.dnd.InvalidDnDOperationException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        log.info("Creating category with name: {}", request.getName());

        if(categoryRepository.findByName(request.getName()).isPresent()) {
            log.warn("Category with name {} already exists", request.getName());

            throw new DuplicateResourceException("Category with name '" + request.getName() + "' already exists");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setActive(true);

        Category saved = categoryRepository.save(category);
        log.info("Category created with id: {}", saved.getId());

        return CategoryResponse.fromEntity(saved);
    }

    @Transactional
    public CategoryResponse getCategory(Long id) {
        log.info("Getting category with id: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Category with id" + id + "not found"));

        return CategoryResponse.fromEntity(category);
    }

    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        log.info("Updating category with id: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id " + id + " not found"
                ));

        if(request.getName() != null && !request.getName().isEmpty()) {
            if(!category.getName().equals(request.getName()) && categoryRepository.findByName(request.getName()).isPresent()) {
                throw new DuplicateResourceException("Category with name '" + request.getName() + "' already exists");
            }
        }
        category.setName(request.getName());

        if(request.getDescription() != null && !request.getDescription().isEmpty()) {
            category.setDescription(request.getDescription());
        }

        if(request.getActive() != null) {
            category.setActive(request.getActive());
        }

        Category saved = categoryRepository.save(category);
        log.info("Category updated with id: {}", saved.getId());
        return CategoryResponse.fromEntity(saved);
    }

    @Transactional
    public void deleteCategory(Long id) {
        log.info("Deleting category with id: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id" + id + "not found"));
        if(!category.getProducts().isEmpty()) {
            log.warn("Delete attempt on category with {} products",  category.getProducts().size());
            throw new InvalidOperationException("Cannot delete category with " + category.getProducts().size() + " products. Delete and reassign products first");
        }

        categoryRepository.delete(category);
        log.info("Category deleted with id: {}", id);
    }

    @Transactional
    public Page<CategoryResponse> listCategories(int page, int size) {
        log.info("Listing categories with page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(CategoryResponse::fromEntity);
    }

    @Transactional
    public Page<CategoryResponse> listActiveCategories(int page, int size) {
        log.info("Listing active categories with page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Category> categories = categoryRepository.findByActiveTrue(pageable);
        return categories.map(CategoryResponse::fromEntity);
    }

    @Transactional
    public Page<CategoryResponse> searchCategories(String query, int page, int size) {
        log.info("Searching categories with page {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Category> categories = categoryRepository.findByDescriptionContainingIgnoreCase(query, pageable);
        return categories.map(CategoryResponse::fromEntity);
    }

    @Transactional
    public Boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }


}
