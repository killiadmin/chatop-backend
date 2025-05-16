package com.openclassrooms.chatop.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentalDTO {

    @Schema(description = "Unique identifier of the rental", example = "5")
    private Long id;

    @Schema(description = "Name of the rental", example = "Cozy Studio in Paris")
    private String name;

    @Schema(description = "Surface area in square meters", example = "30")
    private Integer surface;

    @Schema(description = "Monthly price in euros", example = "750.00")
    private BigDecimal price;

    @Schema(description = "Description of the rental", example = "Nice apartment close to the Eiffel Tower.")
    private String description;

    @Schema(description = "URL or base64 representation of the rental picture")
    private String picture;

    @Schema(description = "ID of the owner of the rental", example = "3")
    private Long owner_id;

    @Schema(description = "Date and time of creation", example = "2024-12-01T14:30:00")
    private LocalDateTime created_at;

    @Schema(description = "Date and time of last update", example = "2025-01-15T09:45:00")
    private LocalDateTime updated_at;
}
