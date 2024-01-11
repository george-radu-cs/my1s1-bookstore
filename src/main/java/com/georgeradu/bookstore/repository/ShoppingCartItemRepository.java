package com.georgeradu.bookstore.repository;

import com.georgeradu.bookstore.model.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {
    List<ShoppingCartItem> findAllByUserId(Long userId);

    void deleteAllByUserId(Long id);
}
