package com.openclassrooms.chatop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for user registration.
 * Field Constraints:
 * - email: Must be non-blank and a valid email format.
 * - password: Must be non-blank.
 * - name: Must be non-blank.
 */
@Data
public class RegisterDTO {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;
}
