package org.example.ecommerceprojectmysql.repository;

import org.example.ecommerceprojectmysql.model.Category;
import org.example.ecommerceprojectmysql.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategory(Category category, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findByActiveTrue(Pageable pageable);

    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    Page<Product> findByCategoryAndPriceBetween(Category category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    Page<Product> findByCategoryAndNameContainingIgnoreCaseAndPriceBetween(Category category, String name, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    @Query("SELECT DISTINCT p from Product p WHERE p.quantity <= :threshold ORDER BY p.quantity ASC")
    Page<Product> findLowStockProducts(@Param("threshold") Integer threshold, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.category")
    Page<Product> findAllWithCategories(Pageable pageable);

    Page<Product> findByCategoryAndActiveTrue(Category category, Pageable pageable);
}
