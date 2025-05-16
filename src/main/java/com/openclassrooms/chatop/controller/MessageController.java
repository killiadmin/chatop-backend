package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.MessageDTO;
import com.openclassrooms.chatop.service.CustomMessageDetailsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Messages", description = "Endpoints for sending messages related to rentals")
public class MessageController {

    private final CustomMessageDetailsService customMessageDetailsService;

    @Operation(
            summary = "Send a message",
            description = "Creates and sends a message from a user regarding a rental.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Message sent successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request (missing fields or invalid data)", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @PostMapping("")
    public ResponseEntity<Map<String, String>> createMessage(@RequestBody MessageDTO messageDTO) throws Exception {
        customMessageDetailsService.createAndSaveMessage(messageDTO);
        return ResponseEntity.ok(Map.of("message", "Message send with success"));
    }
}
