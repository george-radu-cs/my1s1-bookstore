package com.georgeradu.bookstore.services;

import com.georgeradu.bookstore.dto.BookRequest;
import com.georgeradu.bookstore.exception.EntityNotFoundException;
import com.georgeradu.bookstore.model.Book;
import com.georgeradu.bookstore.model.BookCategory;
import com.georgeradu.bookstore.repository.BookRepository;
import com.georgeradu.bookstore.service.BookCategoryService;
import com.georgeradu.bookstore.service.BookService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookServiceTest {
    private final static LocalDateTime NOW = LocalDateTime.now();
    private BookCategory bookCategory;
    private Book book1, book2;

    @MockBean
    private Clock clock;
    private Clock fixedClock;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private BookCategoryService bookCategoryService;
    @Autowired
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        fixedClock = Clock.fixed(NOW
                        .toLocalDate()
                        .atStartOfDay()
                        .toInstant(Clock.systemDefaultZone().getZone().getRules().getOffset(NOW)),
                Clock.systemDefaultZone().getZone());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();

        bookCategory = new BookCategory(1L, "nameValue1", "descriptionValue1",
                LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        book1 = new Book(1L, "titleValue1", "authorValue1", "descriptionValue1", 1, "imageUrl1",
                1, "language1", "publisher1", LocalDateTime.now(clock), "isbn101", "isbn131", "dimensions1", 0,
                bookCategory, LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        book2 = new Book(2L, "titleValue2", "authorValue2", "descriptionValue2", 2, "imageUrl2",
                2, "language2", "publisher2", LocalDateTime.now(clock), "isbn102", "isbn132", "dimensions2", 0,
                bookCategory, LocalDateTime.now(clock), LocalDateTime.now(clock), null);
    }

    @AfterEach
    void tearDown() {}

    @Nested
    @DisplayName("Test getBook method")
    class TestGetBook {
        @Test
        @DisplayName("Should get book")
        void test_getBook_shouldGetBook() {
            // Arrange
            var bookId = 1L;

            // Act
            when(bookRepository.findById(bookId)).thenReturn(Optional.of(book1));

            // Assert
            Book response = bookService.getBook(bookId);
            Assertions.assertEquals(book1, response);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when book is not found")
        void test_getBook_shouldThrowEntityNotFoundExceptionWhenBookIsNotFound() {
            // Arrange
            var bookId = 1L;

            // Act
            when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

            // Assert
            Assertions.assertThrows(EntityNotFoundException.class, () -> bookService.getBook(bookId));
        }
    }

    @Nested
    @DisplayName("Test createBook method")
    class TestCreateBook {
        @Test
        @DisplayName("Should create book")
        void test_createBook_shouldCreateBook() throws EntityNotFoundException {
            // Arrange
            var bookRequest = new BookRequest("titleValue1", "authorValue1", "descriptionValue1", 1, "imageUrl1",
                    1, "language1", "publisher1", LocalDateTime.now(clock), "isbn101", "isbn131", "dimensions1", 1L);

            // Act
            when(bookRepository.save(any())).thenReturn(book1);
            when(bookCategoryService.getBookCategory(bookRequest.getBookCategoryId())).thenReturn(bookCategory);

            // Assert
            Book response = bookService.createBook(bookRequest);
            Assertions.assertEquals(book1, response);
        }
    }

    @Nested
    @DisplayName("Test updateBook method")
    class TestUpdateBook {
        @Test
        @DisplayName("Should update book")
        void test_updateBook_shouldUpdateBook() throws EntityNotFoundException {
            // Arrange
            var bookId = 1L;
            var bookRequest = new BookRequest("titleValue1", "authorValue1", "descriptionValue1", 1, "imageUrl1",
                    1, "language1", "publisher1", LocalDateTime.now(clock), "isbn101", "isbn131", "dimensions1", 1L);

            // Act
            when(bookRepository.findById(bookId)).thenReturn(Optional.of(book1));
            when(bookRepository.save(any())).thenReturn(book1);
            when(bookCategoryService.getBookCategory(bookRequest.getBookCategoryId())).thenReturn(bookCategory);

            // Assert
            Book response = bookService.updateBook(bookId, bookRequest);
            Assertions.assertEquals(book1, response);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when book is not found")
        void test_updateBook_shouldThrowEntityNotFoundExceptionWhenBookIsNotFound() {
            // Arrange
            var bookId = 1L;
            var bookRequest = new BookRequest("titleValue1", "authorValue1", "descriptionValue1", 1, "imageUrl1",
                    1, "language1", "publisher1", LocalDateTime.now(clock), "isbn101", "isbn131", "dimensions1", 1L);

            // Act
            when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

            // Assert
            Assertions.assertThrows(EntityNotFoundException.class, () -> bookService.updateBook(bookId, bookRequest));
        }
    }

    @Nested
    @DisplayName("Test deleteBook method")
    class TestDeleteBook {
        @Test
        @DisplayName("Should delete book")
        void test_deleteBook_shouldDeleteBook() throws EntityNotFoundException {
            // Arrange
            var bookId = 1L;

            // Act
            when(bookRepository.findById(bookId)).thenReturn(Optional.of(book1));

            // Assert
            bookService.deleteBook(bookId);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when book is not found")
        void test_deleteBook_shouldThrowEntityNotFoundExceptionWhenBookIsNotFound() {
            // Arrange
            var bookId = 1L;

            // Act
            when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

            // Assert
            Assertions.assertThrows(EntityNotFoundException.class, () -> bookService.deleteBook(bookId));
        }
    }

    @Nested
    @DisplayName("Test searchBooks method")
    class TestSearchBookMethod {
        @Test
        @DisplayName("Should return books on search")
        void test_searchBooks_shouldReturnBooksOnSearch() {
            // Arrange
            var searchString = book1.getTitle() + " " + book2.getAuthor();

            // Act
            when(bookRepository.findAllByTitleIsInOrAuthorIsIn(any(), any())).thenReturn(List.of(book1, book2));

            // Assert
            List<Book> response = bookService.searchBooks(Optional.of(searchString));
            Assertions.assertEquals(List.of(book1, book2), response);
        }
    }
}
