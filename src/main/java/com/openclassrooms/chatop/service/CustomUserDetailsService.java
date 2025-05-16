package com.openclassrooms.chatop.service;

import java.util.Optional;

import com.openclassrooms.chatop.dto.UserDTO;
import com.openclassrooms.chatop.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import com.openclassrooms.chatop.model.User;
import com.openclassrooms.chatop.repository.UserRepository;

import lombok.Data;

@Data

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads a user's authentication details by their email.
     *
     * @param email the email of the user whose authentication details are to be loaded
     * @return UserDetails object containing the user's information including username, password, and roles
     * @throws UsernameNotFoundException if no user is found with the given email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.openclassrooms.chatop.model.User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email : " + email);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user to be retrieved
     * @return an Optional containing the User if found, or an empty Optional if no user is found with the given id
     */
    public Optional<User> getUser(final Long id) {
        return userRepository.findById(id);
    }

    /**
     * Retrieves a user by their unique identifier
     *
     * @param id the unique identifier of the user to be retrieved
     * @return a UserDTO object containing the user's details
     * @throws UnauthorizedException if no user is found with the given id
     */
    public UserDTO getUserById(Long id) {
        return getUser(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new UnauthorizedException("User not found !"));
    }

    /**
     * Maps a User model
     *
     * @param user the User model to be mapped
     * @return a UserDTO object containing the details of the user
     */
    private UserDTO mapToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .created_at(user.getCreated_at())
                .updated_at(user.getUpdated_at())
                .build();
    }
}
