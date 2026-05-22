package org.example.ecommerceprojectmysql.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductRequest {
    @Size(min = 2, max = 50, message = "Product name should be between 2-50 characters")
    private String name;

    @Size(max = 500, message = "Product description should not exceed 500 characters")
    private String description;

    @DecimalMin(value = "0.01", message = "Product price should be more than 0")
    private BigDecimal price;

    @Min(value = 0, message = "Product quantity should be more than 0")
    private Integer quantity;

    private Boolean active;

    private Long categoryId;
}
