package com.example.eproject4.service;

import com.example.eproject4.dto.ProductDTO;
import com.example.eproject4.model.Product;
import com.example.eproject4.model.ProductImage;
import com.example.eproject4.repository.ProductRepository;
import com.example.eproject4.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setCategory(productDTO.getCategory());
        product.setQty(productDTO.getQty());
        product.setStatus(productDTO.getStatus());
        product.setCanRent(productDTO.getCanRent());
        product.setRentPrice(productDTO.getRentPrice());

        // Handle product images
        for (String imageUrl : productDTO.getImages()) {
            ProductImage productImage = new ProductImage();
            productImage.setImage(imageUrl);
            productImage.setProduct(product);
            product.getProductImages().add(productImage);
        }

        return productRepository.save(product);
    }

    public Product updateProduct(Integer id, ProductDTO productDTO) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setCategory(productDTO.getCategory());
        product.setQty(productDTO.getQty());
        product.setStatus(productDTO.getStatus());
        product.setCanRent(productDTO.getCanRent());
        product.setRentPrice(productDTO.getRentPrice());
        product.getProductImages().clear(); // Clear existing images

        // Handle product images
        for (String imageUrl : productDTO.getImages()) {
            ProductImage productImage = new ProductImage();
            productImage.setImage(imageUrl);
            productImage.setProduct(product);
            product.getProductImages().add(productImage);
        }

        return productRepository.save(product);
    }

    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
}
