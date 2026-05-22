package org.example.ecommerceprojectmysql.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductRequest {
    @NotBlank
    @Size(min = 2, max = 20, message = "Product name should be 2-50 characters")
    private String name;

    @Size(max = 500, message = "Product description should not exceed 500 characters")
    private String description;

    @NotNull(message = "Product price is required")
    @Positive(message = "Product price should be positive")
    @DecimalMin(value = "0.01", message = "Product price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Product quantity is required")
    @Min(value = 0, message = "Product quantity should be positive")
    private Integer quantity;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private Boolean active;
}
