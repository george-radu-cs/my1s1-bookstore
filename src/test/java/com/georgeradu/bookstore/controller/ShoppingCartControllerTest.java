package com.georgeradu.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.georgeradu.bookstore.dto.ShoppingCartItemRequest;
import com.georgeradu.bookstore.dto.ShoppingCartItemResponse;
import com.georgeradu.bookstore.model.*;
import com.georgeradu.bookstore.service.ShoppingCartItemService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class ShoppingCartControllerTest {
    private final static LocalDateTime NOW = LocalDateTime.now();
    private User user;
    private Book book1, book2;
    private ShoppingCartItem shoppingCartItem1, shoppingCartItem2;
    private ShoppingCartItemResponse shoppingCartItemResponse1, shoppingCartItemResponse2;

    @MockBean
    private Clock clock;
    private Clock fixedClock;
    @Mock
    private ShoppingCartItemService shoppingCartItemService;
    @InjectMocks
    private ShoppingCartController shoppingCartController;
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
        mockMvc = MockMvcBuilders.standaloneSetup(shoppingCartController).build();
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
        shoppingCartItem1 = new ShoppingCartItem(1L, user, book1, 1, LocalDateTime.now(clock), LocalDateTime.now(clock),
                null);
        shoppingCartItem2 = new ShoppingCartItem(2L, user, book2, 2, LocalDateTime.now(clock), LocalDateTime.now(clock),
                null);
        shoppingCartItemResponse1 = new ShoppingCartItemResponse(1L, 1L, 1L, 1);
        shoppingCartItemResponse2 = new ShoppingCartItemResponse(2L, 1L, 2L, 2);
    }

    @AfterEach
    void tearDown() {}

    @Nested
    @DisplayName("Test getCurrentUserShoppingCart endpoint")
    class TestGetCurrentUserShoppingCartEndpoint {
        @Test
        @DisplayName("Should return current user shopping cart")
        @WithMockUser(username = "emailValue", roles = {"USER"})
        void test_getCurrentUserShoppingCart_shouldReturnShoppingCart() throws Exception {
            // Arrange

            // Act
            when(shoppingCartItemService.getUserShoppingCartByEmail("emailValue")).thenReturn(
                    List.of(shoppingCartItem1, shoppingCartItem2));

            // Assert
            var actualResult = mockMvc.perform(get("/shopping-cart")).andExpect(status().isOk()).andReturn();

            Assertions.assertEquals(
                    objectMapper.writeValueAsString(List.of(shoppingCartItemResponse1, shoppingCartItemResponse2)),
                    actualResult.getResponse().getContentAsString());
        }
    }

    @Nested
    @DisplayName("Test adminGetUserShoppingCart endpoint")
    class TestAdminGetUserShoppingCartEndpoint {
        @Test
        @DisplayName("Should return user shopping cart")
        @WithMockUser(username = "adminValue", roles = {"ADMIN"})
        void test_adminGetUserShoppingCart_shouldReturnShoppingCart() throws Exception {
            // Arrange

            // Act
            when(shoppingCartItemService.getUserShoppingCart(1L)).thenReturn(
                    List.of(shoppingCartItem1, shoppingCartItem2));

            // Assert
            var actualResult = mockMvc
                    .perform(get("/shopping-cart/{userId}", user.getId()))
                    .andExpect(status().isOk())
                    .andReturn();

            Assertions.assertEquals(
                    objectMapper.writeValueAsString(List.of(shoppingCartItemResponse1, shoppingCartItemResponse2)),
                    actualResult.getResponse().getContentAsString());
        }
    }

    @Nested
    @DisplayName("Test addBookToShoppingCart endpoint")
    class TestAddBookToShoppingCartEndpoint {
        @Test
        @DisplayName("Should add book to shopping cart")
        @WithMockUser(username = "emailValue", roles = {"USER"})
        void test_addBookToShoppingCart_shouldAddBookToShoppingCart() throws Exception {
            // Arrange
            var shoppingCartItemRequest = new ShoppingCartItemRequest(1L, 1);

            // Act
            when(shoppingCartItemService.addBookToShoppingCart("emailValue", shoppingCartItemRequest)).thenReturn(
                    shoppingCartItem1);

            // Assert
            var actualResult = mockMvc
                    .perform(post("/shopping-cart")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(shoppingCartItemRequest)))
                    .andExpect(status().isOk())
                    .andReturn();

            Assertions.assertEquals(objectMapper.writeValueAsString(shoppingCartItemResponse1),
                    actualResult.getResponse().getContentAsString());
        }
    }

    @Nested
    @DisplayName("Test updateShoppingCartItem endpoint")
    class TestUpdateShoppingCartItemEndpoint {
        @Test
        @DisplayName("Should update shopping cart item")
        @WithMockUser(username = "emailValue", roles = {"USER"})
        void test_updateShoppingCartItem_shouldUpdateShoppingCartItem() throws Exception {
            // Arrange
            var shoppingCartItemRequest = new ShoppingCartItemRequest(1L, 1);

            // Act
            when(shoppingCartItemService.updateShoppingCartItem("emailValue", 1L, shoppingCartItemRequest)).thenReturn(
                    shoppingCartItem1);

            // Assert
            var actualResult = mockMvc
                    .perform(put("/shopping-cart/{shoppingCartItemId}", 1L)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(shoppingCartItemRequest)))
                    .andExpect(status().isOk())
                    .andReturn();

            Assertions.assertEquals(objectMapper.writeValueAsString(shoppingCartItemResponse1),
                    actualResult.getResponse().getContentAsString());
        }
    }

    @Nested
    @DisplayName("Test deleteShoppingCartItem endpoint")
    class TestDeleteShoppingCartItemEndpoint {
        @Test
        @DisplayName("Should delete shopping cart item")
        @WithMockUser(username = "emailValue", roles = {"USER"})
        void test_deleteShoppingCartItem_shouldDeleteShoppingCartItem() throws Exception {
            // Arrange

            // Act
            doNothing().when(shoppingCartItemService).deleteShoppingCartItem("emailValue", 1L);

            // Assert
            mockMvc.perform(delete("/shopping-cart/{shoppingCartItemId}", 1L)).andExpect(status().isOk()).andReturn();
        }
    }
}
