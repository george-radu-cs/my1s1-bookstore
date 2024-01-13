package com.georgeradu.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.georgeradu.bookstore.model.User;
import com.georgeradu.bookstore.model.UserRole;
import com.georgeradu.bookstore.service.UserService;
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

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
public class UserControllerTest {
    private final static LocalDateTime NOW = LocalDateTime.now();

    @MockBean
    private Clock clock;
    private Clock fixedClock;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        fixedClock = Clock.fixed(NOW.toLocalDate().atStartOfDay().toInstant(Clock.systemDefaultZone().getZone().getRules().getOffset(NOW)), Clock.systemDefaultZone().getZone());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    void tearDown() {}

    @Nested
    @DisplayName("Test getCurrentUserInfo endpoint")
    class TestGetCurrentUserInfoEndpoint {
        @Test
        @WithMockUser(username = "emailValue", roles = {"USER"})
        void test_getCurrentUserInfo_shouldReturnUserInfo() throws Exception {
            // Arrange
            var user = new User(1L, "firstNameValue", "lastNameValue", "emailValue", "passwordValue",
                    UserRole.ROLE_USER, LocalDateTime.now(clock), LocalDateTime.now(clock), null);

            // Act
            when(userService.getUserByEmail("emailValue")).thenReturn(user);

            // Assert
            mockMvc
                    .perform(get("/user/me"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.id").value(user.getId()))
                    .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                    .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                    .andExpect(jsonPath("$.email").value(user.getEmail()))
                    .andExpect(jsonPath("$.role").value(user.getRole().toString()));
        }
    }

    @Nested
    @DisplayName("Test adminGetUser endpoint")
    class TestAdminGetUserEndpoint {
        @Test
        @WithMockUser(username = "emailValue", roles = {"ADMIN"})
        void test_adminGetUser_shouldReturnUserInfo() throws Exception {
            // Arrange
            var user = new User(1L, "firstNameValue", "lastNameValue", "emailValue", "passwordValue",
                    UserRole.ROLE_USER, LocalDateTime.now(clock), LocalDateTime.now(clock), null);

            // Act
            when(userService.getUser(1L)).thenReturn(user);

            // Assert
            mockMvc
                    .perform(get("/user/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.id").value(user.getId()))
                    .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                    .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                    .andExpect(jsonPath("$.email").value(user.getEmail()))
                    .andExpect(jsonPath("$.role").value(user.getRole().toString()));
        }
    }
}
