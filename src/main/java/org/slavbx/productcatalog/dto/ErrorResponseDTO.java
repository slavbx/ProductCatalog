package org.slavbx.productcatalog.dto;

import lombok.Builder;

@Builder
public record ErrorResponseDTO(
    String message
) {
}
