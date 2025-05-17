package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.dto.RentalDTO;
import com.openclassrooms.chatop.exception.UnauthorizedException;
import com.openclassrooms.chatop.model.Rental;
import com.openclassrooms.chatop.model.User;
import com.openclassrooms.chatop.repository.RentalRepository;
import com.openclassrooms.chatop.repository.UserRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@Service
@RequiredArgsConstructor
public class CustomRentalDetailsService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    /**
     * Retrieves all rental entries
     *
     * @return a map containing a single entry where the key is "rentals" and the value
     *         is a list of RentalDTO objects representing all rentals in the system
     */
    public Map<String, List<RentalDTO>> getAllRentals() {
        List<Rental> rentals = findAllRentals();

        List<RentalDTO> rentalDtos = rentals.stream()
                .map(this::buildRentalDTO)
                .toList();

        return Map.of("rentals", rentalDtos);
    }

    /**
     * Retrieves a rental by its unique identifier and converts it into a RentalDTO.
     *
     * @param id the unique identifier of the rental
     * @return an Optional containing the RentalDTO
     */
    public Optional<RentalDTO> getRentalById(final Long id) {
        return rentalRepository.findById(id)
                .map(this::buildRentalDTO);
    }

    /**
     * Creates a new rental and saves it to the repository.
     *
     * @param name the name of the rental
     * @param surface the surface area of the rental in square meters
     * @param price the price of the rental
     * @param description a textual description of the rental
     * @param picture an optional picture file representing the rental
     * @param userEmail the email address of the user creating the rental
     * @throws IOException if an error occurs while processing the picture file
     */
    public void createRental(String name, Integer surface, BigDecimal price, String description, MultipartFile picture, String userEmail) throws IOException {
        User currentUser = getUserByEmail(userEmail);

        Rental rental = new Rental();
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);
        rental.setOwner(currentUser);

        if (picture != null && !picture.isEmpty()) {
            rental.setPicture(picture.getBytes());
        }

        rentalRepository.save(rental);
    }

    /**
     * Updates the details of an existing rental identified by its unique identifier.
     *
     * @param id the unique identifier of the rental to be updated
     * @param name the new name for the rental
     * @param surface the new surface area of the rental in square meters
     * @param price the new price for the rental
     * @param description the new textual description of the rental
     * @throws UnauthorizedException if the rental with the specified ID is not found
     */
    public void updateRental(Long id, String name, Integer surface, BigDecimal price, String description) {
        Rental rental = rentalRepository.findById(id).orElseThrow(() -> new UnauthorizedException("Rental not found !"));

        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);

        rentalRepository.save(rental);
    }

    /**
     * Retrieves a User entity by their email address.
     *
     * @param email the email address of the user to be retrieved
     * @return the User entity corresponding to the given email address
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Retrieves a rental entity by its unique identifier.
     *
     * @param id the unique identifier of the rental to be retrieved
     * @return an Optional containing the Rental if found
     */
    public Optional<Rental> getRental(final Long id) {
        return rentalRepository.findById(id);
    }

    /**
     * Retrieves a list of all rental entries from the repository.
     *
     * @return a list of all {@link Rental} entities stored in the repository
     */
    private List<Rental> findAllRentals() {
        return rentalRepository.findAll();
    }

    /**
     * Builds a RentalDTO object from a given Rental model.
     *
     * @param rental the Rental model used to build the RentalDTO
     * @return a RentalDTO object containing the data from the given Rental model
     */
    private RentalDTO buildRentalDTO(Rental rental) {
        RentalDTO dto = new RentalDTO();
        dto.setId(rental.getId());
        dto.setName(rental.getName());
        dto.setSurface(rental.getSurface());
        dto.setPrice(rental.getPrice());
        dto.setDescription(rental.getDescription());
        dto.setOwner_id(rental.getOwner().getId());
        dto.setCreated_at(rental.getCreated_at());
        dto.setUpdated_at(rental.getUpdated_at());

        if (rental.getPicture() != null) {
            String base64Image = Base64.getEncoder().encodeToString(rental.getPicture());
            dto.setPicture("data:image/jpeg;base64," + base64Image);
        } else {
            dto.setPicture(null);
        }

        return dto;
    }
}
