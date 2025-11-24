package org.slavbx.productcatalog.dto;

import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Data Transfer Object для передачи категории
 */
@Builder
public record CategoryDTO (
        @NotBlank(message = "Category name is required")
        @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
        String name,

        @NotBlank(message = "Category description is required")
        @Size(min = 10, max = 500, message = "Category desc must be between 10 and 500 characters")
        String desc
) {}
