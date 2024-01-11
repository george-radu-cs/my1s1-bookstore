package com.georgeradu.bookstore.dto;

import com.georgeradu.bookstore.model.OrderItem;

import java.util.List;
import java.util.Objects;

public class OrderItemResponse {
    private Long id;
    private Long orderId;
    private Long bookId;
    private int quantity;
    private double price;

    public OrderItemResponse() {
    }

    public OrderItemResponse(Long id, Long orderId, Long bookId, int quantity, double price) {
        this.id = id;
        this.orderId = orderId;
        this.bookId = bookId;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItemResponse(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.orderId = orderItem.getOrderInfo().getId();
        this.bookId = orderItem.getBook().getId();
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();
    }

    public static List<OrderItemResponse> fromList(List<OrderItem> response) {
        return response.stream().map(OrderItemResponse::new).toList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemResponse that = (OrderItemResponse) o;
        return quantity == that.quantity && Double.compare(price, that.price) == 0 && Objects.equals(id, that.id) &&
               Objects.equals(orderId, that.orderId) && Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, bookId, quantity, price);
    }

    @Override
    public String toString() {
        return "OrderItemResponse{" + "id=" + id + ", orderId=" + orderId + ", bookId=" + bookId + ", quantity=" +
               quantity + ", price=" + price + '}';
    }
}
