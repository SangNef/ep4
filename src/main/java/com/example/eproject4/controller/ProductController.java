package com.example.eproject4.controller;

import com.example.eproject4.model.Product;
import com.example.eproject4.service.ProductService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    // 3. Get Product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Update product by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody Product updatedProduct) {
        Product product = productService.updateProduct(id, updatedProduct);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 5. Delete Product
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/random")
    public ResponseEntity<List<Product>> getRandomProducts() {
        List<Product> randomProducts = productService.getRandomProducts(4);
        return ResponseEntity.ok(randomProducts);
    }

    @GetMapping("/category/fruit-tree")
    public ResponseEntity<List<Product>> getFruitTrees() {
        return ResponseEntity.ok(productService.getFruitTrees());
    }

    @GetMapping("/category/flowering-tree")
    public ResponseEntity<List<Product>> getFloweringTrees() {
        return ResponseEntity.ok(productService.getFloweringTrees());
    }

    @GetMapping("/category/shade-tree")
    public ResponseEntity<List<Product>> getShadeTrees() {
        return ResponseEntity.ok(productService.getShadeTrees());
    }

    @GetMapping("/category/ornamental-tree")
    public ResponseEntity<List<Product>> getOrnamentalTrees() {
        return ResponseEntity.ok(productService.getOrnamentalTrees());
    }

    @GetMapping("/category/evergreen-tree")
    public ResponseEntity<List<Product>> getEvergreenTrees() {
        return ResponseEntity.ok(productService.getEvergreenTrees());
    }
}
