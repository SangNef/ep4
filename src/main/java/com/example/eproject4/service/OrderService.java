package com.example.eproject4.service;

import com.example.eproject4.model.Order;
import com.example.eproject4.repository.OrderRepository;

import org.hibernate.engine.internal.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.data.domain.Sort;

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
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    // Get an order by ID
    public Order getOrderById(int id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order updateOrderStatus(int id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null && order.getStatus() < 4) {  // Giả sử status tối đa là 4
            order.setStatus(order.getStatus() + 1);
            return orderRepository.save(order);
        }
        return null;
    }

    public Order cancelOrder(int id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null && order.getStatus() == 0) {
            order.setStatus(5);
            return orderRepository.save(order);
        }
        return null;
    }

    public List<Order> getOrderByUserId(int userId) {
        return orderRepository.findByUserId(userId);
    }
}
