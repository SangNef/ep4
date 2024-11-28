package com.example.eproject4.controller;

import com.example.eproject4.model.Order;
import com.example.eproject4.model.OrderDetail;
import com.example.eproject4.model.Province;
import com.example.eproject4.dto.DistrictDTO;
import com.example.eproject4.dto.ProvinceDTO;
import com.example.eproject4.dto.WardDTO;
import com.example.eproject4.model.District;
import com.example.eproject4.model.Ward;
import com.example.eproject4.service.OrderService;
import com.example.eproject4.repository.OrderRepository;
import com.example.eproject4.service.ProvinceService;
import com.example.eproject4.service.DistrictService;
import com.example.eproject4.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private WardService wardService;
    @Autowired
    private OrderRepository orderRepository;

    // Endpoint to create a new order
    // Endpoint to create a new order
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        // Ensure that the order details are properly linked to the order
        if (order.getOrderDetails() != null) {
            System.out.println("123123");
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                orderDetail.setOrder(order); // Link the order detail to the order
            }
        }

        // Create the order using the service
        Order createdOrder = orderService.createOrder(order);

        return ResponseEntity.ok(createdOrder);
    }

    // Endpoint to get all orders
    @GetMapping
    public ResponseEntity<Page<Order>> getAllOrders(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String type) {
        Page<Order> orders;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        // Handle both status and type filters
        if (status != null && type != null) {
            Order.OrderType orderType = parseOrderType(type); // Convert string type to enum
            orders = orderService.getOrdersByStatusAndType(status, orderType, pageable);
        } else if (status != null) {
            orders = orderService.getOrdersByStatus(status, pageable);
        } else if (type != null) {
            Order.OrderType orderType = parseOrderType(type); // Convert string type to enum
            orders = orderService.getOrdersByType(orderType, pageable);
        } else {
            orders = orderService.getAllOrders(pageable);
        }

        return ResponseEntity.ok(orders);
    }

    // Helper method to parse the type string to OrderType enum
    private Order.OrderType parseOrderType(String type) {
        try {
            return Order.OrderType.valueOf(type.toUpperCase()); // Ensure to convert string to uppercase to match enum
                                                                // names
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid order type: " + type); // Handle invalid enum type
        }
    }

    // Endpoint to get all provinces
    @GetMapping("/provinces")
    public ResponseEntity<List<ProvinceDTO>> getAllProvinces() {
        List<ProvinceDTO> provinces = provinceService.getAllProvinces();
        return ResponseEntity.ok(provinces);
    }

    // Endpoint to get districts by province id
    @GetMapping("/districts/{provinceId}")
    public ResponseEntity<List<DistrictDTO>> getDistrictsByProvince(@PathVariable int provinceId) {
        List<DistrictDTO> districts = districtService.getDistrictsByProvince(provinceId);
        return ResponseEntity.ok(districts);
    }

    // Endpoint to get wards by district id
    @GetMapping("/wards/{districtId}")
    public ResponseEntity<List<WardDTO>> getWardsByDistrict(@PathVariable int districtId) {
        List<WardDTO> wards = wardService.getWardsByDistrict(districtId);
        return ResponseEntity.ok(wards);
    }

    // Endpoint to get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable int id) {
        Order order = orderService.getOrderById(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable int id) {
        Order updatedOrder = orderService.updateOrderStatus(id);
        if (updatedOrder != null) {
            return ResponseEntity.ok(updatedOrder);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<Order> cancelOrder(@PathVariable int id) {
        Order updatedOrder = orderService.cancelOrder(id);
        if (updatedOrder != null) {
            return ResponseEntity.ok(updatedOrder);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getOrdersByUserId(
            @PathVariable int userId,
            @RequestParam(defaultValue = "0") int page, // Default page is 0
            @RequestParam(defaultValue = "10") int size // Default size is 10
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("id")));
        Page<Order> orders = orderService.getOrderByUserId(userId, pageRequest);

        Map<String, Object> response = new HashMap<>();
        response.put("orders", orders.getContent());
        response.put("totalPages", orders.getTotalPages());
        response.put("totalElements", orders.getTotalElements());
        response.put("currentPage", orders.getNumber());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/extend/{id}")
    public ResponseEntity<Order> updateRentEnd(
            @PathVariable int id,
            @RequestBody Map<String, String> requestBody) {
        String rentEndStr = requestBody.get("rentEnd");
        String depositStr = requestBody.get("deposit"); // Nhận giá trị debt từ request body

        if (rentEndStr != null && depositStr != null) {
            LocalDate rentEnd = LocalDate.parse(rentEndStr); // Chuyển đổi chuỗi thành LocalDate
            int deposit = Integer.parseInt(depositStr); // Chuyển đổi chuỗi thành int

            Order updatedOrder = orderService.updateRentEnd(id, rentEnd, deposit); // Cập nhật rentEnd và debt
            if (updatedOrder != null) {
                return ResponseEntity.ok(updatedOrder);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/refund/{id}")
    public ResponseEntity<Order> refundOrder(@PathVariable int id, @RequestBody Map<String, Integer> requestBody) {
        Integer refundAmount = requestBody.get("refundAmount"); // Số tiền refund từ request
    
        if (refundAmount == null || refundAmount < 0) {
            return ResponseEntity.badRequest().body(null); // Trả lỗi nếu không có refundAmount hoặc không hợp lệ
        }
    
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.notFound().build(); // Trả lỗi nếu không tìm thấy order
        }

        int newPrice = order.getDeposit() - refundAmount;
        if (newPrice < 0) {
            return ResponseEntity.badRequest().body(null); // Trả lỗi nếu price âm
        }

        // Cập nhật các thông tin của order
        order.setDeposit(newPrice + order.getDebt()); // price = price + debt
        order.setDebt(0);                          // debt = 0
        order.setStatus(11);                       // status = 11
        Order updatedOrder = orderRepository.save(order);
        return ResponseEntity.ok(updatedOrder);
    }
}
