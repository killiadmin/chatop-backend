package com.openclassrooms.chatop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MessageDTO {

    @Schema(description = "ID of the rental", example = "1")
    private Integer rental_id;

    @Schema(description = "ID of the user sending the message", example = "42")
    private Integer user_id;

    @Schema(description = "The content of the message", example = "Hello, I’m interested in this rental.")
    private String message;
}
