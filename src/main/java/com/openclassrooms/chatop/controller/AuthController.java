package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.configuration.JwtUtils;
import com.openclassrooms.chatop.dto.LoginDTO;
import com.openclassrooms.chatop.dto.RegisterDTO;
import com.openclassrooms.chatop.dto.UserDTO;
import com.openclassrooms.chatop.mapper.UserMapper;
import com.openclassrooms.chatop.model.User;
import com.openclassrooms.chatop.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * Authenticates a user using the provided credentials and generates a JWT token upon successful authentication.
     *
     * @param loginDTO the login data transfer object containing the user's email and password for authentication.
     *
     * @return a {@code ResponseEntity}
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            String email = loginDTO.getEmail();
            String password = loginDTO.getPassword();

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            String token = jwtUtils.generateToken(authentication);

            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (AuthenticationException e) {
            log.error("Authentication error : {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "error"));
        }
    }

    /**
     * Handles the user registration process by validating the input.
     *
     * @param request the registration data containing email, password, and name, provided as a {@code RegisterDTO}.
     *                Must be a valid and non-null object.
     * @return a {@code ResponseEntity}
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO request) {
        try {
            String email = request.getEmail();
            String password = request.getPassword();
            String name = request.getName();

            if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty() || name == null || name.trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Collections.emptyMap());
            }

            if (userRepository.findByEmail(email) != null) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("A user with this email already exists !");
            }

            String encodedPassword = passwordEncoder.encode(password);

            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(encodedPassword);
            newUser.setName(name);
            userRepository.save(newUser);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            String token = jwtUtils.generateToken(authentication);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error when register : {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Collections.emptyMap());
        }
    }

    /**
     * Retrieves the details of the currently authenticated user.
     *
     * @param authentication the authentication object containing the current user's information.
     *
     * @return a {@code ResponseEntity}
     */
    @GetMapping("/me")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.emptyMap());
            }

            String email = authentication.getName();
            User user = userRepository.findByEmail(email);

            if (user == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.emptyMap());
            }

            UserDTO userDTO = userMapper.toDTO(user);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            log.error("Error when recovering user information: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.emptyMap());
        }
    }
}
