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
            if (savedOrder.getType() == Order.OrderType.RENT) {
                orderEmailDTO.setRentStart(savedOrder.getRentStart());
                orderEmailDTO.setRentEnd(savedOrder.getRentEnd());
            }
            sendOrderConfirmationEmail(orderEmailDTO);
        }
    
        return savedOrder;
    }
    
    public OrderEmailDTO buildOrderEmailDTO(Order order) {
        OrderEmailDTO dto = new OrderEmailDTO();
        dto.setUserEmail(order.getUser().getEmail());
        dto.setUsername(order.getUser().getUsername());
        dto.setFullname(order.getUser().getFullname());
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
    
        // Kiểm tra nếu Order là loại RENT, thêm thông tin rentStart và rentEnd
        if (order.getType() == Order.OrderType.RENT) {
            dto.setRentStart(order.getRentStart());
            dto.setRentEnd(order.getRentEnd());
        }
    
        return dto;
    }
    
    private void sendOrderConfirmationEmail(OrderEmailDTO orderEmailDTO) {
        String subject = "Order Confirmation #" + orderEmailDTO.getUserEmail();
    
        // Tạo nội dung HTML
        StringBuilder body = new StringBuilder();
        body.append("<html>")
            .append("<head>")
            .append("<style>")
            .append("@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap');")
            .append("body { font-family: 'Roboto', Arial, sans-serif; text-align: center; margin: 0; padding: 20px; }")
            .append(".container { width: 100%; max-width: 300px; margin: 0 auto; background-color: #f9f9f9; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }")
            .append(".header { color: #007b00; font-size: 50px; font-weight: bold; margin-bottom: 5px; text-align: center; }")
            .append(".subheader { font-size: 14px; color: #555; margin-bottom: 20px;text-align: center;font-weight: bold; }")
            .append("table { width: 100%; border-collapse: collapse; margin: 20px 0; }")
            .append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }")
            .append("th { background-color: #007b00; color: white; }")
            .append(".section-title { font-size: 18px; margin: 20px 0 10px; font-weight: bold; }")
            .append("p { margin: 5px 0; }")
            .append("strong { font-weight: bold; }")
            .append("h3 { text-align: center;padding-top:5px;font-size: 30px }")
            .append("</style>")
            .append("</head>")
            .append("<body>")
            .append("<div class='container'>")
            .append("<div class='header'>Plant Store</div>")
            .append("<div class='subheader'>Grow your world with our plants!</div>")
            .append("<h3>Thank you for your order !</h3>");
    
        // Bảng chứa thông tin sản phẩm
        body.append("<div class='section-title'>Order Details</div>")
            .append("<table>")
            .append("<thead>")
            .append("<tr>")
            .append("<th>Product Name</th>")
            .append("<th>Quantity</th>")
            .append("<th>Price</th>")
            .append("</tr>")
            .append("</thead>")
            .append("<tbody>");
        
        for (OrderEmailDTO.ProductInfo product : orderEmailDTO.getProducts()) {
            body.append("<tr>")
                .append("<td>").append(product.getProductName()).append("</td>")
                .append("<td>").append(product.getQuantity()).append("</td>")
                .append("<td>$").append(String.format("%.2f", product.getPrice())).append("</td>")
                .append("</tr>");
        }
        body.append("</tbody>")
            .append("</table>")
            .append("<p><strong>Total Price: $").append(String.format("%.2f", orderEmailDTO.getTotalPrice())).append("</strong></p>");
    
        // Thêm thông tin ngày thuê
        if (orderEmailDTO.getRentStart() != null && orderEmailDTO.getRentEnd() != null) {
            body.append("<div class='section-title'>Rental Period</div>")
                .append("<p>Rent Start: ").append(orderEmailDTO.getRentStart()).append("</p>")
                .append("<p>Rent End: ").append(orderEmailDTO.getRentEnd()).append("</p>");
        }
    
        // Thông tin khách hàng
        body.append("<div class='section-title'>Customer Details</div>")
            .append("<p>Email: ").append(orderEmailDTO.getUserEmail()).append("</p>")
            .append("<p>Username: ").append(orderEmailDTO.getUsername()).append("</p>")
            .append("<p>Full Name: ").append(orderEmailDTO.getFullname()).append("</p>")
            .append("<p>Shipping Address: ").append(orderEmailDTO.getShippingAddress()).append("</p>")
            .append("<footer style='margin-top: 20px; padding-top: 20px; border-top: 1px solid #ddd; font-size: 12px; color: #555; text-align: center;'>")
            .append("If you have any questions, feel free to <a href='mailto:support@plantstore.com'>Plan Store</a>.")
            .append("<br>Plant Store &copy; 2024. All Rights Reserved.")
            .append("<br>8A Ton That Thuyet, My Dinh,Ha Noi , Viet Nam")
            .append("</footer>")
            .append("</div>")
            .append("</body>")
            .append("</html>");

        
            
        // Gửi email
        emailService.sendEmail(orderEmailDTO.getUserEmail(), subject, body.toString(), true);
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

    public Order updateRentEnd(int orderId, LocalDate rentEnd, int deposit) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            // Cập nhật ngày kết thúc thuê
            order.setRentEnd(rentEnd);

            // Cập nhật debt: cộng thêm giá trị debt từ request vào debt cũ
            // int newDebt = order.getDebt() + debt;
            // order.setDebt(newDebt); // Cập nhật giá trị debt mới

            int newDeposit = order.getDeposit() - deposit;
            order.setDeposit(newDeposit);

            return orderRepository.save(order); // Lưu thay đổi vào cơ sở dữ liệu
        }
        return null;
    }

    public Order refundOrder(int id, int refundAmount) {
        return orderRepository.findById(id).map(order -> {
            // Ensure the order can be refunded (e.g., it must not already be refunded)
            if (order.getStatus() != 11) {
                int newPrice = order.getDeposit() - refundAmount;
    
                // Ensure the new price is not negative
                if (newPrice < 0) {
                    throw new IllegalArgumentException("Refund amount exceeds the order price.");
                }
    
                order.setDeposit(newPrice);
                order.setStatus(11); // Update status to 'Refunded'
                return orderRepository.save(order);
            } else {
                throw new IllegalStateException("Order is already refunded.");
            }
        }).orElse(null);
    }    
}
