package org.slavbx.productcatalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Data Transfer Object для передачи товара
 */
@Builder
@Schema(description = "DTO для передачи товара")
public record ProductDto(
        @NotBlank(message = "Product name is required")
        @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
        String name,

        @NotBlank(message = "Product description is required")
        @Size(min = 10, max = 1000, message = "Product description must be between 10 and 1000 characters")
        String desc,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
        BigDecimal price,

        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Quantity must be at least 0")
        Integer quantity,

        @NotBlank(message = "Seller email is required")
        @Email(message = "Seller email should be valid")
        String sellerEmail,

        @NotBlank(message = "Category name is required")
        String categoryName,

        @NotBlank(message = "Brand name is required")
        String brandName
) {}
