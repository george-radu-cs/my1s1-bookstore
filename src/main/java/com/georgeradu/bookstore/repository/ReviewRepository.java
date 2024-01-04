package com.georgeradu.bookstore.repository;

import com.georgeradu.bookstore.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByBookId(Long bookId);

    List<Review> findAllByUserId(Long userId);

    Optional<Review> findByBookIdAndUserId(Long bookId, Long userId);
}
