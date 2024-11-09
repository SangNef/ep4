package com.example.eproject4.repository;

import com.example.eproject4.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    // No additional methods are required here; findById is already provided by JpaRepository
}
