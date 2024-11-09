package com.example.eproject4.service;

import com.example.eproject4.dto.ReviewDTO;
import com.example.eproject4.model.OrderDetail;
import com.example.eproject4.model.Review;
import com.example.eproject4.repository.OrderDetailRepository;
import com.example.eproject4.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Transactional
    public ReviewDTO addReview(int orderDetailId, String comment) {
        // Find the order detail by ID
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new RuntimeException("OrderDetail not found"));

        // Create and save the review
        Review review = new Review();
        review.setOrderDetail(orderDetail);
        review.setComment(comment);
        reviewRepository.save(review);

        // Return a new ReviewDTO with only the fields you want in the response
        return new ReviewDTO(review.getId(), review.getComment());
    }
}
