package com.georgeradu.bookstore.services;


import com.georgeradu.bookstore.dto.ReviewRequest;
import com.georgeradu.bookstore.exception.DuplicateObjectException;
import com.georgeradu.bookstore.exception.EntityNotFoundException;
import com.georgeradu.bookstore.exception.InvalidUserAccessException;
import com.georgeradu.bookstore.model.*;
import com.georgeradu.bookstore.repository.ReviewRepository;
import com.georgeradu.bookstore.service.BookService;
import com.georgeradu.bookstore.service.ReviewService;
import com.georgeradu.bookstore.service.UserService;
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
public class ReviewServiceTest {
    private final static LocalDateTime NOW = LocalDateTime.now();
    private BookCategory bookCategory;
    private Book book1, book2;
    private User user1, user2;
    private Review review1, review2;

    @MockBean
    private Clock clock;
    private Clock fixedClock;
    @MockBean
    private ReviewRepository reviewRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private BookService bookService;
    @Autowired
    private ReviewService reviewService;

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
        user1 = new User(1L, "firstNameValue", "lastNameValue", "emailValue", "passwordValue", UserRole.ROLE_USER,
                LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        user2 = new User(2L, "firstNameValue", "lastNameValue", "emailValue", "passwordValue", UserRole.ROLE_USER,
                LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        review1 = new Review(1L, book1, user1, 1, "reviewValue", LocalDateTime.now(clock), LocalDateTime.now(clock),
                null);
        review2 = new Review(2L, book2, user2, 2, "reviewValue", LocalDateTime.now(clock), LocalDateTime.now(clock),
                null);
    }

    @AfterEach
    void tearDown() {}

    @Nested
    @DisplayName("Tests for getReview method")
    class TestGetReview {
        @Test
        @DisplayName("Test should return review when review exists")
        void test_getReview_whenReviewExists() {
            // Arrange

            // Act
            when(reviewRepository.findById(review1.getId())).thenReturn(Optional.of(review1));

            // Assert
            Assertions.assertEquals(review1, reviewService.getReview(review1.getId()));
        }

        @Test
        @DisplayName("Test should throw EntityNotFoundException when review does not exist")
        void test_getReview_whenReviewDoesNotExist() {
            // Arrange

            // Act
            when(reviewRepository.findById(review1.getId())).thenThrow(EntityNotFoundException.class);

            // Assert
            Assertions.assertThrows(EntityNotFoundException.class, () -> reviewService.getReview(review1.getId()));
        }
    }

    @Nested
    @DisplayName("Tests for getReviewsForUser method")
    class TestGetReviewsForBook {
        @Test
        @DisplayName("Test should return reviews for book when book exists")
        void test_getReviewsForBook_whenBookExists() {
            // Arrange

            // Act
            when(bookService.getBook(book1.getId())).thenReturn(book1);
            when(reviewRepository.findAllByBookId(book1.getId())).thenReturn(List.of(review1, review2));

            // Assert
            Assertions.assertEquals(List.of(review1, review2), reviewService.getReviewsForBook(book1.getId()));
        }
    }

    @Nested
    @DisplayName("Tests for getReviewsForUser method")
    class TestGetReviewsForUser {
        @Test
        @DisplayName("Test should return reviews for user when user exists")
        void test_getReviewsForUser_whenUserExists() {
            // Arrange

            // Act
            when(userService.getUser(user1.getId())).thenReturn(user1);
            when(reviewRepository.findAllByUserId(user1.getId())).thenReturn(List.of(review1, review2));

            // Assert
            Assertions.assertEquals(List.of(review1, review2), reviewService.getReviewsForUser(user1.getId()));
        }
    }

    @Nested
    @DisplayName("Tests for createReview method")
    class TestCreateReview {
        @Test
        @DisplayName("Test should return review when review is created")
        void test_createReview_whenReviewIsCreated() {
            // Arrange
            var request = new ReviewRequest(book1.getId(), 1, "reviewValue");

            // Act
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
            when(bookService.getBook(book1.getId())).thenReturn(book1);
            when(reviewRepository.findByBookIdAndUserId(book1.getId(), user1.getId())).thenReturn(Optional.empty());
            when(reviewRepository.save(any())).thenReturn(review1);

            // Assert
            Assertions.assertEquals(review1, reviewService.createReview(request, user1.getEmail()));
        }

        @Test
        @DisplayName("Test should throw DuplicateObjectException when review already exists")
        void test_createReview_whenReviewAlreadyExists() {
            // Arrange
            var request = new ReviewRequest(book1.getId(), 1, "reviewValue");

            // Act
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
            when(bookService.getBook(book1.getId())).thenReturn(book1);
            when(reviewRepository.findByBookIdAndUserId(book1.getId(), user1.getId())).thenReturn(Optional.of(review1));

            // Assert
            Assertions.assertThrows(DuplicateObjectException.class, () -> reviewService.createReview(request, user1.getEmail()));
        }
    }

    @Nested
    @DisplayName("Tests for updateReview method")
    class TestUpdateReview {
        @Test
        @DisplayName("Test should return review when review is updated")
        void test_updateReview_whenReviewIsUpdated() {
            // Arrange
            var request = new ReviewRequest(book1.getId(), 1, "reviewValue");

            // Act
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
            when(reviewRepository.findById(review1.getId())).thenReturn(Optional.of(review1));
            when(reviewRepository.save(any())).thenReturn(review1);

            // Assert
            Assertions.assertEquals(review1, reviewService.updateReview(review1.getId(), request, user1.getEmail()));
        }

        @Test
        @DisplayName("Test should throw InvalidUserAccessException when user is not the owner of the review")
        void test_updateReview_whenUserIsNotTheOwnerOfTheReview() {
            // Arrange
            var request = new ReviewRequest(book1.getId(), 1, "reviewValue");

            // Act
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
            when(reviewRepository.findById(review2.getId())).thenReturn(Optional.of(review2));

            // Assert
            Assertions.assertThrows(
                    InvalidUserAccessException.class, () -> reviewService.updateReview(review2.getId(), request, user1.getEmail()));
        }
    }

    @Nested
    @DisplayName("Tests for deleteReview method")
    class TestDeleteReview {
        @Test
        @DisplayName("Test should delete review when review exists")
        void test_deleteReview_whenReviewExists() {
            // Arrange

            // Act
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
            when(reviewRepository.findById(review1.getId())).thenReturn(Optional.of(review1));

            // Assert
            Assertions.assertDoesNotThrow(() -> reviewService.deleteReview(review1.getId(), user1.getEmail()));
        }

        @Test
        @DisplayName("Test should throw InvalidUserAccessException when user is not the owner of the review")
        void test_deleteReview_whenUserIsNotTheOwnerOfTheReview() {
            // Arrange

            // Act
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
            when(reviewRepository.findById(review2.getId())).thenReturn(Optional.of(review2));

            // Assert
            Assertions.assertThrows(
                    InvalidUserAccessException.class, () -> reviewService.deleteReview(review2.getId(), user1.getEmail()));
        }
    }
}
