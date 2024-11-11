package com.example.eproject4.service;

import com.example.eproject4.model.Order;
import com.example.eproject4.model.OrderDetail;
import com.example.eproject4.model.Product;
import com.example.eproject4.model.User;
import com.example.eproject4.repository.OrderRepository;
import com.example.eproject4.repository.ProductRepository;
import com.example.eproject4.repository.UserRepository;

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

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    public Order createOrder(Order order) {

        Order savedOrder = orderRepository.save(order);

        for (OrderDetail orderDetail : order.getOrderDetails()) {
            Product product = productRepository.findById(orderDetail.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            int newQuantity = (product.getQty() != null ? product.getQty() : 0) - orderDetail.getQty();
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Insufficient quantity for product ID: " + product.getId());
            }

            product.setQty(newQuantity);
            productRepository.save(product);
        }

        if (savedOrder.getUser() != null) {
            User fullUser = userRepository.findById(savedOrder.getUser().getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            savedOrder.setUser(fullUser);

            String userEmail = fullUser.getEmail();
            String subject = "Order Confirmation #" + savedOrder.getId();
            String body = "Thank you for your order! Your order ID is " + savedOrder.getId();

            emailService.sendEmail(userEmail, subject, body);
            System.out.println("Order confirmation email sent to: " + userEmail);
        }

        return savedOrder;
    }

    // Get a list of all orders
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Page<Order> getOrdersByStatus(Integer status, Pageable pageable) {
        return orderRepository.findByStatus(status, pageable); // Implement this in the repository
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
