package com.georgeradu.bookstore.service;

import com.georgeradu.bookstore.dto.BookCategoryRequest;
import com.georgeradu.bookstore.model.BookCategory;

import java.util.List;

public interface BookCategoryService {
    BookCategory getBookCategory(Long id);
    List<BookCategory> getBookCategories();
    BookCategory createBookCategory(BookCategoryRequest request);
    BookCategory updateBookCategory(Long id, BookCategoryRequest request);
    void deleteBookCategory(Long id);
}
