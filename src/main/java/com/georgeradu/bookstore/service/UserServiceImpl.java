package com.georgeradu.bookstore.service;

import com.georgeradu.bookstore.exception.EntityNotFoundException;
import com.georgeradu.bookstore.exception.UserAlreadyExistsException;
import com.georgeradu.bookstore.model.User;
import com.georgeradu.bookstore.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements  UserService {
    private final Clock clock;
    private final UserRepository userRepository;

    public UserServiceImpl(Clock clock, UserRepository userRepository) {
        this.clock = clock;
        this.userRepository = userRepository;
    }

    public User getUser(Long id) throws EntityNotFoundException {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public User getUserByEmail(String userEmail) throws EntityNotFoundException {
        return userRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public User addUser(User user) throws UserAlreadyExistsException {
        var timestamp = LocalDateTime.now(clock);
        user.setCreatedAt(timestamp);
        user.setUpdatedAt(timestamp);

        var existentUser = userRepository.findByEmail(user.getEmail());
        if (existentUser.isPresent()) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail());
        }

        return userRepository.save(user);
    }
}
