package com.georgeradu.bookstore.service;

import com.georgeradu.bookstore.exception.DuplicateObjectException;
import com.georgeradu.bookstore.model.User;
import com.georgeradu.bookstore.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) throws DuplicateObjectException {
        var timestamp = LocalDateTime.now();
        user.setCreatedAt(timestamp);
        user.setUpdatedAt(timestamp);

        var existentUser = userRepository.findByEmail(user.getEmail());
        if (existentUser.isPresent()) {
            throw new DuplicateObjectException("User with email " + user.getEmail());
        }

        return userRepository.save(user);
    }
}
