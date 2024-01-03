package com.georgeradu.bookstore.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book-category")
@Tag(name = "Book Category Controller", description = "Provides endpoints for book categories")
public class BookCategoryController {
}
