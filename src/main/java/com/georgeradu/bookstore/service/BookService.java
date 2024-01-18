package com.georgeradu.bookstore.service;

import com.georgeradu.bookstore.dto.BookRequest;
import com.georgeradu.bookstore.model.Book;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Book getBook(Long id);
    Book createBook(BookRequest request);
    Book updateBook(Long id, BookRequest request);
    void deleteBook(Long id);
    Page<Book> getBooksByCategoryId(Long id, Optional<Integer> page);
    List<Book> searchBooks(Optional<String> searchQueryContainingNameAndAuthorOfTheBook);
}
