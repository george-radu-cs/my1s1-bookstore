package com.georgeradu.bookstore.service;

import com.georgeradu.bookstore.model.OrderInfo;
import com.georgeradu.bookstore.model.OrderItem;

import java.util.List;

public interface OrderService {
    OrderInfo getOrderInfoById(Long id);
    OrderInfo getUserOrderInfoById(String userEmail, Long id);
    List<OrderItem> getOrderItemsByOrderInfoId(Long orderInfoId);
    List<OrderItem> getUserOrderItemsByOrderInfoId(String userEmail, Long orderInfoId);
    List<OrderInfo> getUserOrderHistory(String userEmail);
    List<OrderInfo> getOrderHistoryForUserById(Long userId);
    OrderInfo saveUserShoppingCartAsOrder(String userEmail, String shippingAddress);
    OrderInfo setOrderToBeDelivered(Long id);
    OrderInfo cancelOrder(String userEmail, Long id);
}
