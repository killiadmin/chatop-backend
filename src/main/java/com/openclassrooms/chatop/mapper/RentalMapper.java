package com.openclassrooms.chatop.mapper;

import com.openclassrooms.chatop.dto.RentalDTO;
import com.openclassrooms.chatop.model.Rental;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class RentalMapper {

    /**
     * Converts a Rental entity to its corresponding RentalDTO.
     *
     * @param rental the Rental entity to be converted
     * @return the converted RentalDTO object
     */
    public RentalDTO toDTO(Rental rental) {
        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setId(rental.getId());
        rentalDTO.setName(rental.getName());
        rentalDTO.setSurface(rental.getSurface());
        rentalDTO.setPrice(rental.getPrice());
        rentalDTO.setDescription(rental.getDescription());
        rentalDTO.setOwner_id(rental.getOwner() != null ? rental.getOwner().getId() : null);
        rentalDTO.setCreated_at(rental.getCreated_at());
        rentalDTO.setUpdated_at(rental.getUpdated_at());

        if (rental.getPicture() != null) {
            String base64Image = Base64.getEncoder().encodeToString(rental.getPicture());
            rentalDTO.setPicture("data:image/jpeg;base64," + base64Image);
        } else {
            rentalDTO.setPicture(null);
        }

        return rentalDTO;
    }
}
