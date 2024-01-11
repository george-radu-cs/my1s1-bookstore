package com.georgeradu.bookstore.service;

import com.georgeradu.bookstore.exception.EntityNotFoundException;
import com.georgeradu.bookstore.exception.InvalidUserAccessException;
import com.georgeradu.bookstore.model.OrderInfo;
import com.georgeradu.bookstore.model.OrderItem;
import com.georgeradu.bookstore.repository.OrderInfoRespository;
import com.georgeradu.bookstore.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final ShoppingCartItemService shoppingCartItemService;
    private final UserService userService;
    private final BookService bookService;
    private final OrderInfoRespository orderInfoRespository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(
            ShoppingCartItemService shoppingCartItemService, UserService userService,
            BookService bookService, OrderInfoRespository orderInfoRespository,
            OrderItemRepository orderItemRepository
    ) {
        this.shoppingCartItemService = shoppingCartItemService;
        this.userService = userService;
        this.bookService = bookService;
        this.orderInfoRespository = orderInfoRespository;
        this.orderItemRepository = orderItemRepository;
    }

    public OrderInfo getOrderInfoById(Long id) {
        return orderInfoRespository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order with id " + id));
    }

    public OrderInfo getUserOrderInfoById(String userEmail, Long id) {
        var user = userService.getUserByEmail(userEmail);
        var orderInfo = orderInfoRespository.findById(id).orElseThrow();
        if (!orderInfo.getUser().getId().equals(user.getId())) {
            throw new InvalidUserAccessException(
                    "User " + user.getId() + " cannot access order info " + orderInfo.getId());
        }

        return getOrderInfoById(id);
    }

    public List<OrderItem> getOrderItemsByOrderInfoId(Long orderInfoId) {
        return orderItemRepository.findAllByOrderInfoId(orderInfoId);
    }

    public List<OrderItem> getUserOrderItemsByOrderInfoId(String userEmail, Long orderInfoId) {
        var user = userService.getUserByEmail(userEmail);
        var orderInfo = orderInfoRespository.findById(orderInfoId).orElseThrow();
        if (!orderInfo.getUser().getId().equals(user.getId())) {
            throw new InvalidUserAccessException(
                    "User " + user.getId() + " cannot access order info " + orderInfo.getId());
        }

        return getOrderItemsByOrderInfoId(orderInfoId);
    }

    public List<OrderInfo> getUserOrderHistory(String userEmail) {
        var user = userService.getUserByEmail(userEmail);
        return orderInfoRespository.findAllByUserId(user.getId());
    }

    public List<OrderInfo> getOrderHistoryForUserById(Long userId) {
        var user = userService.getUser(userId);
        return orderInfoRespository.findAllByUserId(user.getId());
    }

    public OrderInfo saveUserShoppinCartAsOrder(String userEmail, String shippingAddress) {
        var user = userService.getUserByEmail(userEmail);
        var shoppingCartItems = shoppingCartItemService.getUserShoppingCartByEmail(userEmail);
        var totalPrice = shoppingCartItems.stream().mapToDouble(item -> item.getBook().getPrice() * item.getQuantity()).sum();

        var orderInfo = new OrderInfo();
        orderInfo.setUser(user);
        orderInfo.setTotalPrice(totalPrice);
        orderInfo.setShippingAddress(shippingAddress);
        orderInfo.setDeliveredAt(LocalDateTime.MIN);
        var timestamp = LocalDateTime.now();
        orderInfo.setCreatedAt(timestamp);
        orderInfo.setUpdatedAt(timestamp);

        orderInfo = orderInfoRespository.save(orderInfo);

        for (var shoppingCartItem : shoppingCartItems) {
            var orderItem = new OrderItem();
            orderItem.setOrderInfo(orderInfo);
            orderItem.setBook(shoppingCartItem.getBook());
            orderItem.setQuantity(shoppingCartItem.getQuantity());
            orderItem.setPrice(shoppingCartItem.getBook().getPrice());
            orderItem.setCreatedAt(timestamp);
            orderItem.setUpdatedAt(timestamp);
            orderItemRepository.save(orderItem);
        }

        shoppingCartItemService.deleteAllUserShoppingCartItems(userEmail);

        return orderInfo;
    }

    public OrderInfo setOrderToBeDelivered(Long id) {
        var orderInfo = getOrderInfoById(id);
        orderInfo.setDeliveredAt(LocalDateTime.now());
        return orderInfoRespository.save(orderInfo);
    }
}
