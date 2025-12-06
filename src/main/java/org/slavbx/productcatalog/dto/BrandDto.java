package org.slavbx.productcatalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Data Transfer Object для передачи бренда
 */
@Builder
@Schema(description = "DTO для передачи бренда")
public record BrandDto(
        @Size(min = 2, max = 50, message = "Brand name must be between 2 and 50 characters")
        String name,

        @NotBlank(message = "Brand description is required")
        @Size(min = 10, max = 500, message = "Brand description must be between 10 and 500 characters")
        String desc
) {}
