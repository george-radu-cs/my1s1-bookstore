package com.georgeradu.bookstore.service;

import com.georgeradu.bookstore.dto.ReviewRequest;
import com.georgeradu.bookstore.model.Review;

import java.util.List;

public interface ReviewService {
    Review getReview(Long id);
    List<Review> getReviewsForBook(Long bookId);
    List<Review> getReviewsForUser(Long userId);
    Review createReview(ReviewRequest request, String userEmail);
    Review updateReview(Long id, ReviewRequest request, String userEmail);
    void deleteReview(Long id, String userEmail);
}
