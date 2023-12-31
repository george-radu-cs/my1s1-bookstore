package com.georgeradu.bookstore.services;

import com.georgeradu.bookstore.exception.DuplicateObjectException;
import com.georgeradu.bookstore.model.User;
import com.georgeradu.bookstore.model.UserRole;
import com.georgeradu.bookstore.repository.UserRepository;
import com.georgeradu.bookstore.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {}

    @AfterEach
    void tearDown() {}

    @Nested
    @DisplayName("Test addUser method")
    class TestAddUser {
        @Test
        @DisplayName("Should add new user")
        void test_addUser_shouldAddNewUser() {
            // Arrange
            User user = new User();
            user.setFirstName("firstNameValue");
            user.setLastName("lastNameValue");
            user.setEmail("emailValue");
            user.setPassword("passwordValue");

            var timestamp = LocalDateTime.now();
            User savedUser = new User();
            savedUser.setId(1L);
            savedUser.setFirstName("firstNameValue");
            savedUser.setLastName("lastNameValue");
            savedUser.setEmail("emailValue");
            savedUser.setPassword("passwordValue");
            savedUser.setRole(UserRole.ROLE_USER);
            savedUser.setCreatedAt(timestamp);
            savedUser.setUpdatedAt(timestamp);

            // Act
            when(userRepository.save(user)).thenReturn(savedUser);

            // Assert
            User response = userService.addUser(user);
            Assertions.assertNotNull(response);
            Assertions.assertEquals(savedUser, response);
        }

        @Test
        @DisplayName("Should throw DuplicateObjectException when user with email already exists")
        void test_addUser_shouldThrowDuplicateObjectExceptionWhenUserWithEmailAlreadyExists() {
            // Arrange
            User user = new User();
            user.setFirstName("firstNameValue");
            user.setLastName("lastNameValue");
            user.setEmail("emailValue");
            user.setPassword("passwordValue");

            // Act
            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

            // Assert
            DuplicateObjectException actualException = Assertions.assertThrows(DuplicateObjectException.class,
                    () -> userService.addUser(user));
            Assertions.assertEquals("Object: User with email " + user.getEmail() + " already exists",
                    actualException.getMessage());
        }
    }
}
