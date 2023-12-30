package com.georgeradu.bookstore.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @Column(name = "publication_date", nullable = false)
    private String publicationDate;

    @Column(name = "isbn10", nullable = false)
    private String isbn10;

    @Column(name = "isbn13", nullable = false)
    private String isbn13;

    @Column(name = "dimensions", nullable = false)
    private String dimensions;

    @Column(name = "average_rating", nullable = false)
    private double averageRating;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private BookCategory category;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    public Book() {
    }

    public Book(Long id) {
        this.id = id;
    }

    public Book(
            Long id, String title, String author, String description, double price, String imageUrl, int quantity,
            String language, String publisher, String publicationDate, String isbn10, String isbn13, String dimensions,
            double averageRating, BookCategory category, Date createdAt, Date updatedAt, Date deletedAt
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
        this.averageRating = averageRating;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
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

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
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

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Double.compare(price, book.price) == 0 && quantity == book.quantity &&
               Double.compare(averageRating, book.averageRating) == 0 && Objects.equals(id, book.id) &&
               Objects.equals(title, book.title) && Objects.equals(author, book.author) &&
               Objects.equals(description, book.description) && Objects.equals(imageUrl, book.imageUrl) &&
               Objects.equals(language, book.language) && Objects.equals(publisher, book.publisher) &&
               Objects.equals(publicationDate, book.publicationDate) && Objects.equals(isbn10, book.isbn10) &&
               Objects.equals(isbn13, book.isbn13) && Objects.equals(dimensions, book.dimensions) &&
               Objects.equals(category, book.category) && Objects.equals(createdAt, book.createdAt) &&
               Objects.equals(updatedAt, book.updatedAt) && Objects.equals(deletedAt, book.deletedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, description, price, imageUrl, quantity, language, publisher,
                publicationDate, isbn10, isbn13, dimensions, averageRating, category, createdAt, updatedAt, deletedAt);
    }

    @Override
    public String toString() {
        return "Book{" + "id=" + id + ", title='" + title + '\'' + ", author='" + author + '\'' + ", description='" +
               description + '\'' + ", price=" + price + ", imageUrl='" + imageUrl + '\'' + ", quantity=" + quantity +
               ", language='" + language + '\'' + ", publisher='" + publisher + '\'' + ", publicationDate='" +
               publicationDate + '\'' + ", isbn10='" + isbn10 + '\'' + ", isbn13='" + isbn13 + '\'' + ", dimensions='" +
               dimensions + '\'' + ", averageRating=" + averageRating + ", category=" + category + ", createdAt=" +
               createdAt + ", updatedAt=" + updatedAt + ", deletedAt=" + deletedAt + '}';
    }
}
