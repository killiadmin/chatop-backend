package com.openclassrooms.chatop.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for handling message-related data.
 * It encapsulates the details of a message, including the rental ID, user ID, and message content.
 */
@Data
public class MessageDTO {
    private Integer rental_id;
    private Integer user_id;
    private String message;
}
