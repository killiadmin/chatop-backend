package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.configuration.JwtUtils;
import com.openclassrooms.chatop.dto.LoginDTO;
import com.openclassrooms.chatop.dto.RegisterDTO;
import com.openclassrooms.chatop.dto.UserDTO;
import com.openclassrooms.chatop.exception.UnauthorizedException;
import com.openclassrooms.chatop.model.User;
import com.openclassrooms.chatop.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomAuthDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public String authenticateAndGenerateToken(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Invalid credentials !");
        }

        return jwtUtils.generateToken(authentication);
    }

    /**
     * Registers a new user with the provided details and generates a JWT token for authentication.
     *
     * @param registerDTO an object containing the registration details
     * @return a signed JWT token as a string for the newly registered user
     * @throws Exception if any of the registration details are missing
     */
    public String registerAndGenerateToken(RegisterDTO registerDTO) throws Exception {
        String email = registerDTO.getEmail().trim();
        String password = registerDTO.getPassword().trim();
        String name = registerDTO.getName().trim();

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || userRepository.findByEmail(email) != null) {
            throw new Exception("An error occurred during recording !");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);

        userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        return jwtUtils.generateToken(authentication);
    }

    /**
     * Retrieves the details of an authenticated user based on their email.
     *
     * @param email the email address of the authenticated user
     * @return a UserDTO object containing the authenticated user's details
     */
    public UserDTO getAuthenticatedMe(String email) {
        User user = userRepository.findByEmail(email);

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
