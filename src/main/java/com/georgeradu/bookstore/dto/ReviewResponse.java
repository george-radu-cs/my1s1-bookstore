package com.georgeradu.bookstore.dto;

import com.georgeradu.bookstore.model.Review;

import java.util.List;
import java.util.Objects;

public class ReviewResponse {
    private Long id;
    private Long userId;
    private Long bookId;
    private int rating;
    private String comment;

    public ReviewResponse() {
    }

    public ReviewResponse(Long id, Long userId, Long bookId, int rating, String comment) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.rating = rating;
        this.comment = comment;
    }

    public ReviewResponse(Review response) {
        this.id = response.getId();
        this.userId = response.getUser().getId();
        this.bookId = response.getBook().getId();
        this.rating = response.getRating();
        this.comment = response.getComment();
    }

    public static List<ReviewResponse> fromList(List<Review> reviews) {
        return reviews.stream().map(ReviewResponse::new).toList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewResponse that = (ReviewResponse) o;
        return rating == that.rating && Objects.equals(id, that.id) && Objects.equals(userId, that.userId) &&
               Objects.equals(bookId, that.bookId) && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, bookId, rating, comment);
    }

    @Override
    public String toString() {
        return "ReviewResponse{" + "id=" + id + ", userId=" + userId + ", bookId=" + bookId + ", rating=" + rating +
               ", comment='" + comment + '\'' + '}';
    }
}
