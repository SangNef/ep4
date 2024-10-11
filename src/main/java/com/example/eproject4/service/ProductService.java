package com.example.eproject4.service;

import com.example.eproject4.model.Product;
import com.example.eproject4.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Integer id, Product productDetails) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(productDetails.getName());
                    product.setPrice(productDetails.getPrice());
                    product.setDescription(productDetails.getDescription());
                    product.setCategory(productDetails.getCategory());
                    product.setQty(productDetails.getQty());
                    product.setImage(productDetails.getImage());
                    return productRepository.save(product);
                }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void deleteProduct(Integer id) {
        productRepository.findById(id)
                .ifPresent(product -> {
                    product.setDeletedAt(new Timestamp(System.currentTimeMillis()));
                    productRepository.save(product);
                });
    }
}
