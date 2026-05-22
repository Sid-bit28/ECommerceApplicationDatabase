package org.example.ecommerceprojectmysql.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCategoryRequest {
    @Size(min = 2, max = 50, message = "Category name should be 2-50 characters long")
    private String name;

    @Size(max = 500, message = "Category description should not exceed 500 characters")
    private String description;

    private Boolean active;
}
