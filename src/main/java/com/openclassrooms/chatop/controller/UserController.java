package com.openclassrooms.chatop.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.chatop.model.User;
import com.openclassrooms.chatop.service.CustomUserDetailsService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping("/users")
    public Iterable<User> getUsers() {
        return customUserDetailsService.getUsers();
    }
}
