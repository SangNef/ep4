package com.example.eproject4.dto;

public class ReviewDTO {
    private int id;
    private String comment;

    // Constructor
    public ReviewDTO(int id, String comment) {
        this.id = id;
        this.comment = comment;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
