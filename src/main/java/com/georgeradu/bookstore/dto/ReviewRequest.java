package com.georgeradu.bookstore.dto;

import jakarta.validation.constraints.*;

import java.util.Objects;

public class ReviewRequest {
    @NotNull(message = "Book ID must not be null")
    private Long bookId;
    @NotNull(message = "Rating must not be null")
    @Min(value = 1, message = "Rating must be greater than or equal to 1")
    @Max(value = 5, message = "Rating must be less than or equal to 5")
    private int rating;
    @NotBlank(message = "Comment must not be blank")
    @Size(min = 1, max = 2000, message = "Comment must be between 1 and 2000 characters")
    private String comment;

    public ReviewRequest() {
    }

    public ReviewRequest(Long bookId, int rating, String comment) {
        this.bookId = bookId;
        this.rating = rating;
        this.comment = comment;
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
        ReviewRequest that = (ReviewRequest) o;
        return rating == that.rating && Objects.equals(bookId, that.bookId) && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, rating, comment);
    }

    @Override
    public String toString() {
        return "ReviewRequest{" + "bookId=" + bookId + ", rating=" + rating + ", comment='" + comment + '\'' + '}';
    }
}
