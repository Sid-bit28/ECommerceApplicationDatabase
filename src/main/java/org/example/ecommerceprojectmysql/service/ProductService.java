package org.example.ecommerceprojectmysql.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerceprojectmysql.dto.CreateProductRequest;
import org.example.ecommerceprojectmysql.dto.ProductResponse;
import org.example.ecommerceprojectmysql.dto.UpdateProductRequest;
import org.example.ecommerceprojectmysql.exception.ResourceNotFoundException;
import org.example.ecommerceprojectmysql.model.Category;
import org.example.ecommerceprojectmysql.model.Product;
import org.example.ecommerceprojectmysql.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        log.info("Create product: {} in category: {}", request.getName(), request.getCategoryId());

        Category category = categoryService.getCategoryEntity(request.getCategoryId());

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity() != null ? request.getQuantity() : 0);
        product.setActive(request.getActive() != null ? request.getActive() : true);
        product.setCategory(category);
        Product saved = productRepository.save(product);
        log.info("Product created with id: {}", saved.getId());
        return ProductResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        log.info("Get product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product not found with id: " + id));
        return ProductResponse.fromEntity(product);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        log.info("Update product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product not found with id: " + id));
        if(request.getName() != null &&  !request.getName().isEmpty()) {
            product.setName(request.getName());
        }

        if(request.getDescription() != null && !request.getDescription().isEmpty()) {
            product.setDescription(request.getDescription());
        }

        if(request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }

        if(request.getQuantity() != null) {
            product.setQuantity(request.getQuantity());
        }

        if(request.getActive() != null) {
            product.setActive(request.getActive());
        }

        if(request.getCategoryId() != null) {
            Category category = categoryService.getCategoryEntity(request.getCategoryId());
            product.setCategory(category);
        }

        Product updated = productRepository.save(product);
        log.info("Product updated with id: {}", updated.getId());
        return ProductResponse.fromEntity(updated);
    }

    @Transactional
    public void deleteProduct(Long id) {
        log.info("Delete product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
        log.info("Product deleted with id: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> listProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAllWithCategories(pageable);
        return  products.map(ProductResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findProductsByCategory(Long categoryId, Pageable pageable) {
        log.info("Fetching products category: {}", categoryId);
        Category category = categoryService.getCategoryEntity(categoryId);
        Page<Product> products = productRepository.findByCategoryAndActiveTrue(category, pageable);
        return products.map(ProductResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(String name, Pageable pageable) {
        log.info("Searching products with name: {}", name);
        Page<Product> products = productRepository.findByNameContainingIgnoreCase(name, pageable);
        return products.map(ProductResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        log.info("Searching products with min-max range: {}, {}.", minPrice, maxPrice);
        Page<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        return products.map(ProductResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findLowStockProducts(Integer threshold, int page, int size) {
        log.info("Searching products with low stock than threshold: {}, page: {}, size: {}",  threshold, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Product> products = productRepository.findLowStockProducts(threshold, pageable);
        return products.map(ProductResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findByCategoryAndPriceRange(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {
        log.info("Searching products with category: {}, minPrice: {}, maxPrice: {}, page: {}, size: {}", categoryId, minPrice, maxPrice, page, size);
        Category category = categoryService.getCategoryEntity(categoryId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Product> products = productRepository.findByCategoryAndPriceBetween(category, minPrice, maxPrice, pageable);
        return products.map(ProductResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findActiveProducts(int page, int size) {
        log.info("Searching products with active status");
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Product> products = productRepository.findByActiveTrue(pageable);
        return products.map(ProductResponse::fromEntity);
    }
}
