package org.example.ecommerceprojectmysql.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerceprojectmysql.dto.CreateProductRequest;
import org.example.ecommerceprojectmysql.dto.ProductResponse;
import org.example.ecommerceprojectmysql.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
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
}
