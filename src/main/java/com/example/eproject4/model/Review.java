package com.example.eproject4.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_detail_id", nullable = false)
    @JsonIgnore // Ignore this field in the JSON response
    private OrderDetail orderDetail;

    private String comment;

    // rate
    private int rate;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public OrderDetail getOrderDetail() { return orderDetail; }
    public void setOrderDetail(OrderDetail orderDetail) { this.orderDetail = orderDetail; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public int getRate() { return rate; }
    public void setRate(int rate) { this.rate = rate; }
}
