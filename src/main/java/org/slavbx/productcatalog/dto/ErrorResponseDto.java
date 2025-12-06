package org.slavbx.productcatalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Data Transfer Object для ответа при ошибке
 */
@Builder
@Schema(description = "DTO для передачи ошибки")
public record ErrorResponseDto(
    String message
) {
}
