package com.georgeradu.bookstore.dto;

import com.georgeradu.bookstore.model.ShoppingCartItem;

import java.util.List;
import java.util.Objects;

public class ShoppingCartItemResponse {
    private Long id;
    private Long userId;
    private Long bookId;
    private int quantity;

    public ShoppingCartItemResponse() {
    }

    public ShoppingCartItemResponse(Long id, Long userId, Long bookId, int quantity) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public ShoppingCartItemResponse(ShoppingCartItem response) {
        this.id = response.getId();
        this.userId = response.getUser().getId();
        this.bookId = response.getBook().getId();
        this.quantity = response.getQuantity();
    }

    public static List<ShoppingCartItemResponse> fromList(List<ShoppingCartItem> shoppingCartItems) {
        return shoppingCartItems.stream().map(ShoppingCartItemResponse::new).toList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCartItemResponse that = (ShoppingCartItemResponse) o;
        return quantity == that.quantity && Objects.equals(id, that.id) && Objects.equals(userId, that.userId) &&
               Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, bookId, quantity);
    }

    @Override
    public String toString() {
        return "ShoppingCartItemResponse{" + "id=" + id + ", userId=" + userId + ", bookId=" + bookId + ", quantity=" +
               quantity + '}';
    }
}
