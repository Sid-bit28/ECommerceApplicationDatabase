package org.example.ecommerceprojectmysql.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCategoryRequest {
    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100, message = "Category name should be 2-100 characters")
    private String name;

    @Size(max = 500, message = "Description should not exceed 500 characters")
    private String description;


}
