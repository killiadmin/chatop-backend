package com.openclassrooms.chatop.service;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.openclassrooms.chatop.model.User;
import com.openclassrooms.chatop.repository.UserRepository;


import lombok.Data;


@Data

@Service

public  class UserService {

    @Autowired

    private UserRepository userRepository;

    public Optional<User> getUser(final Long id) {

        return userRepository.findById(id);

    }

    public Iterable<User> getUsers() {

        return userRepository.findAll();

    }

    public void deleteUser(final Long id) {

        userRepository.deleteById(id);

    }

    public User saveUser(User employee) {

        return userRepository.save(employee);

    }
}
