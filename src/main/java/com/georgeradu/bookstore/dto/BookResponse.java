package com.georgeradu.bookstore.dto;

import com.georgeradu.bookstore.model.Book;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String description;
    private double price;
    private String imageUrl;
    private int quantity;
    private String language;
    private String publisher;
    private LocalDateTime publicationDate;
    private String isbn10;
    private String isbn13;
    private String dimensions;
    private Long bookCategoryId;

    public BookResponse() {
    }

    public BookResponse(
            Long id, String title, String author, String description, double price, String imageUrl, int quantity,
            String language, String publisher, LocalDateTime publicationDate, String isbn10, String isbn13,
            String dimensions, Long bookCategoryId
    ) {
        this.id = id;
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

    public BookResponse(Book response) {
        this.id = response.getId();
        this.title = response.getTitle();
        this.author = response.getAuthor();
        this.description = response.getDescription();
        this.price = response.getPrice();
        this.imageUrl = response.getImageUrl();
        this.quantity = response.getQuantity();
        this.language = response.getLanguage();
        this.publisher = response.getPublisher();
        this.publicationDate = response.getPublicationDate();
        this.isbn10 = response.getIsbn10();
        this.isbn13 = response.getIsbn13();
        this.dimensions = response.getDimensions();
        this.bookCategoryId = response.getCategory().getId();
    }

    public static List<BookResponse> fromList(List<Book> response) {
        return response.stream().map(BookResponse::new).toList();
    }

    public static Page<BookResponse> fromPage(Page<Book> response) {
        return response.map(BookResponse::new);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        BookResponse that = (BookResponse) o;
        return Double.compare(price, that.price) == 0 && quantity == that.quantity && Objects.equals(id, that.id) &&
               Objects.equals(title, that.title) && Objects.equals(author, that.author) &&
               Objects.equals(description, that.description) && Objects.equals(imageUrl, that.imageUrl) &&
               Objects.equals(language, that.language) && Objects.equals(publisher, that.publisher) &&
               Objects.equals(publicationDate, that.publicationDate) && Objects.equals(isbn10, that.isbn10) &&
               Objects.equals(isbn13, that.isbn13) && Objects.equals(dimensions, that.dimensions) &&
               Objects.equals(bookCategoryId, that.bookCategoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, description, price, imageUrl, quantity, language, publisher,
                publicationDate, isbn10, isbn13, dimensions, bookCategoryId);
    }

    @Override
    public String toString() {
        return "BookResponse{" + "id=" + id + ", title='" + title + '\'' + ", author='" + author + '\'' +
               ", description='" + description + '\'' + ", price=" + price + ", imageUrl='" + imageUrl + '\'' +
               ", quantity=" + quantity + ", language='" + language + '\'' + ", publisher='" + publisher + '\'' +
               ", publicationDate=" + publicationDate + ", isbn10='" + isbn10 + '\'' + ", isbn13='" + isbn13 + '\'' +
               ", dimensions='" + dimensions + '\'' + ", bookCategoryId=" + bookCategoryId + '}';
    }
}
