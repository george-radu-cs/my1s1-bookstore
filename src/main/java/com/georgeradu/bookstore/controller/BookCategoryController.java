package com.georgeradu.bookstore.controller;

import com.georgeradu.bookstore.dto.BookCategoryRequest;
import com.georgeradu.bookstore.dto.BookCategoryResponse;
import com.georgeradu.bookstore.dto.SpringErrorResponse;
import com.georgeradu.bookstore.exception.DuplicateObjectException;
import com.georgeradu.bookstore.exception.EntityNotFoundException;
import com.georgeradu.bookstore.service.BookCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book-category")
@Validated
@Tag(name = "Book Category Controller", description = "Provides endpoints for book categories")
public class BookCategoryController {
    private final BookCategoryService bookCategoryService;

    public BookCategoryController(BookCategoryService bookCategoryService) {
        this.bookCategoryService = bookCategoryService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved book category"),
            @ApiResponse(responseCode = "404", description = "Book category not found",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<BookCategoryResponse> getBookCategory(@PathVariable Long id) throws EntityNotFoundException {
        var response = bookCategoryService.getBookCategory(id);
        return ResponseEntity.ok(new BookCategoryResponse(response));
    }

    @GetMapping()
    @Operation(summary = "Get all book categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved book categories"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<List<BookCategoryResponse>> getBookCategories() {
        var response = bookCategoryService.getBookCategories();
        return ResponseEntity.ok(BookCategoryResponse.fromList(response));
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new book category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created book category"),
            @ApiResponse(responseCode = "400", description = "Invalid request body, validation failed",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Book category already exists",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<BookCategoryResponse> createBookCategory(
            @Valid @RequestBody BookCategoryRequest request
    ) throws DuplicateObjectException {
        var response = bookCategoryService.createBookCategory(request);
        return ResponseEntity.ok(new BookCategoryResponse(response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a book category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated book category"),
            @ApiResponse(responseCode = "400", description = "Invalid request body, validation failed",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book category not found",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<BookCategoryResponse> updateBookCategory(
            @PathVariable Long id,
            @Valid @RequestBody BookCategoryRequest request
    ) throws EntityNotFoundException, DuplicateObjectException {
        var response = bookCategoryService.updateBookCategory(id, request);
        return ResponseEntity.ok(new BookCategoryResponse(response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a book category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted book category"),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book category not found",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<Void> deleteBookCategory(@PathVariable Long id) throws EntityNotFoundException {
        bookCategoryService.deleteBookCategory(id);
        return ResponseEntity.ok().build();
    }
}
