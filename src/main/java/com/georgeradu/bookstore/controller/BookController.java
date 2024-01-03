package com.georgeradu.bookstore.controller;

import com.georgeradu.bookstore.dto.BookRequest;
import com.georgeradu.bookstore.dto.BookResponse;
import com.georgeradu.bookstore.dto.SpringErrorResponse;
import com.georgeradu.bookstore.exception.EntityNotFoundException;
import com.georgeradu.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/book")
@Validated
@Tag(name = "Book Controller", description = "Provides endpoints for books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved book"),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<BookResponse> getBook(@PathVariable Long id) throws EntityNotFoundException {
        var response = bookService.getBook(id);
        return ResponseEntity.ok(new BookResponse(response));
    }

    @GetMapping("/category/{id}")
    @Operation(summary = "Get all books by category id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved books"),
            @ApiResponse(responseCode = "404", description = "Book category not found",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<Page<BookResponse>> getBooksByCategoryId(
            @PathVariable Long id,
            @RequestParam Optional<Integer> page
    ) throws EntityNotFoundException {
        var response = bookService.getBooksByCategoryId(id, page);
        return ResponseEntity.ok(BookResponse.fromPage(response));
    }

    @GetMapping("/search")
    @Operation(summary = "Search books by name or author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved books"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<List<BookResponse>> searchBooks(
            @RequestParam Optional<String> searchQuery
    ) {
        var response = bookService.searchBooks(searchQuery);
        return ResponseEntity.ok(BookResponse.fromList(response));
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created book"),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book category not found",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<BookResponse> createBook(@RequestBody @Valid BookRequest request) throws
            EntityNotFoundException {
        var response = bookService.createBook(request);
        return ResponseEntity.ok(new BookResponse(response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated book"),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<BookResponse> updateBook(@PathVariable Long id, @RequestBody @Valid BookRequest request)
            throws EntityNotFoundException {
        var response = bookService.updateBook(id, request);
        return ResponseEntity.ok(new BookResponse(response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted book"),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SpringErrorResponse.class))),
    })
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) throws EntityNotFoundException {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }
}
