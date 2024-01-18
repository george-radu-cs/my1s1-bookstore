package com.georgeradu.bookstore.service;

import com.georgeradu.bookstore.dto.ReviewRequest;
import com.georgeradu.bookstore.exception.DuplicateObjectException;
import com.georgeradu.bookstore.exception.EntityNotFoundException;
import com.georgeradu.bookstore.exception.InvalidUserAccessException;
import com.georgeradu.bookstore.model.Review;
import com.georgeradu.bookstore.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final Clock clock;
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final BookService bookService;

    public ReviewServiceImpl(
            Clock clock, ReviewRepository reviewRepository, UserService userService, BookService bookService
    ) {
        this.clock = clock;
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.bookService = bookService;
    }


    public Review getReview(Long id) throws EntityNotFoundException {
        return reviewRepository.findById(id)
                               .orElseThrow(() -> new EntityNotFoundException("Review not found"));
    }

    public List<Review> getReviewsForBook(Long bookId) {
        bookService.getBook(bookId); // check if book exists
        return reviewRepository.findAllByBookId(bookId);
    }

    public List<Review> getReviewsForUser(Long userId) {
        userService.getUser(userId); // check if user exists
        return reviewRepository.findAllByUserId(userId);
    }


    public Review createReview(ReviewRequest request, String userEmail) throws EntityNotFoundException,
            DuplicateObjectException {
        var user = userService.getUserByEmail(userEmail);
        var book = bookService.getBook(request.getBookId());

        var existentReview = reviewRepository.findByBookIdAndUserId(book.getId(), user.getId());
        if (existentReview.isPresent()) {
            throw new DuplicateObjectException("Review for book " + book.getId() + " and user " + user.getId());
        }

        var review = new Review();
        review.setBook(book);
        review.setUser(user);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        var timestamp = LocalDateTime.now(clock);
        review.setCreatedAt(timestamp);
        review.setUpdatedAt(timestamp);

        return reviewRepository.save(review);
    }

    public Review updateReview(Long id, ReviewRequest request, String userEmail) throws EntityNotFoundException,
            InvalidUserAccessException {
        var review = getReview(id);
        var user = userService.getUserByEmail(userEmail);
        if (!review.getUser().getId().equals(user.getId())) {
            throw new InvalidUserAccessException("User " + user.getEmail() + " cannot update review " + review.getId());
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setUpdatedAt(LocalDateTime.now(clock));

        return reviewRepository.save(review);
    }

    public void deleteReview(Long id, String userEmail) throws EntityNotFoundException, InvalidUserAccessException {
        var review = getReview(id);
        var user = userService.getUserByEmail(userEmail);
        if (!review.getUser().getId().equals(user.getId())) {
            throw new InvalidUserAccessException("User " + user.getEmail() + " cannot delete review " + review.getId());
        }

        reviewRepository.delete(review);
    }
}
