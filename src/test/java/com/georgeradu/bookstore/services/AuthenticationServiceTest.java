package com.georgeradu.bookstore.services;

import com.georgeradu.bookstore.dto.LoginRequest;
import com.georgeradu.bookstore.dto.RegisterRequest;
import com.georgeradu.bookstore.exception.UserAlreadyExistsException;
import com.georgeradu.bookstore.exception.InvalidLoginException;
import com.georgeradu.bookstore.model.User;
import com.georgeradu.bookstore.repository.UserRepository;
import com.georgeradu.bookstore.service.AuthenticationService;
import com.georgeradu.bookstore.service.JwtService;
import com.georgeradu.bookstore.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthenticationServiceTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthenticationService authenticationService;


    @BeforeEach
    public void setUp() {}

    @AfterEach
    void tearDown() {}


    @Nested
    @DisplayName("Test register method")
    class TestRegisterMethod {
        @Test
        @DisplayName("Should register a new user")
        void test_register_shouldRegisterNewUser() {
            // Arrange
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setFirstName("firstNameValue");
            registerRequest.setLastName("lastNameValue");
            registerRequest.setEmail("emailValue");
            registerRequest.setPassword("passwordValue");

            // Act
            when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPasswordValue");
            when(userService.addUser(any())).thenReturn(new User());
            when(jwtService.generateToken(any())).thenReturn("tokenValue");

            // Assert
            var response = authenticationService.register(registerRequest);
            Assertions.assertEquals("tokenValue", response.getToken());
        }

        @Test
        @DisplayName("Should throw exception when user already exists")
        void test_register_shouldThrowExceptionWhenUserAlreadyExists() {
            // Arrange
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setFirstName("firstNameValue");
            registerRequest.setLastName("lastNameValue");
            registerRequest.setEmail("emailValue");
            registerRequest.setPassword("passwordValue");

            // Act
            when(userService.addUser(any())).thenThrow(
                    new UserAlreadyExistsException("User with email " + registerRequest.getEmail()));

            // Assert
            UserAlreadyExistsException actualException = Assertions.assertThrows(UserAlreadyExistsException.class,
                    () -> authenticationService.register(registerRequest));
            Assertions.assertEquals("Object: User with email " + registerRequest.getEmail() + " already exists",
                    actualException.getMessage());
        }
    }

    @Nested
    @DisplayName("Test login method")
    class TestLoginMethod {
        @Test
        @DisplayName("Should login a user")
        void test_login_shouldLoginUser() {
            // Arrange
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail("emailValue");
            loginRequest.setPassword("passwordValue");

            // Act
            when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(new User()));
            when(jwtService.generateToken(any())).thenReturn("tokenValue");
            when(authenticationManager.authenticate(any())).thenReturn(null);

            // Assert
            var response = authenticationService.login(loginRequest);
            Assertions.assertEquals("tokenValue", response.getToken());
        }

        @Test
        @DisplayName("Should throw exception when user does not exist")
        void test_login_shouldThrowExceptionWhenUserDoesNotExist() {
            // Arrange
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail("emailValue");
            loginRequest.setPassword("passwordValue");

            // Act
            when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

            // Assert
            Assertions.assertThrows(InvalidLoginException.class, () -> authenticationService.login(loginRequest));
        }
    }
}
