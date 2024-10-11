package com.example.eproject4.controller;

import com.example.eproject4.model.Product;
import com.example.eproject4.service.ProductService;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String showProductList(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/product/index"; // Đảm bảo tệp index.html tồn tại ở đây
    }

    @GetMapping("/create")
    public String showCreateProductForm() {
        return "admin/product/create"; // Đảm bảo tệp create.html tồn tại ở đây
    }

    @PostMapping("/create")
    public String createProduct(
            @RequestParam("name") String name,
            @RequestParam("price") Double price,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("qty") Integer qty,
            @RequestParam("image") MultipartFile imageFile) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setCategory(category);
        product.setQty(qty);

        if (!imageFile.isEmpty()) {
            String imageUrl = uploadImage(imageFile);
            if (imageUrl != null) {
                product.setImage(imageUrl); // Lưu URL của hình ảnh vào đối tượng Product
            }
        }
        
        // Kiểm tra xem sản phẩm có tên tệp hình ảnh hay không
        System.out.println("Product image: " + product.getImage());

        productService.createProduct(product);
        return "redirect:/admin/product"; // Chuyển hướng về danh sách sản phẩm
    }

    private String uploadImage(MultipartFile image) {
        // Lấy tên gốc của tệp tin
        String originalFilename = image.getOriginalFilename();
        
        // Tạo đường dẫn lưu trữ hình ảnh (sử dụng đường dẫn tuyệt đối)
        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/";
        
        // Tạo tên mới cho tệp (có thể dùng System.currentTimeMillis() để đảm bảo tính duy nhất)
        String newFilename = System.currentTimeMillis() + "_" + originalFilename;
        
        try {
            // Tạo thư mục nếu chưa tồn tại
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            // Lưu tệp tin vào thư mục
            File file = new File(directory, newFilename);
            image.transferTo(file);
            
            // Trả về đường dẫn tương đối đến hình ảnh đã lưu
            return "/images/" + newFilename; // Lưu ý đường dẫn tương đối để có thể truy cập từ front-end
        
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Hoặc ném ra ngoại lệ tùy theo yêu cầu
        }
    }
}
