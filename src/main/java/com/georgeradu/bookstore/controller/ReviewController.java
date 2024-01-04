package com.georgeradu.bookstore.controller;

import com.georgeradu.bookstore.dto.ReviewRequest;
import com.georgeradu.bookstore.dto.ReviewResponse;
import com.georgeradu.bookstore.dto.SpringErrorResponse;
import com.georgeradu.bookstore.exception.DuplicateObjectException;
import com.georgeradu.bookstore.exception.EntityNotFoundException;
import com.georgeradu.bookstore.exception.InvalidUserAccessException;
import com.georgeradu.bookstore.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@Validated
@Tag(name = "Review Controller", description = "Provides endpoints for reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a review by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved review"),
            @ApiResponse(responseCode = "404", description = "Review not found",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long id) throws EntityNotFoundException {
        var response = reviewService.getReview(id);
        return ResponseEntity.ok(new ReviewResponse(response));
    }

    @GetMapping("/for-book/{bookId}")
    @Operation(summary = "Get all reviews for a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved reviews for book"),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<List<ReviewResponse>> getReviewsForBook(@PathVariable Long bookId) throws
            EntityNotFoundException {
        var response = reviewService.getReviewsForBook(bookId);
        return ResponseEntity.ok(ReviewResponse.fromList(response));
    }

    @GetMapping("/for-user/{userId}")
    @Operation(summary = "Get all reviews for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved reviews for user"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<List<ReviewResponse>> getReviewsForUser(@PathVariable Long userId) throws
            EntityNotFoundException {
        var response = reviewService.getReviewsForUser(userId);
        return ResponseEntity.ok(ReviewResponse.fromList(response));
    }

    @PostMapping()
    @Operation(summary = "Create a new review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created review"),
            @ApiResponse(responseCode = "400", description = "Invalid request body, validation failed",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Review already exists",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<ReviewResponse> createReview(@Validated @RequestBody ReviewRequest request) throws
            EntityNotFoundException, DuplicateObjectException {
        var loggedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var response = reviewService.createReview(request, loggedUser.getUsername());
        return ResponseEntity.ok(new ReviewResponse(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated review"),
            @ApiResponse(responseCode = "400", description = "Invalid request body, validation failed",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, only the review owner can update it",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Review not found",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long id, @Validated @RequestBody ReviewRequest request
    ) throws
            EntityNotFoundException, InvalidUserAccessException {
        var loggedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var response = reviewService.updateReview(id, request, loggedUser.getUsername());
        return ResponseEntity.ok(new ReviewResponse(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted review"),
            @ApiResponse(responseCode = "403", description = "Forbidden, only the review owner can delete it",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Review not found",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) throws EntityNotFoundException {
        var loggedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        reviewService.deleteReview(id, loggedUser.getUsername());
        return ResponseEntity.ok().build();
    }
}
