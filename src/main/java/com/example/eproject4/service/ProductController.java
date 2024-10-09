package com.example.eproject4.service;

import com.example.eproject4.model.Product;
import com.example.eproject4.model.ProductImage;
import com.example.eproject4.repository.ProductImageRepository;
import com.example.eproject4.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("content", "products/list");
        model.addAttribute("products", products);
        return "layout/adminLayout"; // Template danh sách sản phẩm
    }

    @GetMapping("/create")
    public String createProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("content", "products/create"); // Template thêm sản phẩm
        return "layout/adminLayout";
    }

    @PostMapping
    public String saveProduct(@ModelAttribute Product product,
            @RequestParam("categoryIds") List<Integer> categoryIds,
            @RequestParam("categoryValues") List<String> categoryValues,
            @RequestParam("images") List<MultipartFile> images) throws IOException {

        productRepository.save(product);

        // Lưu hình ảnh sản phẩm
        for (MultipartFile file : images) {
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR, fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());

                ProductImage productImage = new ProductImage();
                productImage.setProduct(product);
                productImage.setImage(fileName);
                productImageRepository.save(productImage);
            }
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String editProductForm(@PathVariable Integer id, Model model) {
        Product product = productRepository.findById(id).orElseThrow();
        model.addAttribute("product", product);
        model.addAttribute("content", "products/edit");
        return "layout/adminLayout";
    }

    @PostMapping("/{id}")
    public String updateProduct(@PathVariable Integer id,
            @ModelAttribute Product product,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("categoryIds") List<Integer> categoryIds) throws IOException {
        product.setId(id);
        productRepository.save(product);

        // Cập nhật hình ảnh sản phẩm (Nếu có hình ảnh mới)
        for (MultipartFile file : images) {
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR, fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());

                // Lưu thông tin file vào database
                ProductImage productImage = new ProductImage();
                productImage.setProduct(product);
                productImage.setImage(fileName);
                productImageRepository.save(productImage);
            }
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Integer id) {
        productRepository.deleteById(id);
        return "redirect:/admin/products";
    }
}
