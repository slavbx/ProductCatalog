package org.slavbx.productcatalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Data Transfer Object для отправки ответа клиенту на его запрос
 */
@Builder
@Schema(description = "DTO для отправки ответа клиенту на его запрос")
public record ResponseDto(
        String message
) {
}
