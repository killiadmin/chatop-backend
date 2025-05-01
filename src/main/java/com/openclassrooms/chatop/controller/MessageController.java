package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.MessageDTO;

import com.openclassrooms.chatop.model.Message;
import com.openclassrooms.chatop.model.Rental;
import com.openclassrooms.chatop.model.User;

import com.openclassrooms.chatop.service.CustomMessageDetailsService;
import com.openclassrooms.chatop.service.CustomRentalDetailsService;
import com.openclassrooms.chatop.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomRentalDetailsService customRentalDetailsService;
    private final CustomMessageDetailsService customMessageDetailsService;

    /**
     * Handles the creation of a new message using the provided message data.
     *
     * @param messageDTO the data transfer object containing the message details,
     *                   including rental ID, user ID, and the message content
     * @return a ResponseEntity containing a map with a status message
     */
    @PostMapping("")
    public ResponseEntity<Map<String, String>> createMessage(
            @RequestBody MessageDTO messageDTO
    ) {
        try {
            if (messageDTO.getRental_id() == null || messageDTO.getUser_id() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "User not authenticated !"));
            }

            Optional<User> userOptional = customUserDetailsService.getUser(Long.valueOf(messageDTO.getUser_id()));
            Optional<Rental> rentalOptional = customRentalDetailsService.getRental(Long.valueOf(messageDTO.getRental_id()));

            if (userOptional.isEmpty() || rentalOptional.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "User or Rental not found !"));
            }

            if (messageDTO.getMessage().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Message is empty !"));
            }

            User user = userOptional.get();
            Rental rental = rentalOptional.get();

            Message message = new Message();
            message.setUser(user);
            message.setRental(rental);
            message.setMessage(messageDTO.getMessage());

            customMessageDetailsService.saveMessage(message);

            return ResponseEntity.ok(Map.of("message", "Message send with success !"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error when creating the message !"));
        }
    }
}
