package org.slavbx.productcatalog.dto;

import lombok.Builder;

/**
 * Data Transfer Object для отправки ответа клиенту на его запрос
 */
@Builder
public record ResponseDTO(
        String message
) {
}
