package com.openclassrooms.chatop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @GetMapping("")
    public ResponseEntity<?> getRentals() {
        return null;
    }
}
