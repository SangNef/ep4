package com.example.eproject4.repository;

import com.example.eproject4.model.Order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findByUserId(int userId, PageRequest pageRequest);
}

