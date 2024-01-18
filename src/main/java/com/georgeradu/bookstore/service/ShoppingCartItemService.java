package com.georgeradu.bookstore.service;

import com.georgeradu.bookstore.dto.ShoppingCartItemRequest;
import com.georgeradu.bookstore.model.ShoppingCartItem;

import java.util.List;

public interface ShoppingCartItemService {
    List<ShoppingCartItem> getUserShoppingCart(Long userId);
    List<ShoppingCartItem> getUserShoppingCartByEmail(String userEmail);
    ShoppingCartItem addBookToShoppingCart(String userEmail, ShoppingCartItemRequest request);
    ShoppingCartItem updateShoppingCartItem(String userEmail, Long shoppingCartItemId, ShoppingCartItemRequest request);
    void deleteShoppingCartItem(String userEmail, Long shoppingCartItemId);
    void deleteAllUserShoppingCartItems(String userEmail);
}
