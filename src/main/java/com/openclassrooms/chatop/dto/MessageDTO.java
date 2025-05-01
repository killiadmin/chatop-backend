package com.openclassrooms.chatop.dto;

import lombok.Data;

@Data
public class MessageDTO {
    private Integer rental_id;
    private Integer user_id;
    private String message;
}
