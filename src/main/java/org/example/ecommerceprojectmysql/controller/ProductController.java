package org.example.ecommerceprojectmysql.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerceprojectmysql.dto.CreateProductRequest;
import org.example.ecommerceprojectmysql.dto.ProductResponse;
import org.example.ecommerceprojectmysql.dto.UpdateProductRequest;
import org.example.ecommerceprojectmysql.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
@Validated
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        log.info("Create product with name: {}", request.getName());
        ProductResponse response = productService.createProduct(request);
        log.info("Created product with ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        log.info("Get product with ID: {}", id);
        ProductResponse response = productService.getProduct(id);
        log.info("Get product with ID: {}", response.getId());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchProducts(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(required = false) String sort
    ) {
        log.info("Searching products with name: {}", name);
        Sort sortOrder = Sort.unsorted();
        if(sort != null && !sort.isEmpty()) {
            String[] sortParts = sort.split(",");
            Sort.Direction direction = sortParts.length > 1 && sortParts[1].
                    equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            sortOrder = Sort.by(direction, sortParts[0]);
        }
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<ProductResponse> products = productService.searchProducts(name, pageable);
        log.info("Search products with name: {}", name);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<Page<ProductResponse>> findProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        log.info("Find products by category with ID: {}", categoryId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ProductResponse> response = productService.findProductsByCategory(categoryId, pageable);
        log.info("Found {} products in category {}", response.getTotalElements(), categoryId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/price-range")
    public ResponseEntity<Page<ProductResponse>> findProductsByPriceRange(
            @RequestParam(defaultValue = "0.01")BigDecimal min,
            @RequestParam(defaultValue = "0.01")BigDecimal max,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(required = false) String sort
            ) {
        log.info("Find products by price range with min: {}, max: {}", min, max);
        Sort sortOrder =  Sort.unsorted();
        if(sort != null && !sort.isEmpty()) {
            String[] sortParts = sort.split(",");
            Sort.Direction direction = sortParts.length > 1 && sortParts[1].
                    equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            sortOrder = Sort.by(direction, sortParts[0]);
        }
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<ProductResponse> response = productService.findByPriceRange(min, max, pageable);
        log.info("Found {} products in page {}", response.getTotalElements(), pageable);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        log.info("Update product with ID: {}", id);
        ProductResponse response = productService.updateProduct(id, request);
        log.info("Product updated successfully");
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> deleteProduct(
            @PathVariable Long id
    ) {
        log.info("Delete product with ID: {}", id);
        productService.deleteProduct(id);
        log.info("Delete deleted successfully");
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> listProducts(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(required = false) String sort
    ) {
        log.info("Listing all products");
        Sort sortOrder = Sort.unsorted();
        if(sort != null && !sort.isEmpty()) {
            String[] sortParts = sort.split(",");
            Sort.Direction direction = sortParts.length > 1 && sortParts[1]
                    .equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            sortOrder = Sort.by(direction, sortParts[0]);
        }
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<ProductResponse> response = productService.listProducts(pageable);
        log.info("Products retrieved - total: {}, totalPages: {}", response.getTotalElements(), response.getTotalPages());
        return ResponseEntity.ok().body(response);
    }
}
