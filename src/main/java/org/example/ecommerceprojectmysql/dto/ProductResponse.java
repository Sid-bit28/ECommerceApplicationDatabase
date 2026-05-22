package org.example.ecommerceprojectmysql.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommerceprojectmysql.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private Boolean active;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategorySummary {
        private Long id;
        private String name;
    }
    private CategorySummary category;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static ProductResponse fromEntity(Product product) {
        CategorySummary categorySummary = null;

        if (product.getCategory() != null) {
            categorySummary = CategorySummary.builder()
                    .id(product.getCategory().getId())
                    .name(product.getCategory().getName())
                    .build();
        }

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .active(product.getActive())
                .category(categorySummary)
                .createdDate(product.getCreatedAt())
                .updatedDate(product.getUpdatedAt())
                .build();
    }
}
