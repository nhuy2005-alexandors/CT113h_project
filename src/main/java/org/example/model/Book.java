package org.example.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "Books")
public class Book {

    @Id
    private String id;

    @Indexed(unique = true)
    private String isbn;

    private String title;
    private Integer publicationYear;
    private String genre;

    // Các Document nhúng
    private Author author;
    private List<Review> reviews;

    // 1. Constructor mặc định (Cần thiết cho Spring Data và Mapping)
    public Book() {}

    // 2. Constructor đầy đủ tham số (Không bao gồm 'id' vì nó do Mongo tạo)
    public Book(String isbn, String title, Integer publicationYear, String genre, Author author, List<Review> reviews) {
        this.isbn = isbn;
        this.title = title;
        this.publicationYear = publicationYear;
        this.genre = genre;
        this.author = author;
        this.reviews = reviews;
    }

    // 3. GETTERS và SETTERS

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    // --- PHƯƠNG THỨC HỖ TRỢ CHO JAVAFX TABLEVIEW ---
    // Cần thiết để hiển thị tên tác giả trực tiếp trong TableColumn
    public String getAuthorName() {
        return (author != null) ? author.getName() : "";
    }
}