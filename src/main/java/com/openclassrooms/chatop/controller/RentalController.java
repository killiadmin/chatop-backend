package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.RentalDTO;
import com.openclassrooms.chatop.mapper.RentalMapper;
import com.openclassrooms.chatop.model.Rental;
import com.openclassrooms.chatop.model.User;
import com.openclassrooms.chatop.repository.UserRepository;
import com.openclassrooms.chatop.service.CustomRentalDetailsService;
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
public class RentalController {

    private final CustomRentalDetailsService customRentalDetailsService;
    private final UserRepository userRepository;
    private final RentalMapper rentalMapper;

    /**
     * Retrieves the list of all rentals and maps them to their DTO representation.
     *
     * @return a ResponseEntity containing a map with a single key "rentals"
     */
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



    /**
     * Retrieves the rental details for the specified rental ID.
     *
     * @param id the ID of the rental to retrieve
     * @return a ResponseEntity containing the rental details
     */
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

    /**
     * Creates a new rental and associates it with the authenticated user.
     *
     * @param name the name of the rental
     * @param surface the surface area of the rental in square meters
     * @param price the price of the rental
     * @param description a description of the rental
     * @param picture an optional picture file for the rental
     * @param authentication the authentication object containing the credentials of the current user
     * @return a ResponseEntity with a message indicating success or failure, and appropriate HTTP status
     */
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

    /**
     * Updates an existing rental with the specified details.
     *
     * @param id the ID of the rental to update
     * @param name the new name of the rental
     * @param surface the new surface area of the rental in square meters
     * @param price the new price of the rental
     * @param description the new description of the rental
     * @param authentication the authentication object containing the credentials of the current user
     * @return a ResponseEntity containing a map
     */
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
