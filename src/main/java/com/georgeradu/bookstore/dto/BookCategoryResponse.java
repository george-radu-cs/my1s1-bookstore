package com.georgeradu.bookstore.dto;

import com.georgeradu.bookstore.model.BookCategory;

import java.util.List;
import java.util.Objects;

public class BookCategoryResponse {
    private Long id;
    private String name;
    private String description;

    public BookCategoryResponse() {
    }

    public BookCategoryResponse(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public BookCategoryResponse(BookCategory bookCategory) {
        this.id = bookCategory.getId();
        this.name = bookCategory.getName();
        this.description = bookCategory.getDescription();
    }

    public static List<BookCategoryResponse> fromList(List<BookCategory> response) {
        return response.stream().map(BookCategoryResponse::new).toList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookCategoryResponse that = (BookCategoryResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) &&
               Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return "BookCategoryResponse{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description +
               '\'' + '}';
    }
}
