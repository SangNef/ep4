package com.example.eproject4.service;

import com.example.eproject4.dto.ProductDTO;
import com.example.eproject4.model.Product;
import com.example.eproject4.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Product createProduct(Product product) {
        product = productRepository.save(product);
        return product;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll(Sort.by(Sort.Direction.DESC, "id")); // Sort by createdDate in descending order
    }

    public List<Product> getProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name, Sort.by(Sort.Direction.DESC, "id"));
    }

    public List<Product> getAllActiveProducts() {
        return productRepository.findByStatus(true);
    }

    public Product getProductById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    @Transactional
    public Product updateProduct(int id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setQty(updatedProduct.getQty());
        existingProduct.setCanRent(updatedProduct.getCanRent());
        existingProduct.setRentPrice(updatedProduct.getRentPrice());

        existingProduct.setImages(updatedProduct.getImages());

        return productRepository.save(existingProduct); // Save the updated product
    }

    public Product toggleProductStatus(Integer id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Toggle the status
        existingProduct.setStatus(!existingProduct.getStatus());
        return productRepository.save(existingProduct); // Save the updated product
    }

    @Transactional
    public void deleteProduct(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Soft delete by setting status to 0 (inactive)
        product.setStatus(false); // Assuming status is an integer field for active/inactive
        productRepository.save(product);
    }

    public List<Product> getRandomProducts(int count) {
        List<Product> allProducts = productRepository.findAll();
        Random random = new Random();
        return random.ints(0, allProducts.size())
                .distinct()
                .limit(count)
                .mapToObj(allProducts::get)
                .toList();
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryAndStatus(category, true);
    }

    public List<Product> getFruitTrees() {
        return getProductsByCategory("Fruit Tree");
    }

    public List<Product> getFloweringTrees() {
        return getProductsByCategory("Flowering Tree");
    }

    public List<Product> getShadeTrees() {
        return getProductsByCategory("Shade Tree");
    }

    public List<Product> getOrnamentalTrees() {
        return getProductsByCategory("Ornamental Tree");
    }

    public List<Product> getEvergreenTrees() {
        return getProductsByCategory("Evergreen Tree");
    }

    public ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        productDTO.setCategory(product.getCategory());
        productDTO.setQty(product.getQty());
        productDTO.setStatus(product.getStatus());
        productDTO.setCanRent(product.getCanRent());
        productDTO.setRentPrice(product.getRentPrice());
        productDTO.setImages(product.getImages());
        return productDTO;
    }
}
