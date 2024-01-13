package com.georgeradu.bookstore.dto;

import com.georgeradu.bookstore.model.OrderInfo;
import com.georgeradu.bookstore.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class OrderInfoResponse {
    private Long id;
    private Long userId;
    private double totalPrice;
    private String shippingAddress;
    private OrderStatus status;
    private LocalDateTime deliveredAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderInfoResponse() {
    }

    public OrderInfoResponse(
            Long id, Long userId, double totalPrice, String shippingAddress, OrderStatus status,
            LocalDateTime deliveredAt, LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.shippingAddress = shippingAddress;
        this.status = status;
        this.deliveredAt = deliveredAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public OrderInfoResponse(OrderInfo response) {
        this.id = response.getId();
        this.userId = response.getUser().getId();
        this.totalPrice = response.getTotalPrice();
        this.shippingAddress = response.getShippingAddress();
        this.status = response.getStatus();
        this.deliveredAt = response.getDeliveredAt();
        this.createdAt = response.getCreatedAt();
        this.updatedAt = response.getUpdatedAt();
    }

    public static List<OrderInfoResponse> fromList(List<OrderInfo> response) {
        return response.stream().map(OrderInfoResponse::new).toList();
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

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderInfoResponse that = (OrderInfoResponse) o;
        return Double.compare(totalPrice, that.totalPrice) == 0 && Objects.equals(id, that.id) &&
               Objects.equals(userId, that.userId) && Objects.equals(shippingAddress, that.shippingAddress) &&
               status == that.status && Objects.equals(deliveredAt, that.deliveredAt) &&
               Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, totalPrice, shippingAddress, status, deliveredAt, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "OrderInfoResponse{" + "id=" + id + ", userId=" + userId + ", totalPrice=" + totalPrice +
               ", shippingAddress='" + shippingAddress + '\'' + ", status=" + status + ", deliveredAt=" + deliveredAt +
               ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
}
