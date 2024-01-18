package com.georgeradu.bookstore.service;

import com.georgeradu.bookstore.model.User;

public interface UserService {
    User getUser(Long id);
    User getUserByEmail(String userEmail);
    User addUser(User user);
}
