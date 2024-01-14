package com.georgeradu.bookstore.controller;

import com.georgeradu.bookstore.dto.OrderInfoResponse;
import com.georgeradu.bookstore.dto.OrderItemResponse;
import com.georgeradu.bookstore.dto.SpringErrorResponse;
import com.georgeradu.bookstore.service.OrderService;
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
@RequestMapping("/order")
@Validated
@Tag(name = "Orders Controller", description = "Provides endpoints for orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/new-order")
    @Operation(summary = "Make an order from current user shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully made order"),
            @ApiResponse(responseCode = "400", description = "Invalid shipping address",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Shopping cart is empty",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<OrderInfoResponse> makeOrderFromShoppingCart(@RequestParam String shippingAddress) {
        var loggedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var response = orderService.saveUserShoppingCartAsOrder(loggedUser.getUsername(), shippingAddress);
        return ResponseEntity.ok(new OrderInfoResponse(response));
    }

    @PutMapping("/cancel-order/{orderId}")
    @Operation(summary = "Cancel an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully cancelled order"),
            @ApiResponse(responseCode = "400", description = "Invalid order id",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Order is already delivered or cancelled",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class)))
    })
    public ResponseEntity<OrderInfoResponse> cancelOrder(@PathVariable Long orderId) {
        var loggedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var response = orderService.cancelOrder(loggedUser.getUsername(), orderId);
        return ResponseEntity.ok(new OrderInfoResponse(response));
    }

    @PutMapping("/deliver-order/{orderId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Mark an order as delivered")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully marked order as delivered"),
            @ApiResponse(responseCode = "400", description = "Invalid order id",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Order is already delivered or cancelled",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<OrderInfoResponse> deliverOrder(@PathVariable Long orderId) {
        var response = orderService.setOrderToBeDelivered(orderId);
        return ResponseEntity.ok(new OrderInfoResponse(response));
    }

    @GetMapping("/my-order-info/{orderId}")
    @Operation(summary = "Get current user order info by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved order info"),
            @ApiResponse(responseCode = "400", description = "Invalid order id",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<OrderInfoResponse> getCurrentUserOrderInfoById(@PathVariable Long orderId) {
        var loggedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var response = orderService.getUserOrderInfoById(loggedUser.getUsername(), orderId);
        return ResponseEntity.ok(new OrderInfoResponse(response));
    }

    @GetMapping("/my-order-items/{orderId}")
    @Operation(summary = "Get current user order items by order info id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved order items"),
            @ApiResponse(responseCode = "400", description = "Invalid order id",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<List<OrderItemResponse>> getCurrentUserOrderItemsByOrderInfoId(@PathVariable Long orderId) {
        var loggedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var response = orderService.getUserOrderItemsByOrderInfoId(loggedUser.getUsername(), orderId);
        return ResponseEntity.ok(OrderItemResponse.fromList(response));
    }

    @GetMapping("/my-order-history")
    @Operation(summary = "Get current user order history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved order history"),
            @ApiResponse(responseCode = "400", description = "Invalid order id",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<List<OrderInfoResponse>> getCurrentUserOrderHistory() {
        var loggedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var response = orderService.getUserOrderHistory(loggedUser.getUsername());
        return ResponseEntity.ok(OrderInfoResponse.fromList(response));
    }

    @GetMapping("/order-info/{orderId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Admin: Get order info by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved order info"),
            @ApiResponse(responseCode = "400", description = "Invalid order id",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<OrderInfoResponse> getOrderInfoById(@PathVariable Long orderId) {
        var response = orderService.getOrderInfoById(orderId);
        return ResponseEntity.ok(new OrderInfoResponse(response));
    }

    @GetMapping("/order-items/{orderId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Admin: Get order items by order info id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved order items"),
            @ApiResponse(responseCode = "400", description = "Invalid order id",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<List<OrderItemResponse>> getOrderItemsByOrderInfoId(@PathVariable Long orderId) {
        var response = orderService.getOrderItemsByOrderInfoId(orderId);
        return ResponseEntity.ok(OrderItemResponse.fromList(response));
    }

    @GetMapping("/order-history/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Admin: Get order history for user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved order history for user"),
            @ApiResponse(responseCode = "400", description = "Invalid user id",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<List<OrderInfoResponse>> getOrderHistoryForUserById(@PathVariable Long userId) {
        var response = orderService.getOrderHistoryForUserById(userId);
        return ResponseEntity.ok(OrderInfoResponse.fromList(response));
    }
}
