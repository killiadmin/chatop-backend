package com.openclassrooms.chatop.repository;

import com.openclassrooms.chatop.model.Rental;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository  extends JpaRepository<Rental, Long> {
}
