package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.mapper.RentalMapper;
import com.openclassrooms.chatop.model.Rental;
import com.openclassrooms.chatop.model.User;
import com.openclassrooms.chatop.repository.UserRepository;
import com.openclassrooms.chatop.service.CustomRentalDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final CustomRentalDetailsService customRentalDetailsService;
    private final UserRepository userRepository;

    public RentalController(
            CustomRentalDetailsService customRentalDetailsService,
            UserRepository userRepository
    ) {
        this.customRentalDetailsService = customRentalDetailsService;
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public ResponseEntity<Map<String, List<RentalMapper>>> getRentals() {
        List<Rental> rentals = (List<Rental>) customRentalDetailsService.getRentals();

        List<RentalMapper> rentalDtos = rentals.stream()
                .map(this::mapRentalToDto)
                .toList();

        Map<String, List<RentalMapper>> response = Map.of("rentals", rentalDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalMapper> getRental(@PathVariable Long id) {
        Optional<Rental> rentalOpt = customRentalDetailsService.getRental(id);

        if (rentalOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        RentalMapper rentalDto = mapRentalToDto(rentalOpt.get());
        return ResponseEntity.ok(rentalDto);
    }

    @PostMapping("")
    public ResponseEntity<String> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") Integer surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description,
            @RequestParam(value = "picture", required = false) MultipartFile picture,
            Authentication authentication
    ) {
        try {
            String email = authentication.getName();
            User currentUser = userRepository.findByEmail(email);

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non authentifié.");
            }

            Rental rental = new Rental();
            rental.setName(name);
            rental.setSurface(surface);
            rental.setPrice(price);
            rental.setDescription(description);
            rental.setOwner(currentUser);

            if (picture != null && !picture.isEmpty()) {
                rental.setPicture(picture.getBytes()); // Pas de base64 ici !!
            }

            customRentalDetailsService.saveRental(rental);

            return ResponseEntity.ok("Rental créé et image sauvegardée !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la création de la location.");
        }
    }

    private RentalMapper mapRentalToDto(Rental rental) {
        RentalMapper dto = new RentalMapper();
        dto.setId(rental.getId());
        dto.setName(rental.getName());
        dto.setSurface(rental.getSurface());
        dto.setPrice(rental.getPrice());
        dto.setDescription(rental.getDescription());
        dto.setCreatedAt(rental.getCreatedAt());
        dto.setUpdatedAt(rental.getUpdatedAt());
        dto.setOwnerId(rental.getOwner().getId());

        if (rental.getPicture() != null) {
            String base64Image = Base64.getEncoder().encodeToString(rental.getPicture());
            dto.setPicture("data:image/jpeg;base64," + base64Image);
        } else {
            dto.setPicture(null);
        }

        return dto;
    }
}
