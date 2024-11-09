package com.example.eproject4.service;

import com.example.eproject4.model.Order;
import com.example.eproject4.model.OrderDetail;
import com.example.eproject4.model.Product;
import com.example.eproject4.repository.OrderRepository;
import com.example.eproject4.repository.ProductRepository;

import org.hibernate.engine.internal.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order createOrder(Order order) {
        // Save the order first
        Order savedOrder = orderRepository.save(order);

        // Loop through each order detail to update the product quantity
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            // Load the latest Product data from the database
            Product product = productRepository.findById(orderDetail.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            // Ensure qty is not null before performing subtraction
            Integer productQty = product.getQty() != null ? product.getQty() : 0;

            // Calculate new quantity
            int newQuantity = productQty - orderDetail.getQty();
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Insufficient quantity for product ID: " + product.getId());
            }

            // Update and save the product quantity
            product.setQty(newQuantity);
            productRepository.save(product);
        }

        return savedOrder;
    }

    // Get a list of all orders
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
    

    // Get an order by ID
    public Order getOrderById(int id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order updateOrderStatus(int id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null && order.getStatus() < 4) { // Giả sử status tối đa là 4
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

    public Page<Order> getOrderByUserId(int userId, PageRequest pageRequest) {
        return orderRepository.findByUserId(userId, pageRequest);
    }

    public Order updateRentEnd(int orderId, LocalDate rentEnd, int debt) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            // Cập nhật ngày kết thúc thuê
            order.setRentEnd(rentEnd);
            
            // Cập nhật debt: cộng thêm giá trị debt từ request vào debt cũ
            int newDebt = order.getDebt() + debt;
            order.setDebt(newDebt); // Cập nhật giá trị debt mới
            
            return orderRepository.save(order); // Lưu thay đổi vào cơ sở dữ liệu
        }
        return null;
    }    
}
