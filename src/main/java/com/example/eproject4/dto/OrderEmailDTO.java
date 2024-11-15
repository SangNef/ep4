package com.example.eproject4.dto;

import java.util.List;

public class OrderEmailDTO {
    private String userEmail;
    private String shippingAddress;
    private List<ProductInfo> products;
    private double totalPrice;

    public OrderEmailDTO() {
    }

    public OrderEmailDTO(String userEmail, String shippingAddress, List<ProductInfo> products, double totalPrice) {
        this.userEmail = userEmail;
        this.shippingAddress = shippingAddress;
        this.products = products;
        this.totalPrice = totalPrice;
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
