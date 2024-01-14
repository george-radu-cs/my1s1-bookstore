package com.georgeradu.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.georgeradu.bookstore.dto.OrderInfoResponse;
import com.georgeradu.bookstore.dto.OrderItemResponse;
import com.georgeradu.bookstore.model.*;
import com.georgeradu.bookstore.service.OrderService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class OrderControllerTest {
    private final static LocalDateTime NOW = LocalDateTime.now();
    private User user;
    private Book book1, book2;
    private OrderInfo orderInfo, orderInfo2;
    private OrderItem orderItem1, orderItem2;
    private OrderInfoResponse orderInfoResponse, orderInfoResponse2;
    private OrderItemResponse orderItemResponse1, orderItemResponse2;

    @MockBean
    private Clock clock;
    private Clock fixedClock;
    @Mock
    private OrderService orderService;
    @InjectMocks
    private OrderController orderController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        fixedClock = Clock.fixed(NOW
                        .toLocalDate()
                        .atStartOfDay()
                        .toInstant(Clock.systemDefaultZone().getZone().getRules().getOffset(NOW)),
                Clock.systemDefaultZone().getZone());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        user = new User(1L, "firstNameValue", "lastNameValue", "emailValue", "passwordValue", UserRole.ROLE_USER,
                LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        var bookCategory = new BookCategory(1L, "nameValue1", "descriptionValue1", LocalDateTime.now(clock),
                LocalDateTime.now(clock), null);
        book1 = new Book(1L, "titleValue1", "authorValue1", "descriptionValue1", 1, "imageUrl1", 1, "language1",
                "publisher1", LocalDateTime.now(clock), "isbn101", "isbn131", "dimensions1", 0, bookCategory,
                LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        book2 = new Book(2L, "titleValue2", "authorValue2", "descriptionValue2", 2, "imageUrl2", 2, "language2",
                "publisher2", LocalDateTime.now(clock), "isbn102", "isbn132", "dimensions2", 0, bookCategory,
                LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        orderInfo = new OrderInfo(1L, user, 100.0, "shippingAddressValue", OrderStatus.PENDING, null,
                LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        orderInfo2 = new OrderInfo(2L, user, 10.0, "shippingAddressValue", OrderStatus.DELIVERED,
                LocalDateTime.now(clock), LocalDateTime.now(clock), LocalDateTime.now(clock), null);
        orderItem1 = new OrderItem(1L, orderInfo, book1, 1, 100.0, LocalDateTime.now(clock), LocalDateTime.now(clock),
                null);
        orderItem2 = new OrderItem(2L, orderInfo, book2, 2, 200.0, LocalDateTime.now(clock), LocalDateTime.now(clock),
                null);
        orderInfoResponse = new OrderInfoResponse(1L, 1L, 100.0, "shippingAddressValue", OrderStatus.PENDING, null,
                LocalDateTime.now(clock), LocalDateTime.now(clock));
        orderInfoResponse2 = new OrderInfoResponse(2L, 1L, 10.0, "shippingAddressValue", OrderStatus.DELIVERED,
                LocalDateTime.now(clock), LocalDateTime.now(clock), LocalDateTime.now(clock));
        orderItemResponse1 = new OrderItemResponse(1L, 1L, 1L, 1, 100.0);
        orderItemResponse2 = new OrderItemResponse(2L, 1L, 2L, 2, 200.0);
    }

    @AfterEach
    void tearDown() {}

    @Nested
    @DisplayName("Test createNewOrder endpoint")
    class TestCreateNewOrderEndpoint {
        @Test
        @DisplayName("Should create new order")
        @WithMockUser(username = "emailValue", roles = {"USER"})
        void test_createNewOrder_shouldCreateNewOrder() throws Exception {
            // Arrange
            var shippingAddress = "shippingAddressValue";

            // Act
            when(orderService.saveUserShoppingCartAsOrder("emailValue", shippingAddress)).thenReturn(orderInfo);

            // Assert
            MvcResult actualResult = mockMvc
                    .perform(post("/order/new-order?shippingAddress=" + shippingAddress))
                    .andExpect(status().isOk())
                    .andReturn();

            Assertions.assertEquals(objectMapper.writeValueAsString(orderInfoResponse),
                    actualResult.getResponse().getContentAsString());
        }
    }

    @Nested
    @DisplayName("Test cancelOrder endpoint")
    class TestCancelOrderEndpoint {
        @Test
        @DisplayName("Should cancel order")
        @WithMockUser(username = "emailValue", roles = {"USER"})
        void test_cancelOrder_shouldCancelOrder() throws Exception {
            // Arrange
            var orderId = 1L;
            orderInfo.setStatus(OrderStatus.CANCELLED);
            orderInfo.setDeletedAt(LocalDateTime.now(clock));
            orderInfoResponse.setStatus(OrderStatus.CANCELLED);

            // Act
            when(orderService.cancelOrder("emailValue", orderId)).thenReturn(orderInfo);

            // Assert
            MvcResult actualResult = mockMvc
                    .perform(put("/order/cancel-order/{orderId}", orderId))
                    .andExpect(status().isOk())
                    .andReturn();

            Assertions.assertEquals(objectMapper.writeValueAsString(orderInfoResponse),
                    actualResult.getResponse().getContentAsString());
        }
    }

    @Nested
    @DisplayName("Test deliverOrder endpoint")
    class TestDeliverOrderEndpoint {
        @Test
        @DisplayName("Should deliver order")
        @WithMockUser(username = "emailValue", roles = {"ADMIN"})
        void test_deliverOrder_shouldDeliverOrder() throws Exception {
            // Arrange
            var orderId = 1L;
            orderInfo.setStatus(OrderStatus.DELIVERED);
            orderInfo.setDeliveredAt(LocalDateTime.now(clock));
            orderInfoResponse.setStatus(OrderStatus.DELIVERED);
            orderInfoResponse.setDeliveredAt(LocalDateTime.now(clock));

            // Act
            when(orderService.setOrderToBeDelivered(orderId)).thenReturn(orderInfo);

            // Assert
            MvcResult actualResult = mockMvc
                    .perform(put("/order/deliver-order/{orderId}", orderId))
                    .andExpect(status().isOk())
                    .andReturn();

            Assertions.assertEquals(objectMapper.writeValueAsString(orderInfoResponse),
                    actualResult.getResponse().getContentAsString());
        }
    }

    @Nested
    @DisplayName("Test get current user order info endpoint")
    class TestGetCurrentUserOrderInfoEndpoint {
        @Test
        @DisplayName("Should return current user order info")
        @WithMockUser(username = "emailValue", roles = {"USER"})
        void test_getCurrentUserOrderInfo_shouldReturnCurrentUserOrderInfo() throws Exception {
            // Arrange
            var orderId = 1L;

            // Act
            when(orderService.getUserOrderInfoById("emailValue", orderId)).thenReturn(orderInfo);

            // Assert
            MvcResult actualResult = mockMvc
                    .perform(get("/order/my-order-info/{orderId}", orderId))
                    .andExpect(status().isOk())
                    .andReturn();

            Assertions.assertEquals(objectMapper.writeValueAsString(orderInfoResponse),
                    actualResult.getResponse().getContentAsString());
        }
    }

    @Nested
    @DisplayName("Test get current user order items endpoint")
    class TestGetCurrentUserOrderItemsEndpoint {
        @Test
        @DisplayName("Should return current user order items")
        @WithMockUser(username = "emailValue", roles = {"USER"})
        void test_getCurrentUserOrderItems_shouldReturnCurrentUserOrderItems() throws Exception {
            // Arrange
            var orderId = 1L;

            // Act
            when(orderService.getUserOrderItemsByOrderInfoId("emailValue", orderId)).thenReturn(
                    List.of(orderItem1, orderItem2));

            // Assert
            MvcResult actualResult = mockMvc
                    .perform(get("/order/my-order-items/{orderId}", orderId))
                    .andExpect(status().isOk())
                    .andReturn();

            Assertions.assertEquals(objectMapper.writeValueAsString(List.of(orderItemResponse1, orderItemResponse2)),
                    actualResult.getResponse().getContentAsString());
        }
    }

    @Nested
    @DisplayName("Test get current user order history endpoint")
    class TestGetCurrentUserOrderHistoryEndpoint {
        @Test
        @DisplayName("Should return current user order history")
        @WithMockUser(username = "emailValue", roles = {"USER"})
        void test_getCurrentUserOrderHistory_shouldReturnCurrentUserOrderHistory() throws Exception {
            // Arrange

            // Act
            when(orderService.getUserOrderHistory("emailValue")).thenReturn(List.of(orderInfo, orderInfo2));

            // Assert
            MvcResult actualResult = mockMvc
                    .perform(get("/order/my-order-history"))
                    .andExpect(status().isOk())
                    .andReturn();

            Assertions.assertEquals(objectMapper.writeValueAsString(List.of(orderInfoResponse, orderInfoResponse2)),
                    actualResult.getResponse().getContentAsString());
        }
    }

    @Nested
    @DisplayName("Test get order by id by admin endpoint")
    class TestGetOrderByIdByAdminEndpoint {
        @Test
        @DisplayName("Should return order by id by admin")
        @WithMockUser(username = "emailValue", roles = {"ADMIN"})
        void test_getOrderByIdByAdmin_shouldReturnOrderByIdByAdmin() throws Exception {
            // Arrange
            var orderId = 1L;

            // Act
            when(orderService.getOrderInfoById(orderId)).thenReturn(orderInfo);

            // Assert
            MvcResult actualResult = mockMvc
                    .perform(get("/order/order-info/{orderId}", orderId))
                    .andExpect(status().isOk())
                    .andReturn();

            Assertions.assertEquals(objectMapper.writeValueAsString(orderInfoResponse),
                    actualResult.getResponse().getContentAsString());
        }
    }

    @Nested
    @DisplayName("Test get order items by order id by admin endpoint")
    class TestGetOrderItemsByOrderIdByAdminEndpoint {
        @Test
        @DisplayName("Should return order items for order id by admin")
        @WithMockUser(username = "emailValue", roles = {"ADIN"})
        void test_getOrderItemsByOrderId_shouldReturnCurrentUserOrderItems() throws Exception {
            // Arrange
            var orderId = 1L;

            // Act
            when(orderService.getOrderItemsByOrderInfoId(orderId)).thenReturn(List.of(orderItem1, orderItem2));

            // Assert
            MvcResult actualResult = mockMvc
                    .perform(get("/order/order-items/{orderId}", orderId))
                    .andExpect(status().isOk())
                    .andReturn();

            Assertions.assertEquals(objectMapper.writeValueAsString(List.of(orderItemResponse1, orderItemResponse2)),
                    actualResult.getResponse().getContentAsString());
        }
    }

    @Nested
    @DisplayName("Test get user order history by user id by admin endpoint")
    class TestGetUserOrderHistoryByUserIdByAdminEndpoint {
        @Test
        @DisplayName("Should return user order history by user id by admin")
        @WithMockUser(username = "emailValue", roles = {"ADMIN"})
        void test_getCurrentUserOrderHistory_shouldReturnCurrentUserOrderHistory() throws Exception {
            // Arrange
            var userId = 1L;

            // Act
            when(orderService.getOrderHistoryForUserById(userId)).thenReturn(List.of(orderInfo, orderInfo2));

            // Assert
            MvcResult actualResult = mockMvc
                    .perform(get("/order/order-history/{userId}", userId))
                    .andExpect(status().isOk())
                    .andReturn();

            Assertions.assertEquals(objectMapper.writeValueAsString(List.of(orderInfoResponse, orderInfoResponse2)),
                    actualResult.getResponse().getContentAsString());
        }
    }
}
