package com.georgeradu.bookstore.repository;

import com.georgeradu.bookstore.model.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {
    List<OrderInfo> findAllByUserId(Long id);
}
