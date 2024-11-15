package com.example.eproject4.service;

import com.example.eproject4.dto.OrderEmailDTO;
import com.example.eproject4.model.Order;
import com.example.eproject4.model.OrderDetail;
import com.example.eproject4.model.Product;
import com.example.eproject4.model.User;
import com.example.eproject4.repository.OrderDetailRepository;
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

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public Order createOrder(Order order) {
        Order savedOrder = orderRepository.save(order);

        for (OrderDetail orderDetail : order.getOrderDetails()) {
            orderDetail.setOrder(savedOrder);

            Product product = productRepository.findById(orderDetail.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            int newQuantity = (product.getQty() != null ? product.getQty() : 0) - orderDetail.getQty();
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Insufficient quantity for product ID: " + product.getId());
            }

            product.setQty(newQuantity);
            productRepository.save(product);

            orderDetail.setProduct(product);
            orderDetailRepository.save(orderDetail);
        }

        if (savedOrder.getUser() != null) {
            User fullUser = userRepository.findById(savedOrder.getUser().getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            savedOrder.setUser(fullUser);

            OrderEmailDTO orderEmailDTO = buildOrderEmailDTO(savedOrder);
            sendOrderConfirmationEmail(orderEmailDTO);
        }

        return savedOrder;
    }

    public OrderEmailDTO buildOrderEmailDTO(Order order) {
        OrderEmailDTO dto = new OrderEmailDTO();
        dto.setUserEmail(order.getUser().getEmail());
        dto.setShippingAddress(order.getAddress() != null ? order.getAddress() : "No address provided");

        final double[] totalPrice = { 0 };
        List<OrderEmailDTO.ProductInfo> productInfoList = order.getOrderDetails().stream().map(orderDetail -> {
            OrderEmailDTO.ProductInfo productInfo = new OrderEmailDTO.ProductInfo();
            Product product = orderDetail.getProduct();

            productInfo.setProductName(product.getName());
            productInfo.setQuantity(orderDetail.getQty());
            productInfo.setPrice(orderDetail.getPrice());

            totalPrice[0] += orderDetail.getPrice() * orderDetail.getQty();
            return productInfo;
        }).toList();

        dto.setProducts(productInfoList);
        dto.setTotalPrice(totalPrice[0]);

        return dto;
    }

    private void sendOrderConfirmationEmail(OrderEmailDTO orderEmailDTO) {
        String subject = "Order Confirmation #" + orderEmailDTO.getUserEmail();
        StringBuilder body = new StringBuilder();

        body.append("Thank you for your order!\n\n")
                .append("Shipping Address: ").append(orderEmailDTO.getShippingAddress()).append("\n\n")
                .append("Order Details:\n");

        // Duyệt qua các sản phẩm trong đơn hàng
        for (OrderEmailDTO.ProductInfo product : orderEmailDTO.getProducts()) {
            body.append("- ").append(product.getProductName())
                    .append(", Quantity: ").append(product.getQuantity())
                    .append(", Price: $").append(product.getPrice());
            body.append("\n");
        }

        body.append("\nTotal Price: $").append(orderEmailDTO.getTotalPrice());

        // Gửi email
        emailService.sendEmail(orderEmailDTO.getUserEmail(), subject, body.toString());
        System.out.println("Order confirmation email sent to: " + orderEmailDTO.getUserEmail());
    }

    // Get a list of all orders
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Page<Order> getOrdersByStatus(Integer status, Pageable pageable) {
        return orderRepository.findByStatus(status, pageable);
    }

    public Page<Order> getOrdersByStatusAndType(Integer status, Order.OrderType type, Pageable pageable) {
        return orderRepository.findByStatusAndType(status, type, pageable); // Pass the enum directly
    }

    public Page<Order> getOrdersByType(Order.OrderType type, Pageable pageable) {
        return orderRepository.findByType(type, pageable); // Pass the enum directly
    }

    // Get an order by ID
    public Order getOrderById(int id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order updateOrderStatus(int id) {
        return orderRepository.findById(id).map(order -> {
            int currentStatus = order.getStatus();

            // Check if status is within the allowed range to be updated
            if ((currentStatus >= 0 && currentStatus < 4) || (currentStatus > 5 && currentStatus < 12)) {
                order.setStatus(currentStatus + 1);
                return orderRepository.save(order);
            }

            return order; // Returning unchanged order if status update is not allowed
        }).orElse(null);
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
