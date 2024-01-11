package com.georgeradu.bookstore.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class ShoppingCartItemRequest {
    @NotNull(message = "Book ID is required")
    private Long bookId;
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    @Max(value = 100, message = "Quantity must be less than 100")
    private int quantity;

    public ShoppingCartItemRequest() {
    }

    public ShoppingCartItemRequest(Long bookId, int quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
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
        ShoppingCartItemRequest that = (ShoppingCartItemRequest) o;
        return quantity == that.quantity && Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, quantity);
    }

    @Override
    public String toString() {
        return "ShoppingCartItemRequest{" + "bookId=" + bookId + ", quantity=" + quantity + '}';
    }
}
