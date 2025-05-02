package com.openclassrooms.chatop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class RegisterDTO {

    @NotBlank
    @Email
    @Schema(description = "User email", example = "newuser@example.com")
    private String email;

    @NotBlank
    @Schema(description = "User password", example = "SecurePass2024")
    private String password;

    @NotBlank
    @Schema(description = "Full name of the user", example = "Alice Johnson")
    private String name;
}
