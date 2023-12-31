package com.georgeradu.bookstore.service;

import com.georgeradu.bookstore.dto.JwtAuthenticationResponse;
import com.georgeradu.bookstore.dto.LoginRequest;
import com.georgeradu.bookstore.dto.RegisterRequest;
import com.georgeradu.bookstore.exception.DuplicateObjectException;
import com.georgeradu.bookstore.exception.InvalidLoginException;
import com.georgeradu.bookstore.model.User;
import com.georgeradu.bookstore.model.UserRole;
import com.georgeradu.bookstore.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder,
            JwtService jwtService, AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public JwtAuthenticationResponse register(RegisterRequest request) throws DuplicateObjectException {
        var user = User
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.ROLE_USER)
                .build();

        user = userService.addUser(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }


    public JwtAuthenticationResponse login(LoginRequest request) throws InvalidLoginException {
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(InvalidLoginException::new);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }
}
