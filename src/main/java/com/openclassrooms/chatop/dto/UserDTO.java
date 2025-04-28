package com.openclassrooms.chatop.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
