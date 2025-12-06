package org.slavbx.productcatalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Data Transfer Object для передачи пользователя
 */
@Builder
@Schema(description = "DTO для передачи пользователя")
public record UserDTO (
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 4, message = "Password must be at least 4 characters")
        String password,

        @Size(max = 50, message = "Name must not exceed 50 characters")
        String name
) {}
