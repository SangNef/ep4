package com.example.eproject4.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "OrderDetails")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // Reference to the parent Order

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // Reference to the Product

    private int qty; // Quantity of the product in the order
    private int price; // Price of the product

    public OrderDetail() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
}
