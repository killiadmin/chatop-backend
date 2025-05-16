package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.LoginDTO;
import com.openclassrooms.chatop.dto.RegisterDTO;
import com.openclassrooms.chatop.dto.UserDTO;

import com.openclassrooms.chatop.exception.UnauthorizedException;

import com.openclassrooms.chatop.service.CustomAuthDetailsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthController {


    private final CustomAuthDetailsService customAuthDetailsService;


    @Operation(
            summary = "Login a user",
            description = "Authenticate a user using email and password and returns a JWT token.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful"),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
            }
    )
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginDTO loginDTO) {
        String token = customAuthDetailsService.authenticateAndGenerateToken(loginDTO);

        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }


    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account and returns a JWT token upon successful registration.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Registration successful"),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO request) throws Exception {
        String token = customAuthDetailsService.registerAndGenerateToken(request);

        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }


    @Operation(
            summary = "Get authenticated user",
            description = "Retrieves the details of the currently authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
            }
    )
    @GetMapping("/me")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        if (authentication == null) {
            throw new UnauthorizedException("User not found !");
        }

        String email = authentication.getName();
        UserDTO userDTO = customAuthDetailsService.getAuthenticatedMe(email);

        return ResponseEntity.ok(userDTO);
    }
}
