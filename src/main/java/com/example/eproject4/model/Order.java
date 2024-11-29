package com.example.eproject4.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

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
    @JoinColumn(name = "ward_id", nullable = true)
    private Ward ward;

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType type; // Type of order: buy or rent

    // Thay thế `rentDay` bằng `rentStart` và `rentEnd`
    @Column(name = "rent_start", nullable = true)
    private LocalDate rentStart;

    @Column(name = "rent_end", nullable = true)
    private LocalDate rentEnd;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails; // One-to-many relationship with OrderDetail

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefundImage> refundImages;

    public List<RefundImage> getRefundImages() {
        return refundImages;
    }

    public void setRefundImages(List<RefundImage> refundImages) {
        this.refundImages = refundImages;
    }

    @Enumerated(EnumType.STRING)
    private Payment payment;

    @Column(nullable = true)
    private String phone;

    @Column(nullable = true)
    private String address;

    @Column(columnDefinition = "TINYINT DEFAULT 0", nullable = false)
    private int status = 0; // Default value

    private int price; // Added price field

    private int deposit; // Added deposit field

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int debt = 0;

    // add column return_by: string 10, nullable
    @Column(name = "return_by", nullable = true, length = 10)
    private String returnBy;

    public Order() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

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

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPrice() {
        return price;
    } // Getter for price

    public void setPrice(int price) {
        this.price = price;
    } // Setter for price

    public int getDeposit() {
        return deposit;
    } // Getter for deposit

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    } // Setter for deposit

    public int getDebt() {
        return debt;
    }

    public void setDebt(int debt) {
        this.debt = debt;
    }

    public enum Payment {
        CASH,
        PAY
    }

    public enum OrderType {
        BUY,
        RENT
    }

    public String getReturnBy() {
        return returnBy;
    }

    public void setReturnBy(String returnBy) {
        this.returnBy = returnBy;
    }
}
