package com.openclassrooms.chatop.mapper;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class RentalMapper {

    private Long id;
    private String name;
    private Integer surface;
    private BigDecimal price;
    private String description;
    private String picture;
    private Long owner_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public void setCreatedAt(Object createdAt) {
        this.created_at = (LocalDateTime) createdAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updated_at = (LocalDateTime) updatedAt;
    }

    public void setOwnerId(Long id) {
        this.owner_id = id;
    }
}
