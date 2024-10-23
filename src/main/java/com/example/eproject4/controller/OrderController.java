package com.example.eproject4.controller;

import com.example.eproject4.model.Order;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.ok(createdOrder);
    }

    // Endpoint to get all orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
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
}
