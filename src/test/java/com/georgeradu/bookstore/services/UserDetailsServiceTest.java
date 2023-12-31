package com.georgeradu.bookstore.services;

import com.georgeradu.bookstore.model.User;
import com.georgeradu.bookstore.repository.UserRepository;
import com.georgeradu.bookstore.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest

public class UserDetailsServiceTest {
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Nested
    @DisplayName("Test UserDetailsService implementation")
    class TestUserDetailsServiceImplementation {
        @Test
        @DisplayName("Should load user by username")
        void test_loadUserByUsername_shouldLoadUserByUsername() {
            // Arrange
            User user = new User();
            user.setFirstName("firstNameValue");
            user.setLastName("lastNameValue");
            user.setEmail("emailValue");
            user.setPassword("passwordValue");

            // Act
            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

            // Assert
            Assertions.assertEquals(user, userDetailsService.loadUserByUsername(user.getEmail()));
        }
    }
}
