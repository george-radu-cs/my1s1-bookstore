package com.georgeradu.bookstore.services;


import com.georgeradu.bookstore.dto.BookCategoryRequest;
import com.georgeradu.bookstore.exception.DuplicateObjectException;
import com.georgeradu.bookstore.exception.EntityNotFoundException;
import com.georgeradu.bookstore.model.BookCategory;
import com.georgeradu.bookstore.repository.BookCategoryRepository;
import com.georgeradu.bookstore.service.BookCategoryService;
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
public class BookCategoryServiceTest {
    private final static LocalDateTime NOW = LocalDateTime.now();

    @MockBean
    private Clock clock;
    private Clock fixedClock;
    @MockBean
    private BookCategoryRepository bookCategoryRepository;
    @Autowired
    private BookCategoryService bookCategoryService;

    @BeforeEach
    public void setUp() {
        fixedClock = Clock.fixed(NOW.toLocalDate().atStartOfDay().toInstant(Clock.systemDefaultZone().getZone().getRules().getOffset(NOW)), Clock.systemDefaultZone().getZone());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
    }

    @AfterEach
    void tearDown() {}

    @Nested
    @DisplayName("Test getBookCategory method")
    class TestGetBookCategory {
        @Test
        @DisplayName("Should return book category")
        void test_getBookCategory_shouldReturnBookCategory() {
            // Arrange
            var bookCategory = new BookCategory(1L, "nameValue", "descriptionValue", LocalDateTime.now(clock),
                    LocalDateTime.now(clock), null);

            // Act
            when(bookCategoryRepository.findById(1L)).thenReturn(Optional.of(bookCategory));

            // Assert
            BookCategory response = bookCategoryService.getBookCategory(1L);
            Assertions.assertEquals(bookCategory, response);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException")
        void test_getBookCategory_shouldThrowEntityNotFoundException() {
            // Arrange

            // Act
            when(bookCategoryRepository.findById(1L)).thenReturn(Optional.empty());

            // Assert
            Assertions.assertThrows(EntityNotFoundException.class, () -> bookCategoryService.getBookCategory(1L));
        }
    }

    @Nested
    @DisplayName("Test getBookCategories method")
    class TestGetBookCategories {
        @Test
        @DisplayName("Should return book categories")
        void test_getBookCategories_shouldReturnBookCategories() {
            // Arrange
            var bookCategory1 = new BookCategory(1L, "nameValue1", "descriptionValue1", LocalDateTime.now(clock),
                    LocalDateTime.now(clock), null);
            var bookCategory2 = new BookCategory(2L, "nameValue2", "descriptionValue2", LocalDateTime.now(clock),
                    LocalDateTime.now(clock), null);

            // Act
            when(bookCategoryRepository.findAll()).thenReturn(List.of(bookCategory1, bookCategory2));

            // Assert
            List<BookCategory> response = bookCategoryService.getBookCategories();
            Assertions.assertEquals(List.of(bookCategory1, bookCategory2), response);
        }
    }

    @Nested
    @DisplayName("Test createBookCategory method")
    class TestCreateBookCategory {
        @Test
        @DisplayName("Should create book category")
        void test_createBookCategory_shouldReturnBookCategory() {
            // Arrange
            var bookCategoryRequest = new BookCategoryRequest("nameValue", "descriptionValue");
            var bookCategory = new BookCategory(1L, "nameValue", "descriptionValue", LocalDateTime.now(clock),
                    LocalDateTime.now(clock), null);

            // Act
            when(bookCategoryRepository.findByName(bookCategoryRequest.getName())).thenReturn(Optional.empty());
            when(bookCategoryRepository.save(any())).thenReturn(bookCategory);

            // Assert
            BookCategory response = bookCategoryService.createBookCategory(bookCategoryRequest);
            Assertions.assertEquals(bookCategory, response);
        }

        @Test
        @DisplayName("Should throw DuplicateObjectException")
        void test_createBookCategory_shouldThrowDuplicateObjectException() {
            // Arrange
            var bookCategoryRequest = new BookCategoryRequest("nameValue", "descriptionValue");
            var bookCategory = new BookCategory(1L, "nameValue", "descriptionValue", LocalDateTime.now(clock),
                    LocalDateTime.now(clock), null);

            // Act
            when(bookCategoryRepository.findByName(bookCategoryRequest.getName())).thenReturn(Optional.of(bookCategory));

            // Assert
            Assertions.assertThrows(DuplicateObjectException.class, () -> bookCategoryService.createBookCategory(bookCategoryRequest));
        }
    }

    @Nested
    @DisplayName("Test updateBookCategory method")
    class TestUpdateBookCategory {
        @Test
        @DisplayName("Should update book category")
        void test_updateBookCategory_shouldReturnBookCategory() {
            // Arrange
            var bookCategoryRequest = new BookCategoryRequest("newNameValue", "newDescriptionValue");
            var bookCategory = new BookCategory(1L, "nameValue", "descriptionValue", LocalDateTime.now(clock),
                    LocalDateTime.now(clock), null);
            var updatedBookCategory = new BookCategory(1L, "newNameValue", "newDescriptionValue", LocalDateTime.now(clock),
                    LocalDateTime.now(clock), null);

            // Act
            when(bookCategoryRepository.findById(1L)).thenReturn(Optional.of(bookCategory));
            when(bookCategoryRepository.findByName(bookCategoryRequest.getName())).thenReturn(Optional.empty());
            when(bookCategoryRepository.save(any())).thenReturn(updatedBookCategory);

            // Assert
            BookCategory response = bookCategoryService.updateBookCategory(1L, bookCategoryRequest);
            Assertions.assertEquals(updatedBookCategory, response);
        }

        @Test
        @DisplayName("Should update even if name wasn't changed")
        void test_updateBookCategory_shouldUpdateEvenIfNameWasntChanged() {
            // Arrange
            var bookCategoryRequest = new BookCategoryRequest("nameValue", "newDescriptionValue");
            var bookCategory = new BookCategory(1L, "nameValue", "descriptionValue", LocalDateTime.now(clock),
                    LocalDateTime.now(clock), null);
            var updatedBookCategory = new BookCategory(1L, "nameValue", "newDescriptionValue", LocalDateTime.now(clock),
                    LocalDateTime.now(clock), null);

            // Act
            when(bookCategoryRepository.findById(1L)).thenReturn(Optional.of(bookCategory));
            when(bookCategoryRepository.findByName(bookCategoryRequest.getName())).thenReturn(Optional.of(bookCategory));
            when(bookCategoryRepository.save(any())).thenReturn(updatedBookCategory);

            // Assert
            BookCategory response = bookCategoryService.updateBookCategory(1L, bookCategoryRequest);
            Assertions.assertEquals(updatedBookCategory, response);
        }

        @Test
        @DisplayName("Should throw DuplicateObjectException if book category with same name exists")
        void test_updateBookCategory_shouldThrowDuplicateObjectExceptionIfBookCategoryWithSameNameExists() {
            // Arrange
            var bookCategoryRequest = new BookCategoryRequest("newNameValue", "newDescriptionValue");
            var bookCategory = new BookCategory(1L, "nameValue", "descriptionValue", LocalDateTime.now(clock),
                    LocalDateTime.now(clock), null);
            var existingBookCategory = new BookCategory(2L, "newNameValue", "newDescriptionValue", LocalDateTime.now(clock),
                    LocalDateTime.now(clock), null);

            // Act
            when(bookCategoryRepository.findById(1L)).thenReturn(Optional.of(bookCategory));
            when(bookCategoryRepository.findByName(bookCategoryRequest.getName())).thenReturn(Optional.of(existingBookCategory));

            // Assert
            Assertions.assertThrows(DuplicateObjectException.class, () -> bookCategoryService.updateBookCategory(1L, bookCategoryRequest));
        }
    }

    @Nested
    @DisplayName("Test deleteBookCategory method")
    class TestDeleteBookCategory {
        @Test
        @DisplayName("Should delete book category")
        void test_deleteBookCategory_shouldDeleteBookCategory() {
            // Arrange
            var bookCategory = new BookCategory(1L, "nameValue", "descriptionValue", LocalDateTime.now(clock),
                    LocalDateTime.now(clock), null);

            // Act
            when(bookCategoryRepository.findById(1L)).thenReturn(Optional.of(bookCategory));

            // Assert
            Assertions.assertDoesNotThrow(() -> bookCategoryService.deleteBookCategory(1L));
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException")
        void test_deleteBookCategory_shouldThrowEntityNotFoundException() {
            // Arrange

            // Act
            when(bookCategoryRepository.findById(1L)).thenReturn(Optional.empty());

            // Assert
            Assertions.assertThrows(EntityNotFoundException.class, () -> bookCategoryService.deleteBookCategory(1L));
        }
    }
}
