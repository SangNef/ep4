package com.example.eproject4.dto;

import java.time.LocalDate;
import java.util.List;

public class OrderEmailDTO {
    private String userEmail;
    private String shippingAddress;
    private List<ProductInfo> products;
    private double totalPrice;

    private String username;
    private String fullname;
    // Các trường mới cho thông tin thuê
    private LocalDate rentStart;
    private LocalDate rentEnd;

    public LocalDate getRentStart() {
        return rentStart;
    }

    public void setRentStart(LocalDate rentStart) {
        this.rentStart = rentStart;
    }

    public LocalDate getRentEnd() {
        return rentEnd;
    }

    public void setRentEnd(LocalDate rentEnd) {
        this.rentEnd = rentEnd;
    }

    public OrderEmailDTO() {
    }

    public OrderEmailDTO(String userEmail, String shippingAddress, List<ProductInfo> products, double totalPrice,
    String username, String fullname, LocalDate rentStart, LocalDate rentEnd) {
        this.userEmail = userEmail;
        this.shippingAddress = shippingAddress;
        this.products = products;
        this.totalPrice = totalPrice;
        this.username = username;
        this.fullname = fullname;
        this.rentStart = rentStart;
        this.rentEnd = rentEnd;
        }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<ProductInfo> getProducts() {
        return products;
    }

    public void setProducts(List<ProductInfo> products) {
        this.products = products;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public static class ProductInfo {
        private String productName;
        private int quantity;
        private double price;

        public ProductInfo() {
        }

        public ProductInfo(String productName, int quantity, double price) {
            this.productName = productName;
            this.quantity = quantity;
            this.price = price;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}
