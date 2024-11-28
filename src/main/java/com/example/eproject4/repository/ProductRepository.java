package com.example.eproject4.repository;

import com.example.eproject4.model.Product;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
        List<Product> findByCategory(String category);

        List<Product> findByStatus(boolean status);

        List<Product> findByCategoryAndStatus(String category, boolean status);

        List<Product> findByNameContainingIgnoreCase(String name, Sort sort);

        @Query("SELECT p FROM Product p WHERE " +
                        "(:minPrice IS NULL OR p.price >= :minPrice) " +
                        "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
                        "AND (:name IS NULL OR p.name LIKE %:name%) " +
                        "AND (:category IS NULL OR p.category = :category) " +
                        "AND (:status IS NULL OR p.status = :status)" +
                        "ORDER BY p.id DESC")
        List<Product> findByFilters(@Param("minPrice") Double minPrice,
                        @Param("maxPrice") Double maxPrice,
                        @Param("name") String name,
                        @Param("category") String category,
                        @Param("status") Boolean status);

        @Query("SELECT p FROM Product p WHERE " +
                        "p.category = :category " +
                        "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
                        "AND p.status = true")
        List<Product> findByCategoryAndMaxPrice(@Param("category") String category,
                        @Param("maxPrice") Double maxPrice,
                        Sort sort);

}
