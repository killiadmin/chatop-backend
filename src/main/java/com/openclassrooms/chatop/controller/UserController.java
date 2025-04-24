package com.openclassrooms.chatop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.chatop.model.User;
import com.openclassrooms.chatop.service.CustomUserDetailsService;

@RestController
public class UserController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @GetMapping("/users")
    public Iterable<User> getUsers() {
        return customUserDetailsService.getUsers();
    }
}
