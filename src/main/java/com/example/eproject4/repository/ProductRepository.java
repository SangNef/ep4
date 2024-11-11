package com.example.eproject4.repository;

import com.example.eproject4.model.Product;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategory(String category);
    List<Product> findByStatus(boolean status);
    List<Product> findByCategoryAndStatus(String category, boolean status);
    List<Product> findByNameContainingIgnoreCase(String name, Sort sort);
}
