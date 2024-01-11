package com.georgeradu.bookstore.controller;

import com.georgeradu.bookstore.dto.SpringErrorResponse;
import com.georgeradu.bookstore.dto.UserResponse;
import com.georgeradu.bookstore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Validated
@Tag(name = "User Controller", description = "Provides endpoints for users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user info"),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<UserResponse> getCurrentUserInfo() {
        var loggedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var response = userService.getUserByEmail(loggedUser.getUsername());
        return ResponseEntity.ok(new UserResponse(response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
            @ApiResponse(responseCode = "400", description = "Invalid user id",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<UserResponse> adminGetUser(@PathVariable Long id) {
        var response = userService.getUser(id);
        return ResponseEntity.ok(new UserResponse(response));
    }
}
