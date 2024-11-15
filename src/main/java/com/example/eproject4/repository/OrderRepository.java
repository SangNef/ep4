package com.example.eproject4.repository;

import com.example.eproject4.model.Order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findByUserId(int userId, Pageable pageable);
    Page<Order> findByStatus(Integer status, Pageable pageable);
    Page<Order> findByType(Order.OrderType type, Pageable pageable);  // Accept OrderType enum instead of String
    Page<Order> findByStatusAndType(Integer status, Order.OrderType type, Pageable pageable);  // Accept OrderType enum

    long countByStatus(int status);

    @Query("SELECT SUM(o.price) FROM Order o WHERE o.status = 4")
    int sumPriceByStatus(int status);

    long countByType(Order.OrderType type);
}
