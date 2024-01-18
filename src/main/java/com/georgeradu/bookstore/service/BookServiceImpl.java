package com.georgeradu.bookstore.service;

import com.georgeradu.bookstore.dto.BookRequest;
import com.georgeradu.bookstore.exception.EntityNotFoundException;
import com.georgeradu.bookstore.model.Book;
import com.georgeradu.bookstore.model.BookCategory;
import com.georgeradu.bookstore.repository.BookRepository;
import com.georgeradu.bookstore.utils.StringCombinationGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final Clock clock;
    private final BookRepository bookRepository;
    private final BookCategoryService bookCategoryService;

    public BookServiceImpl(Clock clock, BookRepository bookRepository, BookCategoryService bookCategoryService) {
        this.clock = clock;
        this.bookRepository = bookRepository;
        this.bookCategoryService = bookCategoryService;
    }

    public Book getBook(Long id) throws EntityNotFoundException {
        return bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book with id " + id));
    }

    public Book createBook(BookRequest request) throws EntityNotFoundException {
        var bookCategory = bookCategoryService.getBookCategory(request.getBookCategoryId());

        var book = new Book();
        createNewBook(request, bookCategory, book);
        var timestamp = LocalDateTime.now(clock);
        book.setCreatedAt(timestamp);
        book.setUpdatedAt(timestamp);

        return bookRepository.save(book);
    }


    public Book updateBook(Long id, BookRequest request) {
        var book = getBook(id);
        var bookCategory = bookCategoryService.getBookCategory(request.getBookCategoryId());

        createNewBook(request, bookCategory, book);
        book.setUpdatedAt(LocalDateTime.now(clock));

        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        var book = getBook(id);
        bookRepository.delete(book);
    }

    public Page<Book> getBooksByCategoryId(Long id, Optional<Integer> page) throws EntityNotFoundException {
        var bookCategory = bookCategoryService.getBookCategory(id);
        return bookRepository.findAllByCategory(bookCategory,
                PageRequest.of(page.orElse(0), 20, Sort.Direction.ASC, "id"));
    }

    public List<Book> searchBooks(Optional<String> searchQueryContainingNameAndAuthorOfTheBook) {
        List<String> wordsCombinations = StringCombinationGenerator.generateWordsCombinations(
                searchQueryContainingNameAndAuthorOfTheBook.orElse(""));
        return bookRepository.findAllByTitleIsInOrAuthorIsIn(wordsCombinations, wordsCombinations);
    }

    private void createNewBook(BookRequest request, BookCategory bookCategory, Book book) {
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setDescription(request.getDescription());
        book.setPrice(request.getPrice());
        book.setImageUrl(request.getImageUrl());
        book.setQuantity(request.getQuantity());
        book.setLanguage(request.getLanguage());
        book.setPublisher(request.getPublisher());
        book.setPublicationDate(request.getPublicationDate());
        book.setIsbn10(request.getIsbn10());
        book.setIsbn13(request.getIsbn13());
        book.setDimensions(request.getDimensions());
        book.setCategory(bookCategory);
    }
}
