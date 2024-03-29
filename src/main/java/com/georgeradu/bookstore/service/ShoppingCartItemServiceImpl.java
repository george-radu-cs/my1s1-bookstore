package com.georgeradu.bookstore.service;

import com.georgeradu.bookstore.dto.ShoppingCartItemRequest;
import com.georgeradu.bookstore.exception.DuplicateObjectException;
import com.georgeradu.bookstore.exception.InvalidUserAccessException;
import com.georgeradu.bookstore.model.ShoppingCartItem;
import com.georgeradu.bookstore.repository.ShoppingCartItemRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartItemServiceImpl implements ShoppingCartItemService {
    private final Clock clock;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final UserService userService;
    private final BookService bookService;

    public ShoppingCartItemServiceImpl(
            Clock clock, ShoppingCartItemRepository shoppingCartItemRepository, UserService userService,
            BookService bookService
    ) {
        this.clock = clock;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.userService = userService;
        this.bookService = bookService;
    }

    public List<ShoppingCartItem> getUserShoppingCart(Long userId) {
        userService.getUser(userId); // check if user exists
        return shoppingCartItemRepository.findAllByUserId(userId);
    }

    public List<ShoppingCartItem> getUserShoppingCartByEmail(String userEmail) {
        var user = userService.getUserByEmail(userEmail); // check if user exists
        return shoppingCartItemRepository.findAllByUserId(user.getId());
    }

    public ShoppingCartItem addBookToShoppingCart(String userEmail, ShoppingCartItemRequest request) {
        var user = userService.getUserByEmail(userEmail);
        var book = bookService.getBook(request.getBookId());

        var existentShoppingCartItem = shoppingCartItemRepository.findByUserIdAndBookId(user.getId(), book.getId());
        if (existentShoppingCartItem.isPresent()) {
            throw new DuplicateObjectException(
                    "User " + user.getId() + " already has book " + book.getId() + " in their shopping cart");
        }

        var shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setUser(user);
        shoppingCartItem.setBook(book);
        shoppingCartItem.setQuantity(request.getQuantity());
        var timestamp = LocalDateTime.now(clock);
        shoppingCartItem.setCreatedAt(timestamp);
        shoppingCartItem.setUpdatedAt(timestamp);

        return shoppingCartItemRepository.save(shoppingCartItem);
    }

    public ShoppingCartItem updateShoppingCartItem(
            String userEmail, Long shoppingCartItemId, ShoppingCartItemRequest request
    ) {
        var user = userService.getUserByEmail(userEmail);
        var shoppingCartItem = shoppingCartItemRepository.findById(shoppingCartItemId).orElseThrow();
        if (!shoppingCartItem.getUser().getId().equals(user.getId())) {
            throw new InvalidUserAccessException(
                    "User " + user.getId() + " cannot update shopping cart item " + shoppingCartItem.getId());
        }

        shoppingCartItem.setQuantity(request.getQuantity());
        shoppingCartItem.setUpdatedAt(LocalDateTime.now(clock));
        return shoppingCartItemRepository.save(shoppingCartItem);
    }

    public void deleteShoppingCartItem(String userEmail, Long shoppingCartItemId) {
        var user = userService.getUserByEmail(userEmail);
        var shoppingCartItem = shoppingCartItemRepository.findById(shoppingCartItemId).orElseThrow();
        if (!shoppingCartItem.getUser().getId().equals(user.getId())) {
            throw new InvalidUserAccessException(
                    "User " + user.getId() + " cannot delete shopping cart item " + shoppingCartItem.getId());
        }

        shoppingCartItemRepository.delete(shoppingCartItem);
    }

    public void deleteAllUserShoppingCartItems(String userEmail) {
        var user = userService.getUserByEmail(userEmail);
        shoppingCartItemRepository.deleteAllByUserId(user.getId());
    }
}
