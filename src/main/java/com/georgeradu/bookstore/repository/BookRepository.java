package com.georgeradu.bookstore.repository;

import com.georgeradu.bookstore.model.Book;
import com.georgeradu.bookstore.model.BookCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAllByCategory(BookCategory bookCategory, PageRequest pageRequest);

    List<Book> findAllByTitleIsInOrAuthorIsIn(List<String> orElse, List<String> orElse1);
}
