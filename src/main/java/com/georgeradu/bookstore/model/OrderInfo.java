package com.georgeradu.bookstore.model;

import jakarta.persistence.*;

import java.util.Date;
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
    private Date deliveredAt;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    public OrderInfo() {
    }

    public OrderInfo(Long id) {
        this.id = id;
    }

    public OrderInfo(
            Long id, User user, double totalPrice, String shippingAddress, Date deliveredAt, Date createdAt,
            Date updatedAt, Date deletedAt
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

    public Date getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(Date deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
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
