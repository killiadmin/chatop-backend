package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.RentalDTO;

import com.openclassrooms.chatop.mapper.RentalMapper;

import com.openclassrooms.chatop.model.Rental;
import com.openclassrooms.chatop.model.User;

import com.openclassrooms.chatop.repository.UserRepository;
import com.openclassrooms.chatop.service.CustomRentalDetailsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
@Tag(name = "Rentals", description = "Endpoints for managing property rentals")
public class RentalController {

    private final CustomRentalDetailsService customRentalDetailsService;
    private final UserRepository userRepository;
    private final RentalMapper rentalMapper;

    @Operation(
            summary = "Get all rentals",
            description = "Returns a list of all rentals for authenticated users.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of rentals returned",
                            content = @Content(schema = @Schema(implementation = RentalDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content)
            }
    )
    @GetMapping("")
    public ResponseEntity<Map<String, List<RentalDTO>>> getRentals(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.emptyMap());
        }

        List<Rental> rentals = (List<Rental>) customRentalDetailsService.getRentals();

        List<RentalDTO> rentalDtos = rentals.stream()
                .map(rentalMapper::toDTO)
                .toList();

        Map<String, List<RentalDTO>> response = Map.of("rentals", rentalDtos);
        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Get a rental by ID",
            description = "Fetches rental details by its ID for authenticated users.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rental found",
                            content = @Content(schema = @Schema(implementation = RentalDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Rental not found", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRental(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }

        return customRentalDetailsService.getRental(id)
                .map(rentalMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @Operation(
            summary = "Create a new rental",
            description = "Creates a new rental property and associates it with the authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rental created successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized or invalid request", content = @Content)
            }
    )
    @PostMapping("")
    public ResponseEntity<Map<String, String>> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") Integer surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description,
            @RequestParam(value = "picture", required = false) MultipartFile picture,
            Authentication authentication
    ) {
        try {
            if (authentication == null || authentication.getName() == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.emptyMap());
            }

            String email = authentication.getName();
            User currentUser = userRepository.findByEmail(email);

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.emptyMap());
            }

            Rental rental = new Rental();
            rental.setName(name);
            rental.setSurface(surface);
            rental.setPrice(price);
            rental.setDescription(description);
            rental.setOwner(currentUser);

            if (picture != null && !picture.isEmpty()) {
                rental.setPicture(picture.getBytes());
            }

            customRentalDetailsService.saveRental(rental);

            return ResponseEntity.ok(Map.of("message", "Rental created !"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.emptyMap());
        }
    }

    @Operation(
            summary = "Update an existing rental",
            description = "Updates the rental information of a rental owned by the authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rental updated successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized or rental not found", content = @Content)
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateRental(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("surface") Integer surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description,
            Authentication authentication
    ) {
        try {
            if (authentication == null || authentication.getName() == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.emptyMap());
            }

            String email = authentication.getName();

            User currentUser = userRepository.findByEmail(email);
            if (currentUser == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.emptyMap());
            }

            Rental rental = customRentalDetailsService.getRental(id).orElse(null);

            if (rental == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.emptyMap());
            }

            rental.setName(name);
            rental.setSurface(surface);
            rental.setPrice(price);
            rental.setDescription(description);

            customRentalDetailsService.updateRental(rental);

            return ResponseEntity
                    .ok(Map.of("message", "Rental updated !"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.emptyMap());
        }
    }
}
