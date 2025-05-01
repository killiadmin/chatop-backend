package com.openclassrooms.chatop.dto;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for representing user information.
 * Fields:
 * - id: The unique identifier of the user.
 * - name: The name of the user.
 * - email: The email address of the user.
 * - role: The role assigned to the user (admin, user).
 * - created_at: The date and time the user was created.
 * - updated_at: The date and time the user was last updated.
 */
@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
