package com.example.eproject4.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "ward_id", nullable = false) // Thêm quan hệ tới Ward
    private Ward ward;

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType type; // Type of order: buy or rent

    @Column(nullable = true)
    private int rentDay; // Number of days for renting

    private int qty;
    private int price;

    @Enumerated(EnumType.STRING)
    private Payment payment;

    private String phone;
    private String address;

    @Column(columnDefinition = "TINYINT DEFAULT 0", nullable = false)
    private int status = 0; // Default value

    public Order() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Ward getWard() { return ward; }
    public void setWard(Ward ward) { this.ward = ward; }

    public Voucher getVoucher() { return voucher; }
    public void setVoucher(Voucher voucher) { this.voucher = voucher; }

    public OrderType getType() { return type; }
    public void setType(OrderType type) { this.type = type; }

    public int getRentDay() { return rentDay; }
    public void setRentDay(int rentDay) { this.rentDay = rentDay; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public enum Payment {
        CASH,
        PAY
    }

    public enum OrderType { // New enum for order type
        BUY,
        RENT
    }
}
