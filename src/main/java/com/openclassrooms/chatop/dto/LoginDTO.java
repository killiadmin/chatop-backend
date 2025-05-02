package com.openclassrooms.chatop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank
    @Email
    @Schema(description = "User email", example = "john.doe@example.com")
    private String email;

    @NotBlank
    @Schema(description = "User password", example = "P@ssw0rd123")
    private String password;
}