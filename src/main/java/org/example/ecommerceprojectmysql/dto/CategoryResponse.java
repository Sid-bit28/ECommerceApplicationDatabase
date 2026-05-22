package org.example.ecommerceprojectmysql.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommerceprojectmysql.model.Category;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean active;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Integer productCount;

    public static CategoryResponse fromEntity(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.getActive())
                .createdDate(category.getCreatedAt())
                .updatedDate(category.getUpdatedAt())
                .productCount(category.getProducts().size())
                .build();
    }
}
