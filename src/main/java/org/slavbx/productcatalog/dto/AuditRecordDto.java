package org.slavbx.productcatalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;


import java.time.LocalDateTime;

/**
 * Data Transfer Object для передачи записи аудита
 */
@Builder
@Schema(description = "DTO для передачи записи аудита")
public record AuditRecordDto(
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Action is required")
        @Size(min = 2, max = 500, message = "Action must be between 2 and 500 characters")
        String action,

        @NotNull(message = "Date and time is required")
        LocalDateTime dateTime
) {}
