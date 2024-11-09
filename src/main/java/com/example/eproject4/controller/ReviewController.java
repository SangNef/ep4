package com.example.eproject4.controller;

import com.example.eproject4.dto.ReviewDTO;
import com.example.eproject4.model.Review;
import com.example.eproject4.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/{orderDetailId}")
    public ResponseEntity<ReviewDTO> addReview(
            @PathVariable int orderDetailId,
            @RequestBody ReviewDTO reviewDTO) {

        ReviewDTO createdReview = reviewService.addReview(orderDetailId, reviewDTO.getComment());
        return ResponseEntity.ok(createdReview);
    }
}
