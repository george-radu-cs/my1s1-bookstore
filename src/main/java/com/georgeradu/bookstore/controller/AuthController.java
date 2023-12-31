package com.georgeradu.bookstore.controller;

import com.georgeradu.bookstore.dto.JwtAuthenticationResponse;
import com.georgeradu.bookstore.dto.LoginRequest;
import com.georgeradu.bookstore.dto.RegisterRequest;
import com.georgeradu.bookstore.exception.DuplicateObjectException;
import com.georgeradu.bookstore.exception.InvalidLoginException;
import com.georgeradu.bookstore.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "Provides endpoints for user authentication")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public JwtAuthenticationResponse register(@RequestBody RegisterRequest request) throws DuplicateObjectException {
        return authenticationService.register(request);
    }

    @PostMapping("/login")
    public JwtAuthenticationResponse login(@RequestBody LoginRequest request) throws InvalidLoginException {
        return authenticationService.login(request);
    }
}
