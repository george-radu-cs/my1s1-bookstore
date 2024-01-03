package com.georgeradu.bookstore.service;

import com.georgeradu.bookstore.dto.BookCategoryRequest;
import com.georgeradu.bookstore.exception.DuplicateObjectException;
import com.georgeradu.bookstore.exception.EntityNotFoundException;
import com.georgeradu.bookstore.model.BookCategory;
import com.georgeradu.bookstore.repository.BookCategoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookCategoryService {
    private final BookCategoryRepository bookCategoryRepository;

    public BookCategoryService(BookCategoryRepository bookCategoryRepository) {
        this.bookCategoryRepository = bookCategoryRepository;
    }

    public BookCategory getBookCategory(Long id) throws EntityNotFoundException {
        return bookCategoryRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book category with id " + id));
    }

    public List<BookCategory> getBookCategories() {
        return bookCategoryRepository.findAll();
    }

    public BookCategory createBookCategory(BookCategoryRequest request) throws DuplicateObjectException {
        var existingBookCategory = bookCategoryRepository.findByName(request.getName());
        if (existingBookCategory.isPresent()) {
            throw new DuplicateObjectException("Book category with name " + request.getName());
        }

        var bookCategory = new BookCategory();
        bookCategory.setName(request.getName());
        bookCategory.setDescription(request.getDescription());
        var timestamp = LocalDateTime.now();
        bookCategory.setCreatedAt(timestamp);
        bookCategory.setUpdatedAt(timestamp);

        return bookCategoryRepository.save(bookCategory);
    }

    public BookCategory updateBookCategory(Long id, BookCategoryRequest request) throws EntityNotFoundException,
            DuplicateObjectException {
        var bookCategory = getBookCategory(id);
        var existingBookCategory = bookCategoryRepository.findByName(request.getName());
        if (existingBookCategory.isPresent() && !existingBookCategory.get().getId().equals(id)) {
            throw new DuplicateObjectException("Book category with name " + request.getName());
        }
        bookCategory.setName(request.getName());
        bookCategory.setDescription(request.getDescription());
        bookCategory.setUpdatedAt(LocalDateTime.now());

        return bookCategoryRepository.save(bookCategory);
    }

    public void deleteBookCategory(Long id) throws EntityNotFoundException {
        var bookCategory = getBookCategory(id);
        bookCategoryRepository.delete(bookCategory);
    }
}
