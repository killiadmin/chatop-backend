package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.MessageDTO;

import com.openclassrooms.chatop.model.Message;
import com.openclassrooms.chatop.model.Rental;
import com.openclassrooms.chatop.model.User;

import com.openclassrooms.chatop.service.CustomMessageDetailsService;
import com.openclassrooms.chatop.service.CustomRentalDetailsService;
import com.openclassrooms.chatop.service.CustomUserDetailsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Messages", description = "Endpoints for sending messages related to rentals")
public class MessageController {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomRentalDetailsService customRentalDetailsService;
    private final CustomMessageDetailsService customMessageDetailsService;

    @Operation(
            summary = "Send a message",
            description = "Creates and sends a message from a user regarding a rental.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Message sent successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request (missing fields or invalid data)", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
            }
    )
    @PostMapping("")
    public ResponseEntity<Map<String, String>> createMessage(
            @RequestBody MessageDTO messageDTO
    ) {
        try {
            if (messageDTO.getRental_id() == null || messageDTO.getUser_id() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.emptyMap());
            }

            Optional<User> userOptional = customUserDetailsService.getUser(Long.valueOf(messageDTO.getUser_id()));
            Optional<Rental> rentalOptional = customRentalDetailsService.getRental(Long.valueOf(messageDTO.getRental_id()));

            if (userOptional.isEmpty() || rentalOptional.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Collections.emptyMap());
            }

            if (messageDTO.getMessage().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Collections.emptyMap());
            }

            User user = userOptional.get();
            Rental rental = rentalOptional.get();

            Message message = new Message();
            message.setUser(user);
            message.setRental(rental);
            message.setMessage(messageDTO.getMessage());

            customMessageDetailsService.saveMessage(message);

            return ResponseEntity.ok(Map.of("message", "Message send with success"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Collections.emptyMap());
        }
    }
}
