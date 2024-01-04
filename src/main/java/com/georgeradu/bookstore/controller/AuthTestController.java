package com.georgeradu.bookstore.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthTestController
 * Used for testing authentication and role authorization
 * kept as a reference for later features
 */
@RestController
@RequestMapping("/auth-test")
public class AuthTestController {
    @GetMapping("/anyone")
    public String anyoneEndpoint() {
        // fetch the logged user info
        return "anyone can see this";
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('USER')")
    public String usersEndpoint() {
        return "only users can see this";
    }

    @GetMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminsEndpoint() {
        return "only admins can see this";
    }
}
