package com.georgeradu.bookstore.services;

import com.georgeradu.bookstore.dto.ShoppingCartItemRequest;
import com.georgeradu.bookstore.exception.DuplicateObjectException;
import com.georgeradu.bookstore.exception.InvalidUserAccessException;
import com.georgeradu.bookstore.model.*;
import com.georgeradu.bookstore.repository.ShoppingCartItemRepository;
import com.georgeradu.bookstore.service.BookService;
import com.georgeradu.bookstore.service.ShoppingCartItemService;
import com.georgeradu.bookstore.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class ShoppingCartItemServiceTest {
    private final static LocalDateTime NOW = LocalDateTime.now();
    private User user1, user2;
    private Book book1, book2;
    private ShoppingCartItem shoppingCartItem1, shoppingCartItem2;

    @MockBean
    private Clock clock;
    private Clock fixedClock;
    @MockBean
    private ShoppingCartItemRepository shoppingCartItemRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private BookService bookService;
    @Autowired
    private ShoppingCartItemService shoppingCartItemService;

    @BeforeEach
    public void setUp() {
        fixedClock = Clock.fixed(NOW
                        .toLocalDate()
                        .atStartOfDay()
                        .toInstant(Clock.systemDefaultZone().getZone().getRules().getOffset(NOW)),
                Clock.systemDefaultZone().getZone());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();

        user1 = new User(1L, "firstNameValue", "lastNameValue", "emailValue", "passwordValue", UserRole.ROLE_USER,
                LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        user2 = new User(2L, "firstNameValue2", "lastNameValue2", "emailValue2", "passwordValue2", UserRole.ROLE_USER,
                LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        var bookCategory = new BookCategory(1L, "nameValue1", "descriptionValue1", LocalDateTime.now(clock),
                LocalDateTime.now(clock), null);
        book1 = new Book(1L, "titleValue1", "authorValue1", "descriptionValue1", 1, "imageUrl1", 1, "language1",
                "publisher1", LocalDateTime.now(clock), "isbn101", "isbn131", "dimensions1", 0, bookCategory,
                LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        book2 = new Book(2L, "titleValue2", "authorValue2", "descriptionValue2", 2, "imageUrl2", 2, "language2",
                "publisher2", LocalDateTime.now(clock), "isbn102", "isbn132", "dimensions2", 0, bookCategory,
                LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        shoppingCartItem1 = new ShoppingCartItem(1L, user1, book1, 1, LocalDateTime.now(clock), LocalDateTime.now(clock),
                null);
        shoppingCartItem2 = new ShoppingCartItem(2L, user1, book2, 2, LocalDateTime.now(clock), LocalDateTime.now(clock),
                null);
    }

    @AfterEach
    void tearDown() {}

    @Nested
    @DisplayName("Tests for getUserShoppingCart method")
    class TestGetUserShoppingCart {
        @Test
        @DisplayName("Should return user shopping cart items")
        void test_getUserShoppingCart_shouldReturnShoppingCartItems() {
            // Arrange
            var shoppingCartItems = List.of(shoppingCartItem1, shoppingCartItem2);

            // Act
            when(userService.getUser(user1.getId())).thenReturn(user1);
            when(shoppingCartItemRepository.findAllByUserId(user1.getId())).thenReturn(shoppingCartItems);

            // Assert
            var result = shoppingCartItemService.getUserShoppingCart(user1.getId());
            Assertions.assertEquals(shoppingCartItems, result);
        }
    }

    @Nested
    @DisplayName("Tests for getUserShoppingCartByEmail method")
    class TestGetUserShoppingCartByEmail {
        @Test
        @DisplayName("Should return user shopping cart items")
        void test_getUserShoppingCartByEmail_shouldReturnShoppingCartItems() {
            // Arrange
            var shoppingCartItems = List.of(shoppingCartItem1, shoppingCartItem2);

            // Act
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
            when(shoppingCartItemRepository.findAllByUserId(user1.getId())).thenReturn(shoppingCartItems);

            // Assert
            var result = shoppingCartItemService.getUserShoppingCartByEmail(user1.getEmail());
            Assertions.assertEquals(shoppingCartItems, result);
        }
    }

    @Nested
    @DisplayName("Tests for addBookToShoppingCart method")
    class TestAddBookToShoppingCart {
        @Test
        @DisplayName("Should add book to shopping cart")
        void test_addBookToShoppingCart_shouldAddBookToShoppingCart() {
            // Arrange
            var request = new ShoppingCartItemRequest(book1.getId(), 1);

            // Act
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
            when(bookService.getBook(book1.getId())).thenReturn(book1);
            when(shoppingCartItemRepository.findByUserIdAndBookId(user1.getId(), book1.getId())).thenReturn(
                    Optional.empty());
            when(shoppingCartItemRepository.save(any())).thenReturn(shoppingCartItem1);

            // Assert
            var result = shoppingCartItemService.addBookToShoppingCart(user1.getEmail(), request);
            Assertions.assertEquals(shoppingCartItem1, result);
        }

        @Test
        @DisplayName("Should throw DuplicateObjectException when user already has book in shopping cart")
        void test_addBookToShoppingCart_shouldThrowDuplicateObjectException() {
            // Arrange
            var request = new ShoppingCartItemRequest(book1.getId(), 1);

            // Act
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
            when(bookService.getBook(book1.getId())).thenReturn(book1);
            when(shoppingCartItemRepository.findByUserIdAndBookId(user1.getId(), book1.getId())).thenReturn(
                    Optional.of(shoppingCartItem1));

            // Assert
            Assertions.assertThrows(DuplicateObjectException.class,
                    () -> shoppingCartItemService.addBookToShoppingCart(user1.getEmail(), request));
        }
    }

    @Nested
    @DisplayName("Tests for updateShoppingCartItem method")
    class TestUpdateShoppingCartItem {
        @Test
        @DisplayName("Should update shopping cart item")
        void test_updateShoppingCartItem_shouldUpdateShoppingCartItem() {
            // Arrange
            var request = new ShoppingCartItemRequest(book1.getId(), 2);

            // Act
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
            when(shoppingCartItemRepository.findById(shoppingCartItem1.getId())).thenReturn(Optional.of(shoppingCartItem1));
            shoppingCartItem1.setQuantity(request.getQuantity());
            shoppingCartItem1.setUpdatedAt(LocalDateTime.now(clock));
            when(shoppingCartItemRepository.save(shoppingCartItem1)).thenReturn(shoppingCartItem1);

            // Assert
            var result = shoppingCartItemService.updateShoppingCartItem(user1.getEmail(), shoppingCartItem1.getId(), request);
            Assertions.assertEquals(shoppingCartItem1, result);
        }

        @Test
        @DisplayName("Should throw InvalidUserAccessException when user is not the owner of the shopping cart item")
        void test_updateShoppingCartItem_shouldThrowInvalidUserAccessException() {
            // Arrange
            var request = new ShoppingCartItemRequest(book1.getId(), 1);

            // Act
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user2);
            when(shoppingCartItemRepository.findById(shoppingCartItem1.getId())).thenReturn(Optional.of(shoppingCartItem1));

            // Assert
            Assertions.assertThrows(InvalidUserAccessException.class,
                    () -> shoppingCartItemService.updateShoppingCartItem(user1.getEmail(), shoppingCartItem1.getId(), request));
        }
    }

    @Nested
    @DisplayName("Tests for deleteShoppingCartItem method")
    class TestDeleteShoppingCartItem {
        @Test
        @DisplayName("Should delete shopping cart item")
        void test_deleteShoppingCartItem_shouldDeleteShoppingCartItem() {
            // Arrange

            // Act
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
            when(shoppingCartItemRepository.findById(shoppingCartItem1.getId())).thenReturn(Optional.of(shoppingCartItem1));

            // Assert
            Assertions.assertDoesNotThrow(() -> shoppingCartItemService.deleteShoppingCartItem(user1.getEmail(), shoppingCartItem1.getId()));
        }

        @Test
        @DisplayName("Should throw InvalidUserAccessException when user is not the owner of the shopping cart item")
        void test_deleteShoppingCartItem_shouldThrowInvalidUserAccessException() {
            // Arrange

            // Act
            when(userService.getUserByEmail(user2.getEmail())).thenReturn(user2);
            when(shoppingCartItemRepository.findById(shoppingCartItem1.getId())).thenReturn(Optional.of(shoppingCartItem1));

            // Assert
            Assertions.assertThrows(InvalidUserAccessException.class,
                    () -> shoppingCartItemService.deleteShoppingCartItem(user2.getEmail(), shoppingCartItem1.getId()));
        }
    }

    @Nested
    @DisplayName("Tests for deleteAllUserShoppingCartItems method")
    class TestDeleteAllUserShoppingCartItems {
        @Test
        @DisplayName("Should delete all user shopping cart items")
        void test_deleteAllUserShoppingCartItems_shouldDeleteAllUserShoppingCartItems() {
            // Arrange

            // Act
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);

            // Assert
            Assertions.assertDoesNotThrow(() -> shoppingCartItemService.deleteAllUserShoppingCartItems(user1.getEmail()));
        }
    }
}
