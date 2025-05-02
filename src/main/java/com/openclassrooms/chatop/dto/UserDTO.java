package com.openclassrooms.chatop.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserDTO {

    @Schema(description = "Unique identifier of the user", example = "7")
    private Long id;

    @Schema(description = "Full name of the user", example = "Alice Dupont")
    private String name;

    @Schema(description = "User email address", example = "alice.dupont@example.com")
    private String email;

    @Schema(description = "Role of the user", example = "USER")
    private String role;

    @Schema(description = "User creation timestamp", example = "2024-11-30T08:22:00")
    private LocalDateTime created_at;

    @Schema(description = "Last update timestamp", example = "2025-02-10T18:05:00")
    private LocalDateTime updated_at;
}
