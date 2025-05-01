package com.openclassrooms.chatop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

/**
 * Data Transfer Object (DTO) representing login credentials for user authentication.
 * This class encapsulates the user's email and password for authentication purposes.
 * The email field must be a non-blank and valid email address.
 * The password field must be non-blank.
 */
@Data
public class LoginDTO {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}