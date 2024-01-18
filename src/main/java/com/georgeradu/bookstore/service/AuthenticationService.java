package com.georgeradu.bookstore.service;

import com.georgeradu.bookstore.dto.JwtAuthenticationResponse;
import com.georgeradu.bookstore.dto.LoginRequest;
import com.georgeradu.bookstore.dto.RegisterRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse register(RegisterRequest request);

    JwtAuthenticationResponse login(LoginRequest request);
}
