package com.georgeradu.bookstore.controller;

import com.georgeradu.bookstore.dto.ShoppingCartItemRequest;
import com.georgeradu.bookstore.dto.ShoppingCartItemResponse;
import com.georgeradu.bookstore.dto.SpringErrorResponse;
import com.georgeradu.bookstore.service.ShoppingCartItemService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopping-cart")
@Validated
@Tag(name = "Shopping Cart Controller", description = "Provides endpoints for shopping cart")
public class ShoppingCartController {
    private final ShoppingCartItemService shoppingCartItemService;

    public ShoppingCartController(ShoppingCartItemService shoppingCartItemService) {
        this.shoppingCartItemService = shoppingCartItemService;
    }

    @GetMapping()
    @Operation(summary = "Get current user shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved shopping cart"),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<List<ShoppingCartItemResponse>> getCurrentUserShoppingCart() {
        var loggedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var response = shoppingCartItemService.getUserShoppingCartByEmail(loggedUser.getUsername());
        return ResponseEntity.ok(ShoppingCartItemResponse.fromList(response));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get a shopping cart by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved shopping cart"),
            @ApiResponse(responseCode = "400", description = "Invalid user id",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<List<ShoppingCartItemResponse>> adminGetUserShoppingCart(@PathVariable Long userId) {
        var response = shoppingCartItemService.getUserShoppingCart(userId);
        return ResponseEntity.ok(ShoppingCartItemResponse.fromList(response));
    }

    @PostMapping()
    @Operation(summary = "Add a book to the user's shopping cart")
    @ApiResponse(responseCode = "200", description = "Successfully added book to shopping cart")
    public ResponseEntity<ShoppingCartItemResponse> addBookToShoppingCart(@Validated @RequestBody ShoppingCartItemRequest request) {
        var loggedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var response = shoppingCartItemService.addBookToShoppingCart(loggedUser.getUsername(), request);
        return ResponseEntity.ok(new ShoppingCartItemResponse(response));
    }

    @PutMapping("/{shoppingCartItemId}")
    @Operation(summary = "Update a shopping cart item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated shopping cart item"),
            @ApiResponse(responseCode = "400", description = "Invalid request body, validation failed",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Shopping cart item not found",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<ShoppingCartItemResponse> updateShoppingCartItem(@PathVariable Long shoppingCartItemId, @Validated @RequestBody ShoppingCartItemRequest request) {
        var loggedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var response = shoppingCartItemService.updateShoppingCartItem(loggedUser.getUsername(), shoppingCartItemId, request);
        return ResponseEntity.ok(new ShoppingCartItemResponse(response));
    }

    @DeleteMapping("/{shoppingCartItemId}")
    @Operation(summary = "Delete a shopping cart item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted shopping cart item"),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Shopping cart item not found",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<Void> deleteShoppingCartItem(@PathVariable Long shoppingCartItemId) {
        var loggedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        shoppingCartItemService.deleteShoppingCartItem(loggedUser.getUsername(), shoppingCartItemId);
        return ResponseEntity.ok().build();
    }
}
