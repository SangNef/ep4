package com.example.eproject4.service;

import com.example.eproject4.model.Order;
import com.example.eproject4.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Create a new order
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    // Get a list of all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
