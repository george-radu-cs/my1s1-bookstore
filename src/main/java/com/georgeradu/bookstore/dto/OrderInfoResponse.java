package com.georgeradu.bookstore.dto;

import com.georgeradu.bookstore.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class FullOrderInfoResponse {
    private Long id;
    private User user;
    private double totalPrice;
    private String shippingAddres;
    private LocalDateTime deliveredAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemResponse> orderItems;

    public FullOrderInfoResponse() {
    }

    public FullOrderInfoResponse(
            Long id, User user, double totalPrice, String shippingAddres, LocalDateTime deliveredAt,
            LocalDateTime createdAt, LocalDateTime updatedAt, List<OrderItemResponse> orderItems
    ) {
        this.id = id;
        this.user = user;
        this.totalPrice = totalPrice;
        this.shippingAddres = shippingAddres;
        this.deliveredAt = deliveredAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orderItems = orderItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getShippingAddres() {
        return shippingAddres;
    }

    public void setShippingAddres(String shippingAddres) {
        this.shippingAddres = shippingAddres;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemResponse> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FullOrderInfoResponse that = (FullOrderInfoResponse) o;
        return Double.compare(totalPrice, that.totalPrice) == 0 && Objects.equals(id, that.id) &&
               Objects.equals(user, that.user) && Objects.equals(shippingAddres, that.shippingAddres) &&
               Objects.equals(deliveredAt, that.deliveredAt) && Objects.equals(createdAt, that.createdAt) &&
               Objects.equals(updatedAt, that.updatedAt) && Objects.equals(orderItems, that.orderItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, totalPrice, shippingAddres, deliveredAt, createdAt, updatedAt, orderItems);
    }

    @Override
    public String toString() {
        return "FullOrderInfoResponse{" + "id=" + id + ", user=" + user + ", totalPrice=" + totalPrice +
               ", shippingAddres='" + shippingAddres + '\'' + ", deliveredAt=" + deliveredAt + ", createdAt=" +
               createdAt + ", updatedAt=" + updatedAt + ", orderItems=" + orderItems + '}';
    }
}
