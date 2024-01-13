package com.georgeradu.bookstore.services;

import com.georgeradu.bookstore.exception.EntityNotFoundException;
import com.georgeradu.bookstore.exception.UserAlreadyExistsException;
import com.georgeradu.bookstore.model.User;
import com.georgeradu.bookstore.model.UserRole;
import com.georgeradu.bookstore.repository.UserRepository;
import com.georgeradu.bookstore.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    private final static LocalDateTime NOW = LocalDateTime.now();

    @MockBean
    private Clock clock;
    private Clock fixedClock;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {
        fixedClock = Clock.fixed(NOW.toLocalDate().atStartOfDay().toInstant(Clock.systemDefaultZone().getZone().getRules().getOffset(NOW)), Clock.systemDefaultZone().getZone());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
    }

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

            var timestamp = LocalDateTime.now(clock);
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
        @DisplayName("Should throw UserAlreadyExistsException when user with email already exists")
        void test_addUser_shouldThrowUserAlreadyExistsExceptionWhenUserWithEmailAlreadyExists() {
            // Arrange
            User user = new User();
            user.setFirstName("firstNameValue");
            user.setLastName("lastNameValue");
            user.setEmail("emailValue");
            user.setPassword("passwordValue");

            // Act
            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

            // Assert
            UserAlreadyExistsException actualException = Assertions.assertThrows(UserAlreadyExistsException.class,
                    () -> userService.addUser(user));
            Assertions.assertEquals("Object: User with email " + user.getEmail() + " already exists",
                    actualException.getMessage());
        }
    }

    @Nested
    @DisplayName("Test getUser method")
    class TestGetUser {
        @Test
        @DisplayName("Should return user when user exists")
        void test_getUser_shouldReturnUserWhenUserExists() {
            // Arrange
            User user = new User();
            user.setId(1L);
            user.setFirstName("firstNameValue");
            user.setLastName("lastNameValue");
            user.setEmail("emailValue");
            user.setPassword("passwordValue");
            var timestamp = LocalDateTime.now(clock);
            user.setCreatedAt(timestamp);
            user.setUpdatedAt(timestamp);

            // Act
            when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

            // Assert
            User response = userService.getUser(user.getId());
            Assertions.assertNotNull(response);
            Assertions.assertEquals(user, response);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when user does not exist")
        void test_getUser_shouldThrowEntityNotFoundExceptionWhenUserDoesNotExist() {
            // Arrange
            User user = new User();
            user.setId(1L);
            user.setFirstName("firstNameValue");
            user.setLastName("lastNameValue");
            user.setEmail("emailValue");
            user.setPassword("passwordValue");
            var timestamp = LocalDateTime.now(clock);
            user.setCreatedAt(timestamp);
            user.setUpdatedAt(timestamp);

            // Act
            when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

            // Assert
            Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getUser(user.getId()));
        }
    }

    @Nested
    @DisplayName("Test getUserByEmail method")
    class TestGetUserByEmail {
        @Test
        @DisplayName("Should return user when user exists")
        void test_getUserByEmail_shouldReturnUserWhenUserExists() {
            // Arrange
            User user = new User();
            user.setId(1L);
            user.setFirstName("firstNameValue");
            user.setLastName("lastNameValue");
            user.setEmail("emailValue");
            user.setPassword("passwordValue");
            var timestamp = LocalDateTime.now(clock);
            user.setCreatedAt(timestamp);
            user.setUpdatedAt(timestamp);

            // Act
            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

            // Assert
            User response = userService.getUserByEmail(user.getEmail());
            Assertions.assertNotNull(response);
            Assertions.assertEquals(user, response);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when user does not exist")
        void test_getUserByEmail_shouldThrowEntityNotFoundExceptionWhenUserDoesNotExist() {
            // Arrange
            User user = new User();
            user.setId(1L);
            user.setFirstName("firstNameValue");
            user.setLastName("lastNameValue");
            user.setEmail("emailValue");
            user.setPassword("passwordValue");
            var timestamp = LocalDateTime.now(clock);
            user.setCreatedAt(timestamp);
            user.setUpdatedAt(timestamp);

            // Act
            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

            // Assert
            Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getUserByEmail(user.getEmail()));
        }
    }
}
