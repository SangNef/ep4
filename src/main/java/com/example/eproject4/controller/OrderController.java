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
import com.example.eproject4.service.ProvinceService;
import com.example.eproject4.service.DistrictService;
import com.example.eproject4.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
                                                     @RequestParam(required = false) Integer status) {
        Page<Order> orders;
        if (status != null) {
            orders = orderService.getOrdersByStatus(status, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        } else {
            orders = orderService.getAllOrders(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        }
        return ResponseEntity.ok(orders);
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
        String debtStr = requestBody.get("debt"); // Nhận giá trị debt từ request body

        if (rentEndStr != null && debtStr != null) {
            LocalDate rentEnd = LocalDate.parse(rentEndStr); // Chuyển đổi chuỗi thành LocalDate
            int debt = Integer.parseInt(debtStr); // Chuyển đổi chuỗi thành int

            Order updatedOrder = orderService.updateRentEnd(id, rentEnd, debt); // Cập nhật rentEnd và debt
            if (updatedOrder != null) {
                return ResponseEntity.ok(updatedOrder);
            }
        }
        return ResponseEntity.notFound().build();
    }
}
