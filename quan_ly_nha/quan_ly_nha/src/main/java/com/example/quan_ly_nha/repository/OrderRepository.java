package com.example.quan_ly_nha.repository;
import com.example.quan_ly_nha.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);
    List<Order> findByUserId(Long userId);
    List<Order> findByTableId(Long tableId);
    List<Order> findByStatus(Order.OrderStatus status);
    List<Order> findByOrderTimeBetween(LocalDateTime start, LocalDateTime end);
}