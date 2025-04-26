package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.model.Rental;
import com.openclassrooms.chatop.repository.RentalRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Data
@Service
@RequiredArgsConstructor
public class CustomRentalDetailsService {

    private final RentalRepository rentalRepository;

    public Iterable<Rental> getRentals() {
        return rentalRepository.findAll();
    }

    public Optional<Rental> getRental(final Long id) {
        return rentalRepository.findById(id);
    }

    public Rental updateRental(Rental rental) {
        return rentalRepository.save(rental);
    }

    public void deleteRental(final Long id) {
        rentalRepository.deleteById(id);
    }

    public void saveRental(Rental rental) {
        rentalRepository.save(rental);
    }
}
