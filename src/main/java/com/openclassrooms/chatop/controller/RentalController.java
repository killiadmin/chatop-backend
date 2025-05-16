package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.RentalDTO;

import com.openclassrooms.chatop.dto.RentalListDto;
import com.openclassrooms.chatop.model.Rental;

import com.openclassrooms.chatop.service.CustomRentalDetailsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
@Tag(name = "Rentals", description = "Endpoints for managing property rentals")
public class RentalController {


    private final CustomRentalDetailsService customRentalDetailsService;


    @Operation(
            summary = "Get all rentals",
            description = "Returns a list of all rentals for authenticated users.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of rentals returned",
                            content = @Content(schema = @Schema(implementation = RentalDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    @GetMapping("")
    public ResponseEntity<RentalListDto> getAllRentals() {
        List<Rental> rentals = customRentalDetailsService.getAllRentals();
        return ResponseEntity.ok(new RentalListDto(rentals));
    }


    @Operation(
            summary = "Get a rental by ID",
            description = "Fetches rental details by its ID for authenticated users.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rental found",
                            content = @Content(schema = @Schema(implementation = RentalDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Rental>> getRental(@PathVariable Long id) {
        Optional<Rental> rental = customRentalDetailsService.getRentalById(id);
        return ResponseEntity
                .ok(rental);
    }


    @Operation(
            summary = "Create a new rental",
            description = "Creates a new rental property and associates it with the authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rental created successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized or invalid request")
            }
    )
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") Integer surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description,
            @RequestParam(value = "picture", required = false) MultipartFile picture,
            Authentication authentication
    ) throws IOException {
        customRentalDetailsService.createRental(name, surface, price, description, picture, authentication.getName());

        return ResponseEntity.ok(Map.of("message", "Rental created !"));
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
            @RequestParam("description") String description
    ) {
        customRentalDetailsService.updateRental(id, name, surface, price, description);

        return ResponseEntity.ok(Map.of("message", "Rental updated !"));
    }
}
