package com.georgeradu.bookstore.services;

import com.georgeradu.bookstore.exception.EntityNotFoundException;
import com.georgeradu.bookstore.exception.IllegalEntityStateException;
import com.georgeradu.bookstore.exception.InvalidUserAccessException;
import com.georgeradu.bookstore.model.*;
import com.georgeradu.bookstore.repository.OrderInfoRepository;
import com.georgeradu.bookstore.repository.OrderItemRepository;
import com.georgeradu.bookstore.service.BookService;
import com.georgeradu.bookstore.service.OrderService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceTest {
    private final static LocalDateTime NOW = LocalDateTime.now();
    private User user1, user2;
    private Book book1, book2;
    private OrderInfo orderInfo, orderInfo2;
    private OrderItem orderItem1, orderItem2;

    @MockBean
    private Clock clock;
    private Clock fixedClock;
    @MockBean
    private ShoppingCartItemService shoppingCartItemService;
    @MockBean
    private UserService userService;
    @MockBean
    private BookService bookService;
    @MockBean
    private OrderInfoRepository orderInfoRepository;
    @MockBean
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderService orderService;

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
        user2 = new User(2L, "firstNameValue", "lastNameValue", "emailValue", "passwordValue", UserRole.ROLE_USER,
                LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        var bookCategory = new BookCategory(1L, "nameValue1", "descriptionValue1", LocalDateTime.now(clock),
                LocalDateTime.now(clock), null);
        book1 = new Book(1L, "titleValue1", "authorValue1", "descriptionValue1", 1, "imageUrl1", 1, "language1",
                "publisher1", LocalDateTime.now(clock), "isbn101", "isbn131", "dimensions1", 0, bookCategory,
                LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        book2 = new Book(2L, "titleValue2", "authorValue2", "descriptionValue2", 2, "imageUrl2", 2, "language2",
                "publisher2", LocalDateTime.now(clock), "isbn102", "isbn132", "dimensions2", 0, bookCategory,
                LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        orderInfo = new OrderInfo(1L, user1, 100.0, "shippingAddressValue", OrderStatus.PENDING, null,
                LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        orderInfo2 = new OrderInfo(2L, user1, 10.0, "shippingAddressValue", OrderStatus.DELIVERED,
                LocalDateTime.now(clock), LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        orderItem1 = new OrderItem(1L, orderInfo, book1, 1, 100.0, LocalDateTime.now(clock), LocalDateTime.now(clock),
                null);
        orderItem2 = new OrderItem(2L, orderInfo, book2, 2, 200.0, LocalDateTime.now(clock), LocalDateTime.now(clock),
                null);
    }

    @AfterEach
    void tearDown() {}

    @Nested
    @DisplayName("Tests for getOrderInfoById method")
    class TestGetOrderInfoById {
        @Test
        @DisplayName("Should return order info when order info exists")
        void test_getOrderInfoById_shouldReturnWhenOrderInfoExists() {
            // Arrange

            // Act
            when(orderInfoRepository.findById(orderInfo.getId())).thenReturn(Optional.of(orderInfo));

            // Assert
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when order info does not exist")
        void test_getOrderInfoById_shouldThrowWhenOrderInfoDoesNotExists() {
            // Arrange

            // Act
            when(orderInfoRepository.findById(orderInfo.getId())).thenThrow(EntityNotFoundException.class);

            // Assert
            Assertions.assertThrows(EntityNotFoundException.class,
                    () -> orderService.getOrderInfoById(orderInfo.getId()));
        }
    }

    @Nested
    @DisplayName("Tests for getUserOrderInfoById method")
    class TestGetUserOrderInfoById {
        @Test
        @DisplayName("Should return order info when order info exists and user is owner")
        void test_getUserOrderInfoById_whenOrderInfoExistsAndUserIsOwner() {
            // Arrange

            // Act
            when(orderInfoRepository.findById(orderInfo.getId())).thenReturn(Optional.of(orderInfo));
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);

            // Assert
            Assertions.assertEquals(orderInfo, orderService.getUserOrderInfoById(user1.getEmail(), orderInfo.getId()));
        }

        @Test
        @DisplayName("Should throw InvalidUserAccessException when user is not owner")
        void test_getUserOrderInfoById_whenUserIsNotOwner() {
            // Arrange

            // Act
            when(orderInfoRepository.findById(orderInfo.getId())).thenReturn(Optional.of(orderInfo));
            when(userService.getUserByEmail(user2.getEmail())).thenReturn(user2);

            // Assert
            Assertions.assertThrows(InvalidUserAccessException.class,
                    () -> orderService.getUserOrderInfoById(user2.getEmail(), orderInfo.getId()));
        }
    }

    @Nested
    @DisplayName("Tests for getOrderItemsByOrderInfoId method")
    class TestGetOrderItemsByOrderInfoId {
        @Test
        @DisplayName("Should return order items when order info exists")
        void test_getOrderItemsByOrderInfoId_whenOrderInfoExists() {
            // Arrange
            var orderItems = List.of(orderItem1, orderItem2);

            // Act
            when(orderInfoRepository.findById(orderInfo.getId())).thenReturn(Optional.of(orderInfo));
            when(orderItemRepository.findAllByOrderInfoId(orderInfo.getId())).thenReturn(orderItems);

            // Assert
            Assertions.assertEquals(orderItems, orderService.getOrderItemsByOrderInfoId(orderInfo.getId()));
        }

        @Test
        @DisplayName("Should return empty list when order info exists but has no order items")
        void test_getOrderItemsByOrderInfoId_whenOrderInfoExistsButHasNoOrderItems() {
            // Arrange
            var orderItems = List.of();

            // Act
            when(orderInfoRepository.findById(orderInfo.getId())).thenReturn(Optional.of(orderInfo));
            when(orderItemRepository.findAllByOrderInfoId(orderInfo.getId())).thenReturn(List.of());

            // Assert
            Assertions.assertEquals(orderItems, orderService.getOrderItemsByOrderInfoId(orderInfo.getId()));
        }
    }

    @Nested
    @DisplayName("Tests for getUserOrderItemsByOrderInfoId method")
    class TestGetUserOrderItemsByOrderInfoId {
        @Test
        @DisplayName("Should return order items when order info exists and user is owner")
        void test_getUserOrderItemsByOrderInfoId_whenOrderInfoExistsAndUserIsOwner() {
            // Arrange
            var orderItems = List.of(orderItem1, orderItem2);

            // Act
            when(orderInfoRepository.findById(orderInfo.getId())).thenReturn(Optional.of(orderInfo));
            when(orderItemRepository.findAllByOrderInfoId(orderInfo.getId())).thenReturn(orderItems);
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);

            // Assert
            Assertions.assertEquals(orderItems,
                    orderService.getUserOrderItemsByOrderInfoId(user1.getEmail(), orderInfo.getId()));
        }

        @Test
        @DisplayName("Should throw InvalidUserAccessException when user is not owner")
        void test_getUserOrderItemsByOrderInfoId_whenUserIsNotOwner() {
            // Arrange
            var orderItems = List.of(orderItem1, orderItem2);

            // Act
            when(orderInfoRepository.findById(orderInfo.getId())).thenReturn(Optional.of(orderInfo));
            when(orderItemRepository.findAllByOrderInfoId(orderInfo.getId())).thenReturn(orderItems);
            when(userService.getUserByEmail(user2.getEmail())).thenReturn(user2);

            // Assert
            Assertions.assertThrows(InvalidUserAccessException.class,
                    () -> orderService.getUserOrderItemsByOrderInfoId(user2.getEmail(), orderInfo.getId()));
        }
    }

    @Nested
    @DisplayName("Tests for getUserOrderHistory method")
    class TestGetUserOrderHistory {
        @Test
        @DisplayName("Should return order info list when user has order history")
        void test_getUserOrderHistory_whenUserHasOrderHistory() {
            // Arrange
            var orderInfoList = List.of(orderInfo, orderInfo2);

            // Act
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
            when(orderInfoRepository.findAllByUserId(user1.getId())).thenReturn(orderInfoList);

            // Assert
            Assertions.assertEquals(orderInfoList, orderService.getUserOrderHistory(user1.getEmail()));
        }

        @Test
        @DisplayName("Should return empty list when user has no order history")
        void test_getUserOrderHistory_whenUserHasNoOrderHistory() {
            // Arrange
            var orderInfoList = List.of();

            // Act
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
            when(orderInfoRepository.findAllByUserId(user1.getId())).thenReturn(List.of());

            // Assert
            Assertions.assertEquals(orderInfoList, orderService.getUserOrderHistory(user1.getEmail()));
        }
    }

    @Nested
    @DisplayName("Tests for getOrderHistoryForUserById method")
    class TestGetOrderHistoryForUserById {
        @Test
        @DisplayName("Should return order info list when user has order history")
        void test_getOrderHistoryForUserById_whenUserHasOrderHistory() {
            // Arrange
            var orderInfoList = List.of(orderInfo, orderInfo2);

            // Act
            when(userService.getUser(user1.getId())).thenReturn(user1);
            when(orderInfoRepository.findAllByUserId(user1.getId())).thenReturn(orderInfoList);

            // Assert
            Assertions.assertEquals(orderInfoList, orderService.getOrderHistoryForUserById(user1.getId()));
        }

        @Test
        @DisplayName("Should return empty list when user has no order history")
        void test_getOrderHistoryForUserById_whenUserHasNoOrderHistory() {
            // Arrange
            var orderInfoList = List.of();

            // Act
            when(userService.getUser(user1.getId())).thenReturn(user1);
            when(orderInfoRepository.findAllByUserId(user1.getId())).thenReturn(List.of());

            // Assert
            Assertions.assertEquals(orderInfoList, orderService.getOrderHistoryForUserById(user1.getId()));
        }
    }

    @Nested
    @DisplayName("Tests for saveUserShoppingCartAsOrder method")
    class TestSaveUserShoppingCartAsOrder {
        @Test
        @DisplayName("Should save order info when shopping cart is not empty")
        void test_saveUserShoppingCartAsOrder_whenShoppingCartIsNotEmpty() {
            // Arrange
            var shoppingCartItems = List.of(new ShoppingCartItem(1L, user1, book1, 1, LocalDateTime.now(clock),
                    LocalDateTime.now(clock), null));

            // Act
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
            when(shoppingCartItemService.getUserShoppingCartByEmail(user1.getEmail())).thenReturn(shoppingCartItems);
            when(orderInfoRepository.save(any())).thenReturn(orderInfo);

            // Assert
            Assertions.assertEquals(orderInfo, orderService.saveUserShoppingCartAsOrder(user1.getEmail(), "shippingAddressValue"));
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when shopping cart is empty")
        void test_saveUserShoppingCartAsOrder_whenShoppingCartIsEmpty() {
            // Arrange

            // Act
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
            when(shoppingCartItemService.getUserShoppingCartByEmail(user1.getEmail())).thenReturn(List.of());

            // Assert
            Assertions.assertThrows(EntityNotFoundException.class,
                    () -> orderService.saveUserShoppingCartAsOrder(user1.getEmail(), "shippingAddressValue"));
        }
    }

    @Nested
    @DisplayName("Tests for saveUserShoppingCartAsOrder method")
    class TestSetOrderToBeDelivered {
        @Test
        @DisplayName("Should set order status to DELIVERED when order exists and user is owner")
        void test_setOrderToBeDelivered_whenOrderExistsAndUserIsOwner() {
            // Arrange
            var updatedOrderInfo = new OrderInfo(1L, user1, 100.0, "shippingAddressValue", OrderStatus.DELIVERED,
                    LocalDateTime.now(clock), LocalDateTime.now(clock), LocalDateTime.now(clock), null);

            // Act
            when(orderInfoRepository.findById(orderInfo.getId())).thenReturn(Optional.of(orderInfo));
            when(orderInfoRepository.save(updatedOrderInfo)).thenReturn(updatedOrderInfo);

            // Assert
            Assertions.assertEquals(updatedOrderInfo, orderService.setOrderToBeDelivered(orderInfo.getId()));
        }

        @Test
        @DisplayName("Should throw IllegalEntityStateException if order was canceled")
        void test_setOrderToBeDelivered_whenOrderWasCanceled() {
            // Arrange
            orderInfo.setStatus(OrderStatus.CANCELLED);

            // Act
            when(orderInfoRepository.findById(orderInfo.getId())).thenReturn(Optional.of(orderInfo));

            // Assert
            Assertions.assertThrows(IllegalEntityStateException.class,
                    () -> orderService.setOrderToBeDelivered(orderInfo.getId()));
        }

        @Test
        @DisplayName("Should throw IllegalEntityStateException if order was already delivered")
        void test_setOrderToBeDelivered_whenOrderWasAlreadyDelivered() {
            // Arrange
            orderInfo.setStatus(OrderStatus.DELIVERED);

            // Act
            when(orderInfoRepository.findById(orderInfo.getId())).thenReturn(Optional.of(orderInfo));

            // Assert
            Assertions.assertThrows(IllegalEntityStateException.class,
                    () -> orderService.setOrderToBeDelivered(orderInfo.getId()));
        }
    }

    @Nested
    @DisplayName("Tests for cancelOrder method")
    class TestCancelOrder {
        @Test
        @DisplayName("Should cancel order when order exists and user is owner")
        void test_cancelOrder_whenOrderExistsAndUserIsOwner() {
            // Arrange
            var updatedOrderInfo = new OrderInfo(1L, user1, 100.0, "shippingAddressValue", OrderStatus.CANCELLED,
                    null, LocalDateTime.now(clock), LocalDateTime.now(clock), LocalDateTime.now(clock));

            // Act
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
            when(orderInfoRepository.findById(orderInfo.getId())).thenReturn(Optional.of(orderInfo));
            when(orderInfoRepository.save(updatedOrderInfo)).thenReturn(updatedOrderInfo);

            // Assert
            Assertions.assertEquals(updatedOrderInfo, orderService.cancelOrder(user1.getEmail(), orderInfo.getId()));
        }

        @Test
        @DisplayName("Should throw InvalidUserAccessException if user is not owner")
        void test_cancelOrder_whenUserIsNotOwner() {
            // Arrange

            // Act
            when(userService.getUserByEmail(user2.getEmail())).thenReturn(user2);
            when(orderInfoRepository.findById(orderInfo.getId())).thenReturn(Optional.of(orderInfo));

            // Assert
            Assertions.assertThrows(InvalidUserAccessException.class,
                    () -> orderService.cancelOrder(user2.getEmail(), orderInfo.getId()));
        }

        @Test
        @DisplayName("Should throw IllegalEntityStateException if order was already delivered")
        void test_cancelOrder_whenOrderWasAlreadyDelivered() {
            // Arrange
            orderInfo.setStatus(OrderStatus.DELIVERED);

            // Act
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
            when(orderInfoRepository.findById(orderInfo.getId())).thenReturn(Optional.of(orderInfo));

            // Assert
            Assertions.assertThrows(IllegalEntityStateException.class,
                    () -> orderService.cancelOrder(user1.getEmail(), orderInfo.getId()));
        }

        @Test
        @DisplayName("Should throw IllegalEntityStateException if order was already canceled")
        void test_cancelOrder_whenOrderWasAlreadyCanceled() {
            // Arrange
            orderInfo.setStatus(OrderStatus.CANCELLED);

            // Act
            when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
            when(orderInfoRepository.findById(orderInfo.getId())).thenReturn(Optional.of(orderInfo));

            // Assert
            Assertions.assertThrows(IllegalEntityStateException.class,
                    () -> orderService.cancelOrder(user1.getEmail(), orderInfo.getId()));
        }
    }
}
