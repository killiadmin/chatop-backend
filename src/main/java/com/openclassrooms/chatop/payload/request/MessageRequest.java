package com.openclassrooms.chatop.payload.request;

import lombok.Data;

/**
 * Represents a request to create a message in the system.
 */
@Data
public class MessageRequest {
    private Integer rental_id;
    private Integer user_id;
    private String message;
}
