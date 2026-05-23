package org.example.ecommerceprojectmysql.repository;

import org.example.ecommerceprojectmysql.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    Page<Category> findByActiveTrue(Pageable pageable);

    Page<Category> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);
}
