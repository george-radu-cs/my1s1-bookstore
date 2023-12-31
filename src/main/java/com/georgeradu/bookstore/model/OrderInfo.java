package com.georgeradu.bookstore.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "order_info")
public class OrderInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @Column(name = "delivered_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deliveredAt;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deletedAt;

    public OrderInfo() {
    }

    public OrderInfo(Long id) {
        this.id = id;
    }

    public OrderInfo(
            Long id, User user, double totalPrice, String shippingAddress, LocalDateTime deliveredAt,
            LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt
    ) {
        this.id = id;
        this.user = user;
        this.totalPrice = totalPrice;
        this.shippingAddress = shippingAddress;
        this.deliveredAt = deliveredAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
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

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
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

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderInfo orderInfo = (OrderInfo) o;
        return Double.compare(totalPrice, orderInfo.totalPrice) == 0 && Objects.equals(id, orderInfo.id) &&
               Objects.equals(user, orderInfo.user) && Objects.equals(shippingAddress, orderInfo.shippingAddress) &&
               Objects.equals(deliveredAt, orderInfo.deliveredAt) && Objects.equals(createdAt, orderInfo.createdAt) &&
               Objects.equals(updatedAt, orderInfo.updatedAt) && Objects.equals(deletedAt, orderInfo.deletedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, totalPrice, shippingAddress, deliveredAt, createdAt, updatedAt, deletedAt);
    }

    @Override
    public String toString() {
        return "OrderInfo{" + "id=" + id + ", user=" + user + ", totalPrice=" + totalPrice + ", shippingAddress='" +
               shippingAddress + '\'' + ", deliveredAt=" + deliveredAt + ", createdAt=" + createdAt + ", updatedAt=" +
               updatedAt + ", deletedAt=" + deletedAt + '}';
    }
}
