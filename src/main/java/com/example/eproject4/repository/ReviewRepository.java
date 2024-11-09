package com.example.eproject4.repository;

import com.example.eproject4.model.Product;
import com.example.eproject4.model.Review;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    // Additional query methods can be defined here if needed
    List<Review> findByOrderDetail_Product(Product product);
}
