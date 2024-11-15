package com.example.eproject4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eproject4.model.Order;  // Thêm dòng này
import com.example.eproject4.repository.OrderRepository;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/totalOrders")
    public long getTotalOrders() {
        return orderRepository.countByStatus(4); // 1 là trạng thái đã bán
    }

    @GetMapping("/totalRevenue")
    public int getTotalRevenue() {
        return orderRepository.sumPriceByStatus(4); // Sử dụng hàm sumPriceByStatus trong repository
    }

    @GetMapping("/totalBuyOrders")
    public long getTotalBuyOrders() {
        return orderRepository.countByType(Order.OrderType.BUY); // Đếm số đơn hàng với type = BUY
    }

    @GetMapping("/totalRentOrders")
    public long getTotalRentOrders() {
        return orderRepository.countByType(Order.OrderType.RENT); // Đếm số đơn hàng với type = RENT
    }
}
