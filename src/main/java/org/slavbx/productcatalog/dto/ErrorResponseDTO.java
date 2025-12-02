package org.slavbx.productcatalog.dto;

import lombok.Builder;

/**
 * Data Transfer Object для ответа при ошибке
 */
@Builder
public record ErrorResponseDTO(
    String message
) {
}
