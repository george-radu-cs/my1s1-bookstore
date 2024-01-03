package com.georgeradu.bookstore.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

public class BookRequest {
    @NotBlank(message = "Title is mandatory")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters long")
    private String title;
    @NotBlank(message = "Author is mandatory")
    @Size(min = 3, max = 255, message = "Author must be between 3 and 255 characters long")
    private String author;
    @NotBlank(message = "Description is mandatory")
    @Size(min = 3, max = 1000, message = "Description must be between 3 and 1000 characters long")
    private String description;
    @Min(value = 0, message = "Price must be greater than 0")
    @Max(value = 100000, message = "Price must be less than 100000")
    private double price;
    @NotBlank(message = "Image URL is mandatory")
    @Size(min = 3, max = 255, message = "Image URL must be between 3 and 255 characters long")
    private String imageUrl;
    @Min(value = 0, message = "Quantity must be greater than 0")
    @Max(value = 100000, message = "Quantity must be less than 100000")
    private int quantity;
    @NotBlank(message = "Language is mandatory")
    @Size(min = 3, max = 255, message = "Language must be between 3 and 255 characters long")
    private String language;
    @NotBlank(message = "Publisher is mandatory")
    @Size(min = 3, max = 255, message = "Publisher must be between 3 and 255 characters long")
    private String publisher;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime publicationDate;
    @NotBlank(message = "ISBN10 is mandatory")
    @Size(min = 3, max = 255, message = "ISBN10 must be between 3 and 255 characters long")
    private String isbn10;
    @NotBlank(message = "ISBN13 is mandatory")
    @Size(min = 3, max = 255, message = "ISBN13 must be between 3 and 255 characters long")
    private String isbn13;
    @NotBlank(message = "Dimensions is mandatory")
    @Size(min = 3, max = 255, message = "Dimensions must be between 3 and 255 characters long")
    private String dimensions;
    @NotNull(message = "Category ID is mandatory")
    private Long bookCategoryId;

    public BookRequest() {
    }

    public BookRequest(
            String title, String author, String description, double price, String imageUrl, int quantity,
            String language, String publisher, LocalDateTime publicationDate, String isbn10, String isbn13,
            String dimensions, Long bookCategoryId
    ) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.language = language;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.isbn10 = isbn10;
        this.isbn13 = isbn13;
        this.dimensions = dimensions;
        this.bookCategoryId = bookCategoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public Long getBookCategoryId() {
        return bookCategoryId;
    }

    public void setBookCategoryId(Long bookCategoryId) {
        this.bookCategoryId = bookCategoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookRequest that = (BookRequest) o;
        return Double.compare(price, that.price) == 0 && quantity == that.quantity &&
               Objects.equals(title, that.title) && Objects.equals(author, that.author) &&
               Objects.equals(description, that.description) && Objects.equals(imageUrl, that.imageUrl) &&
               Objects.equals(language, that.language) && Objects.equals(publisher, that.publisher) &&
               Objects.equals(publicationDate, that.publicationDate) && Objects.equals(isbn10, that.isbn10) &&
               Objects.equals(isbn13, that.isbn13) && Objects.equals(dimensions, that.dimensions) &&
               Objects.equals(bookCategoryId, that.bookCategoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, description, price, imageUrl, quantity, language, publisher, publicationDate,
                isbn10, isbn13, dimensions, bookCategoryId);
    }

    @Override
    public String toString() {
        return "BookRequest{" + "title='" + title + '\'' + ", author='" + author + '\'' + ", description='" +
               description + '\'' + ", price=" + price + ", imageUrl='" + imageUrl + '\'' + ", quantity=" + quantity +
               ", language='" + language + '\'' + ", publisher='" + publisher + '\'' + ", publicationDate=" +
               publicationDate + ", isbn10='" + isbn10 + '\'' + ", isbn13='" + isbn13 + '\'' + ", dimensions='" +
               dimensions + '\'' + ", categoryId='" + bookCategoryId + '\'' + '}';
    }
}
