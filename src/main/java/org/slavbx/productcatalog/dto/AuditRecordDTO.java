package org.slavbx.productcatalog.dto;

import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Data Transfer Object для передачи записи аудита
 */
@Builder
public record AuditRecordDTO (
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Action is required")
        @Size(min = 2, max = 500, message = "Action must be between 2 and 500 characters")
        String action,

        @NotNull(message = "Date and time is required")
        LocalDateTime dateTime
) {}
