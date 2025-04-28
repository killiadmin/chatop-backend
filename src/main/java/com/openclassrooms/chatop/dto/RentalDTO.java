package com.openclassrooms.chatop.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RentalDTO {
    private Long id;
    private String name;
    private Integer surface;
    private BigDecimal price;
    private String description;
    private String picture;
    private Long ownerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
