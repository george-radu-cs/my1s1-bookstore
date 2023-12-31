package com.georgeradu.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.georgeradu.bookstore.dto.JwtAuthenticationResponse;
import com.georgeradu.bookstore.dto.LoginRequest;
import com.georgeradu.bookstore.dto.RegisterRequest;
import com.georgeradu.bookstore.exception.DuplicateObjectException;
import com.georgeradu.bookstore.exception.InvalidLoginException;
import com.georgeradu.bookstore.service.AuthenticationService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
public class AuthControllerTest {
    @Mock
    private AuthenticationService authenticationService;
    @InjectMocks
    private AuthController authController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    void tearDown() {}

    @Nested
    @DisplayName("Test register endpoint")
    class TestRegisterEndpoint {
        @Test
        @DisplayName("Should register a new user")
        void test_register_shouldRegisterNewUser() throws Exception {
            // Arrange
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setFirstName("firstNameValue");
            registerRequest.setLastName("lastNameValue");
            registerRequest.setEmail("emailValue");
            registerRequest.setPassword("passwordValue");

            // Act
            when(authenticationService.register(registerRequest)).thenReturn(
                    new JwtAuthenticationResponse("tokenValue"));

            // Assert
            mockMvc
                    .perform(post("/auth/register")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(registerRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.token").exists())
                    .andExpect(jsonPath("$.token").isNotEmpty());
        }

        @Test
        @DisplayName("Should fail to register same user twice")
        void test_register_shouldFailToRegisterSameUserTwice() throws Exception {
            // Arrange
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setFirstName("firstNameValue");
            registerRequest.setLastName("lastNameValue");
            registerRequest.setEmail("emailValue");
            registerRequest.setPassword("passwordValue");

            // Act - assume the user already exists
            when(authenticationService.register(registerRequest)).thenThrow(
                    new DuplicateObjectException("User with email " + registerRequest.getEmail()));

            // Assert
            mockMvc
                    .perform(post("/auth/register")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(registerRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> Assertions.assertTrue(
                            result.getResolvedException() instanceof DuplicateObjectException))
                    .andExpect(result -> Assertions.assertEquals(
                            "Object: User with email " + registerRequest.getEmail() + " already exists",
                            result.getResolvedException().getMessage()));
        }
    }

    @Nested
    @DisplayName("Test login endpoint")
    class TestLoginEndpoint {
        @Test
        @DisplayName("Should login a valid user")
        void test_register_shouldLoginUser() throws Exception {
            // Arrange
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail("emailValue");
            loginRequest.setPassword("passwordValue");

            // Act
            when(authenticationService.login(loginRequest)).thenReturn(new JwtAuthenticationResponse("tokenValue"));

            // Assert
            mockMvc
                    .perform(post("/auth/login")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.token").exists())
                    .andExpect(jsonPath("$.token").isNotEmpty());
        }

        @Test
        @DisplayName("Should fail to login invalid user")
        void test_register_shouldFailToLoginInvalidUser() throws Exception {
            // Arrange
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail("emailValue");
            loginRequest.setPassword("passwordValue");

            // Act
            when(authenticationService.login(loginRequest)).thenThrow(new InvalidLoginException());

            // Assert
            mockMvc
                    .perform(post("/auth/login")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> Assertions.assertTrue(
                            result.getResolvedException() instanceof InvalidLoginException))
                    .andExpect(result -> Assertions.assertEquals("Invalid user credentials",
                            result.getResolvedException().getMessage()));
        }
    }
}
