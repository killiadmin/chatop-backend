package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.configuration.JwtUtils;
import com.openclassrooms.chatop.model.User;
import com.openclassrooms.chatop.repository.UserRepository;

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
import java.util.LinkedHashMap;
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

    /**
     * Authenticates a user based on the provided credentials and generates a JWT token upon successful authentication.
     *
     * @param credentials a map containing the user's email and password.
     *
     * @return a {@code ResponseEntity}
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String email = extractCredential(credentials, "email");
            String password = extractCredential(credentials, "password");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            String token = jwtUtils.generateToken(authentication);

            return ResponseEntity.ok(buildLoginResponse(token));
        } catch (AuthenticationException e) {
            log.error("Erreur d'authentification : {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Identifiants incorrects !");
        }
    }

    /**
     * Registers a new user using the provided user data.
     *
     * @param userData a map containing user registration data.
     *
     * @return a {@code ResponseEntity}
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> userData) {
        try {
            String email = userData.get("email");
            String password = userData.get("password");
            String name = userData.get("name");

            if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty() || name == null || name.trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Tous les champs (email, password, name) sont obligatoires.");
            }

            if (userRepository.findByEmail(email) != null) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Un utilisateur avec cet email existe déjà !");
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

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement : {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue lors de l'inscription.");
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
            String email = authentication.getName();

            User user = userRepository.findByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Utilisateur non trouvé.");
            }

            Map<String, Object> userDetails = new LinkedHashMap<>();
            userDetails.put("id", user.getId());
            userDetails.put("name", user.getName());
            userDetails.put("email", user.getEmail());
            userDetails.put("role", user.getRole());
            userDetails.put("createdAt", user.getCreatedAt());
            userDetails.put("updatedAt", user.getUpdatedAt());

            return ResponseEntity.ok(userDetails);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des informations utilisateur : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue.");
        }
    }

    /**
     * Extracts a credential value from the provided map of credentials using the specified key.
     *
     * @param credentials a map containing user credentials as key-value pairs
     * @param key the key whose corresponding value needs to be extracted
     *
     * @return the value associated with the specified key, or an empty string if the key does not exist
     */
    private String extractCredential(Map<String, String> credentials, String key) {
        return credentials.getOrDefault(key, "");
    }

    /**
     * Builds a response containing a single token entry.
     *
     * @param token the JWT token to be included in the response
     *
     * @return a map with a single key-value pair where the key is "token" and the value is the provided token
     */
    private Map<String, String> buildLoginResponse(String token) {
        return Collections.singletonMap("token", token);
    }
}
