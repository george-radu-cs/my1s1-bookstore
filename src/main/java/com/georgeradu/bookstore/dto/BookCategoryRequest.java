package com.georgeradu.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class BookCategoryRequest {
    @NotBlank(message = "Category name cannot be empty")
    @Size(min = 3, max = 255, message = "Category name must be between 3 and 255 characters")
    private String name;
    @NotEmpty(message = "Category description cannot be empty")
    @Size(min = 3, max = 255, message = "Category description must be between 3 and 255 characters")
    private String description;

    public BookCategoryRequest() {
    }

    public BookCategoryRequest(String name, String description) {
        this.name = name;
        this.description = description;
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
        BookCategoryRequest that = (BookCategoryRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return "BookCategoryRequest{" + "name='" + name + '\'' + ", description='" + description + '\'' + '}';
    }
}
